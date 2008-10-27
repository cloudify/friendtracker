/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.friendtracker.client.j2me.map;

import com.eightmotions.map.MapDisplay;
import com.eightmotions.map.Marker;
import com.eightmotions.map.Track;
import javax.microedition.lcdui.Display;

/**
 *
 * @author rafael
 */
public class MapController {

    private Display display;
    private MapDisplay mapDisplay;    
    private Track track;    
    private Marker myLastLocation = null;

    /* Current latitude and longitude updated by gps or rpc via */
    private float latitude=0;
    private float longitude=0;
    
    public MapController(Display display, MapDisplay mapDisplay, Track track) {
        this.setDisplay(display);
        this.setMapDisplay(mapDisplay);
        this.setTrack(track);
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }
    
    public MapDisplay getMapDisplay() {
        return mapDisplay;
    }

    public void setMapDisplay(MapDisplay mapDisplay) {
        this.mapDisplay = mapDisplay;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Marker getMyLastLocation() {
        return myLastLocation;
    }

    public void setMyLastLocation(Marker myLastLocation) {
        this.myLastLocation = myLastLocation;
    }
    
    public float getMyLongitude () {    
        return this.longitude;
    }
    
    public float getMyLatitude () {
        return this.latitude;
    }   
    
    public void setMyLatitude (float customLatitude) {
        this.latitude = customLatitude;
    }
    
    public void setMyLongitude (float customLongitude) {
        this.longitude = customLongitude;
    }
}
