#!/bin/bash

rm CNCVideo-*.apk
cd app/src/main/assets
echo "APK_MODEL = $1" > app.cfg
echo "APK_SVNREVISION = $2" >> app.cfg
cd -
echo "begin gradlew build"
./gradlew clean build
cd app/build/outputs/apk
mv app-channel-release.apk app-channel-release.zip
zip -d app-channel-release.zip lib/arm64*/*.so
zip -d app-channel-release.zip lib/x86*/*.so
mv app-channel-release.zip app-channel-release.apk
cd -
cp app/build/outputs/apk/app-channel-release.apk CNCVideo-$1-r$2-release.apk

