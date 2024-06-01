package com.abu.microjob.Controller;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.abu.microjob.MainActivity;
import com.abu.microjob.Model.AppliedJobModel;
import com.abu.microjob.Model.JobModel;
import com.abu.microjob.R;

import java.util.List;

public class CandidateHomeRecyclerAdapter extends RecyclerView.Adapter<CandidateHomeRecyclerAdapter.ViewHolder> {
    private List<JobModel> jobList;
    private List<AppliedJobModel> appliedJobList;
    MySendJobItemsDataToDetailsViewListener detailsViewListener;
    Activity activity;
    private String userId;

    public CandidateHomeRecyclerAdapter(List<JobModel> jobList, List<AppliedJobModel> appliedJobList,String user_id, Activity activity, MySendJobItemsDataToDetailsViewListener listener) {
        this.detailsViewListener = (MySendJobItemsDataToDetailsViewListener) listener;
        this.jobList = jobList;
        this.userId = user_id;
        this.appliedJobList = appliedJobList;
        this.activity = activity;
    }
    public CandidateHomeRecyclerAdapter(List<JobModel> jobList, Activity activity, MySendJobItemsDataToDetailsViewListener listener) {
        this.detailsViewListener = (MySendJobItemsDataToDetailsViewListener) listener;
        this.jobList = jobList;
        this.activity = activity;
    }

    public interface MySendJobItemsDataToDetailsViewListener{
        void mSendItemDataToDisplay(JobModel item);
    };


    @NonNull
    @Override
    public CandidateHomeRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.worker_home_job_item_demo_design, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateHomeRecyclerAdapter.ViewHolder holder, int position) {
        JobModel item = jobList.get(position);
        holder.tvTitle.setText(item.getJob_title());
        holder.tvPayment.setText(item.getJob_payment()+"  BDT");
        holder.tvLoc.setText(item.getJob_loc());
        holder.tvTypeDur.setText(mItemDurationTimeAvailabilityAutomation(item));
        holder.tvPostTime.setText(mGetTimeAgo(String.valueOf(item.getWork_order_place_time())));
        holder.tvAddr.setText(mMakeAddrTextFormated(item.getJob_address()));

        if (appliedJobList != null){

            if (userId != null){

                int[] bool_pos = isJobIdFound(item.getJob_id(), userId);
                if (bool_pos[0] != -1 && bool_pos[1] != -1  &&  appliedJobList.get(bool_pos[1]).isIs_applied() && appliedJobList.get(bool_pos[1]).isIs_recruited()){
                    holder.tvApply.setText("You are hired!");
                }else if (bool_pos[0] != -1 && bool_pos[1] != -1 &&  appliedJobList.get(bool_pos[1]).isIs_applied() && !appliedJobList.get(bool_pos[1]).isIs_recruited()){
                    holder.tvApply.setText("Applied");
                }else {
                    holder.tvApply.setText("Apply");
                }

            }else{
                Log.d("step10", userId+"      : vvv");
            }

        }else {
            holder.tvApply.setText("Apply");
        }

        holder.llApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] data = {item.getJob_id(), item.getWork_provider_id()};
                ((MainActivity) activity).mPerformApply(data, activity);
//                ((MainActivity) activity).mPerformApply(data, activity, holder);
            }
        });
    }

//    private boolean isJobIdFound(String target_id, String uId){
    private int[] isJobIdFound(String target_id, String uId){
//        boolean b = false;
        int[] bool_pos = {-1, -1};//first is for true false 0,1,    second is for pos
        for (int i = 0; i<appliedJobList.size(); i++){
            if (appliedJobList.get(i).getJob_id().equals(target_id)){
                if (appliedJobList.get(i).getWorker_id().equals(uId)){
                    bool_pos[0] = 1;
                    bool_pos[1] = i;
//                    b = true;
                }else {
//                    b = false;
                }
            }else{
//                b = false;
            };
        }
        return bool_pos;
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPayment, tvLoc, tvTypeDur, tvPostTime, tvAddr, tvApply;
        LinearLayout llApplyBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_worker_job_item_title_id);
            tvPayment = (TextView) itemView.findViewById(R.id.tv_worker_job_item_payment_id);
            tvLoc = (TextView) itemView.findViewById(R.id.tv_worker_job_item_loc_id);
            tvTypeDur = (TextView) itemView.findViewById(R.id.tv_worker_job_item_dur_id);
            tvPostTime = (TextView) itemView.findViewById(R.id.tv_worker_job_item_time_ago_id);
            tvAddr = (TextView) itemView.findViewById(R.id.tv_worker_job_item_addr_id);
            tvApply = (TextView) itemView.findViewById(R.id.tv_worker_job_item_apply_id);
            llApplyBtn = (LinearLayout) itemView.findViewById(R.id.ll_worker_job_item_apply_btn_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p = getAdapterPosition();
                    JobModel item = jobList.get(p);
                    detailsViewListener.mSendItemDataToDisplay(item);
                }
            });




        }
    }








    private String mMakeAddrTextFormated(String jobLoc) {
        if (jobLoc.length() > 12){
            return jobLoc.substring(0, 13)+"...";
        }else {
            return  jobLoc;
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
