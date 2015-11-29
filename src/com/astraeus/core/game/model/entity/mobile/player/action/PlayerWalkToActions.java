package com.astraeus.core.game.model.entity.mobile.player.action;

import com.astraeus.core.game.content.dialogue.Dialogues;
import com.astraeus.core.game.content.dialogue.impl.BankerDialogue;
import com.astraeus.core.game.model.entity.Position;
import com.astraeus.core.game.model.entity.item.container.DepositContainer;
import com.astraeus.core.game.model.entity.mobile.player.Player;

public final class PlayerWalkToActions {

	public static final void objectFirstClick(final Player player, final Position position, final int index) {

		switch (index) {
		
		case 2213:
			Dialogues.sendDialogue(player, new BankerDialogue());
			break;
			
		case 9398:
			DepositContainer.openDepositBox(player);
			break;

		default:
			player.sendMessage("[First Click Object] - Unhandled click - ObjectId: " + index);
			break;

		}
	}
	
	public static final void objectSecondClick(final Player player, final Position position, final int index) {
		
		switch (index) {
		
		default:
			player.sendMessage("[Second Click Object] - Unhandled click: ObjectId: " + index);
			break;
			
		}
	}

}
