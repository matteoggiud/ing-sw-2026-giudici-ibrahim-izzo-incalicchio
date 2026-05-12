package it.polimi.ingsw.client.view.tui;

import it.polimi.ingsw.client.GenericVirtualServer;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ViewController;
import it.polimi.ingsw.client.network.NetworkBuilder;
import it.polimi.ingsw.client.ClientState;
import it.polimi.ingsw.client.network.NetworkConnectionStrategy;
import it.polimi.ingsw.client.network.RmiConnectionStrategy;
import it.polimi.ingsw.client.network.SocketConnectionStrategy;
import it.polimi.ingsw.client.view.Credentials;
import it.polimi.ingsw.client.view.gui.GuiContext;
import javafx.scene.control.Label;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * TuiClientApp is a command-line interface (CLI) application for a client that interacts with a game server.
 * It provides a text-based user interface (TUI) for users to perform various actions such as logging in,
 * registering, creating games, joining games, and viewing available games. The application uses a ClientController
 * to handle the logic of these actions and a Scanner to read user input from the console.
 * The commands are registered in a map, allowing for easy addition of new commands in the future.
 */
public class TuiClientApp {
    private ClientController controller;
    private final Scanner scanner;
    private final Map<String, CommandDefinition> commands;
    private ClientState clientState;
    private TuiClientView view;

    public TuiClientApp(Scanner scanner) {
        this.commands = new LinkedHashMap<>();
        this.scanner = scanner;

        registerCommands();
    }

    public void start(String[] args) {
        String protocol = args.length > 0 ? args[0] : "SOCKET";
        String host = args.length > 1 ? args[1] : "localhost";

        this.clientState = new ClientState();
        this.view = new TuiClientView(clientState);

        ViewController viewController = new ViewController(clientState, view);

        NetworkConnectionStrategy connectionStrategy = "RMI".equals(protocol) ? new RmiConnectionStrategy() : new SocketConnectionStrategy();
        try {
            // Connect to server and get virtual server proxy
            GenericVirtualServer network = connectionStrategy.connect(host, viewController, clientState, view);

            // Initialize client controller with network connection and the view
            this.controller = new ClientController(network);
            clientState.connect();
            clientState.setStatusMessage("Login required!");
            view.showAuthenticator("Login required!");
        } catch (Exception e) {
            System.out.println("Failed to connect to server: " + e.getMessage());
            view.showError("Failed to connect to server: " + e.getMessage());
        }


        this.run();
    }

    /**
     * Starts the TUI client application. It displays a welcome message and enters a loop where it prompts the user for input.
     */
    public void run() {
        printWelcome();

        while (clientState.isConnected()) {
            System.out.print("\nmesos> ");
            String line = scanner.nextLine().trim();

            if (line.isBlank()) {
                continue;
            }

            String[] parts = line.split("\\s+");
            String commandName = parts[0].toLowerCase();

            CommandDefinition command = commands.get(commandName);
            if (command == null) {
                System.out.println("Unknown command. Type 'help'.");
                continue;
            }

            try {
                command.handler().accept(parts);
            } catch (RuntimeException e) {
                System.out.println("[CLIENT ERROR] " + e.getMessage());
            }
        }
    }

    /**
     * Registers the available commands for the TUI client application.
     */
    private void registerCommands() {
        put("help", "help", "Show this help menu", this::printHelp);

        put("login", "login <nickname> <password>", "Login with username and password", this::handleLogin);
        put("register", "register <nickname> <password>", "Create a new account", this::handleRegistration);

        put("games", "games", "Show available games", args -> controller.getAvailableGames());
        put("create", "create <expectedNumOfPlayers>", "Create a new game", this::handleCreateGame);
        put("join", "join <gameId>", "Join an existing game", this::handleJoin);
        put("left", "left", "Left the actual game in lobby", args -> controller.leftGame()); // to create

        put("quit", "quit", "Exit the client", args -> quit());
        put("place", "place <position>", "Place your totem on an offer box position", this::handlePlaceTotem);
        put("draw", "draw <upper|lower> <cardId>", "Draw a card during resolve or extra action", this::handleDrawCard);
        put("draw-extra", "draw-extra <cardId>", "Draw a card during resolve or extra action", this::handleDrawExtraCard);
        put("deck", "deck [nickname]", "Show all decks or one player's deck", this::handleDeck);
        put("rankings", "rankings <numPlayers>", "Show the top numPlayers rankings", this::handleRankings);
    }


    /**
     * Registers a command in the commands map with its name, usage, description, and handler.
     *
     * @param name        the name of the command (e.g., "login", "register")
     * @param usage       a string describing how to use the command (e.g., "login", "join <gameId>")
     * @param description a brief description of what the command does
     * @param handler     a Consumer that takes the command arguments and executes the corresponding action when the command is invoked
     */
    private void put(String name, String usage, String description, Consumer<String[]> handler) {
        commands.put(name, new CommandDefinition(usage, description, handler));
    }

