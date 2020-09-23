// To save as "ebookshop\WEB-INF\classes\QueryServlet.java".

import java.io.*;

import java.sql.*;

import java.util.*;

import javax.servlet.*;

import javax.servlet.http.*;

import javax.servlet.annotation.*;



@WebServlet("/shirtnewadmin")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)

public class ShirtNewAdminServlet extends HttpServlet {



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


         String sqlStr = "select id, name from admin_acc where name ='" + request.getParameter("admin_name") + "'";

         ResultSet rset = stmt.executeQuery(sqlStr);
         rset.next();
         int cust_id = Integer.parseInt(rset.getString("id"));
         String cust_name = rset.getString("name");
         rset.close();

         // Retrieve the books' id. Can order more than one books.
         String name = request.getParameter("name");
         String email = request.getParameter("email");
         String password = request.getParameter("password");
         String phone = request.getParameter("phone");

         int phoneInt = Integer.parseInt(phone);
         ArrayList al = new ArrayList();            

         ResultSet rsetemail = stmt.executeQuery("select email from admin_acc");

         while(rsetemail.next()) {
            if (email.equals(rsetemail.getString("email"))){
               al.add("EMAIL ALREADY IN USE");
            }
         }

         rsetemail.close();

         ResultSet rsetphone = stmt.executeQuery("select phone from member_acc");       

         while(rsetphone.next()) {
            if (email.equals(rsetphone.getString("phone"))){
               al.add("PHONE NUMBER ALREADY IN USE");
            }
         }

         rsetphone.close();



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

                  String sqlStr2;

                  // Update the qty of the table books

                  sqlStr2 = "INSERT into admin_acc (name, email, password, phone) values (\"" + name + "\",\"" + email + "\",\"" + password + "\","  + phoneInt + ");";

                  out.println("<p class='debug'>" + sqlStr2 + "</p>");  // for debugging

                  stmt.executeUpdate(sqlStr2);

                  
                           out.println( name + " has been assigned as an admin!</h3>");   

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
                           
                           out.println("<form action = 'shirtwelcome.html'>");
                           out.println("<button type='submit' >Logout</button></form>");
                           out.println("</form>");        

               }

            
         }

       catch(Exception ex) {

         out.println("<p>Error: " + ex.getMessage() + "</p>");

         out.println("<p>Check Tomcat console for details.</p>");

         ex.printStackTrace();

      }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)

 

      out.println("</body></html>");

      out.close();

   }
}


