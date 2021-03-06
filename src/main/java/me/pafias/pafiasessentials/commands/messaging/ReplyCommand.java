package me.pafias.pafiasessentials.commands.messaging;

import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.User;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand implements CommandExecutor {

    private final PafiasEssentials plugin;

    public ReplyCommand(PafiasEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(CC.translate("&c/" + label + " <message>"));
            return true;
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.translate("&cOnly players!"));
                return true;
            }
            User player = plugin.getSM().getUserManager().getUser((Player) sender);
            if (!TellCommand.msg.containsKey(player.getUUID())) {
                player.getPlayer().sendMessage(CC.translate("&cYou haven't messaged anybody recently!"));
                return true;
            }
            User target = plugin.getSM().getUserManager().getUser(TellCommand.msg.get(player.getUUID()));
            if (target == null) {
                sender.sendMessage(CC.translate("&cThe person you were chatting with is no longer online!"));
                return true;
            }
            StringBuilder sb = new StringBuilder();
            for (String arg : args) sb.append(arg).append(" ");
            String message = sb.toString();
            target.getPlayer().sendMessage(CC.translate("&e[Tell] &c" + player.getName() + "&6: &r" + message));
            player.getPlayer().sendMessage(CC.translate("&e[Tell] &c" + player.getName() + " &6-> &c" + target.getName() + " &6: &r" + message));
        }
        return true;
    }

}
