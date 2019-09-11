import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "searchServlet",
            urlPatterns = "/search")
public class searchServlet extends HttpServlet {

    private final String DRIVER_NAME = "jdbc:derby:";
    private final String DATABASE_PATH = "/WEB-INF/lib/wikidb";
    private final String USER = "zach";
    private final String PW = "zach";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection conn =null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try{
            String searchTerm = request.getParameter("searchTerm");

            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            String path = getServletContext().getRealPath(DATABASE_PATH);
            conn = DriverManager.getConnection(DRIVER_NAME + path, USER, PW);
            String sql = "SELECT itemID FROM Item WHERE Name = ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, searchTerm);

            rset = pstmt.executeQuery();

            StringBuilder html = new StringBuilder("<html><body>");

            while(rset.next()){
                int id = rset.getInt(1);
                html.append("<p>").append(id).append("</p>");

            }
            html.append("</body></html>");

            response.getWriter().print(html.toString());

        }catch(SQLException | ClassNotFoundException e){
            response.getWriter().print(e);
        }finally{
            if(rset != null){
                try {
                    rset.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
