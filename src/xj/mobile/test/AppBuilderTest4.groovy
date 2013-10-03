package xj.mobile.test

import org.junit.*
import static org.junit.Assert.*

import xj.mobile.Main

import static xj.mobile.test.AppBuilderTest.*

class AppBuilderTest4 extends AppBuilderTest {  

  def iOSFileMap = [
    'app12'  : [ name: 'Alerts', 
				 views: [ 'Top' ] ], 
    'app12a' : [ name: 'Alerts 2', 
				 views: [ 'Top' ] ], 
    'app12aa': [ name: 'Alerts 2a', 
				 views: [ 'Top' ] ], 
    'app12ab': [ name: 'Alerts 2b', 
				 views: [ 'Top' ] ], 
    'app12b' : [ name: 'Alerts 3', 
				 views: [ 'Top' ] ], 
    'app12ba': [ name: 'Alerts 3a', 
				 views: [ 'Top' ] ], 
    'app12bb': [ name: 'Alerts 3b', 
				 views: [ 'Top' ] ], 
    'app12c' : [ name: 'Menu', 
				 views: [ 'Top' ] ], 
    'app12d' : [ name: 'Menu 2', 
				 views: [ 'Top' ] ], 
    'app12e' : [ name: 'Alerts 4', 
				 views: [ 'Top' ] ], 
    'app12ea': [ name: 'Alerts 4a', 
				 views: [ 'Top' ] ], 

    'app13'  : [ name: 'Shopping List',
				 views : [ 'Item' ] ],
    'app13a' : [ name: 'Shopping List 2',
				 views : [ 'Item' ] ],
    'app13b' : [ name: 'Shopping List 3',
				 views : [ 'Item' ] ],
    'app13ba' : [ name: 'Shopping List 3a',
				 views : [ 'Item' ] ],
    'app13c' : [ name: 'Shopping List 4',
				 views : [ 'Item' ] ],
    'app13ca' : [ name: 'Shopping List 4a',
				 views : [ 'Item' ] ],
    'app13d' : [ name: 'Shopping List 5',
				 views : [ 'Mylist' ] ],
    'app13da': [ name: 'Shopping List 5a',
				 views : [ 'Mylist' ] ],
    'app13e' : [ name: 'Shopping List 6',
				 views : [ 'Mylist' ] ],
    'app13f' : [ name: 'Shopping List 7',
				 views : [ 'Mylist' ] ],
    'app13g' : [ name: 'Shopping List 8',
				 views : [ 'Mylist' ] ],
    'app13h' : [ name: 'Shopping List 9',
				 views : [ 'Mylist' ] ],
    'app13i' : [ name: 'Shopping List 10',
				 views : [ 'Mylist' ] ],
    'app13j' : [ name: 'Shopping List 11',
				 views : [ 'Mylist' ] ],
    'app13k' : [ name: 'Shopping List 12',
				 views : [ 'Mylist' ] ],
    'app13l' : [ name: 'Shopping List 13',
				 views : [ 'Mylist' ] ],

  ]

