package com.abu.microjob.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abu.microjob.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;


//public class MyMapFragmentDialog extends DialogFragment implements OnMapReadyCallback{
public class MyMapFragmentDialog extends DialogFragment implements View.OnClickListener {

    public MyMapFragmentDialog() {
        // Required empty public constructor
    }
    private final int LOCATION_PERMISSION_REQUEST_CODE = 101;

    private static GoogleMap mMap;
    private Bundle mPreveusSelectedBundle;
    private LatLng mPreveusSelectedLatLng;
    private Button map_confirm_btn;
    private ImageView mapMarker, mMyCurrentLocBtn, mMapPinMarker, mDircetionBtn;
    Marker mMarker;
    Location mLocation;
    private LatLng selectedLatLng, mPinMarkerSelectedPosition;
    private MyOnLocationSelectedListener myOnLocationSelectedListener;
    private FusedLocationProviderClient fusedLocationClient;
    SupportMapFragment mapFragment;
    FrameLayout mFL_map_container;
    TextView tv_cord, tv_job_loc_text;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mPreveusSelectedBundle = getArguments();
        if (mPreveusSelectedBundle != null){
            Double lat =mPreveusSelectedBundle.getDouble("selected_map_lat") ;
            Double lng =mPreveusSelectedBundle.getDouble("selected_map_lng") ;
            if (lat != 0.0 && lng !=0.0 ){
                mPreveusSelectedLatLng = new LatLng(lat, lng);
            }else {
                mPreveusSelectedLatLng = null;
            }
        }else {
            mPreveusSelectedLatLng = null;
        }

        View v = inflater.inflate(R.layout.fragment_my_map_dialog, null);


        mFL_map_container = (FrameLayout) v.findViewById(R.id.map_fragment_container_id);
        map_confirm_btn = (Button) v.findViewById(R.id.confirm_button);
        mMapPinMarker = (ImageView) v.findViewById(R.id.map_pin_marker_id);
        mMyCurrentLocBtn = (ImageView) v.findViewById(R.id.iv_my_current_location_id);
        mDircetionBtn = (ImageView) v.findViewById(R.id.iv_direction_btn_id);
        tv_cord = (TextView) v.findViewById(R.id.tv_coardinator_id);
        tv_job_loc_text = (TextView) v.findViewById(R.id.tv_map_job_location_text_id);


//        mapFragment =  SupportMapFragment.newInstance();
////        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map_fragment_container_id, mapFragment).commitNow();
////        getChildFragmentManager().beginTransaction().replace(R.id.map_fragment_container_id, mapFragment).commitNow();
//        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.from_time_piker_linerar_container_id, mapFragment);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fetchLastLocation(15.0f, mPreveusSelectedLatLng);

        mMapPinMarker.setOnClickListener(this);
        mMyCurrentLocBtn.setOnClickListener(this);
        map_confirm_btn.setOnClickListener(this);
        mDircetionBtn.setOnClickListener(this);



        builder.setView(v)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    mConfirmButtonClicked();
                    dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dismiss());

        return builder.create();


    }



    @Override
    public void onStop() {
        if (mMap != null){
            mMap.clear();
            dismiss();
        }
        super.onStop();
    }


    private void fetchLastLocation(float zoomLevel, LatLng prev_latlng) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    mLocation = location;


                    mapFragment = SupportMapFragment.newInstance();
                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.map_fragment_container_id, mapFragment).commitNow();

                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            myMapReadyFunctionality(googleMap, zoomLevel, prev_latlng);
                        }
                    });


                }
            }
        });



    }


    private void myMapReadyFunctionality(GoogleMap googleMap, float zoomLevel, LatLng prev_latlng) {
        if (mMap != null){
            mMap.clear();
        }
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        selectedLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());

        MarkerOptions mCurrentMarker = new MarkerOptions();
        mCurrentMarker.title("Your Location");

//        MarkerOptions mPinMarker = new MarkerOptions();
//        mPinMarker.title("Selected Location");

//        Bitmap bi = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.icon_add_colored);
//        Bitmap pinBitmap = Bitmap.createScaledBitmap(bi, 20, 20, false);
////        Bitmap pinBitmap = Bitmap.createScaledBitmap(bi, 30, 70, false);

        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.icon_map_loc_indicator);
        Bitmap iconBitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, false);

