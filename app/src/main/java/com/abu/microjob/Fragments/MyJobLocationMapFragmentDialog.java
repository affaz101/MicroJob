package com.abu.microjob.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.abu.microjob.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


//public class MyMapFragmentDialog extends DialogFragment implements OnMapReadyCallback{
public class MyJobLocationMapFragmentDialog extends DialogFragment implements View.OnClickListener {

    public MyJobLocationMapFragmentDialog() {
        // Required empty public constructor
    }
    private final int LOCATION_PERMISSION_REQUEST_CODE = 101;

    private static GoogleMap mJobLoc_Map;
    private Bundle bundle;
    private LatLng mJobLoc_PreveusSelectedLatLng;
    private ImageView mJobLoc_MyCurrentLocBtn, mJobLoc_DircetionBtn, mJobLoc_locationBtn;
    Marker mJobLoc_Marker;
    Location my_current_location;
    private LatLng myCurrent_LatLng, mJobLoc_LatlLng, mPrevJobLatLng;
    private FusedLocationProviderClient fusedLast_LocationClient;
    private SupportMapFragment mapJobLoc_Fragment;
    FrameLayout mJobLoc_FL_map_container;
    TextView tv_JobLoc_cord;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        bundle = getArguments();
        if (bundle != null){
            Double lat = bundle.getDouble("job_lat");
            Double lng = bundle.getDouble("job_lng");
            mJobLoc_LatlLng = new LatLng(lat, lng);
            mPrevJobLatLng = mJobLoc_LatlLng;
        }else{
            mJobLoc_LatlLng = (mPrevJobLatLng != null)? mPrevJobLatLng: null;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
//        mPreveusSelectedBundle = getArguments();
//        if (mPreveusSelectedBundle != null){
//            Double lat =mPreveusSelectedBundle.getDouble("selected_map_lat") ;
//            Double lng =mPreveusSelectedBundle.getDouble("selected_map_lng") ;
//            if (lat != 0.0 && lng !=0.0 ){
//                mPreveusSelectedLatLng = new LatLng(lat, lng);
//            }else {
//                mPreveusSelectedLatLng = null;
//            }
//        }else {
//            mPreveusSelectedLatLng = null;
//        }



        View v = inflater.inflate(R.layout.fragment_my_job_location_map_dialog, null);


        mJobLoc_FL_map_container = (FrameLayout) v.findViewById(R.id.JobLoc_map_fragment_container_id);
        mJobLoc_MyCurrentLocBtn = (ImageView) v.findViewById(R.id.jobLocation_iv_my_current_location_id);
        mJobLoc_DircetionBtn = (ImageView) v.findViewById(R.id.jobLocation_iv_direction_btn_id);
        mJobLoc_locationBtn = (ImageView) v.findViewById(R.id.jobLocation_iv_job_location_btn_id);
        tv_JobLoc_cord = (TextView) v.findViewById(R.id.jobLocation_tv_coardinator_id);
        tv_JobLoc_cord.setText((mJobLoc_LatlLng  != null)?(mJobLoc_LatlLng.latitude+"      -      "+mJobLoc_LatlLng.longitude):tv_JobLoc_cord.getText().toString());


        fusedLast_LocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mGotoJobLatLng(15.0f, mJobLoc_LatlLng);


        mJobLoc_MyCurrentLocBtn.setOnClickListener(this);
        mJobLoc_DircetionBtn.setOnClickListener(this);
        mJobLoc_locationBtn.setOnClickListener(this);


        builder.setView(v)
                .setPositiveButton("Ok", (dialog, which) -> {
                    //mConfirmButtonClicked();
                    dismiss();
                });

//                .setNegativeButton("Cancel", (dialog, which) -> dismiss());

        return builder.create();

    }


    private void mGotoJobLatLng(float zoomLevel, LatLng job_latlng) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLast_LocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    my_current_location = location;


