package com.mickey.placefill.library;

import static com.mickey.placefill.library.Constants.CURRENT_LOCATION_FASTEST_INTERVAL;
import static com.mickey.placefill.library.Constants.CURRENT_LOCATION_INTERVAL;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_API_KEY;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_COUNTRIES;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_FIELDS_LIST;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_IS_ENABLE_YOUR_LOCATION;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_ORIGIN_LAT;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_ORIGIN_LONG;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_PRIMARY_TEXT_STYLE;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_REQUEST_CODE;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_RESULT_DATA;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_SECONDARY_TEXT_STYLE;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_SHOULD_DEBUG;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_YOUR_LOCATION_PRIMARY_COLOR;
import static com.mickey.placefill.library.Constants.REQUEST_CHECK_SETTINGS;
import static com.mickey.placefill.library.Constants.REQUEST_LOCATION_PERMISSION;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlaceFillActivity extends AppCompatActivity implements PlacesAdapter.ItemClickListener {
    private static PlacesClient placesClient;

    //#region Views Declare
    FrameLayout place_fill_root_container;
    ImageButton place_fill_search_bar_back_btn;
    EditText place_fill_search_bar_edt;
    ImageButton place_fill_clear_btn;

    RecyclerView place_fill_predictions_list;

    LinearLayout place_fill_error_1_container;
    ImageView place_fill_error_1_image;
    TextView place_fill_error_1_message;

    LinearLayout place_fill_error_2_container;
    ImageView place_fill_error_2_image;
    TextView place_fill_error_2_message;
    TextView place_fill_try_again;
    ProgressBar place_fill_try_again_progress;

    LinearLayout place_fill_footer_container;
    ProgressBar place_fill_footer_progress;
    //#endregion

    LinearLayout your_location_container;
    ImageView your_location_prediction_icon;
    TextView your_location_prediction_primary_text;
    TextView your_location_prediction_secondary_text;
    View your_location_separator;

    public static AutocompleteSessionToken autocompleteSessionToken;

    FindAutocompletePredictionsRequest.Builder requestBuilder;

    PlacesAdapter placesAdapter;

    private static final int LIST_CONTAINER = 11384;
    private static final int ERROR_1_CONTAINER = 11385;
    private static final int ERROR_2_CONTAINER = 11386;
    private static final int ERROR_2_CONTAINER_TRY_AGAIN = 11387;
    private static final int ERROR_2_CONTAINER_TRY_AGAIN_PROGRESS = 11388;
    private static final String ERROR_TEXT_TEMPLATE = "No results for #";

    private static String API_KEY;
    private static double ORIGIN_LAT;
    private static double ORIGIN_LONG;
    private static String[] COUNTRIES;
    private List<Place.Field> placeFields = new ArrayList<>();

    private static int REQUEST_CODE = -1;

    private static boolean SHOULD_DEBUG = false;

    private static final int API_STATUS_CODE_NO_INTERNET = 7;

    //#region Your Location
    private ExecutorService executorService;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    //#endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_fill);

        //#region Views Init
        place_fill_root_container = findViewById(R.id.place_fill_root_container);
        place_fill_search_bar_back_btn = findViewById(R.id.place_fill_search_bar_back_btn);
        place_fill_search_bar_edt = findViewById(R.id.place_fill_search_bar_edt);
        place_fill_clear_btn = findViewById(R.id.place_fill_clear_btn);

        place_fill_predictions_list = findViewById(R.id.place_fill_predictions_list);

        place_fill_error_1_container = findViewById(R.id.place_fill_error_1_container);
        place_fill_error_1_image = findViewById(R.id.place_fill_error_1_image);
        place_fill_error_1_message = findViewById(R.id.place_fill_error_1_message);

        place_fill_error_2_container = findViewById(R.id.place_fill_error_2_container);
        place_fill_error_2_image = findViewById(R.id.place_fill_error_2_image);
        place_fill_error_2_message = findViewById(R.id.place_fill_error_2_message);
        place_fill_try_again = findViewById(R.id.place_fill_try_again);
        place_fill_try_again_progress = findViewById(R.id.place_fill_try_again_progress);

        place_fill_footer_container = findViewById(R.id.place_fill_footer_container);
        place_fill_footer_progress = findViewById(R.id.place_fill_footer_progress);

        //#region Your Location Views
        your_location_container = findViewById(R.id.your_location_container);
        your_location_prediction_icon = findViewById(R.id.your_location_prediction_icon);
        your_location_prediction_primary_text = findViewById(R.id.your_location_prediction_primary_text);
        your_location_prediction_secondary_text = findViewById(R.id.your_location_prediction_secondary_text);
        your_location_separator = findViewById(R.id.your_location_separator);

        your_location_prediction_icon.setImageResource(R.drawable.gps_fixed);
        your_location_prediction_primary_text.setText("Your Location");
        your_location_prediction_secondary_text.setText("Tap select your current location");
        //#endregion
        //#endregion

        mainLayoutUpdate(LIST_CONTAINER);

        //#region Retrieve Intent Data
        API_KEY = getIntent().getStringExtra(PRE_INTENT_KEY_API_KEY);
        ORIGIN_LAT = getIntent().getDoubleExtra(PRE_INTENT_KEY_ORIGIN_LAT, -1);
        ORIGIN_LONG = getIntent().getDoubleExtra(PRE_INTENT_KEY_ORIGIN_LONG, -1);
        COUNTRIES = getIntent().getStringArrayExtra(PRE_INTENT_KEY_COUNTRIES);

        REQUEST_CODE = getIntent().getIntExtra(PRE_INTENT_KEY_REQUEST_CODE, -1);

        SHOULD_DEBUG = getIntent().getBooleanExtra(PRE_INTENT_KEY_SHOULD_DEBUG, false);

        ArrayList<String> fieldsList = getIntent().getStringArrayListExtra(PRE_INTENT_KEY_FIELDS_LIST);

        for (String fieldString : fieldsList) {
            placeFields.add(Place.Field.valueOf(fieldString));
        }
        //#endregion

        initializePlaces(this, API_KEY);
        configRequestBuilder();

        placesAdapter = new PlacesAdapter(this, null);
        placesAdapter.setClickListener(this);
        placesAdapter.setPrimaryTextStyle(getIntent().getIntExtra(PRE_INTENT_KEY_PRIMARY_TEXT_STYLE, 0));
        placesAdapter.setSecondaryTextStyle(getIntent().getIntExtra(PRE_INTENT_KEY_SECONDARY_TEXT_STYLE, 0));

        //#region Your Location
        executorService = Executors.newSingleThreadExecutor();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if(!getIntent().getBooleanExtra(PRE_INTENT_KEY_IS_ENABLE_YOUR_LOCATION, true)) {
            your_location_container.setVisibility(View.GONE);
            your_location_separator.setVisibility(View.GONE);
        }

        if(getIntent().getIntExtra(PRE_INTENT_KEY_YOUR_LOCATION_PRIMARY_COLOR, 0) != 0) {
            int desired_primary_color = getResources().getColor(getIntent().getIntExtra(PRE_INTENT_KEY_YOUR_LOCATION_PRIMARY_COLOR, 0), null);
            your_location_prediction_icon.setImageTintList(ColorStateList.valueOf(desired_primary_color));
            your_location_prediction_primary_text.setTextColor(desired_primary_color);
        }

        your_location_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printLog("Your location tapped");
                place_fill_footer_progress.setVisibility(View.VISIBLE);

                if (ContextCompat.checkSelfPermission(PlaceFillActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PlaceFillActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                } else {
                    checkLocationSettingsAndRetrieveLocation();
                }
            }
        });
        //#endregion

        place_fill_predictions_list.setLayoutManager(new LinearLayoutManager(this));
        place_fill_predictions_list.setAdapter(placesAdapter);

        place_fill_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printLog("Clicked Try Again Button");
                error2LayoutUpdate(ERROR_2_CONTAINER_TRY_AGAIN_PROGRESS);

                int position = Integer.parseInt(view.getTag().toString());

                if(position < 0) {
                    printLog("Possibly button shown when searching place, so retrying that");
                    fetchPlace(place_fill_search_bar_edt.getText().toString());
                } else {
                    printLog("Possibly button shown when selecting a place, so retrying that");
                    onItemClick(null, position);
                }
            }
        });

        place_fill_clear_btn.setVisibility(View.GONE);
        place_fill_search_bar_edt.requestFocus();

        place_fill_footer_progress.setVisibility(View.INVISIBLE);

        place_fill_search_bar_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                printLog("Searched new place");
                if(editable.length() <= 0) {
                    printLog("Searched Place: <blank>");
                    place_fill_clear_btn.setVisibility(View.GONE);
                    fetchPlace("");
                } else {
                    printLog("Searched Place: " + editable);
                    place_fill_clear_btn.setVisibility(View.VISIBLE);
                    fetchPlace(editable.toString());
                }
            }
        });

        place_fill_root_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printLog("Clicked on Empty Area");
                finishIntent(Activity.RESULT_CANCELED, null);
            }
        });

        place_fill_clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printLog("Clicked Clear Button");
                place_fill_search_bar_edt.setText("");
            }
        });

        place_fill_search_bar_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printLog("Clicked Back Button");
                finishIntent(Activity.RESULT_CANCELED, null);
            }
        });
    }

    //#region Your Location
    /* Checks and Request for Enable GPS on device */
    private void checkLocationSettingsAndRetrieveLocation() {
        LocationSettingsRequest.Builder builder =   new LocationSettingsRequest
                                                    .Builder()
                                                    .addLocationRequest(getCurrentLocationRequest())
                                                    ;

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied
                startLocationUpdates();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                printLog("onFailure: " + e);
                if (e instanceof com.google.android.gms.common.api.ResolvableApiException) {
                    // Location settings are not satisfied, hence informing the user
                    try {
                        com.google.android.gms.common.api.ResolvableApiException resolvable = (com.google.android.gms.common.api.ResolvableApiException) e;
                        resolvable.startResolutionForResult(PlaceFillActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        printLog("checkLocationSettingsAndRetrieveLocation()->onFailure->catch->error: " + sendEx);
                    }
                }
            }
        });
    }

    /* Wait for accurate location then triggers that lat long */
    private void startLocationUpdates() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    printLog("getCurrentLocation(): failed to get current location");
                    mainLayoutUpdate(ERROR_1_CONTAINER);
                    place_fill_error_1_message.setText("Failed to get your current location, try searching your desired location instead");
                    place_fill_footer_progress.setVisibility(View.GONE);
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        getPlaceIdFromLatLng(latitude, longitude, API_KEY);
                        // Stop location updates after receiving the location
                        fusedLocationClient.removeLocationUpdates(locationCallback);
                    }
                }
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(getCurrentLocationRequest(), locationCallback, null);
        }
    }

    private LocationRequest getCurrentLocationRequest() {
        return new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, CURRENT_LOCATION_INTERVAL)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(CURRENT_LOCATION_FASTEST_INTERVAL)
            .build()
        ;
    }

    private void getPlaceIdFromLatLng(double latitude, double longitude, String apiKey) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&key=" + apiKey;

        executorService.submit(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    printLog("getPlaceIdFromLatLng(): unexpected code: " + response);
                    mainLayoutUpdate(ERROR_1_CONTAINER);
                    place_fill_error_1_message.setText("Error while getting Your Location, try searching your desired location instead");
                    place_fill_footer_progress.setVisibility(View.GONE);
                }

                String responseData = response.body().string();
                JSONObject json = new JSONObject(responseData);
                JSONArray results = json.getJSONArray("results");
                if (results.length() > 0) {
                    JSONObject place = results.getJSONObject(0);
                    String placeId = place.getString("place_id");
                    runOnUiThread(() -> {
                        retrievePlace(placeId, -1);
                    });
                } else {
                    printLog("getPlaceIdFromLatLng(): response: " + responseData);
                    mainLayoutUpdate(ERROR_1_CONTAINER);
                    place_fill_error_1_message.setText("Error while getting Your Location, try searching your desired location instead");
                    place_fill_footer_progress.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                runOnUiThread(() -> {
                    printLog("getPlaceIdFromLatLng(): catch: " + e.toString());
                    mainLayoutUpdate(ERROR_2_CONTAINER);
                    error2LayoutUpdate(ERROR_2_CONTAINER_TRY_AGAIN);
                    place_fill_footer_progress.setVisibility(View.GONE);
                });
            }
        });
    }
    //#endregion

    private static void printLog(String message) {
        if(SHOULD_DEBUG) Log.d(PlaceFillActivity.class.getSimpleName(), message);
    }

    public static void initializePlaces(Context context, String apiKey) {
        printLog("initializePlaces()");

        if(apiKey != null) {
            printLog("Initializing with provided API Key");
            Places.initialize(context, apiKey);

            printLog("Initializing Auto Complete Session");
            placesClient = Places.createClient(context);
            autocompleteSessionToken = AutocompleteSessionToken.newInstance();
        } else {
            printLog("API Key not found");
        }
    }

    private void updateAdapter(List<AutocompletePrediction> predictions) {
        printLog("updateAdapter()");

        if (placesAdapter != null) {
            printLog("Updating the data");
            placesAdapter.setData(predictions);
            placesAdapter.notifyDataSetChanged();
        } else {
            printLog("Adapter is not initialized");
        }
    }

    private void configRequestBuilder() {
        printLog("configRequestBuilder()");

        // Create a RectangularBounds object.
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596)
        );

        // Use the builder to create a FindAutocompletePredictionsRequest.
        requestBuilder = FindAutocompletePredictionsRequest.builder()
                .setLocationBias(bounds)
                .setSessionToken(autocompleteSessionToken)
                ;

        if(ORIGIN_LAT != -1 && ORIGIN_LONG != -1) requestBuilder.setOrigin(new LatLng(ORIGIN_LAT, ORIGIN_LONG));

        if(COUNTRIES != null) requestBuilder.setCountries(COUNTRIES);
    }

    private void fetchPlace(String query) {
        printLog("fetchPlace()");

        place_fill_footer_progress.setVisibility(View.VISIBLE);

        printLog("request initializing");
        placesClient.findAutocompletePredictions(requestBuilder.setQuery(query).build())
        .addOnSuccessListener((response) -> {
            printLog("addOnSuccessListener()");

            if(response.getAutocompletePredictions().size() > 0 || place_fill_search_bar_edt.getText().toString().length() == 0) {
                printLog("About to Update the Data Set with new predictions");
                mainLayoutUpdate(LIST_CONTAINER);
                updateAdapter(response.getAutocompletePredictions());
            } else {
                printLog("No predictions are found, possibly place not found");
                mainLayoutUpdate(ERROR_1_CONTAINER);
                place_fill_error_1_message.setText(ERROR_TEXT_TEMPLATE.replace("#", place_fill_search_bar_edt.getText()));
            }

            place_fill_footer_progress.setVisibility(View.INVISIBLE);
        })
        .addOnFailureListener((exception) -> {
            printLog("addOnFailureListener()");

            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                printLog("API Exception Status Code: " + apiException.getStatusCode());

                if(apiException.getStatusCode() == API_STATUS_CODE_NO_INTERNET) {
                    printLog("Possibly No internet connection");

                    mainLayoutUpdate(ERROR_2_CONTAINER);
                    error2LayoutUpdate(ERROR_2_CONTAINER_TRY_AGAIN);
                } else {
                    mainLayoutUpdate(ERROR_1_CONTAINER);
                    place_fill_error_1_message.setText(ERROR_TEXT_TEMPLATE.replace("#", place_fill_search_bar_edt.getText()));
                }
            } else {
                printLog("Exception is of: " + exception.getClass().getSimpleName());
            }

            place_fill_footer_progress.setVisibility(View.INVISIBLE);
        });
    }

    private void mainLayoutUpdate(int container) {
        printLog("mainLayoutUpdate()");

        place_fill_predictions_list.setVisibility(View.GONE);
        place_fill_error_1_container.setVisibility(View.GONE);
        place_fill_error_2_container.setVisibility(View.GONE);

        switch (container) {
            case LIST_CONTAINER:
                printLog("Showing LIST_CONTAINER");
                place_fill_predictions_list.setVisibility(View.VISIBLE);
            break;
            case ERROR_1_CONTAINER:
                printLog("Showing ERROR_1_CONTAINER");
                place_fill_error_1_container.setVisibility(View.VISIBLE);
            break;
            case ERROR_2_CONTAINER:
                printLog("Showing ERROR_2_CONTAINER");
                place_fill_error_2_container.setVisibility(View.VISIBLE);
            break;
        }
    }

    private void error2LayoutUpdate(int container) {
        printLog("error2LayoutUpdate()");
        place_fill_try_again.setVisibility(View.GONE);
        place_fill_try_again_progress.setVisibility(View.GONE);

        switch (container) {
            case ERROR_2_CONTAINER_TRY_AGAIN:
                printLog("Showing ERROR_2_CONTAINER_TRY_AGAIN");
                place_fill_try_again.setVisibility(View.VISIBLE);
            break;
            case ERROR_2_CONTAINER_TRY_AGAIN_PROGRESS:
                printLog("Showing ERROR_2_CONTAINER_TRY_AGAIN_PROGRESS");
                place_fill_try_again_progress.setVisibility(View.VISIBLE);
            break;
        }
    }

    private void finishIntent(int status, Place data) {
        printLog("finishIntent()");

        Intent resultIntent = new Intent();
        resultIntent.putExtra(PRE_INTENT_KEY_REQUEST_CODE, REQUEST_CODE);
        resultIntent.putExtra(PRE_INTENT_KEY_RESULT_DATA, data);

        setResult(status, resultIntent);
        finish();
    }

    private void retrievePlace(String placeId, int position) {
        FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.newInstance(placeId, placeFields);

        placesClient.fetchPlace(fetchPlaceRequest)
        .addOnSuccessListener((response) -> {
            printLog("addOnSuccessListener()");
            finishIntent(Activity.RESULT_OK, response.getPlace());
        })
        .addOnFailureListener((exception) -> {
            printLog("addOnFailureListener()");
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                printLog("API Exception Status Code: " + apiException.getStatusCode());

                if(apiException.getStatusCode() == API_STATUS_CODE_NO_INTERNET) {
                    printLog("Possibly No internet connection");
                    mainLayoutUpdate(ERROR_2_CONTAINER);
                    if(position >=0) place_fill_try_again.setTag(String.valueOf(position));
                    error2LayoutUpdate(ERROR_2_CONTAINER_TRY_AGAIN);
                } else {
                    mainLayoutUpdate(ERROR_1_CONTAINER);
                    place_fill_error_1_message.setText(ERROR_TEXT_TEMPLATE.replace("#", place_fill_search_bar_edt.getText()));
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        printLog("onItemClick()");
        String placeId = placesAdapter.getItem(position).getPlaceId();
        retrievePlace(placeId, position);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationSettingsAndRetrieveLocation();
            } else {
                printLog("onRequestPermissionsResult(): location permission denied");
                mainLayoutUpdate(ERROR_1_CONTAINER);
                place_fill_error_1_message.setText("Location Permission denied, try searching your desired location instead");
                place_fill_footer_progress.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                startLocationUpdates();
            } else {
                printLog("onRequestPermissionsResult(): gps enable request denied");
                mainLayoutUpdate(ERROR_1_CONTAINER);
                place_fill_error_1_message.setText("GPS enable request denied, try searching your desired location instead");
                place_fill_footer_progress.setVisibility(View.GONE);
            }
        }
    }
}