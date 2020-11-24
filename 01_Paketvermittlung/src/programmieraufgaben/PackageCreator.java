package programmieraufgaben;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static programmieraufgaben.MessageSplitter.splitString;

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
        String senderAddress;
        String receiverAddress;
        Scanner input = new Scanner(System.in);
        try{
        System.out.print("Version: ");
        int ipVersion = input.nextInt();
        checkIPversion(ipVersion);
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
            while (true){
                check = input.nextLine();
                if(check.equals(".")){
                    stringBuilder.deleteCharAt(stringBuilder.length()-1);
                    stringBuilder.deleteCharAt(stringBuilder.length()-1);
                    break;
                }
                stringBuilder.append(check);
                    if (input.hasNextLine()) {
                        stringBuilder.append("\\n");
                    }
                }
            dataPackage.setMessage(stringBuilder.toString());
            dataPackage.printAll();
            return dataPackage;
        }
        catch(InputMismatchException in){
            System.out.println("Die Eingabe ist nicht gültig! \nBitte geben sie die IP Version als Zahl ein, gültige Versionen derzeit sind: 4/6");
            in.toString();
            return null;
        }
        catch(Exception e) {
            System.out.println(e);
            return null;
        }
    }//inetAddress.getHostAddress());

    /**
     * Aus dem als Parameter übergebenen Paket sollen die Informationen
     * ausgelesen und in einzelne Datenpakete aufgeteilt werden
     *
     * @param dataPackage Hier wird das Objekt übergeben in das das Resultat gespeichert werden soll
     * @return Gibt das als Parameter übergebene Objekt mit den aufgeteiltet Datenpaketen zurück
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
            for (int i = 0; i < splitted.size(); i++) {
                if (splitted.get(i).length() + lastWord.length() <= dataPackage.getDataPackageLength()) {
                    lastWord += splitted.get(i);
                } else {
                    if (lastWord.endsWith(" ")) {
                        String tmp = lastWord;
                        tmp = tmp.substring(0, tmp.length() - 1);
                        dataPackages.add(new DataPackage(dataPackage.getDataPackageLength(), dataPackage.getVersion(), dataPackage.getSenderAddress(), dataPackage.getReceiverAddress(), tmp, sequence));
                    } else {
                        dataPackages.add(new DataPackage(dataPackage.getDataPackageLength(), dataPackage.getVersion(), dataPackage.getSenderAddress(), dataPackage.getReceiverAddress(), lastWord, sequence));
                    }
                    if (!splitted.get(i).equals(" ")) {
                        lastWord = splitted.get(i);
                    } else {
                        lastWord = "";
                    }
                    sequence++;
                }
            }
            if (!lastWord.isEmpty()) {
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
     * Diese Methode gibt den Inhalt der empfangenen Pakete in der Komandozeile aus
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
    public static boolean checkIPversion(int ipVersion) {
        if(ipVersion == 4 || ipVersion == 6){
            return true;
        }
        else
            throw new IllegalArgumentException("Die von Ihnen eingegebene IP Version: IPv" + ipVersion + " ist nicht gültig! \nBitte geben sie die IP Version als Zahl ein, gültige Versionen derzeit sind: 4/6");
    }

}
