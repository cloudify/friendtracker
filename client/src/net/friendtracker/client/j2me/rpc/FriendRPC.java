/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.friendtracker.client.j2me.rpc;

import com.sun.midp.log.LogChannels;
import com.sun.midp.log.Logging;
import net.friendtracker.client.j2me.map.markers.FriendMarker;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import net.friendtracker.client.j2me.map.MapController;
import net.friendtracker.client.j2me.util.RPCUtil;
import net.friendtracker.client.j2me.util.StringUtils;

/**
 *
 * @author rafael
 */
public class FriendRPC {

    public static void paintFriends(MapController mapController) {
        //String host = "http://192.168.0.166:3000";
        //String host = "http://localhost:1234"; //Netcat tests
        String host = "http://localhost:3000";
        String url = host + "/api/update/?format=csv&userid=1&ln=" + mapController.getMyLongitude() + "&lt=" + mapController.getMyLatitude();

        try {
            String res = RPCUtil.getURLResponse(url);
            
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION)
                Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "Response from '"+url+"':\n"+res);
            
            if (res.startsWith("err")) {
                mapController.getMapDisplay().setInfoOnScreen("Cell not found");
            } else {
                Vector lines = StringUtils.split(res, "\n");
                if (lines.size() > 0) {
                    mapController.getTrack().getWaypoints().setSize(0);
                    for (int loop = 0; loop < lines.size(); loop++) {
                        String line = (String) lines.elementAt(loop);
                        if ("".equals(line)) {
                            break;
                        }
                        Vector cols = StringUtils.split(line, ",");
                        String id = (String) cols.elementAt(0);
                        String login = (String) cols.elementAt(1);
                        float lt = Float.parseFloat((String) cols.elementAt(2));
                        float ln = Float.parseFloat((String) cols.elementAt(3));

                        if (Logging.REPORT_LEVEL <= Logging.INFORMATION)
                            Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180,
                                    "Userid('" + id + "') login('" + login + "') lt('" + lt + "') ln('" + ln + "')");
                        
                        FriendMarker friendLoc = new FriendMarker(login);
                        mapController.getTrack().addMarker(friendLoc);

                        friendLoc.setLonLat(ln, lt);
                    }

                    mapController.getMapDisplay().repaint();
                }         
            }
        } catch (Exception ex) {
            if (Logging.REPORT_LEVEL <= Logging.WARNING) {
                Logging.report(Logging.WARNING, LogChannels.LC_JSR180, "paintFriends error: " + ex.getMessage());
            }

        }
    }
}
