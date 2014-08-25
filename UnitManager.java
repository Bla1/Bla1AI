package Bla1AI;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import com.springrts.ai.oo.clb.*;
import com.springrts.ai.oo.AIFloat3;

/**
 * Manages all the units
 * @author Richard Nai
 * @version (a version number or a date)
 */
public class UnitManager
{
    private LinkedList<Unit> units;
    private LinkedList<Unit> unitsUnderConstruction;
    //private Unit fac = null;
    /**
     * Constructor for objects of class unitManager
     */
    public UnitManager()
    {
        try{
            units = new LinkedList<Unit>();
            unitsUnderConstruction = new LinkedList<Unit>();
        }
        catch(Exception ex){
            CallbackHelper.say("error in unitManager init");
            CallbackHelper.say(ex.toString());
        }
    }

    /**
     * adds the unit to the ArrayList of units
     */
    public void add(Unit unit){
        try{
            //CallbackHelper.say("Unit added " + unit.toString());
            units.add(unit);
            removeFromUnfinished(unit);
        }
        catch(Exception ex){
            CallbackHelper.say("Error in add");
            CallbackHelper.say(ex.toString());
        }
    }

    /**
     * removes the unit from the ArrayList of units
     */
    public void remove(Unit unit){
        units.remove(unit);
    }

    /**
     * adds the unit to the ArrayList of unfinished units
     */
    public void addToUnfinished(Unit unit){
        try{
            unitsUnderConstruction.add(unit);
        }
        catch(Exception ex){
            CallbackHelper.say("Error in add");
            CallbackHelper.say(ex.toString());
        }
    }

    /**
     * removes the unit to the ArrayList of unfinished units
     */
    public void removeFromUnfinished(Unit unit){
        unitsUnderConstruction.remove(unit);
    }

    /**
     * adds the unit to the ArrayList of unfinished units
     */
    public Unit getCommander(){
        for(Unit uni: getAllUnits()){
            if(uni.getDef().isBuilder()&&uni.getDef().getWeaponMounts().size()>0)
                return uni;
        }
        return null;
    }

    public LinkedList<Unit> getAllUnits(){
        return units;
    }

    //public ArrayList<AIFloat3> getAllUnitPos(){
    //    try{
    //        ArrayList<AIFloat3> ans = new ArrayList<AIFloat3>();
    //        for(Unit uni: units){
    //            ans.add(uni.getPos());
    //        }
    //        return ans;
    //    }
    //    catch(Exception ex){
    //        CallbackHelper.say("Error in getAllUnitPos");
    //    }
    //    return null;
    //}
         /**
     * returns a reference to all the unfinished, LinkedList because iterating through all of them is faster
     */
    public LinkedList<Unit> getAllUnfinishedUnits(){
        return unitsUnderConstruction;
    }
    
    public Unit getNextEBuildingUnderConstruction(){
        for(Unit uni: unitsUnderConstruction){
            for(UnitDef mker: UnitDecider.getEMakers()){
                if(uni.getDef().equals(mker))
                    return uni;
            }
        }
        return null;
    }
    
    public Unit getNextNanoUnderConstruction(){
        for(Unit uni: unitsUnderConstruction){
            for(UnitDef nano: UnitDecider.getNanos()){
                if(uni.getDef().equals(nano))
                    return uni;
            }
        }
        return null;
    }
    
    /**
     * gets the next idle builder
     */
    public Unit getNextBuilder(){
        for(Unit uni: units){
            if(uni.getDef().isBuilder()&&uni.getCurrentCommands().size()==0&&uni.getSpeed()>0)
                return uni;
        }
        return null;
    }

    /**
     * gets the next idle builder, excluding the commander
     */
    public Unit getNextBuilderNotCom(){
        for(Unit uni: units){
            if(uni.getDef().isBuilder()&&uni.getCurrentCommands().size()==0&&uni.getSpeed()>0&&uni.getDef().getWeaponMounts().size()==0)
                return uni;
        }
        return null;
    }

    //public Unit getFirstOccupiedBuilder(){
    //    for(Unit uni: units){
    //        if(uni.getDef().isBuilder()&&uni.getCurrentCommands().size()>0&&uni.getSpeed()>0)
    //            return uni;
    //    }
    //    return null;
    //}
    /**
     * returns references to all builders, LinkedList because iterating through all of them is faster
     */
    public LinkedList<Unit> getAllBuilders(){
        LinkedList<Unit> ans = new LinkedList<Unit>();
        for(Unit uni: units){
            if(uni.getDef().isBuilder()&&uni.getSpeed()>0)
                ans.add(uni);
        }
        return ans;
    }

    /**
     * returns references too all idling builders
     */
    public LinkedList<Unit> getAllIdleBuilders(){
        LinkedList<Unit> ans = new LinkedList<Unit>();
        for(Unit uni: units){
            if(uni.getDef().isBuilder()&&uni.getSpeed()>0&&uni.getCurrentCommands().size()==0)
                ans.add(uni);
        }
        return ans;
    }
    
    //public LinkedList<Unit> getAllUnoccupiedBuilders(){
    //    LinkedList<Unit> ans = new LinkedList<Unit>();
    //   for(Unit uni: units){
    //       if(uni.getDef().isBuilder()&&uni.getCurrentCommands().size()==0&&uni.getSpeed()>0)
    //            ans.add(uni);
    //    }
    //    return ans;
    //}

