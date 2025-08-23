package com.google.gson.internal.bind.util;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for ISO8601Utils.
 *
 * Notes:
 * - Tests set the default timezone to UTC for determinism across environments.
 * - Helper methods and constants avoid duplication and magic numbers.
 */
public class ISO8601UtilsTest {

  // Common time zones used in tests
  private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
  // Historical/legacy ID used intentionally to match existing expectations; resolves to -03:00
  private static final TimeZone SAO_PAULO_TZ = TimeZone.getTimeZone("Brazil/East");

  // 2018-06-28T18:06:16.870Z
  private static final long SAMPLE_INSTANT_MS = 1530209176870L;

  private TimeZone originalDefaultTimeZone;

  @Before
  public void setUp() {
    // Make tests deterministic regardless of machine default TZ
    originalDefaultTimeZone = TimeZone.getDefault();
    TimeZone.setDefault(UTC);
  }

  @After
  public void tearDown() {
    TimeZone.setDefault(originalDefaultTimeZone);
  }

  private static GregorianCalendar newUtcCalendar() {
    GregorianCalendar cal = new GregorianCalendar(UTC, Locale.US);
    cal.clear();
    return cal;
  }

  private static Date parseAtStart(String iso8601) throws ParseException {
    return ISO8601Utils.parse(iso8601, new ParsePosition(0));
  }

  @Test
  public void format_usesUtcAndNoMillis_byDefault() {
    GregorianCalendar cal = new GregorianCalendar(UTC, Locale.US);
    cal.clear();
    cal.set(2018, Calendar.JUNE, 25); // Midnight UTC
    Date date = cal.getTime();

    String formatted = ISO8601Utils.format(date);

    assertThat(formatted).isEqualTo("2018-06-25T00:00:00Z");
  }

  @Test
  @SuppressWarnings("JavaUtilDate")
  public void format_includesMillis_whenRequested() {
    Date date = new Date(SAMPLE_INSTANT_MS);

    String formatted = ISO8601Utils.format(date, true);

    assertThat(formatted).isEqualTo("2018-06-28T18:06:16.870Z");
  }

  @Test
  @SuppressWarnings("JavaUtilDate")
  public void format_usesProvidedTimezone() {
    Date date = new Date(SAMPLE_INSTANT_MS);

    String formatted = ISO8601Utils.format(date, true, SAO_PAULO_TZ);

    assertThat(formatted).isEqualTo("2018-06-28T15:06:16.870-03:00");
  }

  @Test
  public void parse_dateOnly_usesDefaultTimezone() throws ParseException {
    // Default TZ is forced to UTC in setUp()
    Date parsed = parseAtStart("2018-06-25");

    // Expected is midnight in the default timezone (UTC)
    Date expected = new GregorianCalendar(2018, Calendar.JUNE, 25).getTime();

    assertThat(parsed).isEqualTo(expected);
  }

  @Test
  public void parse_rejectsInvalidDay() {
    assertThrows(ParseException.class, () -> parseAtStart("2022-12-33"));
  }

  @Test
  public void parse_rejectsInvalidMonth() {
    assertThrows(ParseException.class, () -> parseAtStart("2022-14-30"));
  }

  @Test
  public void parse_datetimeWithOffset_isConvertedToUtc() throws ParseException {
    // 00:00 at -03:00 equals 03:00 UTC on the same date
    Date parsed = parseAtStart("2018-06-25T00:00:00-03:00");

    GregorianCalendar expectedCal = newUtcCalendar();
    expectedCal.set(2018, Calendar.JUNE, 25, 3, 0, 0);
    expectedCal.set(Calendar.MILLISECOND, 0);
    Date expected = expectedCal.getTime();

    assertThat(parsed).isEqualTo(expected);
  }

  @Test
  public void parse_datetimeWithNonStandardOffset_isHandled() throws ParseException {
    // -02:58 is unusual; 00:02 at -02:58 still equals 03:00 UTC
    Date parsed = parseAtStart("2018-06-25T00:02:00-02:58");

    GregorianCalendar expectedCal = newUtcCalendar();
    expectedCal.set(2018, Calendar.JUNE, 25, 3, 0, 0);
    expectedCal.set(Calendar.MILLISECOND, 0);
    Date expected = expectedCal.getTime();

    assertThat(parsed).isEqualTo(expected);
  }

  @Test
  public void parse_rejectsInvalidTime() {
    assertThrows(ParseException.class, () -> parseAtStart("2018-06-25T61:60:62-03:00"));
  }
}