
package xj.mobile

import org.codehaus.groovy.control.*
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.control.customizers.*

import xj.mobile.builder.*
import xj.translate.Logger

import static xj.mobile.util.CommonUtil.*
import static xj.translate.Logger.info 
				    
/**
 * Entry point of Mobile App Builder
 * Arguments:
 *   madl-script user-config        (complete)
 * or 
 *   app-dir                        (to-do) 
 */

class Main { 

  static PROG_NAME = 'Mobile App Builder'
  static PROG_VERSION = 'ver. 0.3'
  
  static final String confDir = 'conf'

  static userConfig = null
  static String scriptFile = null
  static String sourceDir = null 
  static String destDir = null

  static String getImageDir() { 
    sourceDir ? "${sourceDir}/images" : 'images'
  }

  static boolean nodate = false

  static systemConfig = new ConfigSlurper().parse(new File(confDir + '/system.conf').toURL())

  public static void main(String[] args) {
	//systemConfig = new ConfigSlurper().parse(new File(confDir + '/system.conf').toURL())

    reInit()
    if (args?.length > 0) { 
	  boolean expectDest = false 
      args.each { f -> 
		if (f[0] == '-') { 
		  // options 
		  if ('-nodate' == f) { 
			nodate = true
		  } else if ('-d' == f) { 
			// destination dir 
			expectDest = true
		  }
		} else {  
		  if (expectDest) { 
			destDir = f
			expectDest = false 
		  } else if (!scriptFile) { 
			scriptFile = f
			sourceDir = new File(scriptFile).parent
		  } else {  
			userConfig = new ConfigSlurper().parse(new File(f).toURL())
			//println userConfig.toString()
		  }
		}
      }

      if (scriptFile) { 
		println "${PROG_NAME} ${PROG_VERSION}"
		println "Processing ${scriptFile}"
		new Main().runScript(scriptFile)
	  }
    } else {  
      println "[Usage] appbuilder <options> <madl file> <user config file>"
    }
  }

  public static reInit() { 
    userConfig = null;
    scriptFile = null;
    sourceDir = null; 
    nodate = false;
  }

  void runScript(madlScript) { 
    try { 
      File infile = new File(madlScript)
      String filename = getFileName(infile.name)
      //filename = filename[0 ..< filename.indexOf('.')]
    
	  String plat_suffix = [ 'ios', 'android' ].findAll{ userConfig.platform[it] }.join('-')

	  Logger.setLogFile("logs/${filename}-${plat_suffix}.log")

      String srcdir = infile.parent 
      info "[Main] infile: ${infile}   fileanme: ${filename}  srcdir: ${srcdir}"

      def script = SCRIPT_HEADER + infile.text
      def wdir = new File('work')
      if (!wdir.exists()) wdir.mkdir()
      def script1 = new File('work/app.groovy')
      script1.write(script)

      def builder = new AppBuilder(filename, userConfig)
      def errors = []
      boolean okay = checkScript(script1, madlScript, builder, errors)

      if (okay) { 
		def binding = new Binding([builder : builder])
		List<String> classpath = [ 'lib/madl.jar' ]
		CompilerConfiguration cc = new CompilerConfiguration();
		//cc.setScriptBaseClass( Main.class.getName() );
		cc.setClasspathList(classpath);
		// inject default imports
		//ImportCustomizer ic = new ImportCustomizer();
		//ic.addStarImports('xj.mobile.lang.madl')
		//cc.addCompilationCustomizers(ic);

		ClassLoader classloader = this.class.getClassLoader();
		GroovyShell shell = new GroovyShell(classloader, binding, cc)
		//def shell = new GroovyShell(binding);

		[ 'iOS', 'Android' ].each { plat ->	  
		  if (userConfig.platform[plat.toLowerCase()]) { 
			info "===== Start ${plat} ====="
			Preprocessor.reset()
			builder.target = plat
			shell.evaluate(script1)
			info "===== End ${plat} ====="
		  }
		}
      } else { 
		println "[Error] There are errors in the input."
		errors.each { e -> println '[Error] ' + e}
      }
    } catch (FileNotFoundException e) { 
	  println "[Error] ${e.class.simpleName} caused by\n\t${e.message}.\nFail to process ${madlScript}" 
      //println "[Error] File not found: ${madlScript}" 
	}

	  /*
	// comment out this exception for regression test, include for release 
    } catch (Exception e) { 
	  println "[Error] ${e.class.simpleName} caused by\n\t${e.message}.\nFail to process ${madlScript}" 
	  e.printStackTrace()
    }
	  */
  }

  boolean checkScript(File script, String name, AppBuilder builder, def errors = null) {
    boolean verbose = true 
    boolean okay = true

    CompilationUnit cu = new CompilationUnit() 
    //cu.addSource(filename, script);
    cu.addSource(script);
    try {
      cu.compile(Phases.CANONICALIZATION)
      //cu.compile(Phases.SEMANTIC_ANALYSIS)
      //cu.compile(Phases.CONVERSION)
      
      CompileUnit astRoot = cu.ast
      
      astRoot.modules.each {  module -> 
		module.classes.each { c -> 
		  if (verbose) info "[CheckScript] class ${c.name}"

		  if (c.name != 'app') { 
			builder.classes[c.name] = c
		  }

		  c.fields.each { f -> 
			if (verbose) info "[CheckScript] field: ${f.name}"
		  }
		  c.methods.each { m -> 
			if (verbose) info "[CheckScript] method: ${m.name}"

			if (m.name == 'run') { 
			  okay &= Preprocessor.checkViewBuilder(m.code, name, errors)
			}
		  }
		}
      }
    } catch (CompilationFailedException cfe) {
      cfe.printStackTrace()
    } catch (Throwable t) {
      t.printStackTrace()
    }

    return okay
  }

  static final SCRIPT_HEADER = '''import xj.mobile.lang.madl.*
def app = { args, closure -> builder.app(args, closure) }
'''

static final SCRIPT_HEADER_LINE = 2

}

