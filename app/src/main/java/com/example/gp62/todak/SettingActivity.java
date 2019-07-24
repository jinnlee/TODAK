package com.example.gp62.todak;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    TextView back; // 뒤로가기
    Button mailTome; // 문의메일보내는 버튼
//    Button recommend_app; // 앱추천해주는 버튼 (마인드카페)
//    Button share_app; // 친구초대하기 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_);

        back = (TextView) findViewById(R.id.back); // 뒤로가기
        mailTome = (Button) findViewById(R.id.mailTome); //문의메일보내는 버튼
//        recommend_app = (Button) findViewById(R.id.recommend_app); //앱추천해주는 버튼 (마인드카페)
//        share_app = (Button) findViewById(R.id.share_app); //친구초대하기 버튼

        //(뒤로가기) -> 메인화면으로!
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 문의메일보내기
        mailTome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent send_email = new Intent(Intent.ACTION_SENDTO);
                send_email.setData(Uri.parse("mailto:happy_lje@naver.com"));
                send_email.putExtra(Intent.EXTRA_SUBJECT, "토닥토닥 개발자에게 문의 메일 보내기"); // 보낼 이메일 제목
                startActivity(send_email);
            }
        });

//        //앱추천해주는 버튼
//        recommend_app.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent go_playstore = new Intent(Intent.ACTION_VIEW);
//                go_playstore.setData(Uri.parse("market://details?id=atommerce.mindcafe"));
////                go_playstore.setData(Uri.parse("market://details?id="+getPackageName()));
////                go_playstore.setData(Uri.parse("market://details?id=com.conbus.app.around"));
//                startActivity(go_playstore);
//            }
//        });

//        //친구초대하기 버튼
//        share_app.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.putExtra(Intent.EXTRA_TEXT, "누구보다 소중한 당신을 어플 토닥토닥으로 초대합니다♥ \n 구글플레이스토어에서 '토닥토닥'을 검색해주세요♥"); // 보낼 내용
//                //공유창이 나옴
//                Intent chooser = Intent.createChooser(share, "공유");
//                startActivity(chooser);
//            }
//        });
    }
}
