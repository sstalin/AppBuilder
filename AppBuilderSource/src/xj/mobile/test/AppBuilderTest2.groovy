package xj.mobile.test

import org.junit.*
import static org.junit.Assert.*

import xj.mobile.Main

class AppBuilderTest2 extends AppBuilderTest {  

  def iOSFileMap = [
    'app02'  : [ name: 'App Button', 
				 views: [ 'Top' ] ], 
    'app02a' : [ name: 'App Button 2', 
				 views: [ 'Top' ] ], 
    'app02aa': [ name: 'App Button 2a', 
				 views: [ 'Top' ] ], 
    'app02ab': [ name: 'App Button 2b', 
				 views: [ 'Top' ] ], 
    'app02b' : [ name: 'App Button 3', 
				 views: [ 'View1' ] ], 
    'app02ba': [ name: 'App Button 3a', 
				 views: [ 'View1' ] ], 
    'app02bb': [ name: 'App Button 3b', 
				 views: [ 'View1' ] ], 
    'app02bc': [ name: 'App Button 3c', 
				 views: [ 'View1' ] ], 

    'app02c' : [ name: 'App Button 4', 
				 views: [ 'View1' ] ], 
    'app02d' : [ name: 'App Button 5', 
				 views: [ 'View1' ] ], 

    'app02e' : [ name: 'App Button 6', 
				 views: [ 'View1' ] ], 
    'app02ea': [ name: 'App Button 6a', 
				 views: [ 'View1' ] ], 
    'app02eb': [ name: 'App Button 6b', 
				 views: [ 'View1' ] ], 
    'app02ec': [ name: 'App Button 6c', 
				 views: [ 'View1' ] ], 
    'app02ed': [ name: 'App Button 6d', 
				 views: [ 'View1' ] ], 
    'app02ee': [ name: 'App Button 6e', 
				 views: [ 'View1' ] ], 
    'app02ef': [ name: 'App Button 6f', 
				 views: [ 'View1' ] ], 
    'app02f' : [ name: 'App Button 7', 
				 views: [ 'Top', 'V2' ] ], 
    'app02fa': [ name: 'App Button 7a', 
				 views: [ 'Top', 'V2' ] ], 
    'app02g' : [ name: 'App Button 8', 
				 views: [ 'Top' ] ], 
    'app02ga': [ name: 'App Button 8a', 
				 views: [ 'Vw1' ] ], 
    'app02gb': [ name: 'App Button 8b', 
				 views: [ 'Vw1' ] ], 

    'app03'  : [ name: 'Tabs', 
				 views: [ 'Top', 'First', 'Second', 'Third' ] ], 
    'app04'  : [ name: 'NavView', 
				 views: [ 'Top', 'V1', 'V2' ] ], 
    'app04a' : [ name: 'NavView2', 
				 views: [ 'Top', 'V1', 'V2' ] ], 
    'app04b' : [ name: 'NavView3', 
				 views: [ 'Top', 'V1', 'V2', 'V3', 'V4' ] ], 
    'app04c' : [ name: 'NavView4', 
				 views: [ 'Top', 'V1', 'V2', 'V3' ] ], 
    'app04d' : [ name: 'NavView5', 
				 views: [ 'Top', 'V1', 'V2' ] ], 
    'app04e' : [ name: 'Modal View', 
				 views: [ 'V1', 'V2' ] ], 
    'app04ea': [ name: 'Modal View 1a', 
				 views: [ 'V1', 'V2' ] ], 
    'app04eb': [ name: 'Modal View 1b', 
				 views: [ 'V1', 'V2', 'V3' ] ], 
    'app04ec': [ name: 'Modal View 1c', 
				 views: [ 'NavigationView1', 'V1', 'V2', 'V3' ] ], 
    'app04f' : [ name: 'Modal View 2', 
				 views: [ 'V1', 'V2' ] ], 
    'app04g' : [ name: 'Modal View 3', 
				 views: [ 'V1', 'V2' ] ], 
    'app04ga': [ name: 'Modal View 3a', 
				 views: [ 'V1', 'V2' ] ], 

  ]

