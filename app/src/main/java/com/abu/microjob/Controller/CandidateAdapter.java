package com.abu.microjob.Controller;




import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.abu.microjob.Fragments.YourSelectedJobItemFragment;
import com.abu.microjob.Model.AppliedJobModel;
import com.abu.microjob.Model.CandidateModel;
import com.abu.microjob.R;
import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.ViewHolder> {
    List<CandidateModel> candidateList ;
    List<AppliedJobModel> allAppliedJobList ;
    String JOB_ID;
    Activity activity;
    public CandidateAdapter(List<CandidateModel> candidateList, List<AppliedJobModel> allAppliedJobList, String jobId, Activity activity) {
        this.candidateList = candidateList;
        this.activity = activity;
        this.allAppliedJobList = allAppliedJobList;
        this.JOB_ID = jobId;
    }

    @NonNull
    @Override
    public CandidateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("step5", "on bind");

        View v = LayoutInflater.from(activity).inflate(R.layout.candidate_item_demo_design, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        Log.d("step5", candidateList.size()+"");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateAdapter.ViewHolder holder, int position) {
        CandidateModel user = candidateList.get(position);
        holder.cName.setText(user.getC_name());
        holder.cPhone.setText("0"+user.getC_phone());
        holder.cGender.setText(mGetGenderText(user.getC_gender()));
        holder.cAge.setText(mGetCurrentAge(user.getC_bod_day(), user.getC_bod_month(), user.getC_bod_year())+" years old.");

        int[] bool_pos = mIsThisCandidateRecruited(user.getC_id());
        if (bool_pos[0] != -1 && bool_pos[1] != -1 && allAppliedJobList.get(bool_pos[1]).isIs_applied() && allAppliedJobList.get(bool_pos[1]).isIs_recruited() ){
            holder.tvRecruiterText.setText("Recruited");
        }else{
            holder.tvRecruiterText.setText("Recruit");
        }
//        else if (bool_pos[0] != -1 && bool_pos[1] != -1 && allAppliedJobList.get(bool_pos[1]).isIs_applied() && !allAppliedJobList.get(bool_pos[1]).isIs_recruited() ) {
//
//        }


        Glide.with(activity)
                .load(user.getC_img_url())
                .into(holder.cProfile);

        holder.cCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOpenDialPad(user.getC_phone());
            }
        });
        holder.clRecruitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                user.getC_id();
                ((MyRecruiterListener) new YourSelectedJobItemFragment()).mGetRecruiterCandidateId(user.getC_id(), activity);
//                ((MyRecruiterListener) new YourSelectedJobItemFragment()).mGetRecruiterCandidateId(user.getC_id());
//                ((new YourSelectedJobItemFragment())).mRecruitePrepare(user.getC_id());
            }
        });

    }
    private int[] mIsThisCandidateRecruited(String cId){
        int[] bool_pos = {-1, -1};
        for (int i = 0; i<allAppliedJobList.size(); i++){
            if (allAppliedJobList.get(i).getJob_id().equals(JOB_ID)){
                if(allAppliedJobList.get(i).getWorker_id().equals(cId)){
                    bool_pos[0] = 1;
                    bool_pos[1] = i;
                }
            }
        }


        return bool_pos;
    };
    public interface MyRecruiterListener{
        void mGetRecruiterCandidateId(String cId, Activity activity);
    }

    private void mOpenDialPad(String rPhone) {
        Intent dial_intent = new Intent(Intent.ACTION_DIAL);
        dial_intent.setData(Uri.parse("tel:+880"+rPhone));
        activity.startActivity(dial_intent);
    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cName, cPhone, cGender, cAge, tvRecruiterText;
        CircleImageView cProfile;
        ImageView cCall;
        ConstraintLayout clRecruitBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cName = (TextView) itemView.findViewById(R.id.cand_tv_name_id);
            cPhone = (TextView) itemView.findViewById(R.id.cand_tv_phone_id);
            cGender = (TextView) itemView.findViewById(R.id.cand_tv_gender_id);
            cAge = (TextView) itemView.findViewById(R.id.cand_tv_age_id);
            tvRecruiterText = (TextView) itemView.findViewById(R.id.tv_recruiter_txt_id);
            cProfile = (CircleImageView) itemView.findViewById(R.id.cand_iv_img_id);
            cCall = (ImageView) itemView.findViewById(R.id.cand_iv_call_id);
            clRecruitBtn = (ConstraintLayout) itemView.findViewById(R.id.cand_cl_recruiter_btn_id);



        }
    }







    public String mGetGenderText(String g){
        if (g.equals("m")){
            return "Male";
        }else {
            return "Female";
        }
    };
    public String mGetCurrentAge(String d, String m, String y){
        int bod_day = Integer.valueOf(d);
        int bod_month = Integer.valueOf(m);
        int bod_year = Integer.valueOf(y);

        Calendar today = Calendar.getInstance();
        int current_year = today.get(Calendar.YEAR);
        int current_month = today.get(Calendar.MONTH)+1;
        int current_day = today.get(Calendar.DAY_OF_MONTH);

        int age = current_year - bod_year;
        if (current_month<bod_month || (current_month == bod_month && current_day<bod_day )){
            age--;
        }
        return String.valueOf(age);
    };

}
