#!/bin/sh

OLD=Cell
NEW=Item
                          
for file in $(grep -il $OLD test/*.madl)
do
    sed -e "s/$OLD/$NEW/g" $file > /tmp/tempfile.tmp
    mv /tmp/tempfile.tmp $file
done