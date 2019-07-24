package com.example.gp62.todak;

public class UserInfo {
    // 이메일, 비밀번호, 닉네임, 로그인유무

    String email = "";
    String password= "";
    String nickname = "";
    int user_no;
//    Boolean login = false;


    public UserInfo(String email, String password, String nickname, int user_no) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.user_no = user_no;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public int getUser_no() {
        return user_no;
    }
}
