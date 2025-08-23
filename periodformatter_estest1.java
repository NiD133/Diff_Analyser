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

public class PeriodFormatter_ESTestTest1 extends PeriodFormatter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        PeriodFormatterBuilder.FieldFormatter[] periodFormatterBuilder_FieldFormatterArray0 = new PeriodFormatterBuilder.FieldFormatter[0];
        PeriodFormatterBuilder.PluralAffix periodFormatterBuilder_PluralAffix0 = new PeriodFormatterBuilder.PluralAffix("Parsing not supported", "Parsing not supported");
        PeriodFormatterBuilder.FieldFormatter periodFormatterBuilder_FieldFormatter0 = new PeriodFormatterBuilder.FieldFormatter(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, true, 2078, periodFormatterBuilder_FieldFormatterArray0, periodFormatterBuilder_PluralAffix0, periodFormatterBuilder_PluralAffix0);
        Locale locale0 = Locale.GERMANY;
        StringWriter stringWriter0 = new StringWriter(2078);
        Period period0 = new Period(0, 1818, Integer.MAX_VALUE, 0, 623158436, 0, 0, 0);
        Duration duration0 = Duration.standardMinutes((-1814L));
        Seconds seconds0 = duration0.toStandardSeconds();
        PeriodType periodType0 = seconds0.getPeriodType();
        PeriodFormatter periodFormatter0 = new PeriodFormatter(periodFormatterBuilder_FieldFormatter0, periodFormatterBuilder_FieldFormatter0, locale0, periodType0);
        periodFormatter0.printTo((Writer) stringWriter0, (ReadablePeriod) period0);
        assertTrue(periodFormatter0.isPrinter());
    }
}
