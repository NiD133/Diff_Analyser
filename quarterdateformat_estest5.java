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

public class QuarterDateFormat_ESTestTest5 extends QuarterDateFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        QuarterDateFormat quarterDateFormat0 = new QuarterDateFormat();
        quarterDateFormat0.setNumberFormat((NumberFormat) null);
        QuarterDateFormat quarterDateFormat1 = new QuarterDateFormat();
        // Undeclared exception!
        try {
            quarterDateFormat0.equals(quarterDateFormat1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.text.DateFormat", e);
        }
    }
}
