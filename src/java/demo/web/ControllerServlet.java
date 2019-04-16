package demo.web;

import demo.impl.MessageWall_and_RemoteLogin_Impl;
import demo.impl.UserAccess_Impl;
import demo.spec.RemoteLogin;
import demo.spec.UserAccess;
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

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        process(request, response);
    }

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
            String user;
            String password;
            MessageWall_and_RemoteLogin_Impl messageWall_and_RemoteLogin_Impl;
            UserAccess_Impl userAccess_Impl;

            messageWall_and_RemoteLogin_Impl = new MessageWall_and_RemoteLogin_Impl();

            user = request.getParameter("user");
            password = request.getParameter("password");

            userAccess_Impl = (UserAccess_Impl) messageWall_and_RemoteLogin_Impl.connect(user, password);

            session.setAttribute("useraccess", userAccess_Impl);

            return "/view/wallview.jsp";
        } else if (serv_path.equals("/put.do")) {
            String user;
            String message;

            MessageWall_and_RemoteLogin_Impl messageWall_and_RemoteLogin_Impl;
            UserAccess_Impl userAccess_Impl;

            userAccess_Impl = (UserAccess_Impl) session.getAttribute("useraccess");
            message = request.getParameter("msg");

            user = userAccess_Impl.getUser();
            if (user == null) {
                return "/error-not-loggedin.html";
            }

            if (message != null) {
                userAccess_Impl.put(message);
                session.setAttribute("useraccess", userAccess_Impl);
            }

            return "/view/wallview.jsp";
        } else if (serv_path.equals("/delete.do")) {
            String user;
            int index;

            MessageWall_and_RemoteLogin_Impl messageWall_and_RemoteLogin_Impl;
            UserAccess_Impl userAccess_Impl;

            userAccess_Impl = (UserAccess_Impl) session.getAttribute("useraccess");
            

            user = userAccess_Impl.getUser();
            if (user == null) {
                return "/error-not-loggedin.html";
            }
            
            index = Integer.parseInt(request.getParameter("index"));
            
            userAccess_Impl.delete(index);
            session.setAttribute("useraccess", userAccess_Impl);
            
            return "/view/wallview.jsp";         
        } else if (serv_path.equals("/refresh.do")) {
            //...
            return "/error-not-loggedin.html";
        } else if (serv_path.equals("/logout.do")) {
            //...
            return "/goodbye.html";
        } else {
            return "/error-bad-action.html";
        }
    }

    public RemoteLogin getRemoteLogin() {
        return (RemoteLogin) getServletContext().getAttribute("remoteLogin");
    }

    public void forwardRequest(HttpServletRequest request, HttpServletResponse response, String view)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(view);
        if (dispatcher == null) {
            throw new ServletException("No dispatcher for view path '" + view + "'");
        }
        dispatcher.forward(request, response);
    }
}

