# Software Engineering Project - MESOS

The board game Mesos and all related graphic material are the exclusive property of Cranio Creations.

## Team Members

* Matteo Giudici
* Pula Ibrahim
* Andrea Izzo
* Francesco Incalicchio

## Implemented Features

* **Rules:** complete game rules.
* **Interfaces:** TUI and GUI.
* **Connection:** Socket and RMI.
* **Multiple games:** parallel game management through lobbies.
* **Persistence:** game saving and restoration through a PostgreSQL database.
* **Rankings:** player leaderboards.

## Requirements

To run the project from the JAR files, the following are required:

* **Java 25** or later.
* **PostgreSQL** running on the server machine.
* Valid PostgreSQL credentials configured in `server.properties`; the database does not need to be created manually, because the server creates and initializes it automatically at startup if it is missing.

## JAR Structure

The final JAR files are located in:

```text
deliverables/final/jar/
├── client/
│   ├── Client-mac-aarch64.jar
│   ├── Client-win.jar
│   └── client.properties
└── server/
    ├── Server.jar
    └── server.properties
```

`Client-mac-aarch64.jar` is the client for Apple Silicon Macs, such as M1/M2/M3 Macs.

`Client-win.jar` is the client for Windows.

`Server.jar` is the server JAR, suitable for both client platforms.

## Configuration

The server and client already include a default configuration inside their respective JAR files.

At startup, the program first loads the internal properties from the JAR, then looks for an external file in the directory from which the `java -jar` command is executed:

* `server.properties` for the server;
* `client.properties` for the client.

If the external file exists, the properties defined in it override the default ones.

The `.properties` files provided in the deliverables can be edited directly to customize the execution.

### Server Configuration

| Property | Default | Description |
| --- | --- | --- |
| `server.fast_start` | `true` | If `true`, the server uses the configuration without asking for terminal input. |
| `server.reload_games_on_start` | `true` | If `true`, the server automatically restores saved games that are still in progress. |
| `server.port.socket` | `12345` | Port used for Socket connections. |
| `server.port.rmi` | `1234` | Port used for the RMI registry. |
| `server.rmi.name` | `MESOS_RMI` | Name used to register the RMI service. |
| `db.url` | `jdbc:postgresql://localhost:5432/mesos` | URL of the database used by the server. |
| `db.user` | `postgres` | PostgreSQL user. |
| `db.password` | `postgres` | PostgreSQL user password. |

At startup, the server creates the database if it is missing, initializes the schema, and inserts the initial data. The configured PostgreSQL user must therefore be able to access PostgreSQL and create/use the configured database.

### Client Configuration

| Property | Default | Description |
| --- | --- | --- |
| `client.fast_start` | `true` | If `true`, the client uses the configuration without asking for terminal input. |
| `server.host` | `localhost` | Server IP address or hostname. Use `localhost` only if the client and server run on the same machine. |
| `connection.protocol` | `SOCKET` | Connection protocol: `SOCKET` or `RMI`. |
| `client.ui` | `GUI` | Interface to start: `GUI` or `TUI`. |

To manually choose the interface, protocol, and server address at startup, set `client.fast_start=false`.

## Running From JAR

### 1. Configure PostgreSQL

Before starting the server, make sure PostgreSQL is running and that the credentials in `server.properties` are correct.

With the default configuration, the server tries to use:

```text
jdbc:postgresql://localhost:5432/mesos
user: postgres
password: postgres
```

### 2. Start the Server

Open a terminal in the server directory:

```bash
cd deliverables/final/jar/server
java -jar Server.jar
```

The server starts both Socket and RMI. By default:

```text
Socket: port 12345
RMI: port 1234, name MESOS_RMI
```

To force the IP address used by the server for RMI, pass it as the first argument:

```bash
java -jar Server.jar <server-ip>
```

### 3. Start the Client on Apple Silicon Macs

Open a new terminal in the client directory:

```bash
cd deliverables/final/jar/client
java -jar Client-mac-aarch64.jar
```

### 4. Start the Client on Windows

Open a new terminal in the client directory:

```bash
cd deliverables/final/jar/client
java -jar Client-win.jar
```

### 5. Start Multiple Clients

To play with multiple players, start one client for each player, either in separate terminals or on different machines.

All clients must point to the same server through `server.host`.

## Building the JAR Files

To regenerate all JAR files from source, run the following command from the project root:

```bash
mvn clean package
```

The command generates:

```text
target/Server.jar
target/Client-mac-aarch64.jar
target/Client-win.jar
```

## Common Issues

### UnsupportedClassVersionError

The project is compiled with Java 25. If an error similar to `UnsupportedClassVersionError` appears, check the Java version:

```bash
java -version
```

### The Client Does Not Connect to the Server

Check that:

* the server is running;
* `server.host` in `client.properties` points to the correct IP address;
* the selected protocol in `connection.protocol` is either `SOCKET` or `RMI`;
* the server ports are not blocked by a firewall or already in use.

### The Server Does Not Start Due to PostgreSQL Errors

Check that:

* PostgreSQL is running;
* `db.url`, `db.user`, and `db.password` are correct;
* the PostgreSQL user has the permissions required to create and use the `mesos` database.

### The Graphical Client Does Not Start

Use the correct JAR file for the platform:

* Apple Silicon Mac: `Client-mac-aarch64.jar`.
* Windows: `Client-win.jar`.

In case of GUI issues, the TUI can still be used by setting:

```properties
client.ui=TUI
```
