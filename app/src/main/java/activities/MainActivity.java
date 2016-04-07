package activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.utdesign.iot.baseui.R;

import org.physical_web.physicalweb.AboutFragment;
import org.physical_web.physicalweb.BeaconDisplayList;
import org.physical_web.physicalweb.NearbyBeaconsFragment;
import org.physical_web.physicalweb.NearbyBeaconsFragment.NearbyBeaconsAdapter;
import org.physical_web.physicalweb.ScreenListenerService;

import java.util.ArrayList;
import java.util.List;

import fragments.ActionsFragment;
import fragments.DevicesFragment;
import qrcode.QRCSSMainActivity;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private ActionBar actionBar;
    private SearchView searchView;
    private DevicesFragment devicesFragment;
    private ActionsFragment actionsFragment;
    public NearbyBeaconsFragment nearbyBeaconsFragment;
    public NearbyBeaconsAdapter nearbyAdapter;
    public BeaconDisplayList nearbyList;

    private static final int REQUEST_ENABLE_BT = 0;
    private static final String NEARBY_BEACONS_FRAGMENT_TAG = "NearbyBeaconsFragmentTag";

    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private Class<?> mClss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.coordinator), "I'm a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Snackbar Action", Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        devicesFragment = new DevicesFragment();
        actionsFragment = new ActionsFragment();

//        getFragmentManager().beginTransaction()
//                .add(NearbyBeaconsFragment.newInstance(), NEARBY_BEACONS_FRAGMENT_TAG)
//                .commit();
//        getFragmentManager().executePendingTransactions();
//        nearbyBeaconsFragment = ((NearbyBeaconsFragment) getFragmentManager().findFragmentByTag(NEARBY_BEACONS_FRAGMENT_TAG));
//        nearbyAdapter = nearbyBeaconsFragment.getAdapter();
        //devicesFragment.setAdapter(nearbyAdapter);
        //nearbyList = nearbyAdapter.getList();

        adapter.addFragment(devicesFragment, "Devices");
        adapter.addFragment(actionsFragment, "Actions");
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setAdapter(adapter);
        mTabLayout = (TabLayout)findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            // this anonymous class doesn't get called until after onCreateOptionsMenu()
            // Thus, searchView is already initialized.
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

                Toast.makeText(MainActivity.this, "HELLO I READ QR CODES!!!", Toast.LENGTH_LONG).show();

                launchActivity(QRCSSMainActivity.class);

                return true;
            //END OF CAMERA ACTION SECTION.

            case R.id.action_settings:
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_edit_urls:
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
        BluetoothManager btManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager != null ? btManager.getAdapter() : null;
        if (btAdapter == null) {
            Toast.makeText(getApplicationContext(),
                    R.string.error_bluetooth_support, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        //if (checkIfUserHasOptedIn()) {
            ensureBluetoothIsEnabled(btAdapter);
            //showNearbyBeaconsFragment();
            getFragmentManager().beginTransaction()
                    .add(NearbyBeaconsFragment.newInstance(), NEARBY_BEACONS_FRAGMENT_TAG)
                    .commit();
            getFragmentManager().executePendingTransactions();
            nearbyBeaconsFragment = ((NearbyBeaconsFragment) getFragmentManager().findFragmentByTag(NEARBY_BEACONS_FRAGMENT_TAG));
            nearbyAdapter = nearbyBeaconsFragment.getAdapter();
            nearbyList = nearbyAdapter.getList();
            devicesFragment.setAdapter(nearbyAdapter);
            nearbyAdapter.notifyDataSetChanged();


            //PwoMetadata nearbyItem = nearbyList.getItem(0);
            //Log.d("Nearby List Url:", nearbyItem.getUrl());
            //DevicesAdapter devicesAdapter = devicesFragment.devicesAdapter;
            //ArrayList<Device> devices = devicesFragment.devices;
            //devicesFragment.devices.clear();
            //devicesFragment.devices.add(new Device("Device 0", 0, "http://utdallas.edu/~txt103120"));
            Intent intent = new Intent(this, ScreenListenerService.class);
            startService(intent);
            //devicesFragment.devices.add(new Device("Device 1", 1, nearbyList.getItem(0).getUrl()));
        //} else {
            // Show the oob activity
            //Intent intent = new Intent(this, OobActivity.class);
            //startActivity(intent);
        //}
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

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

    private void showAboutFragment() {
        AboutFragment aboutFragment = AboutFragment.newInstance();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in_and_slide_up_fragment, R.anim.fade_out_fragment,
                        R.anim.fade_in_activity, R.anim.fade_out_fragment)
                .replace(R.id.main_activity_container, aboutFragment)
                .addToBackStack(null)
                .commit();
    }

    private boolean checkIfUserHasOptedIn() {
        String preferencesKey = getString(R.string.main_prefs_key);
        SharedPreferences sharedPreferences = getSharedPreferences(preferencesKey,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(getString(R.string.user_opted_in_flag), false);
    }

    // THIS IS FOR THE QRCODE READER.
    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    // THIS IS FOR THE QRCODE READER.
    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }


}//end of main activity class.