  def androidFileMap = [
    'app02'  : [ name: 'App Button', 
				 views: [ 'AppButton' ], 
				 layouts: [ 'main' ] ], 
    'app02a' : [ name: 'App Button 2', 
				 views: [ 'AppButton2' ], 
				 layouts: [ 'main' ] ], 
    'app02aa': [ name: 'App Button 2a', 
				 views: [ 'AppButton2a' ], 
				 layouts: [ 'main' ] ], 
    'app02ab': [ name: 'App Button 2b', 
				 views: [ 'AppButton2b' ], 
				 layouts: [ 'main' ] ], 
    'app02b' : [ name: 'App Button 3', 
				 views: [ 'AppButton3' ], 
				 layouts: [ 'main' ] ], 
    'app02ba': [ name: 'App Button 3a', 
				 views: [ 'AppButton3a' ], 
				 layouts: [ 'main' ] ], 
    'app02bb': [ name: 'App Button 3b', 
				 views: [ 'AppButton3b' ], 
				 layouts: [ 'main' ] ], 
    'app02bc': [ name: 'App Button 3c', 
				 views: [ 'AppButton3c' ], 
				 layouts: [ 'main' ] ], 

    'app02c' : [ name: 'App Button 4', 
				 views: [ 'AppButton4' ], 
				 layouts: [ 'main' ] ], 
    'app02d' : [ name: 'App Button 5', 
				 views: [ 'AppButton5' ], 
				 layouts: [ 'main' ] ], 

    'app02e' : [ name: 'App Button 6', 
				 views: [ 'AppButton6' ], 
				 layouts: [ 'main' ] ], 
    'app02ea': [ name: 'App Button 6a', 
				 views: [ 'AppButton6a' ], 
				 layouts: [ 'main' ] ], 
    'app02eb': [ name: 'App Button 6b', 
				 views: [ 'AppButton6b' ], 
				 layouts: [ 'main' ] ], 
    'app02ec': [ name: 'App Button 6c', 
				 views: [ 'AppButton6c' ], 
				 layouts: [ 'main' ] ], 
    'app02ed': [ name: 'App Button 6d', 
				 views: [ 'AppButton6d' ], 
				 layouts: [ 'main' ] ], 
    'app02ee': [ name: 'App Button 6e', 
				 views: [ 'AppButton6e' ], 
				 layouts: [ 'main' ] ], 
    'app02ef': [ name: 'App Button 6f', 
				 views: [ 'AppButton6f' ], 
				 layouts: [ 'main' ] ], 
    'app02f' : [ name: 'App Button 7', 
				 views: [ 'AppButton7', 'V2' ], 
				 layouts: [ 'main', 'v2' ] ], 
    'app02fa': [ name: 'App Button 7a', 
				 views: [ 'AppButton7a', 'V2' ], 
				 layouts: [ 'main', 'v2' ] ], 
    'app02g' : [ name: 'App Button 8', 
				 views: [ 'AppButton8' ], 
				 layouts: [ 'main' ] ], 
    'app02ga': [ name: 'App Button 8a', 
				 views: [ 'AppButton8a' ], 
				 layouts: [ 'main' ] ], 
    'app02gb': [ name: 'App Button 8b', 
				 views: [ 'AppButton8b' ], 
				 layouts: [ 'main' ] ], 

    'app03'  : [ name: 'Tabs', 
				 views: [ 'Tabs', 'First', 'Second', 'Third' ], 
				 layouts: [ 'main', 'first', 'second', 'third' ] ], 
    'app04'  : [ name: 'NavView', 
				 views: [ 'NavView', 'V2' ], 
				 layouts: [ 'main', 'v2' ] ], 
    'app04a' : [ name: 'NavView2', 
				 views: [ 'NavView2', 'V2', 'V3' ], 
				 layouts: [ 'main', 'v2', 'v3' ] ], 
    'app04b' : [ name: 'NavView3', 
				 views: [ 'NavView3', 'V2', 'V3', 'V4' ], 
				 layouts: [ 'main', 'v2', 'v3','v4' ] ], 
    'app04c' : [ name: 'NavView4', 
				 views: [ 'NavView4', 'V2', 'V3' ], 
				 layouts: [ 'main', 'v2', 'v3' ] ], 
    'app04d' : [ name: 'NavView5', 
				 views: [ 'NavView5', 'V2' ], 
				 layouts: [ 'main', 'v2' ] ], 
    'app04e' : [ name: 'Modal View', 
				 views: [ 'ModalView', 'V2' ], 
				 layouts: [ 'main', 'v2' ] ], 
    'app04ea': [ name: 'Modal View 1a', 
				 views: [ 'ModalView1a', 'V2' ], 
				 layouts: [ 'main', 'v2' ] ], 
    'app04eb': [ name: 'Modal View 1b', 
				 views: [ 'ModalView1b', 'V2', 'V3' ], 
				 layouts: [ 'main', 'v2', 'v3' ] ], 
    'app04ec': [ name: 'Modal View 1c', 
				 views: [ 'ModalView1c', 'V2', 'V3' ], 
				 layouts: [ 'main', 'v2', 'v3' ] ], 
    'app04f' : [ name: 'Modal View 2', 
				 views: [ 'ModalView2', 'V2' ], 
				 layouts: [ 'main', 'v2' ] ], 
    'app04g' : [ name: 'Modal View 3', 
				 views: [ 'ModalView3', 'V2' ], 
				 layouts: [ 'main', 'v2' ] ], 
    'app04ga': [ name: 'Modal View 3a', 
				 views: [ 'ModalView3a', 'V2' ], 
				 layouts: [ 'main', 'v2' ] ], 
  ]


