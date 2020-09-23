// To save as "edesignshop\WEB-INF\classes\QueryServlet.java".

import java.io.*;

import java.sql.*;

import javax.servlet.*;

import javax.servlet.http.*;

import javax.servlet.annotation.*;



@WebServlet("/shirtquery")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)

public class ShirtQueryServlet extends HttpServlet {



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

         String name = request.getParameter("name");
         String size = request.getParameter("size");

         String[] colour = request.getParameterValues("colour");  // Returns an array of Strings
         if (colour == null) {

            out.println("<h2>No colour selected. Please go back to select colour(s)</h2><body></html>");

            return; // Exit doGet()

         } 
         String sqlStr2 = "SELECT * FROM member_acc WHERE name = '" + name + "'";
         String sqlStr = "SELECT * FROM designs WHERE colour IN (";
         for (int i = 0; i < colour.length; ++i) {
            if (i < colour.length - 1) {
               sqlStr += "'" + colour[i] + "', ";  // need a commas
            } else {
               sqlStr += "'" + colour[i] + "'";    // no commas
            }
         }
         sqlStr += ")";
         out.println("<h3>Thank you for your query, " + name + "</h3>");

         out.println("<p>Your SQL statement is: " + sqlStr + "</p>"); // Echo for debugging

         ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server



         // Step 4: Process the query result set
         // Print the <form> start tag

         out.println("<form method='get' action='shirtorder'>");
         out.println("<table>");
         out.println("  <tr>");
         out.println("     <th></th>");
         out.println("     <th>COLOUR</th>");
         out.println("     <th>TYPE</th>");
         out.println("     <th>SIZE</th>");
         out.println("     <th>PRICE</th>");
         out.println("  </tr>");        
         while(rset.next()) {

            out.println("     <tr><td><p><input type='checkbox' name='id' value=" + "'" + rset.getString("id") + "' /></td>");

            out.println("     <td>" + rset.getString("colour") + "</td>");

            out.println("     <td>" + rset.getString("design_name") + "</td>");

            out.println("     <td>"+ size + "</td>");

            out.println("     <td>$"+ rset.getString("price") + "</td>");

            out.println("  </tr>");
         }
         out.println("</table>"); 

         rset.close();

         ResultSet rset2 = stmt.executeQuery(sqlStr2);
         String email = "";
         int phone = 0;

         if (rset2.next()) {

            email = rset2.getString("email");
            phone = rset2.getInt("phone");

         }

         String phone_string = String.valueOf(phone);
        
         out.println("<input type='hidden' name='size' value='" + size +  "''>");



         out.println("<p>Enter your Name: <input type='text' name='cust_name' value='" + name + "'/></p>");
         out.println("<p>Enter your Email: <input type='text' name='cust_email' value='" + email + "' /></p>");
         out.println("<p>Enter your Phone Number: <input type='text' name='cust_phone' value='" + phone_string +"' /></p>");

         // Print the submit button and </form> end-tag

         out.println("<p><input type='submit' value='Add to cart' />");

         out.println("</form>");

         out.println("<form action = 'shirtwelcome.html'>");
         out.println("<button type='submit' >Logout</button></form>");
         out.println("</form>");

      } catch(Exception ex) {

         out.println("<p>Error: " + ex.getMessage() + "</p>");

         out.println("<p>Check Tomcat console for details.</p>");

         ex.printStackTrace();

      }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)

 

      out.println("</body></html>");

      out.close();

   }

}