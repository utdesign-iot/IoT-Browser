package fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.utdesign.iot.baseui.R;

import java.util.ArrayList;

import listadapters.ActionsAdapter;
import listitems.Action;

public class ActionsFragment extends Fragment {
    ListView listView;
    String[] actionNames = {
            "Not Home",
            "Movie Time",
            "Spring Season",
            "Babysitting Crystal's Cat",
            "Dinner Date with Bae"};
    int[] icons = {
            R.mipmap.ic_security_shield,
            R.mipmap.ic_movie,
            R.mipmap.ic_flower,
            R.mipmap.ic_pets,
            R.mipmap.ic_sofa};

    ArrayList<Action> actions;
    ActionsAdapter actionsAdapter;

    public ActionsFragment()
    {
        actions = new ArrayList<>(actionNames.length);
        for(int i = 0 ; i < actionNames.length; i++){
            Action action = new Action(actionNames[i], icons[i]);
            actions.add(action);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.actions_tab,
                container, false);
        listView = (ListView) view.findViewById(R.id.actions_list);
        actionsAdapter = getActionsAdapter();

        listView.setAdapter(actionsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.action_checkbox);
                checkBox.setChecked(!checkBox.isChecked());
            }
        });

        return view;
    }

    public ActionsAdapter getActionsAdapter() {
        if(actionsAdapter == null)
        {
            actionsAdapter = new ActionsAdapter(getActivity(), actions);
        }
        return actionsAdapter;
    }
}
