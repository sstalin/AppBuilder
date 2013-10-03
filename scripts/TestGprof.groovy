
@Grab(group='org.gperfutils', module='gprof', version='0.2.0')

class YourApp {
  void start() {
	def foo = new Foo()
	for (int i = 0; i < 100; i++) {
	  foo.doShortTask()
	}
	def bar = new Bar()
	bar.doLongTask()
  }
}

class Bar {
  void doLongTask() {
	for (int i = 0; i < 1000000; i++);
  }
}

class Foo {
  void doShortTask() {
	for (int i = 0; i < 10000; i++);
  }
}

profile {  // or new gprof.Profiler().run {
  new YourApp().start()
}.prettyPrint()