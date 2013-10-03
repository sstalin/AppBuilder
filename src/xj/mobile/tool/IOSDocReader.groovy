
package xj.mobile.tool

import org.htmlcleaner.*
import org.ho.yaml.Yaml
 

/*
 *
 *  Retrieve the iOS Library from Apple 
 *
 *  Prerequisite: 
 *   1. Use Chrome to access http://developer.apple.com/library/ios/navigation/#section=Frameworks
 *      Select 'Framework' section, search 'framework
 *   2. Select document table, and open View | Developer | Developer Tools
 *      Select 'Element' tab, select root element, copy and paste to a file
 *   3. Save file as: output/api/ios/AllFrameworks.html
 *
 * Run:
 *   bin/iosdocreader.sh -lib
 *      fectch and analyze top level framework doc
 *      requires: output/api/ios/AllFrameworks.html
 *      output:   output/api/ios/AllFrameworks.xml
 *                output/api/ios/AllFrameworkList.yml
 *                output/api/ios/${fname}_Framework.xml
 *   bin/iosdocreader.sh -fetch
 *      analyze framework file, fetch and clean class API page 
 *      requires: output/api/ios/${fname}_Framework.xml
 *      output:   lib/ios/${fname}_FrameworkRefs.yml
 *                output/ios/${fname}/${name}.xml
 *   bin/iosdocreader.sh -analyze
 *      analyze class API doc
 *      requires: output/ios/${fname}/${name}.xml
 *      output:   lib/ios/${fname}_Relation.yml
 *                lib/ios/${fname}/${name}_Def.yml
 *
 * Files:
 *   Intermediate files in output/api/ios
 *      cleaned xml files based on html doc fetched from web
 *   Result files in lib/api/ios
 *      result of analysis of API doc 
 *
 */
class IOSDocReader { 

  //
  // utilities 
  //

  static readPage(address) { 
	try { 
	  def cleaner = new HtmlCleaner()
	  def node = cleaner.clean(address)
 
	  // Convert from HTML to XML
	  def props = cleaner.getProperties()
	  def serializer = new SimpleXmlSerializer(props)
	  def xml = serializer.getXmlAsString(node)
 
	  // Parse the XML into a document we can work with
	  return new XmlSlurper(false,false).parseText(xml)
	} catch (IOException e) { 
	  println e
	  return null
	}
  }

  static base = 'output/api/ios'
  static output_base = 'lib/api/ios'

  static writeXml(page, fname) { 
	def d1= new File(base + '/' + fname).parentFile
	d1.mkdirs()
	def fw = new FileWriter(base + '/' + fname)
	groovy.xml.XmlUtil.serialize(page, fw)
	fw.close()
  }

  static readXml(fname) { 
	try { 
	  new XmlSlurper(false,false).parse(new File(base + '/' + fname))
	} catch (IOException e) { 
	  println e
	  return null
	}
  }

  //
  //  Main section 
  //

  static String PROG_NAME = 'API Doc Reader [iOS] v0.02'

  static api_base = 'http://developer.apple.com/library/ios/'
  static ref_root = 'navigation/#section=Resource%20Types&topic=Reference'

  //static uikit_root = '#documentation/UIKit/Reference/'
  static uikit_root = 'documentation/UIKit/Reference/'
  static uikit_framework = 'UIKit_Framework/_index.html'

  // 'http://developer.apple.com/library/ios/#documentation/UIKit/Reference/UILabel_Class/Reference/UILabel.html'

  static main(args) { 
    println PROG_NAME
    
    boolean lib = false
    boolean fetch = false
    boolean analyze = false 

    for (a in args) { 
      if (a.contains('-lib')) lib = true
      if (a.contains('-fetch')) fetch = true
      if (a.contains('-analyze')) analyze = true
    }

	def outd = new File(output_base)
	outd.mkdirs()

	args = args - [ '-lib', '-fetch', '-analyze' ]

    if (lib) { 
      fetchLibs(args)
    }
    if (fetch) { 
      fetchAPI(args)
    }
    if (analyze) { 
      analyzeAPI(args)
    }

	if (!lib && !fetch && !analyze) { 
	  test()
	}
  }

