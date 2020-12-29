package programmieraufgaben;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Die Client-Klasse enthält alle Methoden zum Erstellen, Verwenden und Schließen des Clients.
 */
public class Client {
    private final static String WRONG_PORT = "Kein korrekter Port! Aktuell ist nur Port 2020 möglich.";
    // Diese Variable gibt den Socket an, an dem die Verbindung aufgebaut werden soll
    private Socket clientSocket;
    private PrintWriter out;
    private int targetPort;
    private String target;
    private BufferedReader in;

    /**
     * Hier werden die Verbindungsinformationen abgefragt und eine Verbindung eingerichtet.
     */
    public void connect() {
        Scanner input = new Scanner(System.in);
        try {
            System.out.print("IP-Adresse: ");
            target = input.nextLine();
            if (!target.matches("127.0.0.1|localhost")) { // Überprüft, ob der User eine zulässige IP-Adresse eingegeben hat
                throw new UnknownHostException();
            }
            System.out.print("Port: ");
            targetPort = Integer.parseInt(input.nextLine());
            //targetPort = input.nextInt();
            if (targetPort != 2020) { // Überprüft, ob der User als Port 2020 gewählt hat
                throw new InputMismatchException();
            }
            clientSocket = new Socket(target, targetPort); // Nach korrekten Eingaben seitens des Users wird eine Verbindung zum Server aufgebaut
            System.out.println("Eine TCP-Verbindung zum Server mit IP-Adresse " + target + " (Port: " + targetPort
                    + ") wurde hergestellt. Sie können nun Ihre Anfragen an den Server stellen.");

        } catch (InputMismatchException e) { // fängt Porteingaben, die nicht 2020 entsprechen, ab
            System.out.println(WRONG_PORT);
        } catch (UnknownHostException e) { // fängt unzulässige IP-Adressen-Eingaben ab
            System.out.println
                    ("Falsche IP-Adresse! Aktuell ist nur die IPv4-Adresse 127.0.0.1 und die Eingabe localhost möglich.");
        } catch (ConnectException e) { // fängt Fehler beim Verbindungsaufbau zum Server ab
            System.out.println("Fehler beim Verbindungsaufbau! Es konnte keine TCP-Verbindung zum Server mit IP-Adresse "
                    + target + " (Port: " + targetPort + ") hergestellt werden.");
        } catch (NumberFormatException e) { // fängt Porteingaben, die kein Integer sind, ab
            System.out.println(WRONG_PORT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Hier werden die Verbindung und alle Streams geschlossen.
     */
    public void disconnect() {
        try {
            if (clientSocket != null) {
                clientSocket.close();
                System.out.println("Die Verbindung zum Server wurde beendet." + System.lineSeparator());
            } else {
                System.out.println("Verbindung bereits beendet.");
            }
        } catch (IOException e) {
            System.out.println("Ein Fehler ist aufgetreten, ClientSocket konnte nicht beendet werden.");
            e.printStackTrace();
        }
    }

    /**
     * In dieser Methode werden die Eingaben des Benutzers an den Server gesendet und die Antwort empfangen
     * <p>
     * testerSocket() prüft bei jedem Request ob der Server noch da ist(indem er sich mit ihm verbindet), da Socket
     * "autocloseable" ist, wird er nach dem try- Block automatisch geschlossen.
     *
     * @param userInput Eingabe des Benutzers
     * @return Die vom Server gesendete Nachricht
     */
    public String request(String userInput) {
        try (Socket testerSocket = new Socket(target, targetPort)) { // try-with resource, da Socket autocloseable
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(userInput);
            return in.readLine();
        } catch (ConnectException e) {
            System.out.println("Die Verbindung wurde vom Server unterbrochen");
            try {
                if (clientSocket != null) { // Bei einer unterbrochenen Verbindung zum Server wird der Client beendet
                    clientSocket.close();
                }
            } catch (Exception ignored) {
            }
        } catch (IOException e) {
            System.out.println("Fehler in I/O Streams");
            disconnect();
        } catch (Exception e) {
            disconnect();
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Die vom Server empfangene Nachricht wird hier für die Konsolenausgaben aufbereitet. Das heißt, es werden nur die nötigen Informationen extrahiert.
     * Es werden also bei den Server Antworten, die ein Signalwort wie "DATE" oder "TIME" beinhalten, dieses Signalwort entfernt. Davon ausgenommen ist die
     * Antwort "PONG", da diese ein solches Wort nicht enthält.
     *
     * @param reply Die vom Server empfangene Nachricht
     * @return Ausgabe für die Konsole
     */
    public String extract(String reply) {
        if (!reply.startsWith("PONG") && !reply.isEmpty()) {
            if (!reply.equals("")) {
                return reply.replaceFirst("\\w+\\s", "").replace("\\n", "\n")
                        + System.lineSeparator();
            } else {
                return reply;
            }
        } else if (reply.isEmpty()) {
            return "";

        } else {
            return reply + System.lineSeparator();
        }
    }

    /**
     * Gibt den Status der Verbindung an
     *
     * @return Wenn die Verbindung aufgebaut ist: TRUE sonst FALSE
     */
    public boolean isConnected() {
        return (clientSocket != null && clientSocket.isConnected() && !clientSocket.isClosed());
    }
}
