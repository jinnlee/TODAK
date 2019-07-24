package com.example.gp62.todak;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static java.lang.String.valueOf;

public class GominListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    // 고민 목록 화면

    Button writeBtn, settingBtn;
    TextView writeText, chat, chat_num, writer;
    int ADD_CODE = 0; // 글 추가
    int GO_POST_CODE = 1; // 상세보기화면
    String Tag = "GominListActivity";

   /* // 음악재생
    SwitchCompat musicBtn;
    MediaPlayer mediaPlayer;
    final int INITITIALIZED = 0;
    //    final int STARTED = 1;
    final int PAUSED = 2;
    int curState = INITITIALIZED;
    static Boolean isPause = false;*/

    ListView listView;
    GominAdapter gomin_adapter;
    ArrayList<GominItem> gomin_items_list;
    int int_heart_num = 0;
    int int_chat_num = 0;
    int listView_position;

    // 로그인한 회원의 정보
    String login_nick = ""; //닉네임
    int login_no; // 고유번호


    // * 글쓰기 화면에서 인텐트로 가지고 오는 내용들 (1.고민내용 2.닉네임 3.배경 4.닉네임공개유무 5.작성자번호)
    String gomin_text = "";
    String gomin_nick = "";
    String gomin_background = "";
    boolean gomin_nick_OX = false;
    int writer_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gomin_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 고민글 등록
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent go_write = new Intent(getApplicationContext(),WriteActivity.class);
                go_write.putExtra("항목", "추가");
                go_write.putExtra("닉네임", login_nick);
                startActivityForResult(go_write, ADD_CODE);
            }
        });

        // 왼쪽 햄버거바를 클릭했을 때, 리스트를 보이게 하기 위한 작업
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 수정함 20190720
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        //* 로그인한 회원의 정보를 가져온다
        // 추가시) 누가 쓴 글인지 알기 위해
        // 수정,삭제시) 자기가 쓴 글만 수정,삭제를 할 수 있게

        // 1. 메인액티비티에서 파일명이 "로그인한회원"인 쉐어드에서 유저의 닉네임을 가져온다.
        SharedPreferences SP = getSharedPreferences("로그인한회원", MODE_PRIVATE);
        Log.e(Tag, "로그인한 회원의 정보가 잘 저장되었나 확인 : " + SP.getString("로그인한회원", ""));
        // 2. 그리고 닉네임을 메인액티비티의 전역변수 'nick'에 저장한다.
        Gson gson = new Gson();
        String json = SP.getString("로그인한회원", "");
        UserInfo user_item = gson.fromJson(json, UserInfo.class);
        login_nick = user_item.getNickname();
        login_no = user_item.getUser_no();
        Log.e(Tag,"현재로그인한 회원no : "+login_no);
        Log.e(Tag,"모든 회원정보 "+SP.getAll());

        // * 유저가 로그인 후, 메인액티비티에서 나오는 첫 멘트
        Toast.makeText(getApplicationContext(), login_nick + "님 반갑습니다", Toast.LENGTH_SHORT).show();

        // 리스트뷰 세팅
        gomin_items_list = new ArrayList<>();
        gomin_adapter = new GominAdapter(getApplicationContext(),gomin_items_list);
        listView = (ListView) findViewById(R.id.listview_1);
        listView.setAdapter(gomin_adapter);
//        musicBtn = (SwitchCompat) findViewById(R.id.musicBtn); //배경음악버튼을 누르면, 배경음악이 나옴
//        settingBtn = (Button) findViewById(R.id.settingBtn); //설정버튼을 누르면, 설정 화면으로 감

        Log.e(Tag, "메인 : onCreate()");

        // * 쉐어드에 저장된 내용들을 어레이리스트에 뿌려준다.
        // 1. "고민목록" 쉐어드 파일을 연다
        SharedPreferences SP_gomin = getSharedPreferences("고민목록", MODE_PRIVATE);

        // 메인화면에 게시글들을 초기화시키려면, 밑에 주석 세줄 잠시 풀기!
