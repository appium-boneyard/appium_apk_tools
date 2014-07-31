#### appium_apk_tools

Extracts `strings.xml` from an APK and converts to JSON.

- `java -jar appium_apk_tools.jar stringsFromApk input.apk outputFolder` creates `strings.json` in `outputFolder`
- `java -jar appium_apk_tools.jar stringsFromApk /tmp/apk/apk2.apk /tmp/apk it` will convert `res/values-it/strings.xml` (Italian strings)
- `java -jar appium_apk_tools.jar stringsFromApk /tmp/apk` will convert `res/values/strings.xml` (default strings)

Prints Launch actvity by parsing AndroidManifest.xml from apk

- `java -jar appium_apk_tools.jar printLaunchActivity input.apk outputFolder` prints launch activity after parsing AndroidManifest.xml from apk.

#### Third party jars

- [apktool-cli-1.5.3-SNAPSHOT.jar](https://github.com/iBotPeaches/Apktool)
- [Jackson Core](https://github.com/FasterXML/jackson-core)

#### Building

`apktool-cli-1.5.3-SNAPSHOT.jar` must be built with JDK 6 for `strings_from_apk.jar` to work on Java 6.

```
# Set JAVA_HOME to JDK 6 (it works with JDK 7 however JDK 6 can't run JDK 7 compiled jars)
$ export JAVA_HOME=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/
$ git clone --recursive git@github.com:iBotPeaches/Apktool.git
$ cd Apktool/
$ ./gradlew build fatJar
# jar is in Apktool/brut.apktool/apktool-cli/build/libs/apktool-cli-1.5.3-SNAPSHOT.jar
```