  def androidFileMap = [
    'app12'  : [ name: 'Alerts', 
				 views: [ 'Alerts' ],
				 layouts: [ 'main' ] ], 
    'app12a' : [ name: 'Alerts2', 
				 views: [ 'Alerts2' ],
				 layouts: [ 'main' ] ], 
    'app12aa': [ name: 'Alerts2a', 
				 views: [ 'Alerts2a' ],
				 layouts: [ 'main' ] ], 
    'app12ab': [ name: 'Alerts2b', 
				 views: [ 'Alerts2b' ],
				 layouts: [ 'main' ] ], 
    'app12b' : [ name: 'Alerts3', 
				 views: [ 'Alerts3' ],
				 layouts: [ 'main' ] ], 
    'app12ba': [ name: 'Alerts3a', 
				 views: [ 'Alerts3a' ],
				 layouts: [ 'main' ] ], 
    'app12bb': [ name: 'Alerts3b', 
				 views: [ 'Alerts3b' ],
				 layouts: [ 'main' ] ], 
    'app12c' : [ name: 'Menu', 
				 views: [ 'Menu' ],
				 layouts: [ 'main' ] ], 
    'app12d' : [ name: 'Menu2', 
				 views: [ 'Menu2' ],
				 layouts: [ 'main' ] ], 
    'app12e' : [ name: 'Alerts4', 
				 views: [ 'Alerts4' ],
				 layouts: [ 'main' ] ], 
    'app12ea': [ name: 'Alerts4a', 
				 views: [ 'Alerts4a' ],
				 layouts: [ 'main' ] ], 

    'app13'  : [ name: 'ShoppingList', 
				 views: [ 'ShoppingList' ],
				 layouts: [ 'list_item_item' ] ],
    'app13a' : [ name: 'ShoppingList2', 
				 views: [ 'ShoppingList2' ],
				 layouts: [ 'list_item_item' ] ],
    'app13b' : [ name: 'ShoppingList3', 
				 views: [ 'ShoppingList3' ],
				 layouts: [ 'list_item_item' ] ],
    'app13ba': [ name: 'ShoppingList3a', 
				 views: [ 'ShoppingList3a' ],
				 layouts: [ 'list_item_item' ] ],
    'app13c' : [ name: 'ShoppingList4', 
				 views: [ 'ShoppingList4' ],
				 layouts: [ 'list_item_item' ] ],
    'app13ca': [ name: 'ShoppingList4a', 
				 views: [ 'ShoppingList4a' ],
				 layouts: [ 'list_item_item' ] ],
    'app13d' : [ name: 'ShoppingList5', 
				 views: [ 'ShoppingList5' ],
				 layouts: [ 'list_item_mylist' ] ],
    'app13da': [ name: 'ShoppingList5a', 
				 views: [ 'ShoppingList5a' ],
				 layouts: [ 'list_item_mylist' ] ],
    'app13e' : [ name: 'ShoppingList6', 
				 views: [ 'ShoppingList6' ],
				 layouts: [ 'list_item_mylist' ] ],
    'app13f' : [ name: 'ShoppingList7', 
				 views: [ 'ShoppingList7' ],
				 layouts: [ 'list_item_mylist' ] ],
    'app13g' : [ name: 'ShoppingList8', 
				 views: [ 'ShoppingList8' ],
				 layouts: [ 'list_item_mylist' ] ],
    'app13h' : [ name: 'ShoppingList9', 
				 views: [ 'ShoppingList9' ],
				 layouts: [ 'list_item_mylist' ] ],
    'app13i' : [ name: 'ShoppingList10', 
				 views: [ 'ShoppingList10' ],
				 layouts: [ 'list_item_mylist' ] ],
    'app13j' : [ name: 'ShoppingList11', 
				 views: [ 'ShoppingList11' ],
				 layouts: [ 'list_item_mylist' ] ],
    'app13k' : [ name: 'ShoppingList12', 
				 views: [ 'ShoppingList12' ],
				 layouts: [ 'list_item_mylist' ] ],
    'app13l' : [ name: 'ShoppingList13', 
				 views: [ 'ShoppingList13' ],
				 layouts: [ 'list_item_mylist' ] ],

  ]


  @Test(timeout=80000L)
  public void test12_iOS() {
    test_iOS('app12')
  }

  @Test(timeout=80000L)
  public void test12_Android() {
    test_Android('app12')
  }

  @Test(timeout=80000L)
  public void test12a_iOS() {
    test_iOS('app12a')
  }

  @Test(timeout=80000L)
  public void test12a_Android() {
    test_Android('app12a')
  }

  @Test(timeout=80000L)
  public void test12aa_iOS() {
    test_iOS('app12aa')
  }

  @Test(timeout=80000L)
  public void test12aa_Android() {
    test_Android('app12aa')
  }

  @Test(timeout=80000L)
  public void test12ab_iOS() {
    test_iOS('app12ab')
  }

  @Test(timeout=80000L)
  public void test12ab_Android() {
    test_Android('app12ab')
  }

  @Test(timeout=80000L)
  public void test12b_iOS() {
    test_iOS('app12b')
  }

  @Test(timeout=80000L)
  public void test12b_Android() {
    test_Android('app12b')
  }

  @Test(timeout=80000L)
  public void test12ba_iOS() {
    test_iOS('app12ba')
  }

  @Test(timeout=80000L)
  public void test12ba_Android() {
    test_Android('app12ba')
  }

  @Test(timeout=80000L)
  public void test12bb_iOS() {
    test_iOS('app12bb')
  }

