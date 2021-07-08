package at.flockenberger.bdoft;

import java.io.File;
import java.time.LocalTime;
import java.util.Date;

import at.flockenberger.bdoft.timer.FarmTimer;
import at.flockenberger.bdoft.timer.FarmTimer.FarmUIUpdate;
import at.flockenberger.bdoft.timer.Server;
import at.flockenberger.bdoft.timer.Tick;
import at.flockenberger.bdoft.timer.TickTimes;
import at.flockenberger.bdoft.util.DataStore;
import at.flockenberger.bdoft.util.TimeOffsetValueFactory;
import at.flockenberger.bdoft.util.TimerSettings;
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

	private Stage mainStage;

	private FarmTimer farmTimer;

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
		loadFile(file);
	}

	void loadFile(File file) {

		TickTimes ticks;
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
			DataStore.storeObject(file.toPath(), farmTimer.getTicks());
		}

	}

	@FXML
	void onApply(ActionEvent event) {
		
		TickTimes ticks = farmTimer.getTicks();
		
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
		farmTimer.initCurrent();
		farmTimer.start();
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

		farmTimer = new FarmTimer();
		farmTimer.setUiUpdateCallback(new FarmUIUpdate() {

			@Override
			public void onUpdate(Date diff, Tick currentTick, LocalTime time) {
				// here we display the "SWITCH" text
				if (diff.getSeconds() <= TimerSettings.TIME_REMAIN_FOR_SWITCH && diff.getMinutes() == 0) {
					id_l_switch.setText("SWITCH");
					id_l_switch.setTextFill(javafx.scene.paint.Color.RED);
					id_l_nextTick.setTextFill(javafx.scene.paint.Color.RED);
				} else {
					id_l_switch.setText("");
					id_l_nextTick.setTextFill(javafx.scene.paint.Color.BLACK);

				}

				id_l_nextTick.setText(diff.getMinutes() + ":" + diff.getSeconds());
				id_l_nextServer.setText(currentTick.getServer().getName());
				idl_timer.setText(time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());

			}
		});
	}

}
