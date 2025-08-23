package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link RefinedSoundex} class.
 */
public class RefinedSoundexTest {

    /**
     * Tests that the constructor successfully creates an instance when provided with an
     * empty char array for the custom mapping. This ensures that edge cases like an
     * empty mapping are handled gracefully without exceptions.
     */
    @Test
    public void shouldCreateInstanceWithEmptyMapping() {
        // Arrange: Create an empty character array for the mapping.
        final char[] emptyMapping = new char[0];

        // Act: Instantiate RefinedSoundex with the empty mapping.
        // The test implicitly verifies that this does not throw an exception.
        final RefinedSoundex refinedSoundex = new RefinedSoundex(emptyMapping);

        // Assert: Confirm that the object was created successfully.
        assertNotNull("The RefinedSoundex instance should not be null.", refinedSoundex);
    }
}