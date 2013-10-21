
@Grab(group='net.sourceforge.htmlcleaner',  module='htmlcleaner', version='2.2')

class HTMLReader {  

  static readPage(address) { 
    def cleaner = new HtmlCleaner()
    def node = cleaner.clean(address.toURL())
 
    // Convert from HTML to XML
    def props = cleaner.getProperties()
    def serializer = new SimpleXmlSerializer(props)
    def xml = serializer.getXmlAsString(node)
 
    // Parse the XML into a document we can work with
    return new XmlSlurper(false,false).parseText(xml)
  }

  static base = 'temp/html'

  static writeXml(page, fname) { 
    def d1= new File(base)
    d1.mkdirs()
    def fw = new FileWriter(base + '/' + fname)
    groovy.xml.XmlUtil.serialize(page, fw)
    fw.close()
  }

}