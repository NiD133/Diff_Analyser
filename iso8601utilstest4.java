package com.google.gson.internal.bind.util;

import static com.google.common.truth.Truth.assertThat;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.Test;

/**
 * Tests for {@link ISO8601Utils}, focusing on date parsing behavior.
 */
public class ISO8601UtilsTest {

  @Test
  public void parse_dateOnlyString_usesDefaultTimezone() throws ParseException {
    // Arrange
    String dateString = "2018-06-25";

    // The ISO8601Utils.parse method should interpret a date-only string
    // (with no time or timezone info) as midnight in the default system timezone.
    // To verify this, we create an expected Date object for the same date,
    // which will also be set to midnight in the default timezone.
    // Note: Calendar months are 0-indexed, so Calendar.JUNE represents the 6th month.
    Date expectedDate = new GregorianCalendar(2018, Calendar.JUNE, 25).getTime();

    // Act
    Date actualDate = ISO8601Utils.parse(dateString, new ParsePosition(0));

    // Assert
    assertThat(actualDate).isEqualTo(expectedDate);
  }
}