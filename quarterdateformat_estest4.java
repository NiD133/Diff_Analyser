package org.jfree.chart.axis;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.StringWriter;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.util.MockDate;
import org.junit.runner.RunWith;

public class QuarterDateFormat_ESTestTest4 extends QuarterDateFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        SimpleTimeZone simpleTimeZone0 = new SimpleTimeZone(4, "");
        QuarterDateFormat quarterDateFormat0 = new QuarterDateFormat(simpleTimeZone0);
        MockDate mockDate0 = new MockDate(1L);
        FieldPosition fieldPosition0 = new FieldPosition(80);
        // Undeclared exception!
        try {
            quarterDateFormat0.format((Date) mockDate0, (StringBuffer) null, fieldPosition0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.chart.axis.QuarterDateFormat", e);
        }
    }
}
