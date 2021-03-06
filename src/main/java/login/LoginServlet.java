package login;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String u = request.getParameter("u");
        String p = request.getParameter("p").trim();

        int value = -1;
        String hp  =  new DBOper().getHashPassword(u);
        if(hp!=null) {
            String hashPassword = hp.trim();


            if (Password.check(p, hashPassword)) {
                DBOper d = new DBOper();
                value = d.login(u, hashPassword);
            }
        }

        if(value!=-1) { // user logat
            HttpSession session = request.getSession();
            session.setAttribute("userid", value);
            session.setAttribute("username", u);
            session.removeAttribute("flag");
            System.out.println("LoginServlet: bravoooo  ");

            response.sendRedirect("opinions.jsp");
        }
        else {
            System.out.println("LoginServlet: user/pwd NOT FOUND in db, retry a new one on the UI ");
            String back = "/index.jsp";
            HttpSession session = request.getSession();
            session.removeAttribute("userid");
            session.removeAttribute("username");
            session.setAttribute("flag", true);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(back);
            dispatcher.forward(request, response);
        }
    }
}
