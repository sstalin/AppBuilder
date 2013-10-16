package xj.mobile.test

import org.junit.*
import static org.junit.Assert.*

import xj.mobile.Main

class AppBuilderTest5 extends AppBuilderTest {  

  def iOSFileMap = [
	'app20'  : [ name: 'Graphics 1', 
				 views: [ 'View1' ],
				 custom: [ 'CanvasView1' ] ], 
	'app20a' : [ name: 'Graphics 2', 
				 views: [ 'View1' ],
				 custom: [ 'CanvasView1' ] ], 
	'app20b' : [ name: 'Graphics 3', 
				 views: [ 'View1' ],
				 custom: [ 'CanvasView1' ] ], 

	'app21'  : [ name: 'Orientation', 
				 views: [ 'View1' ] ], 
	'app21a' : [ name: 'Orientation 2', 
				 views: [ 'View1' ] ], 
	'app21b' : [ name: 'Orientation 3', 
				 views: [ 'View1' ] ], 
	'app21c' : [ name: 'Orientation 4', 
				 views: [ 'View1' ] ], 
	'app21d' : [ name: 'Orientation 5', 
				 views: [ 'View1' ] ], 
	'app21e' : [ name: 'Orientation 6', 
				 views: [ 'View1' ] ], 
	'app21f' : [ name: 'Orientation 7', 
				 views: [ 'View1' ] ], 

    'app23'  : [ name: 'Rotate and Shake', 
				 views: [ 'View1' ] ], 
    'app23a' : [ name: 'Rotate and Shake 2', 
				 views: [ 'View1', 'View2' ] ], 

    'app25'  : [ name: 'Tap Gesture', 
				 views: [ 'View1' ] ], 
    'app25a' : [ name: 'Tap Gesture 2', 
				 views: [ 'View1' ] ], 
    'app25aa': [ name: 'Tap Gesture 2a', 
				 views: [ 'View1' ] ], 
    'app25b' : [ name: 'Tap Gesture 3', 
				 views: [ 'View1' ] ], 
    'app25c' : [ name: 'Tap Gesture 4', 
				 views: [ 'View1' ] ], 

    'app26'  : [ name: 'Swipe Gesture', 
				 views: [ 'View1' ] ], 
    'app26a' : [ name: 'Swipe Gesture 2', 
				 views: [ 'View1', 'View2' ] ], 
    'app26b' : [ name: 'Swipe Gesture 3', 
				 views: [ 'View1' ] ], 
    'app26c' : [ name: 'Swipe Fling Gesture 4', 
				 views: [ 'View1' ] ], 
    'app26d' : [ name: 'Drag Gesture', 
				 views: [ 'View1' ] ], 
    'app26da': [ name: 'Pan Gesture', 
				 views: [ 'View1' ] ], 
    'app26db': [ name: 'Scroll Gesture', 
				 views: [ 'View1' ] ], 
  ];

