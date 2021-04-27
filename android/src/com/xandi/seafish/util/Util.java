package com.xandi.seafish.util;

import com.google.firebase.database.DatabaseReference;

public class Util {

    public static DatabaseReference mDatabaseRankingRef;
    public static DatabaseReference mDatabaseUserRef;

    public static void setmDatabaseRankingRef(DatabaseReference mDatabaseRankingRef) {
        Util.mDatabaseRankingRef = mDatabaseRankingRef;
    }

    public static void setmDatabaseUserRef(DatabaseReference mDatabaseUserRef) {
        Util.mDatabaseUserRef = mDatabaseUserRef;
    }
}