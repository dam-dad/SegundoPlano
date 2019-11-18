package dad.javafx.background;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BackgroundApp extends Application {

	private BackgroundController controller;
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		controller = new BackgroundController();
		
		Scene scene = new Scene(controller.getView());
		
		primaryStage.setTitle("Segundo plano");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
