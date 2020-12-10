package programmieraufgaben;

import java.nio.channels.ScatteringByteChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerServices {
    private static String unknown = "ERROR Unbekannte Anfrage!";
    private static String wrong = "ERROR Falsches Format!";
    private static RequestHistory history = new RequestHistory();
    public static String handleRequest(String request){
        String res = "";
        ArrayList<String> tmp = findCMD(request);
        if(!tmp.get(0).isEmpty()){
            System.out.println("----------------");
            System.out.println(tmp.get(0));
            System.out.println("----------------");
            switch (tmp.get(0)){
                case "GET":
                    res = get(tmp.get(1));
                    break;
                case "ADD":
                        res = "SUM ";
                    if (!tmp.get(1).isEmpty() && !tmp.get(2).isEmpty()){
                        res += calc(tmp.get(1),tmp.get(2),'+');
                    }
                    else if(!tmp.get(1).isEmpty() && tmp.get(2).isEmpty()){
                        res += calc(tmp.get(1));
                    }
                    else if(tmp.get(1).isEmpty() && !tmp.get(2).isEmpty()){
                        res += calc(tmp.get(2));
                    }
                    else{
                        res = wrong;
                    }
                    history.add(request);
                    break;
                case "SUB":
                    res = "DIFFERENCE ";
                    if (!tmp.get(1).isEmpty() && !tmp.get(2).isEmpty()){
                        res += calc(tmp.get(1),tmp.get(2),'-');
                    }
                    else if(!tmp.get(1).isEmpty() && tmp.get(2).isEmpty()){
                        res += calc(tmp.get(1));
                    }
                    else if(tmp.get(1).isEmpty() && !tmp.get(2).isEmpty()){
                        res += calc(tmp.get(2));
                    }
                    else{
                        res = wrong;
                    }
                    history.add(request);
                    break;
                case "MUL":
                    res = "PRODUCT ";
                    if (!tmp.get(1).isEmpty() && !tmp.get(2).isEmpty()){
                        res += calc(tmp.get(1),tmp.get(2),'*');
                    }
                    else if(!tmp.get(1).isEmpty() && tmp.get(2).isEmpty()){
                        res += calc(tmp.get(1));
                    }
                    else if(tmp.get(1).isEmpty() && !tmp.get(2).isEmpty()){
                        res += calc(tmp.get(2));
                    }
                    else{
                        res = wrong;
                    }
                    history.add(request);
                    break;
                case "DIV":
                    res = "QUOTIENT ";
                    if (!tmp.get(1).isEmpty() && !tmp.get(2).isEmpty()){
                        res += calc(tmp.get(1),tmp.get(2),'/');
                    }
                    else if(!tmp.get(1).isEmpty() && tmp.get(2).isEmpty()){
                        res += calc(tmp.get(1));
                    }
                    else if(tmp.get(1).isEmpty() && !tmp.get(2).isEmpty()){
                        res += calc(tmp.get(2));
                    }
                    else{
                        res = wrong;
                    }
                    history.add(request);
                    break;
                case "ECHO":
                    String build = "ECHO ";
                    if(!tmp.get(1).isEmpty()){
                        for (int i = 1; i < tmp.size(); i++){
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
                    history.add(request);
                    res += history.listAll();
                    break;
                default:
                    res = unknown;
                    break;
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
        System.out.println(cmd);
        return arr;
    }
    private static String get(String datetime){
        String res = "";
        switch(datetime.toLowerCase()){
            case "time":
                res = new SimpleDateFormat("HH:mm:ss").format(new Date());
                break;
            case "date":
                res = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                break;
            default:
                res = wrong;
        }
        return res;
    }
    private static String calc(String one){
        String res = "";
        try{
            int a = Integer.parseInt(one);
            int sum = a;
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
                if(a != 0 && b != 0){
                    res += a / b;
                }
                else {
                    res = "";
                    res = "undefined";
                }
                break;
        }



        return res;
    }
}
//(?<=GET|ADD|SUB|MUL|DIV|ECHO|DISCARD|PING|HISTORY)\s+(\S+)\s+(\S+)
