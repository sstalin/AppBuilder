package xj.mobile.test

import org.junit.*
import static org.junit.Assert.*

import xj.mobile.Main

import static xj.mobile.test.AppBuilderTest.*

class AppBuilderTest3 extends AppBuilderTest {  

  def iOSFileMap = [
    'app05'  : [ name: 'List', 
				 views: [ 'Top', 'ListView1' ] ], 
    'app05a' : [ name: 'List2', 
				 views: [ 'Top', 'ListView1' ] ], 
    'app05b' : [ name: 'List3', 
				 views: [ 'Top', 'List1', 'List2', 'Empty' ] ], 
    'app05ba' : [ name: 'List3a', 
				  views: [ 'Top', 'List1', 'List2', 'Empty' ] ], 
    'app05c' : [ name: 'List4', 
				 views: [ 'Top', 'List1', 'List2', 'List3' ] ], 
    'app05d' : [ name: 'List5', 
				 views: [ 'Top', 'ListView1' ] ], 
    'app05e' : [ name: 'List6', 
				 views: [ 'Top', 'ListView1' ] ], 
    'app05f' : [ name: 'List7', 
				 views: [ 'Top', 'ListView1' ] ], 
    'app05g' : [ name: 'List8', 
				 views: [ 'Top', 'List1' ] ], 
    'app05h' : [ name: 'List9', 
				 views: [ 'Top', 'List1' ] ], 
    'app05i' : [ name: 'List10', 
				 views: [ 'Top', 'List1' ] ], 
    'app05j' : [ name: 'List11', 
				 views: [ 'Top', 'List1' ] ], 

    'app06'  : [ name: 'Detail List', 
				 views: [ 'Top', 'List1', 'List2', 'List3' ] ], 
    'app06a' : [ name: 'Detail List 2', 
				 views: [ 'Top', 'List1', 'List2', 'List3' ] ], 
    'app06aa': [ name: 'Detail List 2a', 
				 views: [ 'Top', 'List1', 'List2', 'View3', 'ListView1' ] ], 
    'app06ab': [ name: 'Detail List 2b', 
				 views: [ 'Top', 'List1', 'List2', 'View3', 'ListView1' ] ], 
    'app06ac': [ name: 'Embedded List 2c', 
				 views: [ 'Top', 'ListView1', 'ListView2', 'View2', 'View3', 'View4' ] ], 
    'app06ad': [ name: 'Embedded List 2d', 
				 views: [ 'ListView1', 'View2', 'View3', 'View4' ] ], 
    'app06ae': [ name: 'Detail List 2e', 
				 views: [ 'Top', 'ListView1', 'List1', 'List2', 'List3', 'List4', 'List5', 'List6' ] ], 


    'app06b' : [ name: 'Detail List 3', 
				 views: [ 'Top', 'List1', 'List2', 'List3', 'View4' ] ], 
    'app06c' : [ name: 'Detail List 4', 
				 views: [ 'Top', 'List1', 'View2' ] ], 
    'app06ca': [ name: 'Detail List 4a', 
				 views: [ 'Top', 'List1', 'View2', 'View3', 'View4' ] ], 
    'app06cb': [ name: 'Detail List 4b', 
				 views: [ 'List1', 'View1', 'View2' ] ], 
    'app06cc': [ name: 'Detail List 4c', 
				 views: [ 'NavigationView1', 'List1', 'View1', 'View2' ] ], 


    'app07'  : [ name: 'Countries', 
				 views: [ 'Top', 'List1' ] ], 
    'app07a' : [ name: 'Countries 2', 
				 views: [ 'Top', 'List1' ] ], 
    'app07b' : [ name: 'Countries 3', 
				 views: [ 'Top', 'List1' ] ], 
    'app08'  : [ name: 'Euro Countries', 
				 views: [ 'Top', 'List1', 'List2', 'List3', 'List4', 'List5' ] ], 
    'app09'  : [ name: 'Web', 
				 views: [ 'V1' ] ], 
    'app09a' : [ name: 'Web 2', 
				 views: [ 'V1' ] ], 
    'app10'  : [ name: 'Map', 
				 views: [ 'V1' ] ], 
    'app10a' : [ name: 'Map 2', 
				 views: [ 'V1' ] ], 
    'app11'  : [ name: 'Picker', 
				 views: [ 'Top' ] ], 
    'app11a' : [ name: 'Picker 2', 
				 views: [ 'Top' ] ], 
    'app11b' : [ name: 'Picker 3', 
				 views: [ 'Top' ] ], 
    'app11c' : [ name: 'Picker 4', 
				 views: [ 'Top' ] ], 
    'app11d' : [ name: 'Picker 5', 
				 views: [ 'Top' ] ], 
    'app11e' : [ name: 'Picker 6', 
				 views: [ 'Top' ] ], 

  ]

