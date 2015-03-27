package Controller;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Statement;
import dao.Basket;

import dao.User;
import java.util.ArrayList;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            String driver = getServletContext().getInitParameter("driver");
            String url = getServletContext().getInitParameter("url");
            String dbUser = getServletContext().getInitParameter("dbUser");
            String dbPassword = getServletContext().getInitParameter("dbPassword");

            Class.forName(driver);
            java.sql.Connection con = DriverManager.getConnection(url, dbUser, dbPassword);
            Statement st = (Statement) con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users WHERE userNick='"
                    + username + "'");
            if (rs.next()) {
                if (rs.getString(8).equals(password)) {
                    User user = new User();
                    user.setId(rs.getInt(1));
                    user.setUsername(rs.getString(2));
                    user.setName(rs.getString(3));
                    user.setSurname(rs.getString(4));
                    user.setPoints(rs.getInt(5));
                    user.setEmail(rs.getString(6));
                    user.setBlockade(rs.getBoolean(7));
                    user.setPassword(rs.getString(8));

                    req.getSession().setAttribute("user", user);
                    req.getSession().setAttribute("totalBasket", 0);
                    req.getSession().setAttribute("countBasket", 0);
                    req.getSession().setAttribute("listBasket", new ArrayList<Basket>());
                    resp.sendRedirect("Home");

                } else {

                    System.out.println("Zle haslo, sprobuj ponownie");
                    resp.sendRedirect("login.jsp");
                }
            } else {

                System.out.println("Zle haslo, sprobuj ponownie");
                resp.sendRedirect("login.jsp");
            }

        } catch (Exception e) {
            System.out.println("Blad !!");
        }

    }

}
