/*
 * MyOpenPos.java
 *
 * Created on 15 juin 2008, 23:35
 */

import com.eightmotions.map.BufferImage;
import com.eightmotions.map.GenericOverlay;
import com.eightmotions.map.MapDisplay;
import com.eightmotions.map.MapDisplayListener;
import com.eightmotions.map.Marker;
import com.eightmotions.map.Track;
import com.eightmotions.util.UtilMidp;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 *
 * @author  thomas
 * @version
 */
public class MyOpenPos extends MIDlet implements MapDisplayListener,CommandListener,Runnable{
    MapDisplay myMap;
    Display   m_disp;
    Track t;
    String url;
    Marker lastLoc=null;
    public void startApp() {
        m_disp=Display.getDisplay(this);
        UtilMidp.checkMIDP(this);
        myMap=new MapDisplay();
        myMap.getCanvas().setFullScreenMode(false);
        m_disp.setCurrent(myMap.getCanvas());
        myMap.addListener(this);
        myMap.addCommand(new Command("Exit",Command.EXIT,0));
        myMap.addCommand(new Command("LocateMe",Command.ITEM,0));
        myMap.setCommandListener(this);
        myMap.setZoom(3);
        t=Track.getTrack(m_disp,"");
        myMap.displayTrack(t,false);
        
        GenericOverlay openStreeMap=new GenericOverlay("OpenStreeMap","http://tile.openstreetmap.org/!z!/!x!/!y!.png");
        myMap.setMapProvider(0,openStreeMap);
    }
    
    public void pauseApp() {
 }
    
    public void destroyApp(boolean unconditional) {
    }

    public boolean oneLocationSelected(MapDisplay c, Marker theLoc) {
        return false;
    }

    public boolean mapKeyPressed(MapDisplay c, int code) {
        int key=c.getCanvas().getGameAction(code);
        System.out.println("code:"+code+" key:"+key);
        return true;
    }

    public void onMoveEnd(MapDisplay c) {
    }

    public void locateMe()
    {
            // These properties are implemented on latest SonyEricsson phones
            // This does not work on others (Nokia, etc...)
            String cellid=System.getProperty("com.sonyericsson.net.cellid");
            String mcc= System.getProperty("com.sonyericsson.net.cmcc");
            String mnc= System.getProperty("com.sonyericsson.net.cmnc");
            String lac= System.getProperty("com.sonyericsson.net.lac");
            String info="This phone does not support CellID";
            // uncomment the next line if you want to do some tests...
            //mcc="238";mnc="02";cellid="3F3A1B";lac="0";
            if(cellid!=null){
                info="Cell:"+cellid+" mcc:"+mcc+" mnc:"+mnc+" lac:"+lac;
                url="http://www.opencellid.org/cell/get?cellid="+Integer.parseInt(cellid,16)+"&mcc="+mcc+"&mnc="+mnc+"&lac="+Integer.parseInt(lac,16)+"&fmt=txt";
                Thread t=new Thread(this);
                t.start();
            }
            myMap.setInfoOnScreen(info);          
    }
    public void run(){
        try {
            System.out.println("starting:"+url);
            HttpConnection cnx = (HttpConnection)Connector.open(url);
            InputStream is=cnx.openInputStream();
            StringBuffer b=new StringBuffer();
            int car;
            while( (car=is.read())!= -1){
                b.append((char)car);
            }
            System.out.println(b);
            is.close();
            cnx.close();
            String res=b.toString();
            if(res.startsWith("err")){
                myMap.setInfoOnScreen("Cell not found");
            }else{
                int pos=res.indexOf(',');
                String latStr=res.substring(0,pos);
                int pos2=res.indexOf(',',pos+1);
                String lonStr=res.substring(pos+1,pos2);
                myMap.setInfoOnScreen(latStr+" "+lonStr);
                float lat=Float.parseFloat(latStr);
                float lon=Float.parseFloat(lonStr);
                // If we are in a lower zoom mode, go into an higher mod
                if(myMap.getZoom()<9)myMap.setZoom(14);
                if(lastLoc==null){
                    lastLoc=new MyLoc();
                    t.addMarker(lastLoc);
                }
                lastLoc.setLonLat(lon,lat);
                myMap.goTo(lastLoc);
                
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            myMap.setInfoOnScreen(ex.toString());
        }
        
    }
    public void commandAction(Command c, Displayable displayable) {
        if(c.getCommandType()==Command.EXIT){
            notifyDestroyed();
        }else{
            locateMe();
        }
    }
    
    class MyLoc extends Marker{
        public int range=1000;
        public MyLoc(){
                super();
        }
    public void paint(Graphics g,int inpx,int inpy,int offx,int offy,int w,int h,int zoom){
        int x=offx+w/2+(px-inpx)/(1<<zoom);
        int y=offy+h/2+(py-inpy)/(1<<zoom);
        g.setColor(0x8080A0);
        g.fillArc(x-6,y-6,12,12,0,360);
        g.setColor(0x0000C0);
        g.drawArc(x-50,y-50,100,100,0,360);
    }
   }
}
