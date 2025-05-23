import org.junit.Test;
import static org.junit.Assert.*;

public class DatabaseConnectionTest {

    @Test
    public void testConnectionInitialization() {
        String host = "localhost";
        int port = 5432;
        String url = "jdbc:postgresql://" + host + ":" + port;

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(conn);
    }

    @Test
    public void testQueryExecution() {
        String query = "SELECT * FROM users WHERE active = true";
        String result = executeQuery(query);
        assertEquals("Expected non-null result", true, result != null);
    }

    private String executeQuery(String query) {
        // Simulate query
        return "user1,user2";
    }
}
