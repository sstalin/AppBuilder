package xj.mobile.common

class ListViewData { 

  def sections        = []  // list of widgets (Section) 
  def sectionTitles   = []  // list of strings 
  def sectionItems    = []  // list of list of items, each item is a Cell
  def sectionEntities = []  // list of entities for each section, null if the section is static 

  boolean hasNextView   = false
  boolean hasMenu       = false
  boolean hasSelection  = false

  boolean hasDetailText = false
  boolean hasImage      = false
  boolean hasAccessory  = false
  boolean hasCheckBox   = false
  boolean hasEntity     = false

  boolean hasData       = false

  boolean hasEntityMenuAction = false
  boolean singleSection = true
  boolean handleReadWrite = false 

  int numSections = 0

  ListSectionDataMode dataMode = ListSectionDataMode.Text

}