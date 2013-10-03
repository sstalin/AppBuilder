package xj.mobile.common

import groovy.text.SimpleTemplateEngine

import xj.mobile.Main
import xj.mobile.model.Application

import xj.translate.Language
import xj.translate.Translator
import xj.translate.java.JavaClassProcessor
import xj.translate.common.*

import static xj.translate.Logger.info 

abstract class AppGenerator {  
  
  static generators = [
    ios:     new xj.mobile.ios.IOSAppGenerator(),
    android: new xj.mobile.android.AndroidAppGenerator()
  ]

  static iosConfig = new ConfigSlurper().parse(new File(Main.confDir + '/ios.conf').toURL())
  static androidConfig = new ConfigSlurper().parse(new File(Main.confDir + '/android.conf').toURL())

  static appConfigs = [
	ios: iosConfig,
	android: androidConfig 
  ]

  static AppGenerator getAppGenerator(name) { 
    generators[name.toLowerCase()];
  }

  static getAppConfig(String name) { 
    appConfigs[name.toLowerCase()];
  } 

  static engine = new SimpleTemplateEngine();

  String getTarget() { null }

  ViewHierarchyProcessor vhp
  Translator translator

  void setUp() { }
  void cleanUp() { }

  void generate(Application app, String filename = null, def userConfig = null) { 
	String platform = target?.capitalize()
    info "[${platform}AppGenerator] Generating ${target} App ..."

    if (app) { 
      info "[${platform}AppGenerator] Generating ${target} App: process view hieraychy"

	  def appInfo = new AppInfo(app, filename, getAppConfig(target), userConfig)
      vhp = new ViewHierarchyProcessor(app, appInfo)
      vhp.process()

	  def clazz = "xj.mobile.codegen.${platform}AppTemplate" as Class 
	  def appTemplate = clazz.newInstance(appInfo)
	  appTemplate.generateCode(vhp.project)
    }

    info "[${platform}AppGenerator] ${target} App compleed."   
  }

}