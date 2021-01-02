package programmieraufgaben;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Eine vereinfachte Switch Engine mit folgenden möglichen Kommandos:
 * frame <Eingangsportnummer> <Absenderadresse> <Zieladresse>,
 * table,
 * statistics,
 * del Xs bzw. del Xmin,
 * exit
 */
public class SwitchingEngine {
    private static final String WRONG = "Ungültige Eingabe!";
    private static int[] ports;
    private static TableEntry[] table;

    /**
     * Diese Methode überprüft die Eingabe und erstellt die für den
     * weiteren Funktionsablauf nötige Datenstruktur
     *
     * @param portNumber Anzahl der Ports, die der Switch verwalten soll
     * @return Gibt bei erfolgreicher Erstellung TRUE sonst FALSE zurück
     */
    public boolean createSwitch(int portNumber) {
        if (portNumber < 1) {
            System.out.println("Nur natürliche Zahlen ab 1 sind erlaubt.");
            return false;
        }
        ports = new int[portNumber + 1]; // Jeder Index entspricht dem jeweiligen Port; an der Arrayposition jedes Ports wird seine Nutzungsanzahl gespeichert
        table = new TableEntry[256];
        System.out.println("\nEin " + portNumber + "-Port-Switch wurde erzeugt.\n");
        return true;
    }

    /**
     * Diese Methode überprüft und interpretiert die Eingaben und führt
     * die geforderten Funktionen aus.
     *
     * @param command Anweisung, die der Switch verarbeiten soll
     * @return Gibt an ob der Switch beendet werden soll: TRUE beenden, FALSE weitermachen
     */
    public boolean handleCommand(String command) {
        ArrayList<String> arr = new ArrayList<>();
        Pattern pat = Pattern.compile("\\S+");
        Matcher m = pat.matcher(command);
        try {
            // Überprüfung, ob ein korrektes Befehlswort eingegeben wurde
            if (command.matches("\\b(frame)\\s\\d+\\s\\d+\\s\\d+\\b|\\b(del)\\s\\d+(s|min)\\b|^(table|statistics|exit)$")) {

                while (m.find()) {
                    arr.add(m.group());
                }
            } else throw new Exception();
        } catch (Exception e) {
            System.out.println(WRONG);
            return false;
        }
        // Überprüfung, ob die Syntax bzw. die Parameter des eingegebenen Befehls korrekt sind
        switch (arr.get(0)) {
            case "frame":
                if (Integer.parseInt(arr.get(1)) > ports.length - 1 || Integer.parseInt(arr.get(1)) <= 0) {
                    System.out.println("\nDer von Ihnen ausgewählten Port: " + arr.get(1) + " ist ungültig, nur die Ports 1 bis " + (ports.length - 1) + " sind erlaubt und befinden sich im Switch.\n");
                    return false;
                }
                if (!arr.get(2).matches("\\b([1-9][0-9]?|[12][0-4][0-9]|25[0-4])\\b")) {
                    System.out.println("\nDie von Ihnen eingegebene Senderadresse: " + arr.get(2) + " befindet sich außerhalb der erlaubten Adressen 1-254.\n");
                    return false;
                }

                if (!arr.get(3).matches("\\b([1-9][0-9]?|[12][0-4][0-9]|25[0-5])\\b")) {
                    System.out.println("\nDie von Ihnen eingegebene Zieladresse: " + arr.get(3) + " befindet sich außerhalb der erlaubten Adressen 1-255.\n");
                    return false;
                }
                addFrame(Integer.parseInt(arr.get(1)), Integer.parseInt(arr.get(2)), Integer.parseInt(arr.get(3))); // Ist die Anfrage korrekt, wird ein Frame durch den Switch geleitet
                break;
            case "table":
                printTable(table); // Ist die Anfrage korrekt, wird die Table gedruckt
                break;
            case "statistics":
                printStats(); // Ist die Anfrage korrekt, wird die statistics ausgegeben
                break;
            case "del":
                Pattern timePattern = Pattern.compile("(?>(\\d+))(s|min)");
                Matcher time = timePattern.matcher(arr.get(1));
                if (time.find()) {
                    del(Integer.parseInt(time.group(1)), time.group(2)); // Ist die Anfrage korrekt, werden die zu alten Einträge gelöscht
                }

                break;
            case "exit":
                return true; // Der Switch soll beendet werden
            default:
                System.out.println("\nSomething went wrong :(\n");
                return false;
        }
        return false;
    }

