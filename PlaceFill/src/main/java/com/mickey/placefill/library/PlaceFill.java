package com.mickey.placefill.library;

import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_API_KEY;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_COUNTRIES;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_FIELDS_LIST;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_IS_ENABLE_YOUR_LOCATION;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_ORIGIN_LAT;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_ORIGIN_LONG;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_PRIMARY_TEXT_STYLE;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_REQUEST_CODE;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_SECONDARY_TEXT_STYLE;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_SHOULD_DEBUG;
import static com.mickey.placefill.library.Constants.PRE_INTENT_KEY_YOUR_LOCATION_PRIMARY_COLOR;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.List;

public class PlaceFill {
    Context mContext;
    Intent intent;

    public PlaceFill(Context mContext) {
        this.mContext = mContext;

        initIntent();
    }

    public Intent initIntent() {
        intent = new Intent(this.mContext, PlaceFillActivity.class);

        return intent;
    }

    public Intent destroyIntent() {
        return initIntent();
    }

    public PlaceFill setApiKey(String apiKey) {
        intent.putExtra(PRE_INTENT_KEY_API_KEY, apiKey);
        return this;
    }

    /*
    Typeface.NORMAL
    android.graphics.Typeface.BOLD
    android.graphics.Typeface.ITALIC
    */
    public PlaceFill setPrimaryTextStyle(int textStyle) {
        intent.putExtra(PRE_INTENT_KEY_PRIMARY_TEXT_STYLE, textStyle);
        return this;
    }

    public PlaceFill setSecondaryTextStyle(int textStyle) {
        intent.putExtra(PRE_INTENT_KEY_SECONDARY_TEXT_STYLE, textStyle);
        return this;
    }

    public PlaceFill shouldEnableYourLocation(boolean shouldEnable) {
        intent.putExtra(PRE_INTENT_KEY_IS_ENABLE_YOUR_LOCATION, shouldEnable);
        return this;
    }

    public PlaceFill setYourLocationPrimaryColor(int primaryColor) {
        intent.putExtra(PRE_INTENT_KEY_YOUR_LOCATION_PRIMARY_COLOR, primaryColor);
        return this;
    }

    public PlaceFill setOrigin(LatLng latLng) {
        intent.putExtra(PRE_INTENT_KEY_ORIGIN_LAT, latLng.latitude);
        intent.putExtra(PRE_INTENT_KEY_ORIGIN_LONG, latLng.longitude);
        return this;
    }

    public PlaceFill setCountries(String[] countries) {
        intent.putExtra(PRE_INTENT_KEY_COUNTRIES, countries);
        return this;
    }

    public PlaceFill setFieldsList(List<Place.Field> fieldsList) {
        ArrayList<String> fieldsStringList = new ArrayList<>();

        for (Place.Field field : fieldsList) {
            fieldsStringList.add(field.toString());
        }

        intent.putStringArrayListExtra(PRE_INTENT_KEY_FIELDS_LIST, fieldsStringList);

        return this;
    }

    public PlaceFill setRequestCode(int requestCode) {
        intent.putExtra(PRE_INTENT_KEY_REQUEST_CODE, requestCode);
        return this;
    }

    public PlaceFill setShouldDebug(boolean shouldDebug) {
        intent.putExtra(PRE_INTENT_KEY_SHOULD_DEBUG, shouldDebug);
        return this;
    }

    public Intent getIntent() {
        return intent;
    }
}
