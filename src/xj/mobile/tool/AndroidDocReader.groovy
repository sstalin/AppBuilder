
package xj.mobile.tool

import org.htmlcleaner.*
import org.ho.yaml.Yaml
 
/*
 * Run:
 *   groovy bin/androiddocreader.sh lib
 *      fectch and analyze top level framework doc
 *      output: output/api/android/AllPackages.xml
 *              output/api/android/AllPackageList.yml
 *              output/api/android/${name}_package_summary.xml
 *   groovy bin/androiddocreader.sh fetch
 *      analyze framework file, fetch and clean class API page 
 *      output: lib/api/android/${pkgname}_PackageRefs.yml
 *              output/api/android/${pkgname}/${name}.xml
 *   groovy bin/androiddocreader.sh analyze
 *      analyze class API doc
 *      output: lib/api/android/${pkgname}/${name}_Def.yml
 *
 * Files:
 *   Intermediate files in output/api/android
 *      cleaned xml files based on html doc fetched from web
 *   Result files in lib/api/android
 *      result of analysis of API doc 
 *
 */
class AndroidDocReader { 

  ////////////// utilities 

  static readPage(address) { 
    def cleaner = new HtmlCleaner()
    def node = cleaner.clean(address)
 
    // Convert from HTML to XML
    def props = cleaner.getProperties()
    def serializer = new SimpleXmlSerializer(props)
    def xml = serializer.getXmlAsString(node)
 
    // Parse the XML into a document we can work with
    return new XmlSlurper(false,false).parseText(xml)
  }

  static base = 'output/api/android'
  static output_base = 'lib/api/android'

  static writeXml(page, fname) { 
    def d1= new File(base + '/' + fname).parentFile
    d1.mkdirs()
    def fw = new FileWriter(base + '/' + fname)
    groovy.xml.XmlUtil.serialize(page, fw)
    fw.close()
  }

  static readXml(fname) { 
    new XmlSlurper(false,false).parse(new File(base + '/' + fname))
  }

  //////////////

  static String PROG_NAME = 'API Doc Reader [Android] v0.02'

  static api_base = 'http://developer.android.com'

  static main(args) { 
    println PROG_NAME

	def outd = new File(output_base)
	outd.mkdirs()

    boolean lib = false
    boolean fetch = false
    boolean analyze = false 
	boolean testing = false 

    for (a in args) { 
	  if (a.contains('test')) testing = true
      if (a.contains('lib')) lib = true
      if (a.contains('fetch')) fetch = true
      if (a.contains('analyze')) analyze = true
    }

	if (testing) { 
	  test()
	} else { 
	  if (lib) { 
		fetchLibs()
	  }
	  if (fetch) { 
		fetchAPI()
	  }
	  if (analyze) { 
		analyzeAPI()
	  }
	}

  }

  //
  //
  //

  static fetchLibs() { 
    def page = readPage((api_base + '/reference/packages.html').toURL())
    assert page.head.title == 'Package Index | Android Developers'
    println "Write AllPackages.xml"
    writeXml(page, 'AllPackages.xml')

	def allPkgs = [:]

    page.'**'.findAll { it.@class == 'jd-linkcol' }.each { 
      String title = it.a.text()
	  String href = it.a.@href
      println title 
      println '\t' + href

	  allPkgs[title] = href
	  fetchPackageRef(href, title)
	}

    println "Write AllPackageList.yml"
    Yaml.dump(allPkgs, new File(base + '/AllPackageList.yml'), true)    
  }

  static fetchPackageRef(url, name) { 
    def address = api_base + url
    def page = readPage(address.toURL())
    println '  Fetch: ' + page.head.title
    writeXml(page, "${name}_package_summary.xml")
    if (page.head.title != "${name} | Android Developers") { 
      println "  !!! Page title mismatch: ${page.head.title}"
    }
  }


  //
  //
  //

  static fetchAPI() { 
    println '=== fetch package API'

    analyzePackageRef('android.view', true) //false)
    analyzePackageRef('android.widget', true) //false)
    analyzePackageRef('android.app', true) //false)
    analyzePackageRef('android.webkit', true) //false)
  }

  static analyzePackageRef(pkgname, fetch = true) { 
    println "=== Analyze  Package ${pkgname}"
    def page = readXml("${pkgname}_package_summary.xml")
	
	def refs = [:]

	def node1 = page.'**'.find { it.@id == 'jd-content' }
	String kind = ''
	node1.children().each { sec -> 
	  if (sec.name() == 'h2') { 
		println sec.name() + '  ' +  sec.text()
		kind = sec.text()
		refs[kind] = []
	  } else if (sec.name() == 'div' && sec.@class == 'jd-sumtable') { 
		sec.'**'.findAll { it.@class == 'jd-linkcol' }.each { 
		  def node2 = it.a[0]
		  //if (node2 instanceof List) node2 = node2[0]
		  String name = node2.text()
		  String href = node2.@href
		  println kind + ': ' + name
		  println '\t' + href

		  refs[kind] << [ name: name, href: href ]
		}		
	  }
	}

    Yaml.dump(refs, new File(output_base + "/${pkgname}_PackageRefs.yml"), true)

    if (fetch) { 
      refs.each { k, sec -> 
		sec.each { 
		  String href = it.href
		  println "  fetch: ${it.name} ${href}"
		  fetchClassRef(it.name, pkgname, href)
		}
      }
    }
	
  }

