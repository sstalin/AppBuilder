package xj.mobile.model.impl

//
// represent the output model -- the elements of the generated program  
//
class Project { 

  String rootViewName
  String packageName 

  List classes = []

  def mainViewClass 

  def getImageFiles() { 
    def result = [] 
    classes.each { c ->
      if (c.imageFiles)
		result += c.imageFiles
    }
    return result.unique()
  }

  def getSystemImageFiles() { 
    def result = [] 
    classes.each { c ->
      if (c.systemImageFiles)
		result += c.systemImageFiles
    }
    return result.unique()
  }

  def getFrameworks() { 
    def result = [] 
    classes.each { c ->
      if (c.frameworks)
		result += c.frameworks
    }
    return result.unique()
  }

  def getViewControllerNames() { 
    def result = []
    classes.each { c ->
      result << c.name
    }
    return result
  }

}