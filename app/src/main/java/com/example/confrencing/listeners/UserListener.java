package com.example.confrencing.listeners;

import com.example.confrencing.models.User;

public interface UserListener {

    void initiateVideoMeeting(User user);

    void initiateAudioMeeting(User user);

}
