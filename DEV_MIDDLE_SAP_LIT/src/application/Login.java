package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import com.sap.conn.jco.ext.DestinationDataProvider;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import services.LoginService;
import services.StringUtils;
import services.XptoUtils;

public class Login extends VBox {

    private static final String USER_PROPERTIES = "user.properties";

    private TextField txtHost;
    private TextField txtRouter;
    private TextField txtClient;
    private TextField txtUser;
    private PasswordField txtPassword;
    private ChoiceBox<String> txtLanguage;
    private CheckBox cbSalvar;
    private TextField txtSysnr;

    private Button btn;

    private GridPane grid;

    public Login() {
	grid = new GridPane();
	
	setAlignment(Pos.CENTER);
	grid.setHgap(10);
	grid.setVgap(10);
	setPadding(new Insets(25, 25, 25, 25));

	Text scenetitle = new Text("Bem Vindo!");
	scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	grid.add(scenetitle, 0, 0, 2, 1);

	Label lblHost = new Label("Host:");
	Label lblRouter = new Label("Router:");
	Label lblSysnr = new Label("Sysnr:");
	Label lblClient = new Label("Client:");
	Label lblUser = new Label("User:");
	Label lblPassword = new Label("Password:");
	Label lblLanguage = new Label("Language:");

	txtHost = new TextField();
	txtRouter = new TextField();
	txtSysnr = new TextField();
	txtClient = new TextField();
	txtUser = new TextField();
	txtPassword = new PasswordField();
	txtLanguage = new ChoiceBox<String>();
	txtLanguage.getItems().addAll("EN", "PT");
	txtLanguage.getSelectionModel().selectFirst();

	grid.add(lblHost, 0, 1);
	grid.add(lblRouter, 0, 2);
	grid.add(lblSysnr, 0, 3);
	grid.add(lblClient, 0, 4);
	grid.add(lblUser, 0, 5);
	grid.add(lblPassword, 0, 6);
	grid.add(lblLanguage, 0, 7);

	grid.add(txtHost, 1, 1);
	grid.add(txtRouter, 1, 2);
	grid.add(txtSysnr, 1, 3);
	grid.add(txtClient, 1, 4);
	grid.add(txtUser, 1, 5);
	grid.add(txtPassword, 1, 6);
	grid.add(txtLanguage, 1, 7);

	cbSalvar = new CheckBox("Salvar configurações");
	cbSalvar.setSelected(true);
	grid.add(cbSalvar, 0, 8);

	btn = new Button("Entrar");
	HBox hbBtn = new HBox(10);
	hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
	hbBtn.getChildren().add(btn);
	
	btn.setOnAction(e -> logar());

	getChildren().addAll(grid, hbBtn);
	
	loadProperties();
    }

    private void loadProperties() {
	Properties props = null;

	try {
	    File userProp = new File(USER_PROPERTIES);

	    if (userProp.exists()) {
		FileInputStream file = new FileInputStream(userProp);

		props = new Properties();
		props.load(file);

		file.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}

	if (props != null) {
	    txtHost.setText(props.getProperty(DestinationDataProvider.JCO_ASHOST));
	    txtRouter.setText(props.getProperty(DestinationDataProvider.JCO_SAPROUTER));
	    txtSysnr.setText(props.getProperty(DestinationDataProvider.JCO_SYSNR));
	    txtClient.setText(props.getProperty(DestinationDataProvider.JCO_CLIENT));
	    txtUser.setText(props.getProperty(DestinationDataProvider.JCO_USER));
	    txtPassword.setText(XptoUtils.j(props.getProperty(DestinationDataProvider.JCO_PASSWD)));
	    txtLanguage.getSelectionModel().select(props.getProperty(DestinationDataProvider.JCO_LANG));
	}
    }

    private void logar() {
	if(LoginService.isConnected()) {
	    LoginService.disconnect();
	    btn.setText("Conectar");
	    grid.setDisable(false);
	} else {
	    if(LoginService.logar(getloginProperties())) {
		btn.setText("Desconectar");
		grid.setDisable(true);
		
		if (cbSalvar.isSelected()) {
		    salvarUserProperties();
		}
	    }
	    
	}
    }

    private void salvarUserProperties() {
	File destCfg = new File(USER_PROPERTIES);

	Properties loginProp = getloginProperties();
	loginProp.setProperty(DestinationDataProvider.JCO_PASSWD,
		XptoUtils.k(loginProp.getProperty(DestinationDataProvider.JCO_PASSWD)));

	try {
	    FileOutputStream fos = new FileOutputStream(destCfg, false);
	    loginProp.store(fos, "Arquivo de conexao");
	    fos.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private Properties getloginProperties() {
	Properties loginProp = new Properties();
	loginProp.setProperty(DestinationDataProvider.JCO_ASHOST, StringUtils.getStringOrEmpty(txtHost.getText()));
	loginProp.setProperty(DestinationDataProvider.JCO_SAPROUTER, StringUtils.getStringOrEmpty(txtRouter.getText()));
	loginProp.setProperty(DestinationDataProvider.JCO_SYSNR, StringUtils.getStringOrEmpty(txtSysnr.getText()));
	loginProp.setProperty(DestinationDataProvider.JCO_CLIENT, StringUtils.getStringOrEmpty(txtClient.getText()));
	loginProp.setProperty(DestinationDataProvider.JCO_USER, StringUtils.getStringOrEmpty(txtUser.getText()));
	loginProp.setProperty(DestinationDataProvider.JCO_PASSWD, StringUtils.getStringOrEmpty(txtPassword.getText()));
	loginProp.setProperty(DestinationDataProvider.JCO_LANG,
		StringUtils.getStringOrEmpty(txtLanguage.getSelectionModel().getSelectedItem()));

	return loginProp;
    }

}