package programmieraufgaben;

import java.io.IOError;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageSplitter {


    public static List<String> splitString(String msg, int size) {
        List<String> res = new ArrayList<>();
        if(LongestWordLength(msg) > size) {
            throw new IllegalArgumentException(String.valueOf(LongestWordLength(msg)));
        }
        Pattern p = Pattern.compile("\\b.{1," + size + "}\\b\\W?"); //(?<=\\)\\b.{1," + (size-1) + "}\\b\\W?|\\b.{1," + (size-1) + "}\\b\\W?
        Matcher m = p.matcher(msg);
        while(m.find()) {
            //System.out.println(m.group().trim());   // Debug
            res.add(m.group());
        }
        return res;
    }



    /*
    public static List<String> splitString(String msg, int maxLength) {
        List<String> temp = new ArrayList<>();
        if (LongestWordLength(msg) > maxLength) {
            throw new IllegalArgumentException("Ein Wort hat die Länge: " + String.valueOf(LongestWordLength(msg)) + " und somit überschreitet die gesetzte maximale Datenteil-Länge von " + maxLength);
        }
    }*/
    public static int LongestWordLength(String str)
    {
        int n = str.length();
        int res = 0, curr_len = 0;
        for (int i = 0; i < n; i++) {

            if (str.charAt(i) != ' ')
                curr_len++;
            else {
                res = Math.max(res, curr_len);
                curr_len = 0;
            }
        }
        return Math.max(res, curr_len);
    }

}
