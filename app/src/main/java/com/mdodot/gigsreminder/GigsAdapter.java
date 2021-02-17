package com.mdodot.gigsreminder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class GigsAdapter extends ArrayAdapter<GigModel> {
    private ArrayList<GigModel> dataSet;
    private GigModel gigModel;
    private Context mContext;

    private static class ViewHolder {
        TextView band;
        TextView town;
        TextView date;
        TextView time;
        ImageView edit;
        ImageView delete;
    }

    public GigsAdapter(ArrayList<GigModel> data, Context context){
        super(context, R.layout.array_list_item_gigs, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        gigModel = getItem(position);
        final ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.array_list_item_gigs, parent, false);
            viewHolder.band = (TextView) convertView.findViewById(R.id.band_name);
            viewHolder.town = (TextView) convertView.findViewById(R.id.town);
            viewHolder.date = (TextView) convertView.findViewById(R.id.eventDate);
            viewHolder.time = (TextView) convertView.findViewById(R.id.eventTime);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.band.setText(gigModel.getBand());
        viewHolder.town.setText(gigModel.getTown());
        viewHolder.date.setText(gigModel.getDate());
        viewHolder.time.setText(gigModel.getTime());

        convertView.findViewById(R.id.gig_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(view.getContext(), GigActivity.class));
            }
        });

        convertView.findViewById(R.id.gig_entry).setOnLongClickListener(new View.OnLongClickListener() {

            private final int gigId = gigModel.getId();

            @Override
            public boolean onLongClick(View view) {
                if (mContext instanceof GigsActivity) {
                    DialogFragment optionsGigFragment = new EventOptionsDialogFragment();
                    Bundle args = new Bundle();
                    args.putInt("id", gigId);
                    args.putSerializable("event", gigModel);
                    optionsGigFragment.setArguments(args);
                    optionsGigFragment.show(((GigsActivity) mContext).getSupportFragmentManager(), "optionsGig");
                }
                return true;
            }
        });
        return convertView;
    }
}