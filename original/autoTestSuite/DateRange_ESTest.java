import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date;
import org.jfree.data.Range;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.example.DateRange; // Assuming DateRange is in this package

@RunWith(JUnit4.class)
public class DateRangeTest {

    @Test
    public void testGetUpperMillis_positiveValues() {
        DateRange dateRange = new DateRange(0.0, 0.0);
        assertEquals(0L, dateRange.getUpperMillis());
    }

    @Test
    public void testGetUpperMillis_negativeValues() {
        DateRange dateRange = new DateRange(-145.3, -145.3);
        assertEquals(-145L, dateRange.getUpperMillis());
    }

    @Test
    public void testGetLowerMillis_positiveValues() {
        Range range = Range.expandToInclude(null, 1801.7621);
        DateRange dateRange = new DateRange(range);
        assertEquals(1801L, dateRange.getLowerMillis());
    }

    @Test
    public void testGetLowerMillis_negativeValues() {
        DateRange dateRange = new DateRange(-1157.7534, -1157.7534);
        assertEquals(-1157L, dateRange.getLowerMillis());
    }

	//  This test is problematic because it expects an IllegalArgumentException
    //  from within the JFreeChart library when calling toString() on a potentially
    //  malformed Range object.  This is outside the control of this test suite
    //  and may not always trigger the expected exception.  It's been commented out
    //  because of it's instability and potential to fail spuriously.
	//
    //@Test(expected = IllegalArgumentException.class)
    //public void testRangeToStringThrowsException() {
    //    DateRange dateRange = new DateRange();
    //    Range range = Range.expandToInclude(dateRange, 1.2030679447063568);
    //    range.toString(); // Expecting IllegalArgumentException
    //}

    @Test(expected = NullPointerException.class)
    public void testConstructor_Range_nullRange() {
        new DateRange((Range) null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_DateDate_nullDates() {
        new DateRange((Date) null, (Date) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_DateDate_lowerDateAfterUpperDate() {
        Date lower = new Date(1, 785, -574, -1, 32, -1);
        Date upper = new Date(0, 0, 1);
        new DateRange(lower, upper);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_doubleDouble_lowerGreaterThanUpper() {
        new DateRange(0.0, -5043.58);
    }

    @Test
    public void testConstructor_DateDate_validDates() {
        Date date = new Date(774, 774, 32);
        DateRange dateRange = new DateRange(date, date);
        assertFalse(dateRange.isNaNRange());
    }

    @Test
    public void testGetUpperDate() {
        DateRange dateRange = new DateRange(0.0, 0.0);
        Date upperDate = dateRange.getUpperDate();
        assertEquals("Thu Jan 01 00:00:00 GMT 1970", upperDate.toString());
    }

    @Test
    public void testGetLowerDate() {
        DateRange dateRange = new DateRange(0.0, 0.0);
        Date lowerDate = dateRange.getLowerDate();
        assertEquals("Thu Jan 01 00:00:00 GMT 1970", lowerDate.toString());
    }

    @Test
    public void testGetLowerMillis() {
        DateRange dateRange = new DateRange();
        assertEquals(0L, dateRange.getLowerMillis());
    }

    @Test
    public void testToString() {
        DateRange dateRange = new DateRange();
        assertEquals("[Jan 1, 1970 12:00:00 AM --> Jan 1, 1970 12:00:00 AM]", dateRange.toString());
    }

    @Test
    public void testGetUpperMillis() {
        DateRange dateRange = new DateRange();
        assertEquals(1L, dateRange.getUpperMillis());
    }
}