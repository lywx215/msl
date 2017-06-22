package services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import application.StatusPanel;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SerialService implements Runnable, SerialPortEventListener {

    public String DadosLidos;
    public int nodeBytes;
    private int baudrate;
    private int timeout;
    private CommPortIdentifier cp;
    private SerialPort portaSerial;
    private OutputStream saida;
    private InputStream entrada;
    private Thread threadLeitura;
    private boolean idPortaOk;
    private boolean portaOk;
    private boolean leitura;
    private boolean escrita;
    private String porta;
    protected String peso;
    private int databits;
    private int stopbits;
    private int paridade;
    protected static ArrayList<String> portas;

    public void setProperties(int baudrate, int databits, int paridade, int stopbits, String porta) {
	this.baudrate = baudrate;
	this.databits = databits;
	this.paridade = paridade;
	this.stopbits = stopbits;
	this.porta = porta;
    }

    ////////////
    /////////// Obter ID da porta para ser utilizado na identificação da mesma
    ///////////
    public void obterIdPorta() {
	try {
	    cp = CommPortIdentifier.getPortIdentifier(porta);

	    if (cp == null) {
		System.out.println("Erro na porta!");
		idPortaOk = false;
		System.exit(1);
	    }

	    idPortaOk = true;

	} catch (Exception e) {
	    System.out.println("Erro obtendo ID da porta: " + e);
	    idPortaOk = false;
	    System.exit(1);
	}
    }

    ////////////
    /////////// Abrir a comunicação com a porta serial para enviar/ler a serial
    ///////////
    public void abrirPorta() {
	try {
	    portaSerial = (SerialPort) cp.open("SerialComLeitura", timeout);
	    portaOk = true;

	    /// Configurar Parâmetros
	    portaSerial.setSerialPortParams(baudrate, databits, stopbits, paridade);
	    portaSerial.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

	} catch (Exception e) {
	    portaOk = false;
	    System.out.println("Erro abrindo comunicação: " + e);
	    System.exit(1);
	}
    }

    ////////////
    /////////// Habilitar a leitura da porta serial, monitorando a porta e
    //////////// obtendo as entradas de dados do fluxo
    ///////////
    public void lerDados() {
	if (escrita == false) {
	    try {
		entrada = portaSerial.getInputStream();
	    } catch (Exception e) {
		System.out.println("Erro de stream: " + e);
		System.exit(1);
	    }

	    try {
		portaSerial.addEventListener(this);
	    } catch (Exception e) {
		System.out.println("Erro de listener: " + e);
		System.exit(1);
	    }

	    portaSerial.notifyOnDataAvailable(true);

	    try {
		threadLeitura = new Thread(this);
		threadLeitura.start();

		run();

	    } catch (Exception e) {

		System.out.println("Erro de thread: " + e);

	    }
	}
    }

    ////////////
    /////////// Envia uma string para a porta serial
    ///////////
    public void enviarString(int msg) {
	if (escrita == true) {
	    try {
		saida = portaSerial.getOutputStream();
		System.out.println("FLUXO OK!");
	    } catch (Exception e) {
		System.out.println("Erro.STATUS: " + e);
	    }

	    try {
		System.out.println("Enviando um byte para " + porta);
		System.out.println("Enviando: " + msg);

		saida.write(msg);
		Thread.sleep(100);

		saida.flush();

	    } catch (Exception e) {
		System.out.println("Houve um erro durante o envio.");
		System.out.println("STATUS: " + e);
		System.exit(1);
	    }
	}

	else {
	    System.exit(1);
	}
    }

    public void habilitarEscrita() {
	escrita = true;
	leitura = false;
    }

    public void habilitarLeitura() {
	escrita = false;
	leitura = true;
    }

    public void setPeso(String peso) {
	this.peso = peso;
	StatusPanel.setInfoText("Peso recebido: " + peso);
    }

    public String getPeso() {
	return peso;
    }

    ////////////
    /////////// Gerencia os dados recebidos pela serial
    ///////////
    public void serialEvent(SerialPortEvent ev) {
	StringBuffer bufferLeitura = new StringBuffer();
	int novoDado = 0;

	switch (ev.getEventType()) {
	case SerialPortEvent.BI:
	case SerialPortEvent.CD:
	case SerialPortEvent.CTS:
	case SerialPortEvent.DSR:
	case SerialPortEvent.FE:
	case SerialPortEvent.OE:
	case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
	case SerialPortEvent.PE:
	case SerialPortEvent.RI:
	    break;

	case SerialPortEvent.DATA_AVAILABLE:
	    while (novoDado != -1) {
		try {
		    novoDado = entrada.read();

		    if (novoDado == -1) {
			break;
		    }

		    if ('\r' == (char) novoDado) {
			bufferLeitura.append('\n');
		    } else {
			bufferLeitura.append((char) novoDado);
		    }

		} catch (IOException ioe) {
		    System.out.println("Erro na leitura da serial: " + ioe);
		}
	    }

	    setPeso(new String(bufferLeitura));

	    System.out.println(getPeso());

	    break;
	}
    }

    public void fecharCom() {
	try {
	    portaSerial.close();
	} catch (Exception e) {
	    System.out.println("Erro fechando porta: " + e);
	    System.exit(0);
	}
    }

    ////////////
    /////////// Armazena lista de portas disponíveis para comunicação
    ///////////
    public static void listarPortas() {
	Enumeration listaDePortas = CommPortIdentifier.getPortIdentifiers();
	int i = 0;

	portas = new ArrayList<String>();

	while (listaDePortas.hasMoreElements()) {
	    CommPortIdentifier ips = (CommPortIdentifier) listaDePortas.nextElement();
	    portas.add(ips.getName());
	    i++;
	}
    }

    public static ArrayList<String> obterPortas() {
	return portas;
    }

    public String obterPorta() {
	return porta;
    }

    public int obterBaudrate() {
	return baudrate;
    }

    @Override
    public void run() {
	try {
	    Thread.sleep(5);
	} catch (Exception e) {
	    System.out.println("Erro de thread: " + e);
	}
    }

}
