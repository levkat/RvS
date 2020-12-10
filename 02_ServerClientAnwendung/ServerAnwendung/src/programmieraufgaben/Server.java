package programmieraufgaben;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

import static programmieraufgaben.ServerServices.handleRequest;

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
    private Socket connectionSocket;
    private ServerSocket listen;
    private PrintWriter writer;
    private BufferedReader input;
    private boolean run = true;
    private Logger log;

    /**
     * Diese Methode beinhaltet die gesamte Ausführung (Verbindungsaufbau und Beantwortung
     * der Client-Anfragen) des Servers.
     */
    public void execute(){
        try{
            listen = new ServerSocket(port);
            connectionSocket = listen.accept();
            writer = new PrintWriter(connectionSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream(),"UTF8"));
            String line;
            while ((line = input.readLine())!= null){
                 //line = input.readLine();
                 writer.println(handleRequest(line));
            }
        }
        catch (Exception e){
            if (!listen.isClosed()){
                e.printStackTrace();
            }
        }

    }

    /**
     * Hier soll die Verbindung und alle Streams geschlossen werden.
     */
    public void disconnect() {
        try {
            listen.close();
            input.close();
            writer.close();
            connectionSocket.close();
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
                    "IP-Adresse localhost (Port: " + port + ") hergestellt werden."); //TODO right message
            return false;
        }
        catch (SecurityException e){
            System.out.println("Security Exception"); //TODO right message
        }
        catch (IllegalArgumentException e){
            System.out.println("Kein korrekter Port! Aktuell ist nur Port 2020 möglich.");
        }
        return false;
    }

    /**
     * Gibt die akzeptierte und gespeicherte Port-Nummer zurück
     * @return
     */
    public int getPort() {
        return port;
    }

}
