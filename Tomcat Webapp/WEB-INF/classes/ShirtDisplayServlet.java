// To save as "ebookshop\WEB-INF\classes\QueryServlet.java".

import java.io.*;

import java.sql.*;

import javax.servlet.*;

import javax.servlet.http.*;

import javax.servlet.annotation.*;



@WebServlet("/shirtdisplay")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)

public class ShirtDisplayServlet extends HttpServlet {



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


      out.println("<h2>Yet Another e-Shirtshop</h2>");

      String name = request.getParameter("name");
      out.println(name + ", please choose a shirt colour:<br /><br />");

      out.println("<form method='get' action='shirtquery'>");
      

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


         String sqlStr = "select distinct colour from designs";

         ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server



         // Step 4: Process the query result set
         // Print the <form> start tag

         //out.println("<form method='get' action='eshopquery'>");
        
         while(rset.next()) {
            out.println("<input type='checkbox' name='colour' value='" + rset.getString("colour") + "'/>" + rset.getString("colour"));  
         }
         out.println("<input type='hidden' name='name' value='" + name +  "''>");
         out.println("<br> <br>");
         out.println("Size: <select name='size' size='1'><option value='XS'>XS</option><option value='S'>S</option><option value='M'>M</option><option value='L'>L</option><option value='XL'>XL</option></select>");  

         out.println("<input type='submit' value='Continue'>");
         // Print the submit button and </form> end-tag
         //out.println("<p><input type='submit' value='Search' />");
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