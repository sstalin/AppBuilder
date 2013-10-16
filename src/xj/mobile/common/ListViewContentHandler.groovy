package xj.mobile.common

import xj.mobile.*
import xj.mobile.model.ui.*
import xj.mobile.lang.*
import xj.mobile.builder.ListEntity

import static xj.translate.Logger.info 

import xj.translate.common.ModuleProcessor

class ListViewContentHandler { 

  def dataSectionHandler 
  def sectionHandlers

  def findSectionHandler(ListEntity entity) { 
	if (entity) { 
	  for (int i = 0; i < numSections; i++) { 
		if (sectionEntities[i] == entity) {
		  return sectionHandlers[i]
		}
	  }
	}
	return null
  }

  def getSectionHandler(i) { 
	sectionHandlers ? sectionHandlers[i] : dataSectionHandler 
  }


}
