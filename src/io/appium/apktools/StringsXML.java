package io.appium.apktools;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;

import brut.androlib.res.AndrolibResources;
import brut.androlib.res.data.ResPackage;
import brut.androlib.res.data.ResResource;
import brut.androlib.res.data.ResTable;
import brut.androlib.res.data.ResValuesFile;
import brut.androlib.res.data.value.ResScalarValue;
import brut.androlib.res.util.ExtFile;
import brut.androlib.res.util.ExtMXSerializer;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonGenerator.Feature;

public class StringsXML {
  final static boolean           debug         = false;
  final static AndrolibResources res           = new AndrolibResources();
  final static ExtMXSerializer   xmlSerializer = AbstractAndrolibResources
                                                   .getResXmlSerializer();
  final static JsonFactory       json          = new JsonFactory();

  public static void p(final Object msg) {
    if (debug) {
      System.out.println(msg.toString());
    }
  }

  public static void toJSON(final ResValuesFile input,
      final File outputDirectory) throws Exception {
    String[] paths = input.getPath().split("/"); // always "/" even on Windows
    final String outName = paths[paths.length - 1].replaceFirst("\\.xml$",
        ".json");
    final File outFile = new File(outputDirectory, outName);
    p("Saving to: " + outFile);
    JsonGenerator generator = json.createGenerator(
        new FileOutputStream(outFile), JsonEncoding.UTF8);

    // Ensure output stream is auto closed when generator.close() is called.
    generator.configure(Feature.AUTO_CLOSE_TARGET, true);
    generator.configure(Feature.AUTO_CLOSE_JSON_CONTENT, true);
    generator.configure(Feature.FLUSH_PASSED_TO_STREAM, true);
    generator.configure(Feature.QUOTE_NON_NUMERIC_NUMBERS, true);
    generator.configure(Feature.WRITE_NUMBERS_AS_STRINGS, true);
    generator.configure(Feature.QUOTE_FIELD_NAMES, true);
    // generator.configure(Feature.ESCAPE_NON_ASCII, true); // don't escape non
    // ascii
    generator.useDefaultPrettyPrinter();

    // ResStringValue extends ResScalarValue which has field mRawValue
    final Field valueField = ResScalarValue.class.getDeclaredField("mRawValue");
    valueField.setAccessible(true);

    generator.writeStartObject();
    for (ResResource resource : input.listResources()) {
      if (input.isSynthesized(resource)) {
        continue;
      }

      final String name = resource.getResSpec().getName();
      // Get the value field from the ResStringValue object.
      final String value = (String) valueField.get(resource.getValue());
      generator.writeStringField(name, value);
    }
    generator.writeEndObject();
    generator.flush();
    generator.close();
  }

  public static void run(final File input, final File outputDirectory,
      String localization) throws Exception {
    if (localization == null) {
      localization = "values";
    }
    final ExtFile apkFile = new ExtFile(input);
    ResTable table = res.getResTable(apkFile, true);
    ResValuesFile stringsXML = null;
    for (ResPackage pkg : table.listMainPackages()) {
      p(pkg);
      for (ResValuesFile values : pkg.listValuesFiles()) {
        // strings.xml is not case sensitive. xamarin will call it Strings.xml
        final String path = values.getPath().toLowerCase();
        final String targetPath = (localization + "/strings.xml").toLowerCase();
        p(path);
        if (path.endsWith(targetPath)) {
          stringsXML = values;
          break;
        }
      }
      if (stringsXML != null) {
        break;
      }
    }

    if (stringsXML == null) {
      e("Could not find the strings.xml file for localization: " + localization);
    }

    toJSON(stringsXML, outputDirectory);
    p("complete");
  }

  public static void e(final String msg) throws Exception {
    throw new Exception(msg);
  }

  public static void silenceLogger() throws Exception {
    Field logger = AndrolibResources.class.getDeclaredField("LOGGER");
    logger.setAccessible(true);

    // remove final
    Field mods = logger.getClass().getDeclaredField("modifiers");
    mods.setAccessible(true);
    mods.setInt(logger, logger.getModifiers() & ~Modifier.FINAL);

    Logger newLogger = Logger.getAnonymousLogger();
    newLogger.setLevel(Level.OFF);
    // set logger to anon
    logger.set(res, newLogger);
  }



}