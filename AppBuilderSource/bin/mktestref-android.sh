#files="android11a"
#files="android05a android05b android05c android06a"
files="android05b android05c"

mkdir gen/Platform.Android-Ref
for f in $files
do
    bin/appbuilder -nodate test/$f.madl test/org-android.properties
    cp -R gen/Platform.Android/$f gen/Platform.Android-Ref/
done

