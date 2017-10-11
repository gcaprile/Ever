package com.app.checkinmap.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.widget.TextView;

import com.app.checkinmap.R;
import com.simplify.ink.InkView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignatureActivity extends AppCompatActivity {
    public static String   ARG_NAME="name";

    @BindView(R.id.button_check)
    AppCompatButton mBtnCheckOut;

    @BindView(R.id.ink)
    InkView mInkView;

    /**
     * This method help us to get a single signature activity
     * intent
     */
    public static Intent getIntent(Context context,String name){
        Intent intent = new Intent(context,SignatureActivity.class);
        intent.putExtra(ARG_NAME,name);
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
        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.text_view_clear)
    public void clearInkView(){
        mInkView.clear();
    }
}
