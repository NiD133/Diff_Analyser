package org.apache.commons.compress.archivers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for the {@link Lister} class.
 */
public class ListerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that the Lister constructor throws a NullPointerException
     * when the first command-line argument (the archive path) is null.
     */
    @Test
    public void constructorShouldThrowNullPointerExceptionWhenFirstArgumentIsNull() {
        // Arrange: Prepare arguments where the first element is null.
        // The Lister class expects this to be a non-null file path.
        final String[] argsWithNullPath = { null };

        // Assert: We expect a NullPointerException with a specific message,
        // which confirms that the null check on the first argument is working.
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("args[0]");

        // Act: Attempt to create a Lister instance with the invalid arguments.
        // This line is expected to throw the exception.
        new Lister(false, argsWithNullPath);
    }
}