package at.flockenberger.bdoft;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class Controller {

	@FXML
	private AnchorPane id_pane_settings;

	@FXML
	private Label id_l_server;

	@FXML
	private Label id_l_ssettings;

	@FXML
	private Label id_l_med1;

	@FXML
	private Spinner<Integer> id_s_med1_off;

	@FXML
	private Spinner<Integer> id_s_med1_off_s;

	@FXML
	private Label id_l_ssettings1;

	@FXML
	private Label id_l_val1;

	@FXML
	private Spinner<Integer> id_s_val1_off;

	@FXML
	private Spinner<Integer> id_s_val1_off_s;

	@FXML
	private Label id_l_cal1;

	@FXML
	private Spinner<Integer> id_s_cal1_off;

	@FXML
	private Spinner<Integer> id_s_cal1_off_s;

	@FXML
	private Label id_l_ser1;

	@FXML
	private Spinner<Integer> id_s_ser1_off;

	@FXML
	private Spinner<Integer> id_s_ser1_off_s;

	@FXML
	private Label id_l_bal1;

	@FXML
	private Spinner<Integer> id_s_bal1_off;

	@FXML
	private Spinner<Integer> id_s_bal1_off_s;

	@FXML
	private Button id_bt_apply;

	@FXML
	private Label id_l_server1;

	@FXML
	private CheckBox id_cb_med1;

	@FXML
	private CheckBox id_cb_val1;

	@FXML
	private CheckBox id_cb_cal1;

	@FXML
	private CheckBox id_cb_ser1;

	@FXML
	private CheckBox id_cb_bal1;

	@FXML
	private AnchorPane id_pane_timer;

	@FXML
	private Label idl_timer;

	@FXML
	private AnchorPane id_pane_nextTick;

	@FXML
	private Label id_l_nextTick;

	@FXML
	private Label id_l_switch;

	@FXML
	private Label id_l_nextServer;

	@FXML
	private Label id_l_nt;

	private SimpleDateFormat format = new SimpleDateFormat("mm:ss");

	private TickTimes ticks = new TickTimes();

	private Tick currentTick;

	private Stage mainStage;

	@FXML
	void onAbout(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setHeaderText(null);
		alert.setContentText(
				"BDO Farm Timer by Flockenberger\n\nFlatBee theme by karlthebee (https://github.com/karlthebee/flatbee)");
		alert.initOwner(mainStage);
		alert.showAndWait();
	}

	@FXML
	void onClose(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void onOpen(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter for text files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Farm Timer Filers (*.ftf)", "*.ftf");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showOpenDialog(mainStage);

		if (file != null) {
			ticks = (TickTimes) DataStore.loadObject(file.toPath());

			id_cb_med1.setSelected(false);
			id_cb_val1.setSelected(false);
			id_cb_cal1.setSelected(false);
			id_cb_ser1.setSelected(false);
			id_cb_bal1.setSelected(false);

			for (Tick off : ticks.getOffsets()) {
				if (off.getMinuteOffset() <= 4) {
					System.out.println(off.getSecondsOffset());
					switch (off.getServer()) {
					case MED:
						id_cb_med1.setSelected(true);
						id_s_med1_off.getValueFactory().setValue(off.getMinuteOffset());
						id_s_med1_off_s.getValueFactory().setValue(off.getSecondsOffset());
						break;
					case VAL:
						id_cb_val1.setSelected(true);
						id_s_val1_off.getValueFactory().setValue(off.getMinuteOffset());
						id_s_val1_off_s.getValueFactory().setValue(off.getSecondsOffset());
						break;
					case CAL:
						id_cb_cal1.setSelected(true);
						id_s_cal1_off.getValueFactory().setValue(off.getMinuteOffset());
						id_s_cal1_off_s.getValueFactory().setValue(off.getSecondsOffset());
						break;
					case SER:
						id_cb_ser1.setSelected(true);
						id_s_ser1_off.getValueFactory().setValue(off.getMinuteOffset());
						id_s_ser1_off_s.getValueFactory().setValue(off.getSecondsOffset());
						break;
					case BAL:
						id_cb_bal1.setSelected(true);
						id_s_bal1_off.getValueFactory().setValue(off.getMinuteOffset());
						id_s_bal1_off_s.getValueFactory().setValue(off.getSecondsOffset());
						break;

					default:
						break;
					}
				}
			}
		}
		onApply(null);
	}

	@FXML
	void onSave(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Farm Timer Filers (*.ftf)", "*.ftf");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showSaveDialog(mainStage);

		if (file != null) {
			DataStore.storeObject(file.toPath(), ticks);
		}

	}

	@FXML
	void onApply(ActionEvent event) {

		ticks.clear();

		if (id_cb_med1.isSelected()) {
			ticks.addTickOffset(new Tick(Server.MED, id_s_med1_off.getValue(), id_s_med1_off_s.getValue()));
			int off = id_s_med1_off.getValue() + 5;
			ticks.addTickOffset(new Tick(Server.MED, off, id_s_med1_off_s.getValue()));
		}
		if (id_cb_val1.isSelected()) {
			ticks.addTickOffset(new Tick(Server.VAL, id_s_val1_off.getValue(), id_s_val1_off_s.getValue()));
			int off = id_s_val1_off.getValue() + 5;
			ticks.addTickOffset(new Tick(Server.VAL, off, id_s_val1_off_s.getValue()));
		}
		if (id_cb_cal1.isSelected()) {
			ticks.addTickOffset(new Tick(Server.CAL, id_s_cal1_off.getValue(), id_s_cal1_off_s.getValue()));
			int off = id_s_cal1_off.getValue() + 5;
			ticks.addTickOffset(new Tick(Server.CAL, off, id_s_cal1_off_s.getValue()));
		}
		if (id_cb_ser1.isSelected()) {
			ticks.addTickOffset(new Tick(Server.SER, id_s_ser1_off.getValue(), id_s_ser1_off_s.getValue()));
			int off = id_s_ser1_off.getValue() + 5;
			ticks.addTickOffset(new Tick(Server.SER, off, id_s_ser1_off_s.getValue()));
		}
		if (id_cb_bal1.isSelected()) {
			ticks.addTickOffset(new Tick(Server.BAL, id_s_bal1_off.getValue(), id_s_bal1_off_s.getValue()));
			int off = id_s_bal1_off.getValue() + 5;
			ticks.addTickOffset(new Tick(Server.BAL, off, id_s_bal1_off_s.getValue()));
		}

		ticks.update();
		currentTick = ticks.getNext();
	}

	private void commitEditorText(Spinner<Integer> spinner) {
		if (!spinner.isEditable())
			return;
		String text = spinner.getEditor().getText();
		TimeOffsetValueFactory valueFactory = (TimeOffsetValueFactory) spinner.getValueFactory();
		if (valueFactory != null) {
			StringConverter<Integer> converter = valueFactory.getConverter();
			if (converter != null) {
				Integer value = converter.fromString(text);
				valueFactory.setValue(value);
			}
		}
	}

	private void addListener(Spinner<Integer> spinner) {
		spinner.focusedProperty().addListener((s, ov, nv) -> {
			if (nv)
				return;
			commitEditorText(spinner);
		});
	}

	public void init(Stage stage) {
		mainStage = stage;

		id_s_med1_off.setValueFactory(new TimeOffsetValueFactory(0, 4));
		id_s_med1_off_s.setValueFactory(new TimeOffsetValueFactory(0, 59));
		id_s_val1_off.setValueFactory(new TimeOffsetValueFactory(0, 4));
		id_s_val1_off_s.setValueFactory(new TimeOffsetValueFactory(0, 59));
		id_s_cal1_off.setValueFactory(new TimeOffsetValueFactory(0, 4));
		id_s_cal1_off_s.setValueFactory(new TimeOffsetValueFactory(0, 59));
		id_s_ser1_off.setValueFactory(new TimeOffsetValueFactory(0, 4));
		id_s_ser1_off_s.setValueFactory(new TimeOffsetValueFactory(0, 59));
		id_s_bal1_off.setValueFactory(new TimeOffsetValueFactory(0, 4));
		id_s_bal1_off_s.setValueFactory(new TimeOffsetValueFactory(0, 59));

		addListener(id_s_med1_off);
		addListener(id_s_med1_off_s);
		addListener(id_s_val1_off);
		addListener(id_s_val1_off_s);
		addListener(id_s_cal1_off);
		addListener(id_s_cal1_off_s);
		addListener(id_s_ser1_off);
		addListener(id_s_ser1_off_s);
		addListener(id_s_bal1_off);
		addListener(id_s_bal1_off_s);

		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {

			// get current time
			LocalTime time = LocalTime.now();
			idl_timer.setText(time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());

			if (currentTick != null) { // if we have a tick

				// parse the the minute digits
				String number = String.valueOf(time.getMinute());
				String[] digits = number.split("(?<=.)");

				int offset = Integer.valueOf(digits[0]);

				if (digits.length >= 2)
					offset = Integer.valueOf(digits[1]);

				int currentTime = offset; // 0, 1, 2, 3....

				Date now = null;
				Date tickTime = null;

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
				while (now.compareTo(tickTime) > 0) {
					// here we check if we still have a tick in the list that comes after
					// currentTime but is still within the 10 minutes
					if (ticks.tickExists(currentTime)) {
						currentTick = ticks.getNext(); // then we get the next tick and parse it as date again
						try {
							tickTime = format
									.parse(currentTick.getMinuteOffset() + ":" + currentTick.getSecondsOffset());
						} catch (ParseException e2) {
							e2.printStackTrace();
						}
					} else {
						// otherwise we do not have a new tick within this 10 minute timeframe. This
						// means we need to start with the first tick again
						currentTick = ticks.resetAndGetFirst();

						// and we parse it again
						try {
							tickTime = format
									.parse(currentTick.getMinuteOffset() + ":" + currentTick.getSecondsOffset());
						} catch (ParseException e2) {
							e2.printStackTrace();
						}
						break;
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

				Date diff = new Date(difference);
				
				//here we display the "SWITCH" text
				if (diff.getSeconds() <= 25 && diff.getMinutes() == 0) {
					id_l_switch.setText("SWITCH");
					id_l_switch.setTextFill(javafx.scene.paint.Color.RED);
				} else
					id_l_switch.setText("");

				
				id_l_nextTick.setText(diff.getMinutes() + ":" + diff.getSeconds());
				id_l_nextServer.setText(currentTick.getServer().getName());

			}

		}), new KeyFrame(Duration.seconds(1)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();

	}

}
