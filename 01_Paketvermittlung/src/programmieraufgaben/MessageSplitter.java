package programmieraufgaben;

import java.util.ArrayList;
import java.util.List;

public class MessageSplitter {
    /**
     * Die eingegebene Nachricht wird in ihre einzelnen Wörter und Satzzeichen zerlegt, dabei wird der String \n zusammen gelassen
     * @param msg die Nachricht, die vom User eingegeben wurde
     * @param limit die maximale zulässige Paketlänge
     * @return gibt die zuvor übergebene Nachricht als Liste von einzelnen Wörtern, \n und Satzzeichen zurück
     */


    public static List<String> splitString(String msg, int limit) {
        List<String> splittedString = new ArrayList<>();
        /*
         * Es wird überprüft, ob die einegegebene Nachricht ein zu langes Wort für die vorgegebene Paketlänge enthält.
         * Ist dies der Fall wird eine Exception geworfen.
         */
        if(LongestWordLength(msg) > limit) {
            throw new IllegalArgumentException("Die Nachricht kann nicht versendet werden, da sie ein Wort mit Länge "+ LongestWordLength(msg) + " > " + limit + "\n" +
                    "enthält.\n");
        }
        String specialChars = "\\\\.\\[\\]\\{\\}\\(\\)\\<\\>\\*\\+\\-\\=\\!\\?\\^\\$\\|\\,";
        int length = msg.length();
        String word = "";
        /*
         * Die Schleife geht die einzelnen Zeichen im String durch. Dabei werden Buchstaben zusammengefasst, solange bis
         * ein Sonderzeichen gefunden wird. Das so entstandene Wort wird zur Liste splittedString hinzugefügt ebenso wie
         * das gefundene Sonderzeichen. Wird ein Backslash als Sonderzeichen gefunden, wird überprüft ob dieses im
         * Zusammenhang mit n vorkommt. Somit handelt es sich um ein Newline und die Zeichen müssen zusammen an die Liste
         * angehängt werden.
         */
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
        splittedString.add(word); // Das letzte zusammenhängende Wort aus dem String wird in die Liste eingefügt
        return splittedString;
    }

    /**
     * Sucht in einem String die längste zusammenhängende Buchstabenkette
     * @param str
     * @return gibt die Länge dieser Buchstabenkette aus
     */

    public static int LongestWordLength(String str)
    {
        String specialChars = "\\\\.\\[\\]\\{\\}\\(\\)\\<\\>\\*\\+\\-\\=\\!\\?\\^\\$\\|";
        int length = str.length();
        int longest = 0, curr_len = 0;
        /*
         * Die Schleife durchläuft den String und zählt dabei die Länge eines gefundenen Wortes. Dabei wird die zuvor
         * längste Länge gemerkt und ggf. überschrieben.
         */
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
