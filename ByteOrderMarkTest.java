package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteOrderMark}.
 *
 * The tests are structured into nested classes to group tests for specific methods or related functionalities,
 * improving readability and maintainability.  Clear and descriptive test names are used.
 */
class ByteOrderMarkTest {

    private ByteOrderMark testBom1;
    private ByteOrderMark testBom2;
    private ByteOrderMark testBom3;

    @BeforeEach
    void setUp() {
        // Initialize test BOMs before each test
        testBom1 = new ByteOrderMark("test1", 1);
        testBom2 = new ByteOrderMark("test2", 1, 2);
        testBom3 = new ByteOrderMark("test3", 1, 2, 3);
    }


    @Nested
    class ConstantCharsetNamesTest {
        @Test
        void testConstantCharsetNamesCanBeLoadedAsCharsets() {
            // Test that the charset names associated with the predefined ByteOrderMark constants
            // can be successfully loaded as Charset objects.  This verifies that the names are valid
            // and supported by the JVM.
            assertNotNull(Charset.forName(ByteOrderMark.UTF_8.getCharsetName()), "UTF-8 should be a valid charset");
            assertNotNull(Charset.forName(ByteOrderMark.UTF_16BE.getCharsetName()), "UTF-16BE should be a valid charset");
            assertNotNull(Charset.forName(ByteOrderMark.UTF_16LE.getCharsetName()), "UTF-16LE should be a valid charset");
            assertNotNull(Charset.forName(ByteOrderMark.UTF_32BE.getCharsetName()), "UTF-32BE should be a valid charset");
            assertNotNull(Charset.forName(ByteOrderMark.UTF_32LE.getCharsetName()), "UTF-32LE should be a valid charset");
        }
    }


    @Nested
    class ConstructorExceptionTest {
        @Test
        void testConstructorThrowsNullPointerExceptionForNullCharsetName() {
            assertThrows(NullPointerException.class, () -> new ByteOrderMark(null, 1, 2, 3),
                    "Should throw NullPointerException when charsetName is null");
        }

        @Test
        void testConstructorThrowsIllegalArgumentExceptionForEmptyCharsetName() {
            assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("", 1, 2, 3),
                    "Should throw IllegalArgumentException when charsetName is empty");
        }

        @Test
        void testConstructorThrowsNullPointerExceptionForNullBytesArray() {
            assertThrows(NullPointerException.class, () -> new ByteOrderMark("a", (int[]) null),
                    "Should throw NullPointerException when bytes array is null");
        }

