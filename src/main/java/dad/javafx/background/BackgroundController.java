package dad.javafx.background;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class BackgroundController implements Initializable {
	
	// model
	
	private StringProperty estado = new SimpleStringProperty();
	private IntegerProperty progreso = new SimpleIntegerProperty();
	private BooleanProperty ejecutando = new SimpleBooleanProperty();
	
	private Task<Void> tarea;
	
	// view

    @FXML
    private VBox view;

    @FXML
    private Label estadoLabel;

    @FXML
    private ProgressBar progresoBar;

    @FXML
    private Button iniciarButton, detenerButton;

	public BackgroundController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BackgroundView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		progresoBar.progressProperty().bind(progreso.divide(100.0));
		estadoLabel.textProperty().bind(estado);
		iniciarButton.disableProperty().bind(ejecutando);
		detenerButton.disableProperty().bind(ejecutando.not());
		
	}
	
	public VBox getView() {
		return view;
	}

    @FXML
    private void onDetenerAction(ActionEvent event) {
    	tarea.cancel(true);
    }
	
    @FXML
    private void onIniciarAction(ActionEvent event) {
    	tarea = new Task<Void>() {
    		@Override
    		protected Void call() throws Exception {
    	    	int TOTAL = 1000;
    	    	for (int i = 0; i < TOTAL; i++) {
    	    		int valor = i * 100 / TOTAL;
    	    		updateMessage("Haciendo no se qué ... (" + valor + "%)");
    	    		updateProgress(i, TOTAL);
    	    		if (valor == 75) throw new Exception("Se desacopló la junta de la trócola");
   					Thread.sleep(10L);
    	    	}
	    		updateMessage("Proceso terminado");
	    		updateProgress(TOTAL, TOTAL);
    			return null;
    		}
		};
		
		tarea.setOnSucceeded(e -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Éxito");
			alert.setHeaderText("Todo fue bien");
			alert.setContentText(e.getSource().getMessage());
			alert.showAndWait();
		});

		tarea.setOnFailed(e -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Fallo");
			alert.setHeaderText("Algo no fue bien");
			alert.setContentText(e.getSource().getException().getMessage());
			alert.showAndWait();
		});
		
		tarea.setOnCancelled(e -> {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Cancelado");
			alert.setHeaderText("Se canceló la tarea");
			alert.setContentText(e.getSource().getMessage());
			alert.showAndWait();
			
			progreso.unbind();
			estado.unbind();
			
			progreso.set(0);
			estado.set("Tarea cancelada");
		});
		
		progreso.bind(tarea.progressProperty().multiply(100));
		estado.bind(tarea.messageProperty());
		ejecutando.bind(tarea.runningProperty());
		
		new Thread(tarea).start();
    }
    
}
