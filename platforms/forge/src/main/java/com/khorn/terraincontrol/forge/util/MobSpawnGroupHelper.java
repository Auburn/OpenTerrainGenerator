package com.khorn.terraincontrol.forge.util;

import com.khorn.terraincontrol.TerrainControl;
import com.khorn.terraincontrol.configuration.WeightedMobSpawnGroup;
import com.khorn.terraincontrol.configuration.standard.MojangSettings.EntityCategory;
import com.khorn.terraincontrol.logging.LogMarker;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

/**
 * Methods for conversion between mob lists in Minecraft and in the plugin.
 *
 */
public final class MobSpawnGroupHelper
{

    /**
     * Transforms our MobType into Minecraft's EnumCreatureType.
     * @param type Our type.
     * @return Minecraft's type.
     */
    private static EnumCreatureType toEnumCreatureType(EntityCategory type)
    {
        switch (type)
        {
            case MONSTER:
                return EnumCreatureType.MONSTER;
            case CREATURE:
                return EnumCreatureType.CREATURE;
            case AMBIENT_CREATURE:
                return EnumCreatureType.AMBIENT;
            case WATER_CREATURE:
                return EnumCreatureType.WATER_CREATURE;
        }
        throw new AssertionError("Unknown mob type: " + type);
    }

    /**
     * Transforms a single Minecraft BiomeMeta into our type.
     * @param biomeMeta Minecraft's type.
     * @return Our type.
     */
    private static WeightedMobSpawnGroup fromMinecraftGroup(Biome.SpawnListEntry biomeMeta)
    {
    	String mobName = fromMinecraftClass(biomeMeta.entityClass);
    	if(mobName == null)
    	{
    		return null;
    	}
        return new WeightedMobSpawnGroup(mobName, biomeMeta.itemWeight, biomeMeta.minGroupCount, biomeMeta.maxGroupCount);
    }

    /**
     * Gets the spawn list of the given biome for the given category.
     * @param biome The biome.
     * @param type  The category.
     * @return The spawn list for the given category.
     */
    public static List<WeightedMobSpawnGroup> getListFromMinecraftBiome(Biome biome, EntityCategory type)
    {
        Collection<SpawnListEntry> mobList = biome.getSpawnableList(toEnumCreatureType(type));
        return fromMinecraftList(mobList);
    }

    /**
     * Converts a BiomeMeta collection to a WeightedMobSpawnGroup list. This
     * method is the inverse of {@link #toMinecraftlist(Collection)}.
     * @param biomeMetas The BiomeMeta collection.
     * @return The WeightedMobSpawnGroup list.
     */
    static List<WeightedMobSpawnGroup> fromMinecraftList(Collection<SpawnListEntry> biomeMetas)
    {
        List<WeightedMobSpawnGroup> result = new ArrayList<WeightedMobSpawnGroup>();
        for (SpawnListEntry meta : biomeMetas)
        {
        	WeightedMobSpawnGroup wMSG = fromMinecraftGroup(meta);
        	if(wMSG != null)
        	{
        		result.add(wMSG);	
        	}  
        }
        return result;
    }

    /**
     * Converts a WeightedMobSpawnGroup collection to a BiomeMeta collection.
     * This method is the inverse of {@link #fromMinecraftList(Collection)}.
     * @param weightedMobSpawnGroups The WeighedMobSpawnGroup collection.
     * @return The BiomeMeta list.
     */
    public static List<SpawnListEntry> toMinecraftlist(Collection<WeightedMobSpawnGroup> weightedMobSpawnGroups)
    {
        List<SpawnListEntry> biomeList = new ArrayList<SpawnListEntry>();
        for (WeightedMobSpawnGroup mobGroup : weightedMobSpawnGroups)
        {
            Class<? extends EntityLiving> entityClass = toMinecraftClass(mobGroup.getInternalName());
            if (entityClass != null)
            {
                biomeList.add(new SpawnListEntry(entityClass, mobGroup.getWeight(), mobGroup.getMin(), mobGroup.getMax()));
            } else
            {
                // The .toLowerCase() is just a safeguard so that we get
                // notified if this.af is no longer the biome name
            	if(TerrainControl.getPluginConfig().SpawnLog)
            	{
            		TerrainControl.log(LogMarker.WARN, "Mob type {} not found", mobGroup.getInternalName());
            	}
            }
        }
        return biomeList;
    }

    /**
     * Gets the entity class corresponding to the given entity name. This
     * method is the inverse of {@link #fromMinecraftClass(Class)}.
     * @param mobName The mob name.
     * @return The entity class, or null if not found.
     */
    @SuppressWarnings("unchecked")
	static Class<? extends EntityLiving> toMinecraftClass(String mobName)
    {
    	Class<? extends EntityLiving> mob = (Class<? extends EntityLiving>) EntityList.NAME_TO_CLASS.get(mobName);
    	
    	// Sometimes modded mobs are registered in CLASS_TO_NAME but not in NAME_TO_CLASS. This seems to be a bug so fix it. 
    	if(mob == null)
    	{
    		for(Entry<Class<? extends Entity>, String> registeredMob : EntityList.CLASS_TO_NAME.entrySet())
    		{
    			if(mobName.equals(registeredMob.getValue()))
    			{
    				EntityList.NAME_TO_CLASS.put(registeredMob.getValue(), registeredMob.getKey());
    				mob = (Class<? extends EntityLiving>) registeredMob.getKey();
    				break;
    			}
    		}
    	}
    	
    	return mob; // Quick fix
    }

    /**
     * Gets the entity name corresponding to the given entity class.
     * @param entityClass The entity class.
     * @return The entity name, or null if not found.
     */
    private static String fromMinecraftClass(Class<?> entityClass)
    {
    	String mobName = EntityList.CLASS_TO_NAME.get(entityClass);
    	if(mobName != null)
    	{
    		return mobName;
    	}
    	TerrainControl.log(LogMarker.DEBUG, "No EntityRegistry entry found for class: " + entityClass);
        return null;
    }
}