/*        SharedPreferences.Editor editor = SP_gomin.edit();
        editor.clear();
        editor.commit();

        // 댓글 초기화코드 (아래 6줄)
        for(int i = 0; i<10 ; i++){
            SharedPreferences SP_chat = getSharedPreferences("댓글" + i, MODE_PRIVATE);
            SharedPreferences.Editor editor_chat = SP_chat.edit();
            Log.e(Tag,"댓글가져오기 :"+ SP_chat.getAll());
            editor_chat.clear();
            editor_chat.apply();
            Log.e(Tag,"삭제완료 :"+ SP_chat.getAll());
        }*/

        /*Log.e(Tag,"고민목록 :"+SP_gomin.getAll());*/

        // 2. 쉐어드에 저장된 내용들을 어레이리스트 순서대로 가져온다.
        Gson gson1 = new Gson();
       // int k = 0;
//        while (true) {
//            if (SP_gomin.getString(String.valueOf(k), "").equals("")) {
//                break;
//            }
//            String json1 = SP_gomin.getString(String.valueOf(k), "");
//            ChatItem item1 = gson1.fromJson(json1, ChatItem.class);
//            chat_items_list.add(item1);
//            k++;
//        }

/*        if(SP_gomin.contains("ID")){
            Log.e(Tag,"현재 ID : "+SP_gomin.getInt("ID",0));
            int gomin_size = SP_gomin.getInt("ID",0);
            for(int i = 0; i<= gomin_size; i++){
                if(!SP_gomin.getString(String.valueOf(i),"").equals("")){
                    String json1 = SP_gomin.getString(String.valueOf(i),"");
                    GominItem item1 = gson1.fromJson(json1, GominItem.class);
                    gomin_items_list.add(item1);
                }
            }
        }*/
       //Log.e(Tag,"저장되어있는 것들!"+ SP_gomin.getAll());


        // < 고민글 수정/삭제>
        // * 리스트뷰 한 행을 클릭하면, 포스트액티비티(상세페이지화면)로 넘어간다.
        // * 포스트액티비티에서는 작성한 글의 수정,삭제가 가능하다.
        // * 포스트액티비티에서 고민내용, 닉네임, 닉네임보일지말지, 배경사진, 좋아요체크유무, 좋아요개수, 댓글개수, 선택된행순서를 메인액티비티로 가져 온다.
        // 1. 메인액티비티에서 startActivityForResult 를 사용해, 위 내용들을 메인액티비티로 다시 가져오고자한다.
        //    (GO_POST_CODE : 포스트액티비티로 이동하라고 하는 코드)
        // 2. 포스트액티비티에서 메뉴버튼을 클릭하는 경우 수정, 삭제할 수 있는 다이얼로그 창이 뜬다.
        //    2-1) 수정하는 경우,
        //         1. 포스트액티비티에서 startActivityForResult 를 사용해, 글쓰기액티비티에서 고민내용, 닉네임, 닉네임보일지말지, 배경사진을 포스트액티비티로 가져오고자 한다.
        //         2. 글쓰기액티비티에서 올리기버튼을 누를 경우, setResult 로 포스트액티비티로 보낼 내용들을 세팅시킨다. (RESULT_OK)
        //         3. 포스트액티비티 onActivityResult 메소드에서 RESULT_OK 되고 MODIFY_CODE 인 경우에, 받아온 내용들을 세팅시킨다.
        //         4. 메인액티비티로 위 내용들을 보내기 위해, 포스트액티비티 setResult 메소드에서 위 내용들을 세팅시킨다.
        //         5. 메인액티비티 onActivityResult 메소드에서 RESULT_OK 되고 GO_POST_CODE 이면서
        //           인텐트 이름이 "항목"이고, 그 내용이 "수정끝"인 경우! 받아온 내용들을 세팅시킨다.
        //    2-2) 삭제하는 경우,
        //         1. 포스트액티비티 setResult 메소드에서 메인액티비티에 보낼 내용을 세팅시킨다. (RESULT_OK : GO_POST_CODE 지시에 맞춰서 성공적으로 세팅했다!)
        //         2. 메인액티비티 onActivityResult 메소드에서 RESULT_OK 되고 GO_POST_CODE 이면서
        //           인텐트 이름이 "항목"이고, 그 내용이 "삭제"인 경우! 해당 글을 삭제한다.

        // 상세화면 보기 화면으로 이동(보기화면으로 들어가면, 글을 수정, 삭제할 수 있다)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView_position = listView.getCheckedItemPosition();

                Intent go_post_intent = new Intent(getApplicationContext(), PostActivity.class);
                go_post_intent.putExtra("고민내용", gomin_items_list.get(position).getGomin());
                go_post_intent.putExtra("닉네임", gomin_items_list.get(position).getNick());
                go_post_intent.putExtra("닉네임보일지말지", gomin_items_list.get(position).getNick_OX());
                go_post_intent.putExtra("배경사진", gomin_items_list.get(position).getBackground());
                Log.e(Tag,"좋아요개수 : "+gomin_items_list.get(position).getInt_heart_num());
                go_post_intent.putExtra("좋아요개수", gomin_items_list.get(position).getInt_heart_num());
                go_post_intent.putExtra("댓글개수", gomin_items_list.get(position).getInt_chat_num());
                go_post_intent.putExtra("선택된행순서", position);
                go_post_intent.putExtra("ID", gomin_items_list.get(position).getGomin_no());
                go_post_intent.putExtra("유저번호", gomin_items_list.get(position).getUser_no());


