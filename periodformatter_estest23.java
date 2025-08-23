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

public class PeriodFormatter_ESTestTest23 extends PeriodFormatter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        PeriodFormatterBuilder.Literal periodFormatterBuilder_Literal0 = PeriodFormatterBuilder.Literal.EMPTY;
        String[] stringArray0 = new String[6];
        stringArray0[0] = "";
        stringArray0[1] = "";
        stringArray0[2] = "";
        stringArray0[3] = "";
        stringArray0[4] = "";
        stringArray0[5] = "";
        PeriodFormatterBuilder.Separator periodFormatterBuilder_Separator0 = new PeriodFormatterBuilder.Separator("", "", stringArray0, periodFormatterBuilder_Literal0, periodFormatterBuilder_Literal0, true, true);
        PeriodFormatter periodFormatter0 = new PeriodFormatter(periodFormatterBuilder_Literal0, periodFormatterBuilder_Separator0);
        PeriodFormat.DynamicWordBased periodFormat_DynamicWordBased0 = new PeriodFormat.DynamicWordBased(periodFormatter0);
        periodFormatterBuilder_Separator0.finish(periodFormatterBuilder_Literal0, periodFormat_DynamicWordBased0);
        // Undeclared exception!
        try {
            periodFormatter0.parsePeriod("");
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
