 package com.nila.spareroomapp.view;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nila.spareroomapp.R;
import com.nila.spareroomapp.model.UpcomingModel;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpcomingEventListAdapter extends RecyclerView.Adapter<UpcomingEventListAdapter.UpcomingViewHolder> {

    private List<UpcomingModel> upcomingEvents;


    public UpcomingEventListAdapter(List<UpcomingModel> upcomingEvents){
        this.upcomingEvents = upcomingEvents;
    }

    public void updateUpcomingEvents(List<UpcomingModel> newUpcomingEvents){
        upcomingEvents.clear();
        upcomingEvents.addAll(newUpcomingEvents);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_list, parent,false);
        return new UpcomingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingViewHolder holder, int position) {
        if(upcomingEvents.get(position)!=null)
        holder.bind(upcomingEvents.get(position));
    }

    @Override
    public int getItemCount() {
        return upcomingEvents.size();
    }


    class UpcomingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.event_image)
        ImageView eventImage;

        @BindView(R.id.venue)
        TextView eventVenue;

        @BindView(R.id.event_location)
        TextView eventLocation;

        @BindView(R.id.event_date)
        TextView eventDate;

        @BindView(R.id.event_time)
        TextView eventTime;

        @BindView(R.id.event_cost)
        TextView eventCost;

        @BindView(R.id.month_header)
        TextView monthHeader;


        public UpcomingViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        void bind(UpcomingModel upcomingModel){

            if(upcomingModel.getImage_url()!=null && upcomingModel.getImage_url()!="") {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM-dd");
                SimpleDateFormat startTimeFormatter = new SimpleDateFormat("h:mm");
                SimpleDateFormat endTimeFormatter = new SimpleDateFormat("h:mm aaa");


                String eventDateStr = "";
                String startTime = "";
                String endTime = "";
                Date endDate = null;
                try {
                    Date startDate = dateFormat.parse(upcomingModel.getStart_time());
                    endDate = dateFormat.parse(upcomingModel.getEnd_time());

                    String date = dateFormatter.format(startDate);
                    if (date.endsWith("01") && !date.endsWith("11"))
                        dateFormatter = new SimpleDateFormat("d'st' MMMM");

                    else if (date.endsWith("02") && !date.endsWith("12"))
                        dateFormatter = new SimpleDateFormat("d'nd' MMMM");

                    else if (date.endsWith("03") && !date.endsWith("13"))
                        dateFormatter = new SimpleDateFormat("d'rd' MMMM");

                    else
                        dateFormatter = new SimpleDateFormat("d'th' MMMM");

                    eventDateStr = dateFormatter.format(startDate);
                    startTime = startTimeFormatter.format(startDate);
                    endTime = endTimeFormatter.format(endDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                eventVenue.setText(upcomingModel.getVenue());
                eventLocation.setText(upcomingModel.getLocation());
                eventDate.setText(eventDateStr);
                eventTime.setText(startTime + " - " + endTime);
                eventCost.setText(upcomingModel.getCost());
                Util.loadImage(eventImage, upcomingModel.getImage_url(), Util.getProgressDrawable(eventImage.getContext()));
            }

        }

        @Override
        public void onClick(View v) {
            int callPermissionCheck = ContextCompat.checkSelfPermission(v.getContext(), android.Manifest.permission.CALL_PHONE);

            if (callPermissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        (Activity) v.getContext(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        0);
            }else {
                int pos = getLayoutPosition();
                String phoneNo = upcomingEvents.get(pos).getPhone_number();
                if (phoneNo != null) {
                    String phone = "+44 7466887291";
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phone));
                    v.getContext().startActivity(intent);
                }
            }


        }

    }

}
