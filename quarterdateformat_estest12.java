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

public class QuarterDateFormat_ESTestTest12 extends QuarterDateFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        TimeZone timeZone0 = TimeZone.getDefault();
        String[] stringArray0 = new String[0];
        QuarterDateFormat quarterDateFormat0 = new QuarterDateFormat(timeZone0, stringArray0, true);
        MockDate mockDate0 = new MockDate();
        FieldPosition fieldPosition0 = new FieldPosition((-1375));
        // Undeclared exception!
        try {
            quarterDateFormat0.format((Date) mockDate0, (StringBuffer) null, fieldPosition0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // Index 0 out of bounds for length 0
            //
            verifyException("org.jfree.chart.axis.QuarterDateFormat", e);
        }
    }
}
