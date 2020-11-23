package programmieraufgaben;

import java.io.IOError;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageSplitter {


    public static List<String> splitString(String msg, int size) {
        List<String> splittedString = new ArrayList<>();
        if(LongestWordLength(msg) > size) {
            throw new IllegalArgumentException(String.valueOf(LongestWordLength(msg)));
        }
        String specialChars = "\\\\.\\[\\]\\{\\}\\(\\)\\<\\>\\*\\+\\-\\=\\!\\?\\^\\$\\|\\,";
        int length = msg.length();
        String word = "";
        for (int i = 0; i < length; i++) {
            if (msg.charAt(i) != ' ' && !specialChars.contains(String.valueOf(msg.charAt(i)))) {
                if (msg.charAt(i) == 'n' && msg.charAt(i - 1) == '\\') {
                    splittedString.add("\\n");
                } else {
                    word += msg.charAt(i);
                }
            } else {
                if(word.length() > 0) {
                    splittedString.add(word);
                }
                word = "";
                if(msg.charAt(i) != '\\') {
                    splittedString.add(String.valueOf(msg.charAt(i)));
                }
            }
        }
        splittedString.add(word);
        for(int i = 0; i < splittedString.size() ; i++){
            System.out.println(splittedString.get(i));
        }
        return splittedString;
    }



        /*
        Pattern p = Pattern.compile("[^ ]{" + size  +"}(?<!\\\\(?=n))|(?! {1,"+ size +"})[\\S\\s]{1,"+ size +"}\\b\\W?(?<! |\\\\(?=n))|[!?._,]?"); //(?<=\\)\\b.{1," + (size-1) + "}\\b\\W?|\\b.{1," + (size-1) + "}\\b\\W?
        Matcher m = p.matcher(msg);
        while(m.find()) {
            //System.out.println(m.group().trim());   // Debug
            res.add(m.group());
        }
        return res;*/




    /*
    public static List<String> splitString(String msg, int maxLength) {
        List<String> temp = new ArrayList<>();
        if (LongestWordLength(msg) > maxLength) {
            throw new IllegalArgumentException("Ein Wort hat die Länge: " + String.valueOf(LongestWordLength(msg)) + " und somit überschreitet die gesetzte maximale Datenteil-Länge von " + maxLength);
        }
    }*/
    public static int LongestWordLength(String str)
    {
        String specialChars = "\\\\.\\[\\]\\{\\}\\(\\)\\<\\>\\*\\+\\-\\=\\!\\?\\^\\$\\|";
        int length = str.length();
        int longest = 0, curr_len = 0;
        for (int i = 0; i < length; i++) {
            if (str.charAt(i) != ' ' && !specialChars.contains(String.valueOf(str.charAt(i)))) {
                if (str.charAt(i) == 'n' && str.charAt(i - 1) == '\\') {
                    curr_len = 0;
                    longest = Math.max(longest, 2); //Für den Fall, dass die Maximale Länge 1 ist.
                } else {
                    curr_len++;
                }
                longest = Math.max(longest, curr_len);
            } else {
                curr_len = 0;
            }
        }
        return Math.max(longest, curr_len);
    }

}
