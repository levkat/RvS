package programmieraufgaben;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

/**
 * Die Server-Klasse enthält alle Methoden zum Erstellen, Verwenden und Schließen des Servers.
 *
 * Für die Lösung der Aufgabe müssen die Methoden execute, disconnect
 * und checkPort befüllt werden.
 * Es dürfen beliebig viele Methoden und Klassen erzeugt werden, solange
 * die von den oben genannten Methoden aufgerufen werden.
 */
public class Server{
    private int port;
    private ServerSocket listen;
    private ServerServices handler = new ServerServices();
    private Socket connectionSocket;


    /**
     * Diese Methode beinhaltet die gesamte Ausführung (Verbindungsaufbau und Beantwortung
     * der Client-Anfragen) des Servers.
     */
    public void execute(){
        try{
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
                }
                catch (SocketException e){
                    System.out.println(System.lineSeparator() + "Die Verbindung wurde abgebrochen");
                }
                catch (Exception e){
                    System.out.println(System.lineSeparator() + "Oops, uns ist folgende Fehler unterlaufen:" + System.lineSeparator());
                    e.printStackTrace();
                }
                finally {
                    handler.resetList();
                    connectionSocket.close();
                }
            }while (true);
        }
        catch (SocketException e){
                System.out.println(System.lineSeparator() + "Die Verbindung vom Client ist abgebrochen");
                execute();
        }
        catch (Exception e){
            e.printStackTrace();
            if (!listen.isClosed()){
                e.printStackTrace();
            }
        }
        finally {
            handler.resetList();
        }

    }

    /**
     * Hier soll die Verbindung und alle Streams geschlossen werden.
     */
    public void disconnect() {
        try {
            listen.close();
            System.exit(0);
        }
        catch (Exception e){

            if (!listen.isClosed()){
                System.out.println("Ein Fehler ist aufgetreten");
                e.printStackTrace();
            }
        }

    }

    /**
     * Überprüfung der Port-Nummer und Speicherung dieser in die Klassen-Variable "port"
     * @param port Portnummer als String
     * @return Port-Nummer ist akzeptabel TRUE oder nicht FALSE
     */
    public boolean checkPort(String port) {
        int tmpPort = 0;
        try{
            tmpPort = Integer.parseInt(port);
        }
        catch (NumberFormatException e){
            System.out.println("Kein korrekter Port! Aktuell ist nur Port 2020 möglich.");
            System.exit(1);
        }
        try{
            if(tmpPort != 2020){
                throw new IllegalArgumentException();
            }
            ServerSocket serverSocket = new ServerSocket(tmpPort);
            this.port = tmpPort;
            serverSocket.close();
            return true;
        } catch (IOException e){
            System.out.println("Fehler beim Verbindungsaufbau! Es konnte keine TCP-Verbindung zum Server mit\n" +
                    "IP-Adresse localhost (Port: " + port + ") hergestellt werden.");
            return false;
        }
        catch (IllegalArgumentException e){
            System.out.println("Kein korrekter Port! Aktuell ist nur Port 2020 möglich.");
        }
        catch (Exception e){
            System.out.println("Unerwartete Fehler aufgetaucht:");
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Gibt die akzeptierte und gespeicherte Port-Nummer zurück
     * @return Gibt die akzeptierte und gespeicherte Port-Nummer zurück
     */
    public int getPort() {
        return port;
    }

}
