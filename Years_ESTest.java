package org.joda.time;

import org.junit.Test;

import static org.junit.Assert.*;

public class YearsTest {

  // ---- Constants and basic accessors ----

  @Test
  public void zeroOneTwoThreeExposeExpectedValues() {
    assertEquals(0, Years.ZERO.getYears());
    assertEquals(1, Years.ONE.getYears());
    assertEquals(2, Years.TWO.getYears());
    assertEquals(3, Years.THREE.getYears());
  }

  @Test
  public void factoryCreatesExpectedValue() {
    assertEquals(0, Years.years(0).getYears());
    assertEquals(3, Years.years(3).getYears());
    assertEquals(Integer.MAX_VALUE, Years.years(Integer.MAX_VALUE).getYears());
    assertEquals(Integer.MIN_VALUE, Years.years(Integer.MIN_VALUE).getYears());
  }

  @Test
  public void fieldTypeAndPeriodTypeAreYears() {
    assertEquals("years", Years.ZERO.getFieldType().getName());
    assertEquals(PeriodType.years(), Years.ZERO.getPeriodType());
  }

  @Test
  public void toStringUsesISOFormat() {
    assertEquals("P2Y", Years.TWO.toString());
  }

  // ---- Arithmetic with ints ----

  @Test
  public void plusIntAddsYears() {
    Years y = Years.years(2).plus(5);
    assertEquals(7, y.getYears());
  }

  @Test
  public void minusIntSubtractsYears() {
    Years y = Years.years(10).minus(3);
    assertEquals(7, y.getYears());
  }

  @Test
  public void multipliedByScalesYears() {
    Years y = Years.years(-4).multipliedBy(3);
    assertEquals(-12, y.getYears());
  }

  @Test
  public void dividedByUsesIntegerDivision() {
    Years y = Years.years(3).dividedBy(2);
    assertEquals(1, y.getYears()); // 3 / 2 -> 1
  }

  @Test(expected = ArithmeticException.class)
  public void dividedByZeroThrows() {
    Years.ONE.dividedBy(0);
  }

  @Test(expected = ArithmeticException.class)
  public void plusIntOverflowThrows() {
    Years.MAX_VALUE.plus(1);
  }

  @Test(expected = ArithmeticException.class)
  public void multipliedByOverflowThrows() {
    Years.MAX_VALUE.multipliedBy(3);
  }

  // ---- Arithmetic with Years ----

  @Test
  public void plusYearsAddsValues() {
    Years base = Years.years(2);
    Years result = base.plus(Years.years(5));
    assertEquals(7, result.getYears());
  }

  @Test
  public void plusNullYearsTreatsAsZero() {
    Years base = Years.years(2);
    Years result = base.plus((Years) null);
    assertEquals(2, result.getYears());
  }

  @Test
  public void minusYearsSubtractsValues() {
    Years base = Years.years(2);
    Years result = base.minus(Years.years(5));
    assertEquals(-3, result.getYears());
  }

  @Test
  public void minusNullYearsTreatsAsZero() {
    Years base = Years.years(2);
    Years result = base.minus((Years) null);
    assertEquals(2, result.getYears());
  }

  @Test
  public void minusSameYearsYieldsZero() {
    Years y = Years.years(123);
    assertEquals(0, y.minus(y).getYears());
  }

  @Test(expected = ArithmeticException.class)
  public void minusMinValueByMinValueOverflows() {
    Years.MIN_VALUE.minus(Years.MIN_VALUE);
  }

  @Test
  public void negatedInvertsSign() {
    assertEquals(-2, Years.TWO.negated().getYears());
    assertEquals(0, Years.ZERO.negated().getYears());
  }

  @Test(expected = ArithmeticException.class)
  public void negatedMinValueThrows() {
    Years.MIN_VALUE.negated();
  }

  // ---- Comparisons ----

  @Test
  public void isGreaterThanAndLessThanAgainstValues() {
    assertTrue(Years.THREE.isGreaterThan(Years.years(-1)));
    assertFalse(Years.THREE.isLessThan(Years.years(-1)));
    assertFalse(Years.TWO.isGreaterThan(Years.years(2)));
    assertFalse(Years.TWO.isLessThan(Years.years(2)));
  }

  @Test
  public void comparisonsTreatNullAsZero() {
    assertTrue(Years.MAX_VALUE.isGreaterThan(null));
    assertFalse(Years.ZERO.isGreaterThan(null));
    assertTrue(Years.MIN_VALUE.isLessThan(null));
    assertFalse(Years.ZERO.isLessThan(null));
  }

  // ---- Between instants/partials and intervals ----

  @Test
  public void yearsBetweenSameInstantIsZero() {
    Instant epoch = Instant.EPOCH;
    Years between = Years.yearsBetween(epoch, epoch);
    assertEquals(0, between.getYears());
  }

  @Test
  public void yearsBetweenDifferentInstantsWholeYears() {
    DateTime start = new DateTime(2000, 1, 1, 0, 0, DateTimeZone.UTC);
    DateTime end = start.plusYears(5);
    Years between = Years.yearsBetween(start, end);
    assertEquals(5, between.getYears());
  }

  @Test(expected = IllegalArgumentException.class)
  public void yearsBetweenInstantsNullThrows() {
    Years.yearsBetween((ReadableInstant) null, (ReadableInstant) null);
  }

  @Test
  public void yearsBetweenLocalDatesWholeYears() {
    LocalDate start = new LocalDate(2010, 1, 1);
    LocalDate end = new LocalDate(2013, 1, 1);
    Years between = Years.yearsBetween(start, end);
    assertEquals(3, between.getYears());
  }

  @Test(expected = IllegalArgumentException.class)
  public void yearsBetweenPartialsNullThrows() {
    Years.yearsBetween((ReadablePartial) null, (ReadablePartial) null);
  }

  @Test
  public void yearsInNullIntervalIsZero() {
    assertEquals(0, Years.yearsIn(null).getYears());
  }

  @Test
  public void yearsInIntervalCountsWholeYears() {
    DateTime start = new DateTime(2000, 1, 1, 0, 0, DateTimeZone.UTC);
    DateTime end = start.plusYears(5);
    Interval interval = new Interval(start, end);
    assertEquals(5, Years.yearsIn(interval).getYears());
  }

  // ---- Parsing ----

  @Test
  public void parseNullReturnsZero() {
    assertEquals(0, Years.parseYears(null).getYears());
  }

  @Test
  public void parseValidIsoString() {
    assertEquals(2, Years.parseYears("P2Y").getYears());
    assertEquals(0, Years.parseYears("P0Y").getYears());
  }

  @Test(expected = IllegalArgumentException.class)
  public void parseEmptyStringThrows() {
    Years.parseYears("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void parseWithNonZeroNonYearComponentThrows() {
    Years.parseYears("P1M"); // months must be zero
  }
}