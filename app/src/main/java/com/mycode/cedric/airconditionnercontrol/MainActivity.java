package com.mycode.cedric.airconditionnercontrol;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements HttpRequest.onHttpRequestComplete {
    static String httpServer, port, key;
    private ArrayList<HttpRequest> httpReq  = null;

    private TextView tempView;
    private int mPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgv = (ImageView)findViewById(R.id.imageView);
        imgv.setOnClickListener(termoListener);

        tempView = (TextView)findViewById(R.id.tempView);

        barSlider bsv = (barSlider)findViewById(R.id.barslider);
        bsv.setPos(mPos);
        bsv.setOnPositionListener(new barSlider.OnPositionListener(){
            @Override
            public void OnPositionChanged(int pos){
                tempView.setText(pos + "\u00B0C");
                mPos = pos;
            }
            @Override
            public void OnPositionSet(){
                sendToAircond(mPos);
            }
        });
    }

    @Override
    public void onHttpRequestComplete(String s){
        JSONObject jsonOb;

        if (s.equals("NOK")){
            showMessage("Cannot connect to home server !");
            return;
        }

        if (s.toUpperCase().equals("OK")) {
            return;
        }

        try {
            jsonOb = new JSONObject(s);
            String temperature = jsonOb.getString("ac_temperature");
            String mode = jsonOb.getString("ac_mode");
            String power = jsonOb.getString("ac_power_state");
            String fanmode = jsonOb.getString("ac_fan_mode");
            double room_temp = jsonOb.getDouble("temperature");
            double room_hum = jsonOb.getDouble("humidity");

//            view_temp.setText(String.valueOf(room_temp));
//            view_hum.setText(String.valueOf(room_hum));
//
//            tempPicker.setValue(Integer.valueOf(temperature));

//            if (power.equals("ON"))
//                power_switch.setChecked(true);
//            else
//                power_switch.setChecked(false);

            switch (fanmode){
                case "HIGH":
                    break;
                case "MEDIUM":
                    break;
                case "LOW":
                    break;
                case "QUIET":
                    break;
                default:
                case "AUTO":
                    break;
            }

            switch (mode){
                case "AUTO":
                    break;
                case "COOL":
                    break;
                case "HEAT":
                    break;
                case "FAN":
                    break;
                case "DRY":
                    break;
                default:
                    break;
            }
        }
        catch (JSONException e){
            e.printStackTrace();
            showMessage("Unknown server response... {" + s + "}");
        }
    }

    private void showMessage(String message){
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        return;
    }

    private void sendToAircond(int temp){

    }

    private boolean sendHttp(String s){
        if (httpServer.isEmpty() || port.isEmpty()){
            return false;
        }
        String data = "/control.py?key=" + key + "&" + s;
        String request = "http://" + httpServer + ":" + port + data;
        HttpRequest req = new HttpRequest(this);
        httpReq.add(req);
        req.execute(request);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (extras == null)
            return;
        mPos = extras.getInt("tempPos");
    }

    @Override
    public void onStop() {
        super.onStop();
        Intent activity = getIntent();
        activity.putExtra("tempPos", mPos);
    }

    private View.OnClickListener termoListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
            // Yes we will handle click here but which button clicked??? We don't know
            openSettings();
            Log.v("TAG>>>", "Event received");
        }
    };

    private void openSettings() {
        Intent i = new Intent(getApplicationContext(), SettingsRemoteActivity.class);
        startActivity(i);
    }
}
