package com.example.gp62.todak;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

public class CallLoadingAsync extends AsyncTask<Void, Integer, Void> {

    // 로딩중 표시를 띄우기 위한 어싱크태스크

    ProgressDialog dialog;
    private Context context;
    Class aclass;

    public CallLoadingAsync(Context context, Class aclass) {
        super();
        this.context = context;
        dialog = new ProgressDialog(context);
        this.aclass = aclass;
    }

    // 스레드가 시작하기 전에 수행할 작업
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("로딩 중..");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    // 스레드가 수행할 작업
    @Override
    protected Void doInBackground(Void... voids) {
        try{
                Thread.sleep(1000);
        }catch (InterruptedException e){

        }
        return null;
    }

    // 스레드가 수행되는 사이에 수행할 중간 작업
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
//        if(values[0]==0)dialog.setMessage("번호입력시작!");
//        if(values[0]==1) dialog.setMessage("3번째 숫자 입력 중..");
//        if(values[0]==2)dialog.setMessage("5번째 숫자 입력 중..");
//        if(values[0]==3)dialog.setMessage("전화번호 입력이 거의 끝나갑니다");
//        if(values[0]==4) dialog.setMessage("완료!");
    }

    // 스레드 작업이 모두 끝난 후에 수행할 작업
    @Override
    protected void onPostExecute(Void avoid) {
        super.onPostExecute(avoid);
        dialog.dismiss();
//        Intent in = new Intent(context,Intent.ACTION_VIEW, Uri.parse("tel:01000000000"));
//        context.startActivity(in);

//        Uri uri = Uri.parse("tel:01000000000");
//        Intent i = new Intent();
//        i.setAction(Intent.ACTION_VIEW);
//        i.setData(uri);
//        context.startActivity(i);

        Intent call_intent = new Intent(context,aclass);
        context.startActivity(call_intent);

        ((Activity)context).finish();

    }
}
