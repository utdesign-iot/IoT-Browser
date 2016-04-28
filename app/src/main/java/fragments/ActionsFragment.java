package fragments;

//IMPORTING LIBRARIES.

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.utdesign.iot.baseui.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import listadapters.ActionsAdapter;
import listitems.Action;


//MAIN CLASS START
public class ActionsFragment extends Fragment
{
    //Start of Variable Declarations.
    ListView listView;
    String[] actionNames =
            {
                    "Lights Out: Turn off both lights.",//position 0
                    "Leaving Home: Lock Door, Turn off both lights",//position 1
                    "Movie Time: Turn both lights blue and dim, Dispense Candy",//position 2
                    "Feed the Dog! Woof! Woof!: Dispense Candy, Flash Light ",//position 3
                    "Doing Nothing for Now: blah blah6"//position 4

            };//end of actionNames.

    int[] icons =
            {
                    R.mipmap.ic_lightbulb_black,//position 0
                    R.mipmap.ic_security_shield,//position 1
                    R.mipmap.ic_movie,//position 2
                    R.mipmap.ic_pets,//position 3
                    R.mipmap.ic_sofa//position 4

            };//end of icons.

    ArrayList<Action> actions;

    ActionsAdapter actionsAdapter;



    //Constructor Section.

    //Default Constructor.
    public ActionsFragment()
    {
        actions = new ArrayList<>(actionNames.length);

        for(int i = 0 ; i < actionNames.length; i++)
        {
            Action action = new Action(actionNames[i], icons[i]);

            actions.add(action);

        }

    }//End of Default Constructor.




    //START OF METHODS SECTION.

    //onCreateView method.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.actions_tab, container, false);

        listView = (ListView) view.findViewById(R.id.actions_list);

        actionsAdapter = getActionsAdapter();

        listView.setAdapter(actionsAdapter);

        //Listening for user click on tab.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.action_checkbox);



                //POSITION 0 ///////////////////////////////////////////////////////////////LIGHTS OUT TURN OFF BOTH LIGHTS
                if(position == 0  && !checkBox.isChecked())//TURNING LIGHTS OFF.
                {

                    //TESING PURPOSES WITH LANCE'S TEST LED DEVICE.
                    //new HttpPost().execute("https://api.particle.io/v1/devices/380033000447343339373536/led?access_token=9641cd0e6239dc7cbedca992efc6c23f0ec36858", "led=on");
                    //Toast.makeText(getActivity(), "HELLO THIS IS POSITION: " + position, Toast.LENGTH_SHORT).show();

                    //TURN BOTH LIGHTS 1 AND 2 OFF.
                    new HttpPost().execute("https://maker.ifttt.com/trigger/lights_out/with/key/du5zwwbbpFzqIJZiQrJkyP", "execute");


                    //after turn on, then set check box checked.
                    checkBox.setChecked(true);
                }
                else if(position == 0 && checkBox.isChecked())//TURNING LIGHTS ON.
                {

                    //TESING PURPOSES WITH LANCE'S TEST LED DEVICE.
                    //new HttpPost().execute("https://api.particle.io/v1/devices/380033000447343339373536/led?access_token=9641cd0e6239dc7cbedca992efc6c23f0ec36858", "led=off");
                    //Toast.makeText(getActivity(), "HELLO THIS IS POSITION: " + position, Toast.LENGTH_SHORT).show();

                    //TURN LIGHT1 and Light2 bright yellow.
                    new HttpPost().execute("https://maker.ifttt.com/trigger/movie_end/with/key/du5zwwbbpFzqIJZiQrJkyP", "execute");


                    //after turn on, then set check box checked.
                    checkBox.setChecked(false);
                }


