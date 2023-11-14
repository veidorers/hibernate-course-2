package com.example.listener;

import com.example.entity.Chat;
import com.example.entity.UserChat;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;

public class UserChatListener {
    @PostPersist
    public void incrementCounter(UserChat userChat) {
        var chat = userChat.getChat();
        chat.setCount(chat.getCount() + 1);
    }

    @PostRemove
    public void decrementCounter(UserChat userChat) {
        var chat = userChat.getChat();
        chat.setCount(chat.getCount() - 1);
    }

}
