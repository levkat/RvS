package programmieraufgaben;

import java.util.List;
import java.util.Scanner;

/**
 * Dies ist die Start-Klasse.
 * Änderungen an dieser Klassen sind NICHT gestattet!
 *
 * @author RvS Tutorenteam
 */
public class Main {

    public static void main(String[] args) {

        //maximale Datenteil-Länge
        int dataPackageLength;

        try {
            // wandle den String in Integer
            dataPackageLength = Integer.parseInt(args[0]);
        } catch (Exception e) {
            //Fehlermeldung ausgeben
            System.out.println("Datenteil-Länge konnte nicht gelesen werden!");
            System.out.println("Die Datenteil-Länge muss als Komandozeilenargument übergeben werden!");
            return;
        }

        //Die Scanner-Klasse hilft beim Einlesen des Eingabe-Streams
        Scanner input = new Scanner(System.in);

        //Gibt an ob die Ausführung der Schleife wiederholt werden soll
        char repeat;

        do {
            //Parameter des Datenpaketes: soll mit den Nutzereingaben und den Resultaten gefüllt werden
            DataPackage dataPackage = new DataPackage(dataPackageLength);

            //Komandozeilenausgabe der maximalen Datenteil-Länge
            System.out.println("Die maximale Datenteil-Länge ist "+dataPackage.getDataPackageLength()+".");

            //In dieser Klasse befinden sich alle Methoden um das Paket zu erstellen, zu bearbeiten und auszugeben
            PackageCreator packageCreator = new PackageCreator();

            //Eingaben werden in dieser Methode aus der Komandozeile eingelesen
            dataPackage = packageCreator.fillParameters(dataPackage);

            //Das Datenpaket wird in die geforderten Pakete aufgeteilt
            List<DataPackage> dataPackages = packageCreator.splitPackage(dataPackage);

            //Ausgabe der einzelnen Datenpakete
            packageCreator.printOutPackage(dataPackages);

            //Ausgabe des Strings
            System.out.print("Gib 'J' ein, um den Vorgang zu wiederholen: ");
            //Einlesen eines Zeichens
            repeat = input.next().charAt(0);
        }while (repeat == 'j' || repeat == 'J');
    }
}
