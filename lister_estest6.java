package org.apache.commons.compress.archivers;

import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Tests for the {@link Lister} class.
 */
public class ListerTest {

    /**
     * Verifies that the Lister constructor throws an exception when initialized
     * with an empty arguments array, as it requires at least one argument for
     * the archive file path.
     */
    @Test
    public void constructorShouldThrowExceptionForEmptyArguments() {
        // Arrange: Create an empty arguments array.
        final String[] emptyArgs = {};

        // Act & Assert: Expect an ArrayIndexOutOfBoundsException when constructing
        // a Lister instance with no arguments. The constructor attempts to access
        // the first element (args[0]) for the file path.
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            new Lister(true, emptyArgs);
        });
    }
}