package com.example.cs310project;

public class ChatHelper {

    public boolean checkNull(String key){
        if(key != null){
            return true;
        }
        return false;
    }

    public String getFirst(String sender, String receiver){
        String first = null;
        if(sender == null || receiver == null){
            return "";
        }
        int compareResult = sender.compareTo(receiver);

        if (compareResult < 0) {
            first = sender;
        } else if (compareResult > 0) {
            first = receiver;
        }
        return first;
    }
    public String getSecond(String sender, String receiver){
        String second = null;
        if(sender == null || receiver == null){
            return "";
        }
        int compareResult = sender.compareTo(receiver);
        if (compareResult < 0) {
            second = receiver;
        } else if (compareResult > 0) {
            second = sender;
        }
        return second;
    }
}
