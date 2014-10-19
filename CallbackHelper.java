package Bla1AI;
import com.springrts.ai.oo.clb.*;
import com.springrts.ai.oo.AIFloat3;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
/**
 * A static class with the callback and all other meathods that should be accessible to all classes
 * in Bla1AI
 * 
 * @author Richard Nai
 * @version (a version number or a date)
 */
public class CallbackHelper
{
    private static OOAICallback engineCallback = null;
    /**
     * Constructor for objects of class CallbackHelper
     */
    public CallbackHelper(OOAICallback callback)
    {
        try{
            engineCallback = callback;
        }
        catch(Exception ex){
            say("Error in callback!");
        }
    }

    public static OOAICallback getCallback(){
        return engineCallback;
    }

    /**
     * sends a message to the game
     */
    public static void say(String msg){
        engineCallback.getGame().sendTextMessage(msg, 0);
    }

    public static float getDistanceBetween(AIFloat3 loc1, AIFloat3 loc2){
        float xDistance = loc1.x - loc2.x;
        float yDistance = loc1.y - loc2.y;
        float zDistance = loc1.z - loc2.z;
        float totalDistanceSquared = xDistance*xDistance + yDistance*yDistance + zDistance*zDistance;
        return totalDistanceSquared;
    }

    public static AIFloat3 randomPointAround(AIFloat3 mid, float distance){
        boolean notInRange = true;
        while(notInRange){
            AIFloat3 answer = mid;
            answer.x+= 2*(Math.random()-0.5)*distance;
            answer.z+=2*(Math.random()-0.5)*distance;
            if(Math.sqrt(getDistanceBetween(mid, answer))<=distance){
                return answer;
            }
        }
        return null;
    }

    public static AIFloat3 randomPoint(){
        return new AIFloat3((float)Math.random()*engineCallback.getMap().getWidth()*8, (float)0, (float)Math.random()*engineCallback.getMap().getHeight()*8);
    }

    public static UnitDef findMatch(List<UnitDef> list, Unit unit){
        ArrayList<UnitDef> similarOptions = new ArrayList<UnitDef>();
        for(UnitDef def: list){
            for(UnitDef uniDef: unit.getDef().getBuildOptions()){
                if(def.equals(uniDef)){
                    similarOptions.add(def);
                }
            }
        }
        return similarOptions.get(0);
    }
}
