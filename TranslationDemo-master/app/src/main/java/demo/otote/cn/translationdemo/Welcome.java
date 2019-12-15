package demo.otote.cn.translationdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ljb on 2018/4/16.
 */

public class Welcome extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isfirst();
    }

    private void isfirst() {
        SharedPreferences shared = getSharedPreferences("is", MODE_PRIVATE);
        boolean isfer = shared.getBoolean("isfer", true);
        SharedPreferences.Editor editor = shared.edit();
        if (isfer) {
            //第一次进入跳转到欢迎界面
            Intent in = new Intent(Welcome.this, GuideActivity.class);
            startActivity(in);
            finish();
            editor.putBoolean("isfer", false);
            editor.commit();
        } else {
            //直接计入应用
            Intent in = new Intent(Welcome.this,MainActivity.class);
            startActivity(in);
            finish();

        }
    }
}
