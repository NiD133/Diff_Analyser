package org.jsoup.nodes;

import org.junit.Test;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;

/**
 * This test class contains tests for the {@link Attribute} class.
 * Note: The original class name and inheritance from the auto-generated test
 * suite have been preserved as requested. In a typical scenario, this class
 * would be named {@code AttributeTest}.
 */
public class Attribute_ESTestTest24 extends Attribute_ESTest_scaffolding {

    /**
     * Verifies that attempting to write an attribute's HTML to an Appendable
     * with no remaining capacity throws a BufferOverflowException.
     */
    @Test(timeout = 4000, expected = BufferOverflowException.class)
    public void htmlToAppendableThrowsExceptionWhenBufferIsFull() {
        // Arrange: Create a standard attribute.
        Attribute attribute = new Attribute("id", "test");
        
        // Arrange: Create an Appendable (a CharBuffer) with zero capacity, simulating a full buffer.
        CharBuffer zeroCapacityBuffer = CharBuffer.allocate(0);
        
        // Arrange: Use default output settings.
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        // Act: Attempt to write the attribute's HTML to the zero-capacity buffer.
        // The method is expected to throw a BufferOverflowException, which is
        // asserted by the 'expected' parameter of the @Test annotation.
        attribute.html((Appendable) zeroCapacityBuffer, outputSettings);
    }
}