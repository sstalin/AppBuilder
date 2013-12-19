
package xj.mobile.model

import xj.mobile.model.properties.PropertyList

import static xj.translate.Logger.info 

// context sensitive property 
class PropertyModel extends Expando { 

  String name
  def value

  PropertyModel(String name) { 
    this.name = name
  }

  PropertyModel(value) { 
	this.value = value
  }

  protected PropertyModel() { }

  // without a context ==> name as String
  def eval(Class context, boolean plural = false) { 
	info "[PropertyModel] eval: context=${context?.name} owner=${owner} plural=${plural} name=${name} value=${value}"
	def result = null
	if (name) { 
	  result = evalName(context, name, plural)
	} else { 
	  result = evalValue(context, value, plural)
	}
	if (or_values) { 
	  def vlist = [ result ]
	  or_values.each { v -> 
		if (v instanceof PropertyModel) { 
		  vlist << v.eval(context, plural)
		}
	  }
	  result = new PropertyList()
	  result.values = vlist
	}
	return result
  }

  static evalName(Class context, String name, boolean plural) { 
	def result = null
	if (name) { 
	  String name0 = name?.toLowerCase()
	  if (name0 in [ 'yes', 'no', 'true', 'false' ]) { 
		return name0 in [ 'yes', 'true' ]
	  }

	  if (context) { 
		try { 
		  if (plural) { 
			List names = findAllMatchingNames(context, name)
			result = names.collect { context.getValue(it) }
		  } else { 
			result = context.getValue(name)
			if (result == null) { 
			  String name1 = findMatchingName(context, name)
			  if (name1) { 
				result = context.getValue(name1)
			  } 
			}
		  }
		} catch (Exception e) {
		  e.printStackTrace()
		}
	  } else { 
		result = name
	  }
	}
	return result
  }

  static evalValue(Class context, value, boolean plural) { 
	def result = null
	if (value != null) { 
	  if (context) { 
		try { 
		  if (plural) { 
			if (value instanceof List) { 
			  result = value.collect { context.getValue(it.name) }
			}
		  } else { 
			result = context.getValue(value)
		  }
		} catch (Exception e) {
		  e.printStackTrace()
		}
	  }
	}
	return result
  }

  String toString() { 
	name ? name : value?.toString()
  }

  def or_values = []

  def or(v) { 
	info "[PropertyModel] or: value=${v} class=${v.class}"	
	or_values << v
	return this 
  }

  static String findMatchingName(Class context, String name) { 
	String result = null
	if (name && context) { 
	  String name0 = name.toLowerCase()
	  List names = context.values.keySet() as List
	  for (int i = 0; i < names.size() && result == null; i++) { 
		String name1 = names[i].toLowerCase()
		if (name1.contains(name0)) result = names[i]
	  }
	}
	return result
  }

  static List findAllMatchingNames(Class context, String name) { 
	List result = null
	if (name && context) { 
	  result = []
	  String name0 = name.toLowerCase()
	  List names = context.values.keySet() as List
	  for (int i = 0; i < names.size(); i++) { 
		String name1 = names[i].toLowerCase()
		if (name0 == 'all' || name1.contains(name0)) 
		  result << names[i]
	  }
	}
	return result
  }

  Object getAt(List indx) { null }
  Object getAt(String property) { null }
  Object getAt(int idx) { null }

}