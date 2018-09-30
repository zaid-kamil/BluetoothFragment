package digipodium.episode2;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BluetoothFragment extends Fragment {


    private BluetoothAdapter bluetooth;
    private TextView tvBt;
    private ListView list;
    private Switch swBt;
    private ArrayList<Device> newDevices = new ArrayList<>();
    private ArrayAdapter<Device> adapter;
    private CheckBox scanCheck;
    private BluetoothReciever receiver;

    public BluetoothFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        tvBt = view.findViewById(R.id.tvBt);
        swBt = view.findViewById(R.id.swBt);
        list = view.findViewById(R.id.list);
        scanCheck = view.findViewById(R.id.scanCheck);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        swBt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    enableBluetooth(swBt, tvBt);
                    if (scanCheck.isChecked()) {
                        scanNearbyDevices();
                    } else {
                        getPairedDevices(list);
                    }
                } else {
                    bluetooth.disable();

                    try {
                        getActivity().unregisterReceiver(receiver);
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    public void enableBluetooth(Switch sw, TextView btv) {
        bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (bluetooth != null) {
            if (!bluetooth.isEnabled()) {
                bluetooth.enable();
            }
            sw.setChecked(true);
            btv.setText("Enabled ");

        } else {
            Toast.makeText(getActivity(), "No Bluetooth!! :P", Toast.LENGTH_SHORT).show();
        }
    }

    public void getPairedDevices(ListView listBlue) {
        tvBt.setText("Paired devices");
        newDevices.clear();
        if (bluetooth != null) {
            for (BluetoothDevice device : bluetooth.getBondedDevices()) {
                newDevices.add(new Device(device));
            }
        } else {
            Toast.makeText(getActivity(), "not working", Toast.LENGTH_SHORT).show();
        }


        adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                newDevices
        );

        listBlue.setAdapter(adapter);
    }

    public void scanNearbyDevices() {
        tvBt.setText("New devices");
        newDevices.clear();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, newDevices);
        list.setAdapter(adapter);

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        receiver = new BluetoothReciever();
        getActivity().registerReceiver(receiver, filter);

        bluetooth.startDiscovery();
        tvBt.setText("searching...");


    }


    class BluetoothReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    Toast.makeText(context, "started search",
                            Toast.LENGTH_SHORT).show();
                    tvBt.setText("searching...");
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Toast.makeText(context, "finished search",
                            Toast.LENGTH_SHORT).show();
                    tvBt.setText("Finished");
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(
                            BluetoothDevice.EXTRA_DEVICE);
                    adapter.add(new Device(device));
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

}
