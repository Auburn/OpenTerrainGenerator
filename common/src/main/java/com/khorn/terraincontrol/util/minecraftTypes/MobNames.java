package com.khorn.terraincontrol.util.minecraftTypes;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains a lot of alternative mob names. The implementation should support
 * this names, along with the other names that are available on the current
 * platform.
 */
public enum MobNames
{
	BAT("Bat", "bat"),
	BLAZE("Blaze", "blaze"),
	CAVESPIDER("CaveSpider", "cavespider"),	
	CHICKEN("Chicken", "chicken"),
	COW("Cow", "cow"),
	CREEPER("Creeper", "creeper"),
	ENTITYHORSE("EntityHorse", "horse"),
	ENDERDRAGON("EnderDragon", "enderdragon"),
	ENDERMAN("Enderman", "enderman"),
	ENDERMITE("Endermite", "endermite"),
	GHAST("Ghast", "ghast"),
	GIANT("Giant", "giant", "giantzombie", "zombiegiant"),
	GUARDIAN("Guardian", "guardian"),
	LAVASLIME("LavaSlime", "magmaslime", "lavaslime", "magmacube"),
	MUSHROOMCOW("MushroomCow", "mushroomcow", "mooshroom"),
	OZELOT("Ozelot", "ozelot", "ocelot"),
	PIG("Pig", "pig"),
	PIGZOMBIE("PigZombie", "pigzombie", "zombiepigman"),
	POLARBEAR("PolarBear", "polarbear"),
	RABBIT("Rabbit", "rabbit"),
	SHEEP("Sheep", "sheep"),
	SHULKER("Shulker", "shulker"),
	SILVERFISH("Silverfish", "silverfish"),
	SKELETON("Skeleton", "skeleton"),	
	SLIME("Slime", "slime"),
	SNOWMAN("SnowMan", "snowman"),
	SPIDER("Spider", "spider"),
	SQUID("Squid", "squid"),
	VILLAGER("Villager", "villager"),
	VILLAGERGOLEM("VillagerGolem", "villagergolem", "irongolem"),
	WITCH("Witch", "witch"),
	WITHERBOSS("WitherBoss", "witherboss", "wither"),
	WOLF("Wolf", "wolf"),
	ZOMBIE("Zombie", "zombie");

    // Contains all aliases (alias, internalName)
    private static Map<String, String> mobAliases = new HashMap<String, String>();

    // Auto-register all aliases in the enum
    static
    {
        for (MobNames alt : MobNames.values())
        {
            register(alt.internalMinecraftName, alt.aliases);
        }
    }

    /**
     * Returns the internal name of the mob. If it can't be found, it returns
     * the alias.
     *
     * @param alias The alias.
     * @return The internal name, or if it can't be found, the alias.
     */
    public static String toInternalName(String alias)
    {
    	for(String key : mobAliases.keySet())
    	{
    		if(key.toLowerCase().trim().replace("entity","").replace("_","").equals(alias.toLowerCase().trim().replace("entity","").replace("_","")))
    		{
    			return mobAliases.get(key);
    		}
    	}
    	
        return alias;
    }

    /**
     * Register aliases here
     *
     * @param internalMinecraftName The internal Minecraft mob id, for example Ozelot
     * @param aliases               The alias, for example Ocelot
     */
    private static void register(String internalMinecraftName, String... aliases)
    {
        for (String alias : aliases)
        {
            mobAliases.put(alias, internalMinecraftName);
        }
    }


    private String[] aliases;
    private String internalMinecraftName;

    private MobNames(String internalMinecraftName, String... aliases)
    {
        this.internalMinecraftName = internalMinecraftName;
        this.aliases = aliases;
    }

    /**
     * Gets the internal Minecraft name of this mob.
     * @return The internal Minecraft name.
     */
    public String getInternalName()
    {
        return this.internalMinecraftName;
    }
}