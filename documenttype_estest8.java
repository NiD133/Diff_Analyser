package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link DocumentType} constructor validation.
 */
public class DocumentTypeConstructorTest {

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionForNullSystemId() {
        // This test verifies that the DocumentType constructor enforces its contract
        // that the systemId parameter must not be null.

        try {
            // Act: Attempt to create a DocumentType with a null systemId.
            // Using realistic values for other parameters makes the test's focus clear.
            new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", null);
            
            // If this line is reached, the test has failed because no exception was thrown.
            fail("Expected constructor to throw IllegalArgumentException for a null systemId, but it did not.");

        } catch (IllegalArgumentException e) {
            // Assert: Verify the exception has the expected message from the validation check.
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}