        @Test
        void testConstructorThrowsIllegalArgumentExceptionForEmptyBytesArray() {
            assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("b"),
                    "Should throw IllegalArgumentException when bytes array is empty");
        }
    }

    @Nested
    class EqualsTest {
        @SuppressWarnings("EqualsWithItself")
        @Test
        void testEqualsReturnsTrueForSameInstance() {
            // Test that equals() returns true when comparing a ByteOrderMark instance to itself.
            assertEquals(ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16BE, "UTF_16BE should equal itself");
            assertEquals(ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16LE, "UTF_16LE should equal itself");
            assertEquals(ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32BE, "UTF_32BE should equal itself");
            assertEquals(ByteOrderMark.UTF_32LE, ByteOrderMark.UTF_32LE, "UTF_32LE should equal itself");
            assertEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_8, "UTF_8 should equal itself");

            assertEquals(testBom1, testBom1, "test1 should equal itself");
            assertEquals(testBom2, testBom2, "test2 should equal itself");
            assertEquals(testBom3, testBom3, "test3 should equal itself");
        }

        @Test
        void testEqualsReturnsFalseForDifferentInstances() {
            // Test that equals() returns false when comparing different ByteOrderMark instances.
            assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, "UTF-8 should not equal UTF-16BE");
            assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE, "UTF-8 should not equal UTF-16LE");
            assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32BE, "UTF-8 should not equal UTF-32BE");
            assertNotEquals(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32LE, "UTF-8 should not equal UTF-32LE");
        }

        @Test
        void testEqualsReturnsFalseForDifferentObjects() {
            // Test that equals() returns false when comparing a ByteOrderMark instance to an object of a different class.
            assertNotEquals(testBom1, new Object(), "Should not equal a different object type");
        }

        @Test
        void testEqualsReturnsFalseForDifferentByteOrderMarks() {
            // Test various scenarios where ByteOrderMarks are different based on charset name and/or byte values
            assertNotEquals(testBom1, new ByteOrderMark("1a", 2), "Different charset name and byte");
            assertNotEquals(testBom1, new ByteOrderMark("1b", 1, 2), "Different charset name and bytes");
            assertNotEquals(testBom2, new ByteOrderMark("2", 1, 1), "Different bytes");
            assertNotEquals(testBom3, new ByteOrderMark("3", 1, 2, 4), "Different bytes");
        }
    }


    @Nested
    class GetBytesTest {
        @Test
        void testGetBytesReturnsCorrectByteArray() {
            // Verify that getBytes() returns a copy of the internal byte array, ensuring that modifications
            // to the returned array do not affect the original ByteOrderMark instance.
            assertArrayEquals(testBom1.getBytes(), new byte[] { (byte) 1 }, "test1 bytes");

            // Attempt to modify the returned byte array
            testBom1.getBytes()[0] = 2;

            // Verify that the original ByteOrderMark's bytes remain unchanged
            assertArrayEquals(testBom1.getBytes(), new byte[] { (byte) 1 }, "test1 bytes after modification attempt");
            assertArrayEquals(testBom2.getBytes(), new byte[] { (byte) 1, (byte) 2 }, "test2 bytes");
            assertArrayEquals(testBom3.getBytes(), new byte[] { (byte) 1, (byte) 2, (byte) 3 }, "test3 bytes");
        }
    }

    @Nested
    class GetCharsetNameTest {
        @Test
        void testGetCharsetNameReturnsCorrectName() {
            // Verify that getCharsetName() returns the correct charset name for each ByteOrderMark instance.
            assertEquals("test1", testBom1.getCharsetName(), "test1 name");
            assertEquals("test2", testBom2.getCharsetName(), "test2 name");
            assertEquals("test3", testBom3.getCharsetName(), "test3 name");
        }
    }

    @Nested
    class GetIntTest {
        @Test
        void testGetIntReturnsCorrectByteValueAtIndex() {
            // Verify that get(int) returns the correct byte value at the specified index for each ByteOrderMark.
            assertEquals(1, testBom1.get(0), "test1 get(0)");
            assertEquals(1, testBom2.get(0), "test2 get(0)");
            assertEquals(2, testBom2.get(1), "test2 get(1)");
            assertEquals(1, testBom3.get(0), "test3 get(0)");
            assertEquals(2, testBom3.get(1), "test3 get(1)");
            assertEquals(3, testBom3.get(2), "test3 get(2)");
        }
    }

    @Nested
    class HashCodeTest {
        @Test
        void testHashCodeIsConsistent() {
            // Verify that hashCode() returns a consistent hash code for each ByteOrderMark instance.
            final int bomClassHash = ByteOrderMark.class.hashCode();
            assertEquals(bomClassHash + 1, testBom1.hashCode(), "hash test1 ");
            assertEquals(bomClassHash + 3, testBom2.hashCode(), "hash test2 ");
            assertEquals(bomClassHash + 6, testBom3.hashCode(), "hash test3 ");
        }
    }


    @Nested
    class LengthTest {
        @Test
        void testLengthReturnsCorrectLength() {
            // Verify that length() returns the correct number of bytes in each ByteOrderMark.
            assertEquals(1, testBom1.length(), "test1 length");
            assertEquals(2, testBom2.length(), "test2 length");
            assertEquals(3, testBom3.length(), "test3 length");
        }
    }

    @Nested
    class MatchesTest {
        @Test
        void testMatchesReturnsTrueForMatchingBytes() {
            // Test that matches() returns true when the input byte array matches the ByteOrderMark's bytes.
            assertTrue(ByteOrderMark.UTF_16BE.matches(ByteOrderMark.UTF_16BE.getRawBytes()), "UTF_16BE should match its own bytes");
            assertTrue(ByteOrderMark.UTF_16LE.matches(ByteOrderMark.UTF_16LE.getRawBytes()), "UTF_16LE should match its own bytes");
            assertTrue(ByteOrderMark.UTF_32BE.matches(ByteOrderMark.UTF_32BE.getRawBytes()), "UTF_32BE should match its own bytes");
            assertTrue(ByteOrderMark.UTF_16BE.matches(ByteOrderMark.UTF_16BE.getRawBytes()), "UTF_16BE should match its own bytes");
            assertTrue(ByteOrderMark.UTF_8.matches(ByteOrderMark.UTF_8.getRawBytes()), "UTF_8 should match its own bytes");

            assertTrue(testBom1.matches(testBom1.getRawBytes()), "test1 should match its own bytes");
            assertTrue(testBom2.matches(testBom2.getRawBytes()), "test2 should match its own bytes");
            assertTrue(testBom3.matches(testBom3.getRawBytes()), "test3 should match its own bytes");
        }

        @Test
        void testMatchesReturnsFalseForNonMatchingBytes() {
            assertFalse(testBom1.matches(new ByteOrderMark("1a", 2).getRawBytes()), "Different name and bytes");
            assertTrue(testBom1.matches(new ByteOrderMark("1b", 1, 2).getRawBytes()), "Name and bytes match partially");
            assertFalse(testBom2.matches(new ByteOrderMark("2", 1, 1).getRawBytes()), "Different bytes");
            assertFalse(testBom3.matches(new ByteOrderMark("3", 1, 2, 4).getRawBytes()), "Different bytes");
        }
    }

    @Nested
    class ToStringTest {
        @Test
        void testToStringReturnsCorrectStringRepresentation() {
            // Verify that toString() returns the expected string representation for each ByteOrderMark instance.
            assertEquals("ByteOrderMark[test1: 0x1]", testBom1.toString(), "test1 toString()");
            assertEquals("ByteOrderMark[test2: 0x1,0x2]", testBom2.toString(), "test2 toString()");
            assertEquals("ByteOrderMark[test3: 0x1,0x2,0x3]", testBom3.toString(), "test3 toString()");
        }
    }
}