package com.example.gp62.todak;

import android.widget.TextView;

import java.util.ArrayList;

public class GominItem {
    //고민내용, 닉네임, 닉네임공개유무, 배경사진, 좋아요개수, 댓글개수, 고민글넘버, 유저고유넘버

    String gomin = "";
    String nick = "";
    Boolean nick_OX = false;
    String background = "";
    int int_heart_num = 0;
    int int_chat_num = 0;
    int gomin_no = -1;
    int user_no;


    public GominItem() {
    }

    public GominItem(String gomin, String nick, Boolean nick_OX , String background, int int_heart_num, int int_chat_num, int gomin_no, int user_no) {
        this.gomin = gomin;
        this.nick = nick;
        this.nick_OX = nick_OX;
        this.background = background;
        this.int_heart_num = int_heart_num;
        this.int_chat_num = int_chat_num;
        this.gomin_no = gomin_no;
        this.user_no = user_no;
    }


    public String getGomin() {
        return gomin;
    }

    public String getNick() {
        return nick;
    }

    public Boolean getNick_OX() {
        return nick_OX;
    }

    public String getBackground() {
        return background;
    }

    public int getInt_heart_num() {
        return int_heart_num;
    }

    public int getInt_chat_num() {
        return int_chat_num;
    }

    public void setGomin(String gomin) {
        this.gomin = gomin;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setNick_OX(Boolean nick_OX) {
        this.nick_OX = nick_OX;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setInt_heart_num(int int_heart_num) {
        this.int_heart_num = int_heart_num;
    }

    public void setInt_chat_num(int int_chat_num) {
        this.int_chat_num = int_chat_num;
    }


    public int getGomin_no() {
        return gomin_no;
    }

    public int getUser_no() {
        return user_no;
    }

    public void setGomin_no(int gomin_no) {
        this.gomin_no = gomin_no;
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
    }
}
