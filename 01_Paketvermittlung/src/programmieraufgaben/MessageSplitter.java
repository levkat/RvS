package programmieraufgaben;

import java.util.ArrayList;
import java.util.List;

public class MessageSplitter {

    /**
     * Hier sollen die Kommandozeilen-Abfragen abgefragt und die Antworten
     * gespeichert werden
     * Es sollte auf Fehlerbehandlung geachtet werden (falsche Eingaben, ...)
     *
     * @param msg aus System.in eingelesene Nachricht
     * @param limit vom Benutzer eingegebene Datenteil-Länge
     * @return Gibt das als Parameter übergebene String, als Liste von einzelnen Stücke
     */
    public static List<String> splitString(String msg, int limit) {
        List<String> splittedString = new ArrayList<>();
        if(LongestWordLength(msg) > limit) {
            throw new IllegalArgumentException("Die Nachricht kann nicht versendet werden, da sie ein Wort mit Länge "+ LongestWordLength(msg) + " > " + limit + "\n" +
                    "enthält.\n");
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
        return splittedString;
    }


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
