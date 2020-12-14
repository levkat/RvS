package programmieraufgaben;

import java.nio.channels.ScatteringByteChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerServices {
    private static String unknown = "ERROR Unbekannte Anfrage!";
    private static String wrong = "ERROR Falsches Format!";
    private static List<String> history = new LinkedList<>();

    public static String handleRequest(String request){
        String res = "";
        ArrayList<String> tmp = findCMD(request);
        if(!tmp.get(0).isEmpty()) {
            try {
                switch (tmp.get(0)) {
                    case "GET":
                        res = get(tmp.get(1));
                        history.add(request);
                        break;
                    case "ADD":
                        res = "SUM ";
                        if (tmp.size() <= 2) {
                            res = wrong;
                        } else if (!tmp.get(1).isEmpty() && !tmp.get(2).isEmpty()) {
                            res += calc(tmp.get(1), tmp.get(2), '+');
                        } else {
                            res = wrong;
                        }
                        history.add(request);
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
                            res = wrong;
                        }
                        history.add(request);
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
                            res = wrong;
                        }
                        history.add(request);
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
                            res = wrong;
                        }
                        history.add(request);
                        break;
                    case "ECHO":
                        String build = "ECHO ";
                        if (!tmp.get(1).isEmpty()) {
                            for (int i = 1; i < tmp.size(); i++) {
                                build += tmp.get(i);
                            }
                        }
                        res = build;
                        history.add(request);
                        break;
                    case "DISCARD":
                        res = "";
                        break;
                    case "PING":
                        res = "PONG!";
                        history.add(request);
                        break;
                    case "HISTORY":
                        if (tmp.size() == 1) {
                            res = listAll(history);
                        } else if(tmp.size() == 2) {
                            res = listAll(history, Integer.parseInt(tmp.get(1)));
                        }
                        else {
                            throw new IllegalArgumentException();
                        }
                        history.add(request);
                        break;
                    default:
                        history.add(request);
                        res = unknown;
                        break;
                }

           }
            catch(Exception e){
                res = wrong;
            }

        }
        return res;
    }

    private static ArrayList <String> findCMD(String request){
        String tmp = "";
        String cmd = "";
        ArrayList<String> arr = new ArrayList<>();
        Pattern pat = Pattern.compile("\\S+");
        Matcher m = pat.matcher(request);
        boolean bool = false;
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
                res = wrong;
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
            return wrong;
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
            return wrong;
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
                        res = "";
                        res = "undefined";
                    }
                    break;
            }


            return res;
        }
        catch (Exception e){
            return wrong;
        }
    }
    //TODO JAVA Docs @Lea
    private static String listAll(List <String> list){
        if( list.isEmpty()){
            return "ERROR Keine Historie vorhanden!";
        }
        try{
            String output = "";
            for (int i = list.size()-1; i >= 0; i--){
                output += list.get(i);
                if( i > 0){
                    output+= "\\n";
                }
            }
            return output;
        }
        catch (Exception e){
            return wrong;
        }
    }
    private static String listAll(List <String> list, int lastRequests){
        try{
            if(lastRequests < 0){
                throw new IllegalArgumentException();
            }
            String output = "";
            if( list.isEmpty()){
                return "ERROR Keine Historie vorhanden!";
            }
            else if (lastRequests >= list.size()){
                for (int i = list.size()-1; i >= 0; i--){
                    output += list.get(i);
                    if(i > 0){
                        output+= "\\n";
                    }
                }
            }
            else
            {
                for( int i = list.size() - lastRequests; i < list.size() ; i++){
                    output += list.get(i);
                    if(i < list.size()-1){
                        output+= "\\n";
                    }
                }
            }

            return output;
        }
        catch (Exception e){
            return wrong;
        }
    }
    public void resetList(){
        history.clear();
    }
}
