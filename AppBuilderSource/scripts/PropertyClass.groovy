
def className = 'DatePickerMode'
def packageName = 'xj.mobile.model.ui'

def prefix = 'UIDatePickerMode'
def values = [ 
  'Time',
  'Date',
  'DateAndTime',
  'CountDownTimer' 
]
  
def var = 'mode'

//
//

def file = "src/${packageName.replace('.', '/')}/${className}.groovy"
println "Write ${file}"

def valuesDecl = values.collect { v ->
  "  static final ${className} ${v} = new ${className}('${v}')"
}.join('\n')

def code = """
package ${packageName}

class ${className} { 

  static values = [:]

${valuesDecl}

  String ${var}
  
  private ${className}(${var}) { 
    this.${var} = ${var}
    values[${var}] = this
  }

  def toIOSString() { 
    return '${prefix}' + ${var}
  }

  String toShortString() { 
    ${var}
  }

  String toString() { 
    \"${className}.\${${var}}\"
  }

  static hasValue(name) { 
    values.hasKey(name)
  }

  static getValue(name) { 
    values[name]
  }

}
"""

new File(file).write(code)