  def androidFileMap = [
	'app20'  : [ name: 'Graphics 1', 
				 views: [ 'Graphics1' ],
				 layout: [ 'main' ], 
				 custom: [ 'CanvasView1' ] ], 
	'app20a'  : [ name: 'Graphics 2', 
				 views: [ 'Graphics2' ],
				 layout: [ 'main' ], 
				 custom: [ 'CanvasView1' ] ], 
	'app20b'  : [ name: 'Graphics 3', 
				 views: [ 'Graphics3' ],
				 layout: [ 'main' ], 
				 custom: [ 'CanvasView1' ] ], 

	'app21'  : [ name: 'Orientation', 
				 views: [ 'Orientation' ], 
				 layouts: [ 'main' ] ],
	'app21a' : [ name: 'Orientation 2', 
				 views: [ 'Orientation2' ], 
				 layouts: [ 'main' ] ],
	'app21b' : [ name: 'Orientation 3', 
				 views: [ 'Orientation3' ], 
				 layouts: [ 'main' ] ],
	'app21c' : [ name: 'Orientation 4', 
				 views: [ 'Orientation4' ], 
				 layouts: [ 'main' ] ],
	'app21d' : [ name: 'Orientation 5', 
				 views: [ 'Orientation5' ], 
				 layouts: [ 'main' ] ],
	'app21e' : [ name: 'Orientation 6', 
				 views: [ 'Orientation6' ], 
				 layouts: [ 'main' ] ],
	'app21f' : [ name: 'Orientation 7', 
				 views: [ 'Orientation7' ], 
				 layouts: [ 'main' ] ],

    'app23'  : [ name: 'Rotate and Shake', 
				 views: [ 'RotateandShake' ], 
				 layouts: [ 'main' ] ],
    'app23a' : [ name: 'Rotate and Shake 2', 
				 views: [ 'RotateandShake2', 'View2' ], 
				 layouts: [ 'main', 'view2' ] ],  

    'app25'  : [ name: 'Tap Gesture', 
				 views: [ 'TapGesture' ], 
				 layouts: [ 'main' ] ],
    'app25a' : [ name: 'Tap Gesture 2', 
				 views: [ 'TapGesture2' ], 
				 layouts: [ 'main' ] ],
    'app25aa': [ name: 'Tap Gesture 2a', 
				 views: [ 'TapGesture2a' ], 
				 layouts: [ 'main' ] ],
    'app25b' : [ name: 'Tap Gesture 3', 
				 views: [ 'TapGesture3' ], 
				 layouts: [ 'main' ] ],
    'app25c' : [ name: 'Tap Gesture 4', 
				 views: [ 'TapGesture4' ], 
				 layouts: [ 'main' ] ],

    'app26'  : [ name: 'Swipe Gesture', 
				 views: [ 'SwipeGesture' ], 
				 layouts: [ 'main' ] ],
    'app26a' : [ name: 'Swipe Gesture 2', 
				 views: [ 'SwipeGesture2', 'View2' ], 
				 layouts: [ 'main', 'view2' ] ],
    'app26b' : [ name: 'Swipe Gesture 3', 
				 views: [ 'SwipeGesture3' ], 
				 layouts: [ 'main' ] ],
    'app26c' : [ name: 'Swipe Fling Gesture 4', 
				 views: [ 'SwipeFlingGesture4' ], 
				 layouts: [ 'main' ] ],
    'app26d' : [ name: 'Drag Gesture', 
				 views: [ 'DragGesture' ], 
				 layouts: [ 'main' ] ],
    'app26da': [ name: 'Pan Gesture', 
				 views: [ 'PanGesture' ], 
				 layouts: [ 'main' ] ],
    'app26db': [ name: 'Scroll Gesture', 
				 views: [ 'ScrollGesture' ], 
				 layouts: [ 'main' ] ],

  ];
  

  @Test(timeout=80000L)
  public void test_app20_iOS() {
    test_iOS('app20')
  }

  @Test(timeout=80000L)
  public void test_app20_Android() {
    test_Android('app20')
  }

  @Test(timeout=80000L)
  public void test_app20a_iOS() {
    test_iOS('app20a')
  }

  @Test(timeout=80000L)
  public void test_app20a_Android() {
    test_Android('app20a')
  }

  @Test(timeout=80000L)
  public void test_app20b_iOS() {
    test_iOS('app20b')
  }

  @Test(timeout=80000L)
  public void test_app20b_Android() {
    test_Android('app20b')
  }

  @Test(timeout=80000L)
  public void test_app21_iOS() {
    test_iOS('app21')
  }

  @Test(timeout=80000L)
  public void test_app21_Android() {
    test_Android('app21')
  }

  @Test(timeout=80000L)
  public void test_app21a_iOS() {
    test_iOS('app21a')
  }

  @Test(timeout=80000L)
  public void test_app21a_Android() {
    test_Android('app21a')
  }

  @Test(timeout=80000L)
  public void test_app21b_iOS() {
    test_iOS('app21b')
  }