  def androidFileMap = [
    'app05'  : [ name: 'List', 
				 views: [ 'List' ], 
				 layouts: [ 'main', 'list_header_listview1', 'list_item_listview1' ] ], 
    'app05a' : [ name: 'List2',
				 views: [ 'List2' ], 
				 layouts: [ 'main', 'list_item_listview1' ] ], 
    'app05b' : [ name: 'List3',
				 views: [ 'List3' ], 
				 layouts: [ 'main', 'list_header_list1', 'list_item_empty', 
							'list_item_list1', 'list_item_list2' ] ], 
    'app05ba' : [ name: 'List3a',
				  views: [ 'List3a' ], 
				  layouts: [ 'main', 'list_header_list1', 'list_item_empty', 
							 'list_item_list1', 'list_item_list2' ] ], 
    'app05c' : [ name: 'List4',
				 views: [ 'List2', 'List3', 'List4' ], 
				 layouts: [ 'main', 'list_header_list1', 'list_item_list3', 
							'list_item_list1', 'list_item_list2' ] ], 
    'app05d' : [ name: 'List5', 
				 views: [ 'List5' ], 
				 layouts: [ 'main', 'list_header_listview1', 'list_item_listview1' ] ],   
    'app05e' : [ name: 'List6',
				 views: [ 'List6' ], 
				 layouts: [ 'main', 'list_item_listview1' ] ], 
    'app05f' : [ name: 'List7',
				 views: [ 'List7' ], 
				 layouts: [ 'main', 'list_item_listview1' ] ], 
    'app05g' : [ name: 'List8',
				 views: [ 'List8' ], 
				 layouts: [ 'main', 'list_header_list1', 'list_item_list1' ] ], 
    'app05h' : [ name: 'List9',
				 views: [ 'List9' ], 
				 layouts: [ 'main', 'list_header_list1', 'list_item_list1' ] ], 
    'app05i' : [ name: 'List10',
				 views: [ 'List10' ], 
				 layouts: [ 'main', 'list_header_list1', 'list_item_list1' ] ], 
    'app05j' : [ name: 'List11',
				 views: [ 'List11' ], 
				 layouts: [ 'main', 'list_header_list1', 'list_item_list1' ] ], 


    'app06'  : [ name: 'Detail List',
				 views: [ 'DetailList', 'List2', 'List3' ], 
				 layouts: [ 'main', 'list_header_list1', 'list_item_list1',
							'list_header_list2', 'list_item_list2',
							'list_item_list3'] ], 
    'app06a' : [ name: 'Detail List 2',
				 views: [ 'DetailList2', 'List2', 'List3' ], 
				 layouts: [ 'main', 'list_header_list1', 'list_item_list1',
							'list_header_list2', 'list_item_list2',
							'list_item_list3'] ], 
    'app06aa': [ name: 'Detail List 2a',
				 views: [ 'DetailList2a', 'List2', 'View3' ], 
				 layouts: [ 'main', 'list_header_list1', 'list_item_list1',
							'list_header_list2', 'list_item_list2', 'view3',
							'list_item_listview1'] ], 
    'app06ab': [ name: 'Detail List 2b',
				 views: [ 'DetailList2b', 'List2', 'View3' ], 
				 layouts: [ 'main', 'list_header_list1', 'list_item_list1',
							'list_header_list2', 'list_item_list2', 'view3',
							'list_item_listview1' ] ], 
    'app06ac': [ name: 'Embedded List 2c', 
				 views: [ 'EmbeddedList2c', 'View2', 'View3', 'View4' ],
				 layouts: [ 'main', 'view2', 'view3', 'view4',	
							'list_item_listview1', 	'list_item_listview2' ] ], 
    'app06ad': [ name: 'Embedded List 2d', 
				 views: [ 'EmbeddedList2d', 'View3', 'View4' ],
				 layouts: [ 'main', 'view3', 'view4',	
							'list_item_listview1', ] ], 
    'app06ae': [ name: 'Detail List 2e',
				 views: [ 'DetailList2e', 'List1', 'List2', 'List3', 'List4', 'List5', 'List6' ], 
				 layouts: [ 'main', 'list_header_list1', 'list_item_list1',
							'list_header_list2', 'list_item_list2', 
							'list_item_list3', 'list_item_list4', 
							'list_header_list5', 'list_item_list5', 
							'list_item_list6', 'list_item_listview1' ] ], 

    'app06b' : [ name: 'Detail List 3',
				 views: [ 'DetailList3', 'List2', 'List3', 'View4' ], 
				 layouts: [ 'main', 'list_header_list1', 'list_item_list1',
							'list_header_list2', 'list_item_list2',
							'list_item_list3', 'view4'] ], 
    'app06c' : [ name: 'Detail List 4',
				 views: [ 'DetailList4', 'View2' ], 
				 layouts: [ 'main', 'list_item_list1', 'view2'] ], 
    'app06ca': [ name: 'Detail List 4a',
				 views: [ 'DetailList4a', 'View2', 'View3', 'View4' ], 
				 layouts: [ 'main', 'list_item_list1', 'list_header_list1', 'view2', 'view3', 'view4'] ], 
    'app06cb': [ name: 'Detail List 4b',
				 views: [ 'DetailList4b', 'List1', 'View2' ], 
				 layouts: [ 'main', 'list_item_list1', 'view2'] ], 
    'app06cc': [ name: 'Detail List 4c',
				 views: [ 'DetailList4c', 'List1', 'View2' ], 
				 layouts: [ 'main', 'list_item_list1', 'view2'] ], 

    'app07'  : [ name: 'Countries', 
				 views: [ 'Countries' ], 
				 layouts: [ 'main', 'list_header_list1', 'list_item_list1' ] ], 
    'app07a' : [ name: 'Countries2', 
				 views: [ 'Countries2' ], 
				 layouts: [ 'main', 'list_item_list1' ] ], 
    'app07b' : [ name: 'Countries3', 
				 views: [ 'Countries3' ], 
				 layouts: [ 'main', 'list_header_list1', 'list_item_list1' ] ], 

    'app08'  : [ name: 'Euro Countries', 
				 views: [ 'EuroCountries', 'List2', 'List3', 'List4', 'List5' ], 
				 layouts: [ 'main', 'list_item_list2', 'list_item_list3', 
							'list_item_list4', 'list_item_list5' ] ], 
    'app09'  : [ name: 'Web', 
				 views: [ 'Web' ],
				 layouts: [ 'main' ] ], 
    'app09a' : [ name: 'Web2', 
				 views: [ 'Web2' ],
				 layouts: [ 'main' ] ], 
    'app10'  : [ name: 'Map', 
				 views: [ 'Map' ],
				 layouts: [ 'main' ] ], 
    'app10a' : [ name: 'Map2', 
				 views: [ 'Map2' ],
				 layouts: [ 'main' ] ], 
    'app11'  : [ name: 'Picker', 
				 views: [ 'Picker' ],
				 layouts: [ 'main' ] ], 
    'app11a' : [ name: 'Picker2', 
				 views: [ 'Picker2' ],
				 layouts: [ 'main' ] ], 
    'app11b' : [ name: 'Picker4', 
				 views: [ 'Picker3' ],
				 layouts: [ 'main' ] ], 
    'app11c' : [ name: 'Picker5', 
				 views: [ 'Picker4' ],
				 layouts: [ 'main' ] ], 
    'app11d' : [ name: 'Picker7', 
				 views: [ 'Picker5' ],
				 layouts: [ 'main' ] ], 
    'app11e' : [ name: 'Picker8', 
				 views: [ 'Picker6' ],
				 layouts: [ 'main' ] ], 

  ]


