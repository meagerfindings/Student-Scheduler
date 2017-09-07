//package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
///**
// * Created by matgreten on 9/6/17.
// */
//
//class CourseMentorAdapter extends ArrayAdapter<Mentor>{
//    public CourseMentorAdapter(Context context, ArrayList<Mentor> mentors){
//        super(context, 0, mentors);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent){
//        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mentor_names, parent, false);
//
//        Mentor mentor = getItem(position);
//
//        TextView tvCourseMentorName = (TextView) convertView.findViewById(R.id.tvCourseMentorName);
//        tvCourseMentorName.setText(mentor.getUserName());
//
//        return convertView;
//    }
//
//}
