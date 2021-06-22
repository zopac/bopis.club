package me.bopis.king.manager;

import io.netty.util.internal.ConcurrentSet;
import me.bopis.king.Bopis;
import me.bopis.king.util.Enemy;

public class Enemies extends RotationManager {
    private static final ConcurrentSet<Enemy> enemies = new ConcurrentSet<>();

    public static void addEnemy(String name) {
        enemies.add(new Enemy(name));
    }

    public static void delEnemy(String name) {
        enemies.remove(getEnemyByName(name));
    }

    public static Enemy getEnemyByName(String name) {
        for (Enemy e : enemies) {
            if (Bopis.enemy.username.equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    public static ConcurrentSet<Enemy> getEnemies() {
        return enemies;
    }

    public static boolean isEnemy(String name) {
        return enemies.stream().anyMatch(enemy -> enemy.username.equalsIgnoreCase(name));
    }

}