package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.gui.GuiClientApp;
import it.polimi.ingsw.client.view.tui.TuiClientApp;
import javafx.application.Application;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        // Shared Scanner for user input (passed to view and app)
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- MESOS CLIENT ---");

        // Step 1: Choose UI
        System.out.println("1) GUI client");
        System.out.println("2) TUI client");
        System.out.print("Select interface [1]: ");
        String uiChoice = scanner.nextLine().trim();
        // Default to GUI if input is invalid
        boolean useGui = !uiChoice.equals("2");

        // Step 2: Choose protocol
        System.out.println("\n1) Socket");
        System.out.println("2) RMI");
        System.out.print("Select protocol [1]: ");
        String protocolChoice = scanner.nextLine().trim();
        // Default to Socket if input is invalid
        String protocol = protocolChoice.equals("2") ? "RMI" : "SOCKET";

        System.out.print("\nServer ip [localhost]: ");
        String host = scanner.nextLine().trim();
        if (host.isEmpty()) host = "localhost";

        String[] appArgs = {protocol, host};

        if (useGui) {
            Application.launch(GuiClientApp.class, appArgs);
        } else {
            TuiClientApp tuiApp = new TuiClientApp(scanner);
            tuiApp.init(appArgs);
            tuiApp.run();
        }
    }
}