package com.yudhistira_gm.duinoled_droid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.io.OutputStream;
import java.util.Set;
import java.util.ArrayList;

public class ListaDispositivosBT extends AppCompatActivity {

    Button botonBT;
    ListView DispositivosBT;

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    //private OutputStream outputStream = null;
    public static String EXTRA_ADDRESS = "device_address";
    public static String NAME = "Device_Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_dispositivos_bt);

        botonBT = (Button)findViewById(R.id.buscar);
        DispositivosBT = (ListView)findViewById(R.id.listView);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth == null)
        {
            //Show a mensag. that thedevice has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            //finish apk
            finish();
        }
        else
        {
            if (myBluetooth.isEnabled())
            { }
            else
            {
                //Ask to the user turn the bluetooth on
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon,1);
            }
        }

        botonBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listaDispositivosVinculados(); //method that will be called
            }
        });
    }

    private void listaDispositivosVinculados() {

        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No se encuentra dispositivos Bluetooth Vinculados", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        DispositivosBT.setAdapter(adapter);
        DispositivosBT.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {

        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3) {

            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            /*Get the device name*/
            String name = info.substring(0,info.length() - 17);
            msg(info);
            // Make an intent to start next activity.
            Intent i = new Intent(ListaDispositivosBT.this, ledControl.class);
            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            i.putExtra(NAME,name); //this will be received at ledControl (class) Activity
            startActivity(i);
        }
    };

    private void msg(String info){
        Toast.makeText(this, info,Toast.LENGTH_LONG).show();
    }
}
