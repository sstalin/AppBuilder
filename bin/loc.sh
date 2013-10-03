
APPS="app01 app02 app03 app04 app04a app05 app05a app06 app07 app08"
DIR1="gen/Platform.Android"
DIR2="gen/Platform.iOS"

wc test/*.madl

for app in $APPS 
do
    wc $DIR1/$app/*.xml $DIR1/$app/src/com/apps/*.* $DIR1/$app/res/layout/*.*
done

for app in $APPS 
do
    wc $DIR2/$app/*/*.[hm]
done

