package programmieraufgaben;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: ? 17.12.20 extract logic from handleRequest() to methods so it only switches
// FIXME: 17.12.20 (GET Time Date| ADD 1 2 3) funktioniert soll aber nicht
// FIXME: 17.12.20 (ADD__1_2| ADD_1__2| ADD_1_2_______) funktioniert, soll nicht
// TODO: ? 17.12.20 History als Klasse? -> "private History<String> history" in Server?

public class ServerServices {
    private static final String UNKNOWN = "ERROR Unbekannte Anfrage!";
    private static final String WRONG = "ERROR Falsches Format!";
    private static List<String> history = new LinkedList<>();

    public static String handleRequest(String request){
        String res = "";
        ArrayList<String> tmp = findCMD(request);
        if(!tmp.get(0).isEmpty()) {
            try {
                if (!request.startsWith("HISTORY") && !request.startsWith("DISCARD")) {
                    history.add(request); // alle requests werden registriert und durchnummeriert
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
                                if (i != 1){
                                    build.append(" ").append(tmp.get(i));
                                }
                                else {
                                    build.append(tmp.get(i));
                                }
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
                        res = "HISTORY "; //  Einheitliche Serverantwort für Client.extract()
                        if (tmp.size() == 1) {
                            res+= listAll(history);
                        } else if(tmp.size() == 2) {
                            //history.remove(history.size()-1); // weil ich blöd bin, temporäre Lösung
                            res+= listAll(history, Integer.parseInt(tmp.get(1)));
                        }
                        else {
                            throw new IllegalArgumentException();
                        }
                        history.add(request);
                        break;
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

    private static ArrayList <String> findCMD(String request){
        ArrayList<String> arr = new ArrayList<>();
        Pattern pat = Pattern.compile("\\S+");
        Matcher m = pat.matcher(request);
        if (request.matches("(GET|ADD|SUB|MUL|DIV|ECHO|DISCARD|PING|HISTORY).*")){
            while(m.find()){
                arr.add(m.group());
            }
        }
        else {
            arr.add(UNKNOWN);
        }
        if (request.matches("(ADD|SUB|MUL|DIV)\\s\\d+\\s\\d+") && arr.size() == 3){
            return arr;
        }
        else if(request.matches("(GET).*") && arr.size() == 2){
            return arr;
        }
        else if(request.matches("(PING).*") && arr.size() == 1){
            return arr;
        }
        else if(request.matches("(HISTORY).*") && arr.size() < 2){
            return arr;
        }
        else if(request.matches("(ECHO|DISCARD).*")){
            return arr;
        }
        else {
            arr.clear();
            arr.add("OOPS");
            return arr;
        }
    }
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
     * @return bash-ähnliche Darstellung, aufsteigend durchnummeriert von alt nach neu
     */
    private static String listAll(List <String> list){
        if( list.isEmpty()) {
            return "ERROR Keine Historie vorhanden!";
        }
        try{
            StringBuilder output = new StringBuilder();
            for (String s : list) {
                output.append(s);
                output.append("\\n");
            }
            /*  descending order
                for (int i = list.size()-1; i >= 0; i--){
                output.append(list.get(i));
                if( i > 0){
                    output.append("\\n");
                }
            }*/
            return output.substring(0,output.length()-2); // entfernt letzte \\n
        }
        catch (Exception e){
            return WRONG;
        }
    }
    private static String listAll(List <String> list, int lastRequests){
        try{
            if(lastRequests < 0){
                throw new IllegalArgumentException();
            }
            StringBuilder output = new StringBuilder();
            if( list.isEmpty()) {
                return "ERROR Keine Historie vorhanden!";
            }
            else if (lastRequests >= list.size()){
                return listAll(list);
            }
            else
            {
                for( int i = list.size() - lastRequests; i < list.size() ; i++){
                    output.append(list.get(i));
                    output.append("\\n");
                }
            }

            return output.substring(0,output.length()-2); // entfernt letzte \\n
        }
        catch (Exception e){
            return WRONG;
        }
    }
    public void resetList(){
        history.clear();
    }
}
