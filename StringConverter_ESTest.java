package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.format.*;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class StringConverterTest extends StringConverter_ESTest_scaffolding {

    private final StringConverter stringConverter = StringConverter.INSTANCE;

    @Test(timeout = 4000)
    public void testInvalidDurationFormatThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            stringConverter.getDurationMillis("Pt,v.y");
        });
    }

    @Test(timeout = 4000)
    public void testNullPeriodThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            stringConverter.setInto((ReadWritablePeriod) null, (Object) null, (Chronology) null);
        });
    }

    @Test(timeout = 4000)
    public void testInvalidIntervalFormatThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            MutableInterval.parse("p%/=-V$,Z");
        });
    }

    @Test(timeout = 4000)
    public void testValidIntervalParsing() {
        MutableInterval interval = MutableInterval.parse("6/8");
        assertEquals(-42368486400000L, interval.getStartMillis());
    }

    @Test(timeout = 4000)
    public void testValidDurationParsing() {
        long durationMillis = stringConverter.getDurationMillis("Pt2s");
        assertEquals(2000L, durationMillis);
    }

    @Test(timeout = 4000)
    public void testInvalidInstantFormatThrowsIllegalArgumentException() {
        CopticChronology copticChronology = CopticChronology.getInstanceUTC();
        assertThrows(IllegalArgumentException.class, () -> {
            stringConverter.getInstantMillis(" cannot be compared to ", copticChronology);
        });
    }

    @Test(timeout = 4000)
    public void testGetSupportedTypeReturnsStringClass() {
        Class<?> supportedType = stringConverter.getSupportedType();
        assertEquals(String.class, supportedType);
    }

    // Additional tests can be added here following the same pattern
}