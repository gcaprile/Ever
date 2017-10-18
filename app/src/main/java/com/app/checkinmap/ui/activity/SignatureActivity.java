package com.app.checkinmap.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.checkinmap.R;
import com.app.checkinmap.util.ImageHelper;
import com.simplify.ink.InkView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignatureActivity extends AppCompatActivity {
    public static final String ARG_SING_FILE_PATH ="file_path";
    public static String   ARG_NAME="name";
    public static String   ARG_WORK_ORDER_ID="work_order_id";

    @BindView(R.id.button_check)
    AppCompatButton mBtnCheckOut;

    @BindView(R.id.ink)
    InkView mInkView;

    /**
     * This method help us to get a single signature activity
     * intent
     */
    public static Intent getIntent(Context context,String name,String workOrderId){
        Intent intent = new Intent(context,SignatureActivity.class);
        intent.putExtra(ARG_NAME,name);
        intent.putExtra(ARG_WORK_ORDER_ID,workOrderId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        ButterKnife.bind(this);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setTitle(getIntent().getExtras().getString(ARG_NAME));
        }

        mInkView.setColor(getResources().getColor(android.R.color.black));
        mInkView.setMinStrokeWidth(1.5f);
        mInkView.setMaxStrokeWidth(6f);
    }

    @OnClick(R.id.button_check)
    public void checkOut(){

        /*Here we save the current sing*/
        Bitmap drawing = mInkView.getBitmap(getResources().getColor(R.color.colorWhite));

        File file = ImageHelper.saveBitMap(drawing,getIntent().getExtras().getString(ARG_WORK_ORDER_ID));

        if(file!=null){
            Intent intent = new Intent();
            intent.putExtra(ARG_SING_FILE_PATH,file.getPath());
            setResult(RESULT_OK,intent);
            finish();
        }else{
            Toast.makeText(this,R.string.sing_no_saved,Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.text_view_clear)
    public void clearInkView(){
        mInkView.clear();
    }
}