  /*
   *  Retrieve the iOS Library from Apple 
   *  Prerequisite: 
   *   1. Use Chrome to access http://developer.apple.com/library/ios/navigation/#section=Frameworks
   *      Select 'Framework' section, search 'framework
   *   2. Select document table, and open View | Developer | Developer Tools
   *      Select 'Element' tab, select root element, copy and paste to a file
   *   3. Save file as: output/api/ios/AllFrameworks.html
   *
   *   Fetch the top reference page of each framework in AllFrameworks.html.
   *     - convert AllFrameworks.html to AllFrameworks.xml.
   *   A summary of frameworks are stored in AllFrameworkList.yml.
   *   
   */
  static fetchLibs(args) { 
    def page = readPage(new File(base + '/AllFrameworks.html'))
    assert page.head.title == 'iOS Developer Library'
    println "Write AllFrameworks.xml"
    writeXml(page, 'AllFrameworks.xml')

    def allLibs = [:]

    page.'**'.findAll { it.@class == 'docTitle' }.each { 
      String title = it.a.text()
      String href = it.a.@href
      String href2 = stripHref(href)
      def fnode = it.'..'.'..'.'**'.find { it.@class == 'frameworks' }
      def fname = fnode.text()
      println title + '\t' + fname
      println '\t' + href
      println '\t' + href2

      allLibs[fname] = [name : fname, title: title, href: href2]

      fetchFrameworkRef(href2, title, fname)
    }

    //new File(base + '/AllFrameworkList.yaml').text = Yaml.dump(allLibs)    
    println "Write AllFrameworkList.yml"
    Yaml.dump(allLibs, new File(base + '/AllFrameworkList.yml'), true)    
  }

  // remove leading '../' and trailing '#...'
  static String stripHref(href) { 
    if (href) { 
      int i = href.indexOf('#')
      if (i >= 0) i--
      href = href[3 .. i]
    }
    return href
  }

  static String hrefUp(href) { 
    if (href) { 
      int i = href.lastIndexOf('/')
      href = href[0 .. i-1]
      i = href.lastIndexOf('/')
      href = href[0 .. i]
    }
    return href
  }

  static fetchFrameworkRef(url, title, name) { 
    def address = api_base + url
    def page = readPage(address.toURL())
	if (page) { 
	  println '  Fetch: ' + page.head.title
	  writeXml(page, "${name}_Framework.xml")
	  if (page.head.title != title) { 
		println "  !!! Page title mismatch: ${page.head.title}"
	  }
	}
  }

  /*
   * fetch all the files for each class/protocol in the framework
   */

  static fetchAPI(args) { 
    println '=== fetch framework API'

	if (args.size() > 0) { 
	  args.each { analyzeFrameworkRef(it, true) }
	} else { 
	  analyzeFrameworkRef('UIKit', true)
	}
  }

  static analyzeFrameworkRef(fname, fetch = true) { 
    println "=== Analyze  Framework ${fname}"
    def page = readXml("${fname}_Framework.xml")

    def allLibs = Yaml.load(new File(base + '/AllFrameworkList.yml').text)    
    def href_base = hrefUp(allLibs[fname].href)
    println href_base

    def node1 = page.'**'.find { it.@class == 'FrameworkPath' }
    //def text = node1.parent().parent().parent().'**'.find { it.@class == 'zSharedSpecBoxHeadList' } 
    String frameworkPath = node1.'..'.'..'.'..'.'**'.find { it.@class == 'zSharedSpecBoxHeadList' } 
    println node1
    println frameworkPath

    node1 = page.'**'.find { it.@class == 'HeaderFileDirectory' }
    //text = node1.parent().parent().parent().'**'.find { it.@class == 'zSharedSpecBoxHeadList' } 
    String headerDir = node1.'..'.'..'.'..'.'**'.find { it.@class == 'zSharedSpecBoxHeadList' } 
    println node1
    println headerDir

    def sectionTitles = [ 'Class', 'Protocol', 'Other' ]
    String prefix = 'UI'

    def refs = [ Class: [], Protocol: [], Other: []]

    def sections = page.'**'.findAll { it.@class == 'collectionHead' }
    sections.each { sec -> 
      println '==== ' + sec
      String sectionName = sec.text().split()[0]
      sec.parent().'**'.findAll {  it.@class == 'forums' }.each {  
		String href = it.a.@href
		String href2 = stripHref(href)
		String name = it.a.toString().trim()
		println '    ' + name
		println '\t' + href
		println '\t' + href2

		if (sectionName in sectionTitles) { 
		  //if (name.startsWith(prefix)) { 
		  refs[sectionName] << [ name: name, href: href2 ]
		  //}
		}
      }
    }    

    println '==== Classes'
    println refs['Class'].join('\n')
    println '==== Protocols'
    println refs['Protocol'].join('\n')

    Yaml.dump(refs, new File(output_base + "/${fname}_FrameworkRefs.yml"), true)

    if (fetch) { 
	  [ 'Class', 'Protocol', 'Other' ].each { sec->
		refs[sec].each { 
		  String href = href_base + it.href
		  println "  fetch: ${it.name} ${href}"
		  fetchClassRef(it.name, fname, href)
		}
	  }
    }

  }

