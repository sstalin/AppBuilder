
package xj.mobile.model.properties

class SystemButton extends Property { 

  static values = [:]

  static final SystemButton Done        = new SystemButton('Done');
  static final SystemButton Cancel      = new SystemButton('Cancel');
  static final SystemButton Edit        = new SystemButton('Edit');
  static final SystemButton Save        = new SystemButton('Save');
  static final SystemButton Add         = new SystemButton('Add');
  static final SystemButton Compose     = new SystemButton('Compose');
  static final SystemButton Reply       = new SystemButton('Reply');
  static final SystemButton Action      = new SystemButton('Action');
  static final SystemButton Organize    = new SystemButton('Organize');
  static final SystemButton Bookmarks   = new SystemButton('Bookmarks');
  static final SystemButton Search      = new SystemButton('Search');
  static final SystemButton Refresh     = new SystemButton('Refresh');
  static final SystemButton Stop        = new SystemButton('Stop');
  static final SystemButton Camera      = new SystemButton('Camera');
  static final SystemButton Trash       = new SystemButton('Trash');
  static final SystemButton Play        = new SystemButton('Play');
  static final SystemButton Pause       = new SystemButton('Pause');
  static final SystemButton Rewind      = new SystemButton('Rewind');
  static final SystemButton FastForward = new SystemButton('FastForward');
  static final SystemButton Undo        = new SystemButton('Undo');
  static final SystemButton Redo        = new SystemButton('Redo');
  static final SystemButton PageCurl    = new SystemButton('PageCurl');

  //static final SystemButton FlexibleSpace = new SystemButton('FlexibleSpace');
  //static final SystemButton FixedSpace = new SystemButton('FixedSpace');
  
  String name

  private SystemButton(name) { 
    this.name = name
    values[name] = this
  }

  String toShortString() { 
    name
  }

  String toString() { 
    "SystemButton.${name}"
  }

  String toIOSString() { 
    'UIBarButtonSystemItem' + name
  }

  String toAndroidJavaString() { 
    String ic = androidIconNames[name]
    if (ic) 
      return 'android.R.drawable.' + ic
    else 
      return null
  }

  String toAndroidXMLString() { 
    String ic = androidIconNames[name]
    if (ic) 
      return '@android:drawable/' + ic
    else 
      return null
  }

  static hasValue(name) { 
    values.hasKey(name)
  }

  static getValue(name) { 
    values[name]
  }

  static androidIconNames = [
    'Done'        : '',
    'Cancel'      : '',
    'Edit'        : '',
    'Save'        : '',
    'Add'         : 'ic_menu_add',
    'Compose'     : 'ic_menu_edit',
    'Reply'       : '',
    'Action'      : 'ic_menu_set_as',
    'Organize'    : '',
    'Bookmarks'   : '',
    'Search'      : 'ic_menu_search',
    'Refresh'     : '',
    'Stop'        : 'ic_menu_close_clear_cancel',
    'Camera'      : 'ic_menu_camera',
    'Trash'       : 'ic_menu_delete',
    'Play'        : 'ic_media_play',
    'Pause'       : 'ic_media_pause',
    'Rewind'      : 'ic_media_rew',
    'FastForward' : 'ic_media_ff',
    'Undo'        : '',
    'Redo'        : '',
    'PageCurl'    : '',
  ]

}