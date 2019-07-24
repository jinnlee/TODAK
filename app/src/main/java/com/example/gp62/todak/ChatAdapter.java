package com.example.gp62.todak;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;

import java.util.ArrayList;
public class ChatAdapter extends BaseAdapter {

    TextView chat_nick; // 댓글 내 닉네임 텍스트뷰
    TextView chat_text; // 댓글 내용 텍스트뷰
    EditText chat_edit; // 댓글 수정시 나오는 에디트텍스트
    Button Btn_chat_modify; // 댓글 수정시 나오는 수정하기 버튼
    ImageView heart; // 댓글 내 좋아요 버튼
    TextView heart_num; // 댓글 내 좋아요개수가 보이는 텍스트뷰
    Context context;
    TextView reply; // 답글쓸 수 있는 버튼 (답글 버튼)
    TextView modify; // 댓글 수정버튼
    TextView bar; // 댓글 수정과 삭제 사이에 있는 바
    TextView delete; // 댓글 삭제버튼
    TextView report; // 댓글 신고하기버튼
    TextView chat_num; // 댓글 개수가 보이는 텍스트뷰

    int gomin_no; // 고민글 고유번호
    SwipeMenuListView chat_listview;
    ArrayList<ChatItem> chat_items_list; // 댓글 목록에 보여질 데이터 모음
   // int gomin_list_position;// 몇 번째 게시물인지

    String Tag = "ChatAdapter";

    //int chat_no;

    public ChatAdapter(Context context, ArrayList chat_items_list, int gomin_no, SwipeMenuListView chat_listview, TextView chat_num) {
        this.context = context;
       // this.gomin_list_position = gomin_list_position;
        this.chat_items_list = chat_items_list;
        this.gomin_no = gomin_no;
        this.chat_listview = chat_listview;
        this.chat_num = chat_num;
    }

    @Override
    public int getCount() {
        return chat_items_list.size();
    }

    @Override
    public Object getItem(int position) {
        return chat_items_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // * getView
        // 1. 화면에서 보이지 않던 리스트뷰 한 행이 보이게 되면, 인플레이터로 그려준다. (메모리에 올려준다 = 실제로 만들어 준다)
        // 2. xml 의 뷰와 java 변수와 연결시킨다 (겉 껍데기과 속 알멩이를 연결시킨다) -> 연결만 한다.
        // 3. 겉모습에 실질적인 내용을 세팅시킨다.

        // 1
        // - context 로 레이아웃을 그려주는 레이아웃인플레이터가 안드로이드의 화면그릴 수 있게 권한을 허용한다.
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.chat_item, parent, false);
        }

        // 2
        chat_nick = (TextView) convertView.findViewById(R.id.chat_set_nick);
        chat_text = (TextView) convertView.findViewById(R.id.chat_text);
        heart_num = (TextView) convertView.findViewById(R.id.heart_num);
        reply = (TextView) convertView.findViewById(R.id.reply);
//        modify = (TextView) convertView.findViewById(R.id.modify);
//        bar = (TextView) convertView.findViewById(R.id.bar);
        chat_edit = (EditText) convertView.findViewById(R.id.chat_edit);
        Btn_chat_modify = (Button) convertView.findViewById(R.id.Btn_chat_modify);
