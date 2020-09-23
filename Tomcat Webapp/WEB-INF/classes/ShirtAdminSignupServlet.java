// To save as "ebookshop\WEB-INF\classes\QueryServlet.java".

import java.io.*;

import java.sql.*;

import java.util.*;

import javax.servlet.*;

import javax.servlet.http.*;

import javax.servlet.annotation.*;



@WebServlet("/shirtadminsignup")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)

public class ShirtAdminSignupServlet extends HttpServlet {



   // The doGet() runs once per HTTP GET request to this servlet.

   @Override

   public void doGet(HttpServletRequest request, HttpServletResponse response)

               throws ServletException, IOException {

      // Set the MIME type for the response message

      response.setContentType("text/html");

      // Get a output writer to write the response message into the network socket

      PrintWriter out = response.getWriter();

      out.println("<html>");

      out.println("<head><link rel='stylesheet' type-='text/css' href='css/welcome.css'/></head>");

      out.println("<body>");

      
      // Print an HTML page as the output of the query

      try (

         // Step 1: Allocate a database 'Connection' object

         Connection conn = DriverManager.getConnection(

               "jdbc:mysql://localhost:3306/shirtsale?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",

               "jiawei", "xxxx");   // For MySQL

               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"



         // Step 2: Allocate a 'Statement' object in the Connection

         Statement stmt = conn.createStatement();

      ) {
         // Step 3 & 4: Execute a SQL SELECT query and Process the query result

         // Retrieve the books' id. Can order more than one books.
         String name = request.getParameter("name");

         String sqlStr = "select id, name from admin_acc where name ='" + request.getParameter("name") + "'";

         ResultSet rset = stmt.executeQuery(sqlStr);
         rset.next();
         int cust_id = Integer.parseInt(rset.getString("id"));
         String cust_name = rset.getString("name");
         rset.close();

         out.println("<h2 style='text-align: center;'>Assign New Admin</h2>");
         out.println("<h4 style='text-align: center;'>Please key in the following details to make an admin account</h4><p style='text-align: center;'>&nbsp;</p><form action='shirtnewadmin' method='get'>");
    
         out.println("<table style='margin-right: auto; margin-left: auto;'width='279'");
         out.println("<tbody>");
         out.println("<tr>");
         out.println("<td>Name:</td>");
         out.println("<td><input name='name' type='text' /></td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>Email:</td>");
         out.println("<td><input name='email' type='text' /></td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>Password:</td>");
         out.println("<td><input name='password' type='password' /></td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td>Phone No.:</td>");
         out.println("<td><input name='phone' /></td>");
         out.println("</tr>");
         out.println("<tr>");
         out.println("<td style='text-align: center;' colspan='2'>&nbsp;<input type='submit' value='     Submit       ' /></td>");
         out.println("<input type='hidden' name='admin_name' value='" + name + "'>");  
   
         out.println("</tr>");
         out.println("</tbody>");
         out.println("</table>");
         out.println("</form>");

         String login = "SELECT * FROM admin_acc where id=" + cust_id;
         ResultSet rsetlogin = stmt.executeQuery(login);
         rsetlogin.next();

         String stored_password = rsetlogin.getString("password");
         String stored_email = rsetlogin.getString("email");      
         
         out.println("<form action = 'shirtwelcome'>");
         out.println("<input type='hidden' name='domain' value='Admin'>");
         out.println("<input type='hidden' name='email' value='" + stored_email + "'>");
         out.println("<input type='hidden' name='password' value='" + stored_password + "'>");
         out.println("<button type='submit' >Back To Dashboard</button>");
         out.println("</form>");

         out.println("</body>");
         out.println("</html>");







      } catch(Exception ex) {

         out.println("<p>Error: " + ex.getMessage() + "</p>");

         out.println("<p>Check Tomcat console for details.</p>");

         ex.printStackTrace();

      }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)

 

      out.println("</body></html>");

      out.close();

   }

}