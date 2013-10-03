
@Grab(group='net.sourceforge.nekohtml', module='nekohtml', version='1.9.14')

class DocReader {  

  static String PROG_NAME = 'Doc Reader v0.01'

  static String base = 'test/lang'

  static android = 'http://developer.android.com/reference/packages.html'
  static ios = 'http://developer.apple.com/library/ios/navigation/#section=Resource%20Types&topic=Reference'

  static main(args) { 
    println PROG_NAME

    def slurper = new XmlSlurper(new org.cyberneko.html.parsers.SAXParser())
    
    [ android, ios ].each { site -> 
      def url = new URL(site)
      url.withReader { reader ->
		def html = slurper.parse(reader)
	
		println 'Accessing: ' + site
		//println 'HTML: ' + html
      
		//println html.depthFirst().grep{ it.name() == 'TITLE'}
		println 'Title: ' + html.HEAD.TITLE

      }
    }

  }

}