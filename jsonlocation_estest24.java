package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * This test suite focuses on the helper methods of the {@link JsonLocation} class.
 */
public class JsonLocation_ESTestTest24 {

    /**
     * Tests that the {@link JsonLocation#_wrap(Object)} method returns the
     * exact same instance when the input object is already a {@link ContentReference}.
     * This ensures the method avoids unnecessary object creation.
     */
    @Test
    public void wrapWithContentReferenceShouldReturnSameInstance() {
        // Arrange: Create a ContentReference instance to be used as input.
        // The specific details of this instance are not critical, only that it is
        // a ContentReference with a known property (in this case, textual content).
        ContentReference contentToWrap = ContentReference.construct(
                /* isTextual */ true,
                /* rawContent */ ContentReference.redacted(),
                /* start */ 0,
                /* end */ 100);

        // Act: Call the internal _wrap method with the ContentReference instance.
        ContentReference result = JsonLocation._wrap(contentToWrap);

        // Assert: The returned object should be the same instance as the input.
        // This is a more direct and stronger assertion than just checking a property.
        assertSame("The _wrap method should return the same ContentReference instance.", contentToWrap, result);

        // As a secondary check, we can also verify that its properties are preserved.
        assertTrue("The 'hasTextualContent' property should be preserved.", result.hasTextualContent());
    }
}