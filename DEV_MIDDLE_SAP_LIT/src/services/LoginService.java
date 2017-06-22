package services;

import java.util.Properties;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;

import application.StatusPanel;

public class LoginService {

    private static JCoDestination dest;
    private static JCOProvider jcoProvider;
    private static boolean connected;

    public static boolean logar(Properties userProp) {
	if (jcoProvider != null) {
	    jcoProvider.disconnect();
	}

	jcoProvider = new JCOProvider(userProp);

	try {
	    dest = JCoDestinationManager.getDestination(jcoProvider.getDestinationName());
	    dest.ping();
	    connected = true;

	    StatusPanel.setInfoText("Conexão estabelecida com sucesso!");
	    StatusPanel.setUserStatusEnabled(connected);

	    return connected;
	} catch (Exception e) {
	    connected = false;

	    StatusPanel.setInfoText("Configurações inválidas! Verifique e tente novamente.");
	    StatusPanel.setUserStatusEnabled(connected);

	    return connected;
	}
    }

    public static boolean isConnected() {
	return connected;
    }

    public static JCoDestination getJcoDestination() {
	return dest;
    }

    public static void disconnect() {
	connected = false;

	if (jcoProvider != null) {
	    jcoProvider.disconnect();
	}

	StatusPanel.setUserStatusEnabled(connected);
    }

}
