package com.mickey.placefill.library;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import java.util.HashMap;

public class PredictionsIcon {
    Context mContext;

    public HashMap<String, Drawable> map;

    public PredictionsIcon(Context context) {
        this.mContext = context;
        this.map = new HashMap<>();
        
        setupPredictionsIconMap();
    }

    public void setupPredictionsIconMap() {
        map.put("accounting", AppCompatResources.getDrawable(mContext, R.drawable.account_balance));
        map.put("airport", AppCompatResources.getDrawable(mContext, R.drawable.local_airport));
        map.put("amusement_park", AppCompatResources.getDrawable(mContext, R.drawable.park));
        map.put("aquarium", AppCompatResources.getDrawable(mContext, R.drawable.water));
        map.put("art_gallery", AppCompatResources.getDrawable(mContext, R.drawable.brush));
        map.put("atm", AppCompatResources.getDrawable(mContext, R.drawable.atm));
        map.put("bakery", AppCompatResources.getDrawable(mContext, R.drawable.cake));
        map.put("bank", AppCompatResources.getDrawable(mContext, R.drawable.account_balance));
        map.put("bar", AppCompatResources.getDrawable(mContext, R.drawable.local_bar));
        map.put("beauty_salon", AppCompatResources.getDrawable(mContext, R.drawable.spa));
        map.put("bicycle_store", AppCompatResources.getDrawable(mContext, R.drawable.directions_bike));
        map.put("book_store", AppCompatResources.getDrawable(mContext, R.drawable.local_library));
        map.put("bus_station", AppCompatResources.getDrawable(mContext, R.drawable.directions_bus));
        map.put("cafe", AppCompatResources.getDrawable(mContext, R.drawable.local_cafe));
        map.put("campground", AppCompatResources.getDrawable(mContext, R.drawable.terrain));
        map.put("car_dealer", AppCompatResources.getDrawable(mContext, R.drawable.directions_car));
        map.put("car_rental", AppCompatResources.getDrawable(mContext, R.drawable.directions_car));
        map.put("car_repair", AppCompatResources.getDrawable(mContext, R.drawable.build));
        map.put("car_wash", AppCompatResources.getDrawable(mContext, R.drawable.local_car_wash));
        map.put("casino", AppCompatResources.getDrawable(mContext, R.drawable.casino));
        map.put("cemetery", AppCompatResources.getDrawable(mContext, R.drawable.nature));
        map.put("church", AppCompatResources.getDrawable(mContext, R.drawable.place));
        map.put("city_hall", AppCompatResources.getDrawable(mContext, R.drawable.location_city));
        map.put("clothing_store", AppCompatResources.getDrawable(mContext, R.drawable.storefront));
        map.put("convenience_store", AppCompatResources.getDrawable(mContext, R.drawable.local_convenience_store));
        map.put("courthouse", AppCompatResources.getDrawable(mContext, R.drawable.account_balance));
        map.put("dentist", AppCompatResources.getDrawable(mContext, R.drawable.local_hospital));
        map.put("doctor", AppCompatResources.getDrawable(mContext, R.drawable.local_hospital));
        map.put("drugstore", AppCompatResources.getDrawable(mContext, R.drawable.local_pharmacy));
        map.put("electrician", AppCompatResources.getDrawable(mContext, R.drawable.electrical_services));
        map.put("electronics_store", AppCompatResources.getDrawable(mContext, R.drawable.tv));
        map.put("embassy", AppCompatResources.getDrawable(mContext, R.drawable.place));
        map.put("fire_station", AppCompatResources.getDrawable(mContext, R.drawable.local_fire_department));
        map.put("florist", AppCompatResources.getDrawable(mContext, R.drawable.local_florist));
        map.put("funeral_home", AppCompatResources.getDrawable(mContext, R.drawable.nature));
        map.put("furniture_store", AppCompatResources.getDrawable(mContext, R.drawable.weekend));
        map.put("gas_station", AppCompatResources.getDrawable(mContext, R.drawable.local_gas_station));
        map.put("gym", AppCompatResources.getDrawable(mContext, R.drawable.fitness_center));
        map.put("hair_care", AppCompatResources.getDrawable(mContext, R.drawable.content_cut));
        map.put("hardware_store", AppCompatResources.getDrawable(mContext, R.drawable.handyman));
        map.put("hindu_temple", AppCompatResources.getDrawable(mContext, R.drawable.place));
        map.put("hospital", AppCompatResources.getDrawable(mContext, R.drawable.local_hospital));
        map.put("insurance_agency", AppCompatResources.getDrawable(mContext, R.drawable.security));
        map.put("jewelry_store", AppCompatResources.getDrawable(mContext, R.drawable.storefront));
        map.put("laundry", AppCompatResources.getDrawable(mContext, R.drawable.local_laundry_service));
        map.put("lawyer", AppCompatResources.getDrawable(mContext, R.drawable.gavel));
        map.put("library", AppCompatResources.getDrawable(mContext, R.drawable.local_library));
        map.put("light_rail_station", AppCompatResources.getDrawable(mContext, R.drawable.tram));
        map.put("liquor_store", AppCompatResources.getDrawable(mContext, R.drawable.local_bar));
        map.put("local_government_office", AppCompatResources.getDrawable(mContext, R.drawable.account_balance));
        map.put("locksmith", AppCompatResources.getDrawable(mContext, R.drawable.lock));
        map.put("lodging", AppCompatResources.getDrawable(mContext, R.drawable.hotel));
        map.put("meal_delivery", AppCompatResources.getDrawable(mContext, R.drawable.delivery_dining));
        map.put("meal_takeaway", AppCompatResources.getDrawable(mContext, R.drawable.takeout_dining));
        map.put("mosque", AppCompatResources.getDrawable(mContext, R.drawable.place));
        map.put("movie_rental", AppCompatResources.getDrawable(mContext, R.drawable.movie));
        map.put("movie_theater", AppCompatResources.getDrawable(mContext, R.drawable.local_movies));
        map.put("moving_company", AppCompatResources.getDrawable(mContext, R.drawable.local_shipping));
        map.put("museum", AppCompatResources.getDrawable(mContext, R.drawable.museum));
        map.put("night_club", AppCompatResources.getDrawable(mContext, R.drawable.nightlife));
        map.put("painter", AppCompatResources.getDrawable(mContext, R.drawable.format_paint));
        map.put("park", AppCompatResources.getDrawable(mContext, R.drawable.park));
        map.put("parking", AppCompatResources.getDrawable(mContext, R.drawable.local_parking));
        map.put("pet_store", AppCompatResources.getDrawable(mContext, R.drawable.pets));
        map.put("pharmacy", AppCompatResources.getDrawable(mContext, R.drawable.local_pharmacy));
        map.put("physiotherapist", AppCompatResources.getDrawable(mContext, R.drawable.health_and_safety));
        map.put("plumber", AppCompatResources.getDrawable(mContext, R.drawable.plumbing));
        map.put("police", AppCompatResources.getDrawable(mContext, R.drawable.local_police));
        map.put("post_office", AppCompatResources.getDrawable(mContext, R.drawable.local_post_office));
        map.put("primary_school", AppCompatResources.getDrawable(mContext, R.drawable.school));
        map.put("real_estate_agency", AppCompatResources.getDrawable(mContext, R.drawable.real_estate_agent));
        map.put("restaurant", AppCompatResources.getDrawable(mContext, R.drawable.restaurant));
        map.put("roofing_contractor", AppCompatResources.getDrawable(mContext, R.drawable.roofing));
        map.put("rv_park", AppCompatResources.getDrawable(mContext, R.drawable.rv_hookup));
        map.put("school", AppCompatResources.getDrawable(mContext, R.drawable.school));
        map.put("secondary_school", AppCompatResources.getDrawable(mContext, R.drawable.school));
        map.put("shoe_store", AppCompatResources.getDrawable(mContext, R.drawable.storefront));
        map.put("shopping_mall", AppCompatResources.getDrawable(mContext, R.drawable.local_mall));
        map.put("spa", AppCompatResources.getDrawable(mContext, R.drawable.spa));
        map.put("stadium", AppCompatResources.getDrawable(mContext, R.drawable.stadium));
        map.put("storage", AppCompatResources.getDrawable(mContext, R.drawable.store));
        map.put("store", AppCompatResources.getDrawable(mContext, R.drawable.store));
        map.put("subway_station", AppCompatResources.getDrawable(mContext, R.drawable.subway));
        map.put("supermarket", AppCompatResources.getDrawable(mContext, R.drawable.local_grocery_store));
        map.put("synagogue", AppCompatResources.getDrawable(mContext, R.drawable.place));
        map.put("taxi_stand", AppCompatResources.getDrawable(mContext, R.drawable.local_taxi));
        map.put("tourist_attraction", AppCompatResources.getDrawable(mContext, R.drawable.attractions));
        map.put("train_station", AppCompatResources.getDrawable(mContext, R.drawable.train));
        map.put("transit_station", AppCompatResources.getDrawable(mContext, R.drawable.directions_transit));
        map.put("travel_agency", AppCompatResources.getDrawable(mContext, R.drawable.travel_explore));
        map.put("university", AppCompatResources.getDrawable(mContext, R.drawable.school));
        map.put("veterinary_care", AppCompatResources.getDrawable(mContext, R.drawable.pets));
        map.put("zoo", AppCompatResources.getDrawable(mContext, R.drawable.pets));
    }

    public Drawable getPredictionIcon(String type) {
        Drawable prediction_icon = map.get(type);

        if(prediction_icon == null) {
            prediction_icon = AppCompatResources.getDrawable(mContext, R.drawable.place);
        }

        return prediction_icon;
    }
}
