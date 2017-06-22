package application;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class MenuControll extends HBox {

    private TextField txtTransaction;
    private Button btnTransaction;
    private BorderPane mainPane;
    private static boolean TRIAL = true;
    private static Login loginApp;
    private static LeitorArquivo leitorArquivo;
    private static Transacoes transacoes;
    private static Sobre sobre;

    public MenuControll(BorderPane mainPane) {
	this.mainPane = mainPane;

	/// Menu Usuario
	MenuItem logar = new MenuItem("Logar");
	MenuItem sair = new MenuItem("Sair");

	logar.setOnAction(e -> abrirTelaLogar());
	sair.setOnAction(e -> MiddleSAP.sair());

	Menu menuUser = new Menu("Usuário");
	menuUser.getItems().addAll(logar, sair);

	/// Menu Conexoes
	MenuItem leitorPorta = new MenuItem("Leitor Porta Serial");
	MenuItem leitorArquivo = new MenuItem("Leitor de Arquivo");

	leitorPorta.setOnAction(e -> abrirTelaLeitorPorta());
	leitorArquivo.setOnAction(e -> abrirTelaLeitorArquivo());

	Menu menuCon = new Menu("Conexões");
	menuCon.getItems().addAll(leitorArquivo, leitorPorta);

	/// Menu Sobre

	MenuItem config = new MenuItem("Transações");
	MenuItem sobre = new MenuItem("Informações");

	config.setOnAction(e -> abrirTelaTransacoes());
	sobre.setOnAction(e -> abrirTelaSobre());

	Menu menuAbout = new Menu("Sobre");
	menuAbout.getItems().addAll(config, sobre);

	/// Adiciona menus ao menubar
	MenuBar menuBar = new MenuBar();
	menuBar.setPadding(new Insets(0, 0, 0, 10));

	menuBar.getMenus().addAll(menuUser, menuCon, menuAbout);

	////// Campo transação
	txtTransaction = new TextField();
	txtTransaction.setOnAction(e -> abrirTransaction());
	btnTransaction = new Button();

	ImageView img = new ImageView(
		new Image(getClass().getResourceAsStream("images/check.png"), 17, 17, true, true));

	btnTransaction.setGraphic(img);
	btnTransaction.setOnAction(e -> abrirTransaction());

	setPadding(new Insets(2, 0, 0, 2));
	getChildren().addAll(btnTransaction, txtTransaction, menuBar);
	
	abrirTelaLogar();
    }

    private void abrirTransaction() {
	abrirTransaction(txtTransaction.getText());
    }

    private void abrirTransaction(String tela) {
	if (tela != null) {
	    switch (tela.toUpperCase()) {
	    case Transacoes.TELA_LOGIN:
		abrirTelaLogar();
		break;
	    case Transacoes.TELA_LEITOR_ARQUIVO:
		abrirTelaLeitorArquivo();
		break;
	    case Transacoes.TELA_LEITOR_PORTA:
		abrirTelaLeitorPorta();
		break;
	    case Transacoes.TELA_TRANSACOES:
		abrirTelaTransacoes();
		break;
	    case Transacoes.TELA_SOBRE:
		abrirTelaSobre();
		break;
	    default:
		StatusPanel.setInfoText("Transação inválida!");
		break;
	    }

	    txtTransaction.clear();
	}
    }

    private void abrirTelaLeitorArquivo() {
	if (leitorArquivo == null) {
	    leitorArquivo = new LeitorArquivo();
	}

	mainPane.setCenter(leitorArquivo);
    }

    private void abrirTelaTransacoes() {
	if (transacoes == null) {
	    transacoes = new Transacoes();
	}
	
	mainPane.setCenter(transacoes);
    }

    private void abrirTelaSobre() {
	if (sobre == null) {
	    sobre = new Sobre();
	}

	mainPane.setCenter(sobre);
    }

    private void abrirTelaLeitorPorta() {
	mainPane.setCenter(TRIAL ? new Text("Versão trial") : new LeitorPorta());
    }

    private void abrirTelaLogar() {
	if (loginApp == null) {
	    loginApp = new Login();
	}

	mainPane.setCenter(loginApp);
    }

}
