package at.flockenberger.bdoft;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

public class FarmTimerMain extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(FarmTimerMain.class.getResource("/bdoft.fxml"));
		Parent root = fxmlLoader.load();
		Scene scene = new Scene(root);

		Controller controller = (Controller) fxmlLoader.getController();
		controller.init(stage);

		root.setOnDragOver(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				if (event.getGestureSource() != root && event.getDragboard().hasFiles()
						&& getFileExtension(event.getDragboard().getFiles().get(0)).equals(".ftf")) {
					/* allow for both copying and moving, whatever user chooses */
					event.acceptTransferModes(TransferMode.LINK);
				}
				event.consume();
			}
		});

		root.setOnDragDropped(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasFiles()) {

					File f = db.getFiles().get(0);

					controller.loadFile(f);

					success = true;
				}
				/*
				 * let the source know whether the string was successfully transferred and used
				 */
				event.setDropCompleted(success);

				event.consume();
			}
		});

		scene.getStylesheets().add(getClass().getResource("/systemUIDark.css").toExternalForm());
		stage.setResizable(false);
		stage.setScene(scene);
		stage.setAlwaysOnTop(true);
		stage.getIcons().add(new Image(getClass().getResource("/icon.png").toExternalForm()));
		stage.setTitle("BDO Farm Timer");
		stage.show();

	}

	private static String getFileExtension(File file) {
		String name = file.getName();
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return ""; // empty extension
		}
		return name.substring(lastIndexOf);
	}

	public static void main(String[] args) {
		launch(args);

	}
}