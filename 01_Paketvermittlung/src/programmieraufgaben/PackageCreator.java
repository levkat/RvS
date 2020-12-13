package programmieraufgaben;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static programmieraufgaben.MessageSplitter.splitString;

public class PackageCreator {

    /**
     *
     *
     * @param dataPackage Hier wird das Objekt übergeben in das die abgefragten Werte gespeichert werden sollen
     * @return Gibt das als Parameter übergebene Objekt, dass mit den abgefragten Werten befüllt wurde zurück
     */
    public DataPackage fillParameters(DataPackage dataPackage) {
        String senderAddress;
        String receiverAddress;
        Scanner input = new Scanner(System.in);
        try{
            // Eingaben vom User
            System.out.print("Version: ");
            int ipVersion = input.nextInt();
            checkIPversion(ipVersion); // Überprüfung, ob die eingegebene IP-Version 4 oder 6 ist
            dataPackage.setVersion(ipVersion);

            System.out.print("Absender: ");
            senderAddress = input.next();
            dataPackage.setSenderAddress(senderAddress);

            System.out.print("Empfänger: ");
            receiverAddress = input.next();
            dataPackage.setReceiverAddress(receiverAddress);

            System.out.println("Nachricht:");
            StringBuilder stringBuilder = new StringBuilder();
            String check = input.nextLine();
            stringBuilder.append(check);

            // Eine mehrzeilige Eingabe des Users wird ermöglicht. Beendet wird diese durch die Eingabe von \n.\n
            while (true){
                check = input.nextLine();
                if(check.equals(".")){
                    // Entfernen des Erkennungsmerkmals des Endes der Nachricht, da dieses nicht zur Nutzereingabe gehört
                    stringBuilder.deleteCharAt(stringBuilder.length()-1);
                    stringBuilder.deleteCharAt(stringBuilder.length()-1);
                    break;
                }
                stringBuilder.append(check);
                    if (input.hasNextLine()) {
                        // Falls ein Zeilenumbruch in der Eingabe des Nutzers ist, wird diese als \n in dem String gespeichert
                        stringBuilder.append("\\n");
                    }
                }

            dataPackage.setMessage(stringBuilder.toString());
            dataPackage.printAll(); // Alle Nutzereingaben werden ausgegeben

            return dataPackage;
        }
        catch(InputMismatchException in){
            System.out.println("Die Eingabe ist nicht gültig! \nBitte geben sie die IP Version als Zahl ein, gültige Versionen derzeit sind: 4/6");
            in.toString();
            return null;
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Aus dem als Parameter übergebenen Paket sollen die Informationen
     * ausgelesen und in einzelne Datenpakete aufgeteilt werden
     *
     * @param dataPackage Hier wird das Objekt übergeben in das das Resultat gespeichert werden soll
     * @return Gibt das als Parameter übergebene Objekt mit den aufgeteilten Datenpaketen zurück
     */
    public List<DataPackage> splitPackage(DataPackage dataPackage) {
        if (dataPackage == null){
            return null;
        }
        List<DataPackage> dataPackages = new LinkedList<>();
        int sequence = 1;
        try {
            List<String> splitted = splitString(dataPackage.getMessage(), dataPackage.getDataPackageLength());
            String lastWord = "";
            /*
             * Die Schleife geht die Liste splitted mit den einzelnen Wörtern der vom Nutzer eingegebenen Nachricht
             * durch und fügt mehrere Wörter zusammen in ein Paket ein, solange dadurch die maximale Paketlänge nicht
             * überschritten wird.
             */
            for (String s : splitted) {
                if (s.length() + lastWord.length() <= dataPackage.getDataPackageLength()) {
                    lastWord += s;
                } else {
                    if (lastWord.endsWith(" ")) { // endet ein Paket mit einem Leerzeichen, wird dieses entfernt
                        String tmp = lastWord;
                        tmp = tmp.substring(0, tmp.length() - 1);
                        dataPackages.add(new DataPackage(dataPackage.getDataPackageLength(), dataPackage.getVersion(), dataPackage.getSenderAddress(), dataPackage.getReceiverAddress(), tmp, sequence));
                    } else {
                        dataPackages.add(new DataPackage(dataPackage.getDataPackageLength(), dataPackage.getVersion(), dataPackage.getSenderAddress(), dataPackage.getReceiverAddress(), lastWord, sequence));
                    }
                    if (!s.equals(" ")) {
                        // ist das erste Zeichen für ein neues Paket ein Leerzeichen, wird dieses übersprungen
                        lastWord = s;
                    } else {
                        lastWord = "";
                    }
                    sequence++;
                }
            }
            if (!lastWord.isEmpty()) {
                // Letztes noch nicht in Paketen untergebrachtes Wort wird als einzelnes Paket versandt
                dataPackages.add(new DataPackage(dataPackage.getDataPackageLength(), dataPackage.getVersion(), dataPackage.getSenderAddress(), dataPackage.getReceiverAddress(), lastWord, sequence));
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        return dataPackages;
    }

    /**
     * Diese Methode gibt den Inhalt der empfangenen Pakete in der Kommandozeile aus
     *
     * @param dataPackages Hier wird die Liste übergeben, deren Elemente in die Kommandozeile ausgegeben werden sollen
     */
    public void printOutPackage(List<DataPackage> dataPackages) {
        if (dataPackages == null) {
            return;
        }
        System.out.println(System.lineSeparator() + "Es sind " + dataPackages.size() + " Datenpakete notwendig." + System.lineSeparator());
        for (DataPackage dataPackage : dataPackages) {
            dataPackage.printMessage();
        }
    }

    /**
     * Die Methode überprüft die Gültigkeit der vom Nutzer eingegebenen IP-Version
     * @param ipVersion
     * @return boolean
     */
    public static boolean checkIPversion(int ipVersion) {
        if(ipVersion == 4 || ipVersion == 6){
            return true;
        }
        else
            throw new IllegalArgumentException("Die von Ihnen eingegebene IP Version: IPv" + ipVersion + " ist nicht gültig! \nBitte geben sie die IP Version als Zahl ein, gültige Versionen derzeit sind: 4/6");
    }

}
