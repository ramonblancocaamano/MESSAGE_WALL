package demo.impl;

import demo.spec.Message;
import demo.spec.MessageWall;
import demo.spec.RemoteLogin;
import demo.spec.UserAccess;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: BLANCO CAAMANO, Ramon <ramonblancocaamano@gmail.com>
 */
public class MessageWall_and_RemoteLogin_Impl implements RemoteLogin, MessageWall {    
    private List<Message> messages;
    private UserAccess_Impl userAccess;

    public MessageWall_and_RemoteLogin_Impl() {
        messages = new ArrayList<Message>();
    }

    @Override
    public UserAccess connect(String user, String password) {       
        userAccess = new UserAccess_Impl(this, user);
        return userAccess;
    }
    
    public UserAccess disconnect(String user) {
        if(userAccess.getUser().equals(user)) {
            userAccess = null;
        }
        return userAccess;
    }

    @Override
    public void put(String user, String msg) {
        Message_Impl message = new Message_Impl(user, msg);
        messages.add(message);
    }

    @Override
    public boolean delete(String user, int index) {
        if (user.equals("ADMIN")) {
            messages.remove(index);
            return true;
        } else if (messages.get(index).getOwner().equals(user)) {
            messages.remove(index);
            return true;
        }
        return false;
    }

    @Override
    public Message getLast() {
        Message message = messages.get(getNumber());
        return message;
    }

    @Override
    public int getNumber() {
        int number = messages.size() - 1;
        return number;
    }

    @Override
    public List<Message> getAllMessages() {
        return messages;
    }

}
