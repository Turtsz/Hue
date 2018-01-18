/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huecontroller;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.wrapper.HueLog;
import com.philips.lighting.hue.sdk.wrapper.Persistence;
import com.philips.lighting.hue.sdk.wrapper.connection.BridgeConnectionType;
import com.philips.lighting.hue.sdk.wrapper.discovery.BridgeDiscovery;
import com.philips.lighting.hue.sdk.wrapper.discovery.BridgeDiscoveryCallback;
import com.philips.lighting.hue.sdk.wrapper.domain.Bridge;
import com.philips.lighting.hue.sdk.wrapper.domain.BridgeBuilder;
import com.philips.lighting.hue.sdk.wrapper.domain.ReturnCode;
import java.net.NetworkInterface;
import java.util.List;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.hue.sdk.utilities.impl.Color;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLight.PHLightColorMode;
import com.philips.lighting.model.PHLightState;
import huecontroller.HueProperties;
import static java.lang.Thread.sleep;

/**
 *
 * @author pi
 */
public class HueController {

    /**
     * @param args the command line arguments
     */
   
    
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
        while(true){
        allLights.forEach((light) ->{
                String lightIdentifer = light.getIdentifier();
                
                PHLightState lightState = new PHLightState();
                
                int color = (int) (Math.random()*65355);
                
                lightState.setOn(true);
                lightState.setHue(color);
    
                bridge.updateLightState(lightIdentifer, lightState, null);  // null is passed here as we are not interested in the response from the Bridge.
                try{
                sleep(1);
                }catch(Exception e){}
        
        });
        }
        
    
        
    }
    
}