  @Test(timeout=80000L)
  public void test05_iOS() {
    test_iOS('app05')
  }

  @Test(timeout=80000L)
  public void test05_Android() {
    test_Android('app05')
  }

  @Test(timeout=80000L)
  public void test05a_iOS() {
    test_iOS('app05a')
  }

  @Test(timeout=80000L)
  public void test05a_Android() {
    test_Android('app05a')
  }

  @Test(timeout=80000L)
  public void test05b_iOS() {
    test_iOS('app05b')
  }

  @Test(timeout=80000L)
  public void test05b_Android() {
    test_Android('app05b')
  }

  @Test(timeout=80000L)
  public void test05ba_iOS() {
    test_iOS('app05ba')
  }

  @Test(timeout=80000L)
  public void test05ba_Android() {
    test_Android('app05ba')
  }

  @Test(timeout=80000L)
  public void test05c_iOS() {
    test_iOS('app05c')
  }

  @Test(timeout=80000L)
  public void test05c_Android() {
    test_Android('app05c')
  }

  @Test(timeout=80000L)
  public void test05d_iOS() {
    test_iOS('app05d')
  }

  @Test(timeout=80000L)
  public void test05d_Android() {
    test_Android('app05d')
  }

  @Test(timeout=80000L)
  public void test05e_iOS() {
    test_iOS('app05e')
  }

