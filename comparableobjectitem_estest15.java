package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void equals_whenComparedToItself_shouldReturnTrue() {
        // Arrange: Create an instance of the class under test.
        // Using simple, concrete values makes the setup clear and avoids
        // the complexity of mocks.
        ComparableObjectItem item = new ComparableObjectItem("X-Value", new Object());

        // Act: Compare the object to itself.
        boolean result = item.equals(item);

        // Assert: The result should be true, fulfilling the reflexive
        // property of the equals() contract.
        assertTrue("An object must be equal to itself.", result);
    }
}