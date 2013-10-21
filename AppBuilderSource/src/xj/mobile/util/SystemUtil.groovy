package xj.mobile.util

import java.util.Properties;

import xj.mobile.Main

class SystemUtil { 

  public static boolean isRunningOnWindows() {
	String osName = System.getProperty("os.name")
	return osName.startsWith("Windows")
  }

  public static boolean isRunningOnMac() {
	String osName = System.getProperty("os.name")
	return osName.startsWith("Mac")
  }

  public static boolean isRunningOnKinux() {
	String osName = System.getProperty("os.name")
	return osName.startsWith("Linux")
  }

  public static Properties getAndroidAPIProp() {
	def prop = null
	try {
	  String sdkHome = Main.systemConfig.android.sdk.home;
	  def androidCommand = isRunningOnWindows() ? 'android.bat' : 'android'
	  def command = "${sdkHome}/tools/${androidCommand} list"
	  def p = command.execute()
	  def rawdata = p.getInputStream().readLines()
	  def errdata = p.getErrorStream().readLines()
			
	  if (rawdata.size() > 0) {
		prop = new Properties();
		for (line in rawdata) {
		  if (line.startsWith("id:")) {
			//System.out.printf("[%s]%n", line.substring(4, line.indexOf(" or")));
			//System.out.printf("[%s]%n", line.substring(line.indexOf(" or \"")+5, line.lastIndexOf('"')));
			prop.setProperty(line.substring(line.indexOf(" or \"")+5, line.lastIndexOf('"')),
							 line.substring(4, line.indexOf(" or")));
			//System.out.println(line);
		  }
		}
	  }

	  /*
	  if (errdata.size() > 0) {
		System.out.println("ERROR!")
		for (line in errdata) {
		  System.out.println(line)
		}
	  }
	  */
			
	  p.waitFor();
	} catch (Exception e) {
	  e.printStackTrace()
	}
	return prop
  }

}