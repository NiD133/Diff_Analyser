package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.AssembledChronology;
import org.joda.time.chrono.ISOChronology;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ISOChronology_ESTest extends ISOChronology_ESTest_scaffolding {

    /**
     * Test assembling fields in the default ISOChronology instance.
     */
    @Test(timeout = 4000)
    public void testAssembleFieldsInDefaultInstance() throws Throwable {
        ISOChronology isoChronology = ISOChronology.getInstance();
        AssembledChronology.Fields fields = new AssembledChronology.Fields();
        isoChronology.assemble(fields);
    }

    /**
     * Test assembling fields in an ISOChronology instance with a specific time zone.
     */
    @Test(timeout = 4000)
    public void testAssembleFieldsWithTimeZone() throws Throwable {
        ISOChronology defaultChronology = ISOChronology.getInstance();
        DateTimeZone customZone = DateTimeZone.forOffsetMillis(7593750);
        ISOChronology customChronology = (ISOChronology) defaultChronology.withZone(customZone);
        AssembledChronology.Fields fields = new AssembledChronology.Fields();
        customChronology.assemble(fields);
        assertNotSame(customChronology, defaultChronology);
    }

    /**
     * Test equality of an ISOChronology instance with itself.
     */
    @Test(timeout = 4000)
    public void testEqualsWithSelf() throws Throwable {
        ISOChronology isoChronology = ISOChronology.getInstanceUTC();
        assertTrue(isoChronology.equals(isoChronology));
    }

    /**
     * Test equality of an ISOChronology instance with a different object type.
     */
    @Test(timeout = 4000)
    public void testEqualsWithDifferentObject() throws Throwable {
        ISOChronology isoChronology = ISOChronology.getInstanceUTC();
        Object differentObject = new Object();
        assertFalse(isoChronology.equals(differentObject));
    }

    /**
     * Test assembling fields with a null argument, expecting a NullPointerException.
     */
    @Test(timeout = 4000)
    public void testAssembleFieldsWithNull() throws Throwable {
        ISOChronology isoChronology = ISOChronology.getInstance();
        try {
            isoChronology.assemble(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("org.joda.time.chrono.ISOChronology", e);
        }
    }

    /**
     * Test the string representation of an ISOChronology instance in UTC.
     */
    @Test(timeout = 4000)
    public void testToStringInUTC() throws Throwable {
        ISOChronology isoChronology = ISOChronology.getInstanceUTC();
        assertEquals("ISOChronology[UTC]", isoChronology.toString());
    }

    /**
     * Test getting an ISOChronology instance with a specific time zone offset.
     */
    @Test(timeout = 4000)
    public void testGetInstanceWithOffsetMillis() throws Throwable {
        DateTimeZone offsetZone = DateTimeZone.forOffsetMillis(85);
        ISOChronology isoChronology = ISOChronology.getInstance(offsetZone);
        assertNotNull(isoChronology);
    }

    /**
     * Test getting an ISOChronology instance with UTC from a null time zone.
     */
    @Test(timeout = 4000)
    public void testWithUTCFromNullZone() throws Throwable {
        ISOChronology isoChronology = ISOChronology.getInstance(null);
        Chronology utcChronology = isoChronology.withUTC();
        assertSame(isoChronology, utcChronology);
    }

    /**
     * Test hash code computation for an ISOChronology instance with a specific time zone offset.
     */
    @Test(timeout = 4000)
    public void testHashCodeWithOffsetMillis() throws Throwable {
        DateTimeZone offsetZone = DateTimeZone.forOffsetMillis(-84);
        ISOChronology isoChronology = ISOChronology.getInstance(offsetZone);
        isoChronology.hashCode(); // Just to ensure no exceptions
    }

    /**
     * Test equality of ISOChronology instances with different time zones.
     */
    @Test(timeout = 4000)
    public void testEqualsWithDifferentZones() throws Throwable {
        ISOChronology utcChronology = ISOChronology.getInstanceUTC();
        DateTimeZone offsetZone = DateTimeZone.forOffsetMillis(-1322);
        Chronology offsetChronology = utcChronology.withZone(offsetZone);
        assertFalse(utcChronology.equals(offsetChronology));
    }

    /**
     * Test getting an ISOChronology instance with a null time zone, expecting default behavior.
     */
    @Test(timeout = 4000)
    public void testWithZoneNullReturnsSame() throws Throwable {
        ISOChronology isoChronology = ISOChronology.getInstance();
        Chronology sameChronology = isoChronology.withZone(null);
        assertSame(sameChronology, isoChronology);
    }
}