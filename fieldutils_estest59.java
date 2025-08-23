package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.math.RoundingMode;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.ZonedChronology;
import org.junit.runner.RunWith;

public class FieldUtils_ESTestTest59 extends FieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test58() throws Throwable {
        RoundingMode roundingMode0 = RoundingMode.FLOOR;
        long long0 = FieldUtils.safeDivide((long) Integer.MIN_VALUE, (long) Integer.MIN_VALUE, roundingMode0);
        assertEquals(1L, long0);
    }
}
