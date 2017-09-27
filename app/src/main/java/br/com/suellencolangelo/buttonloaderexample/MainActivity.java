package br.com.suellencolangelo.buttonloaderexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private CircularProgressButton mCircularProgressButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mCircularProgressButton = (CircularProgressButton) findViewById(R.id.CircularProgressButton);
        mCircularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCircularProgressButton.getState() == CircularProgressButton.State.IDLE){
                    mCircularProgressButton.startAnimation();
                } else {
                    mCircularProgressButton.stopAnimation();
                }
            }
        });
    }
}
