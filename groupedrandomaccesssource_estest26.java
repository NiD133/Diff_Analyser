package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

/**
 * Test suite for the {@link GroupedRandomAccessSource} class.
 */
public class GroupedRandomAccessSourceTest {

    /**
     * Verifies that calling the sourceReleased method does not alter the
     * total length of the GroupedRandomAccessSource. The length is calculated
     * at construction time and should remain immutable.
     */
    @Test
    public void sourceReleased_shouldNotAffectTotalLength() throws IOException {
        // --- Arrange ---
        // Create a group of sources, both based on an empty byte array.
        // The total length of the grouped source will therefore be zero.
        RandomAccessSource source1 = new ArrayRandomAccessSource(new byte[0]);
        RandomAccessSource source2 = new GetBufferedRandomAccessSource(new ArrayRandomAccessSource(new byte[0]));

        RandomAccessSource[] sources = {source1, source2};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Sanity check: ensure the initial length is correct.
        assertEquals("Initial length of the grouped source should be zero.", 0L, groupedSource.length());

        // --- Act ---
        // The sourceReleased() method is a protected hook for resource management in subclasses.
        // In the base class, it is expected to have no side effects on the object's state.
        groupedSource.sourceReleased(source1);

        // --- Assert ---
        // Verify that the length remains unchanged after the call.
        assertEquals("Length should remain zero after releasing a source.", 0L, groupedSource.length());
    }
}