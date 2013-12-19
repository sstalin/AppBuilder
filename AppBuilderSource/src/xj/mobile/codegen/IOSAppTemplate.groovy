package xj.mobile.codegen

import xj.mobile.*
import xj.mobile.model.impl.*
import xj.mobile.common.*

import static xj.mobile.util.CommonUtil.*
import static xj.mobile.codegen.IOSUtil.*
import static xj.translate.Logger.info 

class IOSAppTemplate extends AppTemplate { 

  static int sdk_version = 6

  def iosConfig;

  String projectTemplateDir;
  String viewControllerTemplateDir;

  def projHandler = new PBXProjectHandler(this)

  IOSAppTemplate(AppInfo appInfo) {  
    super(appInfo)
    this.iosConfig = appInfo.platformConfig

	if (userConfig.platform.ios_version == 5) { 
	  sdk_version = 5
	  projectTemplateDir = iosConfig.template.sdk5.dir
	  viewControllerTemplateDir = iosConfig.template.sdk5.file.dir
	} else { 
	  sdk_version = 6
	  projectTemplateDir = iosConfig.template.sdk6.dir
	  viewControllerTemplateDir = iosConfig.template.sdk6.file.dir
	}
  }

  void generateCode(Project project) { 
	info '[IOSAppTemplate] Generating iOS App: process app delegate'
	String rootViewControllerName = ViewControllerClass.getViewControllerName(project.rootViewName)

	IOSAppDelegate ad = new IOSAppDelegate(rootViewController : rootViewControllerName)
	ad.process()

	generateProject(project)
	generateAppDelegate(ad)

	info '[IOSAppTemplate] Generating iOS App: generate view controllers'

	project.classes.each { c -> 
	  //generateViewController(c)
	  generateClass(c)
	}
	addFilesToProject(project)
  }

  String getProjectInfo(Project project) { 
	String rootViewControllerName = ViewControllerClass.getViewControllerName(project.rootViewName)
	def rootViewController = project.classes.find { it.name == rootViewControllerName }
	rootViewController?.projectInfoScrap ?: ''	
  }

  void generateProject(Project project) {  
	new File('ant-log.txt').delete()
	ant.record(name: 'ant-log.txt', action: 'start')

    ant.echo('Create iOS App Project')
    ant.delete(dir: projectOutputDir)

    ant.echo("appname: ${appname}")
    ant.echo("appid: ${appid}")
    ant.echo("author: ${author}")
    ant.echo("org: ${org}")

	def bundleid = iosConfig.defaults.bundleID

    ant.copy(todir: projectOutputDir) {
      fileset(dir: projectTemplateDir) {
		include(name:"**/*")
		exclude(name:"**/*.png")
		exclude(name:"**/___APPDELEGATE___.*")
      }
      filtermapper { 
		replacestring(from: '___PROJECTNAME___', to: appname)
		//replacestring(from: '___PROJECTNAMEASIDENTIFIER___', to: appid)
      }
      filterset(begintoken: '___', endtoken: '___') { 
		filter(token: 'PROJECTNAME', value: appname)
		filter(token: 'PROJECTNAMEASIDENTIFIER', value: appid)
		filter(token: 'APPDELEGATE', value: 'AppDelegate')

		filter(token: 'FULLUSERNAME', value: author)
		filter(token: 'DATE', value: dateString)
		filter(token: 'YEAR', value: yearString)
		filter(token: 'ORGANIZATIONNAME', value: org)
		filter(token: 'ORGANIZATIONID', value: orgid)
		filter(token: 'BUNDLEID', value: bundleid)
		filter(token: 'APPICON', value: appIcon)
		filter(token: 'PROJECT_INFO', value: getProjectInfo(project))

      }
    }

    ant.copy(todir: projectOutputDir) {
      fileset(dir: projectTemplateDir) {
		include(name:"**/*.png")
      }
      filtermapper { 
		replacestring(from: '___PROJECTNAME___', to: appname)
      }
	}

    ant.echo('Project Done')
	ant.record(name: 'ant-log.txt', action: 'stop')
	info new File('ant-log.txt').text
  }

  void generateAppDelegate(IOSAppDelegate ad) {  
    if (ad) { 
	  new File('ant-log.txt').delete()
	  ant.record(name: 'ant-log.txt', action: 'start')
	
      ant.echo('Create AppDelegate')

      ant.copy(todir: projectOutputDir) {
		fileset(dir: projectTemplateDir) {
		  include(name:"**/___APPDELEGATE___.*")
		}
		filtermapper { 
		  replacestring(from: '___PROJECTNAME___', to: appname)
		  replacestring(from: '___APPDELEGATE___', to: 'AppDelegate')
		}
		filterset(begintoken: '___', endtoken: '___') { 
		  filter(token: 'PROJECTNAME', value: appname)
		  filter(token: 'PROJECTNAMEASIDENTIFIER', value: appid)
		  filter(token: 'APPDELEGATE', value: 'AppDelegate')
	  
		  filter(token: 'FULLUSERNAME', value: author)
		  filter(token: 'DATE', value: dateString)
		  filter(token: 'YEAR', value: yearString)
		  filter(token: 'ORGANIZATIONNAME', value: org)
		}
		filterset(begintoken: '/**', endtoken: '**/') { 
		  filter(token: 'IMPORT', value: ad.importScrap)
		  //filter(token: 'ATTRIBUTES', value: indent(ad.attributesScrap))
		  filter(token: 'PROPERTY', value: ad.propertyScrap)
		  filter(token: 'SYNTHESIZER', value: ad.synthesizerScrap)
		  filter(token: 'APPLICATION_DID_FINISH_LAUNCHING', value: indent(ad.appLaunchScrap))

		  filter(token: 'APPLICATION_WILL_RESIGN_ACTIVE', value: '')
		  filter(token: 'APPLICATION_DID_ENTER_BACKGROUND', value: '')
		  filter(token: 'APPLICATION_WILL_ENTER_FOREGROUN', value: '')
		  filter(token: 'APPLICATION_DID_BECOME_ACTIVE', value: '')
		  filter(token: 'APPLICATION_WILL_TERMINATE', value: '')

		  if (iosConfig.dealloc)
			filter(token: 'DEALLOC', value: indent(ad.deallocScrap))
		}
      }

      ant.echo('AppDelegate Done')
	  ant.record(name: 'ant-log.txt', action: 'stop')
	  info new File('ant-log.txt').text
    }
  }