  static fetchClassRef(name, pkgname, href) { 
    def address = api_base + href
    def page = readPage(address.toURL())
    println page.head.title

    assert page.head.title == "${name} | Android Developers"

    writeXml(page, "${pkgname}/${name}.xml")
  }


  //
  //
  //

  static analyzeAPI() { 
	analyzeAPI('android.view')
	analyzeAPI('android.widget')
	analyzeAPI('android.app')
	analyzeAPI('android.webkit')
  }
  
  static test() { 
	analyzeClassRef('android.widget', 'SeekBar')
  }

  static analyzeAPI(pkgname) {

    def refs = Yaml.load(new File(output_base + "/${pkgname}_PackageRefs.yml").text)
    refs.Classes.each { 
      analyzeClassRef(pkgname, it.name)
    }

	/*
	analyzeClassRef('android.widget', 'Button')
	analyzeClassRef('android.widget', 'TextView')

	analyzeClassRef('android.view', 'View')
	*/
  }

  static methodPattern1 = ~/(\w+)\s*\(((?:[\w\.]+)(?:,(?:[\w\.]+))*)\)/

  static analyzeClassRef(pkgname, name) { 
	println "=== Analyze class ${pkgname} ${name}"
	def filename = pkgname ? "${pkgname}/${name}.xml" : "${name}.xml"
	def page = readXml(filename)

	assert page.head.title == "${name} | Android Developers"

	def classDef = [:]

	def inheritbox = page.'**'.find{ it.@class == 'jd-inheritance-table' }
	println '-- Inherits from --'
	def inherit = []
	inheritbox.'**'.findAll { it.name() == 'tr' }.each { 
	  println '\t' + it.td[-1].text()
	  inherit << it.td[-1].text()
	}
	inherit = inherit[0 .. -2].reverse()
	classDef['inherit'] = inherit

	// Nested Classes

	// XML Attributes

	def attributes = [:]
	def attrbox = page.'**'.find{ it.name() == 'table' && it.@id == 'lattrs' }
	if (attrbox) { 
	  println '-- XML Attributes --'
	  handleXMLAttributes(attrbox, "${pkgname}.${name}", null, attributes)
	}

	// Inherited XML Attributes
	attrbox = page.'**'.find{ it.name() == 'table' && it.@id == 'inhattrs' }
	if (attrbox) { 
	  println '-- Inherited XML Attributes --'

	  attrbox.tbody.tr.each { 
		if (it.@class.toString().contains('api')) { 
		  String from = it.td.a[-1].text()
		  println '  -- From ' + from 
		  handleXMLAttributes(it.'**'.find { t -> t.name() == 'table'}, "${pkgname}.${name}", from, attributes)
		}
	  }
	}

	classDef['attributes'] = attributes


	// Constants

	// Inherited Constants

	// Fields

	// Inherited Fields

	// Public Constructors

	// Public Methods

	// Inherited Methods

	def d1= new File(output_base + '/' + pkgname)
    d1.mkdirs()
	Yaml.dump(classDef, new File(output_base + "/${pkgname}/${name}_Def.yml"), true)

  }

  static AttributeDef = [
	'android.widget.ProgressBar': [
	  animationResolution: 'int',
	  indeterminate: 'boolean',
	  indeterminateBehavior: 'int',
	  indeterminateDrawable: 'String',
	  indeterminateDuration: 'boolean',
	  indeterminateOnly: 'boolean',
	  interpolator: 'int',		 
	  max: 'int',
	  maxHeight: 'String',
	  maxWidth: 'String',
	  minHeight: 'String',		 
	  minWidth: 'String',		 
	  progress: 'int',
	  progressDrawable: 'String',
	  secondaryProgress: 'int',
	],
  ]


  static handleXMLAttributes(table, classname, from, attributes) { 
	table.tbody.tr.each { 
	  if (it.@class.toString().contains('api')) { 
		def triple = it.td 
		def pname = triple[0].text()
		def shortName = pname
		int i = pname.lastIndexOf(':')
		if (i >= 0) { 
		  shortName = pname.substring(i + 1)
		}
		def method = triple[1].text()
		def explanation = triple[2].text()
		def mname = ''		
		def type = ''
		if (method) { 
		  def match = method =~ methodPattern1
		  if(match && match[0].size > 1) {
			mname = match[0][1]
			type = match[0][2]
		  }
		}
		
		if (!type && AttributeDef[classname]) { 
		  type = AttributeDef[classname][shortName]
		}
		if (!type && from && AttributeDef[from]) { 
		  type = AttributeDef[from][shortName]
		}

		println '\t' + pname + ' ' + method + '   ' + mname + ' ' + type + ' ' + explanation
		  
		if (type) { 
		  def pdef = [name: pname, method: method, methodName: mname, type: type,
					  explanation: explanation]
		  if (from) { 
			pdef['from'] = from
		  }
		  attributes[shortName] = pdef
		}

	  }

	}
  }

}

