/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.friendtracker.client.j2me.map.markers;

import com.eightmotions.map.Marker;
import javax.microedition.lcdui.Graphics;
import net.friendtracker.client.j2me.util.StringUtils;

/**
 * Marker that draws the user with the username.
 * 
 * @author rafael
 */
public class FriendMarker extends Marker {

    public int rage = 500;
    protected String login;
    protected int color;

    public FriendMarker(String login) {
        super();
        this.login = login;

        /* Calculate it here, instead of calculate everytime on the paint method */
        this.color = StringUtils.getRandomMapColorFromString(this.login);
    }

    public void paint(Graphics g, int inpx, int inpy, int offx, int offy, int w, int h, int zoom) {
        int x = offx + w / 2 + (px - inpx) / (1 << zoom);
        int y = offy + h / 2 + (py - inpy) / (1 << zoom);
        g.setColor(this.color);
        g.fillArc(x - 6, y - 6, 5, 5, 0, 360);
        g.drawString(this.login, x + 10, y, 0);
    }
}