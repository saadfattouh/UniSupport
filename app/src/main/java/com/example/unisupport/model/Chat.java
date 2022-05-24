package com.example.unisupport.model;

import androidx.annotation.Nullable;

import java.net.ContentHandler;

public class Chat {

    int hisId;
    String userName;
    String userImageUrl;

    public Chat(){}

    public Chat(String usrName, int hisId, String userImageUrl) {
        this.userName = usrName;
        this.hisId = hisId;
        this.userImageUrl = userImageUrl;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public int getHisId() {
        return hisId;
    }

    public void setHisId(int hisId) {
        this.hisId = hisId;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Chat chat = (Chat) obj;
        if (chat.getHisId() == this.hisId){
            return true;
        }
        return false;
    }
}
