package demo.web;

import demo.impl.MessageWall_and_RemoteLogin_Impl;
import demo.impl.UserAccess_Impl;
import demo.spec.RemoteLogin;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author: BLANCO CAAMANO, Ramon <ramonblancocaamano@gmail.com>
 */
public class ControllerServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        process(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        process(request, response);
    }

    protected void process(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String view = perform_action(request);
        forwardRequest(request, response, view);
    }

    protected String perform_action(HttpServletRequest request)
            throws IOException, ServletException {

        String serv_path = request.getServletPath();
        HttpSession session = request.getSession();

        if (serv_path.equals("/login.do")) {
            return login(session, request);
        } else if (serv_path.equals("/put.do")) {
            return put(session, request);
        } else if (serv_path.equals("/delete.do")) {
            return delete(session, request);
        } else if (serv_path.equals("/refresh.do")) {
            return refresh(session);
        } else if (serv_path.equals("/logout.do")) {
            return logout(session);
        } else {
            return "/error-bad-action.html";
        }

    }

    public UserAccess_Impl getUserAccess(HttpSession session) {
        return (UserAccess_Impl) session.getAttribute("useraccess");
    }

    public void setUserAccess(HttpSession session, UserAccess_Impl userAccess) {
        session.setAttribute("useraccess", userAccess);
    }

    public MessageWall_and_RemoteLogin_Impl getMessageWall() {
        return (MessageWall_and_RemoteLogin_Impl) getServletContext().getAttribute("remoteLogin");
    }

    public void setMessageWall(MessageWall_and_RemoteLogin_Impl messageWall) {
        getServletContext().setAttribute("remoteLogin", messageWall);
    }

    public void forwardRequest(HttpServletRequest request, HttpServletResponse response, String view)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(view);
        if (dispatcher == null) {
            throw new ServletException("No dispatcher for view path '" + view + "'");
        }
        dispatcher.forward(request, response);
    }

    private void contextInitialized() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public String login(HttpSession session, HttpServletRequest request) {
        String user;
        String password;
        UserAccess_Impl userAccess;

        if (getUserAccess(session) != null) {
            logout(session);
        }

        user = request.getParameter("user");
        password = request.getParameter("password");
        if ((user == null) | (password == null)) {
            return "/login.html";
        } else if ((user.equals("")) | (password.equals(""))) {
            return "/login.html";
        }

        userAccess = (UserAccess_Impl) getMessageWall().connect(user, password);

        setUserAccess(session, userAccess);
        return "/view/wallview.jsp";
    }

    public String put(HttpSession session, HttpServletRequest request) {
        String user;
        String message;
        MessageWall_and_RemoteLogin_Impl messageWall;

        user = getUserAccess(session).getUser();
        if (user == null) {
            return "/error-not-loggedin.html";
        }

        message = request.getParameter("msg");
        if (message == null) {
            return "/view/wallview.jsp";
        } else if (message.equals("")) {
            return "/view/wallview.jsp";
        }

        messageWall = getMessageWall();
        getMessageWall().put(user, message);
        
        setMessageWall(messageWall);
        return "/view/wallview.jsp";
    }

    public String delete(HttpSession session, HttpServletRequest request) {
        String user;
        String delete;
        MessageWall_and_RemoteLogin_Impl messageWall;
        
        user = getUserAccess(session).getUser();

        if (user == null) {
            return "/error-not-loggedin.html";
        }

        delete = request.getParameter("index");
        if (delete == null) {
            return "/view/wallview.jsp";
        }

        messageWall = getMessageWall();
        messageWall.delete(user, Integer.parseInt(delete));
        
        setMessageWall(messageWall); 
        return "/view/wallview.jsp";
    }

    public String refresh(HttpSession session) {
        String user;

        user = getUserAccess(session).getUser();
        if (user == null) {
            return "/error-not-loggedin.html";
        }
        return "/view/wallview.jsp";
    }

    public String logout(HttpSession session) {
        String user;
        UserAccess_Impl userAccess;

        user = getUserAccess(session).getUser();
        if (user == null) {
            return "/goodbye.html";
        }
        
        userAccess = (UserAccess_Impl) getMessageWall().disconnect(user);
        
        setUserAccess(session, userAccess);
        return "/goodbye.html";
    }
    
}
