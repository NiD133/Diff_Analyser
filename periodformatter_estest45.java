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

public class PeriodFormatter_ESTestTest45 extends PeriodFormatter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test44() throws Throwable {
        PeriodFormatterBuilder.SimpleAffix periodFormatterBuilder_SimpleAffix0 = new PeriodFormatterBuilder.SimpleAffix("");
        PeriodFormatterBuilder.CompositeAffix periodFormatterBuilder_CompositeAffix0 = new PeriodFormatterBuilder.CompositeAffix(periodFormatterBuilder_SimpleAffix0, periodFormatterBuilder_SimpleAffix0);
        PeriodFormatterBuilder.FieldFormatter periodFormatterBuilder_FieldFormatter0 = new PeriodFormatterBuilder.FieldFormatter(11, 11, 1350, true, (-1158), (PeriodFormatterBuilder.FieldFormatter[]) null, periodFormatterBuilder_CompositeAffix0, periodFormatterBuilder_SimpleAffix0);
        Locale locale0 = Locale.GERMANY;
        PeriodType periodType0 = PeriodType.time();
        PeriodFormatter periodFormatter0 = new PeriodFormatter(periodFormatterBuilder_FieldFormatter0, periodFormatterBuilder_FieldFormatter0, locale0, periodType0);
        MutablePeriod mutablePeriod0 = periodFormatter0.parseMutablePeriod("");
        String string0 = periodFormatter0.print(mutablePeriod0);
        assertEquals("", string0);
    }
}
