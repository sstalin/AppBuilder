package xj.mobile.test

import org.junit.*
import static org.junit.Assert.*

import xj.mobile.Main

class AppBuilderTest6 extends AppBuilderTest {  

  def iOSFileMap = [
    'app27'  : [ name: 'Pinch Gesture', 
				 views: [ 'View1' ] ], 
    'app27a' : [ name: 'Pinch Gesture 2', 
				 views: [ 'View1' ] ], 
    'app27b' : [ name: 'Pinch Gesture 3', 
				 views: [ 'View1' ] ], 
    'app28'  : [ name: 'Rotatation Gesture', 
				 views: [ 'View1' ] ], 
    'app28a' : [ name: 'Rotatation Gesture 2', 
				 views: [ 'View1' ] ], 
    'app28b' : [ name: 'Rotatation Gesture 3', 
				 views: [ 'View1' ] ], 
    'app29'  : [ name: 'Long Press Gesture', 
				 views: [ 'View1' ] ], 
    'app29a' : [ name: 'Long Press Gesture 2', 
				 views: [ 'View1' ] ], 

    'app32'  : [ name: 'Launch Orientation 1', 
				 views: [ 'View1' ] ], 
    'app32a' : [ name: 'Launch Orientation 2', 
				 views: [ 'View1' ] ], 
    'app32b' : [ name: 'Launch Orientation 3', 
				 views: [ 'View1' ] ], 
    'app32c' : [ name: 'Launch Orientation 4', 
				 views: [ 'View1' ] ], 
    'app32d' : [ name: 'Launch Orientation 5', 
				 views: [ 'View1' ] ], 
    'app32e' : [ name: 'Launch Orientation 6', 
				 views: [ 'View1' ] ], 

    'app40'  : [ name: 'Action Def 1', 
				 views: [ 'View1' ] ], 
    'app40a' : [ name: 'Action Def 2', 
				 views: [ 'View1' ] ], 
    'app40b' : [ name: 'Action Def 3', 
				 views: [ 'View1' ] ], 
    'app40c' : [ name: 'Action Def 4', 
				 views: [ 'View1' ] ], 
    'app40d' : [ name: 'Action Def 5', 
				 views: [ 'View1' ] ], 
    'app40e' : [ name: 'Action Def 6', 
				 views: [ 'View1' ] ], 
    'app40f' : [ name: 'Action Def 7', 
				 views: [ 'View1' ] ], 
  ];

  def androidFileMap = [
    'app27'  : [ name: 'Pinch Gesture', 
				 views: [ 'PinchGesture' ], 
				 layouts: [ 'main' ] ],
    'app27a' : [ name: 'Pinch Gesture 2', 
				 views: [ 'PinchGesture2' ], 
				 layouts: [ 'main' ] ],
    'app27b' : [ name: 'Pinch Gesture 3', 
				 views: [ 'PinchGesture3' ], 
				 layouts: [ 'main' ] ],
    'app28'  : [ name: 'Rotatation Gesture', 
				 views: [ 'RotatationGesture' ], 
				 layouts: [ 'main' ] ],
    'app28a' : [ name: 'Rotatation Gesture 2', 
				 views: [ 'RotatationGesture2' ], 
				 layouts: [ 'main' ] ],
    'app28b' : [ name: 'Rotatation Gesture 3', 
				 views: [ 'RotatationGesture3' ], 
				 layouts: [ 'main' ] ],
    'app29'  : [ name: 'Long Press Gesture', 
				 views: [ 'LongPressGesture' ], 
				 layouts: [ 'main' ] ],
    'app29a' : [ name: 'Long Press Gesture 2', 
				 views: [ 'LongPressGesture2' ], 
				 layouts: [ 'main' ] ],

    'app32'  : [ name: 'Launch Orientation 1', 
				 views: [ 'LaunchOrientation1' ], 
				 layouts: [ 'main' ] ],
    'app32a' : [ name: 'Launch Orientation 2', 
				 views: [ 'LaunchOrientation2' ], 
				 layouts: [ 'main' ] ],  
    'app32b' : [ name: 'Launch Orientation 3', 
				 views: [ 'LaunchOrientation3' ], 
				 layouts: [ 'main' ] ],  
    'app32c' : [ name: 'Launch Orientation 4', 
				 views: [ 'LaunchOrientation4' ], 
				 layouts: [ 'main' ] ],  
    'app32d' : [ name: 'Launch Orientation 5', 
				 views: [ 'LaunchOrientation5' ], 
				 layouts: [ 'main' ] ],  
    'app32e' : [ name: 'Launch Orientation 6', 
				 views: [ 'LaunchOrientation6' ], 
				 layouts: [ 'main' ] ],  

    'app40'  : [ name: 'Action Def 1', 
				 views: [ 'ActionDef1' ], 
				 layouts: [ 'main' ] ],
    'app40a' : [ name: 'Action Def 2', 
				 views: [ 'ActionDef2' ], 
				 layouts: [ 'main' ] ],
    'app40b' : [ name: 'Action Def 3', 
				 views: [ 'ActionDef3' ], 
				 layouts: [ 'main' ] ],
    'app40c' : [ name: 'Action Def 4', 
				 views: [ 'ActionDef4' ], 
				 layouts: [ 'main' ] ],
    'app40d' : [ name: 'Action Def 5', 
				 views: [ 'ActionDef5' ], 
				 layouts: [ 'main' ] ],
    'app40e' : [ name: 'Action Def 6', 
				 views: [ 'ActionDef6' ], 
				 layouts: [ 'main' ] ],
    'app40f' : [ name: 'Action Def 7', 
				 views: [ 'ActionDef7' ], 
				 layouts: [ 'main' ] ],

  ];


  
  @Test(timeout=80000L)
  public void test_app27_iOS() {
    test_iOS('app27')
  }

