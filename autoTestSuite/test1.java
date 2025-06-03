/**
 * Test class for ByteOrderMark.
 */
public class ByteOrderMarkTest {

    // Test constants
    private static final ByteOrderMark TEST_BOM_1 = new ByteOrderMark("test1", 1);
    private static final ByteOrderMark TEST_BOM_2 = new ByteOrderMark("test2", 1, 2);
    private static final ByteOrderMark TEST_BOM_3 = new ByteOrderMark("test3", 1, 2, 3);

    /**
     * Tests that the charset names of the predefined ByteOrderMarks can be loaded as a Charset.
     */
    @Test
    public void testPredefinedCharsetNames() {
        // Test that the charset names of the predefined ByteOrderMarks can be loaded as a Charset
        assertCharsetCanBeLoaded(ByteOrderMark.UTF_8);
        assertCharsetCanBeLoaded(ByteOrderMark.UTF_16BE);
        assertCharsetCanBeLoaded(ByteOrderMark.UTF_16LE);
        assertCharsetCanBeLoaded(ByteOrderMark.UTF_32BE);
        assertCharsetCanBeLoaded(ByteOrderMark.UTF_32LE);
    }

    /**
     * Tests that the constructor of ByteOrderMark throws the expected exceptions.
     */
    @Test
    public void testConstructorExceptions() {
        // Test that the constructor throws a NullPointerException when the charset name is null
        assertThrows(NullPointerException.class, () -> new ByteOrderMark(null, 1, 2, 3));

        // Test that the constructor throws an IllegalArgumentException when the charset name is empty
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("", 1, 2, 3));

        // Test that the constructor throws a NullPointerException when the bytes are null
        assertThrows(NullPointerException.class, () -> new ByteOrderMark("a", (int[]) null));

