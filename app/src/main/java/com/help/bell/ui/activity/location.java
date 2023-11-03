package com.help.bell.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class location {

    public class MainActivity extends AppCompatActivity {
        ToggleButton tb;
        TextView gpsResult;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("GPS정보");
        String userID;
        boolean check = true;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            tb = (ToggleButton) findViewById(R.id.button);
            gpsResult = (TextView)findViewById(R.id.gps_info);
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }
            final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            tb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (tb.isChecked()) {
                            gpsResult.setText("수신중..");
                            if(check == true) {
                                userID = databaseReference.push().getKey();
// GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기
                                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                        100, // 통지사이의 최소 시간간격 (miliSecond)
                                        1, // 통지사이의 최소 변경거리 (m)
                                        gpsLocationListener);
                                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                        100, // 통지사이의 최소 시간간격 (miliSecond)
                                        1, // 통지사이의 최소 변경거리 (m)
                                        gpsLocationListener);
                                check = false;

                            }
                        } else {
                            gpsResult.setText("위치정보 미수신중");
                            lm.removeUpdates(gpsLocationListener); // 미수신할때는 반드시 자원해체를 해주어야 한다.
                        }
                    } catch (SecurityException ex) {
                    }
                }
            });
        }

        public final LocationListener gpsLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                String provider = location.getProvider();
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                double altitude = location.getAltitude();

                gpsResult.setText("위치정보 : " + provider + "\n" +
                        "위도 : " + longitude + "\n" +
                        "경도 : " + latitude + "\n" +
                        "고도 : " + altitude);

                databaseReference.child(userID).child("경도").setValue(longitude);
                databaseReference.child(userID).child("위도").setValue(latitude);
                check = true;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

    }
}
