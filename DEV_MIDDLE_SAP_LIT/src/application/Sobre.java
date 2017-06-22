package application;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Sobre extends VBox {
    
    public Sobre() {
	setSpacing(10);
	setAlignment(Pos.CENTER);
	
	ImageView logoLit = new ImageView(new Image(getClass().getResourceAsStream("images/lit.png")));
	Text txtSobre = new Text("Middleware para conecção entre balanças e servidor SAP.");
	Text txtCopyright = new Text("LIT Solutions © 2017");
	
	getChildren().addAll(logoLit, txtSobre, txtCopyright);
    }

}
