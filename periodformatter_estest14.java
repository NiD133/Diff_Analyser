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

public class PeriodFormatter_ESTestTest14 extends PeriodFormatter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        PeriodFormatterBuilder.Literal periodFormatterBuilder_Literal0 = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter periodFormatter0 = new PeriodFormatter((PeriodPrinter) null, periodFormatterBuilder_Literal0);
        StringBuffer stringBuffer0 = new StringBuffer("");
        Minutes minutes0 = Minutes.MIN_VALUE;
        // Undeclared exception!
        try {
            periodFormatter0.printTo(stringBuffer0, (ReadablePeriod) minutes0);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // Printing not supported
            //
            verifyException("org.joda.time.format.PeriodFormatter", e);
        }
    }
}
