package Bla1AI;
import com.springrts.ai.oo.clb.*;
import com.springrts.ai.oo.AIFloat3;
import java.util.List;
import java.util.ArrayList;
/**
 * This class manages the AI's factory and attack units. It decides what the factory will make, and where and when to attack
 * 
 * @author Richard Nai
 */
public class MilitaryManager
{
    private List<UnitDef> unitDefs;
    private List<UnitDef> bestFac;
    private int myID;
    private int numBuilders;
    private UnitManager manage;
    private ResourceManager rManage;
    private EnemyTracker tracker;
    /**
     * Constructor for objects of class attackDecider
     */
    public MilitaryManager(int ID, UnitManager manager, ResourceManager resManager, EnemyTracker track){
        try{
            rManage = resManager;
            numBuilders=10;
            myID = ID;
            manage=manager;
            tracker = track;
            bestFac = UnitDecider.decideFactory();
        }
        catch(Exception ex){
            CallbackHelper.say("Error in militaryManager init " + ex.toString());
        }
    }

    /**
     * called by update(), checks the status of the factory, and attack units
     */

    public void act(){
        try{
            if(manage.getAllRaiders().size()!=0&&manage.getAllOccupiedRaiders().size()/(double)(manage.getAllOccupiedRaiders().size()+manage.getAllRaiders().size())<0.1)
                manage.getNextIdleRaider().fight(CallbackHelper.randomPoint(), (short)0, 0);
            //if(manage.getNumRaiders()>100&&tracker.locsStored()){
            //    for(Unit uni: manage.getAllRaiders()){
            //        uni.fight(tracker.getLocationClosestTo(uni.getPos()), (short)0, 0);
            //    }
            //}
            if(manage.factories.size()==0)
                placeFactory();
            else{
                for(Unit fac: manage.factories){
                    if(fac.getCurrentCommands().size()==0){
                        float metal = rManage.getCurrentMetalStoragePercentage();
                        float energy = rManage.getCurrentEnergyStoragePercentage();
                        if(manage.getNumCons()<numBuilders){
                            fac.build(findBuilder(fac), fac.getPos(), 0, (short)0, 0);
                        }
                        else{// if(metal>0.3&&energy>0.3){
                            manage.getCommander().guard(fac, (short)0, 0);
                            fac.build(randomArmedUnit(fac), fac.getPos(), 0, (short)0, 0);
                        }
                    }
                }
            }
        }
        catch(Exception ex){
            CallbackHelper.say("Error in act " + ex.toString());
        }
    }

    public void setMaxCons(int num){
        numBuilders=num;
    }

    ///**
    //checks to see if a factory exists
    // */
    //public boolean factoryExists(){
    //    return fac!=null;
    //}

    ///**
    //resets the pointer to the factory
    // */
    //public void facDestroyed(){
    //    fac=null;
    //}

    /**
    finds which unitdef of a builder the factory can build
     */
    public UnitDef findBuilder(Unit fac){
        try{
            List<UnitDef> Ops = fac.getDef().getBuildOptions();
            for(UnitDef op: Ops){
                for(UnitDef uni: UnitDecider.getBuilders()){
                    if(op==uni){
                        return op;
                    }
                }
            }
        }
        catch(Exception ex){
            CallbackHelper.say("Error in findBuilder");
            CallbackHelper.say(ex.toString());
        }
        return null;
    }

    /**
    finds which unitdef of a builder the factory can build
     */
    public UnitDef randomArmedUnit(Unit fac){
        try{
            List<UnitDef> armedUnits = new ArrayList<UnitDef>();
            List<UnitDef> Ops = fac.getDef().getBuildOptions();
            for (UnitDef op : Ops) {
                if (op.getWeaponMounts().size() > 0) {
                    armedUnits.add(op);
                }
            }
            return (UnitDef)armedUnits.get((int)(Math.random() * armedUnits.size()));
        }
        catch (Exception ex)
        {
            CallbackHelper.say("Error in randomeArmedUnit " + ex.toString());
        }
        return null;
    }

    public int getNumBuilders(){//for testing purposes only
        return numBuilders;
    }

    /**
    gets a builder, and builds a factory
     */
    private void placeFactory(){
        try{
            if(manage.getNextBuilder()!=null){
                List<UnitDef> buildOps = manage.getNextBuilder().getDef().getBuildOptions();
                UnitDef facToBuild = null;
                for(UnitDef Op: buildOps){
                    for(UnitDef best: bestFac){
                        if(Op.equals(best))
                            facToBuild=best;
                    }
                }

                manage.getNextBuilder().build(facToBuild, manage.getNextBuilder().getPos(), 0, (short)0, 0);
            }
        }
        catch(Exception ex){
            CallbackHelper.say("Error in placeFactory");
            CallbackHelper.say(ex.toString());
        }
    }

    //public void upgradeToT2(){
    
    //}
}
