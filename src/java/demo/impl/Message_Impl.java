package demo.impl;

import demo.spec.Message;

/**
 * @Author: BLANCO CAAMANO, Ramon <ramonblancocaamano@gmail.com>
 */
public class Message_Impl implements Message, java.io.Serializable {

    private String user, message;

    public Message_Impl(String user, String msg) {
        this.user = user;
        this.message = msg;
    }

    @Override
    public String getContent() {
        return message;
    }

    @Override
    public String getOwner() {
        return user;
    }
    
}

