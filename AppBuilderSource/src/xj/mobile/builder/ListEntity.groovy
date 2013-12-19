
package xj.mobile.builder

import org.codehaus.groovy.ast.ClassNode

import static xj.translate.Logger.info 

class ListEntity extends Expando implements GroovyInterceptable { 
  
  ModelBuilder buidler
  ListEntityDummy dummy
  def entityClass

  def fields = [] // a list of fields, including the inherited fields 

  ListEntity(ModelBuilder builder, args) { 
    super(args)
    this.builder = builder
    dummy = new ListEntityDummy()

    info "[ListEntity] args=${args}"

    entityClass = xj.mobile.builder.AppBuilder.app.classes[this['class'].name.toString()]

    ClassNode c = entityClass
    while (c && c.name != 'java.lang.Object') { 
      info "[ListEntity] get fields: c=${c.name} ${c.superClass?.name}"
      c.fields.each { f -> fields << f }
      c = xj.mobile.builder.AppBuilder.app.classes[c.superClass.name]
    }
    info "[ListEntity] fields: ${fields.name}"
  }

  def invokeMethod(String name, arg) { 
    if (name == 'each') { 
      ListEntityHandler handler = new ListEntityHandler()
      handler.handle(this, name, arg)

      def viewDecl = builder.viewStack[-1].declarations
      if (!viewDecl) { 
		viewDecl = [:]
		builder.viewStack[-1].declarations = viewDecl
      }
      viewDecl[this._dummy_] = [ isDummy: true, entity: this ]      

      //def e = [ this ].collect(arg[0])
      def e = [ this ].collect(arg[0])
      //println "[ListEntity] ${e}"
      def object = e[0]
      object.'#entity' = this
    } else if (name in ['add', 'delete']) { 
      info "ListEntity ${this.name} ${name}: " + (arg? arg[0] : '()')

    } else { 
      super.invokeMethod(name, arg)
    }
  }

}

class ListEntityDummy { 
  
} 

class ListEntityHandler { 

  def handle(ListEntity entity, String name, arg) { 
    info "[ListEntityHandler] handle() name: ${name}  arg: ${arg}"
    if (arg[0] instanceof Closure) { 
      def c = arg[0].metaClass.classNode.getDeclaredMethods("doCall")[0]
      // println c.parameters
      info "[ListEntityHandler] ${c.parameters[0].name}"

      entity._dummy_ = c.parameters[0].name
      //println c.code

    }
  }

}