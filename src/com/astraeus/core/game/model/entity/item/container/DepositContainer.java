package com.astraeus.core.game.model.entity.item.container;

import com.astraeus.core.Configuration;
import com.astraeus.core.game.model.entity.item.Item;
import com.astraeus.core.game.model.entity.item.ItemContainer;
import com.astraeus.core.game.model.entity.mobile.player.Player;

public class DepositContainer extends ItemContainer {

	private final Player player;
	
	public DepositContainer(Player player) {
		super(28, StackType.ALWAYS_STACK);
		this.player = player;
	}

	@Override
	public void addItem(Item item) {
		
	}

	@Override
	public void removeItem(int id, int amount) {
		
	}

	@Override
	public void updateContainer() {
		
	}
	
	public static void openDepositBox(Player player) {
		player.sendString("The Bank of " + Configuration.SERVER_NAME + " - Deposit Box", 7165);
		player.sendInventoryInterface(4465, 197);
	}
	
	public Player getPlayer() {
		return player;
	}

}
