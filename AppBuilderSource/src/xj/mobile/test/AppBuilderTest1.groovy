package xj.mobile.test

import org.junit.*
import static org.junit.Assert.*

import xj.mobile.Main

class AppBuilderTest1 extends AppBuilderTest {  

  def iOSFileMap = [
    'app01'  : [ name: 'First App', 
				 views: [ 'Top' ] ], 
    'app01a' : [ name: 'First App 2', 
				 views: [ 'Top' ] ], 
    'app01aa': [ name: 'Test Color', 
				 views: [ 'Top' ] ],
    'app01ab': [ name: 'Test Font', 
				 views: [ 'View1' ] ],
    'app01ac': [ name: 'Test Font 2', 
				 views: [ 'View1' ] ],
    'app01ad': [ name: 'Test Font 3', 
				 views: [ 'View1' ] ],
 
    'app01b' : [ name: 'First App 3', 
				 views: [ 'Top' ] ], 

    'app01c' : [ name: 'First App 4', 
				 views: [ 'Top' ] ], 
    'app01ca': [ name: 'First App 4a', 
				 views: [ 'Top' ] ], 
    'app01cb': [ name: 'First App 4b', 
				 views: [ 'Top' ] ], 
    'app01cc': [ name: 'First App 4c', 
				 views: [ 'Top' ] ], 
    'app01cd': [ name: 'First App 4d', 
				 views: [ 'Top' ] ], 

    'app01d' : [ name: 'First App 5', 
				 views: [ 'Top' ] ], 

    'app01e' : [ name: 'First App 6', 
				 views: [ 'View1' ] ], 
    'app01f' : [ name: 'First App 7', 
				 views: [ 'View1' ] ], 
    'app01g' : [ name: 'First App 8', 
				 views: [ 'View1' ] ], 
    'app01ga': [ name: 'First App 8a', 
				 views: [ 'View1' ] ], 
    'app01h' : [ name: 'First App 9', 
				 views: [ 'View1' ] ], 
    'app01i' : [ name: 'First App 10', 
				 views: [ 'View1' ] ], 
    'app01j' : [ name: 'First App 11', 
				 views: [ 'View1' ] ], 

  ]

  def androidFileMap = [
    'app01'  : [ name: 'First App', 
				 views: [ 'FirstApp' ], 
				 layouts: [ 'main' ] ], 
    'app01a' : [ name: 'First App 2',
				 views: [ 'FirstApp2' ], 
				 layouts: [ 'main' ] ], 
    'app01aa': [ name: 'Test Color',
				 views: [ 'TestColor' ], 
				 layouts: [ 'main' ] ], 
    'app01ab': [ name: 'Test Font',
				 views: [ 'TestFont' ], 
				 layouts: [ 'main' ] ], 
    'app01ac': [ name: 'Test Font 2',
				 views: [ 'TestFont2' ], 
				 layouts: [ 'main' ] ], 
    'app01ad': [ name: 'Test Font 3',
				 views: [ 'TestFont3' ], 
				 layouts: [ 'main' ] ], 

    'app01b' : [ name: 'First App 3',
				 views: [ 'FirstApp3' ], 
				 layouts: [ 'main' ] ], 

    'app01c' : [ name: 'First App 4',
				 views: [ 'FirstApp4' ], 
				 layouts: [ 'main' ] ], 
    'app01ca': [ name: 'First App 4a',
				 views: [ 'FirstApp4a' ], 
				 layouts: [ 'main' ] ], 
    'app01cb': [ name: 'First App 4b',
				 views: [ 'FirstApp4b' ], 
				 layouts: [ 'main' ] ], 
    'app01cc': [ name: 'First App 4c',
				 views: [ 'FirstApp4c' ], 
				 layouts: [ 'main' ] ], 
    'app01cd': [ name: 'First App 4d',
				 views: [ 'FirstApp4d' ], 
				 layouts: [ 'main' ] ], 

    'app01d' : [ name: 'First App 5',
				 views: [ 'FirstApp5' ], 
				 layouts: [ 'main' ] ], 

    'app01e' : [ name: 'First App 6',
				 views: [ 'FirstApp6' ], 
				 layouts: [ 'main' ] ], 
    'app01f' : [ name: 'First App 7',
				 views: [ 'FirstApp7' ], 
				 layouts: [ 'main' ] ], 
    'app01g' : [ name: 'First App 8',
				 views: [ 'FirstApp8' ], 
				 layouts: [ 'main' ] ], 
    'app01ga': [ name: 'First App 8a',
				 views: [ 'FirstApp8a' ], 
				 layouts: [ 'main' ] ], 
    'app01h' : [ name: 'First App 9',
				 views: [ 'FirstApp9' ], 
				 layouts: [ 'main' ] ], 
    'app01i' : [ name: 'First App 10',
				 views: [ 'FirstApp10' ], 
				 layouts: [ 'main' ] ], 
    'app01j' : [ name: 'First App 11',
				 views: [ 'FirstApp11' ], 
				 layouts: [ 'main' ] ], 

  ]