    public Unit getMostExpensiveBuilder(){
        float metalCost=0;
        Unit mostExpensive=null;
        for(Unit uni: getAllIdleBuilders()){
            if(uni.getDef().getCost(CallbackHelper.getCallback().getResourceByName("Metal"))>metalCost&&(uni.getDef().getWeaponMounts().size()==0)){
                metalCost=uni.getDef().getCost(CallbackHelper.getCallback().getResourceByName("Metal"));
                mostExpensive=uni;
            }
        }
        if(mostExpensive==null){
            for(Unit uni: getAllIdleBuilders()){
                if(uni.getDef().getCost(CallbackHelper.getCallback().getResourceByName("Metal"))>metalCost){
                    return uni;
                }
            }
        }
        return mostExpensive;
    }

    //public void assignAllBuildersExceptComToo(Unit unitToGaurd){
    //    for(Unit uni: getAllIdleBuilders()){
    //        if(uni.getCurrentCommands().size()==0)
    //            uni.guard(unitToGaurd, (short)0, 0);
    //    }
    //}
    /**
     * checks to see if there is a factory alive or not
     */
    public boolean checkForFac(){
        try{
            for(Unit uni: getAllUnits()){
                if(uni.getDef().isBuilder()&&uni.getDef().getSpeed()==0&&uni.getDef().getBuildDistance()==128){
                    return true;
                }
            }
            for(Unit uni: getAllUnfinishedUnits()){
                if(uni.getDef().isBuilder()&&uni.getDef().getSpeed()==0&&uni.getDef().getBuildDistance()==128){
                    return true;
                }
            }
            return false;
        }
        catch(Exception ex){
            CallbackHelper.say("Error in checkForFactory");
            CallbackHelper.say(ex.toString());
        }
        return false;
    }
    /**
     * finds the position of the factory
     */
    public AIFloat3 getFacPos(){
        try{
            return findFac().getPos();
        }
        catch(Exception ex){
            CallbackHelper.say("Error in getFacPos" + ex.toString());
        }
        return null;
    }
    /**
     * gets a reference to the factory
     */
    public Unit findFac(){
        try{
            for(Unit uni: getAllUnits()){
                if(uni.getDef().isBuilder()&&uni.getDef().getSpeed()==0){
                    return uni;
                }
            }
            for(Unit uni: getAllUnfinishedUnits()){
                if(uni.getDef().isBuilder()&&uni.getDef().getSpeed()==0){
                    return uni;
                }
            }
        }
        catch(Exception ex){
            CallbackHelper.say("Error in checkForFactory");
            CallbackHelper.say(ex.toString());
        }
        return null;
    }
    ///**
    // * tells all the idle nanos to do something.
    // */
    //public void taskAllIdleNanos(){
    //    for(Unit uni: getAllUnits()){
    //        if(uni.getDef().isBuilder()&&uni.getDef().getSpeed()==0&&uni.getDef().getBuildDistance()>128&&uni.getCurrentCommands().size()==0){
    //            uni.fight(uni.getPos(),(short)0,0);
    //        }
    //    }

    //}

    public Unit getNextRaider(){
        for(Unit uni: getAllUnits()){
            if(uni.getDef().getWeaponMounts().size()==1){
                return uni;
            }
        }
        return null;
    }
    
    public Unit getNextIdleRaider(){
        for(Unit uni: getAllUnits()){
            if(uni.getDef().getWeaponMounts().size()==1&&uni.getCurrentCommands().size()==0){
                return uni;
            }
        }
        return null;
    }
    
    public ArrayList<Unit> getAllRaiders(){ //returns all IDLE raiders
        ArrayList<Unit> raiders = new ArrayList<Unit>();
        for(Unit uni: getAllUnits()){
            if(uni.getDef().getWeaponMounts().size()==1&&uni.getCurrentCommands().size()==0){
                raiders.add(uni);
            }
        }
        return raiders;
    }
    
    public ArrayList<Unit> getAllOccupiedRaiders(){ //returns all IDLE raiders
        ArrayList<Unit> raiders = new ArrayList<Unit>();
        for(Unit uni: getAllUnits()){
            if(uni.getDef().getWeaponMounts().size()==1&&uni.getCurrentCommands().size()>0){
                raiders.add(uni);
            }
        }
        return raiders;
    }

    public Unit getClosestRaider(AIFloat3 loc){ //returns closest idle raider to a point
        ArrayList<Unit> raiders = getAllRaiders();
        if(raiders.size()==0)
            return null;
        float minDistance = CallbackHelper.getDistanceBetween(raiders.get(0).getPos(), loc);
        Unit closest = raiders.get(0);
        for(Unit uni: raiders){
            if(CallbackHelper.getDistanceBetween(uni.getPos(), loc)<minDistance){
                minDistance=CallbackHelper.getDistanceBetween(uni.getPos(), loc);
                closest = uni;
            }
        }
        return closest;
    }

    public int getNumCons(){
        int ans = 0;
        for(Unit uni: getAllUnits()){
            if(uni.getDef().isBuilder()&&uni.getDef().getWeaponMounts().size()==0&&uni.getDef().getSpeed()>0){
                ans++;
            }
        }
        return ans;
    }

    public int getNumRaiders(){
        int ans = 0;
        for(Unit uni: getAllUnits()){
            if(uni.getDef().getWeaponMounts().size()==1){
                ans++;
            }
        }
        return ans;
    }

    public int getNumNanos(){
        int ans = 0;
        for(Unit uni: getAllUnits()){
            if(uni.getDef().getBuildDistance()>128&&uni.getDef().getSpeed()==0)
                ans++;
        }
        return ans;
    }
}
