package org.apache.commons.io;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ByteOrderMarkEqualsTest {

    private static final ByteOrderMark TEST_BOM_1 = new ByteOrderMark("TEST1", 1);
    private static final ByteOrderMark TEST_BOM_2 = new ByteOrderMark("TEST2", 2, 3);
    private static final ByteOrderMark TEST_BOM_3 = new ByteOrderMark("TEST3", 4, 5, 6);

    @Test
    public void testEquals_SameInstance() {
        // Test that an object equals itself.  This covers basic object equality.
        assertEquals(ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16BE, "UTF-16BE should equal itself");
        assertEquals(ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16LE, "UTF-16LE should equal itself");
        assertEquals(ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32BE, "UTF-32BE should equal itself");
        assertEquals(ByteOrderMark.UTF_32LE, ByteOrderMark.UTF_32LE, "UTF-32LE should equal itself");
        assertEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_8, "UTF-8 should equal itself");

        assertEquals(TEST_BOM_1, TEST_BOM_1, "TEST_BOM_1 should equal itself");
        assertEquals(TEST_BOM_2, TEST_BOM_2, "TEST_BOM_2 should equal itself");
        assertEquals(TEST_BOM_3, TEST_BOM_3, "TEST_BOM_3 should equal itself");
    }

    @Test
    public void testEquals_DifferentInstances_SameValues() {
        // Test that different instances with the same values are considered equal.
        // This tests that the ByteOrderMark class correctly overrides the equals method.
        ByteOrderMark utf16be = ByteOrderMark.UTF_16BE; // Get the object to avoid direct comparison
        assertEquals(ByteOrderMark.UTF_16BE, utf16be, "UTF-16BE should equal another instance of UTF-16BE");
    }

    @Test
    public void testEquals_DifferentValues() {
        // Test that ByteOrderMarks with different values are not equal.
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, "UTF-8 should not equal UTF-16BE");
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE, "UTF-8 should not equal UTF-16LE");
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32BE, "UTF-8 should not equal UTF-32BE");
        assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32LE, "UTF-8 should not equal UTF-32LE");

        assertNotEquals(TEST_BOM_1, new ByteOrderMark("1a", 2), "TEST_BOM_1 should not equal ByteOrderMark with different values (length 2)");
        assertNotEquals(TEST_BOM_1, new ByteOrderMark("1b", 1, 2), "TEST_BOM_1 should not equal ByteOrderMark with different values (length 1 and extra bytes)");
        assertNotEquals(TEST_BOM_2, new ByteOrderMark("2", 1, 1), "TEST_BOM_2 should not equal ByteOrderMark with different values");
        assertNotEquals(TEST_BOM_3, new ByteOrderMark("3", 1, 2, 4), "TEST_BOM_3 should not equal ByteOrderMark with different values");
    }

    @Test
    public void testEquals_DifferentObjectType() {
        // Test that a ByteOrderMark is not equal to an object of a different type.
        assertNotEquals(TEST_BOM_1, new Object(), "TEST_BOM_1 should not equal an Object");
    }
}