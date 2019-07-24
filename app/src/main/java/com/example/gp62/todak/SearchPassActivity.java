package com.example.gp62.todak;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchPassActivity extends AppCompatActivity {

    TextView back; //비밀번호가 생각나셨나요? 텍스트뷰 (뒤로가기)
    EditText toSend_email; // 입력받은 이메일주소
    Button request_code; //인증번호 요청하기 버튼


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pass_);

        back = (TextView) findViewById(R.id.back);
        toSend_email = (EditText) findViewById(R.id.toSend_email);
        request_code = (Button) findViewById(R.id.request_code);


        //비밀번호가 생각나셨나요? 텍스트뷰 (뒤로가기)
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //인증번호 요청하기 버튼
        request_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //이메일 인텐트 참고 )
                // https://developer.android.com/guide/components/intents-common.html?hl=ko

                Intent send_email = new Intent(Intent.ACTION_SENDTO);
//                send_email.setType("plain/text");
//                String email_address = toSend_email.getText().toString();
                send_email.setData(Uri.parse("mailto:"+toSend_email.getText().toString()));
//                send_email.putExtra(Intent.EXTRA_EMAIL, email_address); //보낼 이메일 주소
//                send_email.putExtra(Intent.EXTRA_CC,"happy_lje@naver.com");

                send_email.putExtra(Intent.EXTRA_SUBJECT, "토닥토닥 - 비밀번호 찾기 인증 코드"); // 보낼 이메일 제목
                send_email.putExtra(Intent.EXTRA_TEXT, "인증코드 : abcd"); //보낼 이메일 내용
                startActivity(send_email);
            }
        });
    }
}
