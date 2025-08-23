package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link org.apache.commons.lang3.SerializationUtils}.
 */
public class SerializationUtilsTest {

    /**
     * Tests that {@link SerializationUtils#roundtrip(java.io.Serializable)}
     * returns null when given a null input.
     */
    @Test
    public void roundtripShouldReturnNullForNullInput() {
        // The cast to a specific Serializable type (e.g., Integer) is necessary
        // to help the compiler resolve the generic method signature:
        // <T extends Serializable> T roundtrip(T obj)
        final Integer result = SerializationUtils.roundtrip((Integer) null);

        assertNull("Roundtripping a null object should result in null.", result);
    }
}