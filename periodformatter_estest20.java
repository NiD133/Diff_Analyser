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

public class PeriodFormatter_ESTestTest20 extends PeriodFormatter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        LinkedList<Object> linkedList0 = new LinkedList<Object>();
        PeriodFormatterBuilder.Composite periodFormatterBuilder_Composite0 = new PeriodFormatterBuilder.Composite(linkedList0);
        PeriodFormatter periodFormatter0 = new PeriodFormatter(periodFormatterBuilder_Composite0, periodFormatterBuilder_Composite0);
        Hours hours0 = Hours.MAX_VALUE;
        // Undeclared exception!
        try {
            periodFormatter0.print(hours0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.joda.time.format.PeriodFormatterBuilder$Composite", e);
        }
    }
}
