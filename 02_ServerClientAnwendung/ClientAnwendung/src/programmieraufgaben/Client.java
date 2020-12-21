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
 *
 * Für die Lösung der Aufgabe müssen die Methoden connect, disconnect,
 * request und extract befüllt werden.
 * Es dürfen beliebig viele Methoden und Klassen erzeugt werden, solange
 * die von den oben genannten Methoden aufgerufen werden.
 */
public class Client {
    private final static String WRONG_PORT = "Kein korrekter Port! Aktuell ist nur Port 2020 möglich.";
    //Diese Variable gibt den Socket an an dem die Verbindung aufgebaut werden soll
    private Socket clientSocket;
    private PrintWriter out;
    private int targetPort;
    private String target;
    BufferedReader in;

    /**
     * Hier werden die Verbindungsinformationen abgefragt und eine Verbindung eingerichtet.
     */
    public void connect() {
        Scanner input = new Scanner(System.in);
        try {
            System.out.print("IP-Adresse: ");
            target = input.nextLine();
            if (!target.matches("127.0.0.1|localhost")) {
                throw new UnknownHostException();
            }
            System.out.print("Port: ");
            targetPort = input.nextInt();
            if (targetPort != 2020) {
                throw new InputMismatchException();
            }
            clientSocket = new Socket(target, targetPort);
            System.out.println("Eine TCP-Verbindung zum Server mit IP-Adresse " + target + " (Port: " + targetPort
                    + ") wurde hergestellt. Sie können nun Ihre Anfragen an den Server stellen.");

        } catch (InputMismatchException e) { // checks if input.nextInt() ein int ist,
            System.out.println(WRONG_PORT);
        } catch (UnknownHostException e) { // check IP, von Socket geworfen
            System.out.println
                    ("Falsche IP-Adresse! Aktuell ist nur die IPv4-Adresse 127.0.0.1 und die Eingabe localhost möglich.");
        }catch (ConnectException e) {
            System.out.println("Fehler beim Verbindungsaufbau! Es konnte keine TCP-Verbindung zum Server mit IP-Adresse "
                    + target + " (Port: " + targetPort + ") hergestellt werden.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Hier soll die Verbindung und alle Streams geschlossen werden.
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
     * In dieser Methode sollen die Eingaben des Benutzers an den Server gesendet und die Antwort empfangen werden
     *
     * testerSocket() prüft bei jedem Request ob der Server noch da ist(indem er sich mit ihm verbindet), da Socket
     * "autocloseable" ist, wird er nach dem try- Block automatisch geschlossen.
     * @param userInput Eingabe des Benutzers
     * @return Die vom Server empfangene Nachricht
     */
    public String request(String userInput) {
        try (Socket testerSocket = new Socket(target, targetPort)) { // try-with resource, da Socket autocloseable
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(userInput);
            return in.readLine(); //TODO maybe return array? will help in extract
        } catch (ConnectException e) {
            System.out.println("Die Verbindung wurde vom Server abgebrochen");
                try { //FIXME Takes too long
                    if (clientSocket != null) {
                        clientSocket.close();
                    }
                } catch (Exception ignored) {
            }
        }catch (IOException e) {
            System.out.println("Fehler in I/O Streams");
            disconnect();
        } catch (Exception e) {
            disconnect();
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Die vom Server empfangene Nachricht soll hier für die Konsole-ausgaben aufbereitet werden.
     *
     * @param reply Die vom Server empfangene Nachricht
     * @return Ausgabe für die Konsole
     */
    public String extract(String reply) {
        //System.out.println(reply);
        if (!reply.startsWith("PONG") && !reply.isEmpty()) {
            if (!reply.equals("")) {
                return reply.replaceFirst("\\w+\\s", "").replace("\\n", "\n")
                        + System.lineSeparator();
            } else {
                return reply;
            }
        }
        else if (reply.isEmpty()){
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
