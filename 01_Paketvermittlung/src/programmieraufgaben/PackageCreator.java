package programmieraufgaben;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static programmieraufgaben.ipValidator.checkIp;

public class PackageCreator {

    /**
     * Hier sollen die Kommandozeilen-Abfragen abgefragt und die Antworten
     * gespeichert werden
     * Es sollte auf Fehlerbehandlung geachtet werden (falsche Eingaben, ...)
     *
     * @param dataPackage Hier wird das Objekt übergeben in das die abgefragten Werte gespeichert werden sollen
     * @return Gibt das als Parameter übergebene Objekt, dass mit den abgefragten Werten befüllt wurde zurück
     */
    public DataPackage fillParameters(DataPackage dataPackage) {
        String eingabe;
        int validateIPversion;
        String senderAddress;
        String reciverAddress;
        Scanner input = new Scanner(System.in);
        System.out.println("Bitte wählen Sie die IP Version aus(IPv4/IPv6):");
        int validatedInt = Integer.parseInt(String.valueOf(input.nextInt()));
        System.out.println(validatedInt);
        if (validatedInt == 4 || validatedInt == 6) {
            dataPackage.setVersion(validatedInt);
            System.out.println("Bitte geben sie die Adresse des Senders ein:");
            senderAddress = input.next();
            System.out.println(senderAddress);
            if(checkIp(validatedInt, senderAddress)) {
               dataPackage.setSenderAddress(senderAddress);
            }
            System.out.println("Bitte geben sie die Adresse des Empfängers ein:");
            reciverAddress = input.next();
            if(checkIp(validatedInt, reciverAddress)){
                dataPackage.setReciverAddress(reciverAddress);
            }
        }
        else
        {
            throw new IllegalArgumentException("Keine IPv"+validatedInt+". Unterstützt sind nur folgende IPv: 4 / 6");
        }

        return dataPackage;
    }//inetAddress.getHostAddress());

    /**
     * Aus dem als Parameter übergebenen Paket sollen die Informationen
     * ausgelesen und in einzelne Datenpakete aufgeteilt werden
     *
     * @param dataPackage Hier wird das Objekt übergeben in das das Resultat gespeichert werden soll
     * @return Gibt das als Parameter übergebene Objekt mit den aufgeteiltet Datenpaketen zurück
     */
    public List<DataPackage> splitPackage(DataPackage dataPackage) {
        List<DataPackage> dataPackages = new LinkedList<>();
        dataPackages.add(dataPackage); //TODO add spliting mechanism
        return dataPackages;
    }

    /**
     * Diese Methode gibt den Inhalt der empfangenen Pakete in der Komandozeile aus
     *
     * @param dataPackages Hier wird die Liste übergeben, deren Elemente in die Kommandozeile ausgegeben werden sollen
     */
    public void printOutPackage(List<DataPackage> dataPackages) {
        for (DataPackage dataPackage : dataPackages) {
            dataPackage.printAll();
        }
    }

}
