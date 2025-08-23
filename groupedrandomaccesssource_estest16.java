package com.itextpdf.text.io;

import org.junit.Test;
import java.io.IOException;

/**
 * This test class contains tests for the {@link GroupedRandomAccessSource}.
 * Note: The original class name and inheritance were preserved from the auto-generated test.
 * In a typical project, this might be renamed to "GroupedRandomAccessSourceTest".
 */
public class GroupedRandomAccessSource_ESTestTest16 extends GroupedRandomAccessSource_ESTest_scaffolding {

    /**
     * Verifies that calling close() on a GroupedRandomAccessSource propagates the call
     * to its underlying sources and correctly throws a NullPointerException if one of
     * those sources fails during its own close operation.
     *
     * This is tested by using a WindowRandomAccessSource that was intentionally
     * created with a null delegate source. When the GroupedRandomAccessSource tries
     * to close this member source, the attempt to close the null delegate results
     * in the expected NullPointerException.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void closeShouldThrowNpeWhenUnderlyingSourceDelegateIsNull() throws IOException {
        // Arrange: Create a source that is designed to fail on close().
        // A WindowRandomAccessSource initialized with a null delegate will throw an NPE
        // when its close() method is called.
        RandomAccessSource sourceWithNullDelegate = new WindowRandomAccessSource(null, 328L, 328L);

        // Arrange: Create the GroupedRandomAccessSource containing the faulty source.
        RandomAccessSource[] sources = { sourceWithNullDelegate };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act: Attempt to close the grouped source. This action should trigger the NPE
        // from the underlying sourceWithNullDelegate.
        groupedSource.close();

        // Assert: The test passes if a NullPointerException is thrown, as declared
        // by the @Test(expected=...) annotation.
    }
}