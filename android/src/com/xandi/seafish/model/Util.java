package com.xandi.seafish.model;

import com.google.firebase.database.DatabaseReference;

public class Util {

    public static DatabaseReference mDatabaseRankingRef;

    public static DatabaseReference getmDatabaseRankingRef() {
        return mDatabaseRankingRef;
    }

    public static void setmDatabaseRankingRef(DatabaseReference mDatabaseRankingRef) {
        Util.mDatabaseRankingRef = mDatabaseRankingRef;
    }
}