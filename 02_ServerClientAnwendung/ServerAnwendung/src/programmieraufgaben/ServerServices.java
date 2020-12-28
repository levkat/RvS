package programmieraufgaben;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ServerServices {
    private static final String UNKNOWN = "ERROR Unbekannte Anfrage!";
    private static final String WRONG = "ERROR Falsches Format!";
    private static final String NO_HISTORY = "ERROR Keine Historie vorhanden!";
    private static List<String> history = new LinkedList<>();

    /**
     * Methode, die anhand der Usereingabe die richtige auszuführende Methode in Kombination mit dem Befehl auswählt.
     * @param request aus inputstream; wird falls nötig in findCMD in einzelne Tokens getrennt und auf Richtigkeit überfprüft.
     * @return Antwort des Servers
     */
    public static String handleRequest(String request){
        String res = "";
        ArrayList<String> tmp = findCMD(request);
        if(!tmp.get(0).isEmpty()) {
            try {
                if (!request.startsWith("HISTORY ") && !request.startsWith("DISCARD ") && !request.equals("HISTORY")) {
                    history.add(request); // Alle Anfragen werden in History aufgenommen, außer DISCARD und HISTORY (HISTORY wird erst nach der Bearbeitung aufgenommen)
                }
                switch (tmp.get(0)) { // Wählt je nach eingegebenen Befehl die richtige Funktionalität aus
                    case "GET":
                        res = getDateTime(tmp.get(1));
                        break;
                    case "ADD":
                        res += calc(tmp.get(1), tmp.get(2), '+');
                        break;
                    case "SUB":
                        res += calc(tmp.get(1), tmp.get(2), '-');
                        break;
                    case "MUL":
                        res += calc(tmp.get(1), tmp.get(2), '*');
                        break;
                    case "DIV":
                        res += calc(tmp.get(1), tmp.get(2), '/');
                        break;
                    case "ECHO":
                        StringBuilder build = new StringBuilder("ECHO ");
                        if (!tmp.get(1).isEmpty()) {
                            for (int i = 1; i < tmp.size(); i++) {
                                    build.append(tmp.get(i));
                            }
                        }
                        res = build.toString();
                        break;
                    case "DISCARD":
                        res = "";
                        break;
                    case "PING":
                        res = "PONG";
                        break;
                    case "HISTORY":
                        res = "HISTORY ";
                        if (tmp.size() == 1) {
                            // Überprüft, ob eine Fehlermeldung ausgegeben wurde
                            if(listAll(history).equals(NO_HISTORY)){
                                history.add(request);
                                res = "";
                                return NO_HISTORY;
                            }
                            res+= listAll(history);
                        } else if(tmp.size() == 2) {
                            if(listAll(history).equals(NO_HISTORY)){
                                history.add(request);
                                res = "";
                                return NO_HISTORY;
                            }
                            res+= listNlast(history, Integer.parseInt(tmp.get(1)));
                        }
                        else {
                            throw new IllegalArgumentException();
                        }
                        history.add(request);
                        break;
                    case "OOPS":
                        return WRONG;
                    default:
                        res = UNKNOWN;
                        break;
                }

           }
            catch(Exception e){
                res = WRONG;
            }

        }
        return res;
    }

    /**
     * Erkennt und trennt die Befehle nach den vorgegebenen Mustern und überprüft diese auf Korrektheit
     * @param request Eingabe des Users
     * @return Liste der Elemente des Befehls
     */
    private static ArrayList <String> findCMD(String request){
        ArrayList<String> arr = new ArrayList<>();
        Pattern pat = Pattern.compile("\\S+");
        Matcher m = pat.matcher(request);
        // Sonderfall, da nach dem Befehl ein beliebiges Muster folgen kann
        if(request.matches("(ECHO|DISCARD)\\b\\s.*")){
            String[] test = request.split("(?<=ECHO|DISCARD)");
            arr = new ArrayList<>(Arrays.asList(test));
            return arr;
        }
        // Überprüfung, ob ein richtiger Befehl in der Eingabe des Users enthalten ist
        if (request.matches("(GET|ADD|SUB|MUL|DIV|PING|HISTORY)\\b.*")){
            // Trennung nach nicht Leerzeichen, könnte auch theoretisch mit split() nach Leerzeichen getrennt werden.
            while(m.find()){
                arr.add(m.group());
            }
        }
        else {
            // history.add(request);
            arr.add(UNKNOWN);
            return arr;
        }
        // Überprüfung, ob das Format des gültigen eingegebenen Befehls zulässig ist
        if (request.matches("(ADD|SUB|MUL|DIV)\\b\\s(-|\\+)?\\d+\\s(-|\\+)?\\d+") && arr.size() == 3){
            return arr;
        }
        else if(request.matches("(GET)\\b\\s\\w+\\b") && arr.size() == 2){
            return arr;
        }
        else if(request.matches("(PING)\\b") && arr.size() == 1){
            return arr;
        }
        else if(request.matches("\\b(HISTORY)\\b\\s\\d+") && arr.size() < 3){
            return arr;
        }
        else if (request.matches("(HISTORY)\\b") && arr.size() < 2){
            return arr;
        }
        else {
            arr.clear();
            arr.add("OOPS"); // falls ein falsches Format auftaucht für den requestHandler
            return arr;
        }
    }

    /**
     * Diese Methode gibt das heutige Datum oder die aktuelle Zeit zurück
     * @param datetime Date oder Time request
     * @return Zeit bzw. Datum
     */
    private static String getDateTime(String datetime){
        String res;
        switch(datetime){
            case "Time":
                res = "TIME " + new SimpleDateFormat("HH:mm:ss").format(new Date());
                break;
            case "Date":
                res = "DATE " + new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                break;
            default:
                res = WRONG;
        }
        return res;
    }

    /**
     * Führt die durch das arithmetische Zeichen definierte mathematische Operation auf den beiden übergebenen Zahlen durch.
     * @param first Zahl
     * @param second Zahl
     * @param operator arithmetisches Zeichen
     * @return Ergebnis
     */
    private static String calc(String first, String second, char operator){
        String res = "";
        try{
            Integer.parseInt(first);
            Integer.parseInt(second);
        }
        catch(NumberFormatException e)
        {
            return WRONG;
        }
        int a = Integer.parseInt(first);
        int b = Integer.parseInt(second);
        try {
            switch (operator) {
                case '+':
                    res += a + b;
                    res = "SUM " + res;
                    break;

                case '-':
                    res += "DIFFERENCE " + (a - b);
                    break;

                case '*':
                    res += "PRODUCT " + (a * b);
                    break;

                case '/':
                    if(b != 0) {
                        if(a == 0){
                            res += "0";
                        } else {
                    //if (a != 0 && b != 0) {
                        /*if (a >= b) {
                            res += a / b;
                        } else {*/
                            float floaty = (float) a / (float) b;
                            res += floaty;
                        }
                        res = "QUOTIENT " + res;
                    } else {
                        res = "QUOTIENT undefined";
                    }
                    break;
            }


            return res;
        }
        catch (Exception e){
            return WRONG;
        }
    }

    /**
     * Gibt die bisherige Eingabehistorie des Users als String zurück. Dabei werden die jüngsten Anfragen zuerst gelistet.
     *
     * @param list alle bisher angekommene requests außer HISTORY selbst
     * @return Gibt die Liste der bisherigen Abfragen als String zurück
     */
    private static String listAll(List <String> list){
        if( list.isEmpty()) {
            return NO_HISTORY;
        }
        try{
            StringBuilder output = new StringBuilder();
            // Die älteste Abfrage soll als letzte auftauchen
                for (int i = list.size()-1; i >= 0; i--){
                output.append(list.get(i));
                if( i > 0){
                    output.append("\\n");
                }
            }
            return output.toString();
        }
        catch (Exception e){
            return WRONG;
        }
    }

    /**
     * Gibt die n jüngsten Anfragen des Users zurück oder die gesamte Historie, wenn n größer als die Anzahl der bisher gestellten Anfragen ist.
     * Dabei wird die jüngste Anfrage zuerst gelistet.
     *
     * @param list alle bisher angekommene requests außer HISTORY selbst
     * @param lastRequests begrenzt die Anzahl der zurückgegebenen Abfragen
     * @return Gibt die Liste der bisherigen Abfragen als String zurück
     */
    private static String listNlast(List <String> list, int lastRequests){
        try{
            if(lastRequests < 0){
                throw new IllegalArgumentException();
            }
            StringBuilder output = new StringBuilder();
            if( list.isEmpty()) {
                return NO_HISTORY;
            }
            else if (lastRequests >= list.size()){
                return listAll(list);
            }
            else
            {    // Die älteste Abfrage soll als letzte auftauchen
                for( int i = list.size()-1; i >= list.size() - lastRequests; i--){
                    output.append(list.get(i));
                    if(i > list.size() -lastRequests) {
                        output.append("\\n");
                    }
                }
            }

            return output.toString();
        }
        catch (Exception e){
            return WRONG;
        }
    }

    /**
     * Nach jeder neuen Verbindung wird die History gelöscht
     */
    public void resetList(){
        history.clear();
    }
}