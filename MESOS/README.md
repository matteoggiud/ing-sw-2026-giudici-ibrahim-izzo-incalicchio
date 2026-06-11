# Progetto Ingegneria del Software - MESOS

## Componenti del gruppo

* Matteo Giudici
* Pula Ibrahim
* Andrea Izzo
* Francesco Incalicchio

## Funzionalità implementate

* **Regole:** regole complete del gioco.
* **Interfacce:** TUI e GUI.
* **Connessione:** Socket e RMI.
* **Partite multiple:** gestione di più partite in parallelo tramite lobby.
* **Persistenza:** salvataggio e ripristino delle partite tramite database PostgreSQL.
* **Rankings:** classifiche dei giocatori.

## Requisiti

Per eseguire il progetto dai JAR sono necessari:

* **Java 25** o superiore.
* **PostgreSQL** avviato sulla macchina del server.
* Credenziali PostgreSQL valide configurate in `server.properties`; non è necessario creare manualmente il database, perché il server lo crea e lo inizializza automaticamente all'avvio se assente.

## Struttura dei JAR

I JAR finali si trovano in:

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

`Client-mac-aarch64.jar` è il client per Mac Apple Silicon, quindi Mac M1/M2/M3.

`Client-win.jar` è il client per Windows.

`Server.jar` è il server da usare, adatto per entrambe le piattaforme.

## Configurazione

Server e client hanno già una configurazione di default inclusa nei rispettivi JAR.

All'avvio, il programma carica prima le proprietà interne al JAR e poi cerca un file esterno nella cartella da cui viene eseguito il comando `java -jar`:

* `server.properties` per il server;
* `client.properties` per il client.

Se il file esterno esiste, le proprietà definite al suo interno sovrascrivono quelle di default.

I file `.properties` forniti nei deliverables possono essere modificati direttamente per personalizzare l'esecuzione.

### Configurazione del server

| Proprietà | Default                                  | Descrizione |
| --- |------------------------------------------| --- |
| `server.fast_start` | `true`                                   | Se `true`, il server usa la configurazione senza chiedere input da terminale. |
| `server.reload_games_on_start` | `true`                                   | Se `true`, il server ripristina automaticamente le partite salvate e ancora in corso. |
| `server.port.socket` | `12345`                                  | Porta usata per la connessione Socket. |
| `server.port.rmi` | `1234`                                   | Porta usata per il registro RMI. |
| `server.rmi.name` | `MESOS_RMI`                              | Nome con cui il servizio RMI viene registrato. |
| `db.url` | `jdbc:postgresql://localhost:5432/mesos` | URL del database usato dal server. |
| `db.user` | `postgres`                               | Utente PostgreSQL. |
| `db.password` | `postgres`                               | Password dell'utente PostgreSQL. |

All'avvio il server crea il database se assente, inizializza lo schema e inserisce i dati iniziali. L'utente PostgreSQL configurato deve quindi poter accedere a PostgreSQL e creare/usare il database indicato.

### Configurazione del client

| Proprietà | Default | Descrizione |
| --- | --- | --- |
| `client.fast_start` | `true` | Se `true`, il client usa la configurazione senza chiedere input da terminale. |
| `server.host` | `localhost` | Indirizzo IP o hostname del server. Usare `localhost` solo se client e server sono sulla stessa macchina. |
| `connection.protocol` | `SOCKET` | Protocollo di connessione: `SOCKET` oppure `RMI`. |
| `client.ui` | `GUI` | Interfaccia da avviare: `GUI` oppure `TUI`. |

Per scegliere manualmente interfaccia, protocollo e indirizzo server all'avvio, impostare `client.fast_start=false`.

## Avvio da JAR

### 1. Configurare PostgreSQL

Prima di avviare il server, assicurarsi che PostgreSQL sia in esecuzione e che le credenziali in `server.properties` siano corrette.

Con la configurazione di default il server prova a usare:

```text
jdbc:postgresql://localhost:5432/mesos
utente: postgres
password: postgres
```

### 2. Avviare il server

Aprire un terminale nella cartella del server:

```bash
cd deliverables/final/jar/server
java -jar Server.jar
```

Il server avvia sia Socket sia RMI. Di default:

```text
Socket: porta 12345
RMI: porta 1234, nome MESOS_RMI
```

Se si vuole forzare l'indirizzo IP usato dal server per RMI, è possibile passarlo come primo argomento:

```bash
java -jar Server.jar <server-ip>
```

### 3. Avviare il client su Mac Apple Silicon

Aprire un nuovo terminale nella cartella del client:

```bash
cd deliverables/final/jar/client
java -jar Client-mac-aarch64.jar
```

### 4. Avviare il client su Windows

Aprire un nuovo terminale nella cartella del client:

```bash
cd deliverables/final/jar/client
java -jar Client-win.jar
```

### 5. Avviare più client

Per giocare in più persone, avviare un client per ogni giocatore, ciascuno in un terminale separato o su macchine diverse.

Tutti i client devono puntare allo stesso server tramite `server.host`.


## Build dei JAR

Per rigenerare tutti i JAR da sorgente, eseguire dalla root del progetto:

```bash
mvn clean package
```

Il comando genera:

```text
target/Server.jar
target/Client-mac-aarch64.jar
target/Client-win.jar
```

## Problemi comuni

### UnsupportedClassVersionError

Il progetto è compilato con Java 25. Se compare un errore simile a `UnsupportedClassVersionError`, verificare la versione di Java:

```bash
java -version
```

### Il client non si connette al server

Controllare che:

* il server sia avviato;
* `server.host` in `client.properties` punti all'IP corretto;
* il protocollo scelto in `connection.protocol` sia `SOCKET` oppure `RMI`;
* le porte del server non siano bloccate da firewall o già occupate.

### Il server non parte per errori PostgreSQL

Controllare che:

* PostgreSQL sia avviato;
* `db.url`, `db.user` e `db.password` siano corretti;
* l'utente PostgreSQL abbia i permessi necessari per creare e usare il database `mesos`.

### Il client grafico non parte

Usare il JAR corretto per la piattaforma:

* Mac Apple Silicon: `Client-mac-aarch64.jar`.
* Windows: `Client-win.jar`.

In caso di problemi con la GUI è comunque possibile usare la TUI impostando:

```properties
client.ui=TUI
```
