package programmieraufgaben;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Die Server-Klasse enthält alle Methoden zum Erstellen, Verwenden und Schließen des Servers.
 *
 * Für die Lösung der Aufgabe müssen die Methoden connect, disconnect,
 * request und extract befüllt werden.
 * Es dürfen beliebig viele Methoden und Klassen erzeugt werden, solange
 * die von den oben genannten Methoden aufgerufen werden.
 */
public class Client {
    //Diese Variable gibt den Socket an an dem die Verbindung aufgabaut werden soll
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
            if (!target.toLowerCase().matches("127.0.0.1|localhost")){
                throw new UnknownHostException("Falsche IP-Adresse! Aktuell ist nur die IPv4-Adresse 127.0.0.1 und die Eingabe localhost möglich.");
            }
            System.out.print("Port: ");
            targetPort = input.nextInt();
            if (!String.valueOf(targetPort).equals("2020")){
                throw new PortUnreachableException("Kein korrekter Port! Aktuell ist nur Port 2020 möglich." + System.lineSeparator());
            }
            clientSocket = new Socket(target,targetPort);
            System.out.println("Eine TCP-Verbindung zum Server mit IP-Adresse " + target +" (Port: "+ targetPort +") wurde hergestellt. Sie können nun Ihre Anfragen an den Server stellen.");
        }
        catch (InputMismatchException e){
            System.out.println("Kein korrekter Port! Aktuell ist nur Port 2020 möglich." + System.lineSeparator());
        }
        catch (ConnectException e){
            System.out.println("Fehler beim Verbindungsaufbau! Es konnte keine TCP-Verbindung zum Server mit IP-Adresse " + target +" (Port: " + targetPort +") hergestellt werden.");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Hier soll die Verbindung und alle Streams geschlossen werden.
     */
    public void disconnect() {
        try{
            clientSocket.close();
            System.out.println("Die Verbindung zum Server wurde beendet." + System.lineSeparator());
        }
        catch (Exception e){
            if (isConnected()){
                System.out.println("Ein Fehler ist aufgetreten");
                e.printStackTrace();
            }
        }

    }

    /**
     * In dieser Methode sollen die Eingaben des Benutzers an den Server gesendet und die Antwort empfangen werden
     * @param userInput Eingabe des Benutzers
     * @return Die vom Server empfangene Nachricht
     */
    public String request(String userInput) {
        String response = "";
        try{
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

                out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(userInput);
                out.flush();
                response = in.readLine();
        }
        catch (SocketException e){
            System.out.println("Die Verbindung wurde vom Server abgebrochen");
            System.exit(0);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Die vom Server empfangene Nachricht soll hier für die Konsolenausgabe aufbereitet werden.
     * @param reply Die vom Server empfangene Nachricht
     * @return Ausgabe für die Konsole
     */
    public String extract(String reply) {
        return reply.replace("\\n","\n");
    }

    /**
     * Gibt den Status der Verbindung an
     * @return Wenn die Verbindung aufgebaut ist: TRUE sonst FALSE
     */
    public boolean isConnected() {
        return (clientSocket != null && clientSocket.isConnected() && !clientSocket.isClosed());
    }
}
