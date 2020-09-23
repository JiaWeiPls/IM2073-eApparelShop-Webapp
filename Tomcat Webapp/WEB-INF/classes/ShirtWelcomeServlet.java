// To save as "ebookshop\WEB-INF\classes\QueryServlet.java".

import java.io.*;

import java.sql.*;

import java.util.*;

import javax.servlet.*;

import javax.servlet.http.*;

import javax.servlet.annotation.*;



@WebServlet("/shirtwelcome")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)

public class ShirtWelcomeServlet extends HttpServlet {



   // The doGet() runs once per HTTP GET request to this servlet.

   @Override

   public void doGet(HttpServletRequest request, HttpServletResponse response)

               throws ServletException, IOException {

      // Set the MIME type for the response message

      response.setContentType("text/html");

      // Get a output writer to write the response message into the network socket

      PrintWriter out = response.getWriter();

      out.println("<html>");

      out.println("<head><link rel='stylesheet' type-='text/css' href='css/debug.css'/></head>");

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
         String domain = request.getParameter("domain");
         String email = request.getParameter("email");
         String password = request.getParameter("password");

         int phoneInt;
         ArrayList al = new ArrayList();

         if ((password == null) || (password.equals(""))) {
            al.add("PROVIDE YOUR PASSWORD");
         } 

         if ((email == null) || (email.equals(""))) {
            al.add("PROVIDE AN EMAIL");
         } else if (email.indexOf('@') == 0 || email.indexOf('@') == -1 || email.indexOf('@') == (email.length() - 1)) {
            al.add("PROVIDE A VALID EMAIL");
         }


         if (al.size() != 0) {
            
            out.println("<h2>Please go back and login again...</h2>");
            out.println(al);


         } else {
            if (domain.equals("Admin")) {

                  String sqlStr;

                  // Update the qty of the table books

                  sqlStr = "SELECT * FROM admin_acc WHERE email = '" + email + "'";

                  out.println("<p class='debug'>" + sqlStr + "</p>");  // for debugging

                  ResultSet rset = stmt.executeQuery(sqlStr);

                  if(!rset.next()) {
                        out.println("<h2>No user found</h2>");
                        out.println("<h3>Please go back and login again...</h3>");                     
                     } else {
                        
                        out.println(rset.getString("email"));
                        if (password.equals(rset.getString("password"))) {
                           out.println("<h3>Welcome, Admin " + rset.getString("name") + "</h3>");

                           out.println("<form action = 'shirtadminorder'>");
                           out.println("<input type='hidden' name='name' value='" + rset.getString("name") + "'>");
                           out.println("<button type='submit' >Check Orders</button></form>");

                           out.println("<form action = 'shirtadminacc'>");
                           out.println("<input type='hidden' name='name' value='" + rset.getString("name") + "'>");
                           out.println("<button type='submit' >View Accounts</button></form>");                           

                           out.println("<form action = 'shirtadminsignup'>");
                           out.println("<input type='hidden' name='name' value='" + rset.getString("name") + "'>");
                           out.println("<button type='submit' >Assign Admin Accounts</button></form>");

                           out.println("</form>");
                           out.println("<form action = 'shirtwelcome.html'>");
                           out.println("<button type='submit' >Logout</button></form>");
                           out.println("</form>");
                        } else {
                              out.println("<h2>Incorrect password</h2>");
                              out.println("<h3>Please go back and login again...</h3>"); 
                        }            
                     

                  }

            } else { // member

                  String sqlStr2;

                  // Update the qty of the table books

                  sqlStr2 = "SELECT * FROM member_acc WHERE email = '" + email + "'";

                  out.println("<p class='debug'>" + sqlStr2 + "</p>");  // for debugging

                  ResultSet rset = stmt.executeQuery(sqlStr2);

                  if(!rset.next()) {
                        out.println("<h2>No user found</h2>");
                        out.println("<h3>Please go back and login again...</h3>");                     
                     } else {
                        out.println(rset.getString("email"));
                        if (password.equals(rset.getString("password"))) {
                           out.println("<h3>Welcome, " + rset.getString("name") + "</h3>");

                           out.println("<form action = 'shirtdisplay'>");

                           out.println("<input type='hidden' name='name' value='" + rset.getString("name") + "'>");

                           out.println("<button type='submit' >Order Aparrels</button></form>");

                           out.println("<form action = 'shirtcart'>");

                           out.println("<input type='hidden' name='name' value='" + rset.getString("name") + "'>");

                           out.println("<button type='submit' >Review Cart</button></form>");

                   
                           out.println("</form>");
                           out.println("<form action = 'shirtwelcome.html'>");
                           out.println("<button type='submit' >Logout</button></form>");
                           out.println("</form>");

                        } else {
                           out.println("<h2>Incorrect password</h2>");
                           out.println("<h3>Please go back and login again...</h3>"); 
                        }            

                  }

            }
         }

      } catch(Exception ex) {

         out.println("<p>Error: " + ex.getMessage() + "</p>");

         out.println("<p>Check Tomcat console for details.</p>");

         ex.printStackTrace();

      }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)

 

      out.println("</body></html>");

      out.close();

   }

}