package com.example.gp62.todak;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {

    String Tag = "PostActivity";  // 태그

    TextView back; //뒤로가기버튼. 이 버튼을 누르면, 메인화면으로 간다.
    TextView menu; //메뉴버튼. 여기서 게시물의 수정,삭제가 가능하다.
    TextView writeText; //고민내용이 적힌 텍스트뷰
    TextView writer; //고민글 작성자(닉네임)가 적힌 텍스트뷰
    LinearLayout post_background; // 아이템 배경화면
    CheckBox heart_checkbox; // 좋아요버튼
    //좋아요버튼. 회색하트상태인 좋아요버튼을 누르면, 좋아요버튼이 빨간하트로 바뀌면서 하트 옆 좋아요개수가 하나 증가한다.
    //빨간하트가 된 좋아요버튼을 누르면, 좋아요개수가 하나 감소한다.

    TextView heart_num; // 좋아요개수가 표시되는 텍스트뷰
    TextView chat_num; // 댓글개수가 표시되는 텍스트뷰
    //int listView_position; //고민목록 내 해당 행의 순서를 메인에서 받아온다.

    Button sendBtn;

    // 리스트뷰 만들기
    SwipeMenuListView chat_listView;
    ChatAdapter chat_adapter;
    ArrayList<ChatItem> chat_items_list; // 댓글리스트

    // 메인 화면에서 인텐트로 가지고 오는 내용들
    // 1.고민내용 2.닉네임 3.닉네임보일지말지 4.배경사진 5.좋아요체크유무 6.좋아요개수 7.댓글개수 8.선택된행순서
    // 좋아요버튼 체크유무와 좋아요개수의 실질적인 내용
    Boolean heart_checked; // 좋아요가 체크되었는지 확인한다.
    Boolean nick_OX; // 닉네임보일지말지 여부
    int int_heart_num = 0; //좋아요개수. 좋아요버튼에 따라서 좋아요개수가 달라진다.
    int int_chat_num = 0; //댓글개수.
//    int int_chat_num = chat_items_list.size(); //댓글개수.

    int MODIFY_CODE = 3333;

    // 글쓰기 화면에서 인텐트로 가지고 오는 내용들 (1.고민내용 2.닉네임 3.배경)
    String gomin = "";
    String post_nick = "";
    String background = "";

    // 댓글
    EditText comment_text;
//    String chat_nick = ""; // 댓글 작성자 닉네임

    // 로그인한 회원정보
    String login_nick = ""; //닉네임
    int login_no; //고유번호

    // 포스트의 고유번호
    int ID = 0;

    //글쓴이 번호
    int writer_no;

//    // 음악재생
//    SwitchCompat musicBtn;
//    MediaPlayer mediaPlayer;
//    static final int INITITIALIZED = 0;
//    static final int STARTED = 1;
//    static final int PAUSED = 2;
//    static int curState = INITITIALIZED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // * 메인 화면에서 인텐트로 가지고 오는 내용들
        // 1. 다 가져오기
        // (1.고민내용 2.닉네임 3.닉네임보일지말지 4.배경사진 5.좋아요체크유무 6.좋아요개수 7.댓글개수 8.선택된행순서)
        // 2. 세팅하기

        // 1. 메인 화면에서 인텐트로 가지고 오는 내용들 다 가져오기
        Intent intent = getIntent();
        gomin = intent.getStringExtra("고민내용");
        post_nick = intent.getStringExtra("닉네임"); //글 작성자의 닉네임
        nick_OX = intent.getBooleanExtra("닉네임보일지말지", false);

        background = intent.getStringExtra("배경사진");

        heart_checked = intent.getBooleanExtra("좋아요체크유무", false);
        int_heart_num = intent.getIntExtra("좋아요개수", 0);
        int_chat_num = intent.getIntExtra("댓글개수", 0);
        //listView_position = intent.getIntExtra("선택된행순서", 0);
        writer_no = intent.getIntExtra("유저번호", 0);
        ID = intent.getIntExtra("ID", 0); // 글 고유번호


        chat_listView = (SwipeMenuListView) findViewById(R.id.post_listview);
        // 리스트뷰 세팅
        chat_items_list = new ArrayList<>();
        final View header = getLayoutInflater().inflate(R.layout.listview_header, null, false);
        chat_listView.addHeaderView(header);

        chat_num = (TextView) findViewById(R.id.chat_num);
        chat_adapter = new ChatAdapter(getApplicationContext(),chat_items_list, ID, chat_listView,chat_num);
        chat_listView.setAdapter(chat_adapter);
        //setListViewHeightBasedOnChildren(chat_listView);

        back = (TextView) findViewById(R.id.back); //뒤로가기 버튼. 메인화면으로 간다.
        menu = (TextView) findViewById(R.id.menu); //메뉴 버튼. 해당 행의 수정, 삭제가 가능하다
        writeText = (TextView) findViewById(R.id.header_writeText); //고민내용이 적히는 텍스트뷰
        writer = (TextView) findViewById(R.id.writer); //고민을 적은 작성자(글쓴이) 닉네임
        heart_num = (TextView) findViewById(R.id.heart_num);
        heart_checkbox = (CheckBox) findViewById(R.id.heart);
        post_background = (LinearLayout) findViewById(R.id.post_background);

        sendBtn = (Button) findViewById(R.id.sendBtn);
        comment_text = (EditText) findViewById(R.id.comment_text);
        //post_listview = findViewById(R.id.post_listview);

        SharedPreferences SP_chat = getSharedPreferences("댓글" + ID, MODE_PRIVATE);
        // SharedPreferences.Editor editor = SP_chat.edit();
        //editor.clear();

        //2
        Gson gson1 = new Gson();
   /*     int k = 0;
        while (true) {
            if (SP_chat.getString("댓글"+String.valueOf(k), "").equals("")) {
                break;
            }
            String json1 = SP_chat.getString("댓글"+String.valueOf(k), "");
            ChatItem item1 = gson1.fromJson(json1, ChatItem.class);
            chat_items_list.add(item1);
            k++;
        }*/

        for (int i = 0; i < SP_chat.getAll().size(); i++) {
            String json1 = SP_chat.getString(String.valueOf(i), "");
            ChatItem item = gson1.fromJson(json1, ChatItem.class);
            chat_items_list.add(item);
        }
        chat_adapter.notifyDataSetChanged();

        back.setOnClickListener(Post_Btn);
        menu.setOnClickListener(Post_Btn);
        sendBtn.setOnClickListener(Post_Btn);


        // 2. 메인 화면에서 인텐트로 가지고 오는 내용들 세팅하기
        writeText.setText(gomin); //고민내용
        if (nick_OX == true) { // 닉네임보일지말지
            writer.setText(post_nick); //닉네임
        } else {
            writer.setText("누군가");
        }
        if (!background.equals("")) { //배경사진
            Uri uri = Uri.parse(background);
            post_background.setBackground(uriToDrawable(uri));
        } else {
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.background4);
            post_background.setBackground(drawable);
        }

        // * 작성자와 로그인한 회원이 같은 경우, 메뉴버튼(글을 수정,삭제할 수 있는 버튼)이 보이게하고
        //   다른 경우, 안보이게 하기!
        SharedPreferences SP = getSharedPreferences("로그인한회원", MODE_PRIVATE);
        String json = SP.getString("로그인한회원", "");
        final Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(json, UserInfo.class);
        login_nick = userInfo.getNickname();
        login_no = userInfo.getUser_no();

        Log.e(Tag, "post_nick : " + post_nick);
        Log.e(Tag, "login_nick : " + login_nick);

        Log.e(Tag, "writer_no : " + writer_no);
        Log.e(Tag, "login_no : " + login_no);

        if (writer_no == login_no) {
            menu.setVisibility(View.VISIBLE);
        } else {
            menu.setVisibility(View.GONE);
        }

        // 좋아요 쉐어드에서 좋아요유무 가져오기
        SharedPreferences SP_like = getSharedPreferences("좋아요한유저"+ID, Context.MODE_PRIVATE);
        // 현재 글에 현재 로그인한 유저가 좋아요했는지 확인
        if(SP_like.contains(""+login_no)){
            // 있다면, 체크박스에 체크하기
            heart_checkbox.setChecked(true);
        }else{
            // 없다면, 체크박스에 체크하지 않기
            heart_checkbox.setChecked(false);
        }

        // 좋아요개수 세팅
        int_heart_num = SP_like.getAll().size();
        heart_num.setText(String.valueOf(int_heart_num));

        // 좋아요체크하기
        heart_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 유저가 좋아요를 누른경우, 좋아요체크 + 좋아요개수추가하기
                if(heart_checkbox.isChecked()){
                    int_heart_num = add_Like_user_list(""+ID,login_no);
                    heart_num.setText(String.valueOf(int_heart_num));
                }else {
                    //
                    int_heart_num = delete_Like_user_list(""+ID,login_no);
                    heart_num.setText(String.valueOf(int_heart_num));
                }
            }
        });

        // 댓글 개수 세팅
        int_chat_num = chat_items_list.size();
        chat_num.setText(String.valueOf(int_chat_num));

        // * 댓글 추가하기 (참고로, 댓글 삭제는 ChatAdapter 에서 함!)
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 댓글 추가시
                ChatItem item = new ChatItem(login_nick, comment_text.getText().toString(), 'x', 0, login_no);
