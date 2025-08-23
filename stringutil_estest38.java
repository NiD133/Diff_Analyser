package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the StringUtil builder pool functionality.
 */
public class StringUtilBuilderTest {

    @Test
    public void releaseBuilderVoidClearsTheStringBuilder() {
        // Arrange: Borrow a StringBuilder from the pool and add some content to it.
        // This ensures we are testing the clearing mechanism, not just a new, empty builder.
        StringBuilder builder = StringUtil.borrowBuilder();
        builder.append("some initial content");
        assertTrue("Builder should have content before being released", builder.length() > 0);

        // Act: Release the builder back to the pool using the void method.
        StringUtil.releaseBuilderVoid(builder);

        // Assert: The builder should be empty after being released, ready for reuse.
        assertEquals("The builder's content should be cleared upon release", 0, builder.length());
    }
}