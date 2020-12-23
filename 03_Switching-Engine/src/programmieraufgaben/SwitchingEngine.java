package programmieraufgaben;

import javax.swing.text.html.parser.Entity;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
    private static tableEntry[] table;

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
        ports = new int[portNumber + 1];
        //table = new ArrayList[portNumber];
        table = new tableEntry[256];
        System.out.println("\nEin " + portNumber +"-Port-Switch wurde erzeugt.\n");
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
                        System.out.println("\nDer von Ihnen ausgewählten Port: " + arr.get(1) + " befindet sich außerhalb, die von Ihnen gewünschten Anzahl an Ports(" + ports.length + ") im Switch\n");
                        return false;
                    }
                    if (!arr.get(2).matches("\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-4])\\b")){
                        System.out.println("\nDie von Ihnen eingegbene Senderadresse: " + arr.get(2) + " befindet sich außerhalb die Erlaubten Adressen 1-254\n" );
                        return false;
                }

                    if(!arr.get(3).matches("\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b")){
                        System.out.println("\nDie von Ihnen eingegbene Zieladresse: " + arr.get(3) + " befindet sich außerhalb die Erlaubten Adressen 1-255\n" );
                        return false;
                }
                    addFrame(Integer.parseInt(arr.get(1)),Integer.parseInt(arr.get(2)),Integer.parseInt(arr.get(3)));
                    break;
                case "table":
                    printTable(table);
                    break;
                case "statistics":
                    printStats();
                    break;
                case "del":
                    System.out.println("NOCH ZU IMPLEMENTIEREN"); //TODO Methode bauen zum löschen von X
                    break;
                case "exit":
                    return true;
                default:
                    System.out.println("\nSomething went wrong :(\n");
                    return false;
            }
            return false;
    }
    //TODO Broadcast fall behandeln
    private static void broadcoast(int port){
            for (int i = 0; i < ports.length; i++) {
                ports[i]++;
            }
            System.out.println("Broadcast: Ausgabe auf allen Ports außer Port"+ port + ".");
    }
    private static void addFrame(int port, int senderAddress, int targetAddress){
        //wenn wenn Senderadresse nicht in der Tabelle
        if (table[senderAddress] == null && table[targetAddress] == null){
            System.out.println("---------------");
            System.out.println("INIT");
            System.out.println("Senderadresse nicht in der Tabelle && Target auch");
            System.out.println("---------------");
            /**
             * INIT
             */
            table[senderAddress] = (new tableEntry(port));
            /**
             * BROADCAST
             */
            for (int i = 0; i < ports.length; i++) {
                    ports[i]++;
            }
            System.out.println("Ausgabe auf allen Ports außer Port " + port + ".");
        }

        else if (table[senderAddress] == null && table[targetAddress] != null && table[targetAddress].getPort() != port){
            System.out.println("---------------");
            System.out.println("Second");
            System.out.println("Senderadresse nicht in der Tabelle");
            System.out.println("---------------");

            table[senderAddress] = new tableEntry(port);

            ports[table[targetAddress].getPort()]++; //added
            ports[port]++; //added
            System.out.println("Ausgabe auf Port " + table[senderAddress].getPort() + ".");
        }
        else if (table[senderAddress] == null && table[targetAddress] != null && table[targetAddress].getPort() == port){
            System.out.println("---------------");
            System.out.println("Senderadresse nicht in der Tabelle");
            System.out.println("INIT auf gleichem port wie target");
            System.out.println("---------------");
            table[senderAddress] = new tableEntry(port);
            ports[port]++; //added
            System.out.println("Frame wird gefiltert und verworfen.");
        }
        else if (table[senderAddress] != null && table[targetAddress] == null){
            System.out.println("---------------");
            System.out.println("Target nicht in der Tabelle");
            System.out.println("---------------");
            table[senderAddress] = new tableEntry(port);
            ports[table[targetAddress].getPort()]++; //added
            ports[port]++; //added
            System.out.println("Ausgabe auf Port " + table[senderAddress].getPort() + ".");
        }
        //Zieladresse existiert in Table und zieladresse != senderadresse und eingetragene Zieladresse besitzt gleichen port wie senderadresse(Mehere Adressen auf gleichen Port).
        else if ((table[senderAddress] != null && table[targetAddress] != null) && table[senderAddress] != table[targetAddress] && (table[senderAddress].getPort() == port)){
            System.out.println("---------------");
            System.out.println("Mehrere Adressen auf gleichen Port");
            System.out.println("---------------");
            table[senderAddress] = new tableEntry(port);
            ports[table[targetAddress].getPort()]++;
            if(targetAddress == 255){
                broadcoast(port);
            }
            else {
                System.out.println("Frame wird gefiltert und verworfen.");
            }
        }
        else if ((table[senderAddress] != null && table[targetAddress] != null) && (table[senderAddress] == table[targetAddress]) && (table[senderAddress].getPort() == port)){
            System.out.println("---------------");
            System.out.println("sender und target gleich");
            System.out.println("---------------");
            ports[table[senderAddress].getPort()]++;
            System.out.println("Frame wird gefiltert und verworfen.");
        }

        else if(table[targetAddress] != null && (table[senderAddress].getPort() != port)){
            System.out.println("---------------");
            System.out.println("Letzte");
            System.out.println("---------------");
            table[senderAddress].setPort(port);
            ports[table[senderAddress].getPort()]++;
            if(targetAddress == 255){
                broadcoast(port);
            }
            else {
                System.out.println("Ausgabe auf Port " + table[targetAddress].getPort());
                ports[table[targetAddress].getPort()]++;
            }
        }
        else {
            System.out.println("OOPS! Uns ist ein Fehler unterlaufen");
        }
    }
    private static boolean isListEmpty(tableEntry[] list){
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null){
                return false;
            }
        }
        return true;
    }

    private static void printTable(tableEntry[] listForTable){
        if (!isListEmpty(listForTable)) {
            //System.out.println(System.lineSeparator());
            System.out.format("%s%5s%5s\n","Adresse","Port", "Zeit"); //TODO Wenn 3 stellig dann weniger spaces
            for (int i = 1; i < table.length ; i++) {
                if (table[i] != null) {
                    System.out.format("%7s%5s%9s\n", i, table[i].getPort(), table[i].getTimeStamp());
                }
            }
            //System.out.println(System.lineSeparator());
        }
        else{
            System.out.println("\nDie Switch-Tabelle ist leer.\n");
        }
    }

    private static void printStats(){
        System.out.format("%s%7s\n","Port","Frames");
        for (int i = 1; i < ports.length; i++) {
            System.out.format("%4s%7s\n", i,ports[i]);
        }
    }
}
class tableEntry {
    private int port;
    private LocalTime timeStamp;
    private DateTimeFormatter hhmmss = DateTimeFormatter.ofPattern("HH:mm:ss");
    public tableEntry(int port){
        this.port = port;
        this.timeStamp =  java.time.LocalTime.now();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getTimeStamp(){ return timeStamp.format(hhmmss); }
}
