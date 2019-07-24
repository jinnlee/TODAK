package com.example.gp62.todak;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class SignUpActivity extends AppCompatActivity {
    // 회원가입

    Button joinBtn; //회원가입 창 내 가입하기 버튼
    Button check_email; //이메일 중복확인 버튼
    Button check_nick; //닉네임 중복확인 버튼

    TextView back; //이미 계정이 있으신가요? 텍스트뷰 (뒤로가기)
    EditText signUp_email, signUp_pass, signUp_pass_check,signUp_nick;

    String str_signUp_email="";
    String str_signUp_nick="";
    String str_signUp_pass="";
    String str_signUp_pass_check="";
    int int_signUp_user_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        joinBtn = (Button) findViewById(R.id.joinBtn); //회원가입 창 내 가입하기 버튼
        check_email = (Button) findViewById(R.id.check_email); //이메일 중복체크 버튼
        check_nick = (Button) findViewById(R.id.check_nick); //닉네임 중복체크 버튼
        back = (TextView) findViewById(R.id.back); //이미 계정이 있으신가요? 텍스트뷰 (뒤로가기)
        signUp_email = (EditText) findViewById(R.id.signUp_email); //이메일 입력란
        signUp_pass = (EditText) findViewById(R.id.signUp_pass); //패스워드 입력란
        signUp_pass_check = (EditText) findViewById(R.id.signUp_pass_check); //패스워드 체크 입력란
        signUp_nick = (EditText) findViewById(R.id.signUp_nick); //닉네임 입력란

        //이미 계정이 있으신가요? 텍스트뷰 (뒤로가기) 누르면, -> 로그인화면으로 감
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 이메일 중복체크버튼을 누른 경우,
        check_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_signUp_email = signUp_email.getText().toString();//입력된 이메일
                shared_retrieve_email(str_signUp_email);
            }
        });

        // 닉네임 중복체크버튼을 누른 경우,
        check_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_signUp_nick = signUp_nick.getText().toString();//입력된 닉네임
                shared_retrieve_nick(str_signUp_nick);
            }
        });

        // 가입하기 버튼을 누를 경우, 로그인 화면으로 간다
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_signUp_email = signUp_email.getText().toString(); // 입력된 이메일
                str_signUp_nick = signUp_nick.getText().toString(); // 입력된 닉네임
                str_signUp_pass = signUp_pass.getText().toString(); // 입력된 비밀번호
                str_signUp_pass_check = signUp_pass_check.getText().toString(); // 재확인 비밀번호

                if (signUp_email.length() > 0) { //이메일이 입력되었는지 확인!
                    if (signUp_pass.length() > 0) { //이메일 입력 후, 비밀번호가 입력되었는지 확인!
                        if (signUp_pass.length() > 3) { //비밀번호 4자리 이상인지 확인!
                            if (str_signUp_pass_check.length() > 0) { //비밀번호 확인항목에도 비밀번호를 입력했는지 확인
                                // 입력된 비밀번호, 재확인 비밀번호가 같으면 메인으로!
                                // 입력된 이메일을 로그인화면에 이메일 입력창으로!
                                if (str_signUp_pass_check.equals(str_signUp_pass)) {

                                    SharedPreferences SP = getSharedPreferences("회원목록",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = SP.edit();
                                    if(!SP.contains("ID")){
                                        editor.putInt("ID",5);
                                        editor.apply();

                                    }else{
                                        int_signUp_user_no = SP.getInt("ID",0);
                                        int_signUp_user_no = int_signUp_user_no + 1;
                                        editor.putInt("ID",int_signUp_user_no);
                                        editor.apply();

                                        shared_save_member(str_signUp_email,str_signUp_pass,str_signUp_nick,int_signUp_user_no);
                                        Intent GO_Login = new Intent(SignUpActivity.this, LoginActivity.class);
                                        String str_signUp_email = signUp_email.getText().toString();
                                        GO_Login.putExtra("INTENT_input_email", str_signUp_email);
                                        setResult(RESULT_OK, GO_Login);
                                        finish();
                                    }
                                } else { // 입력된 비밀번호, 재확인 비밀번호가 다르면! -> "입력된 비밀번호와 재확인비밀번호가 일치하지 않습니다" 알림 띄우기
                                    Toast.makeText(getApplication(), "입력된 비밀번호와 재확인비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show();
                                }
                            } else { //비밀번호 확인항목에 비밀번호 입력 안되어있음 ->
                                Toast.makeText(getApplication(), "비밀번호 확인 항목에도 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
                            }
                        } else { // 비밀번호 4자리 미만! -> "비밀번호는 4자리이상이에요" 알림 띄우기
                            Toast.makeText(getApplication(), "비밀번호는 4자리이상이에요", Toast.LENGTH_LONG).show();
                        }
                    } else {//비밀번호 입력 안되어있음 -> "비밀번호를 입력해주세요" 알림 띄우기
                        Toast.makeText(getApplication(), "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
                    }
                } else {//이메일 입력 안되어있음 -> "이메일을 입력해주세요" 알림 띄우기
                    Toast.makeText(getApplication(), "이메일을 입력해주세요", Toast.LENGTH_LONG).show();
                }
            }
        });
        Log.e("HEY","onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("HEY","onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("HEY","onResume");

    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e("HEY", "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("HEY","onRestart");
        AlertDialog.Builder dialogBuilder_signup= new AlertDialog.Builder(SignUpActivity.this);
        dialogBuilder_signup.setMessage("작성 중인 내용이 있습니다.\n불러오시겠습니까?");
        dialogBuilder_signup.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialogBuilder_signup.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //다이얼로그 생성
        AlertDialog alertDialog = dialogBuilder_signup.create();
        //다이얼로그 보여주기
        alertDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("HEY","onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("HEY","onDestroy");

    }

    // 회원을 쉐어드에 저장하는 메소드
    public void shared_save_member(String email, String password, String nickname, int user_no){
        //gson을 사용해서 json문자형식으로 저장
        //json을 쉐어드에 저장
        Gson gson = new Gson();
        UserInfo item = new UserInfo(email,password,nickname,user_no);
        String json = gson.toJson(item);
        Log.e("HEY","쉐어드에 저장될 아이템(json형식) : "+json);
        SharedPreferences SP = getSharedPreferences("회원목록",MODE_PRIVATE);
        SharedPreferences.Editor editor = SP.edit();
        editor.putString(email,json);
        editor.commit();
    }

    // 이메일 중복체크시, 쉐어드에 저장된 회원 불러오기
    public void shared_retrieve_email(String email){
        SharedPreferences SP = getSharedPreferences("회원목록",MODE_PRIVATE);
//        SharedPreferences.Editor editor = SP.edit();
        // 이메일유무 = str_email
//        String str_email = SP.getString(email,"");

        if(SP.contains(email)){ // 이메일이 존재한다면!
            Toast.makeText(this,"이미 사용 중인 이메일입니다.",Toast.LENGTH_SHORT).show();
            Log.e("HEY","이미 사용 중인 이메일입니다. 해당 이메일이 있나요?: " +SP.contains(email)+" email: "+ email);
        }else{  // 이메일이 존재하지 않는다면!
            Toast.makeText(this,"사용가능한 이메일입니다",Toast.LENGTH_SHORT).show();
            Log.e("HEY","사용가능한 이메일입니다. 해당 이메일이 있나요? " +SP.contains(email)+" email: "+ email);
        }
    }

    // 닉네임 중복체크시, 쉐어드에 저장된 회원 불러오기
    public void shared_retrieve_nick(String nick){
        SharedPreferences SP = getSharedPreferences("회원목록",MODE_PRIVATE);

        // 쉐어드에 저장

        if(SP.contains(nick)){ // 닉네임이 존재한다면!
            Toast.makeText(this,"이미 사용 중인 닉네임입니다.",Toast.LENGTH_SHORT).show();
            Log.e("HEY","이미 사용 중인 닉네임입니다. 해당 닉네임이 있나요?: " +SP.contains(nick)+" nick: "+ nick);
        }else{  // 이메일이 존재하지 않는다면!
            Toast.makeText(this,"사용가능한 닉네임입니다",Toast.LENGTH_SHORT).show();
            Log.e("HEY","사용가능한 닉네임입니다. 해당 닉네임이 있나요? " +SP.contains(nick)+" nick: "+ nick);
        }
    }
}