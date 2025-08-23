package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Elements} class.
 * This test focuses on handling invalid CSS queries.
 */
// Note: The original test class extended a scaffolding class, likely for test setup.
// This is preserved, but in a real-world scenario, using JUnit's @Before or @BeforeClass
// is often a clearer approach.
public class Elements_ESTestTest64 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that calling selectFirst() with a syntactically invalid CSS query
     * throws an IllegalStateException with an informative message.
     */
    @Test
    public void selectFirstWithInvalidQueryThrowsIllegalStateException() {
        // Arrange: Create an empty Elements collection and define an invalid CSS query.
        Elements elements = new Elements();
        String invalidQuery = ",P<";

        try {
            // Act: Attempt to select the first element using the invalid query.
            elements.selectFirst(invalidQuery);
            fail("Expected an IllegalStateException to be thrown for the invalid query, but it was not.");
        } catch (IllegalStateException e) {
            // Assert: Verify that the exception message is correct and informative.
            String expectedMessage = "Could not parse query '" + invalidQuery + "': unexpected token at '" + invalidQuery + "'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}