package Bla1AI;
import com.springrts.ai.AI;
import com.springrts.ai.oo.AIFloat3;
import com.springrts.ai.oo.OOAI;
import com.springrts.ai.oo.clb.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Main AI file
 * 
 * @author Richard Nai
 */
public class Bla1AI extends OOAI implements AI
{
    private UnitManager manager;
    private EconManager Emanager = null;
    private MilitaryManager Mmanage=null;
    private ResourceManager rManage = null;
    private EnemyTracker tracker = null;
    boolean assistMode;
    //boolean first = true;
    int teamID;
    /**
    The constructor is not actually used, the init() is.
     */
    public Bla1AI()
    {
    }

    /**
     * This meathod is called when the engine puts the AI in control of a team
     */
    public int init(int teamId, OOAICallback callback)
    {
        try
        {
            teamID = teamId;
            new CallbackHelper(callback);
            new UnitDecider();
            tracker = new EnemyTracker();
            manager = new UnitManager();
            rManage = new ResourceManager(this.teamID);
            Emanager = new EconManager(teamId, this.manager, this.rManage);
            Mmanage = new MilitaryManager(this.teamID, this.manager, this.rManage, this.tracker);
            assistMode = false;
        }
        catch (Exception ex)
        {
            CallbackHelper.say("Error in init");
            CallbackHelper.say(ex.toString());
        }
        return 0;
    }

    /**
     * This meathod is called by the engine every frame, approximately 30 times a second, for the AI to update itself.
     */
    public int update(int frame) {
        try{
            if(assistMode&&frame%10==0){
                Mmanage.assistMode();
            }
            else if(!assistMode){
                //CallbackHelper.say("running normal mode");
                Mmanage.act();
                Emanager.expandEcon();
            }
        }
        catch(Exception ex){
            CallbackHelper.say("Error in update");
            CallbackHelper.say(ex.toString());
        }
        return 0;
    }

    /**
     * This meathod is called by the engine every time a unit is created, and in the case of Bla1AI, it is added to the UnitManager
     */
    public int unitCreated(Unit unit, Unit Builder){
        try{
            manager.addToUnfinished(unit);
            //if(unit.getDef().isBuilder()&&unit.getDef().getSpeed()==0)
            //Mmanage.setFactory(true);
        }
        catch(Exception ex){
            CallbackHelper.say("Error in unitCreated");
            CallbackHelper.say(ex.toString());
        }
        return 0;
    }

    /**
     * This meathod is called by the engine every time a unit is finished, and in the case of Bla1AI, it is added to the UnitManager
     */
    public int unitFinished(Unit unit)
    {   try{
            manager.add(unit);
            for(UnitDef uni: UnitDecider.getMexes()){
                if(unit.getDef().equals(uni)){
                    Emanager.removeFromavailable(unit.getPos());
                }
            }
            for(UnitDef uni: UnitDecider.getNanos()){
                if(unit.getDef().equals(uni)){
                    AIFloat3 position = new AIFloat3(unit.getPos().x+100, unit.getPos().y, unit.getPos().z);
                    unit.patrolTo(position, (short)0, 0);
                }
            }
            if(unit.getDef().isBuilder()&&unit.getDef().getSpeed()==0&&unit.getDef().getBuildDistance()==128){
                manager.addToFactory(unit);
            }
        }
        catch(Exception ex){
            CallbackHelper.say("Error in unitFinished");
            CallbackHelper.say(ex.toString());
        }
        return 0;
    }

    /**
     * This meathod is called by the engine every time a message is sent by a player, which I used for debugging
     */
    public int message(int player, String message){
        try{
            if(message.toString().equals("Bla1AI assist")){
                if(assistMode == false){
                    assistMode = true;
                    CallbackHelper.say("Assist mode is " + assistMode);
                }
                else{
                    assistMode = false;
                    CallbackHelper.say("Assist mode is " + assistMode);
                }
            }
            if(message.substring(0,7).equals("Bla1AI ")){
                int numBuilders = Integer.parseInt(message.substring(7, message.length()));
                CallbackHelper.say("Setting max builders to " + numBuilders);
                Mmanage.setMaxCons(numBuilders);
            }
            //if(player==teamID){

            else if(message.equals("Bla1AI assistmode")){
                if(assistMode==true)
                    CallbackHelper.say("Assist mode is " + assistMode);
                else
                    CallbackHelper.say("Assist mode is " + assistMode);
            }
            //}
            return 0;
        }
        catch(Exception ex){
            //If message is too short, an exception will be thrown, but that will mean that player messages was not directed toward AI
            return 0;
        }
    }

