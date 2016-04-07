package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.utdesign.iot.baseui.R;


import org.physical_web.physicalweb.NearbyBeaconsFragment;

import java.util.ArrayList;

import activities.BrowserActivity;
import listadapters.DevicesAdapter;
import listitems.Device;

public class DevicesFragment extends Fragment {
    public final static String URL = "http://ecs.utdallas.edu";
    ListView listView;
    public NearbyBeaconsFragment.NearbyBeaconsAdapter adapter;
    public DevicesAdapter devicesAdapter;
    String[] deviceNames = {
            "Humpus Wumpus",
            "Candy Man",
            "Radiated Guy",
            "Crome guy"};
    String[] descriptions = {
            "http://utdallas.edu",
            "http://google.com",
            "http://yahoo.com",
            "http://ecs.utdallas.edu"};
    int[] imageIds = {R.drawable.ie_icon,
            R.drawable.cool_icon,
            R.drawable.rad_icon,
            R.drawable.chrome_icon};

    public ArrayList<Device> devices;

    public DevicesFragment() {
        devices = new ArrayList<>(deviceNames.length);
        for(int i = 0; i < deviceNames.length; i++) {
            devices.add(new Device(deviceNames[i], imageIds[i], descriptions[i]));
        }

    }

    public void setAdapter(NearbyBeaconsFragment.NearbyBeaconsAdapter adapter) {
        this.adapter = adapter;
    }

    public DevicesAdapter getDevicesAdapter() {
        // this is to ensure, whenever the devices tab gets closed
        // (because the user is 2 tabs away, thus, this tab doesn't need to be opened and is closed)
        // the devicesadapter does not get cleared.
        // however, if the devicesadapter doesn't exist yet, this will create a new one.
        if(devicesAdapter == null)
        {
            devicesAdapter = new DevicesAdapter(getActivity(), devices);
        }
        return devicesAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.devices_tab,
                container, false);
        listView = (ListView) rootView.findViewById(R.id.list);

        //devicesAdapter = getDevicesAdapter();
        //listView.setAdapter(devicesAdapter);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BrowserActivity.class);
                intent.putExtra(URL, adapter.getList().getItem(position).getUrl());
                startActivity(intent);
            }
        });


        return rootView;
    }

}