    /**
     * Diese Methode leitet ein Frame auf alle Ports außer den übergebenen Eingangsport des Frames weiter
     *
     * @param port Eingangsport des zu broadcastenen Frames
     */
    private static void broadcoast(int port) {
        for (int i = 0; i < ports.length; i++) {
            if (i != port)
                ports[i]++;
        }
        System.out.print("Ausgabe auf allen Ports außer Port " + port + "." + System.lineSeparator());
    }

    /**
     * Es wird ein Frame durch den Switch geleitet. Dabei wird die Switch-Tabelle aktualisiert.
     *
     * @param port          Eingangsport des Frames
     * @param senderAddress Adresse, die das Frame sendet
     * @param targetAddress Adresse, an die das Frame gesendet werden soll
     */
    private static void addFrame(int port, int senderAddress, int targetAddress) {
        // Fall 1: Senderadresse besitzt keinen Eintrag in der Switch-Tabelle
        if (table[senderAddress] == null) {
            // Fall 1.1: Zieladresse besitzt ebenfalls keinen Eintrag in der Switch-Tabelle
            if (table[targetAddress] == null) {
                table[senderAddress] = new TableEntry(port);
                ports[port]++;
                if (targetAddress != 255) {
                    broadcoast(table[senderAddress].getPort());
                } else {
                    System.out.print("Broadcast: ");
                    broadcoast(table[senderAddress].getPort());
                }
            }
            // Fall 1.2: Zieladresse besitzt einen Eintrag in der Switch-Tabelle
            else if (table[targetAddress] != null) {
                // Fall 1.2.1: Eingangsport unterscheidet sich vom Port der Zieladresse
                if (table[targetAddress].getPort() != port) {
                    table[senderAddress] = new TableEntry(port);
                    ports[port]++;
                    ports[table[targetAddress].getPort()]++;
                    System.out.println("Ausgabe auf Port " + table[targetAddress].getPort() + ".");
                }
                // Fall 1.2.2: Eingangsport ist identisch mit dem Port der Zieladresse
                else if (table[targetAddress].getPort() == port) {
                    table[senderAddress] = new TableEntry(port);
                    ports[port]++;
                    System.out.println("Frame wird gefiltert und verworfen.");
                }
            }
        }
        // Fall 2: Die Senderadresse besitzt bereits einen Eintrag in der Switch-Tabelle
        else {
            // Fall 2.1: Zieladresse besitzt keinen Eintrag in der Switch-Tabelle
            if (table[targetAddress] == null) {
                table[senderAddress] = new TableEntry(port);
                ports[port]++;
                if (targetAddress != 255) {
                    broadcoast(table[senderAddress].getPort());
                } else {
                    System.out.print("Broadcast: ");
                    broadcoast(table[senderAddress].getPort());
                }
            }
            // Fall 2.2: Zieladresse besitzt einen Eintrag in der Switch-Tabelle
            else {
                // Fall 2.2.1: Zieladresse und Eingangsadresse sind identisch
                if (senderAddress == targetAddress) {
                    // Fall 2.2.1.1: Eingangsport ist identisch zum Zielport
                    if (port == table[targetAddress].getPort()) {
                        table[senderAddress] = new TableEntry(port);
                        ports[port]++;
                        System.out.println("Frame wird gefiltert und verworfen.");
                    }
                    // Fall 2.2.1.2: Eingangsport unterscheidet sich vom Zielport
                    else {
                        table[senderAddress] = new TableEntry(port);
                        ports[port]++;
                        ports[table[targetAddress].getPort()]++;
                        System.out.println("Ausgabe auf Port " + table[targetAddress].getPort() + ".");
                    }
                }
                // Fall 2.2.2: Eingangs- und Zieladresse unterscheiden sich
                else {
                    // Fall 2.2.2.1: Eingangsport ist identisch zum Zielport
                    if (port == table[targetAddress].getPort()) {
                        table[senderAddress] = new TableEntry(port);
                        ports[port]++;
                        System.out.println("Frame wird gefiltert und verworfen.");
                    }
                    // Fall 2.2.2.2: Eingangsport unterscheidet sich vom Zielport
                    else {
                        table[senderAddress] = new TableEntry(port);
                        ports[port]++;
                        ports[table[targetAddress].getPort()]++;
                        System.out.println("Ausgabe auf Port " + table[targetAddress].getPort() + ".");
                    }
                }
            }
        }

    }

