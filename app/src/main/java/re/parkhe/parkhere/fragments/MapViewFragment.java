package re.parkhe.parkhere.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import re.parkhe.parkhere.R;
import re.parkhe.parkhere.model.ParkingLot;
import re.parkhe.parkhere.model.ParkingLotsList;

public class MapViewFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private ParkingLotsList mParkingLotsList;
    private ArrayList<Marker> mMarkers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_fragment, container, false);

        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        mParkingLotsList = ParkingLotsList.get(getActivity());
        mMarkers = new ArrayList<>();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setTrafficEnabled(false);
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                for (ParkingLot parkingLot : mParkingLotsList.getParkingLots()) {
                    MarkerOptions options = new MarkerOptions().position(parkingLot.getLatLng());
                    options.title("Parking Lot Title");
                    options.snippet("$4 per hour");
                    options.icon(BitmapDescriptorFactory.defaultMarker());

                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            int p = mMarkers.indexOf(marker);
                            final ParkingLot tempParkingLot = mParkingLotsList.getParkingLotsFromPostion(p);
                            new MaterialDialog.Builder(getActivity())
                                    .title("Parking Lot Location")
                                    .content("$4 Per Hour")
                                    .positiveText("Pay")
                                    .neutralText("Get Directions")
                                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + tempParkingLot.getLatitude() + "," + tempParkingLot.getLongitude());
                                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                            mapIntent.setPackage("com.google.android.apps.maps");
                                            startActivity(mapIntent);
                                        }
                                    })
                                    .show();
                        }
                    });
                    mMarkers.add(googleMap.addMarker(options));
                }

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(33.7490, -84.3880)).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}