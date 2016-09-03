//package Bla1AI;
import com.springrts.ai.oo.clb.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.Float;
import java.lang.Math;
/**
 * Decides which units the AI will use, all methods here return types of units.
 * @author Richard Nai
 */
public class UnitDecider
{
    private static List<UnitDef> unitDefs;
    private static List<UnitDef> EMakers;
    private static List<UnitDef> Mexes;
    private static List<UnitDef> builders;
    private static List<UnitDef> T1kBotFactories;
    private static List<UnitDef> T1vehFactories;
    private static List<UnitDef> T2kBotFactories;
    private static List<UnitDef> T2vehFactories;
    private static List<UnitDef> raiders;
    private static List<UnitDef> nanos;
    private static List<UnitDef> metalMakers;
    /**
     * Constructor for objects of class unitDecider
     */
    public UnitDecider()
    {
        unitDefs = CallbackHelper.getCallback().getUnitDefs();
        EMakers=findEMakers();
        Mexes = findMexes();
        builders = findBuilders();
        T1kBotFactories = findT1BotFactories();
        T1vehFactories = findT1VehFactories();
        T2kBotFactories = findT2BotFactories();
        T2vehFactories = findT2VehFactories();
        raiders = findRaiders();
        nanos = findNanos();
        metalMakers = findMetalMakers();
        CallbackHelper.say("unitDecider constructed");
    }

    /**
     * returns all the UnitDefs in the selected game
     */
    public static List<UnitDef> getAllUnitDefs(){
        return unitDefs;
    }

    /**
     * returns all the energy making buildings in the game
     */
    private static List<UnitDef> findEMakers(){
        try{
            CallbackHelper.say("Finding energy making buildings");
            List<UnitDef> energy = new ArrayList<UnitDef>();
            for (UnitDef def : unitDefs){
                String defname=def.getName();
                if (defname.equals("aafus")||defname.equals("armadvsol")||defname.equals("armfus")||defname.equals("armsolar")){
                    CallbackHelper.say("Found " + def.getName());
                    energy.add(def);
                }
                if (defname.equals("cafus")||defname.equals("coradvsol")||defname.equals("corfus")||defname.equals("corsolar")){
                    CallbackHelper.say("Found " + def.getName());
                    energy.add(def);
                }
                if ((defname.equals("tllcoldfus")) || (defname.equals("tlladvsolar")) || (defname.equals("tllmedfusion")) || (defname.equals("tllsolar")))
                {
                    CallbackHelper.say("Found " + def.getName());
                    energy.add(def);
                }
            }
            return energy;
        }
        catch(Exception ex){
            CallbackHelper.say("Error in findEMakers");
            CallbackHelper.say(ex.toString());
        }
        return null;
    }

    public static List<UnitDef> getEMakers(){
        return EMakers;
    }

    /**
     * returns a preselected group of builders
     */
    private static List<UnitDef> findBuilders(){
        CallbackHelper.say("Finding Builders");
        List<UnitDef> builders = new ArrayList<UnitDef>();
        for (UnitDef def : unitDefs){
            String defname=def.getName();
            if (defname.equals("armack")||defname.equals("armacv")||defname.equals("armck")||defname.equals("armcv")){
                CallbackHelper.say("Found " + def.getName());
                builders.add(def);
            }
            if (defname.equals("corack")||defname.equals("coracv")||defname.equals("corck")||defname.equals("corcv")){
                CallbackHelper.say("Found " + def.getName());
                builders.add(def);
            }
            if ((defname.equals("tllack")) || (defname.equals("tllacv")) || (defname.equals("tllck")) || (defname.equals("tllcv")))
            {
                CallbackHelper.say("Found " + def.getName());
                builders.add(def);
            }
        }
        return builders;
    }

    public static List<UnitDef> getBuilders(){
        return builders;
    }

    /**
     * returns a preselected group of metal extractors
     */
    private static List<UnitDef> findMexes(){
        try{
            CallbackHelper.say("Finding Mexes");
            List<UnitDef> mexes = new ArrayList<UnitDef>();
            for (UnitDef def : unitDefs){
                String defname=def.getName();
                if (defname.equals("armmex")||defname.equals("armmoho")){
                    CallbackHelper.say("Found " + def.getName());
                    mexes.add(def);
                }
                if (defname.equals("cormex")||defname.equals("cormoho")){
                    CallbackHelper.say("Found " + def.getName());
                    mexes.add(def);
                }
                if ((defname.equals("tllmex")) || (defname.equals("tllamex")))
                {
                    CallbackHelper.say("Found " + def.getName());
                    mexes.add(def);
                }
            }
            return mexes;
        }
        catch(Exception ex){
            CallbackHelper.say("Error in findMexes");
            CallbackHelper.say(ex.toString());
        }
        return null;
    }

    public static List<UnitDef> getMexes(){
        return Mexes;
    }

    public static List<UnitDef> decideFactory(){
        List<Float> heightMap=CallbackHelper.getCallback().getMap().getHeightMap();
        float total = 0;
        int ctr = 0;
        for(Float val: heightMap){
            total+=val;
            ctr++;
        }
        float average = total/ctr;
        total = 0;
        for(Float val: heightMap){
            total+=Math.abs(average-val);
        }
        float roughness=total/ctr;
        if(roughness<100)
            return T1vehFactories;
        else
            return T1kBotFactories;
        //eturn null;
    }

