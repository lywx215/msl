package application;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class StatusPanel extends VBox {

    private static Image imgConnected;
    private static Image imgAlert;

    private static Label lblUserStatus;
    private static ImageView imgUserStatus;
    private static Label lblLeitorPasta;
    private static ImageView imgLeitorPasta;
    private static Text infoTxt;
    private int iconSize = 8;

    public StatusPanel() {
	setSpacing(10);
	setAlignment(Pos.CENTER);
	setPadding(new Insets(15));
	
	getChildren().add(new Separator());

	imgConnected = new Image(getClass().getResourceAsStream("images/green.png"), iconSize, iconSize, true, true);
	imgAlert = new Image(getClass().getResourceAsStream("images/red.png"), iconSize, iconSize, true, true);

	createUserStatus();
	createLeitorArquivoStatus();
	createInfoText();
    }
    
    
    private void createInfoText() {
	infoTxt = new Text();
	
	getChildren().add(infoTxt);
    }


    public static void setInfoText(String text) {
	infoTxt.setText(text);

	Timer t = new Timer();
	t.schedule(new TimerTask() {
	    public void run() {
		infoTxt.setText("");
	    }
	}, 5000);

    }

    public static void setUserStatusEnabled(boolean enabled) {
	imgUserStatus.setImage(enabled ? imgConnected : imgAlert);
	lblUserStatus.setText(enabled ? "Usuário conectado." : "Usuário não conectado, verifique as configurações.");
    }

    public static void setLeitorArquivoEnabled(boolean enabled) {
	imgLeitorPasta.setImage(enabled ? imgConnected : imgAlert);

	String txt = enabled ? "Leitor de arquivo conectado. " : "Leitor de arquivo não conectado.";

	Platform.runLater(new Runnable() {
	    public void run() {
		lblLeitorPasta.setText(txt);
	    }
	});
    }

    private void createUserStatus() {
	imgUserStatus = new ImageView(imgAlert);
	lblUserStatus = new Label("Usuário não conectado, verifique as configurações.");

	HBox hAlert = new HBox(5);
	hAlert.setAlignment(Pos.CENTER_LEFT);
	hAlert.getChildren().addAll(imgUserStatus, lblUserStatus);

	getChildren().add(hAlert);
    }

    private void createLeitorArquivoStatus() {
	imgLeitorPasta = new ImageView(imgAlert);
	lblLeitorPasta = new Label("Leitor de arquivo não conectado.");

	HBox hAlert = new HBox(5);
	hAlert.setAlignment(Pos.CENTER_LEFT);
	hAlert.getChildren().addAll(imgLeitorPasta, lblLeitorPasta);

	getChildren().add(hAlert);
    }

}