//                Log.e("HEY", "메인 : 고민내용 : " + gomin_items_list.get(position).gomin);
//                Log.e("HEY", "메인 : 닉네임 : " + gomin_items_list.get(position).nick);
//                Log.e("HEY", "메인 : 닉네임보일지말지 : " + gomin_items_list.get(position).nick_OX);
//                Log.e("HEY", "메인 : 배경사진 : " + gomin_items_list.get(position).background);
//                Log.e("HEY", "메인 : 좋아요체크유무 : " + gomin_items_list.get(position).heart_isChecked);
//                Log.e("HEY", "메인 : 좋아요개수 : " + gomin_items_list.get(position).int_heart_num);
//                Log.e("HEY", "메인 : 댓글개수 : " + gomin_items_list.get(position).int_chat_num);
//                Log.e("HEY", "메인 : 선택된 행의 순서 : " + position);
//                Log.e("HEY", "메인 : ID : " + gomin_items_list.get(position).getID());

                Log.e("메인", "" + gomin_items_list.get(position).getGomin_no() + "번 게시물");

                startActivityForResult(go_post_intent, GO_POST_CODE);
            }
        });


//        upgrade_thread.start();

        // 배경음악 재생하기
        // (참고 : http://mparchive.tistory.com/3)
        // http://kim6kim.tistory.com/1
        // http://jystudynote.tistory.com/entry/Android-MediaPlayer%EC%98%A4%EB%94%94%EC%98%A4-%EA%B8%B0%EB%8A%A5%EC%8B%9C%EC%9E%91%EC%A0%95%EC%A7%80%EC%9D%BC%EC%8B%9C%EC%A0%95%EC%A7%80
        // 1. 미디어 플레이어세팅
        // 2. 스위치가 on 되면, 쓰레드 시작
        // 3. 버튼체크가 해제되면, pause상태
        // 6.- onDestroy()에는 배경음악쓰레드 정지(음악 정지)
        // 7. onUserLeaveHint() 사용자가 홈버튼을 누를 시, 배경음악쓰레드 정지(음악 일시정지)
        // 8. onBackPressed() 백버튼을 눌렀을 때는 배경음악 정지 (음악 정지)

        // 1. 미디어 플레이어세팅
