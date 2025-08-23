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

public class FieldUtils_ESTestTest41 extends FieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        Object object0 = new Object();
        Integer integer0 = new Integer(0);
        boolean boolean0 = FieldUtils.equals(object0, (Object) integer0);
        assertFalse(boolean0);
    }
}
