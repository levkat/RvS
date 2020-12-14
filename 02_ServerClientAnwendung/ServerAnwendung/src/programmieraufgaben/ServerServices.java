package programmieraufgaben;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerServices {
    private static final String UNKNOWN = "ERROR Unbekannte Anfrage!";
    private static final String WRONG = "ERROR Falsches Format!";
    private static List<String> history = new LinkedList<>();

    public static String handleRequest(String request){
        String res = "";
        ArrayList<String> tmp = findCMD(request);
        if(!tmp.get(0).isEmpty()) {
            try {
                history.add((history.size()+1) + " " + request); // alle requests werden registriert und durchnummeriert
                switch (tmp.get(0)) {
                    case "GET":
                        res = get(tmp.get(1));
                        break;
                    case "ADD":
                        res = "SUM ";
                        if (tmp.size() <= 2) {
                            res = WRONG;
                        } else if (!tmp.get(1).isEmpty() && !tmp.get(2).isEmpty()) {
                            res += calc(tmp.get(1), tmp.get(2), '+');
                        } else {
                            res = WRONG;
                        }
                        break;
                    case "SUB":
                        res = "DIFFERENCE ";
                        if (!tmp.get(1).isEmpty() && !tmp.get(2).isEmpty()) {
                            res += calc(tmp.get(1), tmp.get(2), '-');
                        } else if (!tmp.get(1).isEmpty() && tmp.get(2).isEmpty()) {
                            res += calc(tmp.get(1));
                        } else if (tmp.get(1).isEmpty() && !tmp.get(2).isEmpty()) {
                            res += calc(tmp.get(2));
                        } else {
                            res = WRONG;
                        }
                        break;
                    case "MUL":
                        res = "PRODUCT ";
                        if (!tmp.get(1).isEmpty() && !tmp.get(2).isEmpty()) {
                            res += calc(tmp.get(1), tmp.get(2), '*');
                        } else if (!tmp.get(1).isEmpty() && tmp.get(2).isEmpty()) {
                            res += calc(tmp.get(1));
                        } else if (tmp.get(1).isEmpty() && !tmp.get(2).isEmpty()) {
                            res += calc(tmp.get(2));
                        } else {
                            res = WRONG;
                        }
                        break;
                    case "DIV":
                        res = "QUOTIENT ";
                        if (!tmp.get(1).isEmpty() && !tmp.get(2).isEmpty()) {
                            res += calc(tmp.get(1), tmp.get(2), '/');
                        } else if (!tmp.get(1).isEmpty() && tmp.get(2).isEmpty()) {
                            res += calc(tmp.get(1));
                        } else if (tmp.get(1).isEmpty() && !tmp.get(2).isEmpty()) {
                            res += calc(tmp.get(2));
                        } else {
                            res = WRONG;
                        }
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
                        history.remove(history.size()-1); // Nimmt DISCARD  raus
                        break;
                    case "PING":
                        res = "PONG";
                        break;
                    case "HISTORY":
                        res = "HISTORY "; //  Einheitliche Serverantwort für Client.extract()
                        if (tmp.size() == 1) {
                            res+= listAll(history);
                        } else if(tmp.size() == 2) {
                            res+= listAll(history, Integer.parseInt(tmp.get(1)));
                        }
                        else {
                            throw new IllegalArgumentException();
                        }
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
            arr.add("Unbekannte Anfrage!");
        }
        return arr;
    }
    private static String get(String datetime){
        String res;
        switch(datetime.toLowerCase()){
            case "time":
                res = "TIME " + new SimpleDateFormat("HH:mm:ss").format(new Date());
                break;
            case "date":
                res = "DATE " + new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                break;
            default:
                res = WRONG;
        }
        return res;
    }
    private static String calc(String one){
        String res = "";
        try{
            int sum = Integer.parseInt(one);
            res += sum;
        }
        catch(NumberFormatException e)
        {
            return WRONG;
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
                    break;

                case '-':
                    res += a - b;
                    break;

                case '*':
                    res += a * b;
                    break;

                case '/':
                    if (a != 0 && b != 0) {
                        if (a >= b) {
                            res += a / b;
                        } else {
                            float floaty = (float) a / (float) b;
                            res += floaty;
                        }

                    } else {
                        res = "undefined";
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
     * @param list
     * @return bash-ähnliche Darstellung, aufsteigend durchnummeriert von alt nach neu
     */
    private static String listAll(List <String> list){
        if( list.size() == 1 && list.contains("1 HISTORY")){
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
            return output.toString();
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
            if( list.size() == 1 && list.contains("1 HISTORY")){
                return "ERROR Keine Historie vorhanden!";
            }
            else if (lastRequests >= list.size()){
                listAll(list);
            }
            else
            {
                for( int i = list.size() - lastRequests; i < list.size() ; i++){
                    output.append(list.get(i));
                    output.append("\\n");
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
