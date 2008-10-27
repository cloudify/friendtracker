/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.friendtracker.client.j2me.map;

import net.friendtracker.client.j2me.rpc.FriendRPC;
import net.friendtracker.client.j2me.rpc.LocationRPC;

/**
 * Doing map triangulation + friend updation on the same thread for now, in order to use less resources. 
 * 
 * @author rafael
 */
public class PositionUpdaterThread extends Thread {

    private MapController mapController;

    public PositionUpdaterThread(MapController mapController) {
        this.mapController = mapController;
    }

    public void run() {
        super.run();
        long lastFriendUpdateTime = 0;
        while (true) {
            try {
                long currTime = System.currentTimeMillis();

                /*
                 * Actual position update. 
                 */
                if (lastFriendUpdateTime + (10*1000) < currTime) {
                    LocationRPC.updateLocation(mapController);
                }

                /*
                 * Firend position update. 
                 */
                if (lastFriendUpdateTime + (10*1000) < currTime) {
                    FriendRPC.paintFriends(this.mapController);
                    lastFriendUpdateTime = currTime;
                }
                
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
