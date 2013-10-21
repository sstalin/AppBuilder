
package xj.mobile.codegen

import xj.mobile.ios.IOSAppGenerator
import xj.mobile.model.ui.*

class IOSAppDelegate { 

  String rootViewController;

  String importScrap = '';
  String propertyScrap = '';
  String synthesizerScrap = '';
  String appLaunchScrap = '';
  String deallocScrap = '';
  
  void process() { 
    if (rootViewController) { 
      importScrap = "#import \"${rootViewController}.h\""
      //attributesScrap = "${rootViewController} *rootViewController;"
      propertyScrap = "@property (strong, nonatomic) ${rootViewController} *rootViewController;"
      synthesizerScrap = "@synthesize rootViewController;"
      appLaunchScrap = """rootViewController = [[${rootViewController} alloc] init]; 
self.window.rootViewController = rootViewController;"""

      if (IOSAppGenerator.iosConfig.dealloc)
		deallocScrap = "[rootViewController release];"
    }
  }


}
