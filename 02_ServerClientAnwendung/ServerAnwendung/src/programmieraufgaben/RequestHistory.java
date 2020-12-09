package programmieraufgaben;

import java.util.ArrayList;

public class RequestHistory {
    private ArrayList<String> list;
    RequestHistory(){
        list = new ArrayList<String>();
    }
    public void add(String s){
        list.add(s);
    }
    public String listAll(){
        String tmp = "";
        for (String s : list){
            tmp+= s + "\n";
        }
        return tmp;
    }
}
