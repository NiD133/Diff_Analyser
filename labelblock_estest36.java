package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Tests for the cloning functionality of the {@link LabelBlock} class.
 */
public class LabelBlockCloningTest {

    /**
     * Verifies that the clone() method creates a new instance that is a separate
     * object in memory but is logically equal to the original.
     */
    @Test
    public void testCloneCreatesIndependentAndEqualInstance() throws CloneNotSupportedException {
        // Arrange: Create an original LabelBlock instance.
        LabelBlock original = new LabelBlock("Test Label");

        // Act: Clone the original instance.
        LabelBlock cloned = (LabelBlock) original.clone();

        // Assert: The clone must be a different object but equal in value.
        assertNotSame("The cloned object should be a new instance in memory.", original, cloned);
        assertEquals("The cloned object's state should be equal to the original's.", original, cloned);
    }
}