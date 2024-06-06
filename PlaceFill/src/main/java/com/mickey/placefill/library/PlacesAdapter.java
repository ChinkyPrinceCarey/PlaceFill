package com.mickey.placefill.library;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    private List<AutocompletePrediction> mData;
    private ItemClickListener mClickListener;

    CharacterStyle primaryTextStyle = new StyleSpan(Typeface.NORMAL);
    CharacterStyle secondaryTextStyle = new StyleSpan(Typeface.NORMAL);

    PredictionsIcon predictionsIcon;

    public PlacesAdapter(Context context, List<AutocompletePrediction> predictionList) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = predictionList;

        predictionsIcon = new PredictionsIcon(context);
    }

    public void setPrimaryTextStyle(int characterStyle) {
        if(characterStyle >= 0) primaryTextStyle = new StyleSpan(characterStyle);
    }

    public void setSecondaryTextStyle(int characterStyle) {
        if(characterStyle >= 0) secondaryTextStyle = new StyleSpan(characterStyle);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_place_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AutocompletePrediction prediction = mData.get(position);
        List<String> types = prediction.getTypes();

        holder.prediction_icon.setImageDrawable(predictionsIcon.getPredictionIcon(types.get(0)));
        holder.primary_text_tv.setText(prediction.getPrimaryText(primaryTextStyle));
        holder.secondary_text_tv.setText(prediction.getSecondaryText(secondaryTextStyle));
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public void setData(List<AutocompletePrediction> predictions) {
        this.mData = predictions;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView prediction_icon;
        TextView primary_text_tv, secondary_text_tv;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            prediction_icon = itemView.findViewById(R.id.places_autocomplete_prediction_icon);
            primary_text_tv = itemView.findViewById(R.id.places_autocomplete_prediction_primary_text);
            secondary_text_tv = itemView.findViewById(R.id.places_autocomplete_prediction_secondary_text);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public AutocompletePrediction getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}