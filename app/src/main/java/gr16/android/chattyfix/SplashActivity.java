package gr16.android.chattyfix;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Scene;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadData();
    }

    /**
     * Load data
     */
    private void loadData()
    {
        // Delay added to simulate actual loading and show splashscreen.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dataLoaded();
            }
        }, 3000);
    }

    /**
     * Start the next intended activity and give it loaded data.
     */
    private void dataLoaded()
    {
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtra("Data1", "String");
        i.putExtra("Data2", 5);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        this.finish();
    }
}
