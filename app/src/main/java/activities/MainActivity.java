package activities;

import android.Manifest;//for qrcode
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;//for qrcode
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;//for qrcode
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;//for qrcode
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;//for qrcode
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;//for qrcode
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;//for qrcode
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;//for qrcode
import android.view.Menu;//for qrcode
import android.view.MenuItem;//for qrcode
import android.view.View;//for qrcode
import android.view.ViewGroup;//for qrcode
import android.widget.Toast;//for qrcode

import com.utdesign.iot.baseui.R;

import org.physical_web.physicalweb.AboutFragment;
import org.physical_web.physicalweb.BeaconDisplayList;
import org.physical_web.physicalweb.NearbyBeaconsFragment;
import org.physical_web.physicalweb.NearbyBeaconsFragment.NearbyBeaconsAdapter;
import org.physical_web.physicalweb.ScreenListenerService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import fragments.ActionsFragment;
import fragments.DevicesFragment;
import qrcode.QRCSSMainActivity;//for qrcode

public class MainActivity extends AppCompatActivity {
    public final static String URL = "http://ecs.utdallas.edu";
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private ActionBar actionBar;
    private SearchView searchView;
    private DevicesFragment devicesFragment;
    private ActionsFragment actionsFragment;
    private NfcAdapter nfcAdapter;
    public NearbyBeaconsFragment nearbyBeaconsFragment;
    public NearbyBeaconsAdapter nearbyAdapter;
    public BeaconDisplayList nearbyList;

    private static final int REQUEST_ENABLE_BT = 0;
    private static final String NEARBY_BEACONS_FRAGMENT_TAG = "NearbyBeaconsFragmentTag";

    private static final int ZBAR_CAMERA_PERMISSION = 1;//for qrcode camera permission value.
    private Class<?> mClss;//for qrcode generic class call for qr simple activity.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar); // creates the toolbar
        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar(); // creates the actionbar
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // creates the nav drawer layout

