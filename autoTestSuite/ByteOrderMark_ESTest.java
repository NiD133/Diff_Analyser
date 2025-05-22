package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.io.ByteOrderMark;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true)
public class ByteOrderMark_ESTest extends ByteOrderMark_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test00() throws Throwable {
    // Test case 0: Checks if a given array of integers does NOT match the UTF-16BE byte order mark.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_16BE;
    int[] intArray0 = new int[2];
    intArray0[0] = (int) '\uFEFF'; // Assigning the unicode character for byte order mark to the first element.
    boolean matches = byteOrderMark0.matches(intArray0); // Checks if the array matches the UTF-16BE BOM.
    assertFalse(matches); // Expect the match to be false.
  }

  @Test(timeout = 4000)
  public void test01() throws Throwable {
    // Test case 1: Checks if UTF-16BE and UTF-16LE byte order marks are NOT equal.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_16BE;
    ByteOrderMark byteOrderMark1 = ByteOrderMark.UTF_16LE;
    boolean isEqual = byteOrderMark0.equals(byteOrderMark1); // Checks if the two BOMs are equal.
    assertFalse(isEqual); // Expect the equality check to be false.
    assertFalse(byteOrderMark1.equals((Object) byteOrderMark0)); // Verify the opposite direction.
  }

  @Test(timeout = 4000)
  public void test02() throws Throwable {
    // Test case 2: Checks if UTF-16LE and UTF-32LE byte order marks are NOT equal.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_16LE;
    ByteOrderMark byteOrderMark1 = ByteOrderMark.UTF_32LE;
    boolean isEqual = byteOrderMark0.equals(byteOrderMark1); // Checks if the two BOMs are equal.
    assertFalse(isEqual); // Expect the equality check to be false.
  }

  @Test(timeout = 4000)
  public void test03() throws Throwable {
    // Test case 3: Checks if a byte order mark is equal to itself (should be true).
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_16LE;
    boolean isEqual = byteOrderMark0.equals(byteOrderMark0); // Checks if the BOM is equal to itself.
    assertTrue(isEqual); // Expect the equality check to be true.
  }

  @Test(timeout = 4000)
  public void test04() throws Throwable {
    // Test case 4: Retrieves the integer value at index 2 of the UTF-32LE byte order mark.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_32LE;
    int value = byteOrderMark0.get(2); // Get the integer value at index 2.
    assertEquals(0, value); // Expect the value to be 0.
  }

  @Test(timeout = 4000)
  public void test05() throws Throwable {
    // Test case 5: Retrieves the integer value at index 1 of the UTF-32LE byte order mark.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_32LE;
    int value = byteOrderMark0.get(1); // Get the integer value at index 1.
    assertEquals(254, value); // Expect the value to be 254.
  }

  @Test(timeout = 4000)
  public void test06() throws Throwable {
    // Test case 6: Retrieves the integer value at index 0 of a custom byte order mark.
    int[] intArray0 = new int[4];
    intArray0[0] = (-8); // Assigns -8 to the first element of the integer array.
    ByteOrderMark byteOrderMark0 = new ByteOrderMark("+U", intArray0); // Creates a BOM with the given array.
    int value = byteOrderMark0.get(0); // Get the integer value at index 0.
    assertEquals((-8), value); // Expect the value to be -8.
  }

  @Test(timeout = 4000)
  public void test07() throws Throwable {
    // Test case 7: Tests that a NullPointerException is thrown when creating a ByteOrderMark with a null byte array.
    ByteOrderMark byteOrderMark0 = null;
    try {
      byteOrderMark0 = new ByteOrderMark("pLt'", (int[]) null);
      fail("Expecting exception: NullPointerException");
    } catch (NullPointerException e) {
      // Expected exception: The byte array cannot be null.
      verifyException("java.util.Objects", e);
    }
  }

  @Test(timeout = 4000)
  public void test08() throws Throwable {
    // Test case 8: Gets the length (number of bytes) of the UTF-16BE byte order mark.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_16BE;
    int length = byteOrderMark0.length(); // Gets the length of the BOM.
    assertEquals(2, length); // Expect the length to be 2.
  }

  @Test(timeout = 4000)
  public void test09() throws Throwable {
    // Test case 9: Tests that an ArrayIndexOutOfBoundsException is thrown when accessing an invalid index of a byte order mark.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_16BE;
    // Undeclared exception: Accessing index 2 of a UTF-16BE (length 2) should throw an exception.
    try {
      byteOrderMark0.get(2);
      fail("Expecting exception: ArrayIndexOutOfBoundsException");
    } catch (ArrayIndexOutOfBoundsException e) {
      // Expected exception: Index out of bounds.
      verifyException("org.apache.commons.io.ByteOrderMark", e);
    }
  }

  @Test(timeout = 4000)
  public void test10() throws Throwable {
    // Test case 10: Gets the string representation of the UTF-8 byte order mark.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_8;
    String stringRepresentation = byteOrderMark0.toString(); // Gets the string representation.
    assertEquals("ByteOrderMark[UTF-8: 0xEF,0xBB,0xBF]", stringRepresentation); // Verifies the format of the string.
  }

  @Test(timeout = 4000)
  public void test11() throws Throwable {
    // Test case 11: Checks if a given array of integers does NOT match the UTF-16LE byte order mark due to incorrect length.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_16LE;
    int[] intArray0 = new int[3]; // The array length is 3, but UTF-16LE has length 2.
    boolean matches = byteOrderMark0.matches(intArray0); // Checks if the array matches.
    assertFalse(matches); // Expect the match to be false.
  }

  @Test(timeout = 4000)
  public void test12() throws Throwable {
    // Test case 12: Checks if a given array of integers does NOT match the UTF-8 byte order mark due to incorrect length.
    int[] intArray0 = new int[1]; // The array length is 1, but UTF-8 has length 3.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_8;
    boolean matches = byteOrderMark0.matches(intArray0); // Checks if the array matches.
    assertFalse(matches); // Expect the match to be false.
  }

  @Test(timeout = 4000)
  public void test13() throws Throwable {
    // Test case 13: Checks if a null array of integers matches any byte order mark (should be false).
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_16BE;
    boolean matches = byteOrderMark0.matches((int[]) null); // Checks if the null array matches.
    assertFalse(matches); // Expect the match to be false.
  }

  @Test(timeout = 4000)
  public void test14() throws Throwable {
    // Test case 14: Checks if the raw bytes of the UTF-8 byte order mark match the same byte order mark.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_8;
    int[] rawBytes = byteOrderMark0.getRawBytes(); // Gets the raw bytes as an integer array.
    boolean matches = byteOrderMark0.matches(rawBytes); // Checks if the raw bytes match the BOM.
    assertTrue(matches); // Expect the match to be true.
  }

  @Test(timeout = 4000)
  public void test15() throws Throwable {
    // Test case 15: Generates the hash code for the UTF-8 byte order mark.  Primarily to ensure coverage and avoid warnings.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_8;
    byteOrderMark0.hashCode(); // Calls hashCode method.  No explicit assertion as the purpose is coverage.
  }

  @Test(timeout = 4000)
  public void test16() throws Throwable {
    // Test case 16: Gets the raw bytes of the UTF-8 byte order mark as a byte array.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_8;
    byte[] byteArray = byteOrderMark0.getBytes(); // Gets the raw bytes as a byte array.
    assertArrayEquals(new byte[] {(byte) (-17), (byte) (-69), (byte) (-65)}, byteArray); // Verifies the byte array content.
  }

  @Test(timeout = 4000)
  public void test17() throws Throwable {
    // Test case 17: Checks that UTF-16LE and UTF-16BE ByteOrderMarks are not equal.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_16LE;
    ByteOrderMark byteOrderMark1 = ByteOrderMark.UTF_16BE;
    boolean isEqual = byteOrderMark0.equals(byteOrderMark1); // Checks equality.
    assertFalse(isEqual); // Should be false.
    assertFalse(byteOrderMark1.equals((Object) byteOrderMark0)); // Verify the opposite direction.
  }

  @Test(timeout = 4000)
  public void test18() throws Throwable {
    // Test case 18: Checks that UTF-32BE and UTF-16BE ByteOrderMarks are not equal.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_32BE;
    ByteOrderMark byteOrderMark1 = ByteOrderMark.UTF_16BE;
    boolean isEqual = byteOrderMark0.equals(byteOrderMark1); // Checks equality.
    assertFalse(isEqual); // Should be false.
  }

  @Test(timeout = 4000)
  public void test19() throws Throwable {
    // Test case 19: Checks that a ByteOrderMark is not equal to a String.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_8;
    boolean isEqual = byteOrderMark0.equals("ByteOrderMark[UTF-8: 0xEF,0xBB,0xBF]"); // Compares with a string.
    assertFalse(isEqual); // Should be false.
  }

  @Test(timeout = 4000)
  public void test20() throws Throwable {
    // Test case 20: Tests that an IllegalArgumentException is thrown when creating a ByteOrderMark with an empty byte array.
    int[] intArray0 = new int[0];
    ByteOrderMark byteOrderMark0 = null;
    try {
      byteOrderMark0 = new ByteOrderMark("N%W{9DrL", intArray0);
      fail("Expecting exception: IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected exception:  "No bytes specified"
      verifyException("org.apache.commons.io.ByteOrderMark", e);
    }
  }

  @Test(timeout = 4000)
  public void test21() throws Throwable {
    // Test case 21: Tests that an IllegalArgumentException is thrown when creating a ByteOrderMark with an empty charset name.
    int[] intArray0 = new int[7];
    ByteOrderMark byteOrderMark0 = null;
    try {
      byteOrderMark0 = new ByteOrderMark("", intArray0);
      fail("Expecting exception: IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected exception: "No charsetName specified"
      verifyException("org.apache.commons.io.ByteOrderMark", e);
    }
  }

  @Test(timeout = 4000)
  public void test22() throws Throwable {
    // Test case 22: Creates a ByteOrderMark using the raw bytes of another ByteOrderMark and checks if they match.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_8;
    int[] intArray0 = byteOrderMark0.getRawBytes(); // Get raw bytes.
    ByteOrderMark byteOrderMark1 = new ByteOrderMark("ByteOrderMark[UTF-8: 0xEF,0xBB,0xBF]", intArray0); // Create a new BOM with the raw bytes
    boolean matches = byteOrderMark1.matches(intArray0); // Check if the raw bytes match the new BOM.
    assertTrue(matches); // Should be true.
  }

  @Test(timeout = 4000)
  public void test23() throws Throwable {
    // Test case 23: Gets the charset name of the UTF-16BE ByteOrderMark.
    ByteOrderMark byteOrderMark0 = ByteOrderMark.UTF_16BE;
    String charsetName = byteOrderMark0.getCharsetName(); // Gets the charset name.
    assertEquals("UTF-16BE", charsetName); // Verify the expected charset name.
  }
}