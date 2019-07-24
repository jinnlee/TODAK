package com.example.gp62.todak;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    Button logInBtn, signUpBtn;
    //    TextView search_email, search_pass;
    EditText logIn_email, logIn_pass;

    CheckBox check; //이메일 저장하기 체크박스
    public boolean save_check;

    String Tag = "LoginActivity";

    public int INPUT_EMAIL = 0; // 회원가입 화면에서 로그인 화면으로 이메일을 전달할 때 씀

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 로딩화면 부르기
        Intent go_loading = new Intent(getApplicationContext(),LoadingActivity.class);
        startActivity(go_loading);
        Log.e(Tag,"로딩화면부르기");

        logInBtn = (Button) findViewById(R.id.logInBtn); //로그인 버튼
        signUpBtn = (Button) findViewById(R.id.signUpBtn); //회원가입 버튼
//        search_email = (TextView) findViewById(R.id.search_email); //가입되어있는지 확인 텍스트뷰
//        search_pass = (TextView) findViewById(R.id.search_pass); //비밀번호 찾기 텍스트뷰
        logIn_email = (EditText) findViewById(R.id.logIn_email); //이메일 입력란
        logIn_pass = (EditText) findViewById(R.id.logIn_pass); //비밀번호 입력란
        check = (CheckBox) findViewById(R.id.check); //로그인 화면 내 이메일 저장하기 체크박스

        // 클릭할 때, 반응하게 하기
        logInBtn.setOnClickListener(LogInAct_Btn);
        signUpBtn.setOnClickListener(LogInAct_Btn);
//        search_email.setOnClickListener(LogInAct_TextView);
//        search_pass.setOnClickListener(LogInAct_TextView);

        // 이메일 저장하기 체크박스에 체크가 되었다면, 이메일을 쉐어드에서 꺼내오기
        SharedPreferences SP = getSharedPreferences("LOGIN", MODE_PRIVATE);
        // 1. 쉐어드에서 저장된 이메일, 체크의 유/무 꺼내기
        String str_email = SP.getString("save_Email", "");
        Log.e(Tag, "체크가 되어있나요? 그렇다면 쉐어드에서 이메일을 꺼내오자! : " + SP.getBoolean("save_check", false));
        save_check = SP.getBoolean("save_check", false);


        // * 회원정보 삭제하려면 아래 주석풀기
//        SharedPreferences SP2 = getSharedPreferences("회원목록", MODE_PRIVATE);
//        SharedPreferences.Editor editor = SP2.edit();
//        editor.clear();
//        editor.commit();

        if (save_check) {
            //  쉐어드에 이메일저장하기 체크박스에 체크가 되어있다고 저장되었다면,
            // 2. 이메일, 체크 각각 세팅하기
            logIn_email.setText(str_email); // 이메일입력칸에 이메일 세팅하기
            check.setChecked(save_check); // 이메일저장하기 체크박스에 체크표시 해두기

//        editor.putString("save_Pass",str_logIn_pass);

            // 쉐어드에 저장되었는지 확인!
            Log.e(Tag, "onCreate :쉐어드에서 이메일, 체크박스 유무 꺼내오자!");
            Log.e(Tag, "저장된 이메일: " + SP.getString("save_Email", ""));
            Log.e(Tag, "저장된 체크박스 유/무: " + SP.getBoolean("save_check", false));

        } else {
            // 2. 이메일 저장하기 체크박스에 체크가 안되어있다면, 이메일은 빈칸으로, 체크 유/무 중 무로 세팅하기
            Log.e(Tag, "체크박스에 체크가 안되어 빈칸으로 세팅!");
            logIn_email.setText("");
            check.setChecked(false);
        }
        Log.e(Tag, "로그인 : onCreate()");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(Tag, "로그인 : onPause()");
        if (check.isChecked()) { // 이메일 저장하기 체크박스에 체크가 되었다면, 이메일(이메일o)과 체크상태(유)를 쉐어드에 저장하기
            SharedPreferences SP = getSharedPreferences("LOGIN", MODE_PRIVATE);
            SharedPreferences.Editor editor = SP.edit();

            // 1. 입력된 이메일을 문자열로 바꾸기
            String str_logIn_email = logIn_email.getText().toString();

            // 2. 쉐어드에 저장하기
            editor.putString("save_Email", str_logIn_email);
            editor.putBoolean("save_check", true);

            editor.commit();
            // 쉐어드에 저장되었는지 확인!
            Log.e(Tag, " onPause() :쉐어드에서 이메일, 체크박스 유무 저장시키자");
            Log.e(Tag, "저장된 이메일 : " + SP.getString("save_Email", ""));
            Log.e(Tag, "저장된 체크박스 유/무 : " + SP.getBoolean("save_check", false));
//        editor.putString("save_Pass",str_logIn_pass);

        } else {// 이메일 저장하기 체크박스에 체크가 안되었다면, 이메일(빈칸)과 체크상태(무)를 쉐어드에 저장하기
            SharedPreferences SP = getSharedPreferences("LOGIN", MODE_PRIVATE);
            SharedPreferences.Editor editor = SP.edit();
            editor.putBoolean("save_check", check.isChecked());

//            // 1. 쉐어드에 저장하기
//            editor.putString("save_Email", "");
//            editor.putBoolean("save_check", false);
            editor.commit();
//
//            // 쉐어드에 저장되었는지 확인!
//            Log.e("HEY", " onPause() :쉐어드에서 이메일, 체크박스 유무 저장시키자");
//            Log.e("HEY", "저장된 이메일 : " + SP.getString("save_Email", ""));
            Log.e(Tag, "저장된 체크박스 유/무 : " + SP.getBoolean("save_check", false));
        }
    }

    Button.OnClickListener LogInAct_Btn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.logInBtn: //로그인 버튼을 누른 후, 조건을 만족하면! 메인화면으로 감

                    // 입력된 이메일과 비밀번호를 문자열로 바꾸기
                    String str_logIn_email = logIn_email.getText().toString();
                    String str_logIn_pass = logIn_pass.getText().toString();

                    if (logIn_email.length() > 0) { //이메일이 입력되었는지 확인!
                        if (logIn_pass.length() > 0) { //이메일 입력 후, 비밀번호가 입력되었는지 확인!
                            if (logIn_pass.length() > 3) { //비밀번호 4자리 이상인지 확인!
                                //내가 지정해놓은 이메일와 비번이면 메인으로 go!
//                                if (str_logIn_email.equals("test@naver.com") && str_logIn_pass.equals("1234")) {
//                                    Intent GO_Main = new Intent(LoginActivity.this, MainActivity.class);
//                                    startActivity(GO_Main);
//                                    finish();
                                login_check(str_logIn_email, str_logIn_pass);
                                //내가 지정해놓은 이메일와 비번이 아니면 ->"이메일과 비밀번호가 일치하지 않습니다. 다시 입력해주세요" 알림 띄우기
                            } else { // 비밀번호 4자리 미만! -> "비밀번호는 4자리이상이에요" 알림 띄우기
                                Toast.makeText(getApplication(), "비밀번호는 4자리이상이에요", Toast.LENGTH_LONG).show();
                            }
                        } else {//비밀번호 입력 안함 -> "비밀번호를 입력해주세요" 알림 띄우기
                            Toast.makeText(getApplication(), "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
                        }
                    } else {//이메일 입력안함 -> "이메일을 입력해주세요" 알림 띄우기
                        Toast.makeText(getApplication(), "이메일을 입력해주세요", Toast.LENGTH_LONG).show();
                    }
                    break;

                case R.id.signUpBtn: //회원가입 버튼을 누르면, 회원가입화면으로 감
                    Intent GO_SignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivityForResult(GO_SignUp, INPUT_EMAIL); //회원가입화면에서 이메일 받아오기
                    break;
            }
        }
    };

