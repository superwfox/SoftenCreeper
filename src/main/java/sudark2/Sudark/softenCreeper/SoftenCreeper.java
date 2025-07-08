package sudark2.Sudark.softenCreeper;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public final class SoftenCreeper extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new CreeperExplode(), this);
    }

    public class CreeperExplode implements Listener {
        @EventHandler
        public void onExplode(EntityExplodeEvent e) {
            Entity en = e.getEntity();
            if (en instanceof Creeper || en instanceof EnderCrystal || en instanceof WitherSkull) {
                // 复制方块原始状态
                List<BlockState> blocks = e.blockList().stream()
                        .map(block -> block.getState())
                        .collect(Collectors.toList());

                // 移除所有方块，避免掉落物
                for (Block block : e.blockList()) {
                    block.setType(Material.AIR, false);
                }
                e.blockList().clear();

                // 延迟恢复方块并播放粒子
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (BlockState block : blocks) {
                            block.getLocation().getWorld().spawnParticle(
                                    Particle.SCULK_SOUL,
                                    block.getLocation(),
                                    2, 0.1f, 0.1f, 0.1f, 0.2f
                            );
                            block.update(true, false); // true=force, false=don't apply physics
                        }
                    }
                }.runTaskLater(SoftenCreeper.this, 4 * 20L); // 替换为你的插件实例
            }
        }
    }


}
