package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * This test class focuses on verifying the behavior of the ParallelScatterZipCreator class.
 * The original test class name and inheritance are preserved to demonstrate a focused
 * improvement on a single test case, a common task in maintaining a large test suite.
 */
public class ParallelScatterZipCreator_ESTestTest8 extends ParallelScatterZipCreator_ESTest_scaffolding {

    /**
     * Verifies that calling addArchiveEntry() with a null ZipArchiveEntry
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void addArchiveEntryShouldThrowNPEForNullEntry() {
        // Arrange: Set up the test objects.
        final ParallelScatterZipCreator scatterZipCreator = new ParallelScatterZipCreator();
        final InputStreamSupplier mockInputStreamSupplier = mock(InputStreamSupplier.class);

        // Act: Call the method under test with invalid input (a null entry).
        // The cast to ZipArchiveEntry is used to resolve method overloading ambiguity.
        scatterZipCreator.addArchiveEntry((ZipArchiveEntry) null, mockInputStreamSupplier);

        // Assert: The test will pass only if a NullPointerException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}