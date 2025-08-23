package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link HexDump} class.
 */
public class HexDumpTest {

    /**
     * Tests that the public constructor of the HexDump utility class can be
     * instantiated without throwing an exception. This is often done for
     * code coverage, as the class is intended to be used via its static methods.
     */
    @Test
    public void testConstructor() {
        // The HexDump class is a utility class with static methods.
        // The source Javadoc notes: "Instances should NOT be constructed in standard programming."
        // This test simply confirms that its public constructor is callable and returns a non-null instance.
        final HexDump hexDump = new HexDump();
        assertNotNull("A new HexDump instance should not be null.", hexDump);
    }
}