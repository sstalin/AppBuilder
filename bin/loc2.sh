
APPS="app01 app01a app01b app01c app01d app02 app02a app03 app04 app04a app04b app04c app04d app05 app05a app05b app05c app05d app06 app06a app07 app07a app07b app08 app09 app09a app10 app10a app11 app11a app11b app11c app11d app11e app12 UserRole01 UserRole02"
DIR1="gen/Platform.Android"
DIR2="gen/Platform.iOS"

wc test/*.madl

i=0 
for app in $APPS 
do
    echo "=== APP: "${app}" ==============="
    wc test/${app}.madl
    wc $DIR1/$app/*.xml $DIR1/$app/src/com/apps/*.* $DIR1/$app/res/layout/*.*
    wc $DIR2/$app/*/*.[hm]
    echo "==================================="
    ((i++))
done
echo "Total: " ${i}

