/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.friendtracker.client.j2me.util;

import com.sun.midp.log.LogChannels;
import com.sun.midp.log.Logging;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 *
 * @author rafael
 */
public class RPCUtil {

    public static String getURLResponse(String url) {
        String result = "";

        try {
            HttpConnection cnx = (HttpConnection) Connector.open(url);
            InputStream is = cnx.openInputStream();

            if (cnx.getResponseCode() == 200) {
                byte[] buf = new byte[1024];
                int read = -1;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((read = is.read(buf, 0, 1024)) != -1) {
                    baos.write(buf, 0, read);
                }
                result = new String(baos.toByteArray()).trim();
                baos.close();
            }
            System.out.println(result);
            is.close();
            cnx.close();
            
            /*            
            StringBuffer b = new StringBuffer();
            int car;
            while ((car = is.read()) > 0) {
            b.append((char) car);
            }

            is.close();
            cnx.close();
            result = b.toString();
             */
        } catch (Exception e) {
            result = "err";
            if (Logging.REPORT_LEVEL <= Logging.ERROR) {
                Logging.report(Logging.ERROR, LogChannels.LC_JSR180, "RPCUtil.getURLResponse error: " + e.getMessage());
            }
        }

        return result;
    }
}