//    TextView.OnClickListener LogInAct_TextView = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.search_email: // 가입된 이메일이 있는지 확인 텍스트뷰
//
//                case R.id.search_pass: // 비밀번호 찾기 텍스트뷰 클릭 -> 비밀번호 찾기 화면
//                    Intent go_search_pass = new Intent(LoginActivity.this, SearchPassActivity.class);
//                startActivity(go_search_pass);
//            }
//        }
//    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 회원가입 화면에서 입력된 이메일을 받아와서
        // 로그인 화면 내 이메일 입력창에 세팅해줌!
        if (requestCode == INPUT_EMAIL && resultCode == RESULT_OK) {
            String str_email = data.getStringExtra("INTENT_input_email");
            logIn_email.setText(str_email);
        }
    }

    public void login_check(String email, String password) {
        SharedPreferences SP = getSharedPreferences("회원목록", MODE_PRIVATE);
        // 현재 저장되어있는 회원들
        Log.e(Tag,"SP.getAll() : "+SP.getAll());

        // 입력된 이메일이 쉐어드에 저장되어있다면! 이메일 불러오기
        if (SP.contains(email)) {
            Gson gson = new Gson();
            String json = SP.getString(email, ""); //이메일 불러오기
            UserInfo item = gson.fromJson(json, UserInfo.class);

            // 입력된 이메일에 해당하는 비밀번호가 맞다면! 메인화면으로 이동한다.
            if (item.getPassword().equals(password)) {

//              //* 로그인한 회원의 정보를 저장한다.
                // 추가시) 누가 쓴 글인지 알기 위해
                // 수정,삭제시) 자기가 쓴 글만 수정,삭제를 할 수 있게
                // 1. 로그인액티비티에서 로그인이 된다면, "로그인한회원" 쉐어드에 로그인한 회원의 정보를 저장한다

               // 1
                SharedPreferences SP_login = getSharedPreferences("로그인한회원", MODE_PRIVATE);
                SharedPreferences.Editor editor = SP_login.edit();

                Gson gson1 = new Gson();
                String json1 = gson1.toJson(item);
                editor.putString("로그인한회원",json1);
                editor.commit();
                Log.e(Tag, "로그인한 회원의 정보가 잘 저장되었나 확인 : " + SP.getString("로그인한회원", ""));
                Log.e(Tag, "로그인성공!");

                new CallLoadingAsync(LoginActivity.this,GominListActivity.class).execute();
//                finish();

            }else{ //입력된 이메일에 해당하는 비밀번호가 아닐 경우!
                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT).show();

            }

        } else { //입력된 이메일이 쉐어드에 저장이 안되어있다면!
            Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT).show();
        }
    }
}
