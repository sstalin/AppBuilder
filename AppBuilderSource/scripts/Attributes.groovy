
class Attributes {  

  static String base = 'test/lang'

  static main(args) { 
    def attrMap = [:]

    new File(base).list(
      {d, f-> f ==~ /.*-attr\.txt/ } as FilenameFilter
    ).toList().each { fname -> 
      def file = new File("${base}/${fname}")
      //def file = new File(base + '/app01-attr.txt')
      println "Process ${fname}"

      def name = null
      def set = [] as Set
      
      file.eachLine { line -> 
		if (line.size() > 0) { 
		  if (line[0] != '\t') {
			println '=== ' + line + ' === ' + name
			if (name) { 
			  //println '==='
			  attrMap[name] = set
			}
			name = line
			set = attrMap[name]
			if (set == null) { 
			  set = [] as Set
			}
		  } else if (line.indexOf('[') >= 0) { 
			println '=== ' + line
			def map = [:]
			line[2 .. -2].split(', ').each { 
			  int i = it.indexOf(':')
			  if (i > 0) { 
				map[it[0 .. i-1]] = it[i+1 .. -1]
			  }	      
			}
			map['file'] = fname[0..-10]
			set.add(map)
		  }
		}
      }
      attrMap[name] = set
    }

    //println attrMap
    attrMap.each{ key, value ->
      def vstr = value.sort { it.name }.join('\n\t')
      println "${key}\n\t${vstr}"
    }
  }

}