package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that the public constructor can be called without error.
     * The constructor is deprecated and scheduled for removal, but this test ensures
     * it remains instantiable for backward compatibility until then.
     */
    @Test
    public void testConstructorInstantiation() {
        // Create an instance of ArraySorter.
        final ArraySorter sorter = new ArraySorter();

        // Assert that the instance is not null, confirming successful instantiation.
        assertNotNull(sorter);
    }
}