                //POSITION 1 ///////////////////////////////////////////////////////////////LEAVE HOME: LOCK DOOR, TURN OFF BOTH LIGHTS
                else if(position == 1  && !checkBox.isChecked())//TURNING OFF BOTH LIGHTS AND LOCK DOOR.
                {

                    //TESING PURPOSES WITH LANCE'S TEST LED DEVICE.
                    //new HttpPost().execute("https://api.particle.io/v1/devices/380033000447343339373536/led?access_token=9641cd0e6239dc7cbedca992efc6c23f0ec36858", "led=on");

                    //LOCK THE DOOR.
                    new HttpPost().execute("https://api.particle.io/v1/devices/270024001647343337363432/state?access_token=9641cd0e6239dc7cbedca992efc6c23f0ec36858 ", "toggle");

                    //TURN BOTH LIGHTS 1 AND 2 OFF.
                    new HttpPost().execute("https://maker.ifttt.com/trigger/lights_out/with/key/du5zwwbbpFzqIJZiQrJkyP", "execute");

                    //after turn on, then set check box checked.
                    checkBox.setChecked(true);
                }
                else if(position == 1 && checkBox.isChecked())//TURNING ON BOTH LIGHTS AND UNLOCK DOOR.
                {

                    //TESING PURPOSES WITH LANCE'S TEST LED DEVICE.
                    //new HttpPost().execute("https://api.particle.io/v1/devices/380033000447343339373536/led?access_token=9641cd0e6239dc7cbedca992efc6c23f0ec36858", "led=off");

                    //UNLOCK THE DOOR.
                    new HttpPost().execute("https://api.particle.io/v1/devices/270024001647343337363432/state?access_token=9641cd0e6239dc7cbedca992efc6c23f0ec36858 ", "toggle");

                    //TURN LIGHT1 and Light2 bright yellow.
                    new HttpPost().execute("https://maker.ifttt.com/trigger/movie_end/with/key/du5zwwbbpFzqIJZiQrJkyP", "execute");

                    //after turn on, then set check box checked.
                    checkBox.setChecked(false);
                }


                //POSITION 2 ///////////////////////////////////////////////////////////////MOVIE TIME: TURN BOTH LIGHTS BLUE AND DIM, DISPENSE CANDY.
                else if(position == 2  && !checkBox.isChecked())//TURNING BOTH LIGHTS DIM BLUE AND DISPENSE CANDY.
                {
                    //TESING PURPOSES WITH LANCE'S TEST LED DEVICE.
                    //new HttpPost().execute("https://api.particle.io/v1/devices/380033000447343339373536/led?access_token=9641cd0e6239dc7cbedca992efc6c23f0ec36858", "led=on");

                    //TURN LIGHT1 and Light 2 BLUE AND DIM
                    new HttpPost().execute("https://maker.ifttt.com/trigger/movie_mode/with/key/du5zwwbbpFzqIJZiQrJkyP ", "execute");

                    //DISPENSE CANDY
                    new HttpPost().execute("https://api.particle.io/v1/devices/400023001747343338333633/giveCandy?access_token=9641cd0e6239dc7cbedca992efc6c23f0ec36858", "giveCandy");

                    //after turn on, then set check box checked.
                    checkBox.setChecked(true);
                }
                else if(position == 2 && checkBox.isChecked())//TURNING BOTH LIGHTS BRIGHT YELLOW AND DO NOTHING ELSE.
                {
                    //TESING PURPOSES WITH LANCE'S TEST LED DEVICE.
                    //new HttpPost().execute("https://api.particle.io/v1/devices/380033000447343339373536/led?access_token=9641cd0e6239dc7cbedca992efc6c23f0ec36858", "led=off");

                    //TURN LIGHT1 and Light2 bright yellow.
                    new HttpPost().execute("https://maker.ifttt.com/trigger/movie_end/with/key/du5zwwbbpFzqIJZiQrJkyP", "execute");

                    //DO NOTHIN ELSE.

                    //after turn on, then set check box checked.
                    checkBox.setChecked(false);
                }



