package io.appium.apktools;


import java.io.File;
import static io.appium.apktools.StringsXML.e;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 3 || args.length > 4) {
            e("Usage: <option> input.apk outputFolder [localization]");
        }
        final String option = args[0];
        final File input = new File(args[1]);
        if (!input.exists() || !input.isFile() || !input.canRead()) {
            e("Input is not an existing readable file.");
        }
        final File outputDirectory = new File(args[2]);
        // Attempt to make directory if it doesn't already exist.
        outputDirectory.mkdirs();
        if (!outputDirectory.exists() || !outputDirectory.isDirectory()
                || !outputDirectory.canRead()) {
            e("Output is not an existing readable directory.");
        }
        StringsXML.silenceLogger();
        if (option.equalsIgnoreCase("stringsFromApk")) {
            String localization = null;
            if (args.length == 4) {
                localization = "values-" + args[3];
            }
            StringsXML.run(input, outputDirectory, localization);
        } else if (option.equalsIgnoreCase("printLaunchActivity")) {
            AndroidManifestActivityHelper.decodeManifestXML(input, outputDirectory);
            String manifestPath = outputDirectory + File.separator + "AndroidManifest.xml";
            System.out.println("Launch activity parsed:" + AndroidManifestActivityHelper.getLaunchableActivityName(manifestPath));
        } else {
            e("Not a valid option.");
        }
    }

}
