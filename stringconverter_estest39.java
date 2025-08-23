package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.MonthDay;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableInterval;
import org.joda.time.MutablePeriod;
import org.joda.time.Partial;
import org.joda.time.PeriodType;
import org.joda.time.ReadWritableInterval;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.chrono.ZonedChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;
import org.junit.runner.RunWith;

public class StringConverter_ESTestTest39 extends StringConverter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        ISOChronology iSOChronology0 = ISOChronology.getInstance();
        StringConverter stringConverter0 = new StringConverter();
        MonthDay monthDay0 = MonthDay.now((Chronology) iSOChronology0);
        DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.year();
        Partial partial0 = new Partial(dateTimeFieldType0, (-2212));
        DateTimeFormatter dateTimeFormatter0 = partial0.getFormatter();
        // Undeclared exception!
        try {
            stringConverter0.getPartialValues((ReadablePartial) monthDay0, (Object) monthDay0, (Chronology) iSOChronology0, dateTimeFormatter0);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // org.joda.time.MonthDay cannot be cast to java.lang.String
            //
            verifyException("org.joda.time.convert.StringConverter", e);
        }
    }
}
