/**
 * Test suite for Seconds class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class SecondsTest extends Seconds_ESTest_scaffolding {

    /**
     * Test suite for seconds conversion.
     */
    @Test(timeout = 4000)
    public void testSecondsConversion() {
        // Test conversion of seconds to standard weeks
        Weeks weeks = Seconds.seconds(TWO_SECONDS).toStandardWeeks();
        assertEquals(0, weeks.getWeeks());

        // Test conversion of seconds to standard days
        Days days = Seconds.seconds(TWO_SECONDS).toStandardDays();
        assertEquals(0, days.getDays());

        // Test conversion of seconds to standard hours
        Hours hours = Seconds.seconds(TWO_SECONDS).toStandardHours();
        assertEquals(0, hours.getHours());

        // Test conversion of seconds to standard minutes
        Minutes minutes = Seconds.seconds(TWO_SECONDS).toStandardMinutes();
        assertEquals(0, minutes.getMinutes());
    }

    /**
     * Test suite for duration calculation.
     */
    @Test(timeout = 4000)
    public void testDurationCalculation() {
        // Test duration calculation between two instants
        Instant start = new Instant();
        Instant end = new Instant();
        Seconds seconds = Seconds.secondsBetween(start, end);
        assertEquals(0, seconds.getSeconds());

        // Test duration calculation between two partial dates
        Partial startPartial = new Partial((Chronology) null, new DateTimeFieldType[0], new int[0]);
        Partial endPartial = new Partial((Chronology) null, new DateTimeFieldType[0], new int[0]);
        seconds = Seconds.secondsBetween(startPartial, endPartial);
        assertEquals(0, seconds.getSeconds());
    }

    /**
     * Test suite for parsing seconds from string.
     */
    @Test(timeout = 4000)
    public void testParsingSecondsFromString() {
        // Test parsing seconds from string
        Seconds seconds = Seconds.parseSeconds("PT1S");
        assertEquals(ONE_SECOND, seconds.getSeconds());
    }

    /**
     * Test suite for seconds arithmetic operations.
     */
    @Test(timeout = 4000)
    public void testSecondsArithmeticOperations() {
        // Test addition of seconds
        Seconds seconds1 = Seconds.seconds(ONE_SECOND);
        Seconds seconds2 = seconds1.plus(ONE_SECOND);
        assertEquals(TWO_SECONDS, seconds2.getSeconds());

        // Test subtraction of seconds
        seconds2 = seconds1.minus(ONE_SECOND);
        assertEquals(0, seconds2.getSeconds());

        // Test multiplication of seconds
        seconds2 = seconds1.multipliedBy(TWO_SECONDS);
        assertEquals(TWO_SECONDS, seconds2.getSeconds());

        // Test division of seconds
        seconds2 = seconds1.dividedBy(ONE_SECOND);
        assertEquals(ONE_SECOND, seconds2.getSeconds());
    }

    /**
     * Test suite for seconds comparison.
     */
    @Test(timeout = 4000)
    public void testSecondsComparison() {
        // Test comparison of seconds
        Seconds seconds1 = Seconds.seconds(ONE_SECOND);
        Seconds seconds2 = Seconds.seconds(TWO_SECONDS);
        assertTrue(seconds1.isLessThan(seconds2));
        assertFalse(seconds1.isGreaterThan(seconds2));
    }

    /**
     * Test suite for edge cases.
     */
    @Test(timeout = 4000)
    public void testEdgeCases() {
        // Test edge case for maximum seconds
        Seconds maxSeconds = Seconds.MAX_VALUE;
        assertEquals(MAX_SECONDS, maxSeconds.getSeconds());

        // Test edge case for minimum seconds
        Seconds minSeconds = Seconds.MIN_VALUE;
        assertEquals(MIN_SECONDS, minSeconds.getSeconds());
    }
}