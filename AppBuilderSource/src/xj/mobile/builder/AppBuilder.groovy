
package xj.mobile.builder

import xj.mobile.common.AppGenerator
import xj.mobile.transform.AppTransformer
import xj.mobile.model.Application
import xj.mobile.model.ui.View
import xj.mobile.tool.GraphViz

import static xj.mobile.util.CommonUtil.encodeAttributeValue

import static xj.translate.Logger.info 

/*
 * Common driver for all platforms
 *
 * To-do: each invocation should handle only one platform, 
 *        reason: design and style transformation are applied to the model,
 *                each platform should start with a clean model
 */
class AppBuilder { 

  static ModelBuilder builder = new ModelBuilder()

  static final boolean verbose = true //false
  static final boolean WRITE_ATTRIBUTES = true
  static final boolean GENERATE_DIAGRAMS = false // generate diagram in GraphViz 


  String filename
  String target
  def userConfig 

  static Application app = null
  def classes = [:]

  AppBuilder(filename = null, userConfig = null) { 
    this.filename = filename
    this.userConfig = userConfig 
  }

  //
  // building app: called from script  
  //

  def app(args, closure = null) { 
    info '[AppBuilder] Start App Builder'
    info "[AppBuilder] classes: ${classes}"

    builder.reInit()
    app = new Application()
    if (classes) app.classes = classes 
	builder.handleChildren(app, [ args ], closure)
	if (builder.isOkay()) { 
	  if (app.mainView) process()
	} else { 
	  println "[Error] There are errors in the app definition."
      builder.printMessages('[Error]')
	}
    info '[AppBuilder] End App Builder'
  }

  //
  // Processing app 
  //

  void process() { 
    if (verbose) printApp()
    if (WRITE_ATTRIBUTES) writeAttributes()
    if (checkApp()) {
	  if (GENERATE_DIAGRAMS) GraphViz.generateDiagram(filename, app)
      transformApp()
      generateApp()
    }
  }

  // Write to a file, the attributes used in each element in this app 
  // For generating documentation only, not used for code generation  
  void writeAttributes() { 
    def attrMap = app.attributes() 
    new File('test/lang').mkdirs()
    def file = new File("test/lang/${filename}-attr.txt")
    file.delete()
    file = new File("test/lang/${filename}-attr.txt")
    attrMap.each{ key, value ->
	  value.each { v -> 
		//println "writeAttributes(): Type: ${v.value.class.name}  Value: ${v.value}"
		if (v.value instanceof String || v.value instanceof GString) { 
		  v.value = encodeAttributeValue(v.value)
		}
	  }
      def vstr = value.sort { it.name }.join('\n\t')
      file << "${key}\n\t${vstr}\n"
    }
  }

  void printApp() { 
    info '[AppBuilder] Application:\n' + app.print()
  }

  boolean checkApp() { 
    AppChecker checker = new AppChecker(app)
    if (checker.isOkay()) { 
      return true
    } else {  
      println "[Error] There are errors in the app definition."
      checker.printMessages('[Error]')
      return false
    }
  }

  void transformApp() { 
	canonicalization()

    AppTransformer transformer = AppTransformer.getAppTransformer('common')
    transformer.transform(app)

    if (verbose)    
      info "[AppBuilder] After common transformation:\n" + app.print()
  }

  void canonicalization() { 
    // insert the root navigation view when necessary 
    if (app.mainView.widgetType != 'NavigationView') { 
	  if (app.views.size() > 1 && app.views.every { it.widgetType != 'NavigationView' } && app.navigationBar || 
		  app.mainView.widgetType == 'ListView' ||
		  app.mainView.widgetType == 'ExpandableListView') { 
		def top = new View()
		top.'#type' = 'NavigationView' 
		top.id = Preprocessor.getID('top')
		app.views.each { 
		  top.add(it)
		  it.parent = top
		}
		app.mainView = top
		app.children = [ top ]
	
		if (verbose) info "[AppBuilder] After canonicalization():\n" + app.print()
      } 
      
    }
    app.mainView.root = true
  }

  void generateApp() { 
	AppTransformer transformer = AppTransformer.getAppTransformer(target)
	transformer.transform(app)
		
	if (verbose)
	  info "[AppBuilder] After design transformation:\n" + app.print()
		
	AppGenerator generator = AppGenerator.getAppGenerator(target)
	generator.setUp()
	generator.generate(app, filename, userConfig)
	generator.cleanUp()
  }  

}

