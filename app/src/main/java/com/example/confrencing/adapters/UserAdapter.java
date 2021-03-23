package com.example.confrencing.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.confrencing.R;
import com.example.confrencing.listeners.UserListener;
import com.example.confrencing.models.User;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private List<User> users;
    private UserListener listener;

    public UserAdapter(List<User> users , UserListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_user,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }



    public class UserViewHolder extends RecyclerView.ViewHolder{

        private TextView email;
        private ImageView imageAudioCall, imageVideoCall;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            email = itemView.findViewById(R.id.User_Email);
            imageAudioCall = itemView.findViewById(R.id.AudioCall);
            imageVideoCall = itemView.findViewById(R.id.VideoCall);

        }

        void setUserData(User user){
            email.setText(user.EMAIL);

            imageAudioCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.initiateAudioMeeting(user);
                }
            });

            imageVideoCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.initiateVideoMeeting(user);
                }
            });
        }
    }
}
