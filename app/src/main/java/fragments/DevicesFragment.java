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
import activities.BrowserActivity;
import activities.MainActivity;

public class DevicesFragment extends Fragment {
    ListView listView;
    public NearbyBeaconsFragment.NearbyBeaconsAdapter adapter;

    // EMPTY constructor
    public DevicesFragment() {}

    // setting the adapter here allows the DevicesFragment to
    // display what the physical web found.
    public void setAdapter(NearbyBeaconsFragment.NearbyBeaconsAdapter adapter) {
        this.adapter = adapter;
    }

    // Sets the adapter that displays all nearby beacons.
    public NearbyBeaconsFragment.NearbyBeaconsAdapter getAdapter(){
        return adapter;
    }

    // Gets the adapter that displays all nearby beacons.
    public NearbyBeaconsFragment.NearbyBeaconsAdapter getDevicesAdapter() {
        return adapter;
    }

    /*
    * This is called whenever the fragment is created.
    * Since this fragment is mainly a listview,
    * whenever the fragment is created, initialize the listview.
    * */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.devices_tab,
                container, false);
        listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);
        // this itemClickListener allows the user to click on a list item
        // and have it direct them to the URL written on the list item.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BrowserActivity.class);
                intent.putExtra(MainActivity.URL, adapter.getList().getItem(position).getUrl());
                startActivity(intent);
            }
        });

        return rootView;
    }

}
