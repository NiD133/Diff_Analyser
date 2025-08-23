package org.joda.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * JUnit test suite for the Years class.
 * This class tests various functionalities of the Years class, including constants, factory methods, arithmetic operations, and serialization.
 * 
 * Author: Stephen Colebourne
 */
public class TestYears extends TestCase {

    // Time zone for testing purposes
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    // Main method to run the test suite
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    // Create a test suite containing all test cases
    public static TestSuite suite() {
        return new TestSuite(TestYears.class);
    }

    // Constructor
    public TestYears(String name) {
        super(name);
    }

    // Setup method (currently not used)
    @Override
    protected void setUp() throws Exception {
    }

    // Teardown method (currently not used)
    @Override
    protected void tearDown() throws Exception {
    }

    // Test constants defined in the Years class
    public void testConstants() {
        assertEquals("ZERO constant should be 0 years", 0, Years.ZERO.getYears());
        assertEquals("ONE constant should be 1 year", 1, Years.ONE.getYears());
        assertEquals("TWO constant should be 2 years", 2, Years.TWO.getYears());
        assertEquals("THREE constant should be 3 years", 3, Years.THREE.getYears());
        assertEquals("MAX_VALUE constant should be Integer.MAX_VALUE years", Integer.MAX_VALUE, Years.MAX_VALUE.getYears());
        assertEquals("MIN_VALUE constant should be Integer.MIN_VALUE years", Integer.MIN_VALUE, Years.MIN_VALUE.getYears());
    }

    // Test factory method for creating Years instances from an integer
    public void testFactory_years_int() {
        assertSame("Years instance for 0 should be ZERO", Years.ZERO, Years.years(0));
        assertSame("Years instance for 1 should be ONE", Years.ONE, Years.years(1));
        assertSame("Years instance for 2 should be TWO", Years.TWO, Years.years(2));
        assertSame("Years instance for 3 should be THREE", Years.THREE, Years.years(3));
        assertSame("Years instance for Integer.MAX_VALUE should be MAX_VALUE", Years.MAX_VALUE, Years.years(Integer.MAX_VALUE));
        assertSame("Years instance for Integer.MIN_VALUE should be MIN_VALUE", Years.MIN_VALUE, Years.years(Integer.MIN_VALUE));
        assertEquals("Years instance for -1 should be -1 years", -1, Years.years(-1).getYears());
        assertEquals("Years instance for 4 should be 4 years", 4, Years.years(4).getYears());
    }

