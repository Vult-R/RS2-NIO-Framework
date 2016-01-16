package main.astraeus.core.game.model.entity.mobile.npc.update.impl;

import main.astraeus.core.game.model.entity.mobile.npc.Npc;
import main.astraeus.core.game.model.entity.mobile.npc.update.NpcUpdateBlock;
import main.astraeus.core.game.model.entity.mobile.update.UpdateFlags.UpdateFlag;
import main.astraeus.core.net.packet.PacketBuilder;

public class NpcSingleHitUpdateBlock extends NpcUpdateBlock {

	public NpcSingleHitUpdateBlock() {
		super(0x20, UpdateFlag.SINGLE_HIT);
	}

	@Override
	public void encode(Npc entity, PacketBuilder builder) {
		
	}

}