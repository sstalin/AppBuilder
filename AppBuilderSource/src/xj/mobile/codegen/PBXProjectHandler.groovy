
package xj.mobile.codegen

class PBXProjectHandler { 

  IOSAppTemplate apptemp;

  PBXProjectHandler(IOSAppTemplate apptemp) { 
    this.apptemp = apptemp
  }

  def uuid24 = [] as Set
  def uuid16 = [] as Set
  def random = new Random()
  long uuid8base = 0

  String generateUUID16() { 
    def u16 = null
    while (true) { 
      def u1 = Long.toHexString(System.currentTimeMillis()).toUpperCase()[-8..-1]
      def u2 = Long.toHexString(System.currentTimeMillis() + random.nextLong()).toUpperCase()[-8..-1]
      u16 = u1 + u2
      if (!uuid16.contains(u16)) { 
		uuid16 << u16
		break
      }
    }
    return u16
  }

  String generateUUID24(name, u16) { 
    if (uuid8base == 0) { 
      uuid8base = System.currentTimeMillis() + random.nextLong() + (name ? name.hashCode() : 0)
    } else { 
      uuid8base++
		}
    def u3 = Long.toHexString(uuid8base).toUpperCase()[-8..-1]
    return u3 + u16
  }

  String getImgaeType(f) { 
    if (f) { 
      int i = f.lastIndexOf('.')
      if (i >= 0) { 
		def ext = f[(i + 1) .. -1]
		if (ext == 'png') { 
		  return 'png'
		} else if (ext == 'jpg' || ext == 'jpeg') { 
		  return 'jpeg'
		}
      }
    }
    return 'jpeg' 
  }

