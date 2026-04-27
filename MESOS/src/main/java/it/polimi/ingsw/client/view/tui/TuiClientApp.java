package it.polimi.ingsw.client.view.tui;

import it.polimi.ingsw.client.controller.ClientController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class TuiClientApp {
    private final ClientController controller;
    private final Scanner scanner;
    private final Map<String, CommandDefinition> commands;
    private boolean running;

    public TuiClientApp(ClientController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
        this.commands = new LinkedHashMap<>();
        this.running = true;

        registerCommands();
    }

    public void run() {
        printWelcome();

        while (running) {
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

        System.out.println("Bye.");
    }

    private void registerCommands() {
        put("help", "help", "Show this help menu", args -> printHelp());
        put("login", "login", "Login with username and password", args -> controller.login());
        put("register", "register", "Create a new account", args -> controller.register());
        put("create", "create", "Create a new game", args -> controller.createGame());
        put("join", "join <gameId>", "Join an existing game", this::handleJoin);
        put("games", "games", "Show available games", args -> controller.getAvailableGames());
        put("quit", "quit", "Exit the client", args -> quit());
        put("exit", "exit", "Exit the client", args -> quit());
    }

    private void put(String name, String usage, String description, Consumer<String[]> handler) {
        commands.put(name, new CommandDefinition(usage, description, handler));
    }

    private void handleJoin(String[] parts) {
        if (parts.length != 2) {
            System.out.println("Usage: join <gameId>");
            return;
        }

        try {
            int gameId = Integer.parseInt(parts[1]);
            controller.joinGame(gameId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid gameId. It must be an integer.");
        }
    }

    private void quit() {
        // Se hai il metodo nel controller, decommenta:
        // controller.disconnect();
        running = false;
    }

    private void printWelcome() {
        System.out.println("MESOS TUI");
        System.out.println("Type 'help' to show commands.");
    }

    private void printHelp() {
        System.out.println("Commands:");
        for (CommandDefinition cmd : commands.values()) {
            System.out.printf("  %-16s %s%n", cmd.usage(), cmd.description());
        }
    }

    private record CommandDefinition(
            String usage,
            String description,
            Consumer<String[]> handler
    ) {
    }
}