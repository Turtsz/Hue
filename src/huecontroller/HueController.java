/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huecontroller;

import java.util.List;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import static java.lang.Thread.sleep;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.hue.JSONArray;
import org.json.hue.JSONObject;

/**
 *
 * @author pi
 */
public class HueController {

    /**
     * @param args the command line arguments
     */
    
private static final String USER_AGENT = "Mozilla/5.0";   
    //private static final Token EMPTY_TOKEN = null;

static PHLightState[] states;
    
    public static void main(String[] args) {
        // TODO code application logic here

     PHHueSDK phHueSDK = PHHueSDK.create();
        
        
        HueProperties.loadProperties();

        Controller controller = new Controller();
        
        
        phHueSDK.getNotificationManager().registerSDKListener(controller.getListener());
        
        controller.findBridges();
        
        while(phHueSDK.getSelectedBridge() == null){}
        
        System.out.println("got bridge");
        
        phHueSDK = PHHueSDK.getInstance();
        
        
        
        
        PHBridge bridge = phHueSDK.getSelectedBridge();
        
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        
        String url = "https://www.instagram.com/spyro_the_white_dog/?__a=1";
     HashMap<Integer, Integer> allPicsLikes = new HashMap();
     HashMap<Integer, Integer> allPicsComm = new HashMap();
     int totalPosts = 0;
     int followBy = 0;
     while(true){
     try{
        URL obj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        
        con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
                BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
                String inputLine;
		StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
                in.close();
                JSONObject json = new JSONObject(response.toString());
                
                int followByCount = json.getJSONObject("user")
                                .getJSONObject("followed_by")
                                 .getInt("count");
                
                if(followBy < followByCount){
                followers(allLights, bridge);
                followBy = followByCount;
                }
                
                if(followBy > followByCount){
                followBy = followByCount;
                }
                
                JSONArray nodes = json.getJSONObject("user").getJSONObject("media").getJSONArray("nodes");
                int size = nodes.length();
                int postCount = json.getJSONObject("graphql").getJSONObject("user").getJSONObject( "edge_owner_to_timeline_media").getInt("count");
                if(postCount > totalPosts){
                totalPosts = postCount;
                allPicsLikes = new HashMap();
                allPicsComm = new HashMap();
                for(int i = 0; i < size; i++){
                    JSONObject pic = nodes.getJSONObject(i);
                    int likes = pic.getJSONObject("likes").getInt("count");
                    int comments = pic.getJSONObject("comments").getInt("count");
                    allPicsLikes.put(i, likes);
                    allPicsComm.put(i, comments);
                
                
                }
                
                }
                for(int i = 0; i < size; i++){
                    JSONObject pic = nodes.getJSONObject(i);
                    int likes = pic.getJSONObject("likes").getInt("count");
                    int comments = pic.getJSONObject("comments").getInt("count");
                    
                    if(allPicsLikes.get(i) < likes){
                        likes(allLights, bridge);
                        allPicsLikes.put(i, likes);
                    }
                    
                    if(allPicsComm.get(i) < comments){
                        comments(allLights, bridge);
                        allPicsComm.put(i, comments);
                    }
                
                
                
                }
        sleep(10000);
     }catch(Exception e){
     System.out.println("NETWORK ERROR");
     }
    
     
     }
        
        
    
        
    }
    
    static int color = 0;
    public static void followers(List<PHLight> allLights, PHBridge bridge){
        System.out.println("followers");
        states = new PHLightState[allLights.size()];
        
        getColor(allLights);
        color = 0;
        while(color < 65335 - 500){
            color += 300;
        allLights.forEach((light) ->{
                String lightIdentifer = light.getIdentifier();
                
                PHLightState lightState = new PHLightState();
                
                
                lightState.setOn(true);
                lightState.setHue(color);
                lightState.setSaturation(254);
                lightState.setBrightness(254);
    
                bridge.updateLightState(lightIdentifer, lightState, null);  // null is passed here as we are not interested in the response from the Bridge.
                try{
                    sleep(10);
                }catch(Exception e){
                }
        
        });
        }
        
        restoreColor(allLights, bridge);
    
    }
    
    public static void likes(List<PHLight> allLights, PHBridge bridge){
        System.out.println("likes");
        states = new PHLightState[allLights.size()];
        
        getColor(allLights);
        color = 3000;
        
        while(color  < 1600 || color > 3000){
            color += 300;
                if(color >= 65335 - 500){
                color = 0;
                }
        allLights.forEach((light) ->{
                String lightIdentifer = light.getIdentifier();
                
                PHLightState lightState = new PHLightState();
                
                lightState.setOn(true);
                lightState.setHue(color);
                lightState.setSaturation(254);
                lightState.setBrightness(254);
    
                bridge.updateLightState(lightIdentifer, lightState, null);  // null is passed here as we are not interested in the response from the Bridge.
              try{
                    sleep(10);
                }catch(Exception e){}
        
        });
        }
        
        color -= 500;
        
           while(color  < 1600 || color > 3000){
               color -= 300;
                if(color < 400){
                color = 65335;
                }
        allLights.forEach((light) ->{
                String lightIdentifer = light.getIdentifier();
                
                PHLightState lightState = new PHLightState();
                
                
                lightState.setOn(true);
                lightState.setHue(color);
                lightState.setSaturation(254);
                lightState.setBrightness(254);
    
                bridge.updateLightState(lightIdentifer, lightState, null);  // null is passed here as we are not interested in the response from the Bridge.
              try{
                    sleep(10);
                }catch(Exception e){}
        
        });
        }
        
        restoreColor(allLights, bridge);
    
    }
    
    public static void comments(List<PHLight> allLights, PHBridge bridge){
        states = new PHLightState[allLights.size()];
        getColor(allLights);
        j = 0;
        
        while(j < allLights.size()){
            color = (int) (Math.random()*65355);
        allLights.forEach((light) ->{
                String lightIdentifer = light.getIdentifier();
                
                PHLightState lightState = new PHLightState();
                
                
                
                lightState.setOn(true);
                lightState.setHue(color);
                lightState.setSaturation(254);
                lightState.setBrightness(254);
    
                bridge.updateLightState(lightIdentifer, lightState, null);  // null is passed here as we are not interested in the response from the Bridge.
               try{
                    sleep(10);
                }catch(Exception e){}
        
        });
        j++;
        }
        
        restoreColor(allLights, bridge);
    
    }
    static int j = 0;
    public static void getColor(List<PHLight> allLights){
        
        j = 0;
    allLights.forEach((light) ->{
        states[j++] = light.getLastKnownLightState();
        
    });
    }
    
    public static void restoreColor(List<PHLight> allLights, PHBridge bridge){
        j = 0;
    allLights.forEach((light) ->{
        String lightIdentifer = light.getIdentifier();
        
        bridge.updateLightState(lightIdentifer, states[j++], null);
        
    });
    }

}
