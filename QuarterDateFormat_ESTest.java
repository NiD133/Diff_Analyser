package org.jfree.chart.axis;

import static org.junit.Assert.*;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Readable, behavior-focused tests for QuarterDateFormat.
 * 
 * Notes:
 * - Uses plain JUnit (no EvoSuite runner/mocks/scaffolding).
 * - Uses fixed time zones/dates for deterministic behavior.
 * - Verifies core behaviors: formatting order, constructor validation,
 *   equals contract, and parse stub behavior.
 */
public class QuarterDateFormatTest {

  // Helpers

  private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

  private static Date date(int year, int monthZeroBased, int dayOfMonth, TimeZone zone) {
    Calendar cal = new GregorianCalendar(zone);
    cal.clear();
    cal.set(year, monthZeroBased, dayOfMonth, 12, 0, 0); // mid-day to avoid DST edges
    return cal.getTime();
  }

  // Formatting

  @Test
  public void formatsYearThenRegularQuarter_byDefault() {
    // 2004-02-15 is in Q1
    QuarterDateFormat f = new QuarterDateFormat(UTC);
    Date d = date(2004, Calendar.FEBRUARY, 15, UTC);

    StringBuffer out = new StringBuffer();
    f.format(d, out, new FieldPosition(0));

    assertEquals("2004 1", out.toString());
  }

  @Test
  public void formatsRomanQuarterFirst_whenConfigured() {
    // 2004-10-01 is in Q4
    QuarterDateFormat f = new QuarterDateFormat(UTC, QuarterDateFormat.ROMAN_QUARTERS, true);
    Date d = date(2004, Calendar.OCTOBER, 1, UTC);

    StringBuffer out = new StringBuffer("prefix ");
    f.format(d, out, new FieldPosition(0));

    assertEquals("prefix IV 2004", out.toString());
  }

  @Test
  public void format_throwsNullPointer_whenBufferIsNull() {
    QuarterDateFormat f = new QuarterDateFormat(UTC);
    Date d = date(2020, Calendar.JANUARY, 1, UTC);

    try {
      f.format(d, null, new FieldPosition(0));
      fail("Expected NullPointerException");
    } catch (NullPointerException expected) {
      // expected
    }
  }

  @Test
  public void format_throwsOutOfBounds_whenTooFewQuarterSymbols() {
    // Provide an empty symbol array; any format call will try to index into it
    String[] tooFewSymbols = new String[0];
    QuarterDateFormat f = new QuarterDateFormat(UTC, tooFewSymbols, false);

    try {
      f.format(date(2023, Calendar.JULY, 1, UTC), new StringBuffer(), new FieldPosition(0));
      fail("Expected ArrayIndexOutOfBoundsException");
    } catch (ArrayIndexOutOfBoundsException expected) {
      // expected
    }
  }

  // Parsing

  @Test
  public void parse_alwaysReturnsNull_evenWithNullParsePosition() {
    QuarterDateFormat f = new QuarterDateFormat(UTC);

    assertNull(f.parse("anything", (ParsePosition) null));
    assertNull(f.parse("anything", new ParsePosition(0)));
  }

  // Equals

  @Test
  public void equals_reflexive_and_typeSafety() {
    QuarterDateFormat f = new QuarterDateFormat(UTC);

    assertTrue(f.equals(f));             // reflexive
    assertFalse(f.equals(null));         // against null
    assertFalse(f.equals("not-a-date"));// against different type
  }

  @Test
  public void equals_true_whenSameZoneSymbolsAndOrder() {
    QuarterDateFormat a = new QuarterDateFormat(UTC);
    QuarterDateFormat b = new QuarterDateFormat(UTC);

    assertTrue(a.equals(b));
    assertTrue(b.equals(a));
  }

  @Test
  public void equals_false_whenQuarterFirstDiffers() {
    QuarterDateFormat a = new QuarterDateFormat(UTC, QuarterDateFormat.ROMAN_QUARTERS, false);
    QuarterDateFormat b = new QuarterDateFormat(UTC, QuarterDateFormat.ROMAN_QUARTERS, true);

    assertFalse(a.equals(b));
    assertFalse(b.equals(a));
  }

  @Test
  public void equals_false_whenQuarterSymbolsDiffer() {
    QuarterDateFormat a = new QuarterDateFormat(UTC, QuarterDateFormat.REGULAR_QUARTERS, false);
    QuarterDateFormat b = new QuarterDateFormat(UTC, QuarterDateFormat.ROMAN_QUARTERS, false);

    assertFalse(a.equals(b));
    assertFalse(b.equals(a));
  }

  @Test
  public void equals_mayThrowNPE_whenNumberFormatIsNull() {
    // DateFormat expects a non-null NumberFormat; setting it to null can cause NPE in equals()
    QuarterDateFormat a = new QuarterDateFormat(UTC);
    a.setNumberFormat((NumberFormat) null);
    QuarterDateFormat b = new QuarterDateFormat(UTC);

    try {
      a.equals(b);
      fail("Expected NullPointerException due to null numberFormat");
    } catch (NullPointerException expected) {
      // expected
    }
  }

  // Constructor validation

  @Test
  public void constructor_throws_whenZoneIsNull_singleArg() {
    try {
      new QuarterDateFormat(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Null 'zone' argument."));
    }
  }

  @Test
  public void constructor_throws_whenZoneIsNull_twoArgs() {
    try {
      new QuarterDateFormat(null, QuarterDateFormat.REGULAR_QUARTERS);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Null 'zone' argument."));
    }
  }

  @Test
  public void constructor_throws_whenZoneIsNull_threeArgs() {
    try {
      new QuarterDateFormat(null, QuarterDateFormat.REGULAR_QUARTERS, true);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Null 'zone' argument."));
    }
  }
}