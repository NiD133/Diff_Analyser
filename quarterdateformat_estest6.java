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

public class QuarterDateFormat_ESTestTest6 extends QuarterDateFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        String[] stringArray0 = new String[6];
        QuarterDateFormat quarterDateFormat0 = null;
        try {
            quarterDateFormat0 = new QuarterDateFormat((TimeZone) null, stringArray0, true);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Null 'zone' argument.
            //
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }
}