                //POSITION 3 ///////////////////////////////////////////////////////////////FEED DOG:  DISPENSE CANDY, FLASH THE LIGHT #2
                else if(position == 3  && !checkBox.isChecked())//DISPENSE THE CANDY & FLASH THE LIGHT TEMPORARILY.
                {
                    //TESING PURPOSES WITH LANCE'S TEST LED DEVICE.
                    //new HttpPost().execute("https://api.particle.io/v1/devices/380033000447343339373536/led?access_token=9641cd0e6239dc7cbedca992efc6c23f0ec36858", "led=on");

                    //DISPENSE CANDY
                    new HttpPost().execute("https://api.particle.io/v1/devices/400023001747343338333633/giveCandy?access_token=9641cd0e6239dc7cbedca992efc6c23f0ec36858", "giveCandy");

                    //FLASH THE LIGHT #2
                    new HttpPost().execute("https://maker.ifttt.com/trigger/flash_light/with/key/du5zwwbbpFzqIJZiQrJkyP", "execute");


                    //after turn on, then set check box checked.
                    checkBox.setChecked(true);
                }
                else if(position == 3 && checkBox.isChecked())//DO NOTHING.
                {
                    //TESING PURPOSES WITH LANCE'S TEST LED DEVICE.
                    //new HttpPost().execute("https://api.particle.io/v1/devices/380033000447343339373536/led?access_token=9641cd0e6239dc7cbedca992efc6c23f0ec36858", "led=off");

                    //DO NOTHING

                    //after turn on, then set check box checked.
                    checkBox.setChecked(false);
                }


                //POSITION 4 ///////////////////////////////////////////////////////////////NOTHING THOUGHT OF FOR NOW.
                else if(position == 4  && !checkBox.isChecked())//doing nothing
                {
                    //TESING PURPOSES WITH LANCE'S TEST LED DEVICE.
                    new HttpPost().execute("https://api.particle.io/v1/devices/380033000447343339373536/led?access_token=9641cd0e6239dc7cbedca992efc6c23f0ec36858", "led=on");

                    //DOING NOTHING FOR NOW.


                    //after turn on, then set check box checked.
                    checkBox.setChecked(true);
                }
                else if(position == 4 && checkBox.isChecked())//doing nothing
                {
                    //TESING PURPOSES WITH LANCE'S TEST LED DEVICE.
                    new HttpPost().execute("https://api.particle.io/v1/devices/380033000447343339373536/led?access_token=9641cd0e6239dc7cbedca992efc6c23f0ec36858", "led=off");

                    //DOING NOTHING FOR NOW.


                    //after turn on, then set check box checked.
                    checkBox.setChecked(false);
                }
            }//end of onItemClick void method.

        });//end of setOnItemClickListener method call for listView.
        //last to do -> return the view.
        return view;

    }// End of onCreateView method.



    //getActionsAdapter method.
    public ActionsAdapter getActionsAdapter()
    {
        if(actionsAdapter == null)
        {
            actionsAdapter = new ActionsAdapter(getActivity(), actions);

        }

        return actionsAdapter;

    }//End of getActionsAdapter method.

    //Inner Class HttpPost which extends Async.
    private class HttpPost extends AsyncTask<String, Void, Void>
    {

        //doInBackground execution method.
        protected Void doInBackground(String... strings)
        {

            //trying to execute the sendOnPost Http method.
            try
            {
                //strings0 is the URL, strings1 is the parameters.
                sendOnPost(strings[0], strings[1]);

            } //End of try block.
            catch (Exception e)
            {
                e.printStackTrace();

            }//End of Catch Block.

            //need to return nothing.
            return null;

        }//End of doInBackground method.




        // HTTP POST request void method.
        private void sendOnPost(String url, String urlParameters) throws Exception
        {

            URL obj = new URL(url);

            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add request header
            con.setRequestMethod("POST");

            // Send post request
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());

            wr.writeBytes(urlParameters);

            wr.flush();

            wr.close();

            int responseCode = con.getResponseCode();

            System.out.println("\nSending 'POST' request to URL : " + url);

            System.out.println("Post parameters : " + urlParameters);

            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;

            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null)
            {
                response.append(inputLine);
            }

            //Closing BufferedReader in.
            in.close();

            //print result
            System.out.println(response.toString());

        }//end send post method.



        //onPostExecute METHOD.
        protected void onPostExecute(Void result)
        {

        }//END onPostExecute METHOD.



    }//END OF PRIVATE CLASS HTTPPOST.




}//End of ActionsFragment Main Class
