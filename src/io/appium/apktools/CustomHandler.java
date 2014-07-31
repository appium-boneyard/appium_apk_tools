package io.appium.apktools;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class CustomHandler extends DefaultHandler {

    private String activityName = "";
    private String returnActivityName = "";
    private boolean isLauncherCategory = false;
    private boolean isMainAction = false;

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("activity") || qName.equalsIgnoreCase("activity-alias")) {
            activityName = attributes.getValue("android:name");
            isLauncherCategory = false;
            isMainAction = false;
        }

        if (qName.equalsIgnoreCase("category")) {
            if (attributes.getValue("android:name").equals("android.intent.category.LAUNCHER")) {
                isLauncherCategory = true;
            }
        }

        if (qName.equalsIgnoreCase("action")) {
            if (attributes.getValue("android:name").equals("android.intent.action.MAIN")) {
                isMainAction = true;
            }
        }
    }

    public void endElement(String uri, String localName,
                           String qName) throws SAXException {
        if (isLauncherCategory && isMainAction && (qName.equalsIgnoreCase("activity") || qName.equalsIgnoreCase("activity-alias"))) {
            returnActivityName = activityName;
        }
    }

    public String getLauncherActivity() {
        return returnActivityName;
    }
}