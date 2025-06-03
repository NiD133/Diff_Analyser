import org.junit.jupiter.api.Test;  // Modern JUnit import
import static org.junit.jupiter.api.Assertions.*; // Modern JUnit Assertions
import org.apache.commons.io.ByteOrderMark;

public class ByteOrderMarkTest {

    @Test
    public void testUTF8MatchesItsOwnBytes() {
        // Arrange: Create a UTF-8 ByteOrderMark and get its raw bytes
        ByteOrderMark utf8Bom = ByteOrderMark.UTF_8;
        int[] utf8Bytes = utf8Bom.getRawBytes();

        // Act: Create a new ByteOrderMark using the same label and byte sequence, then check if it matches those bytes
        ByteOrderMark customBom = new ByteOrderMark("Custom UTF-8 BOM", utf8Bytes); // More descriptive label
        boolean matches = customBom.matches(utf8Bytes);

        // Assert:  Verify that the ByteOrderMark matches the byte sequence it was created with.
        assertTrue(matches, "The ByteOrderMark should match its own raw bytes.");
    }
}