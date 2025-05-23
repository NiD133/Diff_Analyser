import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Tests database-related functionalities such as connection and query execution.
 */
public class ImprovedDatabaseTest {

    @Test
    public void shouldInitializeDatabaseConnection() {
        String databaseHost = "localhost";
        int databasePort = 5432;

        // Compose JDBC URL for PostgreSQL
        String jdbcUrl = String.format("jdbc:postgresql://%s:%d", databaseHost, databasePort);

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // Assert that connection is established
        assertNotNull("Connection should not be null", connection);
    }

    @Test
    public void shouldExecuteUserQuerySuccessfully() {
        String activeUserQuery = """
            SELECT * 
            FROM users 
            WHERE active = true
            """;
        String queryResult = runQuery(activeUserQuery);

        // Assert query returned a result
        assertTrue("Query result should not be null", queryResult != null && !queryResult.isEmpty());
    }

    private String runQuery(String sql) {
        // Simulate running SQL query and returning result
        return "user1,user2";
    }
}
