package listadapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.utdesign.iot.baseui.R;

import java.util.ArrayList;

import listitems.Device;

/**
 * Created by Ray on 2/26/2016.
 */
public class DevicesAdapter extends BaseAdapter implements Filterable{

    private Context context;
    ArrayList<Device> devices;
    ArrayList<Device> filterList;
    DeviceFilter deviceFilter;

    public DevicesAdapter(Context c, ArrayList<Device> devices)
    {
        context = c;
        this.devices = devices;
        filterList = devices;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return devices.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.devices_row_model, null);
        }

        if (position % 2 == 1) { convertView.setBackgroundColor(Color.rgb(237,237,237)); }

        TextView deviceTxtView = (TextView) convertView.findViewById(R.id.header_txt);
        ImageView image = (ImageView ) convertView.findViewById(R.id.device_icon);
        TextView description = (TextView) convertView.findViewById(R.id.subheader_txt);

        Device device = devices.get(position);

        deviceTxtView.setText(device.getDeviceName());
        image.setImageResource(device.getImageID());
        description.setText(device.getDescription());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(deviceFilter == null)
        {
            deviceFilter = new DeviceFilter();
        }

        return deviceFilter;
    }

    class DeviceFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if(constraint.length() > 0) {
                constraint = constraint.toString().toLowerCase();
                ArrayList<Device> filters = new ArrayList<Device>();

                for(int i = 0; i < filterList.size(); i++) {

                    Device device = filterList.get(i);
                    if(device.getDeviceName().toLowerCase().contains(constraint)){
                        filters.add(device);
                    }
                }

                filterResults.count = filters.size();
                filterResults.values = filters;
            } else {
                filterResults.count = filterList.size();
                filterResults.values = filterList;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            devices = (ArrayList<Device>) results.values;
            notifyDataSetChanged();
        }
    }

}
