import java.awt.*;

/** Lists the names of all available fonts with Java2D in Java 1.2.
 *
 *  From tutorial on learning Java2D at
 *  http://www.apl.jhu.edu/~hall/java/Java2D-Tutorial.html
 *
 *  1998 Marty Hall, http://www.apl.jhu.edu/~hall/java/
 */

public class ListFonts {
  public static void main(String[] args) {
    GraphicsEnvironment env =
      GraphicsEnvironment.getLocalGraphicsEnvironment();
    String[] fontNames = env.getAvailableFontFamilyNames();
    System.out.println("Available Fonts:");
    for(int i=0; i<fontNames.length; i++)
      System.out.println("  " + fontNames[i]);
  }
}
