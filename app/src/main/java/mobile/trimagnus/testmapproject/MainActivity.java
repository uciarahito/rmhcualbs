package mobile.trimagnus.testmapproject;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextOrigin, editTextDestination;
    TextView textViewOrigin, textViewDestination;
    Button buttonFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextOrigin = (EditText)findViewById(R.id.editTextOrigin);
        editTextDestination = (EditText)findViewById(R.id.editTextDestination);
        textViewOrigin = (TextView)findViewById(R.id.textViewOrigin);
        textViewDestination = (TextView)findViewById(R.id.textViewDestination);
        buttonFind = (Button)findViewById(R.id.buttonFind);

        buttonFind.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonFind){
            String origin="", destination="";
            origin = editTextOrigin.getText().toString();
            destination = editTextDestination.getText().toString();

            if(!TextUtils.isEmpty(origin) && !TextUtils.isEmpty(destination)){
                LatLng originLL = null, destinationLL = null;
                originLL = getLocationFromAddress(origin);
                destinationLL = getLocationFromAddress(destination);

                if(originLL != null && destinationLL != null){
                    textViewOrigin.setText("Lat: "+originLL.latitude+", Long: "+originLL.longitude);
                    textViewDestination.setText("Lat: "+destinationLL.latitude+", Long: "+destinationLL.longitude);
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("latOrigin", ""+originLL.latitude);
                    intent.putExtra("longOrigin", ""+originLL.longitude);
                    intent.putExtra("latDestination", ""+destinationLL.latitude);
                    intent.putExtra("longDestination", ""+destinationLL.longitude);
                    startActivity(intent);
                }
                //startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        }
    }

    private LatLng getLocationFromAddress(String _address){
        Double _latitude=0.0, _longitude=0.0;
        if(Geocoder.isPresent()){
            try {
                String location = _address;
                Geocoder gc = new Geocoder(MainActivity.this);
                List<Address> addresses= gc.getFromLocationName(location, 5); // get the found Address Objects

                List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                for(Address a : addresses){
                    if(a.hasLatitude() && a.hasLongitude()){
                        ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                        _latitude = a.getLatitude();
                        _longitude = a.getLongitude();
                    }
                }
            } catch (IOException e) {
                // handle the exception
            }
        }
        return new LatLng(_latitude, _longitude);
    }
}
