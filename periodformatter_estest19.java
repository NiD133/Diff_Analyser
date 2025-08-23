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

public class PeriodFormatter_ESTestTest19 extends PeriodFormatter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        String[] stringArray0 = new String[1];
        stringArray0[0] = "AJq6";
        PeriodFormatterBuilder.Literal periodFormatterBuilder_Literal0 = new PeriodFormatterBuilder.Literal("AJq6");
        PeriodFormatterBuilder.Separator periodFormatterBuilder_Separator0 = new PeriodFormatterBuilder.Separator("AJq6", "AJq6", stringArray0, periodFormatterBuilder_Literal0, periodFormatterBuilder_Literal0, false, false);
        Locale locale0 = Locale.GERMANY;
        Years years0 = Years.MAX_VALUE;
        PeriodType periodType0 = years0.getPeriodType();
        periodFormatterBuilder_Separator0.finish(periodFormatterBuilder_Separator0, periodFormatterBuilder_Separator0);
        PeriodFormatter periodFormatter0 = new PeriodFormatter(periodFormatterBuilder_Separator0, periodFormatterBuilder_Separator0, locale0, periodType0);
        // Undeclared exception!
        try {
            periodFormatter0.print(years0);
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
