package Bla1AI;
import com.springrts.ai.oo.clb.Resource;
/**
 * Returns information on the economy of the AI.
 * 
 * @author Richard Nai 
 * @version (a version number or a date)
 */
public class ResourceManager
{
    int teamid;
    public ResourceManager(int teamID){
        teamid = teamID;
    }
    
    public float getMetalIncome(){
        return CallbackHelper.getCallback().getGame().getTeamResourceIncome(teamid, 0);
    }
    
    public float getMetalUsage(){
        return CallbackHelper.getCallback().getGame().getTeamResourceUsage(teamid, 0);
    }
    
    public float getMetalUsagePercentage(){
        return getMetalIncome()/getMetalUsage();
    }
    
    public float getEnergyIncome(){
        return CallbackHelper.getCallback().getGame().getTeamResourceIncome(teamid, 1);
    }
    
    public float getEnergyUsage(){
        return CallbackHelper.getCallback().getGame().getTeamResourceUsage(teamid, 1);
    }
    
    public float getEnergyUsagePercentage(){
        return getEnergyIncome()/getEnergyUsage();
    }
    
    public float getCurrentMetal(){
        return CallbackHelper.getCallback().getGame().getTeamResourceCurrent(teamid, 0);
    }
    
    public float getCurrentMetalMax(){
        return CallbackHelper.getCallback().getGame().getTeamResourceStorage(teamid, 0);
    }
    
    public float getCurrentMetalStoragePercentage(){
        return getCurrentMetal()/getCurrentMetalMax();
    }
    
    public float getCurrentEnergy(){
        return CallbackHelper.getCallback().getGame().getTeamResourceCurrent(teamid, 1);
    }
    
    public float getCurrentEnergyMax(){
        return CallbackHelper.getCallback().getGame().getTeamResourceStorage(teamid, 1);
    }
    
    public float getCurrentEnergyStoragePercentage(){
        return getCurrentEnergy()/getCurrentEnergyMax();
    }
    
    public static Resource getMetalReference(){
        return CallbackHelper.getCallback().getResourceByName("Metal");
    }
    
    public static Resource getEnergyReference(){
        return CallbackHelper.getCallback().getResourceByName("Energy");
    }
}
