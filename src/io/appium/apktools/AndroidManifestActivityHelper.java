package io.appium.apktools;

import brut.androlib.res.AndrolibResources;
import brut.androlib.res.data.ResTable;
import brut.androlib.res.util.ExtFile;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;


public class AndroidManifestActivityHelper {

    final static AndrolibResources res = new AndrolibResources();

    public static String getLaunchableActivityName(final String xmlFilePath) throws Exception {
     SAXParserFactory factory = SAXParserFactory.newInstance();
     SAXParser saxParser = factory.newSAXParser();
     final CustomHandler handler = new CustomHandler();
     saxParser.parse(xmlFilePath, handler);
     return handler.getLauncherActivity();
    }

    public static void decodeManifestXML(final File input, final File outputDirectory) throws Exception {
     final ExtFile apkFile = new ExtFile(input);
     ResTable table = res.getResTable(apkFile, true);
     res.decodeManifest(table, apkFile, outputDirectory);
    }
}