  @Test(timeout=80000L)
  public void test05e_Android() {
    test_Android('app05e')
  }

  @Test(timeout=80000L)
  public void test05f_iOS() {
    test_iOS('app05f')
  }

  @Test(timeout=80000L)
  public void test05f_Android() {
    test_Android('app05f')
  }

  @Test(timeout=80000L)
  public void test05g_iOS() {
    test_iOS('app05g')
  }

  @Test(timeout=80000L)
  public void test05g_Android() {
    test_Android('app05g')
  }

  @Test(timeout=80000L)
  public void test05h_iOS() {
    test_iOS('app05h')
  }

  @Test(timeout=80000L)
  public void test05h_Android() {
    test_Android('app05h')
  }

  @Test(timeout=80000L)
  public void test05i_iOS() {
    test_iOS('app05i')
  }

  @Test(timeout=80000L)
  public void test05i_Android() {
    test_Android('app05i')
  }

  @Test(timeout=80000L)
  public void test05j_iOS() {
    test_iOS('app05j')
  }

  @Test(timeout=80000L)
  public void test05j_Android() {
    test_Android('app05j')
  }

  @Test(timeout=80000L)
  public void test06_iOS() {
    test_iOS('app06')
  }

  @Test(timeout=80000L)
  public void test06_Android() {
    test_Android('app06')
  }

  @Test(timeout=80000L)
  public void test06a_iOS() {
    test_iOS('app06a')
  }

  @Test(timeout=80000L)
  public void test06a_Android() {
    test_Android('app06a')
  }

  @Test(timeout=80000L)
  public void test06aa_iOS() {
    test_iOS('app06aa')
  }

  @Test(timeout=80000L)
  public void test06aa_Android() {
    test_Android('app06aa')
  }

  @Test(timeout=80000L)
  public void test06ab_iOS() {
    test_iOS('app06ab')
  }

  @Test(timeout=80000L)
  public void test06ab_Android() {
    test_Android('app06ab')
  }

  @Test(timeout=80000L)
  public void test06ac_iOS() {
    test_iOS('app06ac')
  }

  @Test(timeout=80000L)
  public void test06ac_Android() {
    test_Android('app06ac')
  }

  @Test(timeout=80000L)
  public void test06ad_iOS() {
    test_iOS('app06ad')
  }

  @Test(timeout=80000L)
  public void test06ad_Android() {
    test_Android('app06ad')
  }