//        delete = (TextView) convertView.findViewById(R.id.delete);
        report = (TextView) convertView.findViewById(R.id.report);
        heart = (ImageView) convertView.findViewById(R.id.heart);

        // 3
        final ChatItem chat_item = chat_items_list.get(position);
        chat_text.setText(chat_item.getChat_text());

        // * 댓글을 달 때, 자기 글인 경우와 다른 사람의 글인 경우로 나뉜다!
        // 1. 자기 글에 댓글을 다는 경우
        //   1) 닉네임이 적히는 자리에, 닉네임과 '(글쓴이)'가 나온다.
        //   (만약, 글쓴이가 작성한 글에 닉네임가리기를 한 경우, 닉네임은 안나오고, '글쓴이'만 나온다)
        // 2. 다른 사람글에 댓글을 다는 경우
        //   1) 닉네임이 적히는 자리에, 닉네임만 나온다.

        // * 먼저, 글 작성자가 누군지, 작성된 글에서 글 작성자의 닉네임이 보이는지 안보이는지 확인한다.
        // 1. "고민목록" 쉐어드 파일을 연다
        // 2. gson 객체를 만든다.
        // 3. 쉐어드에서 해당 순서에 맞는 어레이리스트 내용들을 가져온다.
        // 4. gson을 사용해서, json형식으로 저장된 내용들을 gomin_item클래스의 객체로 바꾼다.
        // 5. 닉네임과 닉네임보일지말지 유무 가져온다.
        // 6. 닉네임 보이기의 경우) 댓글을 다는 회원(로그인한 회원)의 닉네임과 같은지 비교해서
        //   같으면 닉네임 옆에 (글쓴이) 가 붙고, 다르면 닉네임만 나온다.
        // 7. 닉네임 가리기의 경우) 댓글을 다는 회원(로그인한 회원)의 닉네임과 같은지 비교해서
        //   같으면 글쓴이만 나오고, 다르면 닉네임만 나온다.

        SharedPreferences SP = context.getSharedPreferences("로그인한회원", context.MODE_PRIVATE);
        String json_login = SP.getString("로그인한회원", "");
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(json_login, UserInfo.class);
        int login_no = userInfo.getUser_no(); // 로그인한 회원의 고유번호
        /*Log.e(Tag,"로그인한 회원의 고유번호 : "+login_no);*/

        String login_nick = userInfo.getNickname(); // 로그인한 회원의 닉네임

        // * 댓글을 달 때, 자기 글인 경우와 다른 사람의 글인 경우로 나뉜다!
        // 1
        SharedPreferences SP_gomin = context.getSharedPreferences("고민목록", context.MODE_PRIVATE);
        // 2
        Gson gson1 = new Gson();
        // 3
        //SP_gomin.getString(String.valueOf(gomin_no), "").equals("");
        //Log.e(Tag,"고민목록 순서 : "+gomin_no);

        // 4
        String json = SP_gomin.getString(""+gomin_no, "");
        GominItem gomin_item1 = gson1.fromJson(json, GominItem.class);
        Log.e(Tag,"글아이템 : "+json);

        // 5
        int writer_no = gomin_item1.getUser_no(); // 글 작성자 고유번호

        /*Log.e(Tag,"글작성자 고유번호 : "+writer_no);*/
        String gomin_writer = gomin_item1.getNick(); // 글 작성자 닉네임
        Boolean gomin_nick_showOrNot = gomin_item1.getNick_OX(); // 닉네임보일지말지

        int chat_writer_no = chat_item.getChat_user_no(); // 댓글 작성자 고유번호
        String chat_writer = chat_item.getChat_nick(); //댓글 작성자 닉네임

        // 6. 닉네임 보이기의 경우) 댓글을 다는 회원(로그인한 회원)이 글쓴이의 고유번호와 같은지 비교해서
        //   같으면 닉네임 옆에 (글쓴이) 가 붙고, 다르면 닉네임만 나온다.
        /*Log.e(Tag,"닉네임보이기 : "+gomin_nick_showOrNot);*/

        if(gomin_nick_showOrNot){
            if(chat_writer_no==writer_no){
                chat_nick.setText(chat_writer+" (글쓴이)");
            }else{
                chat_nick.setText(chat_writer);
            }
        // 7. 닉네임 가리기의 경우) 댓글을 다는 회원(로그인한 회원)의 닉네임과 같은지 비교해서
        //   같으면 글쓴이만 나오고, 다르면 닉네임만 나온다.

        }else {
            if(chat_writer_no==writer_no){
                chat_nick.setText("글쓴이");
            }else{
                chat_nick.setText(chat_writer);
            }
        }

        notifyDataSetChanged();
//        heart_checkbox 보류
//        heart_num.setText(item.getHeart_num());


        // 스와이프리스트메뉴
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
//                // 수정 아이템
//                SwipeMenuItem modifyItem = new SwipeMenuItem(getApplicationContext());
//                // 아이템의 배경화면 세팅하기
//                modifyItem.setBackground(new ColorDrawable(Color.DKGRAY));
//                // 아이템 높이
//                modifyItem.setWidth(150);
//                // 아이템 이름
//                modifyItem.setTitle("수정");
//                // 아이템 이름폰트사이즈
//                modifyItem.setTitleSize(15);
//                // 아이템 이름 색깔
//                modifyItem.setTitleColor(Color.WHITE);
//
//                // 메뉴추가하기
//                menu.addMenuItem(modifyItem);

                // 삭제 아이템
                SwipeMenuItem deleteItem = new SwipeMenuItem(context);
                // 아이템의 배경화면 세팅하기
                deleteItem.setBackground(new ColorDrawable(Color.YELLOW));
                // 아이템 높이
                deleteItem.setWidth(150);
                // 아이콘세팅
                deleteItem.setIcon(R.drawable.ic_delete_black_24dp);
                // 메뉴추가하기
                menu.addMenuItem(deleteItem);
            }
        };

        // * 자기 댓글인 경우, 다른 사람 댓글인 경우로 나뉜다!
        // 1. 내가 쓴 댓글인 경우!
        //   1) 댓글달기버튼이 안보인다.
        //   2) 수정, 삭제 버튼이 보인다. (신고버튼은 안보인다)
        // 2. 다른 사람이 쓴 댓글인 경우!
        //   1) 댓글달기버튼이 보인다.
        //   2) 신고버튼이 보인다. (수정, 삭제 버튼은 안보인다)

        // - 댓글 작성자와 현재 로그인한 회원과 비교해서
        // - 1. 같으면,
        //   1) 답글을 쓸 수 있는 버튼이 안보인다.
        //   2) 수정, 삭제 버튼이 보인다. (신고버튼은 안보인다)
        // - 2. 다르면,
        //   1) 답글을 쓸 수 있는 버튼이 보인다.
        //   2) 신고버튼이 보인다. (수정, 삭제 버튼은 안보인다)

        // * 작성자와 로그인한 회원이 같은 경우, 메뉴버튼(글을 수정,삭제할 수 있는 버튼)이 보이게하고
        //   다른 경우, 안보이게 하기!

        /*SharedPreferences SP = context.getSharedPreferences("로그인한회원", context.MODE_PRIVATE);
        String json_login = SP.getString("로그인한회원", "");
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(json_login, UserInfo.class);
        int login_no = userInfo.getUser_no(); // 로그인한 회원의 고유번호*/


        // 1
        if(chat_writer_no==login_no){
            // 1)
            reply.setVisibility(View.GONE);
            // 2)
//            modify.setVisibility(View.VISIBLE);
//            bar.setVisibility(View.VISIBLE);
//            delete.setVisibility(View.VISIBLE);
            report.setVisibility(View.GONE);

            //  * 댓글작성자와 로그인한 회원이 같은 경우, 메뉴버튼(댓글을 삭제할 수 있는 버튼)이 보이게하고
            //   다른 경우, 안보이게 하기!
            notifyDataSetChanged();
            chat_listview.setMenuCreator(creator);



        // 2
        }else {
            // 1)
            reply.setVisibility(View.GONE);
//            reply.setVisibility(View.VISIBLE);
            // 2)
//            modify.setVisibility(View.GONE);
//            bar.setVisibility(View.GONE);
//            delete.setVisibility(View.GONE);
            report.setVisibility(View.GONE);
//            report.setVisibility(View.VISIBLE);
            chat_listview.setMenuCreator(null);
            notifyDataSetChanged();
        }

        // 스와이프시 만들어지는 메뉴 (수정, 삭제) 각각을 클릭했을 때, 나타나는 반응!
        // 현재 삭제 버튼만 나옴!
        chat_listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
