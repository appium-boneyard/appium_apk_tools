/**
 *  Copyright 2011 Ryszard Wiśniewski <brut.alll@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.appium.apktools;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import brut.androlib.AndrolibException;
import brut.androlib.res.data.ResResource;
import brut.androlib.res.data.ResValuesFile;
import brut.androlib.res.util.ExtMXSerializer;
import brut.androlib.res.util.ExtXmlSerializer;
import brut.androlib.res.xml.ResValuesXmlSerializable;
import brut.directory.Directory;
import brut.directory.DirectoryException;

// Code from /Apktool/brut.apktool/apktool-lib/src/main/java/brut/androlib/res/AndrolibResources.java
abstract class AbstractAndrolibResources {
  /** Converts values file to XML **/
  public static void generateValuesFile(ResValuesFile valuesFile, Directory out,
      ExtXmlSerializer serial) throws AndrolibException {
    try {
      // values/strings.xml => strings.xml
      String[] paths = valuesFile.getPath().split(File.separator);
      final String outPath = paths[paths.length - 1];
      OutputStream outStream = out.getFileOutput(outPath);
      serial.setOutput((outStream), null);
      serial.startDocument(null, null);
      serial.startTag(null, "resources");

      for (ResResource res : valuesFile.listResources()) {
        if (valuesFile.isSynthesized(res)) {
          continue;
        }
        ((ResValuesXmlSerializable) res.getValue())
            .serializeToResValuesXml(serial, res);
      }

      serial.endTag(null, "resources");
      serial.newLine();
      serial.endDocument();
      serial.flush();
      outStream.close();
    } catch (IOException ex) {
      throw new AndrolibException("Could not generate: "
          + valuesFile.getPath(), ex);
    } catch (DirectoryException ex) {
      throw new AndrolibException("Could not generate: "
          + valuesFile.getPath(), ex);
    }
  }

  public static ExtMXSerializer getResXmlSerializer() {
    ExtMXSerializer serial = new ExtMXSerializer();
    serial.setProperty(ExtXmlSerializer.PROPERTY_SERIALIZER_INDENTATION,
        "    ");
    serial.setProperty(ExtXmlSerializer.PROPERTY_SERIALIZER_LINE_SEPARATOR,
        System.getProperty("line.separator"));
    serial.setProperty(ExtXmlSerializer.PROPERTY_DEFAULT_ENCODING, "utf-8");
    serial.setDisabledAttrEscape(true);
    return serial;
  }
}