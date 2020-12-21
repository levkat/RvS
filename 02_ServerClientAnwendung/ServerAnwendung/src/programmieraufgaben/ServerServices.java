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
     *
     * @param request
     * @return
     */
    public static String handleRequest(String request){
        String res = "";
        ArrayList<String> tmp = findCMD(request);
        if(!tmp.get(0).isEmpty()) {
            try {
                if (!request.startsWith("HISTORY") && !request.startsWith("DISCARD")) {
                    history.add(request); // Alle Anfragen werden in Hisotry aufgenommen, ausser DISCARD und HISTORY(HISTORY wird erst nach der Bearbeitung aufgenommen)
                }
                switch (tmp.get(0)) {
                    case "GET":
                        res = get(tmp.get(1));
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
                            //Überprüft, ob es eine Fehlermeldung ausgegeben wurde
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
     *
     * @param request
     * @return
     */
    private static ArrayList <String> findCMD(String request){
        ArrayList<String> arr = new ArrayList<>();
        Pattern pat = Pattern.compile("\\S+");
        Matcher m = pat.matcher(request);
        if(request.matches("(ECHO|DISCARD)\\b.*")){
            String[] test = request.split("(?<=ECHO|DISCARD)");
            arr = new ArrayList<>(Arrays.asList(test));
            return arr;
        }
        if (request.matches("(GET|ADD|SUB|MUL|DIV|PING|HISTORY)\\b.*")){
            while(m.find()){
                arr.add(m.group());
            }
        }
        else {
            //history.add(request);
            arr.add(UNKNOWN);
            return arr;
        }
        if (request.matches("(ADD|SUB|MUL|DIV)\\b\\s(-|\\+)?\\d+\\s(-|\\+)?\\d+") && arr.size() == 3){
            return arr;
        }
        else if(request.matches("(GET)\\b.*") && arr.size() == 2){
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
        else if(request.matches("(ECHO|DISCARD)\\b.*")){
            return arr;
        }
        else {
            arr.clear();
            arr.add("OOPS");
            return arr;
        }
    }

    /**
     *
     * @param datetime
     * @return
     */
    private static String get(String datetime){
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
     *
     * @param first
     * @param second
     * @param operator
     * @return
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
                    if (a != 0 && b != 0) {
                        if (a >= b) {
                            res += a / b;
                        } else {
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
    //TODO JAVA Docs @Lea

    /**
     * History full
     * @param list alle bisher angekommene requests außer HISTORY selbst
     * @return Gibt die Liste der bisherige Abfragen als String zurück
     */
    private static String listAll(List <String> list){
        if( list.isEmpty()) {
            return NO_HISTORY;
        }
        try{
            StringBuilder output = new StringBuilder();
            /*for (String s : list) {
                output.append(s);
                output.append("\\n");
            }*/
            //TODO Die älteste Abfrage soll als letzte auftauchen
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
     *  Lists n requests
     * @param list alle bisher angekommene requests außer HISTORY selbst
     * @param lastRequests begrenzt die Anzahl der zurückgegebenen Abfragen
     * @return Gibt die Liste der bisherige Abfragen als String zurück
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
            {
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
    public void resetList(){
        history.clear();
    }
}
