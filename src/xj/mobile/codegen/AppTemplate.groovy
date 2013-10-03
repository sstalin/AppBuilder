package xj.mobile.codegen

import xj.mobile.*
import xj.mobile.model.impl.Project
import xj.mobile.common.AppInfo

abstract class AppTemplate { 

  static ant = new AntBuilder();

  @groovy.lang.Delegate
  AppInfo appInfo

  AppTemplate(AppInfo appInfo) {  
    this.appInfo = appInfo
  }

  abstract void generateCode(Project project) 

}