  @Test(timeout=80000L)
  public void test02_iOS() {
    test_iOS('app02')
  }

  @Test(timeout=80000L)
  public void test02_Android() {
    test_Android('app02')
  }

  @Test(timeout=80000L)
  public void test02a_iOS() {
    test_iOS('app02a')
  }

  @Test(timeout=80000L)
  public void test02a_Android() {
    test_Android('app02a')
  }

  @Test(timeout=80000L)
  public void test02aa_iOS() {
    test_iOS('app02aa')
  }

  @Test(timeout=80000L)
  public void test02aa_Android() {
    test_Android('app02aa')
  }

  @Test(timeout=80000L)
  public void test02ab_iOS() {
    test_iOS('app02ab')
  }

  @Test(timeout=80000L)
  public void test02ab_Android() {
    test_Android('app02ab')
  }

  @Test(timeout=80000L)
  public void test02b_iOS() {
    test_iOS('app02b')
  }

  @Test(timeout=80000L)
  public void test02b_Android() {
    test_Android('app02b')
  }

  @Test(timeout=80000L)
  public void test02ba_iOS() {
    test_iOS('app02ba')
  }

  @Test(timeout=80000L)
  public void test02ba_Android() {
    test_Android('app02ba')
  }

  @Test(timeout=80000L)
  public void test02bb_iOS() {
    test_iOS('app02bb')
  }

  @Test(timeout=80000L)
  public void test02bb_Android() {
    test_Android('app02bb')
  }

  @Test(timeout=80000L)
  public void test02bc_iOS() {
    test_iOS('app02bc')
  }

  @Test(timeout=80000L)
  public void test02bc_Android() {
    test_Android('app02bc')
  }

  @Test(timeout=80000L)
  public void test02c_iOS() {
    test_iOS('app02c')
  }

  @Test(timeout=80000L)
  public void test02c_Android() {
    test_Android('app02c')
  }

  @Test(timeout=80000L)
  public void test02d_iOS() {
    test_iOS('app02d')
  }

  @Test(timeout=80000L)
  public void test02d_Android() {
    test_Android('app02d')
  }

  @Test(timeout=80000L)
  public void test02e_iOS() {
    test_iOS('app02e')
  }

  @Test(timeout=80000L)
  public void test02e_Android() {
    test_Android('app02e')
  }

  @Test(timeout=80000L)
  public void test02ea_iOS() {
    test_iOS('app02ea')
  }

  @Test(timeout=80000L)
  public void test02ea_Android() {
    test_Android('app02ea')
  }

  @Test(timeout=80000L)
  public void test02eb_iOS() {
    test_iOS('app02eb')
  }

  @Test(timeout=80000L)
  public void test02eb_Android() {
    test_Android('app02eb')
  }

  @Test(timeout=80000L)
  public void test02ec_iOS() {
    test_iOS('app02ec')
  }

  @Test(timeout=80000L)
  public void test02ec_Android() {
    test_Android('app02ec')
  }

  @Test(timeout=80000L)
  public void test02ed_iOS() {
    test_iOS('app02ed')
  }

