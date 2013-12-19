import java.awt.*;
import javax.swing.*; 
import java.awt.*;    
import java.awt.geom.*;
import java.awt.event.*;

public class FontExample extends JPanel {

  protected void printFontData(Graphics2D g2d) { 
	//	def names = [ 'Helvetica', 'Helvetica Neue' ]
	def names = [
	  'Helvetica', 
	  'Helvetica Neue',

	  'American Typewriter',
	  //'AppleGothic',
	  'Arial',
	  //'Arial Hebrew',
	  //'Arial Rounded MT Bold',
	  //'Bangla Sangam MN',
	  //'Baskerville',
	  //'Cochin',
	  'Courier',
	  'Courier New',
	  //'Devanagari Sangam MN',
	  'Droid Sans',
	  'Droid Sans Mono',
	  'Droid Serif',
	  //'Futura',
	  //'Geeza Pro',
	  //'Georgia',
	  //'Gujarati Sangam MN',
	  //'Gurmukhi MN',
	  //'Heiti SC',
	  //'Heiti TC',
	  //'Helvetica',
	  //'Helvetica Neue',
	  //'Hiragino Kaku Gothic Pro',
	  //'Kannada Sangam MN',
	  //'Malayalam Sangam MN',
	  //'Marker Felt',
	  //'Oriya Sangam MN',
	  //'Palatino',
	  //'Sinhala Sangam MN',
	  //'Tamil Sangam MN',
	  //'Telugu Sangam MN',
	  //'Thonburi',
	  'Times New Roman',
	  //'Trebuchet MS',
	  'Verdana',
	  'Zapfino'
	]

	//def sizes = [12, 15, 17 ]
	def size = 15
	for (name in names) { 
	  //for (size in sizes) { 
	  printFontMetrics(name, size, g2d)
	  println() 		
	}

	def cases = names.collect {
	  "    case '${it}': return ${it.replaceAll(' ', '_')}_${size}pt_Heights"
	}.join('\n')
	println """
  static byte[] getFontHeights(String name) {
    switch (name) {
${cases}
    default: return ${names[0].replaceAll(' ', '_')}_${size}pt_Heights
    }
  }
"""

	cases = names.collect {
	  "    case '${it}': return ${it.replaceAll(' ', '_')}_${size}pt_Widths"
	}.join('\n')
	println """
  static byte[][] getFontWidths(String name) {
    switch (name) {
${cases}
    default: return ${names[0].replaceAll(' ', '_')}_${size}pt_Widths
    }
  }
"""



  }

  protected void printFontMetrics(String name, int size, Graphics2D g2d) { 

	println "\n  // Font metrics data for ${name} ${size}pt"

	def varprefix = name.replaceAll(' ', '_') + "_${size}pt_"

	def styles = [ Font.PLAIN, Font.BOLD, Font.ITALIC ]

	for (style in styles) { 
	  Font font = new Font(name, style, size) 
	  String styleName = ''
	  switch (style) { 
	  case Font.PLAIN: styleName = 'plain'; break;
	  case Font.BOLD: styleName = 'bold'; break;
	  case Font.ITALIC: styleName = 'italic'; break;
	  }
	  //println "Font ${name} ${size}pt ${styleName}"

	  FontMetrics metrics = g2d.getFontMetrics(font);

	  //println "Height = " + metrics.height
	  //println "Ascent = " + metrics.ascent
	  //println "Descent = " + metrics.descent
	  //println "Leading = " + metrics.leading

	  if (style == Font.PLAIN) { 
		println "  static byte[]   ${varprefix}Heights = [ " +  metrics.height + ', ' + metrics.ascent + ', ' +
		  metrics.descent + ', ' + metrics.leading + ']  // height, ascent, descent, leading \n'

		println "  static byte[][] ${varprefix}Widths = ["

	  }

	  int[] width = new int[126 - 32 + 1]

	  //for (char c in ('a' .. 'z') + ('A' .. 'Z')) { 
	  for (char c in 32 .. 126) { 
		//int cw = metrics.stringWidth(c as String)
		int cw = metrics.charWidth(c)
		width[c - 32] = cw
		//println "Width of ${c} [${c as int}]\t${cw}"
	  }

	  println "    // ${styleName}"
	  println '    ' + width + ' as byte[],\n'
	}	
	println '  ]'
  }

  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D)g;

    printFontData(g2d);
  }

  public static void main(String[] args) {
    WindowUtilities.openInJFrame(new FontExample(), 100, 100);
  }
}

class WindowUtilities {

  /** Tell system to use native look and feel, as in previous
   *  releases. Metal (Java) LAF is the default otherwise.
   */

  public static void setNativeLookAndFeel() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch(Exception e) {
      System.out.println("Error setting native LAF: " + e);
    }
  }

  /** A simplified way to see a JPanel or other Container.
   *  Pops up a JFrame with specified Container as the content pane.
   */

  public static JFrame openInJFrame(Container content,
                                    int width,
                                    int height,
                                    String title,
                                    Color bgColor) {
    JFrame frame = new JFrame(title);
    frame.setBackground(bgColor);
    content.setBackground(bgColor);
    frame.setSize(width, height);
    frame.setContentPane(content);
    frame.addWindowListener(new ExitListener());
    frame.setVisible(true);
    return(frame);
  }

  /** Uses Color.white as the background color. */

  public static JFrame openInJFrame(Container content,
                                    int width,
                                    int height,
                                    String title) {
    return(openInJFrame(content, width, height, title, Color.white));
  }

  /** Uses Color.white as the background color, and the
   *  name of the Container's class as the JFrame title.
   */

  public static JFrame openInJFrame(Container content,
                                    int width,
                                    int height) {
    return(openInJFrame(content, width, height,
                        content.getClass().getName(),
                        Color.white));
  }
}

class ExitListener extends WindowAdapter {
  public void windowClosing(WindowEvent event) {
    System.exit(0);
  }
}