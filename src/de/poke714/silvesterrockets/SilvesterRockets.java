package de.poke714.silvesterrockets;

import io.netty.util.internal.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class SilvesterRockets extends JavaPlugin implements Listener {
	
	private int scheduler;
	private boolean enabled = false;
	
	@Override
	public void onEnable(){
		Bukkit.getConsoleSender().sendMessage("[SilvesterRockets] Enabled.");
	}
	
	@Override
	public void onDisable(){
		Bukkit.getConsoleSender().sendMessage("[SilvesterRockets] Disabled.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length == 0) return false;
		if(args[0].equalsIgnoreCase("start")){
			if(enabled) Bukkit.getScheduler().cancelTask(scheduler);
			enabled = true;
			
			int count = 10;
			try { count = Integer.valueOf(args[1]); } catch(Exception ignored) {}
			int finalCount = count;
			int radius = 30;
			try { radius = Integer.valueOf(args[2]); } catch(Exception ignored) {}
			int finalRadius = radius;
			int interval = 20;
			try { interval = Integer.valueOf(args[3]); } catch(Exception ignored) {}
			
			scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
				for(Player p : Bukkit.getOnlinePlayers()){
					for(int i = 0; i <= finalCount; i++){
						Color c = Color.fromRGB(ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256));
						Color cf = Color.fromRGB(ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256), ThreadLocalRandom.current().nextInt(256));
						
						Type type = Type.values()[ThreadLocalRandom.current().nextInt(Type.values().length - 1)];
						
						Firework fw = p.getWorld().spawn(new Location(p.getWorld(), p.getLocation().getX() + ThreadLocalRandom.current().nextInt(finalRadius * 2) - finalRadius, p.getLocation().getY() + 5, p.getLocation().getZ() + ThreadLocalRandom.current().nextInt(finalRadius * 2) - finalRadius), Firework.class);
						FireworkMeta fwMeta = fw.getFireworkMeta();
						fwMeta.addEffect(FireworkEffect.builder()
								.flicker(ThreadLocalRandom.current().nextBoolean())
								.withColor(c)
								.withFade(cf)
								.with(type)
								.trail(ThreadLocalRandom.current().nextBoolean())
								.build());
						fwMeta.setPower(ThreadLocalRandom.current().nextInt(4));
						fw.setFireworkMeta(fwMeta);
					}
				}
			}, interval, interval);
			sender.sendMessage("§b[SilvesterRockets] §eStarted. Spawning " + count + " fireworks every " + interval + " ticks (" + interval / 20.0 + " seconds) in a radius of " + radius + " blocks.");
		} else if(args[0].equalsIgnoreCase("stop")){
			if(enabled){
				enabled = false;
				Bukkit.getScheduler().cancelTask(scheduler);
				sender.sendMessage("§b[SilvesterRockets] §eStopped.");
			} else {
				sender.sendMessage("§b[SilvesterRockets] §eNot running.");
			}
		}
		
		return true;
	}
}