  @Test(timeout=80000L)
  public void test02ed_Android() {
    test_Android('app02ed')
  }

  @Test(timeout=80000L)
  public void test02ee_iOS() {
    test_iOS('app02ee')
  }

  @Test(timeout=80000L)
  public void test02ee_Android() {
    test_Android('app02ee')
  }

  @Test(timeout=80000L)
  public void test02ef_iOS() {
    test_iOS('app02ef')
  }

  @Test(timeout=80000L)
  public void test02ef_Android() {
    test_Android('app02ef')
  }

  @Test(timeout=80000L)
  public void test02f_iOS() {
    test_iOS('app02f')
  }

  @Test(timeout=80000L)
  public void test02f_Android() {
    test_Android('app02f')
  }

  @Test(timeout=80000L)
  public void test02fa_iOS() {
    test_iOS('app02fa')
  }

  @Test(timeout=80000L)
  public void test02fa_Android() {
    test_Android('app02fa')
  }

  @Test(timeout=80000L)
  public void test02g_iOS() {
    test_iOS('app02g')
  }

  @Test(timeout=80000L)
  public void test02g_Android() {
    test_Android('app02g')
  }

  @Test(timeout=80000L)
  public void test02ga_iOS() {
    test_iOS('app02ga')
  }

  @Test(timeout=80000L)
  public void test02ga_Android() {
    test_Android('app02ga')
  }

  @Test(timeout=80000L)
  public void test02gb_iOS() {
    test_iOS('app02gb')
  }

  @Test(timeout=80000L)
  public void test02gb_Android() {
    test_Android('app02gb')
  }





  //

  @Test(timeout=80000L)
  public void test03_iOS() {
    test_iOS('app03')
  }

  @Test(timeout=80000L)
  public void test03_Android() {
    test_Android('app03')
  }

  @Test(timeout=80000L)
  public void test04_iOS() {
    test_iOS('app04')
  }

  @Test(timeout=80000L)
  public void test04_Android() {
    test_Android('app04')
  }

  @Test(timeout=80000L)
  public void test04a_iOS() {
    test_iOS('app04a')
  }

  @Test(timeout=80000L)
  public void test04a_Android() {
    test_Android('app04a')
  }

  @Test(timeout=80000L)
  public void test04b_iOS() {
    test_iOS('app04b')
  }

  @Test(timeout=80000L)
  public void test04b_Android() {
    test_Android('app04b')
  }

  @Test(timeout=80000L)
  public void test04c_iOS() {
    test_iOS('app04c')
  }

  @Test(timeout=80000L)
  public void test04c_Android() {
    test_Android('app04c')
  }

  @Test(timeout=80000L)
  public void test04d_iOS() {
    test_iOS('app04d')
  }

  @Test(timeout=80000L)
  public void test04d_Android() {
    test_Android('app04d')
  }

  @Test(timeout=80000L)
  public void test04e_iOS() {
    test_iOS('app04e')
  }

  @Test(timeout=80000L)
  public void test04e_Android() {
    test_Android('app04e')
  }

  @Test(timeout=80000L)
  public void test04ea_iOS() {
    test_iOS('app04ea')
  }

  @Test(timeout=80000L)
  public void test04ea_Android() {
    test_Android('app04ea')
  }

  @Test(timeout=80000L)
  public void test04eb_iOS() {
    test_iOS('app04eb')
  }

  @Test(timeout=80000L)
  public void test04eb_Android() {
    test_Android('app04eb')
  }

  @Test(timeout=80000L)
  public void test04ec_iOS() {
    test_iOS('app04ec')
  }

  @Test(timeout=80000L)
  public void test04ec_Android() {
    test_Android('app04ec')
  }

  @Test(timeout=80000L)
  public void test04f_iOS() {
    test_iOS('app04f')
  }

  @Test(timeout=80000L)
  public void test04f_Android() {
    test_Android('app04f')
  }

  @Test(timeout=80000L)
  public void test04g_iOS() {
    test_iOS('app04g')
  }

  @Test(timeout=80000L)
  public void test04g_Android() {
    test_Android('app04g')
  }

  @Test(timeout=80000L)
  public void test04ga_iOS() {
    test_iOS('app04ga')
  }

  @Test(timeout=80000L)
  public void test04ga_Android() {
    test_Android('app04ga')
  }



}