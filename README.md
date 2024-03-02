PlaceFill
==========

PlaceFill is an advanced Android library that enhances the functionality of Google's Place Autocomplete by introducing features that were previously unavailable. Designed to seamlessly integrate with your Android applications, PlaceFill offers unparalleled convenience for location-based tasks.

![](static/placefill_logo.png)

## Key Features:

- **(Upcoming) Enhanced Autocompletion:** PlaceFill augments Google's Place Autocomplete with additional features such as "Your Location" support. When users select this option, PlaceFill intelligently fetches their current location, streamlining the process of selecting nearby places.

- **UI Enhancements:** Enjoy a more polished user experience with minor UI enhancements that improve usability and visual appeal. From subtle animations to refined interface elements, PlaceFill elevates the standard of location autocompletion on Android.

- **Support for the Latest Android SDK:** Stay ahead of the curve with full compatibility with the latest Android SDK versions. PlaceFill is meticulously maintained to ensure seamless integration with the newest features and optimizations offered by the Android platform.

## Why Choose PlaceFill?:

- **Enhanced Functionality:** Fill the gaps left by Google's Place Autocomplete with added features tailored to meet the needs of modern Android applications.

- **Simplified Integration:** Seamlessly integrate PlaceFill into your projects with straightforward implementation and  minimizing development time and effort.

Download
--------
You can download a jar from GitHub's [releases page][1].

Or use Gradle:

```gradle
repositories {
  google()
  mavenCentral()
}

dependencies {
    implementation 'com.google.android.libraries.places:places:3.3.0'
    implementation 'com.github.ChinkyPrinceCarey:PlaceFill:0.1'
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.0")) //aligns all kotlin sdk with same version fixing duplicate class found error
}
```

How do I use PlaceFill?
--------
Simple use case will look something like this:
```
public static final int AUTO_COMPLETE_VIA_LOCATION = 125;
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
            .setApiKey(PLACES_API_KEY) //Google Places API Key
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
```

Disclaimer
---------
This is not an official Google product.

[1]: https://github.com/ChinkyPrinceCarey/PlaceFill/releases/