  void addFiles(sourceFiles, imageFiles = null, frameworks = null) { 
    if (sourceFiles || imageFiles || frameworks ) { 
      def appname = apptemp.appname
      def pbxprojName = "${apptemp.projectOutputDir}/${appname}.xcodeproj/project.pbxproj"
      def tempName = 'work/project.pbxproj'
      def pbxprojFile = new File(pbxprojName)
      def pbxprojLines = []
      pbxprojFile.eachLine { line ->
		pbxprojLines << line
		// extract all UUID
		def m = (line =~ /^[ \t]*([0-9A-F]*) /)
		if (m) { 
		  def uid = m[0][1]
		  //println "${uid}  ${uid[0..7]}  ${uid[8..23]}"
		  uuid24 << uid
		  uuid16 << uid[8..23]
		}
      }

      def u16 = generateUUID16()

      // for source files 
      def buildFileSection = []
      def fileReferenceSection = []
      def groupSection = []
      def sourceBuildPhaseSection = []
      
      // for image files 
      def imageGroupSection = []
      def resourcesBuildPhaseSection = []

      // for frameworks 
      def frameworksBuildPhaseSection = []
      def frameworksGroupSection = []

      sourceFiles.each { f -> 	
		def u1 = generateUUID24(f, u16)  // for file.m in Sources, in PBXBuildFile section
		def u2 = generateUUID24(f, u16)  // for file.h in PBXFileReference section
		def u3 = generateUUID24(f, u16)  // for file.m file in PBXFileReference section

		buildFileSection << "\t\t${u1} /* ${f}.m in Sources */ = {isa = PBXBuildFile; fileRef = ${u3} /* ${f}.m */; };"	
		fileReferenceSection << "\t\t${u2} /* ${f}.h */ = {isa = PBXFileReference; fileEncoding = 4; lastKnownFileType = sourcecode.c.h; path = ${f}.h; sourceTree = \"<group>\"; };"
		fileReferenceSection <<"\t\t${u3} /* ${f}.m */ = {isa = PBXFileReference; fileEncoding = 4; lastKnownFileType = sourcecode.c.objc; path = ${f}.m; sourceTree = \"<group>\"; };"
		groupSection << "\t\t\t\t${u2} /* ${f}.h */,"
		groupSection << "\t\t\t\t${u3} /* ${f}.m */,"
		sourceBuildPhaseSection << "\t\t\t\t${u1} /* ${f}.m in Sources */,"
      }

      def u2 = generateUUID24('Image', u16)  // for Image group in PBXFileReference section
      if (imageFiles) { 
		groupSection << "\t\t\t\t${u2} /* Images */,"

		imageFiles.each { f -> 
		  def u1 = generateUUID24(f, u16)  // for image file in Sources, in PBXBuildFile section
		  def u3 = generateUUID24(f, u16)  // for image file in PBXFileReference section

		  def type = getImgaeType(f)
		  buildFileSection << "\t\t${u1} /* ${f} in Resources */ = {isa = PBXBuildFile; fileRef = ${u3} /* ${f} */; };"
		  fileReferenceSection << "\t\t${u3} /* ${f} */ = {isa = PBXFileReference; lastKnownFileType = image.${type}; name = ${f}; path = images/${f}; sourceTree = \"<group>\"; };"
		  imageGroupSection << "\t\t\t\t${u3} /* ${f} */,"
		  resourcesBuildPhaseSection << "\t\t\t\t${u1} /* ${f} in Resources */,"
		}
      }

      if (frameworks) { 
		frameworks.each { f -> 
		  def u1 = generateUUID24(f, u16)  // for framework in PBXBuildFile section
		  def u3 = generateUUID24(f, u16)  // for framework in PBXFileReference section

		  buildFileSection << "\t\t${u1} /* ${f} in Frameworks */ = {isa = PBXBuildFile; fileRef = ${u3} /* ${f} */; };"
		  fileReferenceSection << "\t\t${u3} /* ${f} */ = {isa = PBXFileReference; lastKnownFileType = wrapper.framework; name = ${f}; path = System/Library/Frameworks/${f}; sourceTree = SDKROOT; };"

		  frameworksBuildPhaseSection << "\t\t\t\t${u1} /* ${f} in Frameworks */,"
		  frameworksGroupSection << "\t\t\t\t${u3} /* MapKit.framework */,"
		}
      }

      /*
		Order of sections in pbxproject file 
	
		PBXBuildFile section
		PBXFileReference section
		PBXFrameworksBuildPhase section
		PBXGroup section
		PBXNativeTarget section
		PBXProject section
		PBXResourcesBuildPhase section
		PBXSourcesBuildPhase section
		PBXVariantGroup section
		XCBuildConfiguration section
		XCConfigurationList section
	  */


      int start = 0
      int inPBXBuildFileSection = 10
      int inPBXFileReferenceSection = 20
      int inPBXFrameworksBuildPhaseSection = 30
      int inPBXGroupSection = 40
      int inPBXNativeTargetSection = 50
      int inPBXProjectSection = 60
      int inPBXResourcesBuildPhaseSection = 70
      int inPBXSourcesBuildPhaseSection = 80

      def GroupInjectionLocations = [ 
		inPBXFrameworksBuildPhaseSection, 
		inPBXGroupSection + 2, 
		inPBXGroupSection + 5, 
		inPBXGroupSection + 6, 
		inPBXResourcesBuildPhaseSection + 2,
		inPBXSourcesBuildPhaseSection + 2
      ] as Set      

      // inject files
      def tempFile = new File(tempName)
      if (tempFile.exists()) tempFile.delete()
      tempFile.createNewFile()
      int stage = start
      pbxprojLines.each { line ->
		//println "PBCProjectHandler stage: ${stage} line: ${line}"

		//if (stage != 6 && stage != 7 && stage != 11 && stage != 15) 
		if (!(stage in GroupInjectionLocations)) 
		  tempFile << (line + '\n')

		if (stage == start && (line =~ /^\/\* Begin PBXBuildFile section \*\//)) {
		  tempFile << buildFileSection.join('\n') << '\n'
		  //stage++
		} else if (stage == start && (line =~ /^\/\* Begin PBXFileReference section \*\//)) { 
		  tempFile << fileReferenceSection.join('\n') << '\n'
		  //stage++
		} else if (stage == start && (line =~ /^\/\* Begin PBXFrameworksBuildPhase section \*\//)) { 
		  stage = inPBXFrameworksBuildPhaseSection
		} else if (stage == inPBXFrameworksBuildPhaseSection) { 
		  if (line =~ /^[ \t]*\);/) { 
			tempFile << frameworksBuildPhaseSection.join('\n') << '\n'      
			stage = start
			//tempFile << groupSection.join('\n') << '\n'
			//stage++
          } 
          tempFile << (line + '\n')
		} else if (stage == start && (line =~ /^\/\* Begin PBXGroup section \*\//)) { 
		  stage = inPBXGroupSection
		} else if (stage == inPBXGroupSection && (line =~ ~"^[ \t]*[0-9A-F]* \\/\\* Frameworks \\*\\/ = ")) { 
		  stage++ 
		  } else if (stage == inPBXGroupSection + 1 && (line =~ /^[ \t]*children = \(/)) { 
		  stage++
			} else if (stage == inPBXGroupSection + 2) {
		  if (line =~ /^[ \t]*\);/) { 
			tempFile << frameworksGroupSection.join('\n') << '\n'
			stage++
			  } 
          tempFile << (line + '\n')
		} else if (stage == inPBXGroupSection + 3 && (line =~ ~"^[ \t]*[0-9A-F]* \\/\\* ${appname} \\*\\/ = ")) { 
		  stage++
			} else if (stage == inPBXGroupSection + 4 && (line =~ /^[ \t]*children = \(/)) { 
		  stage++
			} else if (stage == inPBXGroupSection + 5) {
		  //if (line =~ /^[ \t]*\);/) { 
		  if (line =~ /^[ \t]*[0-9A-F]* \/\* Supporting Files \*\/,/) { 
			tempFile << groupSection.join('\n') << '\n'
			stage++
			  } 
          tempFile << (line + '\n')
		} else if (stage == inPBXGroupSection + 6) { 
		  if (line =~ /^\/\* End PBXGroup section \*\//) { 
			if (imageFiles) { 
			  tempFile << """\t\t${u2} /* Images */ = {
\t\t\tisa = PBXGroup;
\t\t\tchildren = (
"""
			  tempFile << imageGroupSection.join('\n') << '\n'
			  tempFile <<  """\t\t\t);
\t\t\tname = Images;
\t\t\tsourceTree = \"<group>\";
\t\t};
"""
			}
			stage = start
			//stage++
		  }
		  tempFile << (line + '\n')
		} else if (stage == start && (line =~ /^\/\* Begin PBXResourcesBuildPhase section \*\//)) { 
		  stage = inPBXResourcesBuildPhaseSection
		  //stage++
		} else if (stage == inPBXResourcesBuildPhaseSection && (line =~ /^[ \t]*[0-9A-F]* \/\* Resources \*\/ = /)) { 
		  stage++
			} else if (stage == inPBXResourcesBuildPhaseSection + 1 && (line =~ /^[ \t]*files = \(/)) { 
		  stage++
			} else if (stage == inPBXResourcesBuildPhaseSection + 2) { 
		  if (line =~ /^[ \t]*\);/) { 
			tempFile << resourcesBuildPhaseSection.join('\n') << '\n'
			stage = start
			//stage++
          } 
          tempFile << (line + '\n')
		} else if (stage == start && (line =~ /^\/\* Begin PBXSourcesBuildPhase section \*\//)) { 
		  stage = inPBXSourcesBuildPhaseSection
		  //stage++
		} else if (stage == inPBXSourcesBuildPhaseSection && (line =~ /^[ \t]*[0-9A-F]* \/\* Sources \*\/ = /)) { 
		  stage++
			} else if (stage == inPBXSourcesBuildPhaseSection + 1 && (line =~ /^[ \t]*files = \(/)) { 
		  stage++
			} else if (stage == inPBXSourcesBuildPhaseSection + 2) { 
		  if (line =~ /^[ \t]*\);/) { 
			tempFile << sourceBuildPhaseSection.join('\n') << '\n'
			stage++
			  } 
          tempFile << (line + '\n')
		}
      }

      tempFile.renameTo(pbxprojFile)
    }
  }

}
