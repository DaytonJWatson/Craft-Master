package com.watsonllc.craft.commands;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.commands.admin.AHelpCMD;
import com.watsonllc.craft.commands.admin.BanCMD;
import com.watsonllc.craft.commands.admin.CraftingTableCMD;
import com.watsonllc.craft.commands.admin.EnderChestCMD;
import com.watsonllc.craft.commands.admin.FeedCMD;
import com.watsonllc.craft.commands.admin.FlyCMD;
import com.watsonllc.craft.commands.admin.GodModeCMD;
import com.watsonllc.craft.commands.admin.GroupsCMD;
import com.watsonllc.craft.commands.admin.HealCMD;
import com.watsonllc.craft.commands.admin.InvSeeCMD;
import com.watsonllc.craft.commands.admin.KickCMD;
import com.watsonllc.craft.commands.admin.MuteCMD;
import com.watsonllc.craft.commands.admin.NicknameCMD;
import com.watsonllc.craft.commands.admin.ReloadCMD;
import com.watsonllc.craft.commands.admin.SetSpawnCMD;
import com.watsonllc.craft.commands.admin.SpectateCMD;
import com.watsonllc.craft.commands.admin.UnbanCMD;
import com.watsonllc.craft.commands.admin.UnmuteCMD;
import com.watsonllc.craft.commands.admin.VanishCMD;
import com.watsonllc.craft.commands.admin.WarnCMD;
import com.watsonllc.craft.commands.player.BedCMD;
import com.watsonllc.craft.commands.player.DelHomeCMD;
import com.watsonllc.craft.commands.player.DiscordCMD;
import com.watsonllc.craft.commands.player.HelpCMD;
import com.watsonllc.craft.commands.player.HomeCMD;
import com.watsonllc.craft.commands.player.PvpCMD;
import com.watsonllc.craft.commands.player.RandomTeleportCMD;
import com.watsonllc.craft.commands.player.ReportCMD;
import com.watsonllc.craft.commands.player.RulesCMD;
import com.watsonllc.craft.commands.player.SetHomeCMD;
import com.watsonllc.craft.commands.player.SettingsCMD;
import com.watsonllc.craft.commands.player.SpawnCMD;
import com.watsonllc.craft.commands.player.SpecsCMD;
import com.watsonllc.craft.commands.player.SuicideCMD;
import com.watsonllc.craft.commands.player.TpaCMD;

public class Commands {
	public static void setup() {
		Main.instance.getCommand("ahelp").setExecutor(new AHelpCMD());
		Main.instance.getCommand("ban").setExecutor(new BanCMD());
		Main.instance.getCommand("invsee").setExecutor(new InvSeeCMD());
		Main.instance.getCommand("craftingtable").setExecutor(new CraftingTableCMD());
		Main.instance.getCommand("craftreload").setExecutor(new ReloadCMD());
		Main.instance.getCommand("enderchest").setExecutor(new EnderChestCMD());
		Main.instance.getCommand("feed").setExecutor(new FeedCMD());
		Main.instance.getCommand("fly").setExecutor(new FlyCMD());
		Main.instance.getCommand("god").setExecutor(new GodModeCMD());
		Main.instance.getCommand("groups").setExecutor(new GroupsCMD());
		Main.instance.getCommand("groups").setTabCompleter(new GroupsCMD());
		Main.instance.getCommand("heal").setExecutor(new HealCMD());
		Main.instance.getCommand("kick").setExecutor(new KickCMD());
		Main.instance.getCommand("nickname").setExecutor(new NicknameCMD());
		Main.instance.getCommand("mute").setExecutor(new MuteCMD());
		Main.instance.getCommand("unmute").setExecutor(new UnmuteCMD());
		Main.instance.getCommand("setspawn").setExecutor(new SetSpawnCMD());
		Main.instance.getCommand("setspawn").setTabCompleter(new SetSpawnCMD());
		Main.instance.getCommand("spectate").setExecutor(new SpectateCMD());
		Main.instance.getCommand("unban").setExecutor(new UnbanCMD());
		Main.instance.getCommand("warn").setExecutor(new WarnCMD());
		Main.instance.getCommand("vanish").setExecutor(new VanishCMD());
		Main.instance.getCommand("bed").setExecutor(new BedCMD());
		Main.instance.getCommand("bed").setTabCompleter(new BedCMD());
		Main.instance.getCommand("discord").setExecutor(new DiscordCMD());
		Main.instance.getCommand("discord").setTabCompleter(new DiscordCMD());
		Main.instance.getCommand("help").setExecutor(new HelpCMD());
		Main.instance.getCommand("help").setTabCompleter(new HelpCMD());
		Main.instance.getCommand("home").setExecutor(new HomeCMD());
		Main.instance.getCommand("home").setTabCompleter(new HomeCMD());
		Main.instance.getCommand("delhome").setExecutor(new DelHomeCMD());
		Main.instance.getCommand("delhome").setTabCompleter(new DelHomeCMD());
		Main.instance.getCommand("sethome").setExecutor(new SetHomeCMD());
		Main.instance.getCommand("sethome").setTabCompleter(new SetHomeCMD());
		Main.instance.getCommand("pvp").setExecutor(new PvpCMD());
		Main.instance.getCommand("pvp").setTabCompleter(new PvpCMD());
		Main.instance.getCommand("rtp").setExecutor(new RandomTeleportCMD());
		Main.instance.getCommand("rtp").setTabCompleter(new RandomTeleportCMD());
		Main.instance.getCommand("report").setExecutor(new ReportCMD());
		Main.instance.getCommand("report").setTabCompleter(new ReportCMD());
		Main.instance.getCommand("rules").setExecutor(new RulesCMD());
		Main.instance.getCommand("rules").setTabCompleter(new RulesCMD());
		Main.instance.getCommand("spawn").setExecutor(new SpawnCMD());
		Main.instance.getCommand("spawn").setTabCompleter(new SpawnCMD());
		Main.instance.getCommand("specs").setExecutor(new SpecsCMD());
		Main.instance.getCommand("specs").setTabCompleter(new SpecsCMD());
		Main.instance.getCommand("suicide").setExecutor(new SuicideCMD());
		Main.instance.getCommand("suicide").setTabCompleter(new SuicideCMD());
		Main.instance.getCommand("tpa").setExecutor(new TpaCMD());
		Main.instance.getCommand("tpaccept").setExecutor((sender, command, label, args) -> new TpaCMD().onTpacceptCommand(sender));
		Main.instance.getCommand("tpdeny").setExecutor((sender, command, label, args) -> new TpaCMD().onTpdenyCommand(sender));
		
		// fix these
		Main.instance.getCommand("settings").setExecutor(new SettingsCMD());
	}
}