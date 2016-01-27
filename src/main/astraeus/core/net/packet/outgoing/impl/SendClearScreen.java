package main.astraeus.core.net.packet.outgoing.impl;

import main.astraeus.core.game.model.entity.mobile.player.Player;
import main.astraeus.core.net.packet.PacketWriter;
import main.astraeus.core.net.packet.outgoing.OutgoingPacket;

/**
 * The {@link OutgoingPacket} that clears a players screen of all open interfaces.
 * 
 * @author SeVen
 */
public class SendClearScreen extends OutgoingPacket {

      /**
       * Creates a new {@link SendClearScreen}.
       */
      public SendClearScreen() {
            super(219, 1);
      }

      @Override
      public PacketWriter encode(Player player) {
            player.getContext().prepare(this, writer);
            return writer;
      }

}
