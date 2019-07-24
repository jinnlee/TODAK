package com.example.gp62.todak;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteActivity extends AppCompatActivity {

    // 고민글 등록화면

    // 글쓰기 화면으로, 고민내용을 적고, 닉네임보이기/가리기로 닉네임을 보이거나 가리게 할 수 있고, 배경설정을 할 수 있는 화면이다.
    //* 고민내용 : 화면 중단부에 사용자의 고민을 적을 수 있게 했다.
    //* 닉네임 : 화면 하단부에 보면, 닉네임보이기와 닉네임가리기를 설정할 수 있게 했다.
    //           닉네임보이기로 설정해놓으면, 메인화면에 닉네임이 나오게 했고, 닉네임가리기로 설정해놓으면, 누군가로 뜨게 했다.
    //* 배경설정 : 배경설정버튼을 누르면, 다이얼로그가 뜬다.
    //             다이얼로그에는 '카메라'와 '앨범'을 통해 사진을 가져올 수 있게 했고, 가져온 사진은 글쓰기화면 전체 배경으로 세팅된다.



    String Tag = "WriteActivity";

    Uri Image_uri = null;
    // 카메라 또는 앨범에서 가져온 URI를 저장하기 위해서 만들어주었다.

    TextView back;
    //화면 상단부 맨 왼쪽에 있는 뒤로가기버튼으로 누르게 되면 메인화면으로 간다.

    TextView ok;
    //화면 상단부 맨 오른쪽에 있는 글올리기버튼.
    //누르게 되면 메인화면으로 가며, 사용자가 글쓰거나 설정한 내용들(고민내용, 닉네임보이기/가리기, 배경설정)이 메인화면의 리스트뷰에 세팅된다.
    //주의사항 : 고민내용, 닉네임보이기/가리기, 배경설정이 나오는지 확인!

    EditText edit_gomin;
    //글쓰기 화면 중단부에 사용자가 글을 쓸 수 있게 했다.

    TextView set_background;
    // 글쓰기 화면의 하단부 맨 오른쪽에 있는 텍스트뷰로, 배경을 세팅할 수 있다. 누르면, 다이얼로그가 뜬다.
    // 다이얼로그에서 카메라, 앨범 중 어디에서 사진을 가져올건지 정할 수 있다.

    TextView set_nick;
    // 글쓰기 화면 하단부에 배경설정 오른쪽에 있는 텍스트뷰로, 닉네임을 보일지 가릴지 설정할 수 있다.
    // 닉네임보이기 : 메인화면 리스트뷰의 한 행에 사용자의 닉네임이 보이게 된다.
    // 닉네임가리기 : 메인화면 리스트뷰의 한 행에 사용자의 닉네임 대신 '누군가'가 적힌다.

    LinearLayout write_background;
    // 카메라 또는 앨범에서 가져온 사진을 리니어레이아웃의 배경으로 설정하기 위해서 리니어레이아웃을 만들어준다.

    int CAMERA_CODE = 1010;
    // 카메라로 찍은 사진을 글쓰기 화면으로 가져오는데 필요한 코드.

    int ALBUM_CODE = 1111;
    // 앨범에서 선택한 사진을 글쓰기 화면으로 가져오는데 필요한 코드.

    int MODIFY_CODE = 3333;
    int CROP_FROM_iMAGE = 1234;
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 3434;

    int WRITE_CODE = 0;

    String gomin = "";
    String background = "";
    Boolean nick_OX = false;
    String nick = "";

    int listView_position; // 선택된 행순서

    String mCurrentPhotoPath;

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
        setContentView(R.layout.activity_write);
        Log.e(Tag, "onCreate");

        back = (TextView) findViewById(R.id.back);
        ok = (TextView) findViewById(R.id.ok);
        set_background = (TextView) findViewById(R.id.set_background);
        set_nick = (TextView) findViewById(R.id.set_nick);
        write_background = (LinearLayout) findViewById(R.id.write_background);
        edit_gomin = (EditText) findViewById(R.id.edit_gomin);
        back.setOnClickListener(WriteAct_Text);
        ok.setOnClickListener(WriteAct_Text);
        set_background.setOnClickListener(WriteAct_Text);
        set_nick.setOnClickListener(WriteAct_Text);
        nick = getIntent().getStringExtra("닉네임");

        // 퍼미션
        // WRITE_EXTERNAL_STORAGE 퍼미션 이게 꼭 있어야 사진이 배경화면에 세팅된다!
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            // 권한 없음
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            //Toast.makeText(getApplicationContext(),"권한없음",Toast.LENGTH_SHORT).show();
        }


        // 수정화면인 경우
        // 한 화면이 등록화면도 되고 수정화면도 된다.
        // 만약, '항목'이 '수정해줘'라면, 수정화면으로 세팅하기
        // 수정화면에 세팅할 내용들 보내기 (고민내용, 닉네임보일지말지, 배경사진)
        if (getIntent().getStringExtra("항목").equals("수정해줘")) {

            gomin = getIntent().getStringExtra("고민내용");
            nick_OX = getIntent().getBooleanExtra("닉네임보일지말지", false);
            background = getIntent().getStringExtra("배경사진");

            //고민내용 세팅하기
            edit_gomin.setText(gomin);

            //닉네임보일지말지 세팅하기
            if (nick_OX == false) {
                set_nick.setText("닉네임가리기");
            } else {
                set_nick.setText("닉네임보이기");
            }

            // 배경사진 세팅하기
            if (background != null && !background.equals("")) {
                Image_uri = Uri.parse(background);
//            gomin_item_background.setBackground(uriToDrawable(myUri));
                write_background.setBackground(uriToDrawable(Image_uri));
            }
//            Drawable test = ContextCompat.getDrawable(getApplicationContext(),R.drawable.background1);

            Log.e("수정", "수정화면 : 고민내용 : " + gomin);
            Log.e("수정", "수정화면 : 배경사진 : " + background);

        }

        // 고민을 적는 칸에 주의점을 힌트로 넣었다.
        edit_gomin.setHint(nick + "님의 마음속 이야기를 들려주세요 :)" +
                "\n\n* 홍보, 욕설, 음란성, 불쾌감 조성 및 개인 정보 요청은 삭제될 수 있으며, 정도에 따라 영구 탈퇴 될 수 있습니다.");
    }


    TextView.OnClickListener WriteAct_Text = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                // 뒤로가기버튼을 누르면, 메인화면으로 간다. 그리고 현재 화면은 사라진다.
                case R.id.back:
                    finish();
                    break;



                // 배경설정버튼을 누르면, 배경(사진)을 가져올 다이얼로그가 뜬다. 카메라, 앨범 중 하나를 선택해서 사진을 가져올 수 있다.
                case R.id.set_background:
                    CharSequence[] items = {"카메라", "앨범"};

                    AlertDialog.Builder dialogBuilder_picture = new AlertDialog.Builder(WriteActivity.this);
                    dialogBuilder_picture.setTitle("사진 가져오기");
                    dialogBuilder_picture.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case 0:
                                    // 카메라
                                    doTakePhotoAction();
                                    break;

                                case 1:
                                    // 앨범
                                    doTakeAlbumAction();
                                    break;
                            }
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog alertDialog = dialogBuilder_picture.create();
                    alertDialog.show();
                    break;




                // 닉네임보이기 / 닉네임가리기를 나타내는 텍스트뷰로
                // 1. 닉네임보이기를 클릭하면, 닉네임가리기로 바뀌고, 토스트에 '닉네임이 보이지 않습니다'가 나온다.
                // 2. 반대로, 닉네임가리기 텍스트를 클릭하면, 닉네임보이기로 바뀌고, 토스트에 '닉네임이 보입니다'가 나온다.
                case R.id.set_nick:

                    // 1. 텍스트뷰에 닉네임보이기가 세팅되어있는 상황
                    if (set_nick.getText().equals("닉네임보이기")) {
                        set_nick.setText("닉네임가리기");
                        Toast.makeText(getApplicationContext(), "닉네임이 보이지 않습니다", Toast.LENGTH_SHORT).show();

                    } else // 2. 텍스트뷰에 닉네임가리기가 세팅되어있는 상황
                    {
                        set_nick.setText("닉네임보이기");
                        Toast.makeText(getApplicationContext(), "닉네임이 보입니다", Toast.LENGTH_SHORT).show();
                    }
                    break;


                case R.id.ok: // 고민글을 수정하거나 추가한다.

                    // 현재 화면이 수정화면인 경우,
                    if (getIntent().getStringExtra("항목").equals("수정해줘")) {
                        Intent go_POST = new Intent(WriteActivity.this, PostActivity.class);
                        go_POST.putExtra("항목", "수정끝");

                        String gomin = edit_gomin.getText().toString();

                        if (gomin.length() > 0) {
                            go_POST.putExtra("고민내용", gomin);
                        } else {
                            Toast.makeText(getApplicationContext(), "고민내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (set_nick.getText().equals("닉네임보이기")) {
                            go_POST.putExtra("닉네임보일지말지", true);
                            nick_OX = true;
                        } else {
                            go_POST.putExtra("닉네임보일지말지", false);
                            nick_OX = false;
                        }

                        // 가져올 사진(배경)이 있다면, 인텐트를 통해 이미지를 가져올 수 있는 경로를 메인화면으로 보낸다.
                        // 없다면,""을 보낸다.
                        if (Image_uri != null) {
                            go_POST.putExtra("배경사진", "" + Image_uri);
                        } else {
                            go_POST.putExtra("배경사진", "");
                        }

//                    go_MAIN.putExtra("배경사진", changed_bitmap); //메인에 보냄
                        setResult(RESULT_OK, go_POST);
                        finish();

                     // 현재 화면이 등록화면인 경우,
                    } else if (getIntent().getStringExtra("항목").equals("추가")) {
                        Intent go_MAIN = new Intent(WriteActivity.this, GominListActivity.class);
                        String gomin = edit_gomin.getText().toString();

                        // 1) 고민내용
                        //    고민내용적는 칸(글쓰기 화면 중단부)에 글이 없으면, '고민내용을 입력해주세요' 토스트가 뜬다.
                        if (gomin.length() > 0) {
                            go_MAIN.putExtra("고민내용", gomin);
                        } else {
                            Toast.makeText(getApplicationContext(), "고민내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // 2) 닉네임보일지말지
                        //    닉네임보이기/가리기 선택 텍스트뷰(글쓰기 화면 하단부)에서
                        //    2-1) 닉네임보이기를 선택하면, 메인액티비티의 리스트뷰 아이템에 글쓴이 닉네임이 나오게 하고,
                        Intent intent = getIntent();
                        nick = intent.getStringExtra("닉네임");
                        listView_position = intent.getIntExtra("선택된행순서", 0);

                        if (set_nick.getText().equals("닉네임보이기")) {
                            go_MAIN.putExtra("닉네임보일지말지", true);
                            go_MAIN.putExtra("닉네임", nick);


                            //    2-2) 닉네임가리기를 선택하면, 글쓴이 닉네임이 들어갈 자리에 '누군가'가 나오게 한다.
                        } else if (set_nick.getText().equals("닉네임가리기")) {
                            go_MAIN.putExtra("닉네임보일지말지", false);
                            go_MAIN.putExtra("닉네임", nick);
                        }

                        // 3) 배경 사진
                        //    3-1) 배경사진이 있다면, 이미지를 가져올 수 있는 경로를 메인액티비티로 보낸다.
                        if (Image_uri != null) {
                            go_MAIN.putExtra("배경사진", "" + Image_uri);

                            //    3-2) 배경사진이 없다면, ""를 보낸다.
                        } else {
                            go_MAIN.putExtra("배경사진", "");
                        }

                        Log.e(Tag, "보낸다(고민내용) : " + gomin);
                        Log.e(Tag, "보낸다(nick) : " + nick);
                        Log.e(Tag, "보낸다(닉네임보일지말지) : " + set_nick.getText().equals("닉네임보이기"));
                        if (Image_uri != null) {
                            Log.e(Tag, "보낸다(배경사진) : " + Image_uri);
                        }


                        setResult(RESULT_OK, go_MAIN);
                        finish();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CODE) {
                try {
                    galleryAddPic();
                    Log.e(Tag, "카메라앱에서 데이터를 가져왔다." + Image_uri);

                    // 이미지를 상황에 맞게 회전시킨다.
                    ExifInterface exif = new ExifInterface(mCurrentPhotoPath);
                    Log.e(Tag,"어디서부터1");
                    int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    Log.e(Tag,"어디서부터2");
                    int exifDegree = exifOrientationToDegrees(exifOrientation);
                    Log.e(Tag,"어디서부터3");
                    Drawable drawable_image = uriToDrawable(Image_uri);
                    Log.e(Tag,"어디서부터4");
                    Bitmap bitmap_image = drawableToBitmap(drawable_image);
                    Log.e(Tag,"어디서부터5");
                    bitmap_image = rotate(bitmap_image, exifDegree);
                    Log.e(Tag,"어디서부터6");
                    Image_uri = getBitmaptoUri(getApplicationContext(),bitmap_image);
                    Log.e(Tag,"어디서부터7");
                    write_background.setBackground(uriToDrawable(Image_uri));
                } catch (Exception e) {
                    Log.e(Tag, "카메라에서 데이터를 가져오지 못했다 : " + e.toString());
                }

            } else if (requestCode == ALBUM_CODE) {
                Image_uri = data.getData();
                Log.e("HEY", "앨범_이미지URI : " + Image_uri);
                write_background.setBackground(uriToDrawable(data.getData()));

            } else {
                Log.e("HEY", "이미지 받아오기 실패 : " + requestCode);

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

    //URI에서 Bitmap으로
    public Bitmap UriToBitmap(Uri imgUri) {

        String imagePath = getRealPathFromURI(imgUri); // path 경로
//        ExifInterface exif = null;
//        try {
//            exif = new ExifInterface(imagePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
//        ivImage.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기

        return bitmap;
    }

    // 비트맵을 drawble로
    public Drawable getDrawableFromBitmap(Bitmap bitmap) {

        //Drawable drawable = getResources().getDrawable(R.drawable.my_image);
        Drawable drawable = new BitmapDrawable(bitmap);

        return drawable;

    }

    //URI로부터 경로 받아오기
    private String getRealPathFromURI(Uri contentUri) {
//        Cursor cursor = null;
//        int column_index = 0;
//        String[] proj = {MediaStore.Images.Media.DATA};
//        cursor = getContentResolver().query(contentUri, proj, null, null, null);
//        if (cursor.moveToFirst()) {
//            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        }
//
//        return cursor.getString(column_index);

        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        try {
            Cursor cursor = WriteActivity.this.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);

        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    //Drawable에서 Bitmap로
    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    //Bitmap을 byte로
    public byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        return bytes;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(Tag, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(Tag, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(Tag, "onPause()");

//        SharedPreferences SP = getSharedPreferences("고민목록",MODE_PRIVATE);
//        SharedPreferences.Editor editor = SP.edit();
//        String json = SP.getString(""+listView_position,"");
//        Gson gson = new Gson();
//        GominItem item = gson.fromJson(json,GominItem.class);
//        Log.e(Tag,"nick_OX : "+nick_OX);
//        item.setNick_OX(nick_OX);
//        json = gson.toJson(item);
//        editor.putString(""+listView_position,json);
//        editor.apply();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(Tag, "onRestart()");

//        // 다이얼로그 띄어서 작성 중인 글이 있습니다. 불러올까요? 묻기!
//        // 오른쪽 버튼(네)를 누르면, 작성 중인 글 불러옴 -> 아직 구현 X
//        // 왼쪽 버튼(아니오)를 누르면, "" 빈 텍스트로 입력한다 -> 아직 구현 X
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setTitle("메뉴");
//        alert.setMessage("작성 중인 글이 있습니다. 불러오시겠습니까?");
//
//        alert.setPositiveButton("네", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int i) {
//                dialog.dismiss();
//            }
//        });
//        alert.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int i) {
//                dialog.dismiss();
//            }
//        });
//        alert.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(Tag, "onStop()");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(Tag, "onDestroy()");

    }

    public void doTakePhotoAction() // 카메라 촬영 후 이미지 가져오기
    {
        String state = Environment.getExternalStorageState();
        // 외장 메모리 검사
        if (Environment.MEDIA_MOUNTED.equals(state)) { // 외장메모리가 있다면,
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 카메라앱을 부르는 인텐트를 킨다.
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) { // resolveActivity를 체크해야 앱이 안죽는다고 한다.
                File photoFile = null; //빈 이미지 파일을 만든다.
                try {
                    photoFile = createImageFile(); // 이제 전체 사진을 위에 만든 임시 파일에 받아오자!
                } catch (IOException ex) {
                    Log.e("captureCamera Error", ex.toString());
                }

                // 빈 파일이 생성되었다면,
                if (photoFile != null) {
                    // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함
                    Uri providerURI = FileProvider.getUriForFile(this, "com.example.gp62.todak.fileprovider", photoFile); // 파일프로바이더를 사용해서 사진파일로부터 uri를 가져온다.
                    Image_uri = providerURI; // 파일 프로바이더uri는 image Uri다.

                    // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로, providerURI의 값에 카메라 데이터를 넣어 보낸다.
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                    startActivityForResult(takePictureIntent, CAMERA_CODE); // 카메라앱으로부터 이미지를 가져오라고 시킨다.
                }
            }
        } else {
            // 외장메모리가 없다면,
            Toast.makeText(this, "저장공간이 접근 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void doTakeAlbumAction() // 앨범에서 이미지 가져오기
    {
        // 앨범 호출

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, ALBUM_CODE);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File f = new File(mCurrentPhotoPath);
        Log.e(Tag, "파일의 절대경로 : " + mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        //Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    public Uri getBitmaptoUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    ////////// 퍼미션
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == 0) {
                //Toast.makeText(this,"카메라 권한이 승인됨",Toast.LENGTH_SHORT).show();
            } else {
                //권한 거절된 경우
                Toast.makeText(this, "카메라 권한이 거절 되었습니다.카메라를 이용하려면 권한을 승낙하여야 합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 이미지 파일 만드는 메소드
    public File createImageFile() throws IOException {
        // 이미지 파일 이름을 만든다.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg"; // 타임스탬프를 넣어 이미지파일이름을 만든다.

        File imageFile = null;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Pictures", "gyeom"); // 이미지 파일이 저장될 디렉토리를 지정한다.

        if (!storageDir.exists()) { // 만약 디렉토리가 없다면,
            storageDir.mkdirs(); // 폴더를 만든다.
            Log.e("파일이 저장될 디렉토리 만들기", storageDir.toString());
        }

        imageFile = new File(storageDir, imageFileName); // 디렉토리에 생성한 이미지파일이름으로 파일을 만든다.
        mCurrentPhotoPath = imageFile.getAbsolutePath(); // 이미지의 절대적인 파일경로

        return imageFile;
    }

    // exifOrientation : 회전각
    // return : 실제각도
    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    // 이미지를 회전시킨다.
    // 비트맵이미지, 회전각도, 회전된 이미지
    public Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);
            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }
}
