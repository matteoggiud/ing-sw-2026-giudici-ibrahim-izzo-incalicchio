package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.gui.GuiClientApp;
import it.polimi.ingsw.client.view.tui.TuiClientApp;
import javafx.application.Application;
import it.polimi.ingsw.common.utils.ConfigurationManager;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        // Shared Scanner for user input (passed to view and app)
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- MESOS CLIENT ---");

        ConfigurationManager config = ConfigurationManager.getClientInstance();
        boolean fastStart = config.getBooleanProperty("client.fast_start", false);

        boolean useGui;
        String protocol;
        String host;

        if (fastStart) {
            String uiConf = config.getProperty("client.ui", "GUI");
            useGui = !uiConf.equalsIgnoreCase("TUI");
            protocol = config.getProperty("connection.protocol", "SOCKET");
            host = config.getProperty("server.host", "localhost");
            System.out.println("Fast start enabled. Using: " + (useGui ? "GUI" : "TUI") + ", " + protocol + ", " + host);
        } else {
            // Step 1: Choose UI
            System.out.println("1) GUI client");
            System.out.println("2) TUI client");
            System.out.print("Select interface [1]: ");
            String uiChoice = scanner.nextLine().trim();
            // Default to GUI if input is invalid
            useGui = !uiChoice.equals("2");

            // Step 2: Choose protocol
            String defaultProtocol = config.getProperty("connection.protocol", "SOCKET");
            String defaultProtocolChoice = defaultProtocol.equalsIgnoreCase("RMI") ? "2" : "1";
            System.out.println("\n1) Socket");
            System.out.println("2) RMI");
            System.out.print("Select protocol [" + defaultProtocolChoice + "]: ");
            String protocolChoice = scanner.nextLine().trim();
            if (protocolChoice.isEmpty()) protocolChoice = defaultProtocolChoice;
            // Default to Socket if input is invalid
            protocol = protocolChoice.equals("2") ? "RMI" : "SOCKET";

            String defaultHost = config.getProperty("server.host", "localhost");
            System.out.print("\nServer ip [" + defaultHost + "]: ");
            host = scanner.nextLine().trim();
            if (host.isEmpty()) host = defaultHost;
        }

        String[] appArgs = {protocol, host};

        if (useGui) {
            Application.launch(GuiClientApp.class, appArgs);
        } else {
            new TuiClientApp(scanner).start(appArgs);
        }
    }
}