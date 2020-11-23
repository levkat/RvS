package programmieraufgaben;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static programmieraufgaben.MessageSplitter.splitString;
import static programmieraufgaben.ipValidator.checkIPversion;
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
        String senderAddress;
        String reciverAddress;
        Scanner input = new Scanner(System.in);
        try{
        System.out.println("Bitte wählen Sie die IP Version aus(IPv4/IPv6):");
        int ipVersion = Integer.parseInt(String.valueOf(input.nextInt()));
        checkIPversion(ipVersion);
        System.out.println(ipVersion);
            dataPackage.setVersion(ipVersion);
            System.out.println("Bitte geben sie die Adresse des Senders ein:");
            senderAddress = input.next();
            System.out.println(senderAddress);
            dataPackage.setSenderAddress(senderAddress);
            System.out.println("Bitte geben sie die Adresse des Empfängers ein:");
            reciverAddress = input.next();
            dataPackage.setReciverAddress(reciverAddress);
            System.out.println("Bitte geben Sie Ihre Nachricht ein:");
            StringBuilder stringBuilder = new StringBuilder();
            String check = input.nextLine();
            stringBuilder.append(check);
            String [] parts;
            boolean hasRun = false;
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
        } catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        dataPackage.printAll();
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
        int sequence = 1;
        //dataPackages.add(dataPackage); //TODO add spliting mechanism
        List<String> splitted = splitString(dataPackage.getMessage(),dataPackage.getDataPackageLength());
        String lastWord = "";
        for (int i = 0; i < splitted.size(); i++) {
            if (splitted.get(i).length() + lastWord.length() <= dataPackage.getDataPackageLength()) {
                lastWord += splitted.get(i);
            }
            else
            {
                if(lastWord.endsWith(" ")){
                    String tmp = lastWord;
                    tmp = tmp.substring(0,tmp.length()-1);
                    dataPackages.add(new DataPackage(dataPackage.getDataPackageLength(), dataPackage.getVersion(), dataPackage.getsenderAddress(), dataPackage.getReciverAddress(), tmp, sequence));
                }
                else {
                    System.out.println("---------");
                    System.out.println(lastWord);
                    dataPackages.add(new DataPackage(dataPackage.getDataPackageLength(), dataPackage.getVersion(), dataPackage.getsenderAddress(), dataPackage.getReciverAddress(), lastWord, sequence));
                }
                if (!splitted.get(i).equals(" ")) {
                    lastWord = splitted.get(i);
                }else{
                    lastWord = "";
                }
                sequence++;
            }
            //System.out.println(s); für Testzwecke
        }
        if(!lastWord.isEmpty()){
            dataPackages.add(new DataPackage(dataPackage.getDataPackageLength(), dataPackage.getVersion(), dataPackage.getsenderAddress(), dataPackage.getReciverAddress(), lastWord, sequence));
        }
        return dataPackages;
    }

    /**
     * Diese Methode gibt den Inhalt der empfangenen Pakete in der Komandozeile aus
     *
     * @param dataPackages Hier wird die Liste übergeben, deren Elemente in die Kommandozeile ausgegeben werden sollen
     */
    public void printOutPackage(List<DataPackage> dataPackages) {
        System.out.println(System.lineSeparator() + "Es sind " + dataPackages.size() + " Datenpakete notwendig." + System.lineSeparator());
        for (DataPackage dataPackage : dataPackages) {
            dataPackage.printMessage();
        }
    }

}
