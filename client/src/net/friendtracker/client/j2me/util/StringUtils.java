/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.friendtracker.client.j2me.util;

import java.util.Vector;

/**
 *
 * @author rafael
 */
public class StringUtils {

    public static Vector split(String original, String separator) {
        Vector nodes = new Vector();
        // Parse nodes into vector
        int index = original.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }

        // Get the last node
        nodes.addElement(original);

        return nodes;
    }

    public static String[] splitStr(String original, String separator) {
        Vector nodes = StringUtils.split(original, separator);

        // Create splitted string array
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++) {
                result[loop] = (String) nodes.elementAt(loop);
            }
        }
        nodes.setSize(0);

        return result;
    }
    protected static int[] possibleColors =
            new int[]{0x8080A0, 0x8098a0, 0x809fa0, 0x80a08d, 0x8da080, 0xa08c80, 0xa08096,
        0xf10aa8, 0x0a0af1, 0x4da413, 0xe3db57, 0x9b39ff
    };

    public static int getRandomMapColorFromString(String colorHashString) {
        /*
         * NOTE: In java there is a way to generate the RGB without the index,
         *  new Color(Color.HSBtoRGB(
                (Math.abs(colorHashString.hashCode())%30)/30F,
                0.5F+((Math.abs(colorHashString.hashCode())%10)/20F),
                0.66F+((Math.abs(colorHashString.hashCode())%10)/30F)));         
         */
        int result = Math.abs(colorHashString.hashCode());
        result = result % possibleColors.length;
        result = possibleColors[result];
        return result;
    }
}