//                    case 0:
//                        // 수정버튼
//                        Toast.makeText(getApplicationContext(), "수정하기", Toast.LENGTH_SHORT);
//                        break;
                    case 0:
                        // 삭제버튼
                        Toast.makeText(context, "댓글이 삭제되었습니다", Toast.LENGTH_SHORT);
                        chat_items_list.remove(position);
                        notifyDataSetChanged();
                        chat_num.setText(""+chat_items_list.size());

                        // 댓글 쉐어드에서도 삭제시키기!
                        SharedPreferences SP_chat = context.getSharedPreferences("댓글" +gomin_no,context.MODE_PRIVATE);
                        Log.e(Tag,gomin_no+"번의" +position+"번째 댓글삭제 : ");
                        SharedPreferences.Editor editor_chat = SP_chat.edit();
                        editor_chat.remove(""+position);
                        editor_chat.apply();

                        break;
                }

                // false는 메뉴를 닫고, true는 메뉴를 닫지 않음
                return false;
            }
        });

        // * 참고로, 댓글 추가는 PostActivity 에서 함!)
        // * < 댓글 수정, 삭제하기 >

        // * 댓글 삭제하기
        // 1. chat_items_list 에서 클릭된 행 삭제하기!
        // 2. 새로고침하기!
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 1. chat_items_list 에서 클릭된 행 삭제하기!
//                chat_items_list.remove(position);
//                // 2. 새로고침하기!
//                notifyDataSetChanged();
//            }
//        });

        // * 댓글 수정하기 (다른건 안 바뀌고, 댓글 내용만 바뀌게 한다.)
        // 1. 수정버튼을 클릭하면, 댓글텍스트뷰가 사라지고, 댓글수정창(에디트텍스트)과 수정하기 버튼이 나온다.
        // 2. 댓글수정창에 댓글내용을 세팅한다.
        // 3. 댓글입력창에 댓글을 수정하고, 수정하기 버튼을 누르면, 수정된다.
        // 4. 그리고 댓글수정창과 수정하기버튼은 사라지고, 댓글텍스트뷰가 다시 나온다.

//        modify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 1
//                // 이 부분이 세팅될 때, 이상한 위치에 세팅됨
//
//                chat_text.setVisibility(View.GONE);
//                chat_edit.setVisibility(View.VISIBLE);
//                Btn_chat_modify.setVisibility(View.VISIBLE);
//                Log.e("HEY","댓글 순서 : "+position);
//
//                // 2
//                chat_edit.setText(chat_text.getText());
//            }
//        });

        // 3
        Btn_chat_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat_item.setChat_text(chat_edit.getText().toString());
//                ChatItem item = new ChatItem(chat_writer, chat_edit.getText().toString(), 'x', 0);
                Log.e(Tag,"댓글 순서 : "+position);
                chat_items_list.set(position,chat_item);
//                notifyDataSetChanged();
//                Log.e("수정하고있나요?","네");

                // 4
                chat_text.setVisibility(View.VISIBLE);
                chat_edit.setVisibility(View.GONE);
                Btn_chat_modify.setVisibility(View.GONE);
//                Log.e("세팅되고있나요?","네");

                notifyDataSetChanged();
            }

        });

        return convertView;
    }
}
