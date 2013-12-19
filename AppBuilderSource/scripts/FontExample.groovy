import java.awt.*;
import javax.swing.*; 
import java.awt.*;    
import java.awt.geom.*;
import java.awt.event.*;

/** An example of using local fonts with Java2D in Java 1.2.
 *
 *  From tutorial on learning Java2D at
 *  http://www.apl.jhu.edu/~hall/java/Java2D-Tutorial.html
 *
 *  1998 Marty Hall, http://www.apl.jhu.edu/~hall/java/
 */

public class FontExample extends JPanel {
  public FontExample() {
    GraphicsEnvironment env =
      GraphicsEnvironment.getLocalGraphicsEnvironment();
    env.getAvailableFontFamilyNames();
    //setFont(new Font("Goudy Handtooled BT", Font.PLAIN, 100));
	//setFont(new Font("Helvetica Neue", Font.PLAIN, 60));
  }

  protected void drawBigString(Graphics2D g2d) {
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
						 RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    g2d.setPaint(Color.black);

	Font font = new Font("Helvetica Neue", Font.PLAIN, 40);
	//Font font = new Font("Helvetica Neue", Font.BOLD, 17);
	g2d.setFont(font);
    g2d.drawString("Java 2D", 25, 215);

	//int w = metrics.stringWidth("Java 2D");
	//g2d.drawString("Width = " + width, 25, 285);

	printFonts(g2d)
  }

  protected void printFonts(Graphics2D g2d) { 
	def names = [ 'Helvetica', 'Helvetica Neue' ]
	def sizes = [12, 15, 17 ]
	def styles = [ Font.PLAIN, Font.BOLD, Font.ITALIC ]
	for (name in names) { 
	  for (size in sizes) { 
		for (style in styles) { 
		  Font font = new Font(name, style, size) 
		  String styleName = ''
		  switch (style) { 
		  case Font.PLAIN: styleName = 'plain'; break;
		  case Font.BOLD: styleName = 'bold'; break;
		  case Font.ITALIC: styleName = 'italic'; break;
		  }
		  println "Font ${name} ${size}pt ${styleName}"
		  printFontMetrics(font, g2d)
		  println() 
		}
	  }
	}
  }

  protected void printFontMetrics(Font font, Graphics2D g2d) { 
	FontMetrics metrics = g2d.getFontMetrics(font);

	println "Height = " + metrics.height
	println "Ascent = " + metrics.ascent
	println "Descent = " + metrics.descent
	println "Leading = " + metrics.leading

	int[] width = new int[126 - 32 + 1]

	//for (char c in ('a' .. 'z') + ('A' .. 'Z')) { 
	for (char c in 32 .. 126) { 
	  //int cw = metrics.stringWidth(c as String)
	  int cw = metrics.charWidth(c)
	  width[c - 32] = cw
	  //println "Width of ${c} [${c as int}]\t${cw}"
	}

	println width
	
  }

  public void paintComponent(Graphics g) {
    //clear(g);
    Graphics2D g2d = (Graphics2D)g;
    //drawGradientCircle(g2d);
    drawBigString(g2d);
  }

  public static void main(String[] args) {
    WindowUtilities.openInJFrame(new FontExample(), 380, 400);
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