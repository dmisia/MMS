package pl.edu.pwr.lab46.i236764;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

import edu.pwr.lab46.i236764.R;

public class MapQuizActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener{

    private GoogleMap mMap;
    private LatLng marker;
    private LatLng actualLatLng;
    private MarkerOptions draggable;
    String actualLongitude, actualLatitude;
    double dragLongitude;
    double dragLatitude;
    int score;
    TextView questionTextView2, timeCounterTextView2, option3TextView, nextQuestionTextView, correctAnswersTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity_layout);

        option3TextView = findViewById(R.id.option3TextView);
        nextQuestionTextView = findViewById(R.id.nextQuestionTextView);
        questionTextView2 = findViewById(R.id.questionTextView2);
        timeCounterTextView2 = findViewById(R.id.timeCounterTextView2);

        option3TextView.setBackgroundResource(R.drawable.answeroption_drawable);
        option3TextView.setVisibility(View.VISIBLE);
        option3TextView.setText("CHECK");
        questionTextView2.setVisibility(View.VISIBLE);
        questionTextView2.setText("Where is the city?");

        actualLongitude = getIntent().getStringExtra("longitude");
        actualLatitude = getIntent().getStringExtra("latitude");
        score = getIntent().getExtras().getInt("score");

        if (actualLatitude != null && actualLongitude != null) {
            actualLatLng = new LatLng(Double.parseDouble(actualLatitude), Double.parseDouble(actualLongitude));
        }
        option3TextView.setOnClickListener(this);
        nextQuestionTextView.setOnClickListener(this);

        correctAnswersTextView2 = findViewById(R.id.firstLevelCorrectAnswersTextView2);
        correctAnswersTextView2.setText("Score: "+ score);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        marker = new LatLng(37.6, 14);
        draggable = new MarkerOptions().position(marker).title("Start").draggable(true);
        mMap.addMarker(draggable);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker M0) {  }

            @Override
            public void onMarkerDragEnd(Marker M0) {
                LatLng pos = M0.getPosition();
                dragLongitude =  pos.longitude;
                dragLatitude =pos.latitude;
            }

            @Override
            public void onMarkerDrag(Marker M0) {     }

        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.option3TextView:
                double distance = calculationByDistance(new LatLng(dragLatitude, dragLongitude), actualLatLng);
                option3TextView.setText("Almost there. You missed "+ distance +"km.");
                score = (int) (score + (distance/10));
                nextQuestionTextView.setBackgroundResource(R.drawable.answeroption_drawable);
                nextQuestionTextView.setVisibility(View.VISIBLE);
                nextQuestionTextView.setText("NEXT");
                break;

            case R.id.nextQuestionTextView:
                Intent intent = new Intent(this, MainQuizActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("score", score);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_left_animation, R.anim.slide_out_from_right_animation);
                break;
        }
    }
    public double calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius=6371;//radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult= Radius*c;
        double km=valueResult/1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec =  Integer.valueOf(newFormat.format(km));
        double meter=valueResult%1000;
        int  meterInDec= Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value",""+valueResult+"   KM  "+kmInDec+" Meter   "+meterInDec);

        return Radius * c;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        System.out.print("l");
        dragLongitude = mMap.getMyLocation().getLongitude();
        dragLatitude = mMap.getMyLocation().getLatitude();
    }

}