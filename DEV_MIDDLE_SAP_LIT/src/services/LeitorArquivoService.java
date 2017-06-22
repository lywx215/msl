package services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;

import application.MiddleSAP;
import application.StatusPanel;

public class LeitorArquivoService implements Runnable {

    private static final String ARQUIVO_PESO = "peso.txt";
    private static boolean conected;
    

    private JCoFunction function;
    private JCoDestination destination;

    public static String getArquivoPeso() {
	return ARQUIVO_PESO;
    }

    public static void disconnect() {
	conected = false;
	StatusPanel.setLeitorArquivoEnabled(false);
    }

    @Override
    public void run() {
	conected = true;

	createRfcFuncion();
	StatusPanel.setLeitorArquivoEnabled(true);

	while (conected) {
	    processaArquivoPeso();
	    
	    
	    //Processa arquivo a cada 1 segundo
	    try {
		Thread.sleep(1000);
	    } catch (InterruptedException e) {
		MiddleSAP.debugLog(getClass(), e.toString());
	    }
	}
    }

    private void createRfcFuncion() {
	destination = LoginService.getJcoDestination();
	JCoFunctionTemplate template2 = null;

	if (destination != null) {
	    try {
		template2 = destination.getRepository().getFunctionTemplate("ZWM_RFC_GERA_IMPR_HU_VOL");
	    } catch (JCoException e1) {
		e1.printStackTrace();
	    }

	    if (template2 != null) {
		function = template2.getFunction();
	    }
	}
    }

    private void processaArquivoPeso() {
	File file = new File(ARQUIVO_PESO);

	if (!file.exists()) {
	    return;
	}

	Scanner arq = null;

	try {
	    arq = new Scanner(file);
	} catch (FileNotFoundException e) {
	    MiddleSAP.debugLog(getClass(), e.toString());
	    return;
	}
	
	
	String peso = "";

	while (arq.hasNextLine()) {
	    peso = arq.nextLine().replaceAll(",", ".");
	    StatusPanel.setInfoText("Peso >>> " + peso);

	    if (function != null) {
		function.getImportParameterList().setValue("I_VEMNG", peso);
		try {
		    function.execute(destination);
		} catch (JCoException e) {
		    MiddleSAP.debugLog(getClass(), e.toString());
		}
	    }
	}

	arq.close();
	file.delete();

	String hu = "";
	String aufnr = "";

	if (function != null) {
	    hu = function.getExportParameterList().getString("E_HUKEY");
	    aufnr = function.getExportParameterList().getString("E_AUFNR");
	}

	MiddleSAP.infoLog(getClass(), "|" + aufnr + "|" + hu + "|" + peso + "|");
    }

    public static boolean isConnected() {
	return conected;
    }

}
