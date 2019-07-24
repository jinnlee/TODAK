package com.example.gp62.todak;

public class ChatItem {

    // 댓글리스트의 한 행의 데이터

    String chat_nick;
    String chat_text;
    char heart_isChecked;
    int heart_num;
    int chat_user_no;

    public ChatItem(String chat_nick, String chat_text, char heart_isChecked, int heart_num, int chat_user_no) {

        this.chat_nick = chat_nick;
        this.chat_text = chat_text;
        this.heart_isChecked = heart_isChecked;
        this.heart_num = heart_num;
        this.chat_user_no = chat_user_no;
    }

    public String getChat_nick() {
        return chat_nick;
    }

    public String getChat_text() {
        return chat_text;
    }

    public char getheart_isChecked() {
        return heart_isChecked;
    }

    public int getHeart_num() {
        return heart_num;
    }

    public int getChat_user_no() {
        return chat_user_no;
    }

    public void setChat_nick(String chat_nick) {
        this.chat_nick = chat_nick;
    }

    public void setChat_text(String chat_text) {
        this.chat_text = chat_text;
    }

    public void setHeart_isChecked(char heart) {
        this.heart_isChecked = heart;
    }

    public void setHeart_num(int heart_num) {
        this.heart_num = heart_num;
    }

    public void setChat_user_no(int chat_user_no) {
        this.chat_user_no = chat_user_no;
    }
}
