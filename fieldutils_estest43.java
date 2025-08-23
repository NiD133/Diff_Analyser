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

public class FieldUtils_ESTestTest43 extends FieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        Object object0 = new Object();
        boolean boolean0 = FieldUtils.equals((Object) null, object0);
        assertFalse(boolean0);
    }
}
