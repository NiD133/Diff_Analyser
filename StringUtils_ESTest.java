package com.itextpdf.text.pdf;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable unit tests for StringUtils.
 *
 * Goals:
 * - Use clear, intention-revealing test names.
 * - Avoid EvoSuite-specific scaffolding/runners.
 * - Prefer Arrange/Act/Assert structure and focused assertions.
 * - Cover happy paths and key edge cases (nulls, empties, control characters).
 */
public class StringUtilsTest {

  // ---------------------------
  // convertCharsToBytes
  // ---------------------------

  @Test
  public void convertCharsToBytes_null_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> StringUtils.convertCharsToBytes(null));
  }

  @Test
  public void convertCharsToBytes_empty_returnsEmptyArray() {
    char[] input = new char[0];

    byte[] result = StringUtils.convertCharsToBytes(input);

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  @Test
  public void convertCharsToBytes_returnsArrayTwiceTheLength() {
    // 8 chars -> 16 bytes
    char[] input = new char[8];
    input[0] = 'h'; // value not important for this size assertion

    byte[] result = StringUtils.convertCharsToBytes(input);

    assertEquals(16, result.length);
  }

  @Test
  public void convertCharsToBytes_usesBigEndianOrder() {
    // 'A' = 0x0041 -> [0x00, 0x41]
    byte[] resultA = StringUtils.convertCharsToBytes(new char[] { 'A' });
    assertArrayEquals(new byte[] { 0x00, 0x41 }, resultA);

    // Euro sign U+20AC -> [0x20, 0xAC]
    byte[] resultEuro = StringUtils.convertCharsToBytes(new char[] { '\u20AC' });
    assertArrayEquals(new byte[] { 0x20, (byte) 0xAC }, resultEuro);
  }

  // ---------------------------
  // escapeString (byte[] -> byte[])
  // ---------------------------

  @Test
  public void escapeString_nullBytes_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> StringUtils.escapeString(null));
  }

  @Test
  public void escapeString_emptyBytes_returnsParenthesizedEmptyString() {
    byte[] input = new byte[0];

    byte[] escaped = StringUtils.escapeString(input);

    // Expect just "(" and ")" around empty content.
    assertArrayEquals(new byte[] { (byte) '(', (byte) ')' }, escaped);
  }

  @Test
  public void escapeString_controlTab_isEscapedAndParenthesized() {
    // Input: [0, 9, 0, 0]
    byte[] input = new byte[4];
    input[1] = 0x09; // TAB

    byte[] escaped = StringUtils.escapeString(input);

    // Expected:
    // '(' 40
    // 0
    // '\' 92
    // 't' 116 (TAB escape)
    // 0
    // 0
    // ')' 41
    assertArrayEquals(new byte[] { 40, 0, 92, 116, 0, 0, 41 }, escaped);
  }

  // ---------------------------
  // escapeString (byte[], ByteBuffer)
  // ---------------------------

  @Test
  public void escapeString_withNullContentBuffer_throwsNullPointerException() {
    byte[] input = new byte[3];

    assertThrows(NullPointerException.class, () -> StringUtils.escapeString(input, null));
  }

  @Test
  public void escapeString_writesParenthesizedContentToProvidedBuffer() {
    // Single control char (backspace = 8) should be escaped with a backslash.
    byte[] input = new byte[8];
    input[3] = 0x08; // backspace

    ByteBuffer buffer = new ByteBuffer();

    StringUtils.escapeString(input, buffer);

    // 8 bytes + 1 (escape) + 2 (parentheses) = 11
    assertEquals(11, buffer.size());
  }

  @Test
  public void escapeString_formFeed_isEscaped_sizeMatches() {
    byte[] input = new byte[6];
    input[4] = 0x0C; // form feed

    // ByteBuffer(byte) is a capacity hint in iText, not initial content.
    ByteBuffer buffer = new ByteBuffer((byte) 0xDD);

    StringUtils.escapeString(input, buffer);

    // 6 bytes + 1 (escape) + 2 (parentheses) = 9
    assertEquals(9, buffer.size());
  }

  @Test
  public void escapeString_carriageReturn_isEscaped_sizeMatches() {
    byte[] input = new byte[5];
    input[2] = 0x0D; // carriage return

    ByteBuffer buffer = new ByteBuffer();

    StringUtils.escapeString(input, buffer);

    // 5 bytes + 1 (escape) + 2 (parentheses) = 8
    assertEquals(8, buffer.size());
  }

  @Test
  public void escapeString_lineFeed_isEscaped_sizeMatches() {
    byte[] input = new byte[7];
    input[5] = 0x0A; // line feed

    ByteBuffer buffer = new ByteBuffer((byte) 10);

    StringUtils.escapeString(input, buffer);

    // 7 bytes + 1 (escape) + 2 (parentheses) = 10
    assertEquals(10, buffer.size());
  }

  @Test
  public void escapeString_tab_isEscaped_sizeMatches() {
    byte[] input = new byte[4];
    input[1] = 0x09; // tab

    ByteBuffer buffer = new ByteBuffer(71); // capacity hint

    StringUtils.escapeString(input, buffer);

    // 4 bytes + 1 (escape) + 2 (parentheses) = 7
    assertEquals(7, buffer.size());
  }

  @Test
  public void escapeString_escapingAlreadyEscapedString_addsParensAndEscapesBackslashAndParens() {
    // First, escape the raw input which contains a tab.
    byte[] raw = new byte[4];
    raw[1] = 0x09; // tab

    byte[] onceEscaped = StringUtils.escapeString(raw);

    // Sanity check the once-escaped result is what we expect.
    assertArrayEquals(new byte[] { 40, 0, 92, 116, 0, 0, 41 }, onceEscaped);

    // Now escape the already-escaped bytes into a buffer.
    ByteBuffer buffer = new ByteBuffer(71);
    StringUtils.escapeString(onceEscaped, buffer);

    // Explanation:
    // - Adds parentheses (2).
    // - Escapes the existing '(' and ')' (adds 2).
    // - Escapes the existing backslash (adds 1).
    // Total: 7 (input) + 2 + 2 + 1 = 12
    assertEquals(12, buffer.size());
  }
}