    // Test factory method for calculating years between two instants
    public void testFactory_yearsBetween_RInstant() {
        DateTime start = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime end1 = new DateTime(2009, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime end2 = new DateTime(2012, 6, 9, 12, 0, 0, 0, PARIS);

        assertEquals("Years between start and end1 should be 3", 3, Years.yearsBetween(start, end1).getYears());
        assertEquals("Years between start and start should be 0", 0, Years.yearsBetween(start, start).getYears());
        assertEquals("Years between end1 and end1 should be 0", 0, Years.yearsBetween(end1, end1).getYears());
        assertEquals("Years between end1 and start should be -3", -3, Years.yearsBetween(end1, start).getYears());
        assertEquals("Years between start and end2 should be 6", 6, Years.yearsBetween(start, end2).getYears());
    }

    // Test factory method for calculating years between two partial dates
    @SuppressWarnings("deprecation")
    public void testFactory_yearsBetween_RPartial() {
        LocalDate start = new LocalDate(2006, 6, 9);
        LocalDate end1 = new LocalDate(2009, 6, 9);
        YearMonthDay end2 = new YearMonthDay(2012, 6, 9);

        assertEquals("Years between start and end1 should be 3", 3, Years.yearsBetween(start, end1).getYears());
        assertEquals("Years between start and start should be 0", 0, Years.yearsBetween(start, start).getYears());
        assertEquals("Years between end1 and end1 should be 0", 0, Years.yearsBetween(end1, end1).getYears());
        assertEquals("Years between end1 and start should be -3", -3, Years.yearsBetween(end1, start).getYears());
        assertEquals("Years between start and end2 should be 6", 6, Years.yearsBetween(start, end2).getYears());
    }

    // Test factory method for calculating years in an interval
    public void testFactory_yearsIn_RInterval() {
        DateTime start = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime end1 = new DateTime(2009, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime end2 = new DateTime(2012, 6, 9, 12, 0, 0, 0, PARIS);

        assertEquals("Years in null interval should be 0", 0, Years.yearsIn((ReadableInterval) null).getYears());
        assertEquals("Years in interval from start to end1 should be 3", 3, Years.yearsIn(new Interval(start, end1)).getYears());
        assertEquals("Years in interval from start to start should be 0", 0, Years.yearsIn(new Interval(start, start)).getYears());
        assertEquals("Years in interval from end1 to end1 should be 0", 0, Years.yearsIn(new Interval(end1, end1)).getYears());
        assertEquals("Years in interval from start to end2 should be 6", 6, Years.yearsIn(new Interval(start, end2)).getYears());
    }

    // Test parsing of years from a string
    public void testFactory_parseYears_String() {
        assertEquals("Parsing null string should return 0 years", 0, Years.parseYears((String) null).getYears());
        assertEquals("Parsing 'P0Y' should return 0 years", 0, Years.parseYears("P0Y").getYears());
        assertEquals("Parsing 'P1Y' should return 1 year", 1, Years.parseYears("P1Y").getYears());
        assertEquals("Parsing 'P-3Y' should return -3 years", -3, Years.parseYears("P-3Y").getYears());
        assertEquals("Parsing 'P2Y0M' should return 2 years", 2, Years.parseYears("P2Y0M").getYears());
        assertEquals("Parsing 'P2YT0H0M' should return 2 years", 2, Years.parseYears("P2YT0H0M").getYears());

        // Test invalid parsing
        try {
            Years.parseYears("P1M1D");
            fail("Parsing 'P1M1D' should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }

        try {
            Years.parseYears("P1YT1H");
            fail("Parsing 'P1YT1H' should throw IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    // Test getting the number of years
    public void testGetMethods() {
        Years test = Years.years(20);
        assertEquals("Years should be 20", 20, test.getYears());
    }

    // Test getting the field type
    public void testGetFieldType() {
        Years test = Years.years(20);
        assertEquals("Field type should be years", DurationFieldType.years(), test.getFieldType());
    }

    // Test getting the period type
    public void testGetPeriodType() {
        Years test = Years.years(20);
        assertEquals("Period type should be years", PeriodType.years(), test.getPeriodType());
    }

    // Test isGreaterThan method
    public void testIsGreaterThan() {
        assertTrue("THREE should be greater than TWO", Years.THREE.isGreaterThan(Years.TWO));
        assertFalse("THREE should not be greater than THREE", Years.THREE.isGreaterThan(Years.THREE));
        assertFalse("TWO should not be greater than THREE", Years.TWO.isGreaterThan(Years.THREE));
        assertTrue("ONE should be greater than null", Years.ONE.isGreaterThan(null));
        assertFalse("Years(-1) should not be greater than null", Years.years(-1).isGreaterThan(null));
    }

    // Test isLessThan method
    public void testIsLessThan() {
        assertFalse("THREE should not be less than TWO", Years.THREE.isLessThan(Years.TWO));
        assertFalse("THREE should not be less than THREE", Years.THREE.isLessThan(Years.THREE));
        assertTrue("TWO should be less than THREE", Years.TWO.isLessThan(Years.THREE));
        assertFalse("ONE should not be less than null", Years.ONE.isLessThan(null));
        assertTrue("Years(-1) should be less than null", Years.years(-1).isLessThan(null));
    }

    // Test toString method
    public void testToString() {
        Years test = Years.years(20);
        assertEquals("String representation should be 'P20Y'", "P20Y", test.toString());

        test = Years.years(-20);
        assertEquals("String representation should be 'P-20Y'", "P-20Y", test.toString());
    }

    // Test serialization and deserialization
    public void testSerialization() throws Exception {
        Years test = Years.THREE;

        // Serialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        oos.close();
        byte[] bytes = baos.toByteArray();

        // Deserialize
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Years result = (Years) ois.readObject();
        ois.close();

        assertSame("Deserialized object should be the same as original", test, result);
    }

    // Test adding years
    public void testPlus_int() {
        Years test2 = Years.years(2);
        Years result = test2.plus(3);
        assertEquals("Original years should be 2", 2, test2.getYears());
        assertEquals("Resulting years should be 5", 5, result.getYears());

        assertEquals("Adding 0 to ONE should result in 1 year", 1, Years.ONE.plus(0).getYears());

        // Test overflow
        try {
            Years.MAX_VALUE.plus(1);
            fail("Adding 1 to MAX_VALUE should throw ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    // Test adding Years
    public void testPlus_Years() {
        Years test2 = Years.years(2);
        Years test3 = Years.years(3);
        Years result = test2.plus(test3);
        assertEquals("Original years should be 2", 2, test2.getYears());
        assertEquals("Original years should be 3", 3, test3.getYears());
        assertEquals("Resulting years should be 5", 5, result.getYears());

        assertEquals("Adding ZERO to ONE should result in 1 year", 1, Years.ONE.plus(Years.ZERO).getYears());
        assertEquals("Adding null to ONE should result in 1 year", 1, Years.ONE.plus((Years) null).getYears());

        // Test overflow
        try {
            Years.MAX_VALUE.plus(Years.ONE);
            fail("Adding ONE to MAX_VALUE should throw ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    // Test subtracting years
    public void testMinus_int() {
        Years test2 = Years.years(2);
        Years result = test2.minus(3);
        assertEquals("Original years should be 2", 2, test2.getYears());
        assertEquals("Resulting years should be -1", -1, result.getYears());

        assertEquals("Subtracting 0 from ONE should result in 1 year", 1, Years.ONE.minus(0).getYears());

        // Test underflow
        try {
            Years.MIN_VALUE.minus(1);
            fail("Subtracting 1 from MIN_VALUE should throw ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    // Test subtracting Years
    public void testMinus_Years() {
        Years test2 = Years.years(2);
        Years test3 = Years.years(3);
        Years result = test2.minus(test3);
        assertEquals("Original years should be 2", 2, test2.getYears());
        assertEquals("Original years should be 3", 3, test3.getYears());
        assertEquals("Resulting years should be -1", -1, result.getYears());

        assertEquals("Subtracting ZERO from ONE should result in 1 year", 1, Years.ONE.minus(Years.ZERO).getYears());
        assertEquals("Subtracting null from ONE should result in 1 year", 1, Years.ONE.minus((Years) null).getYears());

        // Test underflow
        try {
            Years.MIN_VALUE.minus(Years.ONE);
            fail("Subtracting ONE from MIN_VALUE should throw ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    // Test multiplying years
    public void testMultipliedBy_int() {
        Years test = Years.years(2);
        assertEquals("2 multiplied by 3 should be 6", 6, test.multipliedBy(3).getYears());
        assertEquals("Original years should be 2", 2, test.getYears());
        assertEquals("2 multiplied by -3 should be -6", -6, test.multipliedBy(-3).getYears());
        assertSame("Multiplying by 1 should return the same instance", test, test.multipliedBy(1));

        // Test overflow
        Years halfMax = Years.years(Integer.MAX_VALUE / 2 + 1);
        try {
            halfMax.multipliedBy(2);
            fail("Multiplying by 2 should throw ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    // Test dividing years
    public void testDividedBy_int() {
        Years test = Years.years(12);
        assertEquals("12 divided by 2 should be 6", 6, test.dividedBy(2).getYears());
        assertEquals("Original years should be 12", 12, test.getYears());
        assertEquals("12 divided by 3 should be 4", 4, test.dividedBy(3).getYears());
        assertEquals("12 divided by 4 should be 3", 3, test.dividedBy(4).getYears());
        assertEquals("12 divided by 5 should be 2", 2, test.dividedBy(5).getYears());
        assertEquals("12 divided by 6 should be 2", 2, test.dividedBy(6).getYears());
        assertSame("Dividing by 1 should return the same instance", test, test.dividedBy(1));

        // Test division by zero
        try {
            Years.ONE.dividedBy(0);
            fail("Dividing by 0 should throw ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    // Test negating years
    public void testNegated() {
        Years test = Years.years(12);
        assertEquals("Negating 12 should be -12", -12, test.negated().getYears());
        assertEquals("Original years should be 12", 12, test.getYears());

        // Test negating MIN_VALUE
        try {
            Years.MIN_VALUE.negated();
            fail("Negating MIN_VALUE should throw ArithmeticException");
        } catch (ArithmeticException ex) {
            // expected
        }
    }

    // Test adding years to a LocalDate
    public void testAddToLocalDate() {
        Years test = Years.years(3);
        LocalDate date = new LocalDate(2006, 6, 1);
        LocalDate expected = new LocalDate(2009, 6, 1);
        assertEquals("Adding 3 years to date should result in expected date", expected, date.plus(test));
    }
}