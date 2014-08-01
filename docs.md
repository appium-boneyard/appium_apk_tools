Reference docs that explain why we're manually parsing the manifest to find the launch activity.

Some apks don't have a launchable that shows up with `dump badging`

`aapt dump badging uber.apk | grep launchable`

Most of them do.

```
aapt dump badging api.apk | grep launch
launchable-activity: name='io.appium.android.apis.ApiDemos' label='' icon=''
```

dump xmltree works however the output is painful to parse.

```
aapt dump xmltree uber.apk AndroidManifest.xml

      E: activity-alias (line=78)
        A: android:name(0x01010003)="com.ubercab.UBUberActivity" (Raw: "com.ubercab.UBUberActivity")
        A: android:targetActivity(0x01010202)="com.ubercab.client.feature.launch.LauncherActivity" (Raw: "com.ubercab.client.feature.launch.LauncherActivity")
        E: intent-filter (line=79)
          E: action (line=80)
            A: android:name(0x01010003)="android.intent.action.MAIN" (Raw: "android.intent.action.MAIN")
          E: category (line=81)
            A: android:name(0x01010003)="android.intent.category.LAUNCHER" (Raw: "android.intent.category.LAUNCHER")
```


dumpsys package works however it must run on the android device after the apk has been installed.

```
adb shell dumpsys package com.ubercab

      android.intent.action.MAIN:
        22597e0f com.ubercab/.UBUberActivity filter c7ff67c
          Action: "android.intent.action.MAIN"
          Category: "android.intent.category.LAUNCHER"
```

apk tool generates xml which is easy to parse. This is the approach we're using in appium.

```
AndroidManifest.xml from apk tool


        <activity-alias android:name="com.ubercab.UBUberActivity" android:targetActivity="com.ubercab.client.feature.launch.LauncherActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity-alias>
```

The final invocation is:

`adb shell am start -n com.ubercab/com.ubercab.UBUberActivity`

The package is from:

```
 aapt dump badging uber.apk | grep package
package: name='com.ubercab' versionCode='30616' versionName='3.0.20'
```

and the activity is from `activity-alias android:name` with the intent filter of 
`android.intent.action.MAIN` and `android.intent.category.LAUNCHER`

`adb shell am start -n package/activity`
