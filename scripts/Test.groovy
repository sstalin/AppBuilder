
def iosConfig = new ConfigSlurper().parse(new File('conf/ios.conf').toURL())

println iosConfig.defaults.Font

String fname = 'Serif'
String styleKey = 'italic'

println iosConfig.defaults.Font[fname][styleKey]