  //void generateViewController(ViewControllerClass vc) { 
  void generateClass(IOSClass vc) { 
    if (vc) { 
	  vc.process()

	  new File('ant-log.txt').delete()
	  ant.record(name: 'ant-log.txt', action: 'start')
	
      ant.echo('Create ViewController')
      def viewControllerName = vc.name
      //def viewControllerSuperClass = vc.superClassClause
      def outputDir = "${projectOutputDir}/${appname}"

      ant.copy(todir: outputDir) {
		fileset(dir: viewControllerTemplateDir) {
		  include(name:"___CLASSNAME___.*")
		}
		filtermapper { 
		  replacestring(from: '___CLASSNAME___', to: viewControllerName)
		}
		filterset(begintoken: '___', endtoken: '___') { 
		  filter(token: 'PROJECTNAME', value: appname)
		  filter(token: 'PROJECTNAMEASIDENTIFIER', value: appid)
		  filter(token: 'APPDELEGATE', value: 'AppDelegate')
		  filter(token: 'CLASSNAME', value: viewControllerName)
		  filter(token: 'SUPERCLASS', value: vc.superClassClause)
	  
		  filter(token: 'FULLUSERNAME', value: author)
		  filter(token: 'DATE', value: "${dateString}")
		  filter(token: 'YEAR', value: "${yearString}")
		  filter(token: 'ORGANIZATIONNAME', value: org)
		}
		filterset(begintoken: '/**', endtoken: '**/') { 
		  filter(token: 'SYSTEM_IMPORT', value: vc.systemImportScrap)
		  filter(token: 'IMPORT', value: vc.importScrap)
		  filter(token: 'CLASS_DECLARATION', value: vc.classDeclarationScrap)
		  filter(token: 'IVAR_DECLARATION', value: vc.ivarDeclarationScrap)
		  filter(token: 'DECLARATION', value: vc.declarationScrap)
		  filter(token: 'PROPERTY', value: vc.propertyScrap)
		  filter(token: 'METHOD_DECLARATION', value: vc.methodDeclarationScrap)
		  filter(token: 'SYNTHESIZER', value: vc.synthesizerScrap)

		  filter(token: 'METHOD', value: vc.methodScrap)

		  filter(token: 'CLASS_EXTENSION', value: vc.classExtensionScrap)
		  filter(token: 'DID_RECEIVE_MEMORY_WARNING', value: '')

		  if (iosConfig.dealloc)
			filter(token: 'DEALLOC', value: indent(vc.deallocScrap))
		}
      }
      
      ant.echo('ViewController Done')
	  ant.record(name: 'ant-log.txt', action: 'stop')
	  info new File('ant-log.txt').text
    }
  }
 
  void addFilesToProject(Project project) { 
	if (project) { 
	  def sourceFiles = project.viewControllerNames
	  def imageFiles = project.imageFiles
	  def systemImageFiles = project.systemImageFiles
	  def frameworks = project.frameworks

	  projHandler.addFiles(sourceFiles, imageFiles + systemImageFiles, frameworks)

	  new File('ant-log.txt').delete()
	  ant.record(name: 'ant-log.txt', action: 'start')
	
	  boolean imageDirExists = (new File(Main.imageDir)).exists()

	  // copy image files
	  if (imageFiles && imageDirExists) { 
		ant.copy(todir: "${projectOutputDir}/${appname}/images") {
		  fileset(dir: Main.imageDir) {
			imageFiles.each { f -> 
			  include(name: f)
			}
		  }
		}
	  }
	  if (systemImageFiles) { 
		ant.copy(todir: "${projectOutputDir}/${appname}/images") {
		  fileset(dir: Main.confDir + '/images') {
			systemImageFiles.each { f -> 
			  include(name: f)
			}
		  }
		}
	  }

	  // app icon files
	  if (appIcon) { 
		boolean filesExist = false
		if (imageDirExists) { 
		  filesExist = ((new File(Main.imageDir +  "/${appIcon}.png")).exists() || 
						(new File(Main.imageDir +  "/${appIcon}@2x.png")).exists())
		}

		String fromDir = filesExist ? Main.imageDir : (Main.confDir + '/images')
		ant.copy(todir: "${projectOutputDir}/${appname}") {
		  fileset(dir: fromDir) {
			include(name: "${appIcon}.png") 
			include(name: "${appIcon}@2x.png")
		  }
		}
	  }

	  ant.record(name: 'ant-log.txt', action: 'stop')
	  info new File('ant-log.txt').text

	}
  }

}


