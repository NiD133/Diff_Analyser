package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

// The original test class was auto-generated. A more conventional name would be StringUtilTest.
public class StringUtil_ESTestTest55 {

    /**
     * Verifies that {@link StringUtil#releaseBuilderVoid(StringBuilder)} correctly handles
     * StringBuilders that exceed the internal maximum size for pooling. Such builders should be
     * discarded without being modified (i.e., not cleared).
     */
    @Test
    public void releaseBuilderVoidDoesNotModifyBuilderExceedingMaxSize() {
        // Arrange: StringUtil's internal MaxBuilderSize is 8192.
        // We create a builder larger than this to test the discard code path.
        final int maxBuilderSize = 8192;
        final int oversizedLength = maxBuilderSize + 1;
        
        StringBuilder oversizedBuilder = new StringBuilder(oversizedLength);
        for (int i = 0; i < oversizedLength; i++) {
            oversizedBuilder.append('a');
        }
        
        // Sanity check the pre-condition
        assertEquals(oversizedLength, oversizedBuilder.length());

        // Act: Attempt to release the oversized builder back to the pool.
        StringUtil.releaseBuilderVoid(oversizedBuilder);

        // Assert: The builder was not cleared because its length exceeds the maximum
        // size allowed for pooling. Therefore, its length should remain unchanged.
        assertEquals("Builder should not be cleared as it's too large to be pooled",
            oversizedLength, oversizedBuilder.length());
    }
}