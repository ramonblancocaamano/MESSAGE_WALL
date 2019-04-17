package demo.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import demo.impl.MessageWall_and_RemoteLogin_Impl;
import demo.impl.UserAccess_Impl;

/**
 * @Author: BLANCO CAAMANO, Ramon <ramonblancocaamano@gmail.com>
 */
public class Action {

    public Action() {
    }

    /**
     * It logs a user in wall message web platform.
     * @param session
     * @param request
     * @return 
     */
    public String login(HttpSession session, HttpServletRequest request) {
        String user;
        String password;

        MessageWall_and_RemoteLogin_Impl messageWall;
        UserAccess_Impl userAccess;

        userAccess = (UserAccess_Impl) session.getAttribute("useraccess");

        if (userAccess != null) {
            logout(session);
        }

        user = request.getParameter("user");
        password = request.getParameter("password");

        if ((user == null) | (password == null)) {
            return "/login.html";
        } else if ((user.equals("")) | (password.equals(""))) {
            return "/login.html";
        }

        messageWall = (MessageWall_and_RemoteLogin_Impl) session.getAttribute("messagewall");
        if (messageWall == null) {
            messageWall = new MessageWall_and_RemoteLogin_Impl();
        }
        userAccess = (UserAccess_Impl) messageWall.connect(user, password);

        session.setAttribute("messagewall", messageWall);
        session.setAttribute("useraccess", userAccess);
        return "/view/wallview.jsp";
    }

    /**
     * A register user put a message in message wall web platform.
     * @param session
     * @param request
     * @return 
     */
    public String put(HttpSession session, HttpServletRequest request) {
        String user;
        String message;

        UserAccess_Impl userAccess;
        MessageWall_and_RemoteLogin_Impl messageWall;

        userAccess = (UserAccess_Impl) session.getAttribute("useraccess");
        user = userAccess.getUser();

        if (user == null) {
            return "/error-not-loggedin.html";
        }

        message = request.getParameter("msg");
        if (message == null) {
            return "/view/wallview.jsp";
        } else if (message.equals("")) {
            return "/view/wallview.jsp";
        }

        messageWall = (MessageWall_and_RemoteLogin_Impl) session.getAttribute("messagewall");
        if (messageWall == null) {
            messageWall = new MessageWall_and_RemoteLogin_Impl();
        }
        messageWall.put(user, message);
        session.setAttribute("messagewall", messageWall);

        return "/view/wallview.jsp";
    }

    /**
     * A register user delete a message in message wall web platform.
     * @param session
     * @param request
     * @return 
     */
    public String delete(HttpSession session, HttpServletRequest request) {
        String user;
        String delete;
        int index;

        UserAccess_Impl userAccess;
        MessageWall_and_RemoteLogin_Impl messageWall;

        userAccess = (UserAccess_Impl) session.getAttribute("useraccess");
        user = userAccess.getUser();

        if (user == null) {
            return "/error-not-loggedin.html";
        }

        delete = request.getParameter("index");
        if (delete == null) {
            return "/view/wallview.jsp";
        }

        index = Integer.parseInt(delete);

        messageWall = (MessageWall_and_RemoteLogin_Impl) session.getAttribute("messagewall");
        if (messageWall == null) {
            messageWall = new MessageWall_and_RemoteLogin_Impl();
        } else {
            messageWall.delete(user, index);
        }

        session.setAttribute("messagewall", messageWall);
        return "/view/wallview.jsp";
    }

    /**
     * A register user reflesh message wall web platform.
     * @param session
     * @return 
     */
    public String refresh(HttpSession session) {
        String user;

        UserAccess_Impl userAccess;

        userAccess = (UserAccess_Impl) session.getAttribute("useraccess");
        user = userAccess.getUser();

        if (user == null) {
            return "/error-not-loggedin.html";
        }
        return "/view/wallview.jsp";
    }

    /**
     * It logs out a user in wall message web platform.
     * @param session
     * @return 
     */
    public String logout(HttpSession session) {
        String user;

        UserAccess_Impl userAccess;
        MessageWall_and_RemoteLogin_Impl messageWall;

        userAccess = (UserAccess_Impl) session.getAttribute("useraccess");
        user = userAccess.getUser();

        if (user == null) {
            return "/goodbye.html";
        }

        messageWall = (MessageWall_and_RemoteLogin_Impl) session.getAttribute("messagewall");
        if (messageWall == null) {
            messageWall = new MessageWall_and_RemoteLogin_Impl();
        }

        userAccess = (UserAccess_Impl) messageWall.disconnect(user);

        session.setAttribute("messagewall", messageWall);
        session.setAttribute("useraccess", userAccess);
        return "/goodbye.html";
    }

}
