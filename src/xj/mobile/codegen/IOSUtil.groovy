package xj.mobile.codegen

class IOSUtil { 

  //
  // using literals 
  //

  // convert list of strings to NSArray  
  static String toNSArrayWithStrings(list, br = ' ', mutable = false) { 
    if (list) { 
	  String contents = '@[ ' + list.collect{ s -> ('@"' + s + '"') }.join(',' + br) + ' ]'
	  if (mutable) { 
		return "[${contents} mutableCopy]"
	  } else { 
		return contents 
	  }
	} else { 
	  if (mutable) { 
		return '[NSMutableArray array]'
	  } else { 
		return '@[]'
	  }
	}
  }

  // convert list of objects to NSArray  
  static String toNSArrayWithObjects(list, br = ' ', mutable = false) { 
    if (list) { 
	  String contents = '@[ ' + list.join(',' + br) + ' ]'
	  if (mutable) { 
		return "[${contents} mutableCopy]"
	  } else { 
		return contents 
	  }
	} else { 
	  if (mutable) { 
		return '[NSMutableArray array]'
	  } else { 
		return '@[]'
	  }
	}
  }

  // convert list of object-key to NSDictionary  
  static String toNSDictionaryWithObjects(list, br = ' ', mutable = false) { 
	if (list) { 
	  String contents = '@{ ' + list.collect{ kv -> "${kv[1]}: ${kv[0]}"}.join(',' + br) + ' }'
	  if (mutable) { 
		return "[${contents} mutableCopy]"
	  } else { 
		return contents 
	  }
	} else { 
	  if (mutable) { 
		return '[NSMutableDictionary dictionary]'
	  } else { 
		return '@{}'
	  }
	}
  }


  //
  // classic methods  
  //

  // convert list of strings to NSArray  
  static String toNSArrayWithStrings_(list, br = ' ', mutable = false) { 
	def cname = mutable ? 'NSMutableArray' : 'NSArray'
	def contents = ''
    if (list) 
	  contents = br + list.collect{ s -> ('@"' + s + '"') }.join(',' + br) + ', '
	"[${cname} arrayWithObjects:${contents}nil]"
  }

  // convert list of objects to NSArray  
  static String toNSArrayWithObjects_(list, br = ' ', mutable = false) { 
	def cname = mutable ? 'NSMutableArray' : 'NSArray'
	def contents = ''
    if (list) 
	  contents = br + list.join(',' + br) + ', '
	"[${cname} arrayWithObjects:${contents}nil]"
  }

  // convert list of object-key to NSDictionary  
  static String toNSDictionaryWithObjects_(list, br = ' ', mutable = false) { 
	def cname = mutable ? 'NSMutableDictionary' : 'NSDictionary'
	def contents = ''
    if (list) 
	  contents = br + list.collect{ kv -> "${kv[0]}, ${kv[1]}"}.join(',' + br) + ', '
	"[${cname} dictionaryWithObjectsAndKeys:${contents}nil]"
  }


}