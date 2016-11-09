package cn.nukkit;

import cn.nukkit.utils.TextFormat;

import java.util.HashMap;

/**
 * Created by CreeperFace on 9. 11. 2016.
 */
public class Achievement {

    public static final HashMap<String, AchievementEntry> achievements = new HashMap<String, AchievementEntry>() {
        {
            put("mineWood", new AchievementEntry("Getting Wood"));
            put("buildWorkBench", new AchievementEntry("Benchmarking", "mineWood"));
            put("buildPickaxe", new AchievementEntry("Time to Mine!", "buildWorkBench"));
            put("buildFurnace", new AchievementEntry("Hot Topic", "buildPickaxe"));
            put("acquireIron", new AchievementEntry("Acquire hardware", "buildFurnace"));
            put("buildHoe", new AchievementEntry("Time to Farm!", "buildWorkBench"));
            put("makeBread", new AchievementEntry("Bake Bread", "buildHoe"));
            put("bakeCake", new AchievementEntry("The Lie", "buildHoe"));
            put("buildBetterPickaxe", new AchievementEntry("Getting an Upgrade", "buildPickaxe"));
            put("buildSword", new AchievementEntry("Time to Strike!", "buildWorkBench"));
            put("diamonds", new AchievementEntry("DIAMONDS!", "acquireIron"));
        }
    };

    public static boolean broadcast(Player player, String achievementId) {
        if (!achievements.containsKey(achievementId)) {
            return false;
        }
        String translation = Server.getInstance().getLanguage().translateString("chat.type.achievement", player.getDisplayName(), TextFormat.GREEN + achievements.get(achievementId).getMessage());

        if (Server.getInstance().getPropertyBoolean("announce-player-achievements", true)) {
            Server.getInstance().broadcastMessage(translation);
        } else {
            player.sendMessage(translation);
        }
        return true;
    }

    public static boolean add(String name, AchievementEntry achievement) {
        if (achievements.containsKey(name)) {
            return false;
        }

        achievements.put(name, achievement);
        return true;
    }

    public static class AchievementEntry {

        public final String message;
        public final String[] requires;

        public AchievementEntry(String message, String... requires) {
            this.message = message;
            this.requires = requires;
        }

        public String getMessage() {
            return message;
        }

        public void broadcast(Player player) {
            String translation = Server.getInstance().getLanguage().translateString("chat.type.achievement", player.getDisplayName(), TextFormat.GREEN + this.getMessage());

            if (Server.getInstance().getPropertyBoolean("announce-player-achievements", true)) {
                Server.getInstance().broadcastMessage(translation);
            } else {
                player.sendMessage(translation);
            }
        }
    }
}