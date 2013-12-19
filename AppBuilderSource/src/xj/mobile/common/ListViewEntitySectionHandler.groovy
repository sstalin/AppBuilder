package xj.mobile.common

import static xj.translate.Logger.info 

class ListViewEntitySectionHandler { 

  String entityVar = null
  String entityClassName = null
  def entityDef = null
  def entityValues = null
  def entityClass = null
  def dummy

  void initEntity(ListViewData ld, pos) { 
    dummy = ld.sectionItems[pos][0]
    entityDef = ld.sectionEntities[pos] 
    entityVar = entityDef?.'_name_' 
    entityValues = entityDef?.values
	entityClass = entityDef?.entityClass
	entityClassName = entityClass?.name
  }

}