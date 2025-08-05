/*
 *  Copyright 2001-2005 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.convert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;

/**
 * Test suite for CalendarConverter class.
 *
 * CalendarConverter is responsible for converting java.util.Calendar objects
 * to Joda-Time instants and partials, automatically selecting appropriate
 * chronologies based on the calendar type.
 *
 * @author Stephen Colebourne
 */
public class TestCalendarConverter extends TestCase {

    // Test data constants with descriptive names
    private static final DateTimeZone PARIS_TIMEZONE = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone MOSCOW_TIMEZONE = DateTimeZone.forID("Europe/Moscow");
    private static final long UNIX_EPOCH_PLUS_123_MILLIS = 123L;
    private static final long UNIX_EPOCH_MILLIS = 0L;
    private static final long TEST_TIMESTAMP_MILLIS = 12345678L;
    private static final int EXPECTED_MIN_DAYS_IN_FIRST_WEEK = 4;

    private static Chronology julianChronology;
    private static Chronology isoChronology;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestCalendarConverter.class);
    }

    public TestCalendarConverter(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        julianChronology = JulianChronology.getInstance();
        isoChronology = ISOChronology.getInstance();
    }

    //-----------------------------------------------------------------------
    // Singleton Pattern Tests
    //-----------------------------------------------------------------------

    /**
     * Verifies that CalendarConverter follows the singleton pattern correctly.
     * Tests class visibility, constructor access, and singleton instance field.
     */
    public void testCalendarConverterFollowsSingletonPattern() throws Exception {
        Class<?> converterClass = CalendarConverter.class;

        // Verify class has package-private visibility (not public, protected, or private)
        assertClassHasPackagePrivateVisibility(converterClass);

        // Verify single protected constructor exists
        assertHasSingleProtectedConstructor(converterClass);

        // Verify singleton instance field has package-private visibility
        assertSingletonInstanceHasPackagePrivateVisibility(converterClass);
    }

    private void assertClassHasPackagePrivateVisibility(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        assertEquals("Class should not be public", false, Modifier.isPublic(modifiers));
        assertEquals("Class should not be protected", false, Modifier.isProtected(modifiers));
        assertEquals("Class should not be private", false, Modifier.isPrivate(modifiers));
    }

    private void assertHasSingleProtectedConstructor(Class<?> clazz) throws Exception {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        assertEquals("Should have exactly one constructor", 1, constructors.length);

        Constructor<?> constructor = clazz.getDeclaredConstructor((Class[]) null);
        assertEquals("Constructor should be protected", true,
                Modifier.isProtected(constructor.getModifiers()));
    }

    private void assertSingletonInstanceHasPackagePrivateVisibility(Class<?> clazz) throws Exception {
        Field instanceField = clazz.getDeclaredField("INSTANCE");
        int modifiers = instanceField.getModifiers();
        assertEquals("INSTANCE field should not be public", false, Modifier.isPublic(modifiers));
        assertEquals("INSTANCE field should not be protected", false, Modifier.isProtected(modifiers));
        assertEquals("INSTANCE field should not be private", false, Modifier.isPrivate(modifiers));
    }

    //-----------------------------------------------------------------------
    // Basic Functionality Tests
    //-----------------------------------------------------------------------

    /**
     * Verifies that the converter reports Calendar.class as its supported type.
     */
    public void testGetSupportedTypeReturnsCalendarClass() throws Exception {
        assertEquals("Supported type should be Calendar.class",
                Calendar.class, CalendarConverter.INSTANCE.getSupportedType());
    }

    /**
     * Tests extraction of millisecond timestamp from Calendar objects.
     * Verifies that the original calendar is not modified during conversion.
     */
    public void testGetInstantMillisExtractsTimestampFromCalendar() throws Exception {
        GregorianCalendar testCalendar = createCalendarWithTimestamp(UNIX_EPOCH_PLUS_123_MILLIS);

        long extractedMillis = CalendarConverter.INSTANCE.getInstantMillis(testCalendar, julianChronology);

        assertEquals("Should extract correct timestamp", UNIX_EPOCH_PLUS_123_MILLIS, extractedMillis);
        assertEquals("Original calendar should remain unchanged",
                UNIX_EPOCH_PLUS_123_MILLIS, testCalendar.getTime().getTime());
    }

    //-----------------------------------------------------------------------
    // Chronology Selection Tests
    //-----------------------------------------------------------------------

    /**
     * Tests chronology selection when a specific DateTimeZone is provided.
     * Verifies correct chronology types are selected based on calendar type and configuration.
     */
    public void testGetChronologyWithSpecificTimeZone() throws Exception {
        // Test 1: GregorianCalendar with different timezone - should use GJ chronology with specified zone
        GregorianCalendar parisCalendar = createGregorianCalendarInTimeZone("Europe/Paris");
        assertEquals("GregorianCalendar should use GJ chronology with specified timezone",
                GJChronology.getInstance(MOSCOW_TIMEZONE),
                CalendarConverter.INSTANCE.getChronology(parisCalendar, MOSCOW_TIMEZONE));

        // Test 2: Default behavior with null zone - should use calendar's timezone
        GregorianCalendar moscowCalendar = createGregorianCalendarInTimeZone("Europe/Moscow");
        assertEquals("Null zone should use calendar's timezone with GJ chronology",
                GJChronology.getInstance(),
                CalendarConverter.INSTANCE.getChronology(moscowCalendar, (DateTimeZone) null));

        // Test 3: Gregorian change at epoch - should use GJ chronology with cutover
        GregorianCalendar epochCutoverCalendar = createGregorianCalendarWithCutover("Europe/Moscow", UNIX_EPOCH_MILLIS);
        assertEquals("Epoch cutover should use GJ chronology with specific parameters",
                GJChronology.getInstance(MOSCOW_TIMEZONE, UNIX_EPOCH_MILLIS, EXPECTED_MIN_DAYS_IN_FIRST_WEEK),
                CalendarConverter.INSTANCE.getChronology(epochCutoverCalendar, MOSCOW_TIMEZONE));

        // Test 4: Very late Gregorian change - should use Julian chronology
        GregorianCalendar lateChangeCalendar = createGregorianCalendarWithCutover("Europe/Moscow", Long.MAX_VALUE);
        assertEquals("Very late Gregorian change should use Julian chronology",
                JulianChronology.getInstance(PARIS_TIMEZONE),
                CalendarConverter.INSTANCE.getChronology(lateChangeCalendar, PARIS_TIMEZONE));

        // Test 5: Very early Gregorian change - should use Gregorian chronology
        GregorianCalendar earlyChangeCalendar = createGregorianCalendarWithCutover("Europe/Moscow", Long.MIN_VALUE);
        assertEquals("Very early Gregorian change should use Gregorian chronology",
                GregorianChronology.getInstance(PARIS_TIMEZONE),
                CalendarConverter.INSTANCE.getChronology(earlyChangeCalendar, PARIS_TIMEZONE));

        // Test 6: Unknown calendar type - should default to ISO chronology
        Calendar unknownCalendar = new MockUnknownCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        assertEquals("Unknown calendar type should use ISO chronology",
                ISOChronology.getInstance(PARIS_TIMEZONE),
                CalendarConverter.INSTANCE.getChronology(unknownCalendar, PARIS_TIMEZONE));

        // Test 7: Buddhist calendar - should use Buddhist chronology (if available)
        testBuddhistCalendarChronologySelection();
    }

    /**
     * Tests chronology selection when no chronology is specified (null chronology parameter).
     * Should infer chronology from calendar type and use calendar's own timezone.
     */
    public void testGetChronologyWithNullChronologyInfersFromCalendar() throws Exception {
        // Test 1: Standard GregorianCalendar - should use GJ chronology with calendar's timezone
        GregorianCalendar parisCalendar = createGregorianCalendarInTimeZone("Europe/Paris");
        assertEquals("Should infer GJ chronology from GregorianCalendar using its timezone",
                GJChronology.getInstance(PARIS_TIMEZONE),
                CalendarConverter.INSTANCE.getChronology(parisCalendar, (Chronology) null));

        // Test 2: GregorianCalendar with epoch cutover
        GregorianCalendar epochCutoverCalendar = createGregorianCalendarWithCutover("Europe/Moscow", UNIX_EPOCH_MILLIS);
        assertEquals("Should handle epoch cutover with calendar's timezone",
                GJChronology.getInstance(MOSCOW_TIMEZONE, UNIX_EPOCH_MILLIS, EXPECTED_MIN_DAYS_IN_FIRST_WEEK),
                CalendarConverter.INSTANCE.getChronology(epochCutoverCalendar, (Chronology) null));

        // Test 3: Late Gregorian change - should use Julian with calendar's timezone
        GregorianCalendar lateChangeCalendar = createGregorianCalendarWithCutover("Europe/Moscow", Long.MAX_VALUE);
        assertEquals("Late Gregorian change should use Julian with calendar's timezone",
                JulianChronology.getInstance(MOSCOW_TIMEZONE),
                CalendarConverter.INSTANCE.getChronology(lateChangeCalendar, (Chronology) null));

        // Test 4: Early Gregorian change - should use Gregorian with calendar's timezone
        GregorianCalendar earlyChangeCalendar = createGregorianCalendarWithCutover("Europe/Moscow", Long.MIN_VALUE);
        assertEquals("Early Gregorian change should use Gregorian with calendar's timezone",
                GregorianChronology.getInstance(MOSCOW_TIMEZONE),
                CalendarConverter.INSTANCE.getChronology(earlyChangeCalendar, (Chronology) null));

        // Test 5: Unknown timezone - should use default
        GregorianCalendar unknownTimezoneCalendar = new GregorianCalendar(new MockUnknownTimeZone());
        assertEquals("Unknown timezone should use default GJ chronology",
                GJChronology.getInstance(),
                CalendarConverter.INSTANCE.getChronology(unknownTimezoneCalendar, (Chronology) null));

        // Test 6: Unknown calendar type - should use ISO with calendar's timezone
        Calendar unknownCalendar = new MockUnknownCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        assertEquals("Unknown calendar should use ISO with calendar's timezone",
                ISOChronology.getInstance(MOSCOW_TIMEZONE),
                CalendarConverter.INSTANCE.getChronology(unknownCalendar, (Chronology) null));

        // Test 7: Buddhist calendar support
        testBuddhistCalendarWithNullChronology();
    }

    /**
     * Tests that when a specific chronology is provided, it overrides calendar-based inference.
     */
    public void testGetChronologyWithSpecificChronologyOverridesCalendarType() throws Exception {
        GregorianCalendar parisCalendar = createGregorianCalendarInTimeZone("Europe/Paris");

        Chronology result = CalendarConverter.INSTANCE.getChronology(parisCalendar, julianChronology);

        assertEquals("Specific chronology should override calendar type inference",
                julianChronology, result);
    }

    //-----------------------------------------------------------------------
    // Partial Values Extraction Tests
    //-----------------------------------------------------------------------

    /**
     * Tests extraction of partial values from Calendar for specific time fields.
     * Verifies that the conversion produces the same field values as direct chronology extraction.
     */
    public void testGetPartialValuesExtractsCorrectTimeFields() throws Exception {
        GregorianCalendar testCalendar = createCalendarWithTimestamp(TEST_TIMESTAMP_MILLIS);
        TimeOfDay timeOfDay = new TimeOfDay();

        int[] expectedValues = isoChronology.get(timeOfDay, TEST_TIMESTAMP_MILLIS);
        int[] actualValues = CalendarConverter.INSTANCE.getPartialValues(timeOfDay, testCalendar, isoChronology);

        assertEquals("Partial values should match direct chronology extraction",
                true, Arrays.equals(expectedValues, actualValues));
    }

    //-----------------------------------------------------------------------
    // String Representation Tests
    //-----------------------------------------------------------------------

    /**
     * Tests the string representation of the converter for debugging and logging purposes.
     */
    public void testToStringProvidesDescriptiveRepresentation() {
        String representation = CalendarConverter.INSTANCE.toString();

        assertEquals("String representation should identify supported type",
                "Converter[java.util.Calendar]", representation);
    }

    //-----------------------------------------------------------------------
    // Helper Methods
    //-----------------------------------------------------------------------

    /**
     * Creates a GregorianCalendar with the specified timestamp.
     */
    private GregorianCalendar createCalendarWithTimestamp(long timestampMillis) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(timestampMillis));
        return calendar;
    }

    /**
     * Creates a GregorianCalendar in the specified timezone.
     */
    private GregorianCalendar createGregorianCalendarInTimeZone(String timezoneId) {
        return new GregorianCalendar(TimeZone.getTimeZone(timezoneId));
    }

    /**
     * Creates a GregorianCalendar with a specific Gregorian cutover date.
     */
    private GregorianCalendar createGregorianCalendarWithCutover(String timezoneId, long cutoverMillis) {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone(timezoneId));
        calendar.setGregorianChange(new Date(cutoverMillis));
        return calendar;
    }

    /**
     * Tests Buddhist calendar chronology selection if the Buddhist calendar class is available.
     * This is JDK-specific functionality that may not be available on all platforms.
     */
    private void testBuddhistCalendarChronologySelection() {
        try {
            Calendar buddhistCalendar = (Calendar) Class.forName("sun.util.BuddhistCalendar").newInstance();
            buddhistCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

            assertEquals("Buddhist calendar should use Buddhist chronology",
                    BuddhistChronology.getInstance(PARIS_TIMEZONE),
                    CalendarConverter.INSTANCE.getChronology(buddhistCalendar, PARIS_TIMEZONE));
        } catch (ClassNotFoundException ex) {
            // Buddhist calendar not available - not a Sun/Oracle JDK
        } catch (IllegalAccessException ex) {
            // Access restricted - likely JDK 9+ with modules
        } catch (InstantiationException ex) {
            // Failed to create instance - skip test
        }
    }

    /**
     * Tests Buddhist calendar with null chronology parameter.
     */
    private void testBuddhistCalendarWithNullChronology() {
        try {
            Calendar buddhistCalendar = (Calendar) Class.forName("sun.util.BuddhistCalendar").newInstance();
            buddhistCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

            assertEquals("Buddhist calendar should use Buddhist chronology with calendar's timezone",
                    BuddhistChronology.getInstance(MOSCOW_TIMEZONE),
                    CalendarConverter.INSTANCE.getChronology(buddhistCalendar, (Chronology) null));
        } catch (ClassNotFoundException ex) {
            // Buddhist calendar not available - not a Sun/Oracle JDK
        } catch (IllegalAccessException ex) {
            // Access restricted - likely JDK 9+ with modules  
        } catch (InstantiationException ex) {
            // Failed to create instance - skip test
        }
    }
}