  @Test(timeout=80000L)
  public void test_app21b_Android() {
    test_Android('app21b')
  }

  @Test(timeout=80000L)
  public void test_app21c_iOS() {
    test_iOS('app21c')
  }

  @Test(timeout=80000L)
  public void test_app21c_Android() {
    test_Android('app21c')
  }

  @Test(timeout=80000L)
  public void test_app21d_iOS() {
    test_iOS('app21d')
  }

  //@Test(timeout=80000L)
  public void test_app21d_Android() {
    test_Android('app21d')
  }

  @Test(timeout=80000L)
  public void test_app21e_iOS() {
    test_iOS('app21e')
  }

  //@Test(timeout=80000L)
  public void test_app21e_Android() {
    test_Android('app21e')
  }

  @Test(timeout=80000L)
  public void test_app21f_iOS() {
    test_iOS('app21f')
  }

  //@Test(timeout=80000L)
  public void test_app21f_Android() {
    test_Android('app21f')
  }

  @Test(timeout=80000L)
  public void test_app23_iOS() {
    test_iOS('app23')
  }

  //@Test(timeout=80000L)
  public void test_app23_Android() {
    test_Android('app23')
  }

  @Test(timeout=80000L)
  public void test_app23a_iOS() {
    test_iOS('app23a')
  }

  //@Test(timeout=80000L)
  public void test_app23a_Android() {
    test_Android('app23a')
  }


  @Test(timeout=80000L)
  public void test_app25_iOS() {
    test_iOS('app25')
  }

  @Test(timeout=80000L)
  public void test_app25_Android() {
    test_Android('app25')
  }

  @Test(timeout=80000L)
  public void test_app25a_iOS() {
    test_iOS('app25a')
  }

  @Test(timeout=80000L)
  public void test_app25a_Android() {
    test_Android('app25a')
  }

  @Test(timeout=80000L)
  public void test_app25aa_iOS() {
    test_iOS('app25aa')
  }

  @Test(timeout=80000L)
  public void test_app25aa_Android() {
    test_Android('app25aa')
  }

  @Test(timeout=80000L)
  public void test_app25b_iOS() {
    test_iOS('app25b')
  }

  @Test(timeout=80000L)
  public void test_app25b_Android() {
    test_Android('app25b')
  }

  @Test(timeout=80000L)
  public void test_app25c_iOS() {
    test_iOS('app25c')
  }

  @Test(timeout=80000L)
  public void test_app25c_Android() {
    test_Android('app25c')
  }

  @Test(timeout=80000L)
  public void test_app26_iOS() {
    test_iOS('app26')
  }

  @Test(timeout=80000L)
  public void test_app26_Android() {
    test_Android('app26')
  }

  @Test(timeout=80000L)
  public void test_app26a_iOS() {
    test_iOS('app26a')
  }

  @Test(timeout=80000L)
  public void test_app26a_Android() {
    test_Android('app26a')
  }

  @Test(timeout=80000L)
  public void test_app26b_iOS() {
    test_iOS('app26b')
  }

  @Test(timeout=80000L)
  public void test_app26b_Android() {
    test_Android('app26b')
  }

  @Test(timeout=80000L)
  public void test_app26c_iOS() {
    test_iOS('app26c')
  }

  @Test(timeout=80000L)
  public void test_app26c_Android() {
    test_Android('app26c')
  }

  @Test(timeout=80000L)
  public void test_app26d_iOS() {
    test_iOS('app26d')
  }

  @Test(timeout=80000L)
  public void test_app26d_Android() {
    test_Android('app26d')
  }

  @Test(timeout=80000L)
  public void test_app26da_iOS() {
    test_iOS('app26da')
  }

  @Test(timeout=80000L)
  public void test_app26da_Android() {
    test_Android('app26da')
  }

  @Test(timeout=80000L)
  public void test_app26db_iOS() {
    test_iOS('app26db')
  }

  @Test(timeout=80000L)
  public void test_app26db_Android() {
    test_Android('app26db')
  }


}