/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.friendtracker.client.j2me;

/*
 * MyOpenPos.java
 *
 * Created on 15 juin 2008, 23:35
 */
import com.eightmotions.map.GenericOverlay;
import com.eightmotions.map.MapDisplay;
import com.eightmotions.map.MapDisplayListener;
import com.eightmotions.map.Marker;
import com.eightmotions.map.Track;
import com.eightmotions.util.UtilMidp;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import net.friendtracker.client.j2me.util.StringUtils;

/**
 *
 * @author  thomas
 * @version
 */
public class MapUpdatePosition extends MIDlet implements MapDisplayListener, CommandListener, Runnable {

    MapDisplay myMap;
    Display m_disp;
    Track t;
    String url;
    Marker lastLoc = null;

    public void startApp() {
        m_disp = Display.getDisplay(this);
        UtilMidp.checkMIDP(this);
        myMap = new MapDisplay();
        myMap.getCanvas().setFullScreenMode(false);
        m_disp.setCurrent(myMap.getCanvas());
        myMap.addListener(this);
        myMap.addCommand(new Command("Exit", Command.EXIT, 0));
        myMap.addCommand(new Command("LocateMe", Command.ITEM, 0));
        myMap.setCommandListener(this);
        myMap.setZoom(3);
        t = Track.getTrack(m_disp, "");
        myMap.displayTrack(t, false);

        GenericOverlay openStreeMap = new GenericOverlay("OpenStreeMap", "http://tile.openstreetmap.org/!z!/!x!/!y!.png");
        //new GenericOverlay("CloudMade","http://tiles.cloudmade.com/APIKEY/2/256/!z!/!x!/!y!.png");
        myMap.setMapProvider(0, openStreeMap);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public boolean oneLocationSelected(MapDisplay c, Marker theLoc) {
        return false;
    }

    public boolean mapKeyPressed(MapDisplay c, int code) {
        int key = c.getCanvas().getGameAction(code);
        System.out.println("code:" + code + " key:" + key);
        return true;
    }

    public void onMoveEnd(MapDisplay c) {
    }

    public void locateMe() {
        String info = "Longitude: " + myMap.getLon() + " Latitude: " + myMap.getLat();
        String host = "http://192.168.0.166:3000";
        //String host = "http://localhost:1234"; //Netcat tests
        url = host + "/api/update/?format=csv&userid=2&ln=" + myMap.getLon() + "&lt=" + myMap.getLat();

        Thread me = new Thread(this);
        me.start();

        myMap.setInfoOnScreen(info);
    }

    public void run() {
        try {
            System.out.println("starting:" + url);
            HttpConnection cnx = (HttpConnection) Connector.open(url);
            InputStream is = cnx.openInputStream();
            StringBuffer b = new StringBuffer();
            int car;
            while ((car = is.read()) != -1) {
                b.append((char) car);
            }
            System.out.println(b);
            is.close();
            cnx.close();
            String res = b.toString();
            if (res.startsWith("err")) {
                myMap.setInfoOnScreen("Cell not found");
            } else {
                Vector lines = StringUtils.split(res, "\n");
                if (lines.size() > 0) {
                    t.getWaypoints().setSize(0);
                    for (int loop = 0; loop < lines.size(); loop++) {
                        String line = (String) lines.elementAt(loop);
                        Vector cols = StringUtils.split(line, ",");
                        String id = (String) cols.elementAt(0);
                        String login = (String) cols.elementAt(1);
                        float lt = Float.parseFloat((String) cols.elementAt(2));
                        float ln = Float.parseFloat((String) cols.elementAt(3));

                        System.out.println("Userid('" + id + "') login('" + login + "') lt('" + lt + "') ln('" + ln + "')");
                        
                        
                        FriendLoc friendLoc = new FriendLoc();                        
                        t.addMarker(friendLoc);
                        
                        friendLoc.setLonLat(ln, lt);
                        //myMap.goTo(friendLoc); // sets the pointer to the specific poistion
                    }
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
            myMap.setInfoOnScreen(ex.toString());
        }
    }

    public void commandAction(Command c, Displayable displayable) {
        if (c.getCommandType() == Command.EXIT) {
            notifyDestroyed();
        } else {
            locateMe();
        }
    }

    class MyLoc extends Marker {

        public int range = 1000;

        public MyLoc() {
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
    
    class FriendLoc extends Marker {
        public int rage = 500;
        
        public FriendLoc() {
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
}