    private static List<UnitDef> findT1BotFactories(){
        try{
            CallbackHelper.say("Finding Factories");
            List<UnitDef> factories = new ArrayList<UnitDef>();
            for (UnitDef def : unitDefs){
                String defname=def.getName();
                if ((defname.equals("armlab")) || (defname.equals("corlab")) || (defname.equals("tlllab"))){
                    CallbackHelper.say("Found " + def.getName());
                    factories.add(def);
                }
            }
            return factories;
        }
        catch(Exception ex){
            CallbackHelper.say("Error in findFactories");
            CallbackHelper.say(ex.toString());
        }
        return null;
    }


    public static List<UnitDef> findT2BotFactories(){
        try{
            CallbackHelper.say("Finding Factories");
            List<UnitDef> factories = new ArrayList<UnitDef>();
            for (UnitDef def : unitDefs){
                String defname=def.getName();
                if ((defname.equals("armalab")) || (defname.equals("coralab")) || (defname.equals("tllalab"))){
                    CallbackHelper.say("Found " + def.getName());
                    factories.add(def);
                }
            }
            return factories;
        }
        catch(Exception ex){
            CallbackHelper.say("Error in findFactories");
            CallbackHelper.say(ex.toString());
        }
        return null;
    }

    public static List<UnitDef> getT2BotFactories(){
        return T2kBotFactories;
    }

    private static List<UnitDef> findT1VehFactories(){
        try{
            CallbackHelper.say("Finding Factories");
            List<UnitDef> factories = new ArrayList<UnitDef>();
            for (UnitDef def : unitDefs){
                String defname=def.getName();
                if (defname.equals("armvp")||defname.equals("corvp")||defname.equals("tllvp")){
                    CallbackHelper.say("Found " + def.getName());
                    factories.add(def);
                }
            }
            return factories;
        }
        catch(Exception ex){
            CallbackHelper.say("Error in findFactories");
            CallbackHelper.say(ex.toString());
        }
        return null;
    }

    public static List<UnitDef> findT2VehFactories(){
        try{
            CallbackHelper.say("Finding Factories");
            List<UnitDef> factories = new ArrayList<UnitDef>();
            for (UnitDef def : unitDefs){
                String defname=def.getName();
                if (defname.equals("armavp")||defname.equals("coravp")||defname.equals("tllavp")){
                    CallbackHelper.say("Found " + def.getName());
                    factories.add(def);
                }
            }
            return factories;
        }
        catch(Exception ex){
            CallbackHelper.say("Error in findFactories");
            CallbackHelper.say(ex.toString());
        }
        return null;
    }

    public static List<UnitDef> getT2VehFactories(){
        return T2vehFactories;
    }

    private static List<UnitDef> findRaiders(){
        try{
            CallbackHelper.say("Finding Raiders");
            List<UnitDef> raiders = new ArrayList<UnitDef>();
            for (UnitDef def : unitDefs){
                String defname=def.getName();
                if (defname.equals("armpw")||defname.equals("armflash")||defname.equals("armfast")||defname.equals("armlatnk")){
                    CallbackHelper.say("Found " + def.getName());
                    raiders.add(def);
                }
                if (defname.equals("corak")||defname.equals("corgator")||defname.equals("corpyro")||defname.equals("corseal")){
                    CallbackHelper.say("Found " + def.getName());
                    raiders.add(def);
                }
                if ((defname.equals("tllprivate")) || (defname.equals("tllburner")) || (defname.equals("tllares")) || (defname.equals("tllcoyote")))                {
                    CallbackHelper.say("Found " + def.getName());
                    raiders.add(def);
                }
            }
            return raiders;
        }
        catch(Exception ex){
            CallbackHelper.say("Error in findRaiders");
            CallbackHelper.say(ex.toString());
        }
        return null;
    }

    public static List<UnitDef> getRaiders(){
        return raiders;
    }

    public static List<UnitDef> findNanos(){
        try{
            CallbackHelper.say("Finding Nanos");
            List<UnitDef> nanos = new ArrayList<UnitDef>();
            for (UnitDef def : unitDefs){
                String defname=def.getName();
                if (defname.equals("armnanotc")||defname.equals("cornanotc")||defname.equals("tllnanotc")){
                    CallbackHelper.say("Found " + def.getName());
                    nanos.add(def);
                }
            }
            return nanos;
        }
        catch(Exception ex){
            CallbackHelper.say("Error in findNanos");
            CallbackHelper.say(ex.toString());
        }
        return null;
    }

    public static List<UnitDef> getNanos(){
        return nanos;
    }

    public static List<UnitDef> findMetalMakers(){
        try{
            CallbackHelper.say("Finding metal makers");
            List<UnitDef> mms = new ArrayList<UnitDef>();
            for (UnitDef def : unitDefs){
                String defname=def.getName();
                if (defname.equals("armmakr")||defname.equals("armamakr")||defname.equals("armmmakr")||defname.equals("armckmakr")||defname.equals("ametalmakerlvl2")){
                    CallbackHelper.say("Found " + def.getName());
                    mms.add(def);
                }
                if (defname.equals("cormakr")||defname.equals("coramakr")||defname.equals("cormmakr")||defname.equals("corhmakr")||defname.equals("cmetalmakerlvl2")){
                    CallbackHelper.say("Found " + def.getName());
                    mms.add(def);
                }
                if ((defname.equals("tllmm")) || (defname.equals("tllammakr")))
                {
                    CallbackHelper.say("Found " + def.getName());
                    mms.add(def);
                }
            }
            return mms;
        }
        catch(Exception ex){
            CallbackHelper.say("Error in metalMakers");
            CallbackHelper.say(ex.toString());
        }
        return null;
    }
    
    public static List<UnitDef> getMetalMakers(){
        return metalMakers;
    }
}
