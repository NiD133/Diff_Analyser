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

public class PeriodFormatter_ESTestTest11 extends PeriodFormatter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        PeriodFormatterBuilder.Literal periodFormatterBuilder_Literal0 = PeriodFormatterBuilder.Literal.EMPTY;
        Locale locale0 = Locale.ENGLISH;
        Hours hours0 = Hours.TWO;
        PeriodType periodType0 = hours0.getPeriodType();
        PeriodType periodType1 = periodType0.withHoursRemoved();
        PeriodFormatter periodFormatter0 = new PeriodFormatter(periodFormatterBuilder_Literal0, periodFormatterBuilder_Literal0, locale0, periodType1);
        PeriodType periodType2 = periodFormatter0.getParseType();
        assertEquals(0, periodType2.size());
    }
}