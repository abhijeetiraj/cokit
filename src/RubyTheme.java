/*
 * @(#)RubyTheme.java 1.3 99/07/22
 * 
 * Copyright (c) 1997-1999 by Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use, modify and redistribute this software in
 * source and binary code form, provided that i) this copyright notice and license appear on all copies of the software;
 * and ii) Licensee does not utilize the software in a manner which is disparaging to Sun.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS
 * AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL
 * OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY
 * TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * This software is not designed or intended for use in on-line control of aircraft, air traffic, aircraft navigation or
 * aircraft communications; or in the design, construction, operation or maintenance of any nuclear facility. Licensee
 * represents and warrants that it will not use or redistribute the Software for such purposes.
 */

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 * This class describes a theme using red colors.
 * 
 * @version 1.3 07/22/99
 * @author Jeff Dinkins
 */
public class RubyTheme extends DefaultMetalTheme
{

    public String getName()
    {
        return "Ruby";
    }

    private final ColorUIResource primary1 = new ColorUIResource(80, 10, 22);
    private final ColorUIResource primary2 = new ColorUIResource(193, 10, 44);
    private final ColorUIResource primary3 = new ColorUIResource(244, 10, 66);

    protected ColorUIResource getPrimary1()
    {
        return primary1;
    }

    protected ColorUIResource getPrimary2()
    {
        return primary2;
    }

    protected ColorUIResource getPrimary3()
    {
        return primary3;
    }

}