  @Test(timeout=80000L)
  public void test06ae_iOS() {
    test_iOS('app06ae')
  }

  @Test(timeout=80000L)
  public void test06ae_Android() {
    test_Android('app06ae')
  }

  //@Test(timeout=80000L)
  public void test06b_iOS() {
    test_iOS('app06b')
  }

  //@Test(timeout=80000L)
  public void test06b_Android() {
    test_Android('app06b')
  }

  @Test(timeout=80000L)
  public void test06c_iOS() {
    test_iOS('app06c')
  }

  @Test(timeout=80000L)
  public void test06c_Android() {
    test_Android('app06c')
  }

  @Test(timeout=80000L)
  public void test06ca_iOS() {
    test_iOS('app06ca')
  }

  @Test(timeout=80000L)
  public void test06ca_Android() {
    test_Android('app06ca')
  }

  @Test(timeout=80000L)
  public void test06cb_iOS() {
    test_iOS('app06cb')
  }

  @Test(timeout=80000L)
  public void test06cb_Android() {
    test_Android('app06cb')
  }

  @Test(timeout=80000L)
  public void test06cc_iOS() {
    test_iOS('app06cc')
  }

  @Test(timeout=80000L)
  public void test06cc_Android() {
    test_Android('app06cc')
  }

  @Test(timeout=80000L)
  public void test07_iOS() {
    test_iOS('app07')
  }

  @Test(timeout=80000L)
  public void test07_Android() {
    test_Android('app07')
  }

  @Test(timeout=80000L)
  public void test07a_iOS() {
    test_iOS('app07a')
  }

  @Test(timeout=80000L)
  public void test07a_Android() {
    test_Android('app07a')
  }

  @Test(timeout=80000L)
  public void test07b_iOS() {
    test_iOS('app07b')
  }

  @Test(timeout=80000L)
  public void test07b_Android() {
    test_Android('app07b')
  }

  @Test(timeout=80000L)
  public void test08_iOS() {
    test_iOS('app08')
  }

  @Test(timeout=80000L)
  public void test08_Andriod() {
    test_Android('app08')
  }

  @Test(timeout=80000L)
  public void test09_iOS() {
    test_iOS('app09')
  }

  @Test(timeout=80000L)
  public void test09_Android() {
    test_Android('app09')
  }

  @Test(timeout=80000L)
  public void test09a_iOS() {
    test_iOS('app09a')
  }

  //@Test(timeout=80000L)
  public void test09a_Android() {
    test_Android('app09a')
  }

  @Test(timeout=80000L)
  public void test10_iOS() {
    test_iOS('app10')
  }

  @Test(timeout=80000L)
  public void test10_Android() {
    test_Android('app10')
  }

  @Test(timeout=80000L)
  public void test10a_iOS() {
    test_iOS('app10a')
  }

  @Test(timeout=80000L)
  public void test10a_Android() {
    test_Android('app10a')
  }

  @Test(timeout=80000L)
  public void test11_iOS() {
    test_iOS('app11')
  }

  @Test(timeout=80000L)
  public void test11_Android() {
    test_Android('app11')
  }

  @Test(timeout=80000L)
  public void test11a_iOS() {
    test_iOS('app11a')
  }

  @Test(timeout=80000L)
  public void test11a_Android() {
    test_Android('app11a')
  }

  @Test(timeout=80000L)
  public void test11b_iOS() {
    test_iOS('app11b')
  }

  @Test(timeout=80000L)
  public void test11b_Android() {
    test_Android('app11b')
  }

  @Test(timeout=80000L)
  public void test11c_iOS() {
    test_iOS('app11c')
  }

  @Test(timeout=80000L)
  public void test11c_Android() {
    test_Android('app11c')
  }

  @Test(timeout=80000L)
  public void test11d_iOS() {
    test_iOS('app11d')
  }

  @Test(timeout=80000L)
  public void test11d_Android() {
    test_Android('app11d')
  }

  @Test(timeout=80000L)
  public void test11e_iOS() {
    test_iOS('app11e')
  }

  @Test(timeout=80000L)
  public void test11e_Android() {
    test_Android('app11e')
  }

}