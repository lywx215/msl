package services;

import java.util.Properties;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.Environment;

public class JCOProvider implements DestinationDataProvider {

    private String SAP_SERVER = "SAPSERVER";
    private DestinationDataEventListener eventListener;
    private Properties destProperties;
    private static int connection;

    public JCOProvider(Properties userProp) {
	SAP_SERVER += connection++;
	destProperties = userProp;

	Environment.registerDestinationDataProvider(this);

	if (eventListener != null) {
	    eventListener.updated(SAP_SERVER);
	}
    }

    @Override
    public Properties getDestinationProperties(String name) {
	return destProperties;
    }

    @Override
    public boolean supportsEvents() {
	return true;
    }

    @Override
    public void setDestinationDataEventListener(DestinationDataEventListener eventListener) {
	this.eventListener = eventListener;
    }

    public void disconnect() {
	if (eventListener != null) {
	    destProperties = null;
	    eventListener.deleted(SAP_SERVER);
	}

	if (Environment.isDestinationDataProviderRegistered()) {
	    Environment.unregisterDestinationDataProvider(this);
	}
    }
    
    public String getDestinationName() {
	return SAP_SERVER;
    }
}