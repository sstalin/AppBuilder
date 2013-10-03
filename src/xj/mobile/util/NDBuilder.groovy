
package xj.mobile.util

import groovy.xml.MarkupBuilder

class NDBuilder extends MarkupBuilder { 
  
  NDBuilder(file) { 
	super(file)
	setDoubleQuotes(true)
  }

  static brushes = [
	groovy : 'GroovyM',
	java : 'Java',
	objc : 'ObjC',
	xml: 'Xml'
  ]

  // lang: a list of language names for using syntaxhighlighter 
  def ndhead(theTitle, lang = null) { 
	head { 
	  meta('http-equiv': 'Content-Type', content: 'text/html; charset=UTF-8')
	  link(rel: 'stylesheet', type: 'text/css', href: 'css/main.css', '')
	  link(rel: 'stylesheet', type: 'text/css', href: 'css/styles2.css', '')

	  //link(rel: 'stylesheet', type: 'text/css', href: 'css/styles.css', '')
	  //link(rel: 'stylesheet', type: 'text/css', href: 'css/Default.css', '')
	  script(language: 'JavaScript', src: 'script/main.js', '') 

	  if (lang) { 
		// syntax highlighter
		link(href: 'css/shCore.css', rel: 'stylesheet', type: 'text/css', '')
		link(href: 'css/shThemeDefault.css', rel: 'stylesheet', type: 'text/css', '')
		//link(href: 'css/shThemeMist.css', rel: 'stylesheet', type: 'text/css', '')

		script(type: 'text/javascript', src: 'script/shCore.js', '') 

		lang?.each { 
		  script(type: 'text/javascript', src: "script/shBrush${brushes[it]}.js", '') 
		}
	  }

	  style(type: 'text/css') {  mkp.yieldUnescaped('.syntaxhighlighter { font-size: 12px !important; }') }
	  title theTitle
	}
  }

  def ndbody(framed, contents) { 
	body(class: (framed ? 'FramedMenuPage' : 'ContentPage'), onLoad: 'NDOnLoad()') { 	
	  script(language: 'JavaScript') { 
		mkp.yieldUnescaped('''<!--
if (browserType) {document.write("<div class=" + browserType + ">");if (browserVer) {document.write("<div class=" + browserVer + ">"); }}// -->''')
	  }

	  
	  if (contents) { 
		contents.delegate = this
		contents()
	  }

	  script(language: 'JavaScript') { 
		mkp.yieldUnescaped('''<!--
if (browserType) {if (browserVer) {document.write("</div>"); }document.write("</div>");}// -->''')
	  }
	}

  }

  // level 1: main topic
  def ndtopic(title, anchor, int level, contents) { 
	if (level < 1) level = 1
	if (level > 4) level = 4

	div(class: (level <= 2 ? 'CSection' : 'CGeneric')) { 
	  def tmap = [class: 'CTopic']
	  if (level == 1) tmap['id'] = 'MainTopic'
	  div(tmap) { 
		"h${level}"(class: 'CTitle') { 
		  a(name: anchor) { 
			mkp.yieldUnescaped(title)	
		  }
		}		
		if (contents) { 
		  div(class: 'CBody') { 
			contents.delegate = this
			contents()
		  }
		}
	  }
	}	
  }

  def ndgroup(title, anchor, int level, contents) { 
	if (level < 1) level = 1
	if (level > 4) level = 4

	div(class: 'CGroup') { 
	  def tmap = [class: 'CTopic']
	  if (level == 1) tmap['id'] = 'MainTopic'
	  div(tmap) { 
		"h${level}"(class: 'CTitle') { 
		  a(name: anchor) { 
			mkp.yieldUnescaped(title)	
		  }
		}		
		if (contents) { 
		  div(class: 'CBody') { 
			contents.delegate = this
			contents()
		  }
		}
	  }
	}	
  }

  def ndtoc(toc) { 
	if (toc) { 
	  div(class: 'TOC') { 
		toc.eachWithIndex { item, i -> 
		  if (i > 0) { 
			mkp.yieldUnescaped(' &middot; ')
		  }
		  a(href: "#${item[1]}", item[0])
		}
	  } 
	}
  }

  def ndsummary(title, entries) { 
	div(class: 'Summary') { 
	  div(class: 'STitle') { mkp.yield(title) }
	  div(class: 'SBorder') { 
		table(border: 0, cellspacing: 0, cellpadding: 0, class: 'STable') { 
		  tbody { 
			entries.eachWithIndex { e, i ->   // entry [ name, anchor, description ]
			  tr(class: i % 2 == 0 ? 'SGeneric' : 'SGeneric SMarked') { 
				td(class: 'SEntry') { 
				  a(href: "#${e[1]}") { mkp.yield(e[0]) }
				}
				if (e[2]) { 
				  td(class: 'SDescription') { 
					mkp.yield(e[2])
				  }
				}
			  }
			}
		  }
		}
	  }
	}
  }

  def footer(text, href = null) { 
	if (text || href) { 
	  if (text == null) text = href
	  div(id: "Footer") {
		if (href) { 
		  a(href: "${href}", "${text}")
		} else { 
		  mkp.yieldUnescaped("${text}")
		}
	  }
	}
  }

}