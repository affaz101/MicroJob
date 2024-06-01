package com.abu.microjob.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abu.microjob.Fragments.HomeFragment;
import com.abu.microjob.Interface.MyOnYourSelectedJobItemIsSelectedListener;
import com.abu.microjob.MainActivity;
import com.abu.microjob.Model.JobModel;
import com.abu.microjob.R;

import java.util.Collections;
import java.util.List;

public class YourJobAdapter extends RecyclerView.Adapter<YourJobAdapter.ViewHolder> {
    List<JobModel> jobList;
//    MyOnYourSelectedJobItemIsSelectedListener listener;
    MySendJobItemDataListener mySendJobItemDataListener;
    public YourJobAdapter(List<JobModel> jobList, MySendJobItemDataListener listener) {
        this.jobList = jobList;
        Collections.reverse(this.jobList);
        this.mySendJobItemDataListener = listener;
    }

//    public YourJobAdapter(List<JobModel> jobList, MySendJobItemDataListener listener) {
//        this.jobList = jobList;
//        Collections.reverse(this.jobList);
//        this.mySendJobItemDataListener = listener;
//    }


    public interface MySendJobItemDataListener{
        void mOnSendJobItemData(JobModel item);
    };


    @NonNull
    @Override
    public YourJobAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_job_item_demo_design, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull YourJobAdapter.ViewHolder holder, int position) {
        JobModel item = jobList.get(position);

        holder.tv_name.setText(item.getJob_title());
        holder.tv_payment.setText(item.getJob_payment()+"  BDT");
        holder.tv_loc.setText(item.getJob_loc());
        holder.tv_time.setText(mItemDurationTimeAvailabilityAutomation(item));
//        mItemDurationTimeAvailabilityAutomation(holder, item);
//        holder.tv_time.setText(String.valueOf(item.getJob_duration()).equals("null")?"":item.getJob_duration());
        holder.tv_posted_time.setText(mGetTimeAgo(String.valueOf(item.getWork_order_place_time())));
        holder.tv_addr.setText(item.getJob_address());

    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_payment, tv_loc, tv_time, tv_posted_time, tv_addr, tv_apply_count;
        View conditonal_bulet_dot;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tv_name = itemView.findViewById(R.id.your_job_item_tv_name_id);
            tv_payment = itemView.findViewById(R.id.your_job_item_tv_payment_id);
            tv_loc = itemView.findViewById(R.id.your_job_item_tv_loc_id);
            tv_time = itemView.findViewById(R.id.your_job_item_tv_time_id);
            tv_posted_time = itemView.findViewById(R.id.your_job_item_tv_posted_time_id);
            tv_addr = itemView.findViewById(R.id.your_job_item_tv_addr_id);
            tv_apply_count = itemView.findViewById(R.id.your_job_item_tv_applied_count_id);
            conditonal_bulet_dot = itemView.findViewById(R.id.conditional_hide_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p = getAdapterPosition();
                    JobModel item = jobList.get(p);
                    mySendJobItemDataListener.mOnSendJobItemData(item);
                }
            });

        }
    }


    private String mItemDurationTimeAvailabilityAutomation( JobModel item) {
        String s_dur = String.valueOf(item.getJob_duration());
        String tmp = "";
        if (s_dur.equals("null")){
            tmp = item.getJob_type();
        }else {
            tmp = String.valueOf(item.getJob_duration());
            tmp = mGetCorrectFormattedTimeText(tmp);
        }

        return tmp;
    }
    private String mGetCorrectFormattedTimeText(String time_str){

        String a = time_str.substring(6,11);//02:00
        String b = time_str.substring(12, 14);//PM
        String c = time_str.substring(19, 24);//08:45
        String d = time_str.substring(25,27);//PM

        String[] aa = a.split(":");
        String[] bb = c.split(":");
        int m = Integer.parseInt(aa[0]);
        int n = Integer.parseInt(aa[1]);
        int o = Integer.parseInt(bb[0]);
        int p = Integer.parseInt(bb[1]);

        String s = String.valueOf(m);
        s+=String.valueOf(n==0?"":":"+n);
        s+=b+"-";
        s+=String.valueOf(o);
        s+=String.valueOf(p==0?"":":"+p);
        s+=d;

        return s;

    };

    private String mGetTimeAgo(String time){
        long secondsInMilli = 1000;
        int minutesInMinute = 60;
        int hoursInDay = 24;
        int daysInMonth = 30; // approximation for simplicity
        int monthsInYear = 12;
        int secondsInMinute = 60;
        int minutesInHour = 60;
        int daysInYear = 365;


        long posted_time = Long.valueOf(time);

        long now = System.currentTimeMillis();

        long diff = now - posted_time;

        if (diff < secondsInMilli) {
            return "just now";
        }

        int seconds = (int) (diff / secondsInMilli);
        int minutes = seconds / secondsInMinute;
        int hours = minutes / minutesInHour;
        int days = hours / hoursInDay;
        int months = days / daysInMonth;
        int years = days / daysInYear;

        if (seconds < minutesInMinute) {
            return seconds + " seconds ago";
        } else if (minutes < minutesInHour) {
            return minutes + " minutes ago";
        } else if (hours < hoursInDay) {
            return hours + " hours ago";
        } else if (days < daysInMonth) {
            return days + " days ago";
        } else if (months < monthsInYear) {
            return months + " months ago";
        } else {
            return years + " years ago";
        }






    };
}