  static fetchClassRef(name, fname, href) { 
    def address = api_base + href
    def page = readPage(address.toURL())
	if (page) { 
	  println page.head.title

	  assert page.head.title.toString().startsWith(name)

	  writeXml(page, "${fname}/${name}.xml")
	}
  }



  /*
   * analyze class API from cached files 
   */

  static analyzeAPI(args) { 
	if (args.size() > 0) { 
	  args.each { analyzeFrameworkAPI(it) }
	} else { 
	  analyzeFrameworkAPI('UIKit')
	}
  }

  // relationships, within a framework  
  static relInherits = [:]
  static relConforms = [:]
  static relHas = [:]

  static analyzeFrameworkAPI(String fname) { 
	relInherits = [:]
	relConforms = [:]
	relHas = [:]

    def refs = Yaml.load(new File(output_base + "/${fname}_FrameworkRefs.yml").text)
	[ 'Class', 'Protocol', 'Other' ].each { sec->
	  refs[sec].each { 
		String title = it.name
		title = title.replace('\u00A0', ' ')

		if (!title.startsWith('Revision') || !title.endsWith('History')) 
		  //if (title != 'Revision History')
		  analyzeClassRef(fname, title, sec)
	  }
	}
	
	// writes relationships
    Yaml.dump([ 'inherits': relInherits, 'conforms': relConforms, 'has': relHas ], 
			  new File(output_base + "/${fname}_Relation.yml"), true)
	
  }

  static test() { 
	analyzeClassRef('UIKit', 'UIView', 'Class')

    //analyzeClassRef('UIKit', 'UIButton', 'Class')
    //analyzeClassRef('UIKit', 'UIControl', 'Class')
  }

  static propertyPattern = ~/@property\s*\([\w, =]+\)\s*(\w+)\s*(\*?)\w*/

  static methodPattern1 = ~/(\-|\+)\s*\(((?:const)?)\s*(\w+)\s*(<[\w\s,]+>)?\s*(\*?)\)\s*(\w+)(:\s*\(((?:const)?)\s*(\w+)\s*(<[\w\s,]+>)?\s*(\*?)\)\s*(\w+))?\s*/
  static methodPattern2 = ~/(\w+):\s*\(((?:const)?)\s*(\w+)\s*(<[\w\s,]+>)?\s*(\*?)\)\s*(\w+)/
  static methodPattern3 = ~/,\s*(...)\s*$/

  static matchMethod(String m) { 
    def result = [:]
    def args = []
    boolean varArgs = false
	
	if (m) { 
	  m = m.replace('\u00A0', ' ')
	}

    def match = m =~ methodPattern1
	

	//if (!match || match[0].size < 1) { 
	if (match == null || match.size() == 0 || match[0].size() == 0) { 
	  println "!!!!! no match"
	  return null
	}


	if (match != null && match.size() > 0 && match[0].size() > 0) { 
	  for (j in 0 .. (match[0].size() - 1)) { 
		println "\t  == match[0][${j}]: ${match[0][j]}"
	  }


	  //if(match != null && match[0].size() >= 1) {
      result['isInstance'] = match[0][1] == '-'
      result['name'] = match[0][6]
      result['type'] = [ name: match[0][3],
						 delegate: match[0][4],
						 isConst: match[0][2] == 'const', 
						 isRef: match[0][5] == '*']
      if (match[0][7]) { 
		args << [ param: match[0][12], 
				  type: [ name: match[0][9], 
						  delegate: match[0][10],
						  isConst: match[0][8] == 'const', 
						  isRef: match[0][11] == '*']] 
      }

      int len = match[0][0].length()    
      if (len < m.length()) { 
		String mtail = m[len .. -1]
		match = mtail =~ methodPattern2
		if(match) {
		  for (int j = 0; j < match.count; j++) { 
			args << [ name: match[j][1], 
					  param: match[j][6], 
					  type: [ name: match[j][3], 
							  delegate: match[j][4],
							  isConst: match[j][2] == 'const', 
							  isRef: match[j][5] == '*']] 
		  }
		}

		match = mtail =~ methodPattern3
		if (match && match[0].size > 1) {
		  varArgs = true
		}
      }
    }
    result['isVarArgs'] = varArgs
    result['args'] = args
    return result
  }

