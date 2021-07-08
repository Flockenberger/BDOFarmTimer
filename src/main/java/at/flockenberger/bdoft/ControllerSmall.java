package at.flockenberger.bdoft;

import java.io.File;
import java.time.LocalTime;
import java.util.Date;

import at.flockenberger.bdoft.timer.FarmTimer;
import at.flockenberger.bdoft.timer.Tick;
import at.flockenberger.bdoft.timer.TickTimes;
import at.flockenberger.bdoft.timer.FarmTimer.FarmUIUpdate;
import at.flockenberger.bdoft.util.DataStore;
import at.flockenberger.bdoft.util.TimerSettings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ControllerSmall {

	@FXML
	private Label id_l_nextTick;

	@FXML
	private Label id_l_nt;

	@FXML
	private Label id_l_nextServer;

	@FXML
	private Label idl_timer;

	private FarmTimer timer;

	void loadFile(File file) {

		timer = new FarmTimer();
		System.out.println("haha");
		TickTimes ticks = null;

		if (file != null) {
			ticks = (TickTimes) DataStore.loadObject(file.toPath());
			ticks.update();
			timer.setTicks(ticks);
			timer.initCurrent();
			timer.start();

			timer.setUiUpdateCallback(new FarmUIUpdate() {

				@Override
				public void onUpdate(Date diff, Tick currentTick, LocalTime time) {
					// here we display the "SWITCH" text
					if (diff.getSeconds() <= TimerSettings.TIME_REMAIN_FOR_SWITCH && diff.getMinutes() == 0) {
						id_l_nextTick.setTextFill(javafx.scene.paint.Color.RED);
					} else {
						id_l_nextTick.setTextFill(javafx.scene.paint.Color.BLACK);

					}

					id_l_nextTick.setText(diff.getMinutes() + ":" + diff.getSeconds());
					id_l_nextServer.setText(currentTick.getServer().getName());
					idl_timer.setText(time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());

				}
			});
		}
	}

}