                    mapJobLoc_Fragment = SupportMapFragment.newInstance();
                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.JobLoc_map_fragment_container_id, mapJobLoc_Fragment).commitNow();

                    mapJobLoc_Fragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            myMapReadyFunctionality(googleMap, zoomLevel, job_latlng);
                        }
                    });


                }
            }
        });



    }


    private void myMapReadyFunctionality(GoogleMap googleMap, float zoomLevel, LatLng job_latlng) {
        if (mJobLoc_Map != null){
            mJobLoc_Map.clear();
        }
        mJobLoc_Map = googleMap;

        mJobLoc_Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mJobLoc_Map.getUiSettings().setZoomControlsEnabled(true);
        mJobLoc_Map.getUiSettings().setMapToolbarEnabled(true);
        mJobLoc_Map.getUiSettings().setMyLocationButtonEnabled(true);


        myCurrent_LatLng = new LatLng(my_current_location.getLatitude(), my_current_location.getLongitude());

        MarkerOptions mJobMarker = new MarkerOptions();


        if (job_latlng != null){
            Bitmap btm = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.icon_map_location);
            Bitmap job_btm = Bitmap.createScaledBitmap(btm, 80, 80, false);
            mJobMarker.title("Job Location");
            mJobMarker.position(job_latlng);
            mJobMarker.anchor(0.5f, 1.0f);
            mJobMarker.icon(BitmapDescriptorFactory.fromBitmap(job_btm));
            mJobLoc_Map.addMarker(mJobMarker);

        }


        MarkerOptions mCurrentMarker = new MarkerOptions();
        mCurrentMarker.title("Your Location");

        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.icon_map_loc_indicator);
        Bitmap iconBitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, false);


        Bitmap shadowBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.icon_map_marker_bg_shadow);
        shadowBitmap = Bitmap.createScaledBitmap(shadowBitmap, 500, 500, false);
        mCurrentMarker.icon(BitmapDescriptorFactory.fromBitmap(combineBitmaps(iconBitmap, shadowBitmap)));
        mCurrentMarker.anchor(0.5f, 0.5f);//setted bitmap icon in center of latlng

        mCurrentMarker.position(myCurrent_LatLng);
        mJobLoc_Map.addMarker(mCurrentMarker);


//        Toast.makeText(getActivity(), job_latlng.latitude+"    :lat"+job_latlng.longitude+"   :lng", Toast.LENGTH_SHORT).show();


        if(job_latlng == null){


            mJobLoc_Map.moveCamera(CameraUpdateFactory.newLatLngZoom(myCurrent_LatLng, zoomLevel));

            Bitmap btm = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.icon_map_location);
            Bitmap job_btm = Bitmap.createScaledBitmap(btm, 80, 80, false);
            mJobMarker.title("Job Location");
            mJobMarker.position(mJobLoc_LatlLng);
            mJobMarker.anchor(0.5f, 1.0f);
            mJobMarker.icon(BitmapDescriptorFactory.fromBitmap(job_btm));
            mJobLoc_Map.addMarker(mJobMarker);

        }else {
            mJobLoc_Map.moveCamera(CameraUpdateFactory.newLatLngZoom(job_latlng, zoomLevel));
        }





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
                mGotoJobLatLng(15.0f, null);
            }else {
                Toast.makeText(getActivity(), "Location Permission is deny.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.jobLocation_iv_my_current_location_id) {
            mPrevJobLatLng = (mJobLoc_LatlLng != null)?mJobLoc_LatlLng:null;
            mGotoJobLatLng(15.0f, null);
        }else if (v.getId() == R.id.jobLocation_iv_job_location_btn_id) {

            mPrevJobLatLng = (mJobLoc_LatlLng != null)?mJobLoc_LatlLng:null;
            mGotoJobLatLng(15.0f, mPrevJobLatLng);
        }else if (v.getId() == R.id.jobLocation_iv_direction_btn_id) {
            mDirectionDraw();
        }

    }

    private void mDirectionDraw() {
        LatLng origin = new LatLng(myCurrent_LatLng.latitude, myCurrent_LatLng.longitude);
        LatLng destination = new LatLng(mJobLoc_LatlLng.latitude, mJobLoc_LatlLng.longitude);


        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=" + origin.latitude + "," + origin.longitude +
                "&daddr=" + destination.latitude + "," + destination.longitude);


        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        intent.setPackage("com.google.android.apps.maps");


        // Check if Google Maps is installed before launching the intent
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            getActivity().startActivity(intent);
            dismiss();
        } else {
            Toast.makeText(getActivity(), "Google Map App is Not Installed In Your Phone", Toast.LENGTH_SHORT).show();
        }

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