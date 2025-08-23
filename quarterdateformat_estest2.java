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

public class QuarterDateFormat_ESTestTest2 extends QuarterDateFormat_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        SimpleTimeZone simpleTimeZone0 = new SimpleTimeZone(712, "");
        String[] stringArray0 = new String[1];
        QuarterDateFormat quarterDateFormat0 = new QuarterDateFormat(simpleTimeZone0, stringArray0);
        MockDate mockDate0 = new MockDate((-1), 3, (-1), (-1), 1943, (-2263));
        Format.Field format_Field0 = mock(Format.Field.class, new ViolatedAssumptionAnswer());
        FieldPosition fieldPosition0 = new FieldPosition(format_Field0, 712);
        StringBuffer stringBuffer0 = new StringBuffer("{F@~<xQ@d0");
        quarterDateFormat0.format((Date) mockDate0, stringBuffer0, fieldPosition0);
        assertEquals(19, stringBuffer0.length());
    }
}
