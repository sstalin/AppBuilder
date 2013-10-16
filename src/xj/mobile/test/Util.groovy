package xj.mobile.test

class Util { 

  /* return true if the files are the same*/ 
  static boolean diff(dir1, dir2, file) { 
    def cmd = [ 'diff', '-bB', "${dir1}/${file}", "${dir2}/${file}" ]
    // ignore changes in blank lines and white spaces 
    //println cmd
    def p = cmd.execute()
    p.waitFor()
	//p.text
    boolean same = (p.exitValue() == 0 )
    if (!same) 
      println p.text //+ p.err.text
    return same
  }

}