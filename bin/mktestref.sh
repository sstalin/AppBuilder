
#files="app01 app01a app01aa app01ab app01ac app01ad app01b app01c app01ca app01cb app01cc app01d app01e app01f app01g app01ga app01h app01i app01j app02 app02a app02c app02b app02ba app02bb app02bc app02c app02d app02e app02ea app02eb app02ec app02ed app02ee app02ef app02f app02fa app02g app02ga app02gb app03 app04 app04a app04b app04c app04d app04e app04ea app04eb app04f app05 app05a app05b app05ba app05c app05d app05e app05f app05g app05h app05i app05j app06 app06a app06aa app06ab app06ac app06ad app06ae app06c app06ca app06cb app06cc app07 app07a app07b app08 app09 app09a app10 app10a app11 app11a app11b app11c app11d app11e app12 app12a app12aa app12ab app12b app12ba app12bb app12c app12d app12e app12ea app13 app13a app13b app13ba app13c app13ca app13d app13e app13f app13g app13h app13i app13j app13k app13l app21 app21a app21b app21c app21d app21e app21f app23 app23a app25 app25a app26 app26a app26b app32 app32a app32b app32c app32d app32e app40 app40a app40b app40d app40e app40f Hello01 Hello01a Hello01b Hello02 Hello02a Hello03"

files="app01 app01a app01aa app01ab app01ac app01ad app01b app01c app01ca app01cb app01cc app01cd app01d app01e app01f app01g app01ga app01h app01i app01j app02 app02a app02aa app02ab app02b app02ba app02bb app02bc app02c app02d app02e app02ea app02eb app02ec app02ed app02ee app02ef app02eg app02f app02fa app02g app02ga app02gb app03 app04 app04a app04b app04c app04d app04e app04ea app04eb app04ec app04f app04g app04ga app05 app05a app05b app05ba app05c app05d app05e app05f app05g app05h app05i app05j app06 app06a app06aa app06ab app06ac app06ad app06ae app06c app06ca app06cb app06cc app07 app07a app07b app08 app09 app09a app10 app10a app11 app11a app11b app11c app11d app11e app12 app12a app12aa app12ab app12b app12ba app12bb app12c app12d app12e app12ea app12f app13 app13a app13b app13ba app13c app13ca app13d app13e app13f app13g app13h app13i app13j app13k app13l app21 app21a app21b app21c app21d app21e app21f app23 app23a app25 app25a app25aa app25b app25c app26 app26a app26b app26c app26d app26da app26db app27 app27b app28 app28b app29 app29a app30 app32 app32a app32b app32c app32d app32e app40 app40a app40b app40d app40e app40f Hello01 Hello01a Hello01b Hello02 Hello02a Hello03"


iosfiles="ios01a ios01b ios02a ios02b ios11a ios11b ios25"

androidfiles="android01a android01b android02a android02b android02c android05a android05b android05c android06a android11a"

mkdir gen/Platform.iOS-Ref
mkdir gen/Platform.Android-Ref
for f in $files
do
    #mkdir gen/Platform.iOS-Ref/$f
    #mkdir gen/Platform.Android-Ref/$f
    bin/appbuilder -nodate test/$f.madl test/org-ios.properties
    cp -R gen/Platform.iOS/$f gen/Platform.iOS-Ref/

    bin/appbuilder -nodate test/$f.madl test/org-android.properties
    cp -R gen/Platform.Android/$f gen/Platform.Android-Ref/
done

for f in $iosfiles
do
    bin/appbuilder -nodate test/$f.madl test/org-ios.properties
    cp -R gen/Platform.iOS/$f gen/Platform.iOS-Ref/
done

for f in $androidfiles
do
    bin/appbuilder -nodate test/$f.madl test/org-android.properties
    cp -R gen/Platform.Android/$f gen/Platform.Android-Ref/
done