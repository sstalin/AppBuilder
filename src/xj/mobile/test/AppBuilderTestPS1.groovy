package xj.mobile.test

import org.junit.*
import static org.junit.Assert.*

import xj.mobile.Main

import static xj.mobile.test.AppBuilderTest.*

class AppBuilderTestPS1 extends AppBuilderTest {  

  def iOSFileMap = [
    'ios01a' : [ name: 'First App 6', 
				 views: [ 'Top', 'View1', 'View2', 'View3', 'View4' ] ], 
    'ios01b' : [ name: 'iOS App', 
				 views: [ 'Top' ] ], 
    'ios02a' : [ name: 'iOS App', 
				 views: [ 'Top' ] ], 
    'ios02b' : [ name: 'iOS App', 
				 views: [ 'Top' ] ], 
    'ios11a' : [ name: 'Picker 3', 
				 views: [ 'Top' ] ], 
    'ios11b' : [ name: 'Picker 6', 
				 views: [ 'Top' ] ], 
    'ios25'  : [ name: 'Tap Gesture', 
				 views: [ 'View1' ] ], 
  ]

  def androidFileMap = [
    'android01a' : [ name: 'Android App', 
					 views: [ 'AndroidApp' ], 
					 layouts: [ 'main' ] ], 
    'android01b' : [ name: 'Spinner App', 
					 views: [ 'SpinnerApp' ], 
					 layouts: [ 'main' ] ], 
    'android02a' : [ name: 'Android App', 
					 views: [ 'AndroidApp' ], 
					 layouts: [ 'main' ] ], 
    'android02b' : [ name: 'Spinner App', 
					 views: [ 'SpinnerApp' ], 
					 layouts: [ 'main' ] ], 
    'android02c' : [ name: 'Android App', 
					 views: [ 'AndroidApp' ], 
					 layouts: [ 'main' ] ], 
    'android05a' : [ name: 'List', 
					 views: [ 'List' ], 
					 layouts: [ 'main', 'list_header_expandablelistview1', 'list_item_expandablelistview1' ] ], 
    'android05b' : [ name: 'List3',
					 views: [ 'List3' ], 
					 layouts: [ 'main', 'list_header_list1', 'list_item_empty', 
								'list_item_list1', 'list_item_list2' ] ], 
    'android05c' : [ name: 'List4',
					 views: [ 'List2', 'List3', 'List4' ], 
					 layouts: [ 'main', 'list_header_list1', 'list_item_list3', 
								'list_item_list1', 'list_item_list2' ] ], 

    'android06a' : [ name: 'Detail List',
					 views: [ 'DetailList', 'List2', 'List3' ], 
					 layouts: [ 'main', 'list_header_list1', 'list_item_list1',
								'list_header_list2', 'list_item_list2',
								'list_item_list3'] ], 
    'android11a' : [ name: 'Picker5', 
					 views: [ 'Picker5' ],
					 layouts: [ 'main' ] ], 
  ]

  // iOS only tests

  @Test(timeout=80000L)
  public void test_ios01a() {
    test_iOS('ios01a')
  }

  @Test(timeout=80000L)
  public void test_ios01b() {
    test_iOS('ios01b')
  }

  @Test(timeout=80000L)
  public void test_ios02a() {
    test_iOS('ios02a')
  }

  @Test(timeout=80000L)
  public void test_ios02b() {
    test_iOS('ios02b')
  }

  @Test(timeout=80000L)
  public void test_ios11a() {
    test_iOS('ios11a')
  }

  @Test(timeout=80000L)
  public void test_ios11b() {
    test_iOS('ios11b')
  }

  @Test(timeout=80000L)
  public void test_ios25() {
    test_iOS('ios25')
  }

  // Android only tests 

  @Test(timeout=80000L)
  public void test_android01a() {
    test_Android('android01a')
  }

  @Test(timeout=80000L)
  public void test_android01b() {
    test_Android('android01b')
  }

  @Test(timeout=80000L)
  public void test_android02a() {
    test_Android('android02a')
  }

  @Test(timeout=80000L)
  public void test_android02b() {
    test_Android('android02b')
  }

  @Test(timeout=80000L)
  public void test_android02c() {
    test_Android('android02c')
  }

  @Test(timeout=80000L)
  public void test_android05a() {
    test_Android('android05a')
  }

  @Test(timeout=80000L)
  public void test_android05b() {
    test_Android('android05b')
  }

  @Test(timeout=80000L)
  public void test_android05c() {
    test_Android('android05c')
  }

  @Test(timeout=80000L)
  public void test_android06a() {
    test_Android('android06a')
  }

  @Test(timeout=80000L)
  public void test_android11a() {
    test_Android('android11a')
  }

}