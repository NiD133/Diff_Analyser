/**
 * Tests for the ByteUtils class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ByteUtils_ESTest extends ByteUtils_ESTest_scaffolding {

    /**
     * Test that fromLittleEndian with a byte array and an offset returns the correct value.
     *
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testFromLittleEndian_ByteArray_Offset() throws Throwable {
        // Arrange
        byte[] bytes = new byte[9];
        bytes[0] = (byte) 2;

        // Act
        long result = ByteUtils.fromLittleEndian(bytes, 0, 1);

        // Assert
        assertEquals(2L, result);
    }

    /**
     * Test that fromLittleEndian with a byte supplier and a length returns the correct value.
     *
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testFromLittleEndian_ByteSupplier_Length() throws Throwable {
        // Arrange
        ByteUtils.ByteSupplier byteSupplier = mock(ByteUtils.ByteSupplier.class, new ViolatedAssumptionAnswer());
        doReturn(3148, 3148, 856, 3148, 3148).when(byteSupplier).getAsByte();

        // Act
        long result = ByteUtils.fromLittleEndian(byteSupplier, 8);

        // Assert
        assertEquals(5497853135745207372L, result);
    }

    /**
     * Test that fromLittleEndian with a data input and a length returns the correct value.
     *
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testFromLittleEndian_DataInput_Length() throws Throwable {
        // Arrange
        byte[] bytes = new byte[2];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

        // Act
        long result = ByteUtils.fromLittleEndian(dataInputStream, 2);

        // Assert
        assertEquals(0L, result);
    }

    /**
     * Test that fromLittleEndian with an input stream and a length returns the correct value.
     *
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testFromLittleEndian_InputStream_Length() throws Throwable {
        // Arrange
        byte[] bytes = new byte[6];
        bytes[0] = (byte) 95;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        // Act
        long result = ByteUtils.fromLittleEndian(byteArrayInputStream, 1);

        // Assert
        assertEquals(95L, result);
    }

    /**
     * Test that toLittleEndian with a byte array, a value, an offset, and a length writes the correct value.
     *
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testToLittleEndian_ByteArray_Value_Offset_Length() throws Throwable {
        // Arrange
        byte[] bytes = new byte[9];
        long value = 0L;

        // Act
        ByteUtils.toLittleEndian(bytes, value, 0, 8);

        // Assert
        assertArrayEquals(new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0}, bytes);
    }

    /**
     * Test that toLittleEndian with a byte consumer, a value, and a length writes the correct value.
     *
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testToLittleEndian_ByteConsumer_Value_Length() throws Throwable {
        // Arrange
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteUtils.ByteConsumer byteConsumer = new ByteUtils.OutputStreamByteConsumer(byteArrayOutputStream);

        // Act
        ByteUtils.toLittleEndian(byteConsumer, 0L, 8);

        // Assert
        assertEquals(0, byteArrayOutputStream.size());
    }

    /**
     * Test that toLittleEndian with an output stream, a value, and a length writes the correct value.
     *
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testToLittleEndian_OutputStream_Value_Length() throws Throwable {
        // Arrange
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Act
        ByteUtils.toLittleEndian(byteArrayOutputStream, 0L, 8);

        // Assert
        assertEquals(0, byteArrayOutputStream.size());
    }

    /**
     * Test that fromLittleEndian with a null byte array throws a NullPointerException.
     *
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testFromLittleEndian_NullByteArray() throws Throwable {
        // Act and Assert
        try {
            ByteUtils.fromLittleEndian((byte[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test that fromLittleEndian with a null byte supplier throws an IllegalArgumentException.
     *
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testFromLittleEndian_NullByteSupplier() throws Throwable {
        // Act and Assert
        try {
            ByteUtils.fromLittleEndian((ByteUtils.ByteSupplier) null, 8);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test that fromLittleEndian with a null data input throws a NullPointerException.
     *
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testFromLittleEndian_NullDataInput() throws Throwable {
        // Act and Assert
        try {
            ByteUtils.fromLittleEndian((DataInput) null, 8);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test that fromLittleEndian with a null input stream throws a NullPointerException.
     *
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testFromLittleEndian_NullInputStream() throws Throwable {
        // Act and Assert
        try {
            ByteUtils.fromLittleEndian((InputStream) null, 8);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test that toLittleEndian with a null byte array throws a NullPointerException.
     *
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testToLittleEndian_NullByteArray() throws Throwable {
        // Act and Assert
        try {
            ByteUtils.toLittleEndian((byte[]) null, 0L, 0, 8);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test that toLittleEndian with a null byte consumer throws a NullPointerException.
     *
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testToLittleEndian_NullByteConsumer() throws Throwable {
        // Act and Assert
        try {
            ByteUtils.toLittleEndian((ByteUtils.ByteConsumer) null, 0L, 8);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test that toLittleEndian with a null output stream throws a NullPointerException.
     *
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testToLittleEndian_NullOutputStream() throws Throwable {
        // Act and Assert
        try {
            ByteUtils.toLittleEndian((OutputStream) null, 0L, 8);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}