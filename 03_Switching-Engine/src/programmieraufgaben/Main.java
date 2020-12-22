package programmieraufgaben;

import java.util.Scanner;

/**
 * Dies ist die Start-Klasse.
 * Änderungen an dieser Klassen sind NICHT gestattet!
 *
 * @author RvS Tutorenteam
 */
public class Main {

    public static void main(String[] args) {

        //Die Scanner-Klasse hilft beim Einlesen des Eingabe-Streams
        Scanner input = new Scanner(System.in);

        //In dieser Klasse befinden sich Methoden um den Switch zu erstellen und die Eingaben abzuarbeiten
        SwitchingEngine engine = new SwitchingEngine();

        System.out.print("Anzahl Ports: ");

        int portNumber;
        if(input.hasNextInt()){
            portNumber = Integer.parseInt(input.nextLine());

            boolean switchCreated = engine.createSwitch(portNumber);

            if(switchCreated){

                boolean abort = false;

                do {
                    System.out.print("$ ");
                    String command;

                    command = input.nextLine();

                    abort = engine.handleCommand(command);
                }
                while (!abort);
            }
        }
        else{
            System.out.println("Falsche Eingabe: "+input.next()+" ist keine Natürliche Zahl!\n");
        }

        System.out.println("Das Programm wird beendet.");
    }
}
