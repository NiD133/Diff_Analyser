package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Test suite for the {@link Elements#clone()} method.
 */
public class ElementsCloneTest {

    @Test
    public void cloneShouldCreateSeparateButEqualInstance() {
        // Arrange: Create an empty Elements object.
        Elements originalElements = new Elements();

        // Act: Clone the original object.
        Elements clonedElements = originalElements.clone();

        // Assert: Verify the clone is a new instance but has the same content.
        // 1. The cloned object should not be the same instance as the original.
        assertNotSame("The cloned object should be a new instance.", originalElements, clonedElements);

        // 2. The cloned object should be logically equal to the original.
        assertEquals("The cloned object should be equal to the original.", originalElements, clonedElements);
    }
}