//        mediaPlayer = new MediaPlayer();

       /* musicBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch (curState) {
                        case INITITIALIZED:
                            // MediaPlayer 객체 초기화 , 재생
//                        isPlaying = true; // 쓰레드 반복 하도록
                            mediaPlayer = MediaPlayer.create(
                                    getApplicationContext(), // 현재 화면의 제어권자
                                    R.raw.healing); // 음악파일
                            mediaPlayer.start();
                            mediaPlayer.setLooping(true);
                            Log.e("음악상태", "" + curState);
                            break;
                        case PAUSED:
//                        // 멈춘 지점부터 재시작
                            mediaPlayer.start(); // 시작
//                        isPlaying = true; // 재생하도록 flag 변경
////                        new MyThread().start(); // 쓰레드 시작
                            Log.e("음악상태", "" + curState);
                            break;
                    }
                } else {
//                        // 일시중지
                    mediaPlayer.pause(); // 일시중지
//                        mp.stop(); // 멈춤
//                        isPlaying = false; // 쓰레드 정지
//                        CurState = STOP;
                    curState = PAUSED;
                    Log.e("음악상태", "" + curState);
//                    참고로, 0,1,2의 뜻
//                    final int INIT = 0;
//                    final int RUNNING = 1;
//                    final int PAUSE = 2;
                }

            }
        });*/
    }// OnCreate 끝!

    // 뒤로가기 두번하면, 앱종료시키기
    private long lastTimeBackPressed;

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            // 버튼을 한번 누른 다음, 1.5초 이내로 한 번 더 누르게 되면 앱종료가 됨
            finish();
            return;
        }
        Toast.makeText(this, "'뒤로'버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_write) {
            Intent go_write = new Intent(this,WriteActivity.class);
            go_write.putExtra("닉네임", login_nick);
            startActivityForResult(go_write, ADD_CODE);

        } else if (id == R.id.nav_myInfo) {

        } else if (id == R.id.nav_good) {

        } else if (id == R.id.nav_contact) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // * 글쓰기버튼을 누르면, 글쓰기 화면으로 간다.
        // * 글쓰기액티비티에서 올리기 버튼을 누르면, 글쓴 내용이 메인액티비티 리스트뷰에 저장된다.
        // * 그러기 위해서는! 글쓰기액티비티에서 글내용, 닉네임, 배경사진을 메인액티비티로 가져 온다.
        // 1. 메인액티비티에서 startActivityForResult 를 사용해, 위 내용들을 메인액티비티로 다시 가져오고자한다. (ADD_CODE : 내용추가하라는 지시코드)
        // 2. 글쓰기액티비티에서 setResult 로, 메인액티비티에 보낼 내용을 세팅시킨다. (RESULT_OK : ADD_CODE 지시에 맞춰서 성공적으로 세팅했다!)
        // 3. 메인액티비티에서 onActivityResult 로, RESULT_OK 되고, ADD_CODE 인 내용을 받아온다.
        //    3-1) 고민내용, 닉네임보일지말지, 배경사진 받아옴
        //    3-2) 고민아이템클래스에 내용들을 세팅시킨다. + 고민아이템 고유 번호 가져와서
        //    3-3) 어레이리스트에 저장된 내용들을 추가한다.

        // 3. 메인액티비티에서 onActivityResult 로, RESULT_OK 되고, ADD_CODE 인 내용을 받아온다.
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_CODE) {

                // 3-1) 고민내용, 닉네임보일지말지, 배경사진 받아옴
                gomin_text = data.getStringExtra("고민내용");
                gomin_nick = data.getStringExtra("닉네임");
                gomin_nick_OX = data.getBooleanExtra("닉네임보일지말지", false);
                gomin_background = data.getStringExtra("배경사진");

                // 3-2) 고민아이템클래스에 내용들을 세팅시킨다. + "고민목록" 쉐어드에서 고민아이템의 고유 번호 가져와서
                SharedPreferences SP = getSharedPreferences("고민목록", MODE_PRIVATE);
                SharedPreferences.Editor editor = SP.edit();
                Log.e("고민목록에 ID 값이 저장되어있을까 : ", "" + SP.contains("ID"));

                // 초기에는 키값 "ID"에 내용이 없을 수도 있다. 그런 경우에는 "ID"에 0을 세팅시킨다.
                if (!SP.contains("ID")) {
                    editor.putInt("ID", 0);
                    editor.apply();
                }

                int ID_gomin = SP.getInt("ID", 0);
                ID_gomin = ID_gomin + 1;
                Log.e("1이 추가된 ID값", "" + ID_gomin);

                editor.putInt("ID", ID_gomin);


//                Log.e(Tag, "고민내용 : " + gomin_text);
//                Log.e(Tag, "닉네임 : " + gomin_nick);
//                Log.e(Tag, "닉네임보일지말지 : " + gomin_nick_OX);
//                Log.e(Tag, "배경사진 : " + gomin_background);
//                Log.e(Tag, "int_heart_num : " + int_heart_num);
//                Log.e(Tag, "int_chat_num : " + int_chat_num);
//                Log.e(Tag, "ID_gomin : " + ID_gomin);
//                Log.e(Tag, "login_no : " + login_no);

                GominItem gomin_Item = new GominItem(gomin_text, gomin_nick, gomin_nick_OX, gomin_background, int_heart_num, int_chat_num, ID_gomin,login_no);

                /*쉐어드에 ID를 키로 고민글 저장*/
                Gson gson = new Gson();
                String json = gson.toJson(gomin_Item);
                editor.putString(""+ID_gomin,json);
                Log.e(Tag, "쉐어드 '"+ID_gomin+"'에 추가된 정보 : " + json);
                editor.apply();

                //Log.e(Tag,"고민글번호 : "+a);

                // 3-3) 어레이리스트에 저장된 내용들을 추가한다. (고유 번호도 넣음)
                //gomin_items_list.add(0,GominItem);
                gomin_items_list.add(0,gomin_Item);

               // Gson gson = new Gson();
                for (int i = 0; i < gomin_items_list.size(); i++) {
                    GominItem item = gomin_items_list.get(i);
                    String json2 = gson.toJson(item);
                    Log.e(Tag, "어레이리스트"+i+"번째에 추가된 정보 : " + json2);
                }

                // 아래 한 줄 때문에 자꾸 오류가 나는 건가! (오류 내용 : gomin_item에서 getGomin_no() 못가지고 온다. 널이다.)
                /*GominAdapter.notifyDataSetChanged();*/

            }
        }


    }

    // 배경음악
    // 6. onPause 에는 배경음악쓰레드 정지(음악은 일시정지), onResume 에 배경음악쓰레드 다시 재생(음악 다시 재생) v
    // 7. onUserLeaveHint() 사용자가 홈버튼을 누를 시, 배경음악쓰레드 정지(음악 일시정지)
    // 8. onBackPressed() 백버튼을 눌렀을 때는 배경음악 정지 (음악 정지)

    @Override
    protected void onPause() {
        super.onPause();
       /* Log.e(Tag, "메인 : onPause() : 배경음악 상태"+curState);*/

        // 현재 어레이리스트 내용들을 쉐어드에 저장시킨다.
        // 1. "고민목록" 쉐어드 파일을 연다.
        // 2. 에디터 객체를 만들어, 쉐어드에 내용을 추가,수정,삭제,저장할 수 있게 한다.
        // 3. 고유값 id은 꺼내서 "ID" 쉐어드에 저장시켜놓고,
//         4. 쉐어드에 저장된 내용들 다 삭제시킨다
        // 5. 쉐어드에 어레이리스트 순서를 키로 두고, 순서에 해당하는 각각의 내용들을 저장한다.
        // ( + 각각의 포스트에 고유값 id를 부여한다. 게시판처럼)
        // - gson 객체를 만든다.
        // - 어레이리스트 내용들을 gson을 사용해서 json형식으로 바꾼다.
        // - 어레이리스트 순서를 키로 두고, json형식으로 바꾼 내용들을 쉐어드에 저장시킨다.
        // 6. * "ID"쉐어드에서 ID 값을 가져와서 "고민목록" 쉐어드에 저장시킨다.

        // 1. "고민목록" 쉐어드 파일을 연다.
        SharedPreferences SP_gomin = getSharedPreferences("고민목록", MODE_PRIVATE);
        // 2. 에디터 객체를 만들어, 쉐어드에 내용을 추가,수정,삭제,저장할 수 있게 한다.
        SharedPreferences.Editor editor = SP_gomin.edit();

        // 3. 고유값 id은 꺼내서 "ID" 쉐어드에 저장시켜놓고,
        int ID_int = SP_gomin.getInt("ID", 0);
        SharedPreferences SP_ID = getSharedPreferences("ID", MODE_PRIVATE);
        SharedPreferences.Editor ID_editor = SP_ID.edit();
        ID_editor.putInt("ID", ID_int);
        //Log.e("ID 쉐어드에 저장된 ID값", "" + ID_int);
        ID_editor.apply();

        // 4. 쉐어드에 저장된 내용들 다 삭제시킨다
        editor.clear();

        // 5. 쉐어드에 어레이리스트에서 가져온 고민넘버를 키로 두고, 순서에 해당하는 각각의 내용들을 다시 저장한다.
        Gson gson = new Gson();

        for (int i = 0; i < gomin_items_list.size(); i++) {
            GominItem item = gomin_items_list.get(i);
            //int post_no = item.getGomin_no();
            String json = gson.toJson(item);
            editor.putString(String.valueOf(gomin_items_list.get(i).getGomin_no()), json);
        }
        gomin_items_list.clear();
        Gson gson1 = new Gson();

        // 쉐어드에 있는 내용들을 어레이리스트에 다시 추가한다.
        if(SP_gomin.contains("ID")){
            Log.e(Tag,"현재 ID : "+SP_gomin.getInt("ID",0));
            int gomin_size = SP_gomin.getInt("ID",0);
            for(int i = 0; i<= gomin_size; i++){
                if(!SP_gomin.getString(String.valueOf(i),"").equals("")){
                    String json1 = SP_gomin.getString(String.valueOf(i),"");
                    GominItem item1 = gson1.fromJson(json1, GominItem.class);
                    gomin_items_list.add(0,item1);

                    // 댓글 쉐어드 가져오기
                    SharedPreferences SP_chat = getSharedPreferences("댓글" + i, MODE_PRIVATE);
                    SharedPreferences.Editor editor_chat = SP_chat.edit();
                    //Log.e(Tag,i+"번 글의 댓글가져오기 :"+ SP_chat.getAll());

                }
            }
        }
        //Log.e(Tag,"고민목록 : "+ SP_gomin.getAll());


        // 아래 한 줄 때문에 자꾸 오류가 나는 건가! (오류 내용 : gomin_item에서 getGomin_no() 못가지고 온다. 널이다.)
        gomin_adapter.notifyDataSetChanged();

        // 쉐어드에 잘 저장되었나 확인하기!
/*        for (int i = 0; i < gomin_items_list.size(); i++) {
            Log.e(Tag, "메인 : onPause() - 쉐어드에 저장된 내용들 : " + SP_gomin.getString(valueOf(i), ""));
        }*/

        // 6. * "ID"쉐어드에서 ID 값을 가져와서 "고민목록" 쉐어드에 저장시킨다.
        editor.putInt("ID", SP_ID.getInt("ID", 0));
        //Log.e(Tag,"ID 값을 가져와서 고민목록 쉐어드에 저장 // 현재 ID : "+SP_gomin.getInt("ID",0));
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
       /* Log.e(Tag, "메인 : onStart() : 배경음악 상태"+curState);*/

    }

    // 배경음악) 6. onResume 에 배경음악쓰레드 다시 재생(음악 다시 재생)
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(Tag, "onResume()");

        // 현재 list 내용들 삭제시킴
        gomin_items_list.clear();

        Gson gson1 = new Gson();
        // 고민목록에 있는 내용들을 불러와 list에 세팅한다.
        SharedPreferences SP_gomin = getSharedPreferences("고민목록", MODE_PRIVATE);
        if(SP_gomin.contains("ID")){
            Log.e(Tag,"현재 ID : "+SP_gomin.getInt("ID",0));
            int gomin_size = SP_gomin.getInt("ID",0);
            for(int i = 0; i<= gomin_size; i++){
                if(!SP_gomin.getString(String.valueOf(i),"").equals("")){
                    String json1 = SP_gomin.getString(String.valueOf(i),"");
                    GominItem item1 = gson1.fromJson(json1, GominItem.class);
                    gomin_items_list.add(0,item1);
                }
            }
        }

        gomin_adapter.notifyDataSetChanged();

    }

    @Override
    protected void onStop() {
        super.onStop();

        /*Log.e(Tag, "메인 : onStop() : 배경음악 상태"+curState);*/
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        /*Log.e(Tag, "메인 : onRestart() : 배경음악 상태"+curState);*/
//        if(curState == PAUSED){
//            mediaPlayer.start();
//        }
    }

    // 배경음악) 6. onDestroy()에는 배경음악쓰레드 정지(음악 정지)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(Tag, "메인 : onDestroy()");
    }

    // 배경음악) 7. onUserLeaveHint() 사용자가 홈버튼을 누를 시, 배경음악쓰레드 정지(음악 일시정지)

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
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


}

