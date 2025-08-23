package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.io.PipedWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadablePeriod;
import org.joda.time.Seconds;
import org.joda.time.Weeks;
import org.joda.time.Years;
import org.junit.runner.RunWith;

public class PeriodFormatter_ESTestTest36 extends PeriodFormatter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test35() throws Throwable {
        PeriodFormatterBuilder.Literal periodFormatterBuilder_Literal0 = new PeriodFormatterBuilder.Literal("");
        StringBuffer stringBuffer0 = new StringBuffer("~eGa!VG)p");
        PeriodFormatter periodFormatter0 = new PeriodFormatter(periodFormatterBuilder_Literal0, periodFormatterBuilder_Literal0);
        // Undeclared exception!
        try {
            periodFormatter0.printTo(stringBuffer0, (ReadablePeriod) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Period must not be null
            //
            verifyException("org.joda.time.format.PeriodFormatter", e);
        }
    }
}
