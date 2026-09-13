package org.mark.server9;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public final class Server9 extends JavaPlugin implements Listener {

    // --- НАСТРОЙКИ ---
    private static final double SPRINT_HORIZONTAL_KB = 0.34; // Дальность
    private static final double SPRINT_VERTICAL_KB = 0.38;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Запуск успешен");
    }

    @Override
    public void onDisable() {
        getLogger().info("Выкл");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player attacker && event.getEntity() instanceof LivingEntity target) {

            if (!attacker.isSprinting()) {
                return;
            }

            attacker.setSprinting(false);


            Vector direction = attacker.getLocation().getDirection().normalize();
            Vector customKnockback = new Vector(direction.getX(), 0, direction.getZ()).normalize();
            customKnockback.multiply(SPRINT_HORIZONTAL_KB);


            customKnockback.setY(SPRINT_VERTICAL_KB);

            getServer().getScheduler().runTask(this, () -> {

                Vector targetVelocity = target.getVelocity();
                if (targetVelocity.getY() > 0) {
                    customKnockback.setY(Math.min(SPRINT_VERTICAL_KB, targetVelocity.getY() + 0.1));
                }

                target.setVelocity(customKnockback);
            });
        }
    }
}