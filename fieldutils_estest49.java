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

public class FieldUtils_ESTestTest49 extends FieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test48() throws Throwable {
        DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.weekOfWeekyear();
        FieldUtils.verifyValueBounds(dateTimeFieldType0, 1537, (-2144353229), 1537);
        assertEquals("weekOfWeekyear", dateTimeFieldType0.getName());
    }
}
