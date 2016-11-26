package list.umorili.android.com.umorili;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.splash_layout)
public class SplashActivity extends AppCompatActivity{

    @AfterViews
    void main (){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity_.class));
                finish();
            }
        }, 500);
    }
}
