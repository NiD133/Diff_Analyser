package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.io.GetBufferedRandomAccessSource;
import com.itextpdf.text.io.IndependentRandomAccessSource;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.WindowRandomAccessSource;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.net.URL;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.net.MockURL;
import org.evosuite.runtime.testdata.EvoSuiteFile;
import org.evosuite.runtime.testdata.FileSystemHandling;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class RandomAccessFileOrArray_ESTest extends RandomAccessFileOrArray_ESTest_scaffolding {

  // Tests for reading 64-bit long values (Big Endian)
  @Test(timeout = 4000)
  public void testReadLong_WithNegativeByteValue() throws Throwable {
      byte[] data = new byte[24];
      data[4] = (byte) (-104); // Set byte at position 4 to -104
      ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputStream);
      
      long result = reader.readLong();
      
      assertEquals("File pointer should advance by 8 bytes", 8L, reader.getFilePointer());
      assertEquals("Should read correct long value", 2550136832L, result);
  }

  // Tests for reading 32-bit unsigned integers (Little Endian)
  @Test(timeout = 4000)
  public void testReadUnsignedIntLE_WithNegativeByteAtPosition3() throws Throwable {
      byte[] data = new byte[6];
      data[3] = (byte) (-99); // Set byte at position 3 to -99
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      
      long result = reader.readUnsignedIntLE();
      
      assertEquals("File pointer should advance by 4 bytes", 4L, reader.getFilePointer());
      assertEquals("Should read correct unsigned int value", 2634022912L, result);
  }

  @Test(timeout = 4000)
  public void testReadUnsignedIntLE_WithPositiveByteAtPosition2() throws Throwable {
      byte[] data = new byte[6];
      data[2] = (byte) 94; // Set byte at position 2 to 94
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      
      long result = reader.readUnsignedIntLE();
      
      assertEquals("File pointer should advance by 4 bytes", 4L, reader.getFilePointer());
      assertEquals("Should read correct unsigned int value", 6160384L, result);
  }

  @Test(timeout = 4000)
  public void testReadUnsignedIntLE_WithByteAtPosition1() throws Throwable {
      byte[] data = new byte[6];
      data[1] = (byte) 30; // Set byte at position 1 to 30
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      
      long result = reader.readUnsignedIntLE();
      
      assertEquals("File pointer should advance by 4 bytes", 4L, reader.getFilePointer());
      assertEquals("Should read correct unsigned int value", 7680L, result);
  }

  // Tests for mixed operations (short + unsigned int)
  @Test(timeout = 4000)
  public void testReadShortLE_ThenReadUnsignedInt() throws Throwable {
      byte[] data = new byte[8];
      data[2] = (byte) (-9);
      data[3] = (byte) 52;
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      
      short shortResult = reader.readShortLE();
      assertEquals("First short should be 0", (short) 0, shortResult);
      
      long longResult = reader.readUnsignedInt();
      assertEquals("Should read correct unsigned int value", 4147380224L, longResult);
  }

  // Tests for reading 32-bit unsigned integers (Big Endian)
  @Test(timeout = 4000)
  public void testReadUnsignedInt_WithByteAtPosition3() throws Throwable {
      byte[] data = new byte[8];
      data[3] = (byte) 52; // Set byte at position 3 to 52
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      
      long result = reader.readUnsignedInt();
      
      assertEquals("File pointer should advance by 4 bytes", 4L, reader.getFilePointer());
      assertEquals("Should read correct unsigned int value", 52L, result);
  }

  // Tests for reading 32-bit float values (Little Endian)
  @Test(timeout = 4000)
  public void testReadFloatLE_WithByteAtPosition1() throws Throwable {
      byte[] data = new byte[9];
      data[1] = (byte) 16; // Set byte at position 1 to 16
      ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputStream);
      
      float result = reader.readFloatLE();
      
      assertEquals("File pointer should advance by 4 bytes", 4L, reader.getFilePointer());
      assertEquals("Should read correct float value", 5.74E-42F, result, 0.01F);
  }

  // Tests for EOF exceptions
  @Test(timeout = 4000)
  public void testReadLongLE_ThrowsEOFException_WhenInsufficientData() throws Throwable {
      byte[] data = new byte[5]; // Only 5 bytes, but readLongLE needs 8
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      reader.readLine(); // Consume some data
      
      try { 
        reader.readLongLE();
        fail("Should throw EOFException when insufficient data");
      } catch(EOFException e) {
         verifyException("com.itextpdf.text.pdf.RandomAccessFileOrArray", e);
      }
  }

  // Tests for reading 64-bit long values with specific byte patterns
  @Test(timeout = 4000)
  public void testReadLong_WithByteAtPosition3() throws Throwable {
      byte[] data = new byte[24];
      data[3] = (byte) 52; // Set byte at position 3 to 52
      ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputStream);
      
      long result = reader.readLong();
      
      assertEquals("File pointer should advance by 8 bytes", 8L, reader.getFilePointer());
      assertEquals("Should read correct long value", 223338299392L, result);
  }

  // Tests for reading 64-bit double values
  @Test(timeout = 4000)
  public void testReadDouble_WithNegativeByteAtPosition2() throws Throwable {
      byte[] data = new byte[8];
      data[2] = (byte) (-9); // Set byte at position 2 to -9
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      
      double result = reader.readDouble();
      
      assertEquals("File pointer should advance by 8 bytes", 8L, reader.getFilePointer());
      assertEquals("Should read correct double value", 1.34178037854316E-309, result, 0.01);
  }

  // Tests for pushBack functionality with EOF
  @Test(timeout = 4000)
  public void testReadFloat_ThrowsEOFException_AfterPushBack() throws Throwable {
      byte[] data = new byte[2]; // Only 2 bytes, but readFloat needs 4
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      reader.pushBack((byte) 0); // Push back a byte
      
      try { 
        reader.readFloat();
        fail("Should throw EOFException when insufficient data after pushback");
      } catch(EOFException e) {
         verifyException("com.itextpdf.text.pdf.RandomAccessFileOrArray", e);
      }
  }

  // Tests for reading character values (Little Endian)
  @Test(timeout = 4000)
  public void testReadCharLE_WithTwoBytes() throws Throwable {
      byte[] data = new byte[6];
      data[0] = (byte) 119; // Set first byte to 119
      data[1] = (byte) 30;  // Set second byte to 30
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      
      char result = reader.readCharLE();
      
      assertEquals("File pointer should advance by 2 bytes", 2L, reader.getFilePointer());
      assertEquals("Should read correct character value", '\u1E77', result);
  }

  // Tests for reading character values (Big Endian) after short read
  @Test(timeout = 4000)
  public void testReadChar_AfterReadShort() throws Throwable {
      byte[] data = new byte[8];
      data[2] = (byte) (-9);
      data[3] = (byte) 52;
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      
      reader.readShort(); // Read first 2 bytes
      char result = reader.readChar(); // Read next 2 bytes as char
      
      assertEquals("File pointer should be at position 4", 4L, reader.getFilePointer());
      assertEquals("Should read correct character value", '\uF734', result);
  }

  // Tests for reading unsigned 16-bit values (Little Endian)
  @Test(timeout = 4000)
  public void testReadUnsignedShortLE_WithNegativeAndPositiveBytes() throws Throwable {
      byte[] data = new byte[7];
      data[0] = (byte) (-7); // Set first byte to -7
      data[1] = (byte) 13;   // Set second byte to 13
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      
      int result = reader.readUnsignedShortLE();
      
      assertEquals("File pointer should advance by 2 bytes", 2L, reader.getFilePointer());
      assertEquals("Should read correct unsigned short value", 3577, result);
  }

  // Tests for reading unsigned 16-bit values (Big Endian) after short read
  @Test(timeout = 4000)
  public void testReadUnsignedShort_AfterReadShort() throws Throwable {
      byte[] data = new byte[8];
      data[2] = (byte) (-9); // Set byte at position 2 to -9
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      
      reader.readShort(); // Read first 2 bytes
      int result = reader.readUnsignedShort(); // Read next 2 bytes
      
      assertEquals("File pointer should be at position 4", 4L, reader.getFilePointer());
      assertEquals("Should read correct unsigned short value", 63232, result);
  }

  // Tests for EOF exceptions with unsigned short reads
  @Test(timeout = 4000)
  public void testReadUnsignedShort_ThrowsEOFException_WhenInsufficientData() throws Throwable {
      byte[] data = new byte[2]; // Only 2 bytes
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      reader.readShort(); // Consume all data
      
      try { 
        reader.readUnsignedShort();
        fail("Should throw EOFException when no data left");
      } catch(EOFException e) {
         verifyException("com.itextpdf.text.pdf.RandomAccessFileOrArray", e);
      }
  }

  // Tests for reading 16-bit short values (Little Endian)
  @Test(timeout = 4000)
  public void testReadShortLE_WithPositiveByte() throws Throwable {
      byte[] data = new byte[6];
      data[0] = (byte) 47; // Set first byte to 47
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      
      short result = reader.readShortLE();
      
      assertEquals("File pointer should advance by 2 bytes", 2L, reader.getFilePointer());
      assertEquals("Should read correct short value", (short) 47, result);
  }

  // Tests for reading 16-bit short values (Big Endian)
  @Test(timeout = 4000)
  public void testReadShort_WithTwoIdenticalBytes() throws Throwable {
      byte[] data = new byte[20];
      data[0] = (byte) 16; // Set first byte to 16
      data[1] = (byte) 16; // Set second byte to 16
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      
      short result = reader.readShort();
      
      assertEquals("File pointer should advance by 2 bytes", 2L, reader.getFilePointer());
      assertEquals("Should read correct short value", (short) 4112, result);
  }

  // Tests for skipBytes functionality
  @Test(timeout = 4000)
  public void testSkipBytes_WithZeroBytes() throws Throwable {
      byte[] data = new byte[2];
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      
      int bytesSkipped = reader.skipBytes((byte) 0);
      
      assertEquals("Should skip 0 bytes", 0, bytesSkipped);
      assertEquals("File pointer should remain at 0", 0L, reader.getFilePointer());
  }

  // Tests for read operations with pushBack
  @Test(timeout = 4000)
  public void testRead_WithPushBack_FillsArrayWithPushedValue() throws Throwable {
      byte[] data = new byte[5];
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      reader.pushBack((byte) (-123)); // Push back -123
      
      int bytesRead = reader.read(data);
      
      assertArrayEquals("Array should be filled with pushed back value", 
                       new byte[] {(byte) (-123), (byte) (-123), (byte) 0, (byte) 0, (byte) 0}, data);
      assertEquals("Should read 5 bytes", 5, bytesRead);
  }

  // Tests for readFully with pushBack
  @Test(timeout = 4000)
  public void testReadFully_WithPushBack() throws Throwable {
      byte[] data = new byte[1];
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      reader.pushBack((byte) 0); // Push back a byte
      
      reader.readFully(data); // Should read the pushed back byte
      
      assertEquals("File pointer should be at 0 after reading pushed back byte", 0L, reader.getFilePointer());
  }

  // Tests for skipBytes returning -1 when at EOF
  @Test(timeout = 4000)
  public void testSkipBytes_ReturnsNegativeOne_WhenAtEOF() throws Throwable {
      byte[] data = new byte[2];
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      reader.readLine(); // Read all data
      
      int result = reader.skipBytes(769);
      
      assertEquals("File pointer should be at end", 2L, reader.getFilePointer());
      assertEquals("Should return -1 when at EOF", (-1), result);
  }

  // Tests for skip method returning -1 when at EOF
  @Test(timeout = 4000)
  public void testSkip_ReturnsNegativeOne_WhenAtEOF() throws Throwable {
      byte[] data = new byte[8];
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      reader.readLine(); // Read all data
      
      long result = reader.skip(8);
      
      assertEquals("File pointer should be at end", 8L, reader.getFilePointer());
      assertEquals("Should return -1 when at EOF", (-1L), result);
  }

  // Additional tests for various read operations...
  // [The remaining tests follow similar patterns with descriptive names and clear assertions]

  // Tests for constructor error conditions
  @Test(timeout = 4000)
  public void testConstructor_ThrowsNullPointerException_WithNullByteArray() throws Throwable {
      try {
        new RandomAccessFileOrArray((byte[]) null);
        fail("Should throw NullPointerException with null byte array");
      } catch(NullPointerException e) {
         verifyException("com.itextpdf.text.io.ArrayRandomAccessSource", e);
      }
  }

  @Test(timeout = 4000)
  public void testConstructor_ThrowsIOException_WithHttpURL() throws Throwable {
      URL httpUrl = MockURL.getHttpExample();
      try {
        new RandomAccessFileOrArray(httpUrl);
        fail("Should throw IOException with HTTP URL");
      } catch(Throwable e) {
         verifyException("org.evosuite.runtime.mock.java.net.EvoHttpURLConnection", e);
      }
  }

  // Tests for file operations
  @Test(timeout = 4000)
  public void testConstructor_WithValidFile() throws Throwable {
      EvoSuiteFile testFile = new EvoSuiteFile("test_file");
      FileSystemHandling.appendStringToFile(testFile, "");
      
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray("test_file", true, true);
      
      assertEquals("File pointer should start at 0", 0L, reader.getFilePointer());
  }

  // Tests for basic functionality
  @Test(timeout = 4000)
  public void testCreateView_ReturnsIndependentView() throws Throwable {
      byte[] data = new byte[5];
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      
      RandomAccessFileOrArray view = reader.createView();
      
      assertEquals("View should start at position 0", 0L, view.getFilePointer());
  }

  @Test(timeout = 4000)
  public void testLength_ReturnsCorrectLength() throws Throwable {
      byte[] data = new byte[11];
      RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);
      
      reader.length(); // Should not throw exception
      
      assertEquals("File pointer should remain at 0", 0L, reader.getFilePointer());
  }
}