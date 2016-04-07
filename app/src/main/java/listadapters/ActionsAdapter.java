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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.utdesign.iot.baseui.R;

import java.util.ArrayList;

import listitems.Action;

public class ActionsAdapter extends BaseAdapter implements Filterable{

    private Context context;
    ArrayList<Action> actions;
    ArrayList<Action> filterList;
    ActionFilter actionFilter;

    public ActionsAdapter(Context context, ArrayList<Action> actions)
    {
        this.context = context;
        this.actions = actions;
        filterList = actions;
    }

    @Override
    public int getCount() {
        return actions.size();
    }

    @Override
    public Object getItem(int position) {
        return actions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return actions.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.actions_row_model, null);
        }

        if (position % 2 == 1) { view.setBackgroundColor(Color.rgb(237, 237, 237)); }

        TextView actionView = (TextView) view.findViewById(R.id.action_title);
        ImageView iconView = (ImageView) view.findViewById(R.id.action_icon_list);

        Action actionItem = actions.get(position);

        actionView.setText(actionItem.getName());
        iconView.setImageResource(actionItem.getIcon());

//        LinearLayout layout = (LinearLayout) view.findViewById(R.id.action_icon_list_layout);
//        ImageView imageView = new ImageView(getContext());
//        imageView.setImageResource(R.mipmap.ic_person_pin);
//        layout.addView(imageView);

        return view;
    }

    @Override
    public Filter getFilter() {
        if(actionFilter == null)
        {
            actionFilter = new ActionFilter();
        }

        return actionFilter;
    }

    class ActionFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if(constraint.length() > 0) {
                constraint = constraint.toString().toLowerCase();
                ArrayList<Action> filters = new ArrayList<Action>();

                for(int i = 0; i < filterList.size(); i++) {

                    Action action = filterList.get(i);
                    if(action.getName().toLowerCase().contains(constraint)){
                        filters.add(action);
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
            actions = (ArrayList<Action>) results.values;
            notifyDataSetChanged();
        }
    }
}