//        Bitmap shadowBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.icon_shadow_bg); // Replace with your transparent circle image
        Bitmap shadowBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.icon_map_marker_bg_shadow); // Replace with your transparent circle image
        shadowBitmap = Bitmap.createScaledBitmap(shadowBitmap, 500, 500, false);

        mCurrentMarker.icon(BitmapDescriptorFactory.fromBitmap(combineBitmaps(iconBitmap, shadowBitmap)));
        mCurrentMarker.anchor(0.5f, 0.5f);//setted bitmap icon in center of latlng

        mCurrentMarker.position(selectedLatLng);
        mMap.addMarker(mCurrentMarker);

        if(prev_latlng == null){

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, zoomLevel));

        }else {
//            mCurrentMarker.position(prev_latlng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(prev_latlng, zoomLevel));
        }

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
//                mPinMarker.icon(BitmapDescriptorFactory.fromBitmap(pinBitmap));
//                mPinMarker.anchor(0.5f, -1f);//TODO: 0.5/0.5 mane center, ar amader pin icon er niddle point indicator hisehebe 0.5/-1 perfect.
//                mPinMarker.position(mMap.getCameraPosition().target);
//                mMap.addMarker(mPinMarker);
                mPinMarkerSelectedPosition = mMap.getCameraPosition().target;
                
                tv_cord.setText(mMap.getCameraPosition().target.latitude+" -- "+mMap.getCameraPosition().target.longitude);
            }
        });
        



    }



    public Bitmap combineBitmaps(Bitmap icon, Bitmap shadow) {


        int width = shadow.getWidth();
        int height = shadow.getHeight();

        // Create a new bitmap with transparency
        Bitmap combinedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combinedBitmap);


        canvas.drawBitmap(shadow, 0f, 0f, null);
        canvas.drawBitmap(icon,  (shadow.getHeight()/2)-(icon.getWidth()/2) ,(shadow.getWidth()/2)-(icon.getHeight()/2), null);


        return combinedBitmap;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                fetchLastLocation(15.0f, null);
            }else {
                Toast.makeText(getActivity(), "Location Permission is deny.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.map_pin_marker_id){
            if (tv_job_loc_text.getVisibility() == View.VISIBLE){
                tv_job_loc_text.setVisibility(View.GONE);
            }else{
                tv_job_loc_text.setVisibility(View.VISIBLE);
            }
        } else if (v.getId() == R.id.iv_my_current_location_id) {
            fetchLastLocation(20.0f, null);
        } else if (v.getId() == R.id.confirm_button) {
            mConfirmButtonClicked();
        } else if (v.getId() == R.id.iv_direction_btn_id) {
            mDirectionDraw();
        }


    }

    private void mDirectionDraw() {
        LatLng origin = new LatLng(selectedLatLng.latitude, selectedLatLng.longitude);
        LatLng destination = new LatLng(mPinMarkerSelectedPosition.latitude, mPinMarkerSelectedPosition.longitude);


        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=" + origin.latitude + "," + origin.longitude +
                "&daddr=" + destination.latitude + "," + destination.longitude);


        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            getActivity().startActivity(intent);
            dismiss();
        } else {
            Toast.makeText(getActivity(), "Google Map App is Not Installed In Your Phone", Toast.LENGTH_SHORT).show();
        }

    }

    public void mConfirmButtonClicked (){
        if (mMap != null){
            myOnLocationSelectedListener.onLocationSelected(mPinMarkerSelectedPosition.latitude, mPinMarkerSelectedPosition.longitude);
            dismiss();
        }else {
            Toast.makeText(getActivity(), "mMap is null", Toast.LENGTH_SHORT).show();
        }

    }


    public interface MyOnLocationSelectedListener {
        void onLocationSelected(double latitude, double longitude);
    }
    public void setMyOnLocationSelectedListener (MyOnLocationSelectedListener listener) {
        this.myOnLocationSelectedListener = listener;
    }


}







/*















  mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Toast.makeText(getActivity(), "21221", Toast.LENGTH_SHORT).show();
                if (marker != null) {
                    marker.remove();
                }

                MarkerOptions mo = new MarkerOptions();
                mo.position(latLng);
                mo.title("selected");
                mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.google));
                mMap.addMarker(mo);

                selectedLocation = latLng;
//                tm.setIcon();
//                mySendMapData(latLng);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Geocoder geocoder = new Geocoder(getActivity());

                try {
                    List<Address> addresses = geocoder.getFromLocationName(query, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        LatLng searchLoc = new LatLng(address.getLatitude(), address.getLongitude());
                        MyMapFragmentDialog.this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchLoc, 15.0f));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });




























































































































































*/