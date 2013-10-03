package xj.mobile.common

import xj.mobile.*
import xj.mobile.model.*

class AppInfo { 

  String filename; 
  def userConfig;
  def platformConfig;

  String outputDir;
  String projectOutputDir;

  String appname;
  String appid;
  String author;
  String org;
  String orgid;

  String dateString
  String yearString

  String appIcon = 'icon'

  String packageName = null

  AppInfo(Application app, 
		  String filename,
		  platformConfig,
		  userConfig) {
    this.filename = filename
	this.platformConfig = platformConfig
    this.userConfig = userConfig

    appname = app.name ?: 'My App'
	appid = appname.replaceAll('[^a-zA-Z0-9_]', '')
	appname = appname.replaceAll('[^a-zA-Z0-9_ ]', ' ')
    author = userConfig?.developer.name ?: 'Developer'
    org = userConfig?.developer.org ?: 'My Company'
    orgid = org.replaceAll('[ \\t]', '')

	if (app.icon) {
	  appIcon = app.icon
	}

	packageName = (platformConfig.defaults.packageName ?: 'com.example') + '.' + filename.toLowerCase()

    outputDir = platformConfig.output.dir
	if (Main.destDir) { 
	  outputDir = outputDir + '/' + Main.destDir
	}
    projectOutputDir = "${outputDir}/${filename}"	

    if (Main.nodate) { 
      dateString = 'Jul 4, 2011'
      yearString = '2011'
    } else { 
      dateString = java.text.DateFormat.getDateInstance().format(new Date())
      yearString = Calendar.getInstance().get(Calendar.YEAR)
    }
  }

}