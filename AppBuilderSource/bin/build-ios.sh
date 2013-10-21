
#files="app13 app13a app13b app13ba app13c app13ca app13l"
files="app05f app05h app05j"

cd gen/Platform.iOS

#for f in `ls | grep -v mod` 
for f in $files
do
	echo "Building project:"$f
	cd $f
	pwd
	if xcodebuild > /dev/null 2>&1; then
		echo "============ Build $f: Success"
	else 
		echo "============ Build $f: Fail"
	fi
	cd ..
done