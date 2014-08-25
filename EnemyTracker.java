package Bla1AI;
import com.springrts.ai.oo.AIFloat3;
import com.springrts.ai.oo.clb.*;
import java.util.ArrayList;
/**
 * Write a description of class EnemyTracker here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EnemyTracker
{
    ArrayList<AIFloat3> enemyLocs;

    public EnemyTracker()
    {
        enemyLocs = new ArrayList<AIFloat3>();
    }

    public void add(Unit unit){
        enemyLocs.add(unit.getPos());
    }
    
    public void remove(Unit unit){
        enemyLocs.remove(unit);
    }
    
    public boolean locsStored(){
        return enemyLocs.size()!=0;
    }
    
    public AIFloat3 getLocationClosestTo(AIFloat3 loc){
        if(enemyLocs.size()==0)
        return null;
        float minDistance = CallbackHelper.getDistanceBetween(loc, enemyLocs.get(0));
        AIFloat3 ans = enemyLocs.get(0);
        for(AIFloat3 location: enemyLocs){
            if(CallbackHelper.getDistanceBetween(loc, location)<minDistance){
                minDistance = CallbackHelper.getDistanceBetween(loc, location);
                ans = location;
            }
        }
        return ans;
    }
}