    /**
     * This meathod is called by the engine every time a unit dies, in the case of Bla1AI, it is removed from the unit manager
     */
    public int unitDestroyed(Unit unit, Unit attacker) {
        try{
            if(unit.getTeam()==teamID){
                manager.remove(unit);
                manager.removeFromUnfinished(unit);
                //Mmanage.calculateNumBuilders();
                //if(unit.getDef().isBuilder()&&unit.getDef().getSpeed()==0){
                //Mmanage.setFactory(false);
                //    Mmanage.facDestroyed();
                //}
                for(UnitDef uni: UnitDecider.getMexes()){
                    if(unit.getDef().equals(uni)){
                        //CallbackHelper.say("found match"+unit.getPos().x+""+unit.getPos().z);
                        Emanager.addToAvailable(unit.getPos());
                    }
                }
                if(unit.getDef().isBuilder()&&unit.getDef().getSpeed()==0&&unit.getDef().getBuildDistance()==128){
                    manager.removeFromFactory(unit);
                }
            }
        }
        catch(Exception ex){
            CallbackHelper.say("Error in unitDestroyed " + ex.toString());
        }
        return 0;
    }

    /**
    called by the engine every time a unit is damaged, in the case of Bla1AI, a random number of raiders are selected to move to the area of attack
     */
    public int unitDamaged(Unit unit, Unit attacker, float damage, AIFloat3 dir, WeaponDef weaponDef, boolean paralyzer)
    {
        try{
            if(attacker!=null){
                double metalNeeded = (attacker.getHealth()/attacker.getMaxHealth())*attacker.getDef().getCost(CallbackHelper.getCallback().getResourceByName("Metal"));
                //CallbackHelper.say("Metal of attacker is initialized at: " + metalNeeded +"attacker: " + attacker.toString());
                for(Unit raider: manager.getAllRaiders()){
                    if(metalNeeded>0){
                        raider.fight(CallbackHelper.randomPointAround(attacker.getPos(),raider.getDef().getMaxWeaponRange()), (short)0, 0);
                        metalNeeded-=raider.getDef().getCost(CallbackHelper.getCallback().getResourceByName("Metal"));
                        //CallbackHelper.say("Sending 1 raider: " + metalNeeded);
                    }
                    else{
                        //CallbackHelper.say("breaking");
                        break;
                    }
                }

                //while(manager.getAllRaiders().size()>0&&metalNeeded>0){
                //    Unit raider = manager.getNextRaider();
                //    metalNeeded-=raider.getDef().getCost(CallbackHelper.getCallback().getResourceByName("Metal"));
                //    CallbackHelper.say("Sending 1 raider, metal of attacker is now: " + metalNeeded);
                //    raider.fight(attacker.getPos(), (short)0, 0);
                //}
            }
        }
        catch (Exception ex) {
            //CallbackHelper.say("Error in unitDamaged() "+ex.toString());
        }
        return 0;
    }

    /**
    called by the engine every time an enemy unit is seen, in the case of Bla1AI, the unit is added to the enemy tracker
     */
    public int enemyEnterLOS(Unit enemy){
        //if(enemy.getSpeed()==0)
        //    tracker.add(enemy);
        double metalNeeded = (enemy.getHealth()/enemy.getMaxHealth())*enemy.getDef().getCost(CallbackHelper.getCallback().getResourceByName("Metal"));
        for(Unit raider: manager.getAllRaiders()){
            if(metalNeeded>0){
                raider.fight(CallbackHelper.randomPointAround(enemy.getPos(),raider.getDef().getMaxWeaponRange()), (short)0, 0);
                metalNeeded-=raider.getDef().getCost(CallbackHelper.getCallback().getResourceByName("Metal"));
            }
            else{
                break;
            }
        }
        return 0;
    }

    /**
    called by the engine every time an enemy unit dies, in the case of Bla1AI, the unit is removed from the enemy tracker
     */
    public int enemyDestroyed(Unit enemy, Unit attacker){
        if(enemy.getSpeed()==0)
            tracker.remove(enemy);
        return 0;
    }

    public int unitGiven(Unit unit, int oldTeamId, int newTeamId){
        if(newTeamId==teamID){
            manager.add(unit);
        }
        return 0;
    }
    //public int unitMoveFailed(Unit unit){
    //    CallbackHelper.say(unit.getDef().getHumanName());
    //    return 0;
    //}
}