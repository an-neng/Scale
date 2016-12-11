package com.example.fernando.mypersonalscale;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

	private final String DEVICE_ADDRESS="20:13:10:15:33:66";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    Button startButton, sendButton,clearButton,stopButton;
    TextView textView;
    EditText editText;
    boolean deviceConnected=false;
    Thread thread;
    byte buffer[];
    int bufferPosition;
    boolean stopThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textview = (TextView) findViewById(R.id.textView);
    }

	public boolean BTinit()
	    {
	        boolean found=false;
	        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
	        if (bluetoothAdapter == null) {
	            Toast.makeText(getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
	        }
	        if(!bluetoothAdapter.isEnabled())
	        {
	            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	            startActivityForResult(enableAdapter, 0);
	            try {
	                Thread.sleep(1000);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
	        if(bondedDevices.isEmpty())
	        {
	            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
	        }
	        else
	        {
                Toast.makeText(this, "BTinit", Toast.LENGTH_SHORT).show();
	            for (BluetoothDevice iterator : bondedDevices)
	            {
	                if(iterator.getAddress().equals(DEVICE_ADDRESS))
	                {
	                    device=iterator;
	                    found=true;
	                    break;
	                }
	            }
	        }
	        return found;
	    }

    public boolean BTconnect()
    {
        boolean connected=true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
            try {
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return connected;
    }


    
    public void onClickStart(View view) {
        if(BTinit())
        {
            if(BTconnect())
            {
                deviceConnected=true;
                //beginListenForData();
                textView.append("\nConnection Opened!\n");

            }

        }
    }

}
