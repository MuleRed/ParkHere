package re.parkhe.parkhere;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import im.delight.android.location.SimpleLocation;
import re.parkhe.parkhere.event.PayStationEvent;
import re.parkhe.parkhere.fragments.MapViewFragment;
import re.parkhe.parkhere.fragments.TicketFragment;
import re.parkhe.parkhere.model.ParkingLotsList;


public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private SimpleLocation location;
    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;
    private AHBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLocationPermission();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.frame_layout, new MapViewFragment(), "map").commit();


        bottomNavigation = findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Maps", R.drawable.ic_map, R.color.md_material_blue_600);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Tickets", R.drawable.ic_car, R.color.md_material_blue_600);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);

        bottomNavigation.disableItemAtPosition(1);

        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (!wasSelected) {
                    switch (position) {
                        case 0:
                            if (mFragmentManager.findFragmentByTag("map") != null) {
                                //if the fragment exists, show it.
                                mFragmentManager.beginTransaction().show(mFragmentManager.findFragmentByTag("map")).commit();
                            } else {
                                //if the fragment does not exist, add it to fragment manager.
                                mFragmentManager.beginTransaction().add(R.id.frame_layout, new MapViewFragment(), "map").commit();
                            }
                            if (mFragmentManager.findFragmentByTag("ticket") != null) {
                                //if the other fragment is visible, hide it.
                                mFragmentManager.beginTransaction().hide(mFragmentManager.findFragmentByTag("ticket")).commit();
                            }
                            break;
                        case 1:
                            if (mFragmentManager.findFragmentByTag("ticket") != null) {
                                //if the fragment exists, show it.
                                mFragmentManager.beginTransaction().show(mFragmentManager.findFragmentByTag("ticket")).commit();
                            }
                            if (mFragmentManager.findFragmentByTag("map") != null) {
                                //if the other fragment is visible, hide it.
                                mFragmentManager.beginTransaction().hide(mFragmentManager.findFragmentByTag("map")).commit();
                            }
                            break;
                    }
                }
                return true;
            }
        });
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        getPoint();
                    }

                } else {

                }
                return;
            }

        }
    }

    private void getPoint() {
        location = new SimpleLocation(this);

        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe()
    public void onEvent(PayStationEvent event) {
        bottomNavigation.enableItemAtPosition(1);
        bottomNavigation.setCurrentItem(1);
        ParkingLotsList.hour_add = event.getId();
        mFragmentManager.beginTransaction().add(R.id.frame_layout, new TicketFragment(), "ticket").commit();
        mFragmentManager.beginTransaction().hide(mFragmentManager.findFragmentByTag("map")).commit();

    }


}