  static typedefPattern = ~/typedef\s*(\w+)\s*(\w+)\s*;/
  static typedefEnumPattern = ~/};[\r\n]+typedef\s*((?:NS|UI)\w+)\s*((?:NS|UI)\w+)\s*/

  static enumPattern1 = ~/}\s*(\w+)\s*;/
  static enumPattern2 = ~/(\w+)(?:\s*=\s*(\d+))?\s*,/

  static analyzeClassRef(fname, name, sec) { 
	println "=== Analyze ${name}"
	def filename = fname ? "${fname}/${name}.xml" : "${name}.xml"
	def page = readXml(filename)
	
	if (page == null) return

	assert page.head.title.toString().startsWith(name)

	def classDef = [:]

	def specbox = page.'**'.find{ it.@class == 'specbox' }
	specbox.'**'.findAll { it.name() == 'tr' }.each { 
	  def node = it.'**'.find{ it.text() == 'Inherits from' }
	  if (node) { 
		println '-- Inherits from --'
		def inherit = []
		it.'**'.find{ it.@class = 'zSharedSpecBoxHeadList' }.'**'.findAll{ it.name() == 'a' }.each { 
		  println '\t' + it.text()
		  inherit << it.text()
		} 
		classDef['inherit'] = inherit

		if (sec != 'Other') { 
		  if (inherit.size() > 0) { 
			relInherits[name] = inherit[0]
		  }
		}
	  } else { 
		node = it.'**'.find{ it.text() == 'Conforms to' }
		if (node) { 
		  println '-- Conforms to --'
		  def conform = []
		  it.'**'.find{ it.@class = 'zSharedSpecBoxHeadList' }.'**'.findAll{ it.name() == 'a' }.each { 
			println '\t' + it.text()
			conform << it.text()
		  } 
		  classDef['conform'] = conform

		  if (sec != 'Other') { 
			relConforms[name] = conform
		  }
		}
	  }
	}

	println '-- properties --'

	def properties = [:]
	page.'**'.findAll{ it.@class == 'api propertyObjC' }.each { 
	  def declaration = it.'**'.find { d -> d.@class == 'declaration' }
	  def pname = it.h3.text()
	  def explanation = it.'**'.find { d -> d.@class == 'abstract' }?.text()
	  boolean readonly = explanation?.contains('(read-only)')
	  String decl = declaration.text()
	  decl = decl.replaceAll('\u00A0', ' ')
	  decl = decl.replaceAll('<em>', '')
	  decl = decl.replaceAll('</em>', '')

	  //println '\t' + pname + ' ' + decl

	  def match = decl =~ propertyPattern
	  if(match && match[0].size > 1) {
		def ptype = match[0][1]
		def ref = match[0][2]
		println '\t' + pname + ' : ' + ptype + '  ' + ref
		def pdef = [name: pname, type: ptype, ref: ref.toString(), explanation: explanation, readonly: readonly ? 'true' : 'false' ]
		properties[pname] = pdef
	  }
	}
	classDef['properties'] = properties

	def methods = [:]
	def has = []
	println '-- class methods --'
	page.'**'.findAll{ it.@class == 'api classMethod' }.each { 
	  def declaration = it.'**'.find { d -> d.@class == 'declaration' }
	  def mname = it.h3
	  println '    ' + mname + ' : ' + declaration.text()

	  def mdef = matchMethod(declaration.text()) 
	  if (mdef) { 
		println '\t' + mdef['name'] + ' : ' + mdef 
		methods[mdef['name']] = mdef
	  } else { 
		println "!!!!! mdef == null"
	  }
	}

	println '-- instance methods --'

	page.'**'.findAll{ it.@class == 'api instanceMethod' }.each { 
	  def declaration = it.'**'.find { d -> d.@class == 'declaration' }
	  def mname = it.h3
	  println '    ' + mname + ' : ' + declaration.text()

	  def mdef = matchMethod(declaration.text()) 
	  if (mdef) { 
		println '\t' + mdef['name'] + ' : ' + mdef 
		methods[mdef['name']] = mdef

		if (mdef['name'].startsWith('add') && 
			mdef.args.size() == 1) { 
		  has << [ mdef['name'], mdef.args[0].type.name ]
		}

	  } else { 
		println "!!!!! mdef == null"
	  }
	}

	classDef['methods'] = methods
	if (sec != 'Other' && has.size() > 0) { 
	  relHas[name] = has
	}


	def constSec
	if (sec == 'Other') { 
	  constSec = page.body //'**'.findAll { it.name() == 'section' } 
	} else { 
	  constSec = page.'**'.find { it.@class == 'zObjCConstants' }
	}
	if (constSec) { 
	  println '--- Constants  ---'

	  def constants = [:]

	  boolean isEnum = false, isTypedef = false, typedefFollowEnum = false
	  def typeValues = []
	  def constName = null
	  def typeName = null
	  def typeAlias = null
	  constSec.'**'.findAll { it.name() == 'h3' || 
		it.@class == 'declaration' || it.@class == 'termdef' }.each { 
		if (it.name() == 'h3') { 
		  constName = it.text()
		  isEnum = false
		  isTypedef = false
		  typedefFollowEnum = false
		  typeName = null
		  typeAlias = null
		  typeValues = []
		} else if (it.@class == 'declaration') { 
		  String text = it.text().trim()
		  if (text.startsWith('typedef enum {')) { 
			isEnum = true
			isTypedef = true
		  } else if (text.startsWith('enum {')) { 
			isEnum = true
		  } else if (text.startsWith('typedef')) { 
			isTypedef = true
		  }
		  if (isEnum) { 
			def match = text =~ enumPattern1
			if (match) { 
			  typeName = match[0][1]
			} else { 
			  match = text =~ typedefEnumPattern
			  if (match) { 
				isTypedef = true
				typedefFollowEnum = true
				typeName = match[0][2]
				typeAlias = match[0][1]
			  }
			}
		  } else if (isTypedef) { 
		  	def match = text =~ typedefPattern
			if (match) { 
			  typeName = match[0][2]
			  typeAlias = match[0][1]
			}
		  }
		  
		  typeValues = it.'*'.findAll { it.name() == 'a' }.collect { it.text() }
		  if (typedefFollowEnum) { 
			//typeName = typeValues[-1]
			//typeAlias = typeValues[-2]
			if (typeName) typeValues.remove(typeName)
			if (typeAlias) typeValues.remove(typeAlias)
		  }
		  
		  typeValues.removeAll([ 'NSUIntegerMax', 'NSUIntegerMin', 'NSIntegerMax', 'NSIntegerMin' ])


		  println "\t${constName} ${isEnum ? '[enum] ' : ''}${isTypedef ? '[typedef] ' : ''}: ${typeValues}"

		  constants[constName] = [name: typeName, 
								  isEnum: isEnum, isTypedef: isTypedef, 
								  values: isEnum ? typeValues : null]
		  if (typeAlias) { 
			constants[constName]['aliasOf'] = typeAlias
		  }
		} else if (it.@class == 'termdef') { 
		  def defValues = it.'**'.findAll{ it.@class == 'jump constantName' }.collect { it.text() }
		  constants[constName].defValues = defValues
		}

	  }

	  // 
	  constants.each { cname, cdef -> 
		if (!cdef.values && cdef.name) { 
		  def entry = null
		  constants.each { cname2, cdef2 -> 
			if (cdef.name == 'UI' + cname2.replaceAll(' ', '') &&
				cdef2.values != null) {  
			  entry = cdef2
			}
		  }
		  if (entry) { 
			cdef.values = entry.values
			cdef.defValues = entry.defValues
			cdef.isEnum = entry.isEnum
		  }
		}
	  }

	  classDef['constants'] = constants

	  /*
	  constSec.'**'.findAll { it.@class == 'declaration' }.each { decl ->
		println '=== const decl ==='
		boolean isEnum = false
		def typeValues = []
		def typeName = null

		decl.'**'.findAll { it.name() == 'code' }.each {	  
		  println it
		  String text = it.text()
		  if (text.startsWith('typedef enum {')) { 
			isEnum = true
		  } else if (isEnum) { 
			def match = text =~ enumPattern1
			if (match) { 
			  typeName = match[0][1]
			} else { 
			  match = text =~ enumPattern2
			  if (match) { 
				typeValues << [ name: match[0][1], value: match[0][2] ]
			  }
			}
		  }
		}
		println "\t${typeName}: ${typeValues}"
	  }
	  */
	}

	def d1= new File(output_base + '/' + fname)
    d1.mkdirs()
	Yaml.dump(classDef, new File(output_base + "/${fname}/${name.replaceAll(' ', '_')}_Def.yml"), true)

  }



}