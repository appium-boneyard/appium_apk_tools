package strings;

import java.io.File;

import brut.androlib.res.AndrolibResources;
import brut.androlib.res.data.ResPackage;
import brut.androlib.res.data.ResTable;
import brut.androlib.res.data.ResValuesFile;
import brut.androlib.res.util.ExtFile;
import brut.androlib.res.util.ExtMXSerializer;
import brut.directory.FileDirectory;

public class StringsXML {
  final static boolean           debug         = true;
  final static AndrolibResources res           = new AndrolibResources();
  final static ExtMXSerializer   xmlSerializer = AbstractAndrolibResources
                                                   .getResXmlSerializer();

  public static void p(final Object msg) {
    if (debug) {
      System.out.println(msg.toString());
    }
  }

  public static void run(final File input, final File outputDirectory) throws Exception {
    final ExtFile apkFile = new ExtFile(input);
    ResTable table = res.getResTable(apkFile, true);
    ResValuesFile stringsXML = null;
    for (ResPackage pkg : table.listMainPackages()) {
      p(pkg);
      for (ResValuesFile values : pkg.listValuesFiles()) {
        p(values.getPath());
        if (values.getPath().endsWith("/strings.xml")) {
          stringsXML = values;
          break;
        }
      }
      if (stringsXML != null) {
        break;
      }
    }

    AbstractAndrolibResources.generateValuesFile(stringsXML, new FileDirectory(outputDirectory), xmlSerializer);
    p("complete");
  }

  public static void main(String[] args) throws Exception {
    final File input = new File("/tmp/apk/apk.apk");
    final File outputDirectory = new File("/tmp/apk/");
    run(input, outputDirectory);
  }
}