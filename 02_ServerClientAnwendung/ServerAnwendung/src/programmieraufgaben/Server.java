package programmieraufgaben;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

/**
 * Die Server-Klasse enthält alle Methoden zum Erstellen, Verwenden und Schließen des Servers.
 */
public class Server {
    private int port;
    private ServerSocket listen;
    private ServerServices handler = new ServerServices();
    private Socket connectionSocket;


    /**
     * Diese Methode beinhaltet die gesamte Ausführung (Verbindungsaufbau und Beantwortung
     * der Client-Anfragen) des Servers.
     */
    public void execute() {
        try {
            listen = new ServerSocket(port);
            do {
                try {
                    connectionSocket = listen.accept();
                    PrintWriter writer = new PrintWriter(connectionSocket.getOutputStream(), true);
                    BufferedReader input = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream(), StandardCharsets.UTF_8));
                    String line;
                    while ((line = input.readLine()) != null) {
                        writer.println(handler.handleRequest(line));
                    }
                } catch (SocketException e) {
                    System.out.println(System.lineSeparator() + "Die Verbindung wurde unterbrochen.");
                } catch (Exception e) {
                    System.out.println(System.lineSeparator() + "Oops, uns ist folgender Fehler unterlaufen:" + System.lineSeparator());
                    e.printStackTrace();
                } finally {
                    handler.resetList(); // Leert die History
                    //if (connectionSocket != null) {
                        connectionSocket.close();
                        //connectionSocket = null;
                    //}
                }
            } while (true);
        } catch (SocketException e) {
            System.out.println(System.lineSeparator() + "Die Verbindung zum Client wurde unterbrochen.");
            execute(); // Ist die Verbindung zu einem Client beendet worden, wartet der Server auf den nächsten Client
        } catch (Exception e) {
            e.printStackTrace();
            if (!listen.isClosed()) {
                e.printStackTrace();
            }
        } finally {
            handler.resetList(); // Leert die History
        }

    }

    /**
     * Hier werden die Verbindung und alle Streams geschlossen.
     */
    public void disconnect() {
        try {
            listen.close();
            System.exit(0);
        } catch (Exception e) {

            if (!listen.isClosed()) {
                System.out.println("Ein Fehler ist aufgetreten.");
                e.printStackTrace();
            }
        }

    }

    /**
     * Überprüfung der Portnummer und Speicherung dieser im Attribut "port"
     *
     * @param port Portnummer als String
     * @return Portnummer ist akzeptabel TRUE sonst FALSE
     */
    public boolean checkPort(String port) {
        int tmpPort = 0;
        try {
            tmpPort = Integer.parseInt(port); // Überprüfung, ob ein Integer als Port eingegeben wurde
        } catch (NumberFormatException e) {
            System.out.println("Kein korrekter Port! Aktuell ist nur Port 2020 möglich.");
            System.exit(1);
        }
        try {
            if (tmpPort != 2020) { // Überprüfung, ob der einzig zulässige Port 2020 gewählt wurde
                throw new IllegalArgumentException();
            }
            ServerSocket serverSocket = new ServerSocket(tmpPort); // Nach korrekter Portwahl wird der Server gestartet
            this.port = tmpPort;
            serverSocket.close();
            return true;
        } catch (IOException e) {
            System.out.println("Fehler beim Verbindungsaufbau! Es konnte keine TCP-Verbindung zum Server mit\n" +
                    "IP-Adresse localhost (Port: " + port + ") hergestellt werden.");
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("Kein korrekter Port! Aktuell ist nur Port 2020 möglich.");
        } catch (Exception e) {
            System.out.println("Unerwartete Fehler aufgetaucht:");
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Gibt die akzeptierte und gespeicherte Portnummer zurück
     *
     * @return die akzeptierte und gespeicherte Portnummer
     */
    public int getPort() {
        return port;
    }

}
