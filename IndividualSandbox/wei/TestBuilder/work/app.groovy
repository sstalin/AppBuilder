import xj.mobile.lang.madl.*
def app = { args, closure -> builder.app(args, closure) }
app(name : 'Modal View 2') { 

  View(id: v1, title: 'First') { 
    Label(text: '''View 1 
Line 1-1 
Line 1-2
 ...
''')

    Button(text: 'Press', next: v2, transition: PartialCurl)   
  }
  
  View(id: v2, title: 'Second') { 
    Label(text: '''View 2 
Line 2-1 
Line 2-2
 ...
''')

    Button(text: 'Dismiss', next: Previous)  
  }

}