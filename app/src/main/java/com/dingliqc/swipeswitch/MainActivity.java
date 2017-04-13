package com.dingliqc.swipeswitch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dingliqc.swipeswitch.swipeswitch.ActivityManager;
import com.dingliqc.swipeswitch.swipeswitch.BaseSwipeSwitchActivity;

import java.util.Random;

public class MainActivity extends BaseSwipeSwitchActivity {
    private ViewGroup main;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = (ViewGroup) findViewById(R.id.activity_main);
        btn = (Button) findViewById(R.id.btn);
        Random random = new Random();
        main.setBackgroundColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}
