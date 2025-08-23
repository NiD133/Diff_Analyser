package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link SegmentUtils} class.
 */
public class SegmentUtilsTest {

    /**
     * Tests that countArgs() handles a malformed method descriptor that includes
     * an extra, non-standard character after a valid object type signature.
     * The current implementation incorrectly counts this extra character as a
     * separate argument.
     */
    @Test
    public void countArgsShouldCountExtraCharacterInMalformedDescriptor() {
        // Arrange
        // This descriptor is malformed. The argument string "(LkEf;|)" contains a
        // valid object type "LkEf;|" followed by an unexpected '|' character.
        // The method implementation counts "LkEf;|" as the first argument and
        // the stray '|' as a second, single-character argument.
        final String malformedDescriptor = "&L(LkEf;|)7g<";
        final int expectedArgumentCount = 2;

        // Act
        final int actualArgumentCount = SegmentUtils.countArgs(malformedDescriptor);

        // Assert
        assertEquals("The number of arguments in the malformed descriptor was not counted correctly.",
                     expectedArgumentCount, actualArgumentCount);
    }
}