package at.flockenberger.bdoft;

/**
 * <h1>Server</h1><br>
 * BDO Servers where there are node wars.
 * @author Florian Wagner
 *
 */
public enum Server {
	
	/**
	 * Mediah 1 Server
	 */
	MED("Mediah 1"), 
	
	/**
	 * Valencia 1 Server
	 */
	VAL("Valencia 1"), 
	
	/**
	 * Calpheon 1 Server
	 */
	CAL("Calpheon 1"), 
	
	/**
	 * Serendia 1 Server
	 */
	SER("Serendia 1"), 
	
	/**
	 * Balenos 1 Server
	 */
	BAL("Balenos 1");
	
	/**
	 * The display name of the server
	 */
	private String name;
	
	Server(String n) {
		this.name = n;
	}
	
	/**
	 * @return the display name of this server
	 */
	public String getName() {
		return this.name;
	}
}