package main.astraeus.game.model.entity.mobile.npc.drop;

/**
 * An enumeration of probable cases for an npc drop.
 * 
 * @author SeVen <https://github.com/7winds>
 */
public enum Chance {
	/**
	 * 100% 1/1
	 */
	ALWAYS(1),
	/**
	 * 50%
	 */
	ALMOST_ALWAYS(2),
	/**
	 * 20%
	 */
	VERY_COMMON(5),
	/**
	 * 5%
	 */
	COMMON(20),
	/**
	 * 2% 1/50
	 */
	UNCOMMON(50),
	/**
	 * 1%
	 */
	VERY_UNCOMMON(100),
	/**
	 * 0.5% 1/200
	 */
	RARE(200),
	/**
	 * 0.35% 1/286
	 */
	VERY_RARE(286),
	/**
	 * 0.2% 1/500
	 */
	EXTREMELY_RARE(500),
	/**
	 * 0.1% 1/900
	 */
	LEGENDARY(900);
	
	/**
	 * The rate at which an item will drop.
	 */
	private int rate;
	
	private Chance(int rate) {
		this.rate = rate;
	}
	
	/**
	 * Gets the rate at which an item drops.
	 * 
	 * @return rate 
	 */
	public int getRate() {
		return rate;
	}
	
}
