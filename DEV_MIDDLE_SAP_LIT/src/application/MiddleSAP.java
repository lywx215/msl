package application;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import services.LeitorArquivoService;

public class MiddleSAP extends Application {

    private static BorderPane mainPane;
    private static boolean firstTime;
    private static TrayIcon trayIcon;
    private static Stage stage;

    public static void main(String[] args) {
	launch(args);
    }

    public void start(Stage stage) throws Exception {
	this.stage = stage;

	PropertyConfigurator.configure(createLogConfig());
	createTrayIcon();

	firstTime = true;
	Platform.setImplicitExit(false);

	stage.setTitle("Middleware SAP LIT");
	stage.getIcons().add(new Image(getClass().getResourceAsStream("images/tray.png")));

	/// Painel Principal

	mainPane = new BorderPane();
	mainPane.setTop(new MenuControll(mainPane));
	mainPane.setBottom(new StatusPanel());

	Scene scene = new Scene(mainPane, 380, 550);

	stage.setScene(scene);
	stage.show();
    }

    public static void infoLog(Class clazz, String info) {
	Logger.getLogger(clazz).info(info);
    }

    public static void debugLog(Class clazz, String info) {
	Logger.getLogger(clazz).debug(info);
    }

    public void createTrayIcon() {
	if (SystemTray.isSupported()) {
	    SystemTray tray = SystemTray.getSystemTray();
	    java.awt.Image image = null;
	    try {
		image = ImageIO.read(getClass().getResourceAsStream("images/tray.png"));
	    } catch (IOException ex) {
		MiddleSAP.debugLog(getClass(), ex.getLocalizedMessage());
	    }

	    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		public void handle(WindowEvent t) {
		    hide();
		}
	    });

	    final ActionListener closeListener = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
		    sair();
		}
	    };

	    ActionListener showListener = new ActionListener() {
		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
		    Platform.runLater(new Runnable() {
			public void run() {
			    stage.show();
			    stage.centerOnScreen();
			}
		    });
		}
	    };

	    PopupMenu popup = new PopupMenu();

	    MenuItem showItem = new MenuItem("Abrir");
	    showItem.addActionListener(showListener);
	    popup.add(showItem);

	    MenuItem closeItem = new MenuItem("Encerrar");
	    closeItem.addActionListener(closeListener);
	    popup.add(closeItem);

	    trayIcon = new TrayIcon(image, "Middleware SAP LIT", popup);
	    trayIcon.addActionListener(showListener);

	    try {
		tray.add(trayIcon);
	    } catch (AWTException e) {
		e.printStackTrace();
	    }
	}
    }

    public static void showProgramIsMinimizedMsg() {
	if (firstTime) {
	    trayIcon.displayMessage("Middleware SAP LIT", "Continua executando em background",
		    TrayIcon.MessageType.NONE);
	    firstTime = false;
	}
    }

    private static void hide() {
	Platform.runLater(new Runnable() {
	    @Override
	    public void run() {
		if (SystemTray.isSupported()) {
		    stage.hide();
		    showProgramIsMinimizedMsg();
		} else {
		    sair();
		}
	    }
	});
    }

    public static void sair() {
	Platform.runLater(new Runnable() {
	    public void run() {
		try {
		    LeitorArquivoService.disconnect();
		    LeitorPorta.fecharCom();
		} catch (Exception e) {
		    e.printStackTrace();
		}

		System.exit(0);
	    }
	});

	if (SystemTray.isSupported()) {
	    SystemTray.getSystemTray().remove(trayIcon);

	}

	Platform.exit();
    }

    private Properties createLogConfig() {
	// #lembrando a ordem: DEBUG - INFO - WARN - ERROR - FATAL

	Properties logProp = new Properties();
	logProp.setProperty("log4j.rootCategory", "INFO,stdout,fileOut");
	logProp.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
	logProp.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
	logProp.setProperty("log4j.appender.stdout.layout.ConversionPattern",
		"(%c:%L) %-3d{dd MMM yy HH:mm:ss} %5p | - %m%n");
	logProp.setProperty("log4j.appender.fileOut", "org.apache.log4j.RollingFileAppender");
	logProp.setProperty("log4j.appender.fileOut.File", "peso.log");
	logProp.setProperty("log4j.appender.fileOut.MaxFileSize", "5000KB");
	logProp.setProperty("log4j.appender.fileOut.MaxBackupIndex", "1");
	logProp.setProperty("log4j.appender.fileOut.layout", "org.apache.log4j.PatternLayout");
	logProp.setProperty("log4j.appender.fileOut.layout.ConversionPattern",
		"(%c:%L) %-3d{dd MMM yy HH:mm:ss} %5p | - %m%n");

	return logProp;
    }

}
