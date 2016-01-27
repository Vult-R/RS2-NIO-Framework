package main.astraeus.core.net.packet.outgoing.impl;

import main.astraeus.core.game.model.entity.mobile.player.Player;
import main.astraeus.core.net.packet.PacketWriter;
import main.astraeus.core.net.packet.outgoing.OutgoingPacket;
import main.astraeus.core.net.protocol.codec.ByteOrder;
import main.astraeus.core.net.protocol.codec.ByteModification;

/**
 * The {@link OutgoingPacket} that updates a region for a player.
 * 
 * @author SeVen
 */
public class SendRegionUpdate extends OutgoingPacket {

	/**
	 * Creates a new {@link SendRegionUpdate}.
	 */
	public SendRegionUpdate() {
		super(73, 5);
	}

	@Override
	public PacketWriter encode(Player player) {
		player.getContext().prepare(this, writer);
		writer.writeShort(player.getPosition().getRegionalX() + 6,
				ByteModification.ADDITION, ByteOrder.BIG);
		writer.writeShort(player.getPosition().getRegionalY() + 6);
		player.getLastPosition().setPosition(player.getPosition());
		return writer;
	}

}
