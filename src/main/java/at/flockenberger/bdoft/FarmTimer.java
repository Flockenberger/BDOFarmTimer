package at.flockenberger.bdoft;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class FarmTimer extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(FarmTimer.class.getResource("/bdoft.fxml"));
		Parent root = fxmlLoader.load();
		Scene scene = new Scene(root);

		scene.getStylesheets().add(getClass().getResource("/systemUIDark.css").toExternalForm());
		stage.setResizable(false);
		stage.setScene(scene);
		stage.setAlwaysOnTop(true);
		stage.getIcons().add(new Image(getClass().getResource("/icon.png").toExternalForm()));
		stage.setTitle("BDO Farm Timer");
		stage.show();

		Controller controller = (Controller) fxmlLoader.getController();
		controller.init(stage);
		
	}

	public static void main(String[] args) {
		launch(args);

	}
}