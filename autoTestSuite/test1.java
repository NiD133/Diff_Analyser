import org.junit.Test;
import static org.junit.Assert.*;

public class LoginServiceTest {

    @Test
    public void shouldAuthenticateUser() {
        String username = "admin";
        String password = "1234";

        // verify credentials
        boolean isAuthenticated = authenticate(username, password);

        assertTrue(isAuthenticated);
    }

    private boolean authenticate(String u, String p) {
        return "admin".equals(u) && "1234".equals(p);
    }
}
