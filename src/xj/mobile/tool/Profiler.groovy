//@Grab(group='org.gperfutils', module='gprof', version='0.2.0')
package xj.mobile.tool

import xj.mobile.Main

class Profiler {  

  static USER_CONFIG = 'test/org.properties'
  static String infile = 'app01'


  void test() { 
	def args = [ "-nodate", "test/${infile}.madl", USER_CONFIG ] as String[]
	Main.main(args)  // error if there is an exception
  }

  static void main(String[] args) { 

	new gprof.Profiler().run {
	  new Profiler().test()
	}.prettyPrint()
  }
 
}



