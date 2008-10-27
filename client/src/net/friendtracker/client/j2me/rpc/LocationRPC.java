/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * See:
 * http://ajay-mobileappdev.blogspot.com/2008/05/getiing-network-information-without-gps.html
 */
package net.friendtracker.client.j2me.rpc;

import com.sun.midp.log.LogChannels;
import com.sun.midp.log.Logging;
import net.friendtracker.client.j2me.map.MapController;
import net.friendtracker.client.j2me.map.markers.MyLocationMarker;
import net.friendtracker.client.j2me.util.RPCUtil;

/**
 * TODO: Take a look at http://feblog.yahoo.net/2008/08/07/api-exploration-fun/
 * 
 * @author rafael
 */
public class LocationRPC {

    public static void updateLocation(final MapController mapController) {
        try {
            String cellid = null, mcc = null, mnc = null, lac = null;

            /* SonyEricsson platform, taken from opencellid example */
            if (System.getProperty("com.sonyericsson.net.cellid") != null && !"".equals(System.getProperty("com.sonyericsson.net.cellid"))) {
                cellid = System.getProperty("com.sonyericsson.net.cellid");
                mcc = System.getProperty("com.sonyericsson.net.cmcc");
                mnc = System.getProperty("com.sonyericsson.net.cmnc");
                lac = System.getProperty("com.sonyericsson.net.lac");
            } else if (System.getProperty("com.nokia.mid.cellid") != null && !"".equals(System.getProperty("com.nokia.mid.cellid"))) {
                /* Nokia platform, taken from:
                 * http://wiki.forum.nokia.com/index.php/TSJ000964_-_Series_40:_System_Properties_in_Operator_and_Manufacturer_domain
                 */

                /* Returns CELLID as string. This is current GSM cell identifier (CELLID). 
                 * Device should have SIM card and acquired service to return a CELLID value, otherwise returns NULL. 
                 */
                cellid = System.getProperty("com.nokia.mid.cellid");

                /* don't know if this is right:
                 * Returns string of current/active network (MCC/MNC). 
                 * Returns "" if not available (e.g. out of service) and null if the API is not supported. 
                 */
                mcc = System.getProperty("com.nokia.mid.networkid");

                /* Returns MCC/MNC as a 4 to 6 digit string (depending on Operator) */
                mnc = System.getProperty("com.nokia.mid.mnc");

                /* Returns LAC as string. This is current GSM location area code (LAC). 
                 * Device should have SIM card and acquired service to return a LAC value, otherwise returns NULL. 
                 */
                lac = System.getProperty("com.nokia.mid.lac");
            } else if (System.getProperty("CellID") != null && !"".equals(System.getProperty("CellID"))) {
                /*
                 * Tested on v3xx,v3x .for testing this code your application must be signed.
                 */
                cellid = System.getProperty("CellID");
                lac = System.getProperty("LocAreaCode");
                final String imsi = System.getProperty("IMSI");
                mcc = imsi.substring(0, 3);
                mnc = imsi.substring(3, 6);
            } else if (System.getProperty("phone.cid") != null && !"".equals(System.getProperty("phone.cid"))) {
                /* Motorola implementation for the java.lang.System.getProperty method
                 * will support additional system properties beyond what is outlined in the JSR 118
                 * specification and is controlled by a flex bit. These additional system properties can only be
                 * accessed by trusted MIDlets.
                 */
                cellid = System.getProperty("phone.cid");
                lac = System.getProperty("phone.lai");
                mcc = System.getProperty("phone.mcc");
                mnc = System.getProperty("phone.mnc");
                //System.getProperty("phone.imei");
                //System.getProperty("phone.ta");
            }

            /*
            mcc = "238";
            mnc = "02";
            cellid = "3F3A1B";
            lac = "0";
            */

            float lat = 0;
            float lon = 0;
            if (cellid != null && !"".equals(cellid)) {
                if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                    Logging.report(Logging.ERROR, LogChannels.LC_JSR180, "LocationRPC.updateLocation cellid:" + cellid + " mcc:" + mcc + " mnc:" + mnc + " lac:" + lac);
                }

                final String url = "http://www.opencellid.org/cell/get?cellid=" + Integer.parseInt(cellid, 16) + "&mcc=" + mcc + "&mnc=" + mnc + "&lac=" + Integer.parseInt(lac, 16) + "&fmt=txt";

                final String res = RPCUtil.getURLResponse(url);
                if (res.startsWith("err")) {
                    mapController.getMapDisplay().setInfoOnScreen("Cell not found");
                } else {
                    final int pos = res.indexOf(',');
                    final String latStr = res.substring(0, pos);
                    final int pos2 = res.indexOf(',', pos + 1);
                    final String lonStr = res.substring(pos + 1, pos2);
                    mapController.getMapDisplay().setInfoOnScreen(latStr + " " + lonStr);
                    lat = Float.parseFloat(latStr);
                    lon = Float.parseFloat(lonStr);
                    /* TODO: if it's tracking my position update it! 
                     myMap.goTo(lastLoc);*/
                }
            } else {
                lon = mapController.getMapDisplay().getLon();
                lat = mapController.getMapDisplay().getLat();
            }

            /*
             * Update the last location marker. 
             */
            if (mapController.getMyLastLocation() == null) {
                final MyLocationMarker lastLoc = new MyLocationMarker();
                mapController.setMyLastLocation(lastLoc);
                mapController.getTrack().addMarker(lastLoc);
            }

            mapController.getMyLastLocation().setLonLat(lon, lat);
            mapController.setMyLatitude(lat);
            mapController.setMyLongitude(lon);
            //mapController.getMapDisplay().setInfoOnScreen("Longitude: " + mapController.getMyLongitude() + " Latitude: " + mapController.getMyLatitude());

        } catch (Exception e) {
            if (Logging.REPORT_LEVEL <= Logging.ERROR) {
                Logging.report(Logging.ERROR, LogChannels.LC_JSR180, "LocationRPC.updateLocation error: " + e.getMessage());
            }
        }
    }
}
