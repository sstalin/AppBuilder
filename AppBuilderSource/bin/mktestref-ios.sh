
#files="app01 app02 app03 app04 app04a app05 app05a app06 app07 app08"
#files="ios01a ios01b ios02a"
#files="ios02b"
#files="ios01b ios11a ios11b"
#files="ios11a"
#files="ios25"
files="ios02b"

mkdir gen/Platform.iOS-Ref
for f in $files
do
    bin/appbuilder -nodate test/$f.madl test/org-ios.properties
    cp -R gen/Platform.iOS/$f gen/Platform.iOS-Ref/
done