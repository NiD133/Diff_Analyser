package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * This class contains the refactored test case.
 * The original test class name "Elements_ESTestTest140" and its scaffolding are kept
 * as changing them is beyond the scope of improving a single test case.
 */
public class Elements_ESTestTest140 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that the {@link Elements#addClass(String)} method returns the same
     * instance it was called on, enabling method chaining.
     *
     * This test specifically covers the edge case where the method is called on an
     * empty Elements collection.
     */
    @Test
    public void addClassShouldReturnSameInstanceForChaining() {
        // Arrange: Create an empty Elements collection.
        Elements elements = new Elements();
        String className = "test-class";

        // Act: Call the addClass method.
        Elements result = elements.addClass(className);

        // Assert: The method should return the same instance.
        assertSame("addClass should return the same Elements instance to support method chaining.", elements, result);
    }
}