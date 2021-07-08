package at.flockenberger.bdoft.timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class FarmTimer {

	public interface FarmUIUpdate {
		void onUpdate(Date diff, Tick currentTick, LocalTime time);
	}

	private TickTimes ticks;

	private SimpleDateFormat format = new SimpleDateFormat("mm:ss");

	private Tick currentTick;
	private Date diff;

	private LocalTime time;

	private Date now = null;
	private Date tickTime = null;

	private FarmUIUpdate uiUpdate;

	public FarmTimer() {
		ticks = new TickTimes();
	}

	public void setUiUpdateCallback(FarmUIUpdate update) {
		this.uiUpdate = update;
	}

	public TickTimes getTicks() {
		return ticks;
	}

	public void setTicks(TickTimes times) {
		this.ticks = times;
	}

	public void initCurrent() {
		currentTick = ticks.getNext();
	}

	public void start() {
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {

			// get current time
			time = LocalTime.now();

			if (currentTick != null) { // if we have a tick

				// parse the the minute digits
				String number = String.valueOf(time.getMinute());
				String[] digits = number.split("(?<=.)");

				int offset = Integer.valueOf(digits[0]);

				if (digits.length >= 2)
					offset = Integer.valueOf(digits[1]);

				int currentTime = offset; // 0, 1, 2, 3....

				// this is very hacky and there are waayyy better solutions to do this but for
				// now it works and thats all I care about...

				// parse the current time as date
				try {
					now = format.parse(currentTime + ":" + time.getSecond());
				} catch (ParseException e2) {
					e2.printStackTrace();
				}

				// parse the tick time as date
				try {
					tickTime = format.parse(currentTick.getMinuteOffset() + ":" + currentTick.getSecondsOffset());
				} catch (ParseException e2) {
					e2.printStackTrace();
				}

				// now compare the current tick to the current time. If the current time is
				// greater than the "last"/"current" tick then we need to switch to the next
				// one.
				if (now.compareTo(tickTime) > 0) {
					// here we check if we still have a tick in the list that comes after
					// currentTime but is still within the 10 minutes
					if (ticks.tickExists(currentTime, time.getSecond())) {
						currentTick = ticks.getNext(); // then we get the next tick and parse it as date again
					} else {
						// otherwise we do not have a new tick within this 10 minute timeframe. This
						// means we need to start with the first tick again
						currentTick = ticks.resetAndGetFirst();
					}
					// and we parse it again
					try {
						tickTime = format.parse(currentTick.getMinuteOffset() + ":" + currentTick.getSecondsOffset());
					} catch (ParseException e2) {
						e2.printStackTrace();
					}

				}

				// here we calculate the difference in time to display the remaining time left
				// before the tick
				long difference = tickTime.getTime() - now.getTime();

				// if the difference is less than 0 we know that the tick "overflows" into the
				// next 10 minute frame so we "add" 10 minutes to the tick time. That way we
				// still get the correct remaining time
				if (difference < 0) {
					try {
						Date ten = format.parse("10:0");
						difference = (tickTime.getTime() + ten.getTime()) - now.getTime();
					} catch (ParseException e1) {
						e1.printStackTrace();
					}

				}

				diff = new Date(difference);

				if (uiUpdate != null) {
					uiUpdate.onUpdate(diff, currentTick, time);
				}
				
			}

		}), new KeyFrame(Duration.seconds(0.1f)));
		clock.setCycleCount(Animation.INDEFINITE);

		clock.play();
	}

}
