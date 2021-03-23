package com.example.confrencing.Utils;

import java.util.HashMap;

public class Constants {

    public static final String KEY_USERS = "USERS";
    public static final String KEY_EMAIL = "EMAIL";
    public static final String KEY_PASSWORD = "PASSWORD";
    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_FCM_TOKEN = "FCM_TOKEN";

    public static final String PREFERENCE_MANAGER = "SHARED_PREFERENCE";
    public static final String KEY_USER_SIGNED_IN = "isSignIn";

    public static final String REMOTE_MESSAGE_AUTHORIZATION_KEY  = "Authorization";
    public static final String REMOTE_MESSAGE_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MESSAGE_TYPE = "type";
    public static final String REMOTE_MESSAGE_INVITATION = "invitation";
    public static final String REMOTE_MESSAGE_DATA = "data";
    public static final String REMOTE_MESSAGE_MEETING_TYPE = "meetingType";
    public static final String REMOTE_MESSAGE_INVITOR_TOKEN = "invitorToken";
    public static final String REMOTE_MESSAGE_REGISTRATION_ID = "registration_ids";

    public static final String REMOTE_MESSAGE_INVITATION_RESPONSE = "invitationResponse";

    public static final String REMOTE_MESSAGE_CALL_ACCEPTED = "accepted";
    public static final String REMOTE_MESSAGE_CALL_REJECTED = "rejected";
    public static final String REMOTE_MESSAGE_CALL_CANCELED = "canceled";

    public static final String REMOTE_MESSAGE_MEETING_ROOM = "meetingRoom";



    public static HashMap<String,String> getRemoteMessage(){
        HashMap<String,String> headers = new HashMap<>();
        headers.put(Constants.REMOTE_MESSAGE_AUTHORIZATION_KEY,
                "key=AAAA_WNB3Tk:APA91bG-7SZQ3zZnhMVeq_AcGDBgRQ8sWULpZuscfW_Jhwi86aPhVg2a3wk2OyIVsMNKZwtEH_hYz5CVLRuiQpwOh0abzxJGiSnnngHleEBKyCLFV3AHuPgybYEscgoT_0Aft8ExK9o3");
        headers.put(Constants.REMOTE_MESSAGE_CONTENT_TYPE,"application/json");
        return headers;
    }

}