//    public ChatItem(String chat_nick, String chat_text, char heart_isChecked, int heart_num)
                chat_items_list.add(item);
                chat_adapter.notifyDataSetChanged();
                int_chat_num = chat_items_list.size();
                chat_num.setText(""+int_chat_num);
                comment_text.setText("");

//                // 댓글이 3개 이상이면, 리스트뷰의 높이를 정해준다.
//                if(chat_items_list.size()>3){
//                    chat_listView.getMeasuredHeight();
//                    chat_listView.sethei
//                }
            }
        });

//        // * 쉐어드에 저장된 내용들을 어레이리스트에 뿌려준다.
        // 0. 해당 포스트의 고유번호를 가져온다.
//        // 1. "고민목록" 쉐어드 파일을 연다
//        // 2. 쉐어드에 저장된 내용들을 어레이리스트 순서대로 가져온다.
//        // - gson 객체를 만든다.
//        // - 쉐어드에서 해당 순서에 맞는 어레이리스트 내용들을 가져온다.
//        // - gson을 사용해서, json형식으로 저장된 내용들을 gomin_item클래스의 객체로 바꾼다.
//        // - 어레이리스트에 gomin_item 객체로 바꾼 내용들을 하나씩 추가하기
//
        // 포스트의 고유번호를 받아와서 초기화시킨다.
       /* ID = intent.getIntExtra("ID", 0);*/

        //1

        //스와이프 할 때!
        chat_listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                chat_listView.smoothOpenMenu(position);

            }

            @Override
            public void onSwipeEnd(int position) {
                chat_listView.smoothOpenMenu(position);
            }
        });

//        heart_checkbox.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                //체크 되어 있는 경우
//                Log.e(Tag, "하트체크유무 : " + heart_checked);
//                if (heart_checked) {
//                    heart_checked = false;
//                    int_heart_num -= 1;
//
//                    //체크가 안되어 있는 경우
//                } else {
//                    heart_checked = true;
//                    int_heart_num += 1;
//                }
//
//                heart_checkbox.setChecked(heart_checked); //좋아요체크유무
//                heart_num.setText(String.valueOf(int_heart_num)); //좋아요개수
//            }
//        });

    } //create 끝


    TextView.OnClickListener Post_Btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // * 뒤로가기버튼을 클릭하는 경우, 메인액티비티로 간다.
                case R.id.back:
                    finish();
                    break;

                case R.id.menu:
                    // < 고민글 수정/삭제> 2
                    CharSequence[] items = {"수정", "삭제"};
                    AlertDialog.Builder dialogBuilder_post = new AlertDialog.Builder(PostActivity.this);
//                    dialogBuilder_picture.setTitle("사진 가져오기");
                    dialogBuilder_post.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case 0:
                                    // < 고민글 수정/삭제> 2-1) 1
                                    Intent toModify_goWrite_intent = new Intent(PostActivity.this, WriteActivity.class);
                                    toModify_goWrite_intent.putExtra("항목", "수정해줘");

                                    toModify_goWrite_intent.putExtra("ID", ID);
                                    toModify_goWrite_intent.putExtra("고민내용", gomin);
                                    toModify_goWrite_intent.putExtra("닉네임", post_nick);
                                    toModify_goWrite_intent.putExtra("닉네임보일지말지", nick_OX);

