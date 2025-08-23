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

public class PeriodFormatter_ESTestTest2 extends PeriodFormatter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        String[] stringArray0 = new String[6];
        stringArray0[0] = "E";
        stringArray0[1] = "E";
        stringArray0[2] = "E";
        stringArray0[3] = "E";
        stringArray0[4] = "E";
        stringArray0[5] = "E";
        LinkedList<Object> linkedList0 = new LinkedList<Object>();
        PeriodFormatterBuilder.Composite periodFormatterBuilder_Composite0 = new PeriodFormatterBuilder.Composite(linkedList0);
        PeriodFormatterBuilder.Separator periodFormatterBuilder_Separator0 = new PeriodFormatterBuilder.Separator("E", "E", stringArray0, periodFormatterBuilder_Composite0, periodFormatterBuilder_Composite0, false, true);
        Locale locale0 = Locale.KOREA;
        PeriodType periodType0 = PeriodType.millis();
        PeriodFormatter periodFormatter0 = new PeriodFormatter(periodFormatterBuilder_Separator0, (PeriodParser) null, locale0, periodType0);
        PeriodFormatter periodFormatter1 = periodFormatter0.withParseType(periodType0);
        assertSame(periodFormatter1, periodFormatter0);
    }
}
