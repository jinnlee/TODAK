package com.example.gp62.todak;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static java.lang.String.valueOf;

public class GominAdapter extends BaseAdapter {

    Context context;
    ArrayList<GominItem> gomin_items_list;
    ArrayList<String> like_user_list = new ArrayList<>();

    String Tag = "GominAdapter";

    public GominAdapter(Context context, ArrayList<GominItem> gomin_items_list) {
        this.context = context;
        this.gomin_items_list = gomin_items_list;
    }

    @Override
    public int getCount() {
        return gomin_items_list.size();
    }

    @Override
    public Object getItem(int position) {
        return gomin_items_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView writeText;
        CheckBox heart_checkbox;
        TextView heart_num;
        TextView writer;
        TextView chat_num;
        RelativeLayout gomin_item_background;
        ImageView popup_heart = null;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //
        View itemLayout = convertView;
        //
        //리스너에서 사용할 포지션 변수 (그냥 position 값을 넣으면 컴파일 에러)
        final int checkBoxPosition = position;

        final ViewHolder holder;


        if (itemLayout == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemLayout = layoutInflater.inflate(R.layout.gomin_ltem, parent, false);

            holder = new ViewHolder();

            holder.writeText = (TextView) itemLayout.findViewById(R.id.writeText);
            holder.writer = (TextView) itemLayout.findViewById(R.id.writer);
            holder.heart_checkbox = (CheckBox) itemLayout.findViewById(R.id.heart);
            holder.heart_num = (TextView) itemLayout.findViewById(R.id.heart_num);
            holder.chat_num = (TextView) itemLayout.findViewById(R.id.chat_num);
            holder.gomin_item_background = (RelativeLayout) itemLayout.findViewById(R.id.gomin_item_background);
            holder.popup_heart = (ImageView) itemLayout.findViewById(R.id.popup_heart);

            itemLayout.setTag(holder);

            //뷰홀더가 정의되어 있을경우는
        } else {
            holder = (ViewHolder) itemLayout.getTag();
        }

//        GominItem item = gomin_items_list.get(checkBoxPosition);
        // 데이터를 셋팅한다.
        //viewHolder._id.setText(mData.get(position).phoneId);


        final Gson gson = new Gson();
        // 로그인한유저 쉐어드
        SharedPreferences SP_login = context.getSharedPreferences("로그인한회원", Context.MODE_PRIVATE);
        String json = SP_login.getString("로그인한회원", "");
//        UserInfo item = gson.fromJson(Login_user_info, UserInfo.class);
        UserInfo user_item = gson.fromJson(json, UserInfo.class);
        //String login_nick = user_item.getNickname();
        final int login_no = user_item.getUser_no();

        // 로그인한 유저의 좋아요 쉐어드
        /*Log.e(Tag, "position : " + position);*/
        // 댓글쉐어드
        final int gomin_item_position = gomin_items_list.get(position).getGomin_no();
        SharedPreferences SP_chat = context.getSharedPreferences("댓글" + gomin_item_position, Context.MODE_PRIVATE);


        // 1)고민글 세팅
        holder.writeText.setText(gomin_items_list.get(position).getGomin());

        // 2)닉네임보일지말지여부로 닉네임 세팅
        if (gomin_items_list.get(position).getNick_OX() == true) {
            holder.writer.setText(gomin_items_list.get(position).getNick());
        } else {
            holder.writer.setText("누군가");
        }

        // 3)배경사진 세팅
        Uri myUri = Uri.parse(gomin_items_list.get(position).getBackground());
        if (myUri != null && !myUri.toString().equals("")) {
//            Log.e("체크", "배경사진 셋팅 완료" + myUri);
            holder.gomin_item_background.setBackground(uriToDrawable(myUri));
        } else {
            Drawable drawable = context.getDrawable(R.drawable.background4);
            holder.gomin_item_background.setBackground(drawable);
        }

        // 4) 좋아요체크유무 : 모든 체크박스의 체크를 false 시키고, 수동으로 다시 확인하는 것
//        holder.heart_checkbox.setChecked(false);
//        holder.heart_checkbox.setChecked(gomin_items_list.get(position).heart_isChecked);
//        holder.heart_num.setText(""+0);

        // 5) 좋아요개수 세팅
        // 좋아요한유저 + 고민글no
        SharedPreferences SP_like = context.getSharedPreferences("좋아요한유저"+gomin_item_position, Context.MODE_PRIVATE);
       // Log.e(Tag,gomin_item_position+"번 글의 좋아요 유저 리스트 : "+SP_like.getAll());
        holder.heart_num.setText("" + SP_like.getAll().size());

        // 6) 댓글개수
        holder.chat_num.setText("" + SP_chat.getAll().size());


        // 좋아요 유무 세팅
        // 만약 현재 글에 유저가 좋아요를 했다면 체크하기
        if(SP_like.contains(""+login_no)){
            holder.heart_checkbox.setChecked(true);
        }else{
            // 좋아요체크를 하지 않았다면 체크안하기
            holder.heart_checkbox.setChecked(false);
        }

        // 애니메이션 넣기
        final Animation anim_popup_heart = AnimationUtils.loadAnimation(context, R.anim.anim_heart);
        // 좋아요체크
        holder.heart_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 유저가 좋아요를 누른경우, 좋아요체크 + 좋아요개수추가하기
                if(holder.heart_checkbox.isChecked()){
                    int int_heart_num = add_Like_user_list(""+gomin_item_position,login_no);
                    holder.heart_num.setText(String.valueOf(int_heart_num));

                    // * 팝업하트 애니메이션
                    holder.popup_heart.setVisibility(View.VISIBLE);
                    holder.popup_heart.startAnimation(anim_popup_heart);

                    // 회전하기
                    // http://android-coding.blogspot.kr/2015/10/interactive-flip-imageview-using.html
                    ObjectAnimator flip = ObjectAnimator.ofFloat(holder.popup_heart, "rotationY", 0f, 180f);
                    flip.setDuration(1000);
                    flip.start();
                    holder.popup_heart.setVisibility(View.INVISIBLE);

                }else {
                    //
                    int int_heart_num = delete_Like_user_list(""+gomin_item_position,login_no);
                    holder.heart_num.setText(String.valueOf(int_heart_num));
                }
            }
        });

        //값이 다 셋팅된 view 객체를 반환한다.
        return itemLayout;
    }

    //URI에서 Drawable로
    public Drawable uriToDrawable(Uri yourUri) {
        Drawable yourDrawable = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(yourUri);
            yourDrawable = Drawable.createFromStream(inputStream, yourUri.toString());
        } catch (FileNotFoundException e) {
            // yourDrawable = getResources().getDrawable(R.drawable.default_image);
        }
        return yourDrawable;
    }


    // 유저가 좋아요한 경우, '좋아요유저'쉐어드에 추가
    // 고민글no, 유저no
    // 현재 글에 좋아요한 유저 수를 출력한다.
    public int add_Like_user_list(String gomin_no, int user_no) {

        // '좋아요유저'+ 고민글no로 쉐어드를 불러온다.
        SharedPreferences SP = context.getSharedPreferences("좋아요한유저"+gomin_no, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SP.edit();
        // key를 좋아요한 유저 no로 저장한다.
        if(!SP.contains(""+user_no)){
            editor.putString(""+user_no,"");
        }
        editor.apply();
        Log.e(Tag,"좋아요한 유저 추가");
        return SP.getAll().size();
    }

    // 유저가 좋아요를 "취소"한 경우, '좋아요유저'쉐어드에서 유저 삭제
    // 고민글no, 유저no
    // 현재 글에 좋아요한 유저 수를 출력한다.
    public int delete_Like_user_list(String gomin_no, int user_no) {
        // '좋아요유저'+ 고민글no로 쉐어드를 불러온다.
        SharedPreferences SP = context.getSharedPreferences("좋아요한유저"+gomin_no, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SP.edit();

        // 현재 유저의 좋아요한 내역을 삭제한다.
        if(SP.contains(""+user_no)){
            editor.remove(""+user_no);
        }
        editor.apply();
        Log.e(Tag,"좋아요한 유저 삭제");
        return SP.getAll().size();
    }
}