  @Test(timeout=80000L)
  public void test12bb_Android() {
    test_Android('app12bb')
  }

  @Test(timeout=80000L)
  public void test12c_iOS() {
    test_iOS('app12c')
  }

  @Test(timeout=80000L)
  public void test12c_Android() {
    test_Android('app12c')
  }

  @Test(timeout=80000L)
  public void test12d_iOS() {
    test_iOS('app12d')
  }

  @Test(timeout=80000L)
  public void test12d_Android() {
    test_Android('app12d')
  }

  @Test(timeout=80000L)
  public void test12e_iOS() {
    test_iOS('app12e')
  }

  @Test(timeout=80000L)
  public void test12e_Android() {
    test_Android('app12e')
  }

  @Test(timeout=80000L)
  public void test12ea_iOS() {
    test_iOS('app12ea')
  }

  @Test(timeout=80000L)
  public void test12ea_Android() {
    test_Android('app12ea')
  }

  @Test(timeout=80000L)
  public void test13_iOS() {
    test_iOS('app13')
  }

  @Test(timeout=80000L)
  public void test13_Android() {
    test_Android('app13')
  }

  @Test(timeout=80000L)
  public void test13a_iOS() {
    test_iOS('app13a')
  }

  @Test(timeout=80000L)
  public void test13a_Android() {
    test_Android('app13a')
  }

  @Test(timeout=80000L)
  public void test13b_iOS() {
    test_iOS('app13b')
  }

  @Test(timeout=80000L)
  public void test13b_Android() {
    test_Android('app13b')
  }

  @Test(timeout=80000L)
  public void test13ba_iOS() {
    test_iOS('app13ba')
  }

  @Test(timeout=80000L)
  public void test13ba_Android() {
    test_Android('app13ba')
  }

  @Test(timeout=80000L)
  public void test13c_iOS() {
    test_iOS('app13c')
  }

  @Test(timeout=80000L)
  public void test13c_Android() {
    test_Android('app13c')
  }


  @Test(timeout=80000L)
  public void test13ca_iOS() {
    test_iOS('app13ca')
  }

  @Test(timeout=80000L)
  public void test13ca_Android() {
    test_Android('app13ca')
  }

  @Test(timeout=80000L)
  public void test13d_iOS() {
    test_iOS('app13d')
  }

  @Test(timeout=80000L)
  public void test13d_Android() {
    test_Android('app13d')
  }

  @Test(timeout=80000L)
  public void test13da_iOS() {
    test_iOS('app13da')
  }

  @Test(timeout=80000L)
  public void test13da_Android() {
    test_Android('app13da')
  }

  @Test(timeout=80000L)
  public void test13e_iOS() {
    test_iOS('app13e')
  }

  @Test(timeout=80000L)
  public void test13e_Android() {
    test_Android('app13e')
  }

  @Test(timeout=80000L)
  public void test13f_iOS() {
    test_iOS('app13f')
  }

  @Test(timeout=80000L)
  public void test13f_Android() {
    test_Android('app13f')
  }

  @Test(timeout=80000L)
  public void test13g_iOS() {
    test_iOS('app13g')
  }

  @Test(timeout=80000L)
  public void test13g_Android() {
    test_Android('app13g')
  }

  @Test(timeout=80000L)
  public void test13h_iOS() {
    test_iOS('app13h')
  }

  @Test(timeout=80000L)
  public void test13h_Android() {
    test_Android('app13h')
  }

  @Test(timeout=80000L)
  public void test13i_iOS() {
    test_iOS('app13i')
  }

  @Test(timeout=80000L)
  public void test13i_Android() {
    test_Android('app13i')
  }

  @Test(timeout=80000L)
  public void test13j_iOS() {
    test_iOS('app13j')
  }

  @Test(timeout=80000L)
  public void test13j_Android() {
    test_Android('app13j')
  }

  @Test(timeout=80000L)
  public void test13k_iOS() {
    test_iOS('app13k')
  }

  @Test(timeout=80000L)
  public void test13k_Android() {
    test_Android('app13k')
  }

  @Test(timeout=80000L)
  public void test13l_iOS() {
    test_iOS('app13l')
  }

  @Test(timeout=80000L)
  public void test13l_Android() {
    test_Android('app13l')
  }

}