//package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;
//
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import java.util.ArrayList;
//
//import static com.meagerfindings.matgreten.c196_student_scheduler_mat_greten.ScheduleContract.TABLE_MENTORS;
//
///**
// * Created by matgreten on 9/6/17.
// */
//
//class Mentor {
//
//    private String userName;
//
//    public Mentor(String userName){
//        this.userName = userName;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public ArrayList<Mentor> getMentors(String courseID){
//        ArrayList<Mentor> mentorNames = new ArrayList<>();
//        ScheduleDBHelper handler = new ScheduleDBHelper(this);
//        String queryString = "SELECT " + ScheduleContract.MentorEntry.MENTOR_NAME +
//                " FROM " + TABLE_MENTORS +
//                " WHERE " + ScheduleContract.MentorEntry.MENTOR_COURSE_ID_FK + " = " + courseID;
//        SQLiteDatabase db = handler.getWritableDatabase();
//        Cursor termCursor = db.rawQuery(queryString, null);
//        if (termCursor.moveToFirst())
//            do mentorNames.add(new Mentor(termCursor.getString(0))); while (termCursor.moveToNext());
//        termCursor.close();
//        db.close();
//
//        if (mentorNames.isEmpty()) {
//            mentorNames.add(new Mentor("No Course Mentors have been added yet."));
//            mentorNames.add(new Mentor("Black canvas Slippers"));
//            mentorNames.add(new Mentor("Third time's a charm."));
//        }
//
//        return mentorNames;
//    }
//
//
//
//}