  @Test(timeout=80000L)
  public void test01_iOS() {
    test_iOS('app01')
  }

  @Test(timeout=80000L)
  public void test01_Android() {
    test_Android('app01')
  }

  @Test(timeout=80000L)
  public void test01a_iOS() {
    test_iOS('app01a')
  }

  @Test(timeout=80000L)
  public void test01a_Android() {
    test_Android('app01a')
  }

  @Test(timeout=80000L)
  public void test01aa_iOS() {
    test_iOS('app01aa')
  }

  @Test(timeout=80000L)
  public void test01aa_Android() {
    test_Android('app01aa')
  }

  @Test(timeout=80000L)
  public void test01ab_iOS() {
    test_iOS('app01ab')
  }

  @Test(timeout=80000L)
  public void test01ab_Android() {
    test_Android('app01ab')
  }

  @Test(timeout=80000L)
  public void test01ac_iOS() {
    test_iOS('app01ac')
  }

  @Test(timeout=80000L)
  public void test01ac_Android() {
    test_Android('app01ac')
  }

  @Test(timeout=80000L)
  public void test01ad_iOS() {
    test_iOS('app01ad')
  }

  @Test(timeout=80000L)
  public void test01ad_Android() {
    test_Android('app01ad')
  }

  @Test(timeout=80000L)
  public void test01b_iOS() {
    test_iOS('app01b')
  }

  @Test(timeout=80000L)
  public void test01b_Android() {
    test_Android('app01b')
  }

  @Test(timeout=80000L)
  public void test01c_iOS() {
    test_iOS('app01c')
  }

  @Test(timeout=80000L)
  public void test01c_Android() {
    test_Android('app01c')
  }

  @Test(timeout=80000L)
  public void test01ca_iOS() {
    test_iOS('app01ca')
  }

  @Test(timeout=80000L)
  public void test01ca_Android() {
    test_Android('app01ca')
  }

  @Test(timeout=80000L)
  public void test01cb_iOS() {
    test_iOS('app01cb')
  }

  @Test(timeout=80000L)
  public void test01cb_Android() {
    test_Android('app01cb')
  }

  @Test(timeout=80000L)
  public void test01cc_iOS() {
    test_iOS('app01cc')
  }

  @Test(timeout=80000L)
  public void test01cc_Android() {
    test_Android('app01cc')
  }

  @Test(timeout=80000L)
  public void test01cd_iOS() {
    test_iOS('app01cd')
  }

  @Test(timeout=80000L)
  public void test01cd_Android() {
    test_Android('app01cd')
  }

  @Test(timeout=80000L)
  public void test01d_iOS() {
    test_iOS('app01d')
  }

  @Test(timeout=80000L)
  public void test01d_Android() {
    test_Android('app01d')
  }

  @Test(timeout=80000L)
  public void test01e_iOS() {
    test_iOS('app01e')
  }

  @Test(timeout=80000L)
  public void test01e_Android() {
    test_Android('app01e')
  }

  @Test(timeout=80000L)
  public void test01f_iOS() {
    test_iOS('app01f')
  }

  @Test(timeout=80000L)
  public void test01f_Android() {
    test_Android('app01f')
  }

  @Test(timeout=80000L)
  public void test01g_iOS() {
    test_iOS('app01g')
  }

  @Test(timeout=80000L)
  public void test01g_Android() {
    test_Android('app01g')
  }

  @Test(timeout=80000L)
  public void test01ga_iOS() {
    test_iOS('app01ga')
  }

  @Test(timeout=80000L)
  public void test01ga_Android() {
    test_Android('app01ga')
  }

  @Test(timeout=80000L)
  public void test01h_iOS() {
    test_iOS('app01h')
  }

  @Test(timeout=80000L)
  public void test01h_Android() {
    test_Android('app01h')
  }

  @Test(timeout=80000L)
  public void test01i_iOS() {
    test_iOS('app01i')
  }

  @Test(timeout=80000L)
  public void test01i_Android() {
    test_Android('app01i')
  }

  @Test(timeout=80000L)
  public void test01j_iOS() {
    test_iOS('app01j')
  }

  @Test(timeout=80000L)
  public void test01j_Android() {
    test_Android('app01j')
  }



}