import java.awt.Container;
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;

/*
 * @CoKitHelp.java
 * 
 * @version 1.1 19/01/2002
 * 
 * @author Abhijit Iraj
 */

public class CoKitHelp extends JInternalFrame
{

    public CoKitHelp()
    {
        super("Help", true, true, true, true);

        // setFrameIcon( (Icon)UIManager.get("Tree.openIcon")); // PENDING(steve) need more general palce to get this
        // icon
        setBounds(200, 25, 400, 400);
        HtmlPane html = new HtmlPane();
        setContentPane(html);
    }

}


class HtmlPane extends JScrollPane implements HyperlinkListener
{
    JEditorPane html;

    public HtmlPane()
    {
        try
        {
            File f = new File("Help_Files/toc.html");
            String s = f.getAbsolutePath();
            s = "file:" + s;
            URL url = new URL(s);
            html = new JEditorPane(s);
            html.setEditable(false);
            html.addHyperlinkListener(this);

            JViewport vp = getViewport();
            vp.add(html);
        }
        catch (MalformedURLException e)
        {
            System.out.println("Malformed URL: " + e);
        }
        catch (IOException e)
        {
            System.out.println("IOException: " + e);
        }
    }

    /**
     * Notification of a change relative to a hyperlink.
     */
    public void hyperlinkUpdate(HyperlinkEvent e)
    {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
        {
            linkActivated(e.getURL());
        }
    }

    /**
     * Follows the reference in an link. The given url is the requested reference. By default this calls <a
     * href="#setPage">setPage</a>, and if an exception is thrown the original previous document is restored and a beep
     * sounded. If an attempt was made to follow a link, but it represented a malformed url, this method will be called
     * with a null argument.
     * 
     * @param u
     *            the URL to follow
     */
    protected void linkActivated(URL u)
    {
        Cursor c = html.getCursor();
        Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        html.setCursor(waitCursor);
        SwingUtilities.invokeLater(new PageLoader(u, c));
    }

    /**
     * temporary class that loads synchronously (although later than the request so that a cursor change can be done).
     */
    class PageLoader implements Runnable
    {

        PageLoader(URL u, Cursor c)
        {
            url = u;
            cursor = c;
        }

        public void run()
        {
            if (url == null)
            {
                // restore the original cursor
                html.setCursor(cursor);

                // PENDING(prinz) remove this hack when
                // automatic validation is activated.
                Container parent = html.getParent();
                parent.repaint();
            }
            else
            {
                Document doc = html.getDocument();
                try
                {
                    html.setPage(url);
                }
                catch (IOException ioe)
                {
                    html.setDocument(doc);
                    getToolkit().beep();
                }
                finally
                {
                    // schedule the cursor to revert after
                    // the paint has happended.
                    url = null;
                    SwingUtilities.invokeLater(this);
                }
            }
        }

        URL url;
        Cursor cursor;
    }

}
