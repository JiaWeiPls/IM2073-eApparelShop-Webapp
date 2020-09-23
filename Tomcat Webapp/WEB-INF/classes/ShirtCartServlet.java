// To save as "ebookshop\WEB-INF\classes\QueryServlet.java".

import java.io.*;

import java.sql.*;

import javax.servlet.*;

import javax.servlet.http.*;

import javax.servlet.annotation.*;



@WebServlet("/shirtcart")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)

public class ShirtCartServlet extends HttpServlet {



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

         String sqlStr = "select id, name from member_acc where name ='" + request.getParameter("name") + "'";

         ResultSet rset = stmt.executeQuery(sqlStr);
         rset.next();
         int cust_id = Integer.parseInt(rset.getString("id"));
         String cust_name = rset.getString("name");
         rset.close();

         String sqlStr2 = "select designs.price, colour, design_name, size from cust_orders left join designs on cust_orders.design_id = designs.id where cust_id=" + cust_id;

         ResultSet rset2 = stmt.executeQuery(sqlStr2);  // Send the query to the server





         // Step 4: Process the query result set
         // Print the <form> start tag

         //out.println("<form method='get' action='eshopquery'>");

         int count = 0;
         int total_price = 0;
        
         while(rset2.next()) {

               if(count == 0) {

                     out.println("<h2> Here are what's in your cart </h2>");

                           out.println("<table border = '1'>");
                           out.println("  <tr>");
                           out.println("     <th>COLOUR</th>");
                           out.println("     <th>TYPE</th>");
                           out.println("     <th>SIZE</th>");
                           out.println("     <th>PRICE</th>");
                           out.println("  </tr>");
                     count++;
               }

            
               out.println("     <tr><td>" + rset2.getString("colour") + "</td>");

               out.println("     <td>" + rset2.getString("design_name") + "</td>");

               out.println("     <td>"+ rset2.getString("size") + "</td>");

               out.println("     <td>$"+ rset2.getString("price") + "</td>");

               out.println("  </tr>");  

               count++;
               total_price += Integer.parseInt(rset2.getString("price"));

         }

         if(count != 0) {

            out.println("     <tr><td style='width: 131.333px; text-align: center; colspan='2''>&nbsp;<form action = 'shirtmemcfm'>");
            out.println("     <input type='hidden' name='name' value='" + cust_name + "'>");
            out.println("     <button type='submit'>Confirm Orders</button></form></td>");
            out.println("<td></td><td>Total Price: </td><td>$" + total_price +".00</td>") ;
            out.println("</tr>");

            out.println("</table>"); 
         }

         if (count == 0){
            out.println("<h3> Your cart is empty. </h3>");
            out.println("<form action = 'shirtdisplay'>");
            out.println("<input type='hidden' name='name' value='" + cust_name + "'>");
            out.println("<button type='submit' >Order Aparrels Here</button></form>");
         } else {
            out.println("<form action = 'shirtdisplay'>");
            out.println("<input type='hidden' name='name' value='" + cust_name + "'>");
            out.println("<button type='submit' >Order More</button></form>");
         }

         rset2.close();

         String login = "SELECT * FROM member_acc where id=" + cust_id;
         ResultSet rsetlogin = stmt.executeQuery(login);
         rsetlogin.next();

         String password = rsetlogin.getString("password");
         String stored_email = rsetlogin.getString("email");      
         
         out.println("<form action = 'shirtwelcome'>");
         out.println("<input type='hidden' name='domain' value='member'>");
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