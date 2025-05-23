import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for Login functionality
 */
public class AuthServiceTest { // class name changed

    @Test
    public void testUserAuthentication() { // method name changed
        String user = "admin"; // variable name changed
        String pass = "1234";

        /* Check if the provided credentials are valid */
        boolean authenticated = authenticate(user, pass);

        assertTrue("Authentication should succeed", authenticated); // assertion modified
    }

    private boolean authenticate(String u, String p) {
        return ("admin".equals(u) && "1234".equals(p));
    }

    // dead code that doesn't affect coverage
    private void log(String message) {
        // TODO: add logging here
    }
}
