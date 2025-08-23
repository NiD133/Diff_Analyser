package org.joda.time.chrono;

import java.util.Locale;
import java.util.TimeZone;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.joda.time.DateTime.Property;

public class IslamicChronologyTestTest14 extends TestCase {

    private static long SKIP = 1 * DateTimeConstants.MILLIS_PER_DAY;

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private static final Chronology ISLAMIC_UTC = IslamicChronology.getInstanceUTC();

    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();

    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365;

    // 2002-06-09
    private long TEST_TIME_NOW = (y2002days + 31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;

    private DateTimeZone originalDateTimeZone = null;

    private TimeZone originalTimeZone = null;

    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        SKIP = 1 * DateTimeConstants.MILLIS_PER_DAY;
        return new TestSuite(TestIslamicChronology.class);
    }

    @Override
    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    /**
     * Tests era, year, monthOfYear, dayOfMonth and dayOfWeek.
     */
    public void testCalendar() {
        if (TestAll.FAST) {
            return;
        }
        System.out.println("\nTestIslamicChronology.testCalendar");
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ISLAMIC_UTC);
        long millis = epoch.getMillis();
        long end = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC).getMillis();
        DateTimeField dayOfWeek = ISLAMIC_UTC.dayOfWeek();
        DateTimeField dayOfYear = ISLAMIC_UTC.dayOfYear();
        DateTimeField dayOfMonth = ISLAMIC_UTC.dayOfMonth();
        DateTimeField monthOfYear = ISLAMIC_UTC.monthOfYear();
        DateTimeField year = ISLAMIC_UTC.year();
        DateTimeField yearOfEra = ISLAMIC_UTC.yearOfEra();
        DateTimeField era = ISLAMIC_UTC.era();
        int expectedDOW = new DateTime(622, 7, 16, 0, 0, 0, 0, JULIAN_UTC).getDayOfWeek();
        int expectedDOY = 1;
        int expectedDay = 1;
        int expectedMonth = 1;
        int expectedYear = 1;
        while (millis < end) {
            int dowValue = dayOfWeek.get(millis);
            int doyValue = dayOfYear.get(millis);
            int dayValue = dayOfMonth.get(millis);
            int monthValue = monthOfYear.get(millis);
            int yearValue = year.get(millis);
            int yearOfEraValue = yearOfEra.get(millis);
            int dayOfYearLen = dayOfYear.getMaximumValue(millis);
            int monthLen = dayOfMonth.getMaximumValue(millis);
            if (monthValue < 1 || monthValue > 12) {
                fail("Bad month: " + millis);
            }
            // test era
            assertEquals(1, era.get(millis));
            assertEquals("AH", era.getAsText(millis));
            assertEquals("AH", era.getAsShortText(millis));
            // test date
            assertEquals(expectedDOY, doyValue);
            assertEquals(expectedMonth, monthValue);
            assertEquals(expectedDay, dayValue);
            assertEquals(expectedDOW, dowValue);
            assertEquals(expectedYear, yearValue);
            assertEquals(expectedYear, yearOfEraValue);
            // test leap year
            boolean leap = ((11 * yearValue + 14) % 30) < 11;
            assertEquals(leap, year.isLeap(millis));
            // test month length
            switch(monthValue) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 9:
                case 11:
                    assertEquals(30, monthLen);
                    break;
                case 2:
                case 4:
                case 6:
                case 8:
                case 10:
                    assertEquals(29, monthLen);
                    break;
                case 12:
                    assertEquals((leap ? 30 : 29), monthLen);
                    break;
            }
            // test year length
            assertEquals((leap ? 355 : 354), dayOfYearLen);
            // recalculate date
            expectedDOW = (((expectedDOW + 1) - 1) % 7) + 1;
            expectedDay++;
            expectedDOY++;
            if (expectedDay > monthLen) {
                expectedDay = 1;
                expectedMonth++;
                if (expectedMonth == 13) {
                    expectedMonth = 1;
                    expectedDOY = 1;
                    expectedYear++;
                }
            }
            millis += SKIP;
        }
    }
}
