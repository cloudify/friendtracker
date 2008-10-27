/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.friendtracker.client.j2me.map;

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
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import net.friendtracker.client.j2me.map.markers.MyLocationMarker;

/**
 *
 * @author  thomas
 * @version
 */
public class MapMIDlet extends MIDlet implements MapDisplayListener, CommandListener, Runnable {

    MapController mapController;
    PositionUpdaterThread positionUpdater;

    public void startApp() {
        Display display = Display.getDisplay(this);
        UtilMidp.checkMIDP(this);
        MapDisplay myMap = new MapDisplay();
        myMap.getCanvas().setFullScreenMode(false);
        display.setCurrent(myMap.getCanvas());
        myMap.addListener(this);
        myMap.addCommand(new Command("Exit", Command.EXIT, 0));
        myMap.addCommand(new Command("LocateMe", Command.ITEM, 0));
        myMap.setCommandListener(this);
        myMap.setZoom(3);
        Track track = Track.getTrack(display, "");
        myMap.displayTrack(track, false);

        GenericOverlay openStreeMap = new GenericOverlay("OpenStreeMap", "http://tile.openstreetmap.org/!z!/!x!/!y!.png");
        //new GenericOverlay("CloudMade","http://tiles.cloudmade.com/APIKEY/2/256/!z!/!x!/!y!.png");
        myMap.setMapProvider(0, openStreeMap);

        this.mapController = new MapController(display, myMap, track);
        positionUpdater = new PositionUpdaterThread(mapController);
        positionUpdater.start();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        positionUpdater.interrupt();
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
        /* If we are in a lower zoom mode, go into an higher mod */
        if (this.mapController.getMapDisplay().getZoom() < 9) {
            this.mapController.getMapDisplay().setZoom(14);
        }
        
        this.mapController.getMapDisplay().goTo(this.mapController.getMyLastLocation());        
    }

    public void run() {
    }

    public void commandAction(Command c, Displayable displayable) {
        if (c.getCommandType() == Command.EXIT) {
            notifyDestroyed();
        } else {
            locateMe();
        }
    }
}