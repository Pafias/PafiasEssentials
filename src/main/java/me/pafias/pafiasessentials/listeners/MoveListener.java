package me.pafias.pafiasessentials.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.pafias.pafiasessentials.PafiasEssentials;
import me.pafias.pafiasessentials.User;
import me.pafias.pafiasessentials.nms.NMSProvider;
import me.pafias.pafiasessentials.util.CC;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MoveListener implements Listener {

    private final PafiasEssentials ae;

    Map<UUID, Double> speed = new HashMap<>();

    public MoveListener(final PafiasEssentials ae) {
        this.ae = ae;
        try {
            ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(ae, PacketType.Play.Client.STEER_VEHICLE) {
                NMSProvider nms = ae.getSM().getNMSProvider();

                public void onPacketReceiving(PacketEvent event) {
                    try {
                        User user = ae.getSM().getUserManager().getUser(event.getPlayer());
                        if (user == null || !user.movingEntity)
                            return;
                        Entity entity = user.getPlayer().getVehicle();
                        if (entity == null)
                            return;
                        Object packet = event.getPacket().getHandle();
                        event.setCancelled(true);
                        if (!speed.containsKey(user.getUUID()))
                            speed.put(user.getUUID(), 0.1D);
                        double i = speed.get(user.getUUID());
                        Vector v = new Vector();
                        if (nms.steeringForward(packet)) {
                            v = user.getPlayer().getLocation().getDirection();
                            v.multiply(i);
                        }
                        if (nms.steeringBackwards(packet)) {
                            v = user.getPlayer().getLocation().getDirection();
                            v.multiply(-i);
                        }
                        if (nms.steeringRight(packet)) {
                            double ii = i + 0.1D;
                            speed.replace(user.getUUID(), ii);
                            DecimalFormat df = new DecimalFormat("#.##");
                            user.getPlayer().sendTitle("", CC.translate("&6Speed: &7" + df.format(ii)), 2, 10, 5);
                        }
                        if (nms.steeringLeft(packet)) {
                            double ii = i - 0.1D;
                            speed.replace(user.getUUID(), ii);
                            DecimalFormat df = new DecimalFormat("#.##");
                            user.getPlayer().sendTitle("", CC.translate("&6Speed: &7" + df.format(ii)), 2, 10, 5);
                        }
                        entity.setVelocity(v);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception ignored) {
        }
    }

}
