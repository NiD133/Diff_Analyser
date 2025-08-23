package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link ParallelScatterZipCreator} class.
 */
public class ParallelScatterZipCreatorTest {

    /**
     * Tests that createCallable() throws a NullPointerException when the
     * ZipArchiveEntry parameter is null, as this is a required argument.
     */
    @Test(expected = NullPointerException.class)
    public void createCallableShouldThrowNullPointerExceptionForNullEntry() {
        // Arrange
        final ParallelScatterZipCreator scatterZipCreator = new ParallelScatterZipCreator();
        final InputStreamSupplier mockInputStreamSupplier = mock(InputStreamSupplier.class);

        // Act: Call the method under test with a null entry.
        // An exception is expected.
        scatterZipCreator.createCallable(null, mockInputStreamSupplier);

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}