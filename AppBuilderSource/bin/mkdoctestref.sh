
test_summary_base=test/output
lang_def_base=test/lang/def

rm -Rf $test_summary_base-Ref
rm -Rf $lang_def_base-Ref
mkdir $test_summary_base-Ref
mkdir $lang_def_base-Ref

bin/testSummary.sh
bin/lang -def

cp -R $test_summary_base/* $test_summary_base-Ref/
cp -R $lang_def_base/* $lang_def_base-Ref/