  @Test(timeout=80000L)
  public void test_app27_Android() {
    test_Android('app27')
  }

  //@Test(timeout=80000L)
  public void test_app27a_iOS() {
    test_iOS('app27a')
  }

  //@Test(timeout=80000L)
  public void test_app27a_Android() {
    test_Android('app27a')
  }

  @Test(timeout=80000L)
  public void test_app27b_iOS() {
    test_iOS('app27b')
  }

  @Test(timeout=80000L)
  public void test_app27b_Android() {
    test_Android('app27b')
  }

  @Test(timeout=80000L)
  public void test_app28_iOS() {
    test_iOS('app28')
  }

  @Test(timeout=80000L)
  public void test_app28_Android() {
    test_Android('app28')
  }

  //@Test(timeout=80000L)
  public void test_app28a_iOS() {
    test_iOS('app28a')
  }

  //@Test(timeout=80000L)
  public void test_app28a_Android() {
    test_Android('app28a')
  }

  @Test(timeout=80000L)
  public void test_app28b_iOS() {
    test_iOS('app28b')
  }

  @Test(timeout=80000L)
  public void test_app28b_Android() {
    test_Android('app28b')
  }

  @Test(timeout=80000L)
  public void test_app29_iOS() {
    test_iOS('app29')
  }

  @Test(timeout=80000L)
  public void test_app29_Android() {
    test_Android('app29')
  }

  @Test(timeout=80000L)
  public void test_app29a_iOS() {
    test_iOS('app29a')
  }

  @Test(timeout=80000L)
  public void test_app29a_Android() {
    test_Android('app29a')
  }





  @Test(timeout=80000L)
  public void test_app32_iOS() {
    test_iOS('app32')
  }

  @Test(timeout=80000L)
  public void test_app32_Android() {
    test_Android('app32')
  }

  @Test(timeout=80000L)
  public void test_app32a_iOS() {
    test_iOS('app32a')
  }

  @Test(timeout=80000L)
  public void test_app32a_Android() {
    test_Android('app32a')
  }

  @Test(timeout=80000L)
  public void test_app32b_iOS() {
    test_iOS('app32b')
  }

  @Test(timeout=80000L)
  public void test_app32b_Android() {
    test_Android('app32b')
  }

  @Test(timeout=80000L)
  public void test_app32c_iOS() {
    test_iOS('app32c')
  }

  @Test(timeout=80000L)
  public void test_app32c_Android() {
    test_Android('app32c')
  }

  @Test(timeout=80000L)
  public void test_app32d_iOS() {
    test_iOS('app32d')
  }

  @Test(timeout=80000L)
  public void test_app32d_Android() {
    test_Android('app32d')
  }

  @Test(timeout=80000L)
  public void test_app32e_iOS() {
    test_iOS('app32e')
  }

  @Test(timeout=80000L)
  public void test_app32e_Android() {
    test_Android('app32e')
  }


  @Test(timeout=80000L)
  public void test_app40_iOS() {
    test_iOS('app40')
  }

  @Test(timeout=80000L)
  public void test_app40_Android() {
    test_Android('app40')
  }

  @Test(timeout=80000L)
  public void test_app40a_iOS() {
    test_iOS('app40a')
  }

  @Test(timeout=80000L)
  public void test_app40a_Android() {
    test_Android('app40a')
  }

  @Test(timeout=80000L)
  public void test_app40b_iOS() {
    test_iOS('app40b')
  }

  @Test(timeout=80000L)
  public void test_app40b_Android() {
    test_Android('app40b')
  }

  //@Test(timeout=80000L)
  public void test_app40c_iOS() {
    test_iOS('app40c')
  }

  //@Test(timeout=80000L)
  public void test_app40c_Android() {
    test_Android('app40c')
  }

  @Test(timeout=80000L)
  public void test_app40d_iOS() {
    test_iOS('app40d')
  }

  @Test(timeout=80000L)
  public void test_app40d_Android() {
    test_Android('app40d')
  }

  @Test(timeout=80000L)
  public void test_app40e_iOS() {
    test_iOS('app40e')
  }

  @Test(timeout=80000L)
  public void test_app40e_Android() {
    test_Android('app40e')
  }

  @Test(timeout=80000L)
  public void test_app40f_iOS() {
    test_iOS('app40f')
  }

  @Test(timeout=80000L)
  public void test_app40f_Android() {
    test_Android('app40f')
  }



}