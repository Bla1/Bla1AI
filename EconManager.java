package Bla1AI;
import com.springrts.ai.oo.clb.*;
import java.util.List;
import java.util.ArrayList;
import com.springrts.ai.oo.AIFloat3;
/**
 * Manages all the cons of the AI. Instructs them to build economic structures accordingly
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EconManager
{
    // instance variables - replace the example below with your own
    private int teamid;
    private int numCons;
    private UnitManager uManage;
    private ResourceManager rManage;
    Resource metal= CallbackHelper.getCallback().getResourceByName("Metal");
    List<AIFloat3> availableMetalExtractors= null;
    /**
     * Constructor for objects of class econManager
     */
    public EconManager(int teamID, UnitManager manager, ResourceManager resManager)
    {
        numCons = 10;
        rManage = resManager;
        teamid = teamID;
        uManage=manager;
        availableMetalExtractors = CallbackHelper.getCallback().getMap().getResourceMapSpotsPositions(metal);
    }

    /**
     * called by act, instructs builders to build economic structures according to the current economic standing
     */
    public void expandEcon(){
        try{
            //float metalUsagePercentage = rManage.getMetalUsagePercentage();
            //float energyUsagePercentage = rManage.getEnergyUsagePercentage();
            float metal = rManage.getCurrentMetalStoragePercentage();
            float energy = rManage.getCurrentEnergyStoragePercentage();

            if(uManage.getNextBuilder()!=null){
                if(metal>0.4&&energy>0.4){
                    if(uManage.getNextBuilderNotCom()!=null&&uManage.getNumNanos()<numCons){
                        boolean notInRange = true;
                        Unit uni = uManage.getNextBuilderNotCom();
                        AIFloat3 goodLoc = null;
                        while(notInRange){
                            AIFloat3 loc = uManage.getFacPos();
                            loc.x+= 2*(Math.random()-0.5)*CallbackHelper.findMatch(UnitDecider.getNanos(), uni).getBuildDistance();
                            //loc.y+= 2*(Math.random()-0.5)*findAppropriateNano(uni).getBuildDistance();
                            loc.z+= 2*(Math.random()-0.5)*CallbackHelper.findMatch(UnitDecider.getNanos(), uni).getBuildDistance();
                            if(Math.sqrt(CallbackHelper.getDistanceBetween(uManage.getFacPos(),loc))<=CallbackHelper.findMatch(UnitDecider.getNanos(), uni).getBuildDistance()){
                                goodLoc=loc;
                                break;
                            }
                        }
                        if(uManage.getNextNanoUnderConstruction()!=null)
                            uni.repair(uManage.getNextNanoUnderConstruction(), (short)0, 0);
                        else
                            uni.build(CallbackHelper.findMatch(UnitDecider.getNanos(), uni), goodLoc,0,(short)0, 0);
                    }
                }
                else if(metal>energy&&energy<0.95){
                    Unit uni = uManage.getMostExpensiveBuilder();
                    if(uManage.getNextEBuildingUnderConstruction()!=null)
                        uni.repair(uManage.getNextEBuildingUnderConstruction(),(short)0, 0);
                    else
                        uni.build(CallbackHelper.findMatch(UnitDecider.getEMakers(), uni),uni.getPos(),0,(short)0, 0);
                }
                else if(availableMetalExtractors.size()>0){
                    placeMex(uManage.getNextBuilder());
                }
                else{
                    Unit uni = uManage.getMostExpensiveBuilder();
                    uni.build(CallbackHelper.findMatch(UnitDecider.getMetalMakers(), uni),uni.getPos(),0,(short)0, 0);
                }
            }
        }
        catch(Exception ex){
            CallbackHelper.say("Error in expandEcon "+ex.toString());
        }

    }

    ///**
    // * finds the UnitDef of an energy building that a builder can build.
    // */
    //public UnitDef findAppropriateEnergy(Unit uni){
    //    try{
    //        List<UnitDef> uniBuildEnergyOptions = uni.getDef().getBuildOptions();
    //        ArrayList<UnitDef> similarOptions = new ArrayList<UnitDef>();
    //        for(UnitDef def: UnitDecider.getEMakers()){
    //            for(UnitDef uniDef: uniBuildEnergyOptions){
    //                if(def.equals(uniDef)){
    //                    similarOptions.add(def);
    //                }
    //            }
    //        }
    //        return similarOptions.get(0);
    //    }
    //    catch(Exception ex){
    //        CallbackHelper.say("Error in findApprpriateEnergy");
    //        CallbackHelper.say(ex.toString());
    //    }
    //    return null;
    //}

    ///**
    // * finds the UnitDef of an metal mine building that a builder can build.
    // */
    //public UnitDef findAppropriateMetal(Unit uni){
    //    try{
    //        List<UnitDef> uniBuildMetalOptions = uni.getDef().getBuildOptions();
    //        ArrayList<UnitDef> similarOptions = new ArrayList<UnitDef>();
    //        for(UnitDef def: UnitDecider.getMexes()){
    //           for(UnitDef uniDef: uniBuildMetalOptions){
    //                if(def.equals(uniDef)){
    //                    similarOptions.add(def);
    //                }
    //            }
    //        }
    //        return similarOptions.get(0);
    //    }
    //    catch(Exception ex){
    //        CallbackHelper.say("Error in findApprpriateMetal");
    //        CallbackHelper.say(ex.toString());
    //    }
    //    return null;
    //}

    ///**
    // * finds the UnitDef of a nano tower that a builder can build.
    // */
    //public UnitDef findAppropriateNano(Unit uni){
    //    try{
    //        List<UnitDef> uniBuildMetalOptions = uni.getDef().getBuildOptions();
    //        ArrayList<UnitDef> similarOptions = new ArrayList<UnitDef>();
    //        for(UnitDef def: UnitDecider.getNanos()){
    //            for(UnitDef uniDef: uniBuildMetalOptions){
    //                if(def.equals(uniDef)){
    //                    similarOptions.add(def);
    //                }
    //            }
    //        }
    //        return similarOptions.get(0);
    //    }
    //    catch(Exception ex){
    //        CallbackHelper.say("Error in findAppropriateNano");
    //        CallbackHelper.say(ex.toString());
    //    }
    //    return null;
    //}

    /**
     * finds the closest availale metal spot, and builds a extractor there
     */
    public void placeMex(Unit unit){
        float bestDistance = CallbackHelper.getDistanceBetween(unit.getPos(), availableMetalExtractors.get(0));
        AIFloat3 bestLoc = availableMetalExtractors.get(0);
        for(AIFloat3 loc:availableMetalExtractors){
            if(CallbackHelper.getDistanceBetween(unit.getPos(), loc)<bestDistance){
                bestDistance = CallbackHelper.getDistanceBetween(unit.getPos(), loc);
                bestLoc=loc;
            }
        }
        uManage.getNextBuilder().build(CallbackHelper.findMatch(UnitDecider.getMexes(),uManage.getNextBuilder()), bestLoc,0, (short)0, 0);
    }

    /**
     * adds the location to the ArrayList of available metal spots
     */
    public void addToAvailable(AIFloat3 loc){
        availableMetalExtractors.add(loc);
    }

    /**
     * removes the location to the ArrayList of available metal spots
     */
    public void removeFromavailable(AIFloat3 loc){
        for(int ind = availableMetalExtractors.size()-1; ind>=0; ind--){
            if(isOnTopOf(loc, availableMetalExtractors.get(ind))){
                availableMetalExtractors.remove(ind);
                return;
            }
        }
        CallbackHelper.say("Error in removeFromavailalbe");
    }

    /**
     * checks to see if the 2 locations are on top of each other
     */
    public boolean isOnTopOf(AIFloat3 loc1, AIFloat3 loc2){
        boolean x = Math.abs(loc1.x-loc2.x)<10;
        boolean z = Math.abs(loc1.z-loc2.z)<10;
        return x&&z;
    }

    public void setNumCons(int num){
        numCons = num;
    }
}
