package com.google.gson.internal;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable tests for LazilyParsedNumber.
 *
 * These tests focus on:
 * - Returning the original string via toString()
 * - Converting valid numeric strings to primitive number types
 * - Throwing appropriate exceptions for invalid or null input
 * - Basic equals/hashCode behavior
 */
public class LazilyParsedNumberTest {

  // toString

  @Test
  public void toString_returnsOriginalValue() {
    LazilyParsedNumber n = new LazilyParsedNumber("123.45");
    assertEquals("123.45", n.toString());
  }

  @Test
  public void toString_handlesEmptyString() {
    LazilyParsedNumber n = new LazilyParsedNumber("");
    assertEquals("", n.toString());
  }

  @Test
  public void toString_returnsNullWhenConstructedWithNull() {
    LazilyParsedNumber n = new LazilyParsedNumber(null);
    assertNull(n.toString());
  }

  // Numeric conversions with valid input

  @Test
  public void parsesZeroAcrossTypes() {
    LazilyParsedNumber n = new LazilyParsedNumber("0");
    assertEquals(0, n.intValue());
    assertEquals(0L, n.longValue());
    assertEquals(0.0f, n.floatValue(), 0.0f);
    assertEquals(0.0, n.doubleValue(), 0.0);
  }

  @Test
  public void parsesPositiveIntegerAcrossTypes() {
    LazilyParsedNumber n = new LazilyParsedNumber("3");
    assertEquals(3, n.intValue());
    assertEquals(3L, n.longValue());
    assertEquals(3.0f, n.floatValue(), 0.0f);
    assertEquals(3.0, n.doubleValue(), 0.0);
  }

  @Test
  public void parsesNegativeIntegerAcrossTypes() {
    LazilyParsedNumber n = new LazilyParsedNumber("-6");
    assertEquals(-6, n.intValue());
    assertEquals(-6L, n.longValue());
    assertEquals(-6.0f, n.floatValue(), 0.0f);
    assertEquals(-6.0, n.doubleValue(), 0.0);
  }

  // Numeric conversions with invalid input

  @Test
  public void numericConversionsRejectNonNumericString() {
    LazilyParsedNumber n = new LazilyParsedNumber("not-a-number");

    assertThrows(NumberFormatException.class, n::intValue);
    assertThrows(NumberFormatException.class, n::longValue);
    assertThrows(NumberFormatException.class, n::floatValue);
    assertThrows(NumberFormatException.class, n::doubleValue);
  }

  @Test
  public void numericConversionsRejectMalformedNumber() {
    LazilyParsedNumber n = new LazilyParsedNumber("...");

    assertThrows(NumberFormatException.class, n::intValue);
    assertThrows(NumberFormatException.class, n::longValue);
    assertThrows(NumberFormatException.class, n::floatValue);
    assertThrows(NumberFormatException.class, n::doubleValue);
  }

  // Numeric conversions with null input

  @Test
  public void numericConversionsThrowNpeWhenConstructedWithNull() {
    LazilyParsedNumber n = new LazilyParsedNumber(null);

    assertThrows(NullPointerException.class, n::intValue);
    assertThrows(NullPointerException.class, n::longValue);
    assertThrows(NullPointerException.class, n::floatValue);
    assertThrows(NullPointerException.class, n::doubleValue);
  }

  // equals and hashCode

  @Test
  public void equals_isReflexive() {
    LazilyParsedNumber n = new LazilyParsedNumber("7");
    assertTrue(n.equals(n));
  }

  @Test
  public void equals_comparesUnderlyingStringValue() {
    LazilyParsedNumber a = new LazilyParsedNumber("42");
    LazilyParsedNumber b = new LazilyParsedNumber("42");

    assertTrue(a.equals(b));
    assertTrue(b.equals(a));
    assertEquals(a.hashCode(), b.hashCode());
  }

  @Test
  public void equals_returnsFalseWhenComparedWithDifferentType() {
    LazilyParsedNumber n = new LazilyParsedNumber("123");
    assertFalse(n.equals("123")); // different type
  }

  @Test
  public void equals_throwsNpeWhenBackingValueIsNull() {
    LazilyParsedNumber a = new LazilyParsedNumber(null);
    LazilyParsedNumber b = new LazilyParsedNumber("anything");

    assertThrows(NullPointerException.class, () -> a.equals(b));
  }

  @Test
  public void hashCode_throwsNpeWhenBackingValueIsNull() {
    LazilyParsedNumber n = new LazilyParsedNumber(null);
    assertThrows(NullPointerException.class, n::hashCode);
  }
}