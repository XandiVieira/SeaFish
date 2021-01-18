package com.xandi.seafish.model;

import com.google.firebase.database.DatabaseReference;

public class Util {

    public static DatabaseReference mDatabaseRankingRef;
    public static DatabaseReference mDatabaseUserRef;
    public static String userUid;

    public static DatabaseReference getmDatabaseRankingRef() {
        return mDatabaseRankingRef;
    }

    public static void setmDatabaseRankingRef(DatabaseReference mDatabaseRankingRef) {
        Util.mDatabaseRankingRef = mDatabaseRankingRef;
    }

    public static DatabaseReference getmDatabaseUserRef() {
        return mDatabaseUserRef;
    }

    public static void setmDatabaseUserRef(DatabaseReference mDatabaseUserRef) {
        Util.mDatabaseUserRef = mDatabaseUserRef;
    }

    public static String getUserUid() {
        return userUid;
    }

    public static void setUserUid(String userUid) {
        Util.userUid = userUid;
    }
}