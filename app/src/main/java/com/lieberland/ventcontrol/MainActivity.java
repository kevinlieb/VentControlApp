package com.lieberland.ventcontrol;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.lieberland.ventcontrol.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    MqttAndroidClient client;

    TextView debugText;
    Button connectButton;
    Button oneButton;
    Button twoButton;
    Button threeButton;
    Button fourButton;
    Button fiveButton;
    Button sixButton;
    Button sevenButton;
    Button eightButton;
    Button nineButton;
    Button tenButton;

    Button buttons[];

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(this);

        oneButton = findViewById(R.id.oneButton);
        oneButton.setOnClickListener(this);

        twoButton = findViewById(R.id.twoButton);
        twoButton.setOnClickListener(this);

        threeButton = findViewById(R.id.threeButton);
        threeButton.setOnClickListener(this);

        fourButton = findViewById(R.id.fourButton);
        fourButton.setOnClickListener(this);

        fiveButton = findViewById(R.id.fiveButton);
        fiveButton.setOnClickListener(this);

        sixButton = findViewById(R.id.sixButton);
        sixButton.setOnClickListener(this);

        sevenButton = findViewById(R.id.sevenButton);
        sevenButton.setOnClickListener(this);

        eightButton = findViewById(R.id.eightButton);
        eightButton.setOnClickListener(this);

        nineButton = findViewById(R.id.nineButton);
        nineButton.setOnClickListener(this);

        tenButton = findViewById(R.id.tenButton);
        tenButton.setOnClickListener(this);

        buttons = new Button[11];

        buttons[1] = oneButton;
        buttons[2] = twoButton;
        buttons[3] = threeButton;
        buttons[4] = fourButton;
        buttons[5] = fiveButton;
        buttons[6] = sixButton;
        buttons[7] = sevenButton;
        buttons[8] = eightButton;
        buttons[9] = nineButton;
        buttons[10] = tenButton;

        buttons[1].setText("WipeRed");
        buttons[2].setText("WipeGreen");
        buttons[3].setText("WipeBlue");

        buttons[4].setText("T-White");
        buttons[5].setText("T-Red");
        buttons[6].setText("T-Blue");

        buttons[7].setText("Rainbow");
        buttons[8].setText("R-Chase");
        buttons[9].setText("Christmas");
        buttons[10].setText("Chanukah");

        debugText = findViewById(R.id.debugText);

        String clientId = MqttClient.generateClientId();
        //client = new MqttAndroidClient(this.getApplicationContext(), "tcp://broker.mqttdashboard.com:1883",clientId);
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://192.168.1.103:1883",clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this,"connected!!",Toast.LENGTH_LONG).show();
                    setSubscription();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this,"connection failed!!",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                debugText.setText(new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(TAG, "delivery complete");

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setSubscription(){
        try{
            client.subscribe("voltages",0);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }


    public void conn(View v){
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this,"connected!!",Toast.LENGTH_LONG).show();
                    setSubscription();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this,"connection failed!!",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void disconn(View v){

        try {
            IMqttToken token = client.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this,"Disconnected!!",Toast.LENGTH_LONG).show();


                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this,"Could not diconnect!!",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.connectButton) {
            Log.d(TAG, "Connect button pressed");
        }

        try {
            if (view.getId() == R.id.oneButton) {
                Log.d(TAG, "One button pressed");
                client.publish("lights", new MqttMessage("1".getBytes(StandardCharsets.UTF_8)));
            }

            if (view.getId() == R.id.twoButton) {
                Log.d(TAG, "Two button pressed");
                client.publish("lights", new MqttMessage("2".getBytes(StandardCharsets.UTF_8)));
            }

            if (view.getId() == R.id.threeButton) {
                Log.d(TAG, "Three button pressed");
                client.publish("lights", new MqttMessage("3".getBytes(StandardCharsets.UTF_8)));
            }

            if (view.getId() == R.id.fourButton) {
                Log.d(TAG, "Four button pressed");
                client.publish("lights", new MqttMessage("4".getBytes(StandardCharsets.UTF_8)));
            }

            if (view.getId() == R.id.fiveButton) {
                Log.d(TAG, "Five button pressed");
                client.publish("lights", new MqttMessage("5".getBytes(StandardCharsets.UTF_8)));
            }

            if (view.getId() == R.id.sixButton) {
                Log.d(TAG, "Six button pressed");
                client.publish("lights", new MqttMessage("6".getBytes(StandardCharsets.UTF_8)));
            }

            if (view.getId() == R.id.sevenButton) {
                Log.d(TAG, "Seven button pressed");
                client.publish("lights", new MqttMessage("7".getBytes(StandardCharsets.UTF_8)));
            }

            if (view.getId() == R.id.eightButton) {
                Log.d(TAG, "Eight button pressed");
                client.publish("lights", new MqttMessage("8".getBytes(StandardCharsets.UTF_8)));
            }

            if (view.getId() == R.id.nineButton) {
                Log.d(TAG, "Nine button pressed");
                client.publish("lights", new MqttMessage("9".getBytes(StandardCharsets.UTF_8)));
            }

            if (view.getId() == R.id.tenButton) {
                Log.d(TAG, "Ten button pressed");
                client.publish("lights", new MqttMessage("0".getBytes(StandardCharsets.UTF_8)));
            }
        }
        catch (Exception e) {
            Log.e(TAG, "Failed to send message");
        }
    }
}