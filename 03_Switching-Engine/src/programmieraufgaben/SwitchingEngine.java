package programmieraufgaben;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Eine vereinfachte Switch Engine mit folgenden möglichen Kommandos:
 * frame <Eingangsportnummer> <Absenderadresse> <Zieladresse>,
 * table,
 * statistics,
 * del Xs bzw. del Xmin,
 * exit
 */
public class SwitchingEngine {
    private static final String WRONG = "Ungültige Eingabe!";
    private static int [] ports;
    private static ArrayList<Entity> table;

    /**
     * Diese Methode überprüft die Eingabe und erstellt die für den
     * weiteren Funktionsablauf nötige Datenstruktur
     * @param portNumber Anzahl der Ports, die der Switch verwalten soll
     * @return Gibt bei erfolgreicher erstellung TRUE sonst FALSE zurück
     */
    public boolean createSwitch(int portNumber) {
        if (portNumber < 1) {
            System.out.println("Nur natürliche Zahlen ab 1 sind erlaubt.");
            return false;
        }
        ports = new int[portNumber];
        //table = new ArrayList[portNumber];
        table = new ArrayList<>();
        return true;
    }

    /**
     * Diese Methode überprüft und interpretiert die Eingaben und führt
     * die geforderten Funktionen aus.
     * @param command Anweisung die der Switch verarbeiten soll
     * @return Gibt an ob der Switch beendet werden soll: TRUE beenden, FALSE weitermachen
     */
    public boolean handleCommand(String command) {
        ArrayList<String> arr = new ArrayList<>();
        Pattern pat = Pattern.compile("\\S+");
        Matcher m = pat.matcher(command);
        try {
            if (command.matches("\\b(frame)\\s\\d+\\s\\d+\\s\\d+\\b|\\b(del)\\s\\d+[sm]{1}\\b|^(table|statistics|exit)$")) {

                while (m.find()) {
                    arr.add(m.group());
                }
            }
            else throw new Exception();
        }catch (Exception e){ System.out.println(WRONG); return false; }
            switch (arr.get(0)) {
                case "frame":
                    if (Integer.parseInt(arr.get(1)) > ports.length ){
                        System.out.println("Der von Ihnen ausgewählten Port: " + arr.get(1) + " befindet sich außerhalb, die von Ihnen gewünschten Anzahl an Ports(" + ports.length + ") im Switch");
                        return false;
                    }
                    if (!arr.get(2).matches("\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-4])\\b")){
                        System.out.println("Die von Ihnen eingegbene Senderadresse: " + arr.get(2) + " befindet sich außerhalb die Erlaubten Adressen 1-254" );
                        return false;
                }

                    if(!arr.get(3).matches("\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b")){
                        System.out.println("Die von Ihnen eingegbene Zieladresse: " + arr.get(3) + " befindet sich außerhalb die Erlaubten Adressen 1-255" );
                        return false;
                }
                    doSomething(Integer.parseInt(arr.get(1)),Integer.parseInt(arr.get(2)),Integer.parseInt(arr.get(3)));
                    break;
                case "table":
                    printTable(table);
                    break;
                case "statistics":
                    printStats();
                    break;
                case "del":
                    table.set(0,null); //TODO Methode bauen zum löschen von X
                    break;
                case "exit":
                    return true;
                default:
                    System.out.println("Something went wrong :(");
                    return false;
            }
            return false;
    }
    public void addFrame(String portNumber, String senderAddress, String targetAddress){}

    /*
    private static void printTable(){
        System.out.println("Adresse Port Zeit");
        for (int i = 0; i < table.length; i++) {
            System.out.println(table[i]);
        }
    }
     */
    private static void printTable(ArrayList<Entity> listForTable){
        ArrayList<Entity> tmp = listForTable;
        tmp.sort(Comparator.comparing(Entity::getAddress));
        System.out.println("Adresse Port Zeit"); //TODO Wenn 3 stellig dann weniger spaces
        for(Entity e : listForTable){
            System.out.println(e.getAddress() + "    " + e.getPort() + "    " + e.getTimeStamp());
        }
    }

    private static void doSomething(int port, int senderAddress, int targetAddress){
        int searchResult = findAddressIndex(targetAddress); //Falls gefunden gibt den Index des targets sonst -1
        if ((ports[port-1] == 0) && (searchResult == -1)){
            table.add(new Entity(senderAddress, port));
            for (int i = 0; i < ports.length; i++) {
                if (i != port - 1) {
                    ports[i]++;
                }
            }
            System.out.println("Ausgabe auf allen Ports außer Port " + port + ".");
        }
        else if (searchResult != -1 && (table.get(searchResult).getAddress() != senderAddress) && (table.get(searchResult).getPort() == port)){
            table.add(new Entity(port,senderAddress));
            ports[port-1]++;
            System.out.println("Frame wird gefiltert und verworfen.");
        }
        else if(searchResult != -1 && (table.get(searchResult).getAddress() == senderAddress) && (table.get(searchResult).getPort() != port)){
            table.get(searchResult).setPort(port); //TODO Add fall behandeln wo 2 frames auf gleichem port + frame ändert port
        }
        else {
            System.out.println("OOPS! Uns ist ein Fehler unterlaufen");
        }
    }

    private static int findAddressIndex( int address) {
        for (int i = 0; i < table.size(); i++) {
            if (table.get(i).getAddress() == address){
                return i;
            }
        }

        return -1;
    }

    private static void printStats(){
        System.out.println("Port Frames");
        for (int i = 0; i < ports.length; i++) {
            System.out.println(i+1 + "      " + ports[i]);
        }
    }
}
class Entity{
    private int port;
    private int address;
    private SimpleDateFormat timeStamp;
    public Entity(int address, int port){
        this.port = port;
        this.address = address;
        this.timeStamp =  new SimpleDateFormat("HH:mm:ss");
    }

    public int getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setAddress(int address) {
        this.address = address;
    }
    public String getTimeStamp(){ return timeStamp.format(new Date()); }
}
