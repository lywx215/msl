package application;

import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import services.LeitorArquivoService;

public class LeitorArquivo extends GridPane {

    private ToggleButton btnConectar;

    public LeitorArquivo() {
	setAlignment(Pos.CENTER);
	setHgap(10);
	setVgap(10);
	setPadding(new Insets(25, 25, 25, 25));

	Text scenetitle = new Text("Leitor de arquivo na pasta");
	scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	add(scenetitle, 0, 0, 2, 1);

	Label lblPasta = new Label("Pasta:");
	Label lblArquivo = new Label("Arquivo:");

	File pasta = new File("");

	String cp = "";

	try {
	    cp = pasta.getCanonicalPath();
	} catch (Exception e) {
	}

	add(lblPasta, 0, 1);
	add(lblArquivo, 0, 2);

	add(new Label(cp), 1, 1);
	add(new Label(LeitorArquivoService.getArquivoPeso()), 1, 2);

	btnConectar = new ToggleButton("Conectar");
	HBox hbBtn = new HBox(10);
	hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
	hbBtn.getChildren().add(btnConectar);
	add(hbBtn, 1, 4);

	btnConectar.setOnAction(e -> conectar());
    }

    private void conectar() {
	if (LeitorArquivoService.isConnected()) {
	    LeitorArquivoService.disconnect();
	    btnConectar.setText("Conectar");
	} else {
	    Thread t1 = new Thread(new LeitorArquivoService());
	    t1.setDaemon(true);

	    t1.start();
	    btnConectar.setText("Desconectar");
	}
    }
}
