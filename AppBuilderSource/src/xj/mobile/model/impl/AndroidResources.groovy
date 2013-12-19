package xj.mobile.model.impl

//
// represent XML resources in Android   
//
class AndroidResources { 

  List<String> permissions = []
  List<String> libraries = []

  // Map of drawable resources: name -> contents
  def drawableResources = [:]

  // Map of layout files: name -> contents
  def viewLayouts = [:]

  // Map of menu files: name -> contents
  def menuResources = [:]

  // Map of entries of <declare-styleable> in attrs.xml
  def styleableResources = [:]

  // Map of name -> string in strings.xml
  def stringResources = [:]

}