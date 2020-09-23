// To save as "ebookshop\WEB-INF\classes\QueryServlet.java".

import java.io.*;

import java.sql.*;

import java.util.*;

import javax.servlet.*;

import javax.servlet.http.*;

import javax.servlet.annotation.*;



@WebServlet("/shirtcongrats")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)

public class ShirtCongratsServlet extends HttpServlet {



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
         String name = request.getParameter("name");
         String email = request.getParameter("email");
         String password = request.getParameter("password");
         String phone = request.getParameter("phone");

         int phoneInt;
         ArrayList al = new ArrayList();            

         ResultSet rsetemail = stmt.executeQuery("select email from member_acc");

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

                  sqlStr2 = "INSERT into member_acc (name, email, password, phone) values (\"" + name + "\",\"" + email + "\",\"" + password + "\","  + phone + ");";

                  out.println("<p class='debug'>" + sqlStr2 + "</p>");  // for debugging

                  stmt.executeUpdate(sqlStr2);

                  
                           out.println("<h3>Congratulations, " + name + "! You are one of us now!</h3>");   

                           out.println("<form action = 'shirtwelcome.html'>");
                           out.println("<button type='submit' >Click here to login!</button></form>");
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