//                                    String str_post_background = post_background.getBackground().toString();
                                    toModify_goWrite_intent.putExtra("배경사진", background);

                                    startActivityForResult(toModify_goWrite_intent, MODIFY_CODE);

                                    break;

                                case 1:
                                    // < 고민글 수정/삭제> 2-2) 1
//                                    Intent delete_intent = new Intent(PostActivity.this, GominListActivity.class);
//                                    delete_intent.putExtra("항목", "삭제");

                                    SharedPreferences SP_chat = getSharedPreferences("댓글" +ID, MODE_PRIVATE);
                                    Log.e(Tag,"삭제할"+ID+"번 댓글 내용 : "+SP_chat.getString(""+ID,""));
                                    SharedPreferences.Editor editor_chat = SP_chat.edit();
                                    editor_chat.clear();
                                    editor_chat.apply();
                                    Log.e(Tag,ID+"번 댓글삭제함");

                                    // 글 삭제
                                    SharedPreferences gomin = getSharedPreferences("고민목록", MODE_PRIVATE);
                                    SharedPreferences.Editor editor_gomin = gomin.edit();
                                    Log.e(Tag,"삭제할"+ID+"번 고민글 내용 : "+gomin.getString(""+ID,""));
                                    editor_gomin.remove(""+ID);
                                    editor_gomin.apply();
                                    Log.e(Tag,ID+"번 글삭제함");
                                    finish();
                                    break;

//                                case 2: //기본배경
//                                    break;
                            }
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog alertDialog = dialogBuilder_post.create();
                    alertDialog.show();
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // < 고민글 수정/삭제> 2-1) 3
            if (requestCode == MODIFY_CODE) {
                gomin = data.getStringExtra("고민내용");
                nick_OX = data.getBooleanExtra("닉네임보일지말지", false);
                Log.e(Tag, "닉네임보일지말지 :" + nick_OX);
                background = data.getStringExtra("배경사진");

                chat_adapter.notifyDataSetChanged();

                writeText.setText(gomin);

                if (nick_OX) {
                    writer.setText(post_nick);
                } else {
                    writer.setText("누군가");
                }

                if (!background.equals("")) {
                    Uri uri = Uri.parse(background);
                    post_background.setBackground(uriToDrawable(uri));
                }
                heart_checkbox.setChecked(heart_checked);

                // < 고민글 수정/삭제> 2-1) 4
                // 1.고민내용 2.닉네임 3.닉네임보일지말지 4.배경사진 5.좋아요체크유무 6.좋아요개수 7.댓글개수 8.선택된행순서

                Gson gson = new Gson();
                GominItem gomin_Item = new GominItem(gomin, post_nick, nick_OX, background, int_heart_num, int_chat_num, ID,writer_no);
                SharedPreferences SP_gomin = getSharedPreferences("고민목록", MODE_PRIVATE);
                SharedPreferences.Editor editor_gomin = SP_gomin.edit();
                String json_gomin = gson.toJson(gomin_Item);
                editor_gomin.putString(""+ID,json_gomin);
                Log.e(Tag, "쉐어드 '"+ID+"'에 추가된 정보 : " + json_gomin);
                editor_gomin.apply();

            }
        }

    }

    //URI에서 Drawable로
    public Drawable uriToDrawable(Uri yourUri) {
        Drawable yourDrawable = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(yourUri);
            yourDrawable = Drawable.createFromStream(inputStream, yourUri.toString());
        } catch (FileNotFoundException e) {
            // yourDrawable = getResources().getDrawable(R.drawable.default_image);
        }
        return yourDrawable;
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(Tag, "포스트 : onStart()");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(Tag, "포스트 : onResume()");

