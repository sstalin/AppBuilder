
#files="app13 app13a app13b app13ba app13c app13ca app13l"
#files="app05f app05h app05j"
files="android05b android05c"

cd gen/Platform.Android

for f in `ls | grep -v mod` 
#for f in $files
do
	echo "Building project:"$f
	cd $f
	pwd
	if ant debug > /dev/null 2>&1; then
		echo "============ Build $f: Success"
	else 
		echo "============ Build $f: Fail"
	fi
	cd ..
done