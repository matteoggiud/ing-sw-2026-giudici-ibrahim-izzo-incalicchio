# Progetto Ingegneria del Software - MESOS

## Componenti del gruppo
* Matteo Giudici
* Pula Ibrahim
* Andrea Izzo
* Francesco Incalicchio

## Funzionalità implementate
* **Regole:** Regole Complete (Standard Rule Set)
* **Interfacce:** TUI (Testuale) e GUI (Grafica)
* **Connessione:** Socket e RMI
* **Funzionalità avanzate:**
  * **Partite Multiple:** Gestione tramite un sistema di Lobby e `GamePortal` per permettere l'esecuzione di più partite in parallelo.
  * **Persistenza:** Salvataggio su database (PostgreSQL) dello stato delle partite per poterle ripristinare.
  * **Rankings (Classifiche):** Sistema di classificazione per visualizzare i punteggi e la posizione dei giocatori.

## Istruzioni su come eseguire il progetto dai JAR

### Configurazione Preliminare
Prima di avviare i JAR, l'unica operazione necessaria è configurare i parametri all'interno dei file di properties dedicati:
* Nel file **`server.properties`**: configurate le porte di rete, la connessione al database (URL, utente, password) e le opzioni del server.
* Nel file **`client.properties`**: configurate l'indirizzo IP del server a cui connettervi, il protocollo (Socket o RMI) e l'interfaccia desiderata (TUI o GUI).

### Avvio del Server
Dopo aver impostato le preferenze nel file di properties, aprite un terminale nella cartella contenente i JAR ed eseguite:
```bash
java -jar Server.jar

### Avvio del Client
Allo stesso modo per il client, aprite un nuovo terminale ed eseguite:

```bash
java -jar Client.jar