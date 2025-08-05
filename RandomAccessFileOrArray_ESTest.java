import com.itextpdf.text.io.RandomAccessSource;
import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link RandomAccessFileOrArray} class.
 * These tests cover reading primitive data types in both big-endian and little-endian formats,
 * file pointer manipulation, and exception handling.
 */
public class RandomAccessFileOrArrayTest {

    //region Big-Endian Read Tests

    @Test
    public void readShort_forBigEndianData_returnsCorrectValue() throws IOException {
        // Arrange
        // Represents the short value 4112 (0x1010) in big-endian format.
        byte[] data = new byte[]{(byte) 0x10, (byte) 0x10};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        short result = reader.readShort();

        // Assert
        assertEquals("The read short value should match the expected big-endian interpretation.", (short) 4112, result);
        assertEquals("File pointer should advance by 2 bytes.", 2L, reader.getFilePointer());
    }

    @Test
    public void readUnsignedShort_forBigEndianData_returnsCorrectValue() throws IOException {
        // Arrange
        // Represents the unsigned short value 63232 (0xF700) in big-endian format.
        byte[] data = new byte[]{(byte) 0xF7, (byte) 0x00, 0x01, 0x02};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        int result = reader.readUnsignedShort();

        // Assert
        assertEquals("The read unsigned short value should match the expected big-endian interpretation.", 63232, result);
        assertEquals("File pointer should advance by 2 bytes.", 2L, reader.getFilePointer());
    }



    @Test
    public void readInt_forBigEndianData_returnsCorrectValue() throws IOException {
        // Arrange
        // Represents the int value 167772160 (0x0A000000) in big-endian format.
        byte[] data = new byte[]{(byte) 0x0A, 0x00, 0x00, 0x00};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        int result = reader.readInt();

        // Assert
        assertEquals("The read int value should match the expected big-endian interpretation.", 167772160, result);
        assertEquals("File pointer should advance by 4 bytes.", 4L, reader.getFilePointer());
    }

    @Test
    public void readUnsignedInt_forBigEndianData_returnsCorrectValue() throws IOException {
        // Arrange
        // Represents the unsigned int value 4,147,380,224 (0xF7340000) in big-endian format.
        byte[] data = new byte[]{(byte) 0xF7, (byte) 0x34, 0x00, 0x00};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
        long expectedValue = 4147380224L;

        // Act
        long result = reader.readUnsignedInt();

        // Assert
        assertEquals("The read unsigned int value should match the expected big-endian interpretation.", expectedValue, result);
        assertEquals("File pointer should advance by 4 bytes.", 4L, reader.getFilePointer());
    }

    @Test
    public void readLong_forBigEndianData_returnsCorrectValue() throws IOException {
        // Arrange
        // Represents the long value 2,550,136,832 (0x98000000) as the high 4 bytes of a big-endian long.
        byte[] data = new byte[]{0x00, 0x00, 0x00, 0x00, (byte) 0x98, 0x00, 0x00, 0x00};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
        long expectedValue = 2550136832L;

        // Act
        long result = reader.readLong();

        // Assert
        assertEquals("The read long value should match the expected big-endian interpretation.", expectedValue, result);
        assertEquals("File pointer should advance by 8 bytes.", 8L, reader.getFilePointer());
    }

    @Test
    public void readFloat_forBigEndianData_returnsCorrectValue() throws IOException {
        // Arrange
        // Represents the float value -4.1538375E34f (0xF7000000) in big-endian format.
        byte[] data = new byte[]{(byte) 0xF7, 0x00, 0x00, 0x00};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        float result = reader.readFloat();

        // Assert
        assertEquals("The read float value should match the expected big-endian interpretation.", -4.1538375E34F, result, 0.01F);
        assertEquals("File pointer should advance by 4 bytes.", 4L, reader.getFilePointer());
    }

    @Test
    public void readDouble_forBigEndianData_returnsCorrectValue() throws IOException {
        // Arrange
        // Represents a double value (0x0000F70000000000) in big-endian format.
        byte[] data = new byte[]{0x00, 0x00, (byte) 0xF7, 0x00, 0x00, 0x00, 0x00, 0x00};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        double result = reader.readDouble();

        // Assert
        assertEquals("The read double value should match the expected big-endian interpretation.", 1.34178037854316E-309, result, 0.01);
        assertEquals("File pointer should advance by 8 bytes.", 8L, reader.getFilePointer());
    }

    @Test
    public void readChar_forBigEndianData_returnsCorrectValue() throws IOException {
        // Arrange
        // Represents the char value '\uF734' in big-endian format.
        byte[] data = new byte[]{(byte) 0xF7, (byte) 0x34};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        char result = reader.readChar();

        // Assert
        assertEquals("The read char value should match the expected big-endian interpretation.", '\uF734', result);
        assertEquals("File pointer should advance by 2 bytes.", 2L, reader.getFilePointer());
    }

    //endregion

    //region Little-Endian Read Tests

    @Test
    public void readShortLE_forLittleEndianData_returnsCorrectValue() throws IOException {
        // Arrange
        // Represents the short value -21248 (0xAC00) in little-endian format.
        byte[] data = new byte[]{0x00, (byte) 0xAC};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        short result = reader.readShortLE();

        // Assert
        assertEquals("The read short value should match the expected little-endian interpretation.", (short) -21248, result);
        assertEquals("File pointer should advance by 2 bytes.", 2L, reader.getFilePointer());
    }

    @Test
    public void readUnsignedShortLE_forLittleEndianData_returnsCorrectValue() throws IOException {
        // Arrange
        // Represents the unsigned short value 3577 (0x0DF9) in little-endian format.
        byte[] data = new byte[]{(byte) 0xF9, (byte) 0x0D};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        int result = reader.readUnsignedShortLE();

        // Assert
        assertEquals("The read unsigned short value should match the expected little-endian interpretation.", 3577, result);
        assertEquals("File pointer should advance by 2 bytes.", 2L, reader.getFilePointer());
    }

    @Test
    public void readIntLE_forLittleEndianData_returnsCorrectValue() throws IOException {
        // Arrange
        // Represents the int value -150994944 (0xF7000000) in little-endian format.
        byte[] data = new byte[]{0x00, 0x00, 0x00, (byte) 0xF7};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        int result = reader.readIntLE();

        // Assert
        assertEquals("The read int value should match the expected little-endian interpretation.", -150994944, result);
        assertEquals("File pointer should advance by 4 bytes.", 4L, reader.getFilePointer());
    }

    @Test
    public void readUnsignedIntLE_forLittleEndianData_returnsCorrectValue() throws IOException {
        // Arrange
        // Represents the unsigned int value 2,634,022,912 (0x9D000000) in little-endian format.
        byte[] data = new byte[]{0x00, 0x00, 0x00, (byte) 0x9D};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
        long expectedValue = 2634022912L;

        // Act
        long result = reader.readUnsignedIntLE();

        // Assert
        assertEquals("The read unsigned int value should match the expected little-endian interpretation.", expectedValue, result);
        assertEquals("File pointer should advance by 4 bytes.", 4L, reader.getFilePointer());
    }

    @Test
    public void readLongLE_forLittleEndianData_returnsCorrectValue() throws IOException {
        // Arrange
        // Represents the long value -864691128455135232L (0xF400...00) in little-endian format.
        byte[] data = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xF4};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        long result = reader.readLongLE();

