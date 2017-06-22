package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Transacoes extends GridPane {
    
    public static final String TELA_LOGIN = "MS10";
    public static final String TELA_LEITOR_ARQUIVO = "MS20";
    public static final String TELA_LEITOR_PORTA = "MS22";
    public static final String TELA_TRANSACOES = "MS30";
    public static final String TELA_SOBRE = "MS32";
    
    public Transacoes() {
	setAlignment(Pos.CENTER);
	setHgap(10);
	setVgap(10);
	setPadding(new Insets(25, 25, 25, 25));

	Text scenetitle = new Text("Código das transações");
	scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	add(scenetitle, 0, 0, 2, 1);

	Label lblLogin = new Label("Login:");
	Label lblLeitorArquivo = new Label("Leitor Arquivo:");
	Label lblLeitorPorta = new Label("Leitor Porta:");
	Label lblTransacoes = new Label("Transações:");
	Label lblSobre = new Label("Sobre:");

	add(lblLogin, 0, 1);
	add(lblLeitorArquivo, 0, 2);
	add(lblLeitorPorta, 0, 3);
	add(lblTransacoes, 0, 4);
	add(lblSobre, 0, 5);

	add(createTxtTransacao(TELA_LOGIN), 1, 1);
	add(createTxtTransacao(TELA_LEITOR_ARQUIVO), 1, 2);
	add(createTxtTransacao(TELA_LEITOR_PORTA), 1, 3);
	add(createTxtTransacao(TELA_TRANSACOES), 1, 4);
	add(createTxtTransacao(TELA_SOBRE), 1, 5);
    }
    
    private TextField createTxtTransacao(String codigo) {
	TextField txtTransacao = new TextField(codigo);
	txtTransacao.setDisable(true);
	
	return txtTransacao;
    }
}