        // this initializes the navigation drawer.
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setCheckable(true);
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                if (menuItem.getOrder() == 1) {
                    mToolbar.setTitle(menuItem.getTitle());
                } else {
                    mToolbar.setTitle(getTitle());
                }
                Toast.makeText(MainActivity.this,
                        menuItem.getTitle() + " " + menuItem.getOrder(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        devicesFragment = new DevicesFragment(); // instantiate the DevicesFragment
        actionsFragment = new ActionsFragment(); // instantiate the ActionsFragment
        adapter.addFragment(devicesFragment, "Devices"); // add DevicesFragment to view
        adapter.addFragment(actionsFragment, "Actions"); // add ActionsFragment to view
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setAdapter(adapter);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mViewPager);

        // sets the query hint depending on the current tab you're on.
        mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                switch(tab.getPosition())
                {
                    case 0:
                        searchView.setQueryHint("Search Devices...");
                        break;

                    case 1:
                        searchView.setQueryHint("Search Actions...");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    // checks if bluetooth is enabled.  If not instantiates a new Bluetooth adapter object
    private void ensureBluetoothIsEnabled(BluetoothAdapter bluetoothAdapter) {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        //since we start highlighted at item 0
        searchView.setQueryHint("Search Devices...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //search every fragment for the text entered
                // TODO: find a more efficient way to search.
                // maybe search only active tabs, and make sure to filter text upon switching tabs.
                // maybe just use multiple threads

                devicesFragment.getDevicesAdapter().getFilter().filter(newText);
                actionsFragment.getActionsAdapter().getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            //THIS IS FOR THE QRCODE READER.
            case R.id.action_camera:
                //toast message for qr reader activity call
                Toast.makeText(MainActivity.this, "HELLO I READ QR CODES!!!", Toast.LENGTH_LONG).show();
                //function call to launch qr reader activitty
                launchActivity(QRCSSMainActivity.class);
                //return true that qr activity launched.
                return true;
            //END OF CAMERA ACTION SECTION.

            case R.id.action_settings: // placeholder for settings
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_edit_urls: // placeholder for edit urls
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_about: // launch about activity
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // this class manages the views and fragments
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // if nfc adapter is null, then it means that the device does not have an nfcAdapter.
        if(nfcAdapter != null && nfcAdapter.isEnabled())
        {
            Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            IntentFilter[] intentFilters = new IntentFilter[]{};
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
        }
        // check device for bluetooth
        BluetoothManager btManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager != null ? btManager.getAdapter() : null;
        if (btAdapter == null) {
            Toast.makeText(getApplicationContext(),
                    R.string.error_bluetooth_support, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        ensureBluetoothIsEnabled(btAdapter);
        // instantiate the Physical Web nearby beacons fragment
        getFragmentManager().beginTransaction()
                .add(NearbyBeaconsFragment.newInstance(), NEARBY_BEACONS_FRAGMENT_TAG)
                .commit();
        getFragmentManager().executePendingTransactions();
        nearbyBeaconsFragment = ((NearbyBeaconsFragment) getFragmentManager().findFragmentByTag(NEARBY_BEACONS_FRAGMENT_TAG));
        nearbyAdapter = nearbyBeaconsFragment.getAdapter();
        //nearbyList = nearbyAdapter.getList();

        if(devicesFragment.getAdapter() == null)
            devicesFragment.setAdapter(nearbyAdapter);

        nearbyAdapter.notifyDataSetChanged();

        Intent intent = new Intent(this, ScreenListenerService.class);
        startService(intent);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if(nfcAdapter != null && nfcAdapter.isEnabled())
            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.hasExtra(NfcAdapter.EXTRA_TAG))
        {
            Toast.makeText(this, "NfcIntent", Toast.LENGTH_SHORT).show();
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if(parcelables != null && parcelables.length > 0)
                readTextFromMessage((NdefMessage) parcelables[0]);
            else
                Toast.makeText(this, "No NDEF messages found", Toast.LENGTH_SHORT).show();
        }
        else
            setIntent(intent);
    }

    private void readTextFromMessage (NdefMessage ndefMessage)
    {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if(ndefRecords != null && ndefRecords.length > 0)
        {
            NdefRecord ndefRecord = ndefRecords[0];
            String tagContent = getTextFromNdefRecord(ndefRecord);
            Intent intent = new Intent(this, BrowserActivity.class);
            intent.putExtra(URL, tagContent);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "No NDEF records found", Toast.LENGTH_SHORT).show();
    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord)
    {
        String tagContent = null;
        try
        {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1, payload.length - languageSize - 1, textEncoding);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return tagContent;
    }

    // This is a legacy function from the Physical Web
    private void showNearbyBeaconsFragment() {
        // Look for an instance of the nearby beacons fragment
        android.app.Fragment nearbyBeaconsFragment =
                getFragmentManager().findFragmentByTag(NEARBY_BEACONS_FRAGMENT_TAG);
        // If the fragment does not exist
        if (nearbyBeaconsFragment == null) {
            // Create the fragment
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_activity_container, NearbyBeaconsFragment.newInstance(),
                            NEARBY_BEACONS_FRAGMENT_TAG)
                    .commit();
            // If the fragment does exist
        } else {
            // If the fragment is not currently visible
            if (!nearbyBeaconsFragment.isVisible()) {
                // Assume another fragment is visible, so pop that fragment off the stack
                getFragmentManager().popBackStack();
            }
        }
    }

    // This function creates a new instance of the AboutFragment and displays to the user
    private void showAboutFragment() {
        AboutFragment aboutFragment = AboutFragment.newInstance();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in_and_slide_up_fragment, R.anim.fade_out_fragment,
                        R.anim.fade_in_activity, R.anim.fade_out_fragment)
                .replace(R.id.main_activity_container, aboutFragment)
                .addToBackStack(null)
                .commit();
    }

    // This is a legacy function from the Physical Web
    private boolean checkIfUserHasOptedIn() {
        String preferencesKey = getString(R.string.main_prefs_key);
        SharedPreferences sharedPreferences = getSharedPreferences(preferencesKey,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(getString(R.string.user_opted_in_flag), false);
    }

    // THIS IS FOR THE QRCODE READER. This is just a generic function to launch any activity passed as args.
    public void launchActivity(Class<?> clss) {
        //make sure have permission for camera.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {//if no permission, then request camera.
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {//if all permissions are good then launch this activity.
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }//end of launchActiviy for qr reader code.

    // THIS IS FOR THE QRCODE READER.  Request permission for camera function.
    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {//if permission granted then launch the activity.
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {//if no permission, then message user to give camera permission in device settings.
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }//end of onRequestPermissionsResult for qr reader code.


}//end of main activity class.