        // Test that the constructor throws an IllegalArgumentException when the bytes are empty
        assertThrows(IllegalArgumentException.class, () -> new ByteOrderMark("b"));
    }

    /**
     * Tests the equals method of ByteOrderMark.
     */
    @Test
    public void testEquals() {
        // Test that the predefined ByteOrderMarks are equal to themselves
        assertEqual(ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16BE);
        assertEqual(ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16LE);
        assertEqual(ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32BE);
        assertEqual(ByteOrderMark.UTF_32LE, ByteOrderMark.UTF_32LE);
        assertEqual(ByteOrderMark.UTF_8, ByteOrderMark.UTF_8);

        // Test that the predefined ByteOrderMarks are not equal to each other
        assertNotEqual(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE);
        assertNotEqual(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE);
        assertNotEqual(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32BE);
        assertNotEqual(ByteOrderMark.UTF_8, ByteOrderMark.UTF_32LE);

        // Test that the test ByteOrderMarks are equal to themselves
        assertEqual(TEST_BOM_1, TEST_BOM_1);
        assertEqual(TEST_BOM_2, TEST_BOM_2);
        assertEqual(TEST_BOM_3, TEST_BOM_3);

        // Test that the test ByteOrderMarks are not equal to other objects
        assertNotEqual(TEST_BOM_1, new Object());
        assertNotEqual(TEST_BOM_1, new ByteOrderMark("1a", 2));
        assertNotEqual(TEST_BOM_1, new ByteOrderMark("1b", 1, 2));
        assertNotEqual(TEST_BOM_2, new ByteOrderMark("2", 1, 1));
        assertNotEqual(TEST_BOM_3, new ByteOrderMark("3", 1, 2, 4));
    }

    /**
     * Tests the getBytes method of ByteOrderMark.
     */
    @Test
    public void testGetBytes() {
        // Test that the getBytes method returns the expected bytes
        assertBytesEqual(TEST_BOM_1.getBytes(), new byte[] { (byte) 1 });
        assertBytesEqual(TEST_BOM_2.getBytes(), new byte[] { (byte) 1, (byte) 2 });
        assertBytesEqual(TEST_BOM_3.getBytes(), new byte[] { (byte) 1, (byte) 2, (byte) 3 });

        // Test that the getBytes method returns a defensive copy
        byte[] bytes = TEST_BOM_1.getBytes();
        bytes[0] = 2;
        assertBytesEqual(TEST_BOM_1.getBytes(), new byte[] { (byte) 1 });
    }

    /**
     * Tests the getCharsetName method of ByteOrderMark.
     */
    @Test
    public void testGetCharsetName() {
        // Test that the getCharsetName method returns the expected charset name
        assertEquals("test1", TEST_BOM_1.getCharsetName());
        assertEquals("test2", TEST_BOM_2.getCharsetName());
        assertEquals("test3", TEST_BOM_3.getCharsetName());
    }

    /**
     * Tests the get method of ByteOrderMark.
     */
    @Test
    public void testGet() {
        // Test that the get method returns the expected byte
        assertEquals(1, TEST_BOM_1.get(0));
        assertEquals(1, TEST_BOM_2.get(0));
        assertEquals(2, TEST_BOM_2.get(1));
        assertEquals(1, TEST_BOM_3.get(0));
        assertEquals(2, TEST_BOM_3.get(1));
        assertEquals(3, TEST_BOM_3.get(2));
    }

    /**
     * Tests the hashCode method of ByteOrderMark.
     */
    @Test
    public void testHashCode() {
        // Test that the hashCode method returns the expected hash code
        int expectedHashCode = ByteOrderMark.class.hashCode();
        assertEquals(expectedHashCode + 1, TEST_BOM_1.hashCode());
        assertEquals(expectedHashCode + 3, TEST_BOM_2.hashCode());
        assertEquals(expectedHashCode + 6, TEST_BOM_3.hashCode());
    }

    /**
     * Tests the length method of ByteOrderMark.
     */
    @Test
    public void testLength() {
        // Test that the length method returns the expected length
        assertEquals(1, TEST_BOM_1.length());
        assertEquals(2, TEST_BOM_2.length());
        assertEquals(3, TEST_BOM_3.length());
    }

    /**
     * Tests the matches method of ByteOrderMark.
     */
    @Test
    public void testMatches() {
        // Test that the matches method returns true for the predefined ByteOrderMarks
        assertTrue(ByteOrderMark.UTF_16BE.matches(ByteOrderMark.UTF_16BE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_16LE.matches(ByteOrderMark.UTF_16LE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_32BE.matches(ByteOrderMark.UTF_32BE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_16BE.matches(ByteOrderMark.UTF_16BE.getRawBytes()));
        assertTrue(ByteOrderMark.UTF_8.matches(ByteOrderMark.UTF_8.getRawBytes()));

        // Test that the matches method returns true for the test ByteOrderMarks
        assertTrue(TEST_BOM_1.matches(TEST_BOM_1.getRawBytes()));
        assertTrue(TEST_BOM_2.matches(TEST_BOM_2.getRawBytes()));
        assertTrue(TEST_BOM_3.matches(TEST_BOM_3.getRawBytes()));

        // Test that the matches method returns false for different ByteOrderMarks
        assertFalse(TEST_BOM_1.matches(new ByteOrderMark("1a", 2).getRawBytes()));
        assertTrue(TEST_BOM_1.matches(new ByteOrderMark("1b", 1, 2).getRawBytes()));
        assertFalse(TEST_BOM_2.matches(new ByteOrderMark("2", 1, 1).getRawBytes()));
        assertFalse(TEST_BOM_3.matches(new ByteOrderMark("3", 1, 2, 4).getRawBytes()));
    }

    /**
     * Tests the toString method of ByteOrderMark.
     */
    @Test
    public void testToString() {
        // Test that the toString method returns the expected string
        assertEquals("ByteOrderMark[test1: 0x1]", TEST_BOM_1.toString());
        assertEquals("ByteOrderMark[test2: 0x1,0x2]", TEST_BOM_2.toString());
        assertEquals("ByteOrderMark[test3: 0x1,0x2,0x3]", TEST_BOM_3.toString());
    }

    // Helper methods

    private void assertCharsetCanBeLoaded(ByteOrderMark bom) {
        assertNotNull(Charset.forName(bom.getCharsetName()));
    }

    private void assertEqual(ByteOrderMark bom1, ByteOrderMark bom2) {
        assertEquals(bom1, bom2);
    }

    private void assertNotEqual(ByteOrderMark bom1, ByteOrderMark bom2) {
        assertNotEquals(bom1, bom2);
    }

    private void assertBytesEqual(byte[] bytes1, byte[] bytes2) {
        assertArrayEquals(bytes1, bytes2);
    }
}