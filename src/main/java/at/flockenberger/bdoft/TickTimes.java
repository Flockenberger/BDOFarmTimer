package at.flockenberger.bdoft;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * <h1>TickTimes</h1><br>
 * TickTimes manages the ticks given. <br>
 * Ticks are sorted by their offset times in ascending order
 * 
 * @author Florian Wagner
 *
 */
public class TickTimes implements Serializable {

	// serial version
	private static final long serialVersionUID = -6674929345988061020L;

	/**
	 * The tick offsets to be managed
	 */
	private List<Tick> tickOffsets = new ArrayList<Tick>();

	private transient ListIterator<Tick> iter;

	public TickTimes() {
	}

	/**
	 * @return list of all tick offsets
	 */
	public List<Tick> getOffsets() {
		return tickOffsets;
	}

	/**
	 * Adds a new {@link Tick}
	 * 
	 * @param off the tick to add
	 */
	public void addTickOffset(Tick off) {
		tickOffsets.add(off);
		Collections.sort(tickOffsets, (a, b) -> a.compareTo(b));
	}

	/**
	 * Clears all {@link Tick}s
	 */
	public void clear() {
		tickOffsets.clear();
	}

	/**
	 * Called to sort the {@link Tick}s and to retrieve an {@link ListIterator}
	 */
	public void update() {
		Collections.sort(tickOffsets, (a, b) -> a.compareTo(b));
		iter = tickOffsets.listIterator();
	}

	/**
	 * Check if there is a {@link Tick} that has an offset time greater than
	 * currentTime. <br>
	 * The parameter currentTime must be between 0-9!
	 * 
	 * @param currentTime the current minute
	 * @return true if there is another {@link Tick} otherwise false
	 */
	public boolean tickExists(int currentTime) {

		for (Tick off : tickOffsets) {
			if (off.getMinuteOffset() > currentTime)
				return true;
		}
		return false;
	}

	/**
	 * @return the next {@link Tick} in the list. If there is no {@link Tick} left
	 *         the first one will be returned.
	 */
	public Tick getNext() {
		if (iter.hasNext())
			return iter.next();
		else {
			return resetAndGetFirst();
		}
	}

	/**
	 * Resets the internal iterator and returns the first {@link Tick}
	 * 
	 * @return the first {@link Tick}
	 */
	public Tick resetAndGetFirst() {
		iter = tickOffsets.listIterator();
		return iter.next();
	}

}
