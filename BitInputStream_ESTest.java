package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.InputStream;
import java.nio.ByteOrder;
import org.apache.commons.compress.utils.BitInputStream;

/**
 * This test class contains JUnit tests for the BitInputStream class.
 */
public class BitInputStreamTest {

    /**
     * Tests that the constructor throws a NoClassDefFoundError when null InputStream is passed.
     * 
     * @throws Throwable
     */
    @Test(timeout = 4000, expected = NoClassDefFoundError.class)
    public void testConstructor_WithNullInputStream_ThrowsNoClassDefFoundError() throws Throwable {
        // Arrange
        ByteOrder byteOrder = ByteOrder.nativeOrder();
        InputStream inputStream = null;

        // Act and Assert
        new BitInputStream(inputStream, byteOrder);
    }

    /**
     * Tests that the constructor does not throw an exception when a valid InputStream is passed.
     * 
     * @throws Throwable
     */
    @Test(timeout = 4000)
    public void testConstructor_WithValidInputStream_DoesNotThrowException() throws Throwable {
        // Arrange
        ByteOrder byteOrder = ByteOrder.nativeOrder();
        InputStream inputStream = this.getClass().getResourceAsStream("/testFile.txt");

        // Act
        new BitInputStream(inputStream, byteOrder);

        // Assert
        // No exception was thrown, so the test passes.
    }
}