package com.mickey.placefill.demo;

import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_REQUEST_CODE;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_RESULT_DATA;
import static com.mickey.placefill.demo.BuildConfig.PLACES_API_KEY;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.mickey.placefill.library.PlaceFill;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    public static final int AUTO_COMPLETE_FROM_LOCATION = 1233;
    public static final int AUTO_COMPLETE_TO_LOCATION = 1234;
    public static final int AUTO_COMPLETE_VIA_LOCATION = 125;

    TextView result_tv;
    String result_pre_text = "Result: ";

    PlaceFill placeFill;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result_tv = findViewById(R.id.result_tv);

        initPlaceFill();
        buildPlaceFill();
    }

    //#region PlaceFill
    public void initPlaceFill() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Log.d(TAG, "result ok");
                Intent data = result.getData();

                if (data != null) {
                    int request_code = data.getIntExtra(PRE_INTENT_KEY_REQUEST_CODE, -1);

                    Log.d(TAG, "request_code: " + request_code);

                    if(data.hasExtra(PRE_INTENT_KEY_RESULT_DATA)) {
                        Log.d(TAG, "has extras");
                        Place place = data.getParcelableExtra(PRE_INTENT_KEY_RESULT_DATA);
                        result_tv.setText(result_pre_text + "\nUser Selected Place: " + place.getName() + ", \n" + place.getId() + ", \n" + place.getAddress());
                    }
                } else {
                    Log.d(TAG, "no extras");
                }
            } else {
                Log.d(TAG, "result not ok");
                result_tv.setText(result_pre_text + "\nUser canceled autocomplete");
            }
        });
    }

    public void buildPlaceFill() {
        placeFill = new PlaceFill(this);

        placeFill
                .setApiKey(PLACES_API_KEY)
                .setPrimaryTextStyle(Typeface.BOLD)
                .setSecondaryTextStyle(Typeface.NORMAL)
                .setOrigin(new LatLng(-33.8749937, 151.2041382))
                .setCountries(new String[]{"IN"})
                .setFieldsList(Arrays.asList(Place.Field.NAME, Place.Field.ID, Place.Field.LAT_LNG, Place.Field.ADDRESS))
                .setShouldDebug(true)
        ;
    }

    public void launchPlaceFill(View view) {
        placeFill.setRequestCode(AUTO_COMPLETE_VIA_LOCATION);
        launcher.launch(placeFill.getIntent());
    }
    //#endregion
}