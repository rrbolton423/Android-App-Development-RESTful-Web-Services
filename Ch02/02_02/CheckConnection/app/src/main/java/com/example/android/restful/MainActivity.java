package com.example.android.restful;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.restful.utils.NetworkHelper;

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity {

    private TextView tvOut;
    private boolean networkOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOut = (TextView) findViewById(R.id.tv_out);

        networkOk = NetworkHelper.hasNetworkAccess(this);
        tvOut.setText(String.format("Network ok: %s", networkOk));
    }
}
