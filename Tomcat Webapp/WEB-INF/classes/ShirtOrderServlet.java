// To save as "ebookshop\WEB-INF\classes\QueryServlet.java".

import java.io.*;

import java.sql.*;

import java.util.*;

import javax.servlet.*;

import javax.servlet.http.*;

import javax.servlet.annotation.*;



@WebServlet("/shirtorder")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)

public class ShirtOrderServlet extends HttpServlet {



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

      out.println("<head><title>Query Response</title><link rel='stylesheet' type-='text/css' href='css/order.css'/></head>");

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

         // Step 3 & 4: Execute a SQL SELECT query and Process the query result

         // Retrieve the books' id. Can order more than one books.

         String[] ids = request.getParameterValues("id");

         String cust = request.getParameter("cust_name");
         String email = request.getParameter("cust_email");
         String phone = request.getParameter("cust_phone");
         String size = request.getParameter("size");

         int phoneInt;
         ArrayList al = new ArrayList();

         if ((phone == null) || (phone.equals(""))) {
            al.add("PROVIDE A PHONE NUMBER");
         } else if (phone.length() != 8) {
            al.add("PROVIDE A VALID PHONE NUMBER");
         } else {
            try {
               phoneInt = Integer.parseInt(phone);
            } catch (NumberFormatException nfe) {
               al.add("PHONE NUMBERS MUST BE AN INTEGER");
            }
         } if ((cust == null) || (cust.equals(""))) {
            al.add("PROVIDE YOUR NAME");
         } if ((email == null) || (email.equals(""))) {
            al.add("PROVIDE AN EMAIL");
         } else if (email.indexOf('@') == 0 || email.indexOf('@') == -1 || email.indexOf('@') == (email.length() - 1)) {
            al.add("PROVIDE A VALID EMAIL");
         }
         if (al.size() != 0) {

            out.println(al);
         } else {
            if (ids != null) {

               String sqlStr;

               int count;

               String[] design_id = request.getParameterValues("id");  // Returns an array of Strings
               String sqlStr2 = "SELECT * FROM designs WHERE id IN (";
               for (int i = 0; i < design_id.length; ++i) {
                  if (i < design_id.length - 1) {
                     sqlStr2 += "'" + design_id[i] + "', ";  // need a commas
                  } else {
                     sqlStr2 += "'" + design_id[i] + "'";    // no commas
                  }
               }
               sqlStr2 += ")";

               out.println("<p class='debug'>Your SQL statement is: " + sqlStr2 + "</p>"); // Echo for debugging


               out.println("<h3>You have ordered the following apparels: </h3>");

               ResultSet rset2 = stmt.executeQuery(sqlStr2);  // Send the query to the server


               // Process each of the books

               out.println("<table>");
               out.println("  <tr>");
               out.println("     <th>COLOUR</th>");
               out.println("     <th>TYPE</th>");
               out.println("     <th>SIZE</th>");
               out.println("     <th>PRICE</th>");
               out.println("  </tr>");


               for(int i = 0; i < design_id.length; ++i) {
                  rset2.next();

                  out.println("     <td>" + rset2.getString("colour") + "</td>");

                  out.println("     <td>" + rset2.getString("design_name") + "</td>");

                  out.println("     <td>"+ size + "</td>");

                  out.println("     <td>$"+ rset2.getString("price") + "</td>");

                  out.println("  </tr>");                 

               }
               out.println("</table>"); 

               rset2.close();


               ResultSet rset3 = stmt.executeQuery("SELECT id from member_acc where name = '" + cust + "'");
               rset3.next();
               int cust_id = rset3.getInt("id");


               rset3.close();

               int totalprice = 0;

               for (int i = 0; i < design_id.length; ++i) {
                  // Find design based on id
                  String findId = "SELECT * FROM designs WHERE id=" + design_id[i];
                  ResultSet rset = stmt.executeQuery(findId);
                  rset.next();

                  // Create a transaction record

                  int priceInt = Integer.parseInt(rset.getString("price"));

                  sqlStr = "INSERT INTO cust_orders (cust_id, name, email,phone, design_id, size, price) VALUES ("
                        + cust_id + ", '" + cust + "','" +  email + "'," +  Integer.parseInt(phone) + "," +  design_id[i] + ",'" + size + "'," + priceInt + ")";

                  rset.close();

                  out.println("<p class='debug'>" + sqlStr + "</p>");  // for debugging

                  count = stmt.executeUpdate(sqlStr);

                  out.println("<p class='debug'>" + count + " record inserted.</p>");


                  totalprice += priceInt;

               }


               String login = "SELECT * FROM member_acc where id=" + cust_id;
               ResultSet rsetlogin = stmt.executeQuery(login);
               rsetlogin.next();

               String password = rsetlogin.getString("password");
               String stored_email = rsetlogin.getString("email");
               
               out.println("<h3>Your order has been added to cart.</h3>");

               out.println("<h3>Thank you. Total price: $" + totalprice + ".00<h3>");


               out.println("<form action = 'shirtwelcome'>");
               out.println("<input type='hidden' name='domain' value='member'>");
               out.println("<input type='hidden' name='email' value='" + stored_email + "'>");
               out.println("<input type='hidden' name='password' value='" + password + "'>");



               out.println("<button type='submit' >Back To Dashboard</button>");
 

               out.println("</form>");

            } else { // No book selected

               out.println("<h3>Please go back and select an item...</h3>");

            }

         out.println("<form action = 'shirtwelcome.html'>");
         out.println("<button type='submit' >Logout</button></form>");
         out.println("</form>");
         
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