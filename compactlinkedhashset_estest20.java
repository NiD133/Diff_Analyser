package com.google.common.collect;

import org.junit.Test;
import java.util.Collections;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CompactLinkedHashSet_ESTestTest20 extends CompactLinkedHashSet_ESTest_scaffolding {

    /**
     * Verifies that the internal `insertEntry` method throws an `ArrayIndexOutOfBoundsException`
     * when called with an entry index that is outside the bounds of the set's internal arrays.
     *
     * <p><b>Test Logic:</b>
     * 1. A `CompactLinkedHashSet` is created with a single element. This results in a small
     *    internal capacity (the default initial capacity is 3).
     * 2. The package-private `insertEntry` method is then called with an index deliberately chosen
     *    to be much larger than this capacity.
     * 3. The test asserts that an `ArrayIndexOutOfBoundsException` is thrown, confirming
     *    the expected boundary check behavior of the internal data structure.
     */
    @Test
    public void insertEntry_withOutOfBoundsIndex_throwsException() {
        // Arrange: Create a set with a single element to ensure its internal arrays
        // have a small, predictable capacity.
        CompactLinkedHashSet<Locale.Category> set =
                CompactLinkedHashSet.create(Collections.singleton(Locale.Category.FORMAT));

        // An index that is guaranteed to be out of the bounds of the internal arrays.
        final int outOfBoundsIndex = 133;
        final Locale.Category elementToInsert = Locale.Category.FORMAT;
        // The hash and mask values are required by the method signature but are not
        // relevant to triggering this specific exception.
        final int arbitraryHash = 133;
        final int arbitraryMask = 1485;

        // Act & Assert
        try {
            set.insertEntry(outOfBoundsIndex, elementToInsert, arbitraryHash, arbitraryMask);
            fail("Expected an ArrayIndexOutOfBoundsException but none was thrown.");
        } catch (ArrayIndexOutOfBoundsException expected) {
            // The exception is expected. For ArrayIndexOutOfBoundsException, the message
            // typically contains the invalid index, which provides a more specific check.
            assertEquals(
                    "Exception message should contain the out-of-bounds index",
                    String.valueOf(outOfBoundsIndex),
                    expected.getMessage());
        }
    }
}