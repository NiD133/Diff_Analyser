package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class for {@link TaiInstant}.
 */
public class TaiInstantTest {

    @Test
    public void equals_returnsFalseForInstantsWithDifferentSeconds() {
        // Arrange: Create two TaiInstant objects with different second values but the same nano value.
        TaiInstant baseInstant = TaiInstant.ofTaiSeconds(0L, 0L);
        TaiInstant differentSecondsInstant = baseInstant.withTaiSeconds(5495L);

        // Assert: Verify that the two instants are not considered equal.
        assertNotEquals(baseInstant, differentSecondsInstant);
        
        // Also assert the state of the newly created instant is correct.
        assertEquals(5495L, differentSecondsInstant.getTaiSeconds());
        assertEquals(0, differentSecondsInstant.getNano());
    }
}