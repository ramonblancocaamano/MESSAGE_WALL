package demo.spec;

import java.util.List;

/**
 * @Author: BLANCO CAAMANO, Ramon <ramonblancocaamano@gmail.com>
 */
public interface UserAccess {
    String getUser();
    Message getLast();
    int getNumber();
    void put(String msg);
    boolean delete(int index);
    List<Message> getAllMessages();
}