//        if(mediaPlayer.isPlaying()==false) {
//            musicBtn.setChecked(false);
//        }else{
//            musicBtn.setChecked(true);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(Tag, "포스트 : onPause()");

//        // 현재 댓글목록 내용들을 쉐어드에 저장시킨다.
//        // 1. "댓글목록" 쉐어드 파일을 연다.
//        // 2. 에디터 객체를 만들어, 쉐어드에 내용을 추가,수정,삭제,저장할 수 있게 한다.
//        // 3. 쉐어드에 저장된 내용들을 다 없앤다 (새로 저장시키기 위해)
//        // 4. 쉐어드에 어레이리스트 순서를 키로 두고, 순서에 해당하는 각각의 내용들을 저장한다.
//        // - gson 객체를 만든다.
//        // - 어레이리스트 내용들을 gson을 사용해서 json형식으로 바꾼다.
//        // - 어레이리스트 순서를 키로 두고, json형식으로 바꾼 내용들을 쉐어드에 저장시킨다.
//

        // 댓글저장
        //1
        SharedPreferences SP_chat = getSharedPreferences("댓글" + ID, MODE_PRIVATE);
        //2
        SharedPreferences.Editor editor = SP_chat.edit();
        //3
        editor.clear();
        //4
        Gson gson = new Gson();
        //Log.e(Tag, "댓글개수 : " + chat_items_list.size());
        for (int i = 0; i < chat_items_list.size(); i++) {
            ChatItem item = chat_items_list.get(i);
            String json = gson.toJson(item);
            editor.putString(String.valueOf(i), json);
            editor.apply();
        }
        // 쉐어드에 잘 저장되었나 확인하기!
        for (int i = 0; i < chat_items_list.size(); i++) {
            Log.e(Tag, "댓글 : onPause() - 쉐어드에 저장된 내용들 : " + SP_chat.getString("댓글" + String.valueOf(i), ""));
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.e(Tag, "포스트 : onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(Tag, "포스트 : onRestart()");
//        if(curState == PAUSED){
//            mediaPlayer.start();
//        }
    }

    // 배경음악) 6. onDestroy()에는 배경음악쓰레드 정지(음악 정지)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(Tag, "포스트 : onDestroy()");
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    // 유저가 좋아요한 경우, '좋아요유저'쉐어드에 추가
    // 고민글no, 유저no
    // 현재 글에 좋아요한 유저 수를 출력한다.
    public int add_Like_user_list(String gomin_no, int user_no) {

        // '좋아요유저'+ 고민글no로 쉐어드를 불러온다.
        SharedPreferences SP = getSharedPreferences("좋아요한유저"+gomin_no, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SP.edit();
        // key를 좋아요한 유저 no로 저장한다.
        editor.putString(""+user_no,"");
        editor.apply();
        Log.e(Tag,"좋아요한 유저 추가 :"+SP.getAll());
        return SP.getAll().size();
    }

    // 유저가 좋아요를 "취소"한 경우, '좋아요유저'쉐어드에서 유저 삭제
    // 고민글no, 유저no
    // 현재 글에 좋아요한 유저 수를 출력한다.

    public int delete_Like_user_list(String gomin_no, int user_no) {
        // '좋아요유저'+ 고민글no로 쉐어드를 불러온다.
        SharedPreferences SP_like = getSharedPreferences("좋아요한유저"+gomin_no, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_like = SP_like.edit();

        // 현재 유저의 좋아요한 내역을 삭제한다.
        if(SP_like.contains(""+user_no)){
            editor_like.remove(""+user_no);
        }
        editor_like.apply();
        Log.e(Tag,"좋아요한 유저 삭제 :"+SP_like.getAll());

        return SP_like.getAll().size();
    }

}

