

package dev.madcat.rebirth.manager;

import io.netty.util.internal.*;
import dev.madcat.rebirth.util.*;
import dev.madcat.rebirth.*;
import java.util.*;

public class Enemies extends RotationManager
{
    private static ConcurrentSet<Enemy> enemies;
    
    public static void addEnemy(final String name) {
        Enemies.enemies.add((Object)new Enemy(name));
    }
    
    public static void delEnemy(final String name) {
        Enemies.enemies.remove((Object)getEnemyByName(name));
    }
    
    public static Enemy getEnemyByName(final String name) {
        for (final Enemy e : Enemies.enemies) {
            if (Rebirth.enemy.username.equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
    
    public static ConcurrentSet<Enemy> getEnemies() {
        return Enemies.enemies;
    }
    
    public static boolean isEnemy(final String name) {
        return Enemies.enemies.stream().anyMatch(enemy -> enemy.username.equalsIgnoreCase(name));
    }
    
    static {
        Enemies.enemies = (ConcurrentSet<Enemy>)new ConcurrentSet();
    }
}