    /**
     * Handles the "register" command, which allows the user to create a new account by providing a nickname and password.
     *
     * @param parts an array of strings representing the command and its arguments, where parts[0] is the command name ("register"), parts[1] is the nickname, and parts[2] is the password.
     */
    private void handleRegistration(String[] parts) {
        if (parts.length != 3) {
            System.out.println(commands.get("register").usage);
            return;
        }

        try {
            String username = parts[1].trim();
            String password = parts[2].trim();
            controller.submitRegistration(new Credentials(username, password));
        } catch (Exception e) {
            System.out.println("Invalid parameters");
        }
    }

    /**
     * Handles the "login" command, which allows the user to log in to their account by providing a nickname and password.
     *
     * @param parts an array of strings representing the command and its arguments, where parts[0] is the command name ("login"), parts[1] is the nickname, and parts[2] is the password.
     */
    private void handleLogin(String[] parts) {
        if (parts.length != 3) {
            System.out.println(commands.get("login").usage);
            return;
        }

        try {
            String username = parts[1].trim();
            String password = parts[2].trim();
            controller.submitLogin(new Credentials(username, password));
        } catch (Exception e) {
            System.out.println("Invalid parameters");
        }
    }

    /**
     * Handles the "join" command, which allows the user to join an existing game by providing the game ID.
     *
     * @param parts an array of strings representing the command and its arguments.
     */
    private void handleJoin(String[] parts) {
        if (parts.length != 2) {
            System.out.println(commands.get("join").usage);
            return;
        }

        try {
            int gameId = Integer.parseInt(parts[1]);
            controller.joinGame(gameId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid gameId. It must be an integer.");
        }
    }

    /**
     * Handles the "create" command, which allows the user to create a new game by providing the expected number of players.
     *
     * @param parts an array of strings representing the command and its arguments.
     */
    private void handleCreateGame(String[] parts) {
        if (parts.length != 2) {
            System.out.println(commands.get("create").usage);
            return;
        }

        try {
            int expectedNumOfPlayers = Integer.parseInt(parts[1]);
            controller.createGame(expectedNumOfPlayers);
        } catch (NumberFormatException e) {
            System.out.println("Invalid expectedNumOfPlayers. It must be an integer.");
        }
    }

    private void quit() {
        controller.disconnect();
    }

    private void printWelcome() {
        //System.out.println("---MESOS TUI---");
        //System.out.println(commands.get("help").usage);
    }

    private void printHelp(String[] parts) {
        System.out.println("Commands:");
        for (CommandDefinition cmd : commands.values()) {
            System.out.printf("  %-16s %s%n", cmd.usage(), cmd.description());
        }
    }

    public void setController(ClientController clientController) {
        this.controller = clientController;
    }

    private record CommandDefinition(
            String usage,
            String description,
            Consumer<String[]> handler
    ) {
    }

    private void handlePlaceTotem(String[] parts) {
        if (parts.length != 2) {
            System.out.println(commands.get("place").usage);
            return;
        }

        try {
            int offerBoxPosition = Integer.parseInt(parts[1]);
            controller.placeTotem(offerBoxPosition);
        } catch (NumberFormatException e) {
            System.out.println("Invalid position. It must be an integer.");
        }
    }


    private void handleDrawCard(String[] parts) {
        if (parts.length != 3) {
            System.out.println(commands.get("draw").usage);
            return;
        }

        String row = parts[1].toLowerCase();
        boolean fromUpperRow;

        if (row.equals("upper") || row.equals("u")) {
            fromUpperRow = true;
        } else if (row.equals("lower") || row.equals("l")) {
            fromUpperRow = false;
        } else {
            System.out.println("Invalid row. Use upper or lower.");
            return;
        }

        try {
            int selectedCardId = Integer.parseInt(parts[2]);
            controller.drawCard(selectedCardId, fromUpperRow);
        } catch (NumberFormatException e) {
            System.out.println("Invalid cardId. It must be an integer.");
        }
    }

    private void handleDrawExtraCard(String[] parts) {
        if (parts.length != 2) {
            System.out.println(commands.get("draw-extra").usage);
            return;
        }
        try {
            int selectedCardId = Integer.parseInt(parts[1]);
            controller.drawExtraCard(selectedCardId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid cardId. It must be an integer.");
        }
    }

    private void handleRankings(String[] parts) {
        if (parts.length != 2) {
            System.out.println(commands.get("rankings").usage);
            return;
        }

        try {
            int numPlayers = Integer.parseInt(parts[1]);
            controller.getRankings(numPlayers);
        } catch (NumberFormatException e) {
            System.out.println("Invalid numPlayers. It must be an integer.");
        }
    }

    private void handleDeck(String[] parts) {
        if (parts.length == 1) {
            view.showDecks();
            return;
        }

        if (parts.length == 2) {
            view.showDeck(parts[1]);
            return;
        }

        System.out.println(commands.get("deck").usage);
    }


}