// To save as "ebookshop\WEB-INF\classes\QueryServlet.java".

import java.io.*;

import java.sql.*;

import javax.servlet.*;

import javax.servlet.http.*;

import javax.servlet.annotation.*;



@WebServlet("/shirtadminacc")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)

public class ShirtAdminAccServlet extends HttpServlet {



   // The doGet() runs once per HTTP GET request to this servlet.

   @Override

   public void doGet(HttpServletRequest request, HttpServletResponse response)

               throws ServletException, IOException {

      // Set the MIME type for the response message

      response.setContentType("text/html");

      // Get a output writer to write the response message into the network socket

      PrintWriter out = response.getWriter();

      // Print an HTML page as the output of the query

      out.println("<html>");

      out.println("<head><title>Query Response</title></head>");

      out.println("<body>");

      String name = request.getParameter("name");
      

      try (

         // Step 1: Allocate a database 'Connection' object

         Connection conn = DriverManager.getConnection(

               "jdbc:mysql://localhost:3306/shirtsale?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",

               "jiawei", "xxxx");   // For MySQL

               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"



         // Step 2: Allocate a 'Statement' object in the Connection

         Statement stmt = conn.createStatement();

      ) {

         // Step 3: Execute a SQL SELECT query

         String sqlStr = "select id, name from admin_acc where name ='" + request.getParameter("name") + "'";

         ResultSet rset = stmt.executeQuery(sqlStr);
         rset.next();
         int cust_id = Integer.parseInt(rset.getString("id"));
         String cust_name = rset.getString("name");
         rset.close();

         String sqlStr2 = "select * from member_acc";

         ResultSet rset2 = stmt.executeQuery(sqlStr2);  // Send the query to the server

         int count = 0;
         int total_price = 0;
        
         while(rset2.next()) {

               if(count == 0) {

                     out.println("<h2> List of customers </h2>");

                           out.println("<table border = '1'>");
                           out.println("  <tr>");
                           out.println("     <th>ID</th>");
                           out.println("     <th>NAME</th>");
                           out.println("     <th>EMAIL</th>");
                           out.println("     <th>PASSWORD</th>");                           
                           out.println("     <th>PHONE</th>");
                           out.println("  </tr>");
                     count++;


               }

            
               out.println("     <tr><td>" + rset2.getString("id") + "</td>");

               out.println("     <td>" + rset2.getString("name") + "</td>");

               out.println("     <td>"+ rset2.getString("email") + "</td>");

               out.println("     <td>"+ rset2.getString("password") + "</td>");

               out.println("     <td>"+ rset2.getString("phone") + "</td>");

               out.println("  </tr>");  

               count++;
         }

         if (count == 0){
            out.println("<h3> There are no customers. </h3>");
         } else {
            out.println("</table>");
            out.println("<h3> You have " + (count-1) + " customers." );
         }

         rset2.close();


         String sqlStr3 = "select * from admin_acc";

         ResultSet rset3 = stmt.executeQuery(sqlStr3);  // Send the query to the server

         int count3 = 0;
        
         while(rset3.next()) {

               if(count3 == 0) {

                     out.println("<h2> List of admins </h2>");

                           out.println("<table border = '1'>");
                           out.println("  <tr>");
                           out.println("     <th>ID</th>");
                           out.println("     <th>NAME</th>");
                           out.println("     <th>EMAIL</th>");
                           out.println("     <th>PASSWORD</th>");                           
                           out.println("     <th>PHONE</th>");
                           out.println("  </tr>");
                     count3++;


               }

            
               out.println("     <tr><td>" + rset3.getString("id") + "</td>");

               out.println("     <td>" + rset3.getString("name") + "</td>");

               out.println("     <td>"+ rset3.getString("email") + "</td>");

               out.println("     <td>"+ rset3.getString("password") + "</td>");

               out.println("     <td>"+ rset3.getString("phone") + "</td>");

               out.println("  </tr>");  

               count++;
         }

         out.println("</table>");
         out.println("<h3> You have " + (count-1) + " admins." );

         rset3.close();




         String login = "SELECT * FROM admin_acc where id=" + cust_id;
         ResultSet rsetlogin = stmt.executeQuery(login);
         rsetlogin.next();

         String password = rsetlogin.getString("password");
         String stored_email = rsetlogin.getString("email");      
         
         out.println("<form action = 'shirtwelcome'>");
         out.println("<input type='hidden' name='domain' value='Admin'>");
         out.println("<input type='hidden' name='email' value='" + stored_email + "'>");
         out.println("<input type='hidden' name='password' value='" + password + "'>");

         out.println("<button type='submit' >Back To Dashboard</button>");

         out.println("</form>");

         out.println("<form action = 'shirtwelcome.html'>");
         out.println("<button type='submit' >Logout</button></form>");
         out.println("</form>");
         // Print the submit button and </form> end-tag
         //out.println("<p><input type='submit' value='Search' />");
         

      } catch(Exception ex) {

         out.println("<p>Error: " + ex.getMessage() + "</p>");

         out.println("<p>Check Tomcat console for details.</p>");

         ex.printStackTrace();

      }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)

 

      out.println("</body></html>");

      out.close();

   }

}