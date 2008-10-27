/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.friendtracker.client.j2me.map.markers;

import com.eightmotions.map.Marker;
import javax.microedition.lcdui.Graphics;

/**
 * Marker that draws the locate me arc. 
 * 
 * @author rafael
 */
public class MyLocationMarker extends Marker {

    public int range = 1000;

    public MyLocationMarker() {
        super();
    }

    public void paint(Graphics g, int inpx, int inpy, int offx, int offy, int w, int h, int zoom) {
        int x = offx + w / 2 + (px - inpx) / (1 << zoom);
        int y = offy + h / 2 + (py - inpy) / (1 << zoom);
        g.setColor(0x8080A0);
        g.fillArc(x - 6, y - 6, 12, 12, 0, 360);
        g.setColor(0x0000C0);
        g.drawArc(x - 50, y - 50, 100, 100, 0, 360);        
    }
}