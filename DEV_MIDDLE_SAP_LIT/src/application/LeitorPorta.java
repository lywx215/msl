package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Properties;

import gnu.io.SerialPort;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import services.SerialService;

public class LeitorPorta extends GridPane {

    private static final int ENQ = 5; // Comando para requisitar peso da balança
    private static final HashMap<String, Integer> paridades = new HashMap<String, Integer>();

    static {
	paridades.put("NONE", SerialPort.PARITY_NONE);
	paridades.put("ODD", SerialPort.PARITY_ODD);
	paridades.put("EVEN", SerialPort.PARITY_EVEN);
	paridades.put("MARK", SerialPort.PARITY_MARK);
	paridades.put("SPACE", SerialPort.PARITY_SPACE);

	SerialService.listarPortas();
    }

    private ChoiceBox<Integer> txtBaudrate;
    private ChoiceBox<Integer> txtDataBits;
    private ChoiceBox<String> txtParidade;
    private ChoiceBox<Integer> txtStopBits;
    private ChoiceBox<String> txtPorta;
    private CheckBox cbSalvar;
    private static SerialService serialService;
    private boolean portaConectada;

    public LeitorPorta() {
	setAlignment(Pos.CENTER);
	setHgap(10);
	setVgap(10);
	setPadding(new Insets(25, 25, 25, 25));

	Text scenetitle = new Text("Conectar balança");
	scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	add(scenetitle, 0, 0, 2, 1);

	Label lblBaudrate = new Label("Taxa de Transmissão:");
	Label lblDataBits = new Label("Bits de Dados:");
	Label lblParidade = new Label("Paridade:");
	Label lblStopBits = new Label("Bits de parada:");
	Label lblPorta = new Label("Porta:");

	txtBaudrate = new ChoiceBox<Integer>();
	txtBaudrate.getItems().addAll(2400, 4800, 9600);
	txtBaudrate.getSelectionModel().selectFirst();

	txtDataBits = new ChoiceBox<Integer>();
	txtDataBits.getItems().addAll(5, 6, 7, 8);
	txtDataBits.getSelectionModel().selectFirst();

	txtParidade = new ChoiceBox<String>();
	txtParidade.getItems().addAll(paridades.keySet());
	txtParidade.getSelectionModel().selectFirst();

	txtStopBits = new ChoiceBox<Integer>();
	txtStopBits.getItems().addAll(1, 2, 3);
	txtStopBits.getSelectionModel().selectFirst();

	txtPorta = new ChoiceBox<String>();
	txtPorta.getItems().addAll(SerialService.obterPortas());
	txtPorta.getSelectionModel().selectFirst();

	add(lblBaudrate, 0, 1);
	add(lblDataBits, 0, 2);
	add(lblParidade, 0, 3);
	add(lblStopBits, 0, 4);
	add(lblPorta, 0, 5);

	add(txtBaudrate, 1, 1);
	add(txtDataBits, 1, 2);
	add(txtParidade, 1, 3);
	add(txtStopBits, 1, 4);
	add(txtPorta, 1, 5);

	cbSalvar = new CheckBox("Salvar configurações");
	cbSalvar.setSelected(true);
	add(cbSalvar, 0, 6);

	Button btn = new Button("Conectar");
	HBox hbBtn = new HBox(10);
	hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
	hbBtn.getChildren().add(btn);
	add(hbBtn, 1, 7);

	btn.setOnAction(e -> conectarBalanca());

	loadProperties();
    }
    
    public static void fecharCom() {
	if(serialService != null) {
	    serialService.fecharCom();
	}
    }
    
    private void loadProperties() {
	Properties props = new Properties();

	try {
	    FileInputStream file = new FileInputStream("./properties/conexao.properties");
	    props.load(file);
	} catch (Exception e) {

	}

	if (! props.isEmpty()) {
	    txtBaudrate.getSelectionModel().select(new Integer(props.getProperty("prop.con.baudrate")));
	    txtDataBits.getSelectionModel().select(new Integer(props.getProperty("prop.con.databits")));
	    txtParidade.getSelectionModel().select(props.getProperty("prop.con.paridade"));
	    txtStopBits.getSelectionModel().select(new Integer(props.getProperty("prop.con.stopbits")));
	    txtPorta.getSelectionModel().select(props.getProperty("prop.con.porta"));
	}

    }

    private void conectarBalanca() {
	if (serialService == null) {
	    serialService = new SerialService();
	}
	
	if(portaConectada) {
	    serialService.fecharCom();
	}
	
	serialService.setProperties(txtBaudrate.getValue(), txtDataBits.getValue(), paridades.get(txtParidade.getValue()), txtStopBits.getValue(), txtPorta.getValue());

	try {
	    serialService.habilitarEscrita();
	    serialService.obterIdPorta();
	    serialService.abrirPorta();
	    serialService.enviarString(ENQ);
	    serialService.habilitarLeitura();
	    serialService.lerDados();

	    StatusPanel.setInfoText("Balança conectada com sucesso, aguardando peso...");
	    
	    
	    if (cbSalvar.isSelected()) {
		salvarPropriedades();
	    }
	    
	    portaConectada = true;
	} catch (Exception e) {
	    StatusPanel.setInfoText("Erro ao conectar balança: " + e);
	}
    }

    private void salvarPropriedades() {
	Properties conProp = new Properties();
	conProp.setProperty("prop.con.baudrate", txtBaudrate.getValue().toString());
	conProp.setProperty("prop.con.databits", txtDataBits.getValue().toString());
	conProp.setProperty("prop.con.paridade", txtParidade.getValue());
	conProp.setProperty("prop.con.stopbits", txtStopBits.getValue().toString());
	conProp.setProperty("prop.con.porta", txtPorta.getValue());

	File destCfg = new File("./properties/conexao.properties");

	try {
	    FileOutputStream fos = new FileOutputStream(destCfg, false);
	    conProp.store(fos, "Arquivo de conexao da balanca");
	    fos.close();
	} catch (Exception e) {
	    throw new RuntimeException("Unable to create the destination files", e);
	}
    }

}
