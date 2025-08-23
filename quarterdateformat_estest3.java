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

public class QuarterDateFormat_ESTestTest3 extends QuarterDateFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        TimeZone timeZone0 = TimeZone.getDefault();
        QuarterDateFormat quarterDateFormat0 = new QuarterDateFormat(timeZone0);
        QuarterDateFormat quarterDateFormat1 = new QuarterDateFormat(timeZone0, quarterDateFormat0.ROMAN_QUARTERS, true);
        MockDate mockDate0 = new MockDate((-2355), 0, 2123, 584, 0, (-797));
        StringWriter stringWriter0 = new StringWriter();
        StringBuffer stringBuffer0 = stringWriter0.getBuffer();
        quarterDateFormat1.format((Date) mockDate0, stringBuffer0, (FieldPosition) null);
        assertEquals("IV 451", stringBuffer0.toString());
        assertEquals("IV 451", stringWriter0.toString());
    }
}
