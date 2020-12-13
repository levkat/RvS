package programmieraufgaben;

import java.util.Scanner;

/**
 * Dies ist die Start-Klasse.
 * Änderungen an dieser Klassen sind NICHT gestattet!
 *
 * @author RvS Tutorenteam
 */
public class Main {
    private static Scanner input = new Scanner(System.in);
    private static Server server = new Server();
    private static Thread answerThread;

    public static void main(String[] args) {

        System.out.println("An welchem Port soll der Server gestartet werden?");
        System.out.print("Port: ");
        String port = input.nextLine();

        if(server.checkPort(port)){
            System.out.println("Der Server mit den Diensten:");
            System.out.println(" * Zeit und Datum");
            System.out.println(" * Rechner");
            System.out.println(" * Echo und Discard");
            System.out.println(" * Ping-Pong");
            System.out.println("wurde gestartet und wartet am Port " + server.getPort() + " auf Anfragen vom Client.");
            System.out.println("");

            //Startet den AnswertThread und wartet auf Eingabe zum Beenden des Servers.
            //Bei korrekter Eingabe wird die disconnect-Methode des Servers aufgerufen.
            startAnswertThread();

            //Starte den Ablauf des Servers
            server.execute();
        }

        System.out.println("Der Server wurde beendet.");
    }

    /**
     * Startet einen Thread der die Komandozeilenabfrage für das Beenden des Servers ausgibt.
     */
    private static void startAnswertThread() {
        answerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                char answer;
                do{
                    System.out.print("Wenn Sie den Server beenden wollen, dann geben Sie \"J\" ein: ");
                    answer =  input.next().charAt(0);
                }
                while (answer != 'J');
                server.disconnect();
            }
        });
        answerThread.start();
    }
}