    /**
     * Überprüft, ob ein Array von Objekten leer ist.
     *
     * @param list zu überprüfendes Array
     * @return TRUE wenn leer, sonst FALSE
     */
    private static boolean isListEmpty(TableEntry[] list) {
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gibt die aktuelle Switch-Tabelle aus, wenn diese nicht leer ist.
     *
     * @param listForTable aktuelle Switch-Tabelle
     */
    private static void printTable(TableEntry[] listForTable) {
        if (!isListEmpty(listForTable)) {
            System.out.format("%s%5s%5s\n", "Adresse", "Port", "Zeit");
            for (int i = 1; i < table.length; i++) {
                if (table[i] != null) {
                    System.out.format("%7s%5s%9s\n", i, table[i].getPort(), table[i].getTimeStamp());
                }
            }
        } else {
            System.out.println("\nDie Switch-Tabelle ist leer.\n");
        }
    }

    /**
     * Löscht alle Einträge, die älter als gewünscht sind.
     *
     * @param time Minuten bzw. Sekundenanzahl, ab wann Einträge als zu alt gelten
     * @param unit min oder s, gibt an ob time in Minuten oder Sekunden angegeben ist
     */
    public static void del(int time, String unit) {
        LocalTime actual_timeStamp = java.time.LocalTime.now();
        LocalTime oldestStamp = actual_timeStamp;
        if (unit.equals("min")) {
            System.out.println("minutes");
            oldestStamp = actual_timeStamp.minusMinutes(time);
        } else if (unit.equals("s")) {
            System.out.println("seconds");
            oldestStamp = actual_timeStamp.minusSeconds(time);
        }
        if (!oldestStamp.equals(actual_timeStamp)) {
            System.out.println(oldestStamp.equals(actual_timeStamp));
            System.out.println("delete loop");
            System.out.println("Actual Time: " + actual_timeStamp.toString());
            System.out.println("Delete limit: " + oldestStamp.toString());
            for (int i = 0; i < table.length; i++) {
                // Löschen aller vorhandenen Einträge, die älter als gewünscht sind
                if (table[i] != null && oldestStamp.isBefore(table[i].getCleanTime())) {
                    System.out.println("deleted: " + table[i].getCleanTime().toString());
                    table[i] = null;
                }
            }
        }

    }

    /**
     * Ausgabe der Nuzungsstatistik der einzelnen Ports des Switchs
     */
    private static void printStats() {
        System.out.format("%s%7s\n", "Port", "Frames");
        for (int i = 1; i < ports.length; i++) {
            System.out.format("%4s%7s\n", i, ports[i]);
        }
    }
}

/**
 * Die Klasse verwaltet die Einträge für die verschiedenen Adressen in der Switch-Tabelle.
 */
class TableEntry {
    private int port;
    private LocalTime timeStamp;
    private DateTimeFormatter hhmmss = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Konstruktor, Zeitpunkt der Erstellung des Eintrags wird automatisch gesetzt
     *
     * @param port von dem aus ein Frame weitergeleitet werden soll
     */
    public TableEntry(int port) {
        this.port = port;
        this.timeStamp = java.time.LocalTime.now();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public LocalTime getCleanTime() {
        return timeStamp;
    }

    /**
     * Gibt den Erstellungszeitpunkt des Tabelleneintrags formatiert zurück
     *
     * @return Zeit formatiert als HH:mm:ss
     */
    public String getTimeStamp() {
        return timeStamp.format(hhmmss);
    }
}