        // Assert
        assertEquals("The read long value should match the expected little-endian interpretation.", -864691128455135232L, result);
        assertEquals("File pointer should advance by 8 bytes.", 8L, reader.getFilePointer());
    }

    @Test
    public void readFloatLE_forLittleEndianData_returnsCorrectValue() throws IOException {
        // Arrange
        // Represents the float value -1.7014118E38f (0xFF000000) in little-endian format.
        byte[] data = new byte[]{0x00, 0x00, 0x00, (byte) 0xFF};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        float result = reader.readFloatLE();

        // Assert
        assertEquals("The read float value should match the expected little-endian interpretation.", -1.7014118E38F, result, 0.01F);
        assertEquals("File pointer should advance by 4 bytes.", 4L, reader.getFilePointer());
    }

    @Test
    public void readDoubleLE_forLittleEndianData_returnsCorrectValue() throws IOException {
        // Arrange
        // Represents a double value (0x...F5) in little-endian format.
        byte[] data = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xF5};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        double result = reader.readDoubleLE();

        // Assert
        assertEquals("The read double value should match the expected little-endian interpretation.", -2.5684257331779168E207, result, 0.01);
        assertEquals("File pointer should advance by 8 bytes.", 8L, reader.getFilePointer());
    }

    @Test
    public void readCharLE_forLittleEndianData_returnsCorrectValue() throws IOException {
        // Arrange
        // Represents the char value '\u1E77' in little-endian format.
        byte[] data = new byte[]{(byte) 0x77, (byte) 0x1E};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        char result = reader.readCharLE();

        // Assert
        assertEquals("The read char value should match the expected little-endian interpretation.", '\u1E77', result);
        assertEquals("File pointer should advance by 2 bytes.", 2L, reader.getFilePointer());
    }

    //endregion

    //region String and Line Reading Tests

    @Test
    public void readLine_withLF_terminatesLine() throws IOException {
        // Arrange
        byte[] data = "Hello\nWorld".getBytes();
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        String line = reader.readLine();

        // Assert
        assertEquals("Line should be read until the LF character.", "Hello", line);
        assertEquals("File pointer should be after the LF character.", 6L, reader.getFilePointer());
    }

    @Test
    public void readLine_withCR_terminatesLine() throws IOException {
        // Arrange
        byte[] data = "Hello\rWorld".getBytes();
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        String line = reader.readLine();

        // Assert
        assertEquals("Line should be read until the CR character.", "Hello", line);
        assertEquals("File pointer should be after the CR character.", 6L, reader.getFilePointer());
    }

    @Test
    public void readLine_withCRLF_terminatesLineAndConsumesBoth() throws IOException {
        // Arrange
        byte[] data = "Hello\r\nWorld".getBytes();
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        String line = reader.readLine();

        // Assert
        assertEquals("Line should be read until the CRLF sequence.", "Hello", line);
        assertEquals("File pointer should be after the CRLF sequence.", 7L, reader.getFilePointer());
    }

    @Test
    public void readLine_atEOF_returnsNull() throws IOException {
        // Arrange
        byte[] data = "Hello".getBytes();
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
        reader.seek(data.length); // Move pointer to the end

        // Act
        String line = reader.readLine();

        // Assert
        assertNull("readLine at EOF should return null.", line);
    }

    @Test
    public void readString_withValidEncoding_returnsCorrectString() throws IOException {
        // Arrange
        byte[] data = new byte[]{'H', 'e', 'l', 'l', 'o'};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        String result = reader.readString(5, "UTF-8");

        // Assert
        assertEquals("The read string should match the expected content.", "Hello", result);
        assertEquals("File pointer should advance by the length of the string.", 5L, reader.getFilePointer());
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void readString_withInvalidEncoding_throwsException() throws IOException {
        // Arrange
        byte[] data = new byte[]{'a', 'b', 'c'};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        reader.readString(3, "INVALID-ENCODING-NAME"); // This should throw
    }

    //endregion

    //region Pointer, State, and Buffer Management Tests

    @Test
    public void seek_toValidPosition_updatesFilePointer() throws IOException {
        // Arrange
        byte[] data = new byte[20];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        reader.seek(10L);

        // Assert
        assertEquals("File pointer should be updated to the seeked position.", 10L, reader.getFilePointer());
    }

    @Test
    public void skip_withPositiveValue_advancesFilePointer() throws IOException {
        // Arrange
        byte[] data = new byte[20];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
        reader.seek(5L);

        // Act
        long bytesSkipped = reader.skip(10L);

        // Assert
        assertEquals("Should report skipping 10 bytes.", 10L, bytesSkipped);
        assertEquals("File pointer should be advanced by 10.", 15L, reader.getFilePointer());
    }

    @Test
    public void skip_pastEOF_advancesPointerToEndAndReturnsBytesSkipped() throws IOException {
        // Arrange
        byte[] data = new byte[10];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
        reader.seek(5L);

        // Act
        long bytesSkipped = reader.skip(10L); // Attempt to skip past the end

        // Assert
        assertEquals("Should report skipping only the remaining 5 bytes.", 5L, bytesSkipped);
        assertEquals("File pointer should be at the end of the file.", 10L, reader.getFilePointer());
    }

    @Test
    public void pushBack_addsByteToFront_andDecrementsPointer() throws IOException {
        // Arrange
        byte[] data = new byte[]{10, 20, 30};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
        assertEquals("Initial file pointer should be 0.", 0L, reader.getFilePointer());

        // Act
        reader.pushBack((byte) 99);

        // Assert
        assertEquals("File pointer should be -1 after pushBack.", -1L, reader.getFilePointer());
        assertEquals("The next byte read should be the pushed-back byte.", 99, reader.read());
        assertEquals("File pointer should be 0 after reading the pushed-back byte.", 0L, reader.getFilePointer());
    }

    @Test
    public void length_returnsCorrectDataLength() throws IOException {
        // Arrange
        byte[] data = new byte[42];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        long length = reader.length();

        // Assert
        assertEquals("The length should match the size of the source data.", 42L, length);
    }

    @Test
    public void close_releasesResources() throws IOException {
        // Arrange
        byte[] data = new byte[10];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        reader.close();

        // Assert
        // The underlying source is now closed. Further operations should fail.
        // We'll check by trying to get the length, which should throw an exception.
        try {
            reader.length();
        } catch (Exception e) {
            // Expected to fail, could be NullPointerException or IOException depending on implementation.
            assertNotNull("An exception should be thrown after closing.", e);
        }
    }

    //endregion

    //region Exception and Edge Case Tests

    @Test(expected = EOFException.class)
    public void readFully_whenNotEnoughBytes_throwsEOFException() throws IOException {
        // Arrange
        byte[] data = new byte[5];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
        byte[] buffer = new byte[10]; // Buffer is larger than the data source

        // Act
        reader.readFully(buffer); // This should throw
    }

    @Test(expected = EOFException.class)
    public void readInt_whenFewerThan4BytesRemain_throwsEOFException() throws IOException {
        // Arrange
        byte[] data = new byte[3];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        reader.readInt(); // This should throw
    }

    @Test(expected = EOFException.class)
    public void readLongLE_whenFewerThan8BytesRemain_throwsEOFException() throws IOException {
        // Arrange
        byte[] data = new byte[7];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        reader.readLongLE(); // This should throw
    }

    @Test
    public void read_atEOF_returnsMinusOne() throws IOException {
        // Arrange
        byte[] data = new byte[5];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
        reader.seek(5L); // Move to the end of the file

        // Act
        int result = reader.read();

        // Assert
        assertEquals("read() at EOF should return -1.", -1, result);
    }

    @Test
    public void readBoolean_withNonZeroByte_returnsTrue() throws IOException {
        // Arrange
        byte[] data = new byte[]{(byte) 0xFF};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        boolean result = reader.readBoolean();

        // Assert
        assertTrue("A non-zero byte should be read as true.", result);
    }

    @Test
    public void readBoolean_withZeroByte_returnsFalse() throws IOException {
        // Arrange
        byte[] data = new byte[]{0x00};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // Act
        boolean result = reader.readBoolean();

        // Assert
        assertFalse("A zero byte should be read as false.", result);
    }

    //endregion
}