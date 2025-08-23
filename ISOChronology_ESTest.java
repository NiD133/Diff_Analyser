package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, deterministic tests for ISOChronology.
 * These tests avoid relying on the process default time zone unless explicitly controlled.
 */
public class ISOChronologyTest {

  private DateTimeZone originalDefault;

  @Before
  public void saveDefaultTimeZone() {
    originalDefault = DateTimeZone.getDefault();
  }

  @After
  public void restoreDefaultTimeZone() {
    DateTimeZone.setDefault(originalDefault);
  }

  @Test
  public void withUTC_returnsSameSingletonInstance() {
    ISOChronology utc = ISOChronology.getInstanceUTC();
    assertSame(utc, utc.withUTC());
    assertSame(utc, ISOChronology.getInstanceUTC());
  }

  @Test
  public void equals_sameZone_true_and_hashCodesMatch() {
    DateTimeZone zone = DateTimeZone.forOffsetHours(2);
    ISOChronology a = ISOChronology.getInstance(zone);
    ISOChronology b = ISOChronology.getInstance(zone);

    assertTrue(a.equals(b));
    assertEquals(a.hashCode(), b.hashCode());
  }

  @Test
  public void equals_differentZone_false() {
    ISOChronology utc = ISOChronology.getInstanceUTC();
    ISOChronology plusThree = ISOChronology.getInstance(DateTimeZone.forOffsetHours(3));

    assertFalse(utc.equals(plusThree));
  }

  @Test
  public void equals_withNonChronologyObject_false() {
    assertFalse(ISOChronology.getInstanceUTC().equals(new Object()));
  }

  @Test
  public void withZone_changesZone_andEqualsFactoryInstance() {
    DateTimeZone zone = DateTimeZone.forOffsetHours(5);
    ISOChronology utc = ISOChronology.getInstanceUTC();

    Chronology withZone = utc.withZone(zone);

    assertEquals(ISOChronology.getInstance(zone), withZone);
    assertFalse(utc.equals(withZone));
  }

  @Test
  public void withZone_sameZone_returnsSameInstance() {
    DateTimeZone zone = DateTimeZone.forOffsetHours(-4);
    ISOChronology chrono = ISOChronology.getInstance(zone);

    assertSame(chrono, chrono.withZone(zone));
  }

  @Test
  public void withZone_null_whenDefaultZoneMatches_returnsSameInstance() {
    DateTimeZone desiredDefault = DateTimeZone.forOffsetHours(1);
    DateTimeZone.setDefault(desiredDefault);

    ISOChronology defaultChrono = ISOChronology.getInstance(); // bound to current default zone
    Chronology result = defaultChrono.withZone(null);

    assertSame(defaultChrono, result);
  }

  @Test
  public void toString_inUTC_isStable() {
    assertEquals("ISOChronology[UTC]", ISOChronology.getInstanceUTC().toString());
  }

  @Test
  public void assemble_withNonNullFields_doesNotThrow() {
    ISOChronology chrono = ISOChronology.getInstanceUTC();
    AssembledChronology.Fields fields = new AssembledChronology.Fields();

    // Should not throw
    chrono.assemble(fields);
  }

  @Test(expected = NullPointerException.class)
  public void assemble_withNullFields_throwsNullPointerException() {
    ISOChronology.getInstanceUTC().assemble(null);
  }
}