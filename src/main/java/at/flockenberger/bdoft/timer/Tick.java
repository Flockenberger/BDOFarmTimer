package at.flockenberger.bdoft.timer;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h1>Tick</h1><br>
 * This describes one tick where your farms get an update.
 * 
 * @author Florian Wagner
 *
 */
public class Tick implements Comparable<Tick>, Serializable {

	// Serial version
	private static final long serialVersionUID = -6327922135655647341L;

	/**
	 * The server this tick is assigned to
	 */
	private Server server;

	/**
	 * The offset in seconds for this tick
	 */
	private int offsetSeconds;

	/**
	 * THe offset in minutes for this tick
	 */
	private int offsetMinutes;

	private static transient SimpleDateFormat format = new SimpleDateFormat("mm:ss");

	/**
	 * Creates a new Tick.
	 * 
	 * @param s  the server of this tick
	 * @param t  the minute offset of this tick
	 * @param ss the seconds offset of this tick
	 */
	public Tick(Server s, int t, int ss) {
		this.server = s;
		this.offsetMinutes = t;
		this.offsetSeconds = ss;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Tick o) {
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(offsetMinutes + ":" + offsetSeconds);
			d2 = format.parse(o.offsetMinutes + ":" + o.offsetSeconds);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (d1 == null || d2 == null)
			return 0;
		else
			return d1.compareTo(d2);

	}
	
	/**
	 * @return the server of this tick
	 */
	public Server getServer() {
		return server;
	}
	
	/**
	 * @return the minute offset of this tick
	 */
	public int getMinuteOffset() {
		return offsetMinutes;
	}
	
	/** 
	 * @return the seconds offset of this tick
	 */
	public int getSecondsOffset() {
		return offsetSeconds;
	}

	@Override
	public String toString() {
		return "TickOffset [server=" + server + ", Offset=" + offsetMinutes + ", seconds=" + offsetSeconds + "]";
	}

}
