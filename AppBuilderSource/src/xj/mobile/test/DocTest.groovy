package xj.mobile.test

import org.junit.*
import static org.junit.Assert.*

import static xj.mobile.test.Util.*

class DocTest { 

  @Test
  void testTestSummary() { 
    try { 
      def p = "bin/testSummary.sh".execute()
      p.waitFor()
      p.text
    } catch (Exception e) { 
      e.printStackTrace()
    }

    def outdir = 'test/output'
    def refdir = 'test/output-Ref'

    boolean pass = true
    new File(outdir).list(
		{d, f -> f ==~ '.*\\.html' } as FilenameFilter
	).toList().each { file -> 
	  boolean okay = diff(outdir, refdir, file)
	  def result = okay ? 'Same' : '!!! Different'
	  println "Compare file ${file}: ${result}"
	  pass &= okay
    }
    assertTrue("Test Case: Test Summary", pass)
  }

  @Test
  void testLangDef() { 
    try { 
      def p = "bin/lang -def".execute()
      p.waitFor()
      p.text
    } catch (Exception e) { 
      e.printStackTrace()
    }

    def outdir = 'test/lang/def'
    def refdir = 'test/lang/def-Ref'

    boolean pass = true
    new File(outdir).list(
		{d, f -> f ==~ '.*\\.html' } as FilenameFilter
	).toList().each { file -> 
	  boolean okay = diff(outdir, refdir, file)
	  def result = okay ? 'Same' : '!!! Different'
	  println "Compare file ${file}: ${result}"
	  pass &= okay
    }
    assertTrue("Test Case: Test Summary", pass)
  }

}