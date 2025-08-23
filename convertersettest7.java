package org.joda.time.convert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.GregorianCalendar;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.ReadWritableDateTime;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableInstant;

public class ConverterSetTestTest7 extends TestCase {

    private static final Converter c1 = new Converter() {

        public Class getSupportedType() {
            return Boolean.class;
        }
    };

    private static final Converter c2 = new Converter() {

        public Class getSupportedType() {
            return Character.class;
        }
    };

    private static final Converter c3 = new Converter() {

        public Class getSupportedType() {
            return Byte.class;
        }
    };

    private static final Converter c4 = new Converter() {

        public Class getSupportedType() {
            return Short.class;
        }
    };

    private static final Converter c4a = new Converter() {

        public Class getSupportedType() {
            return Short.class;
        }
    };

    private static final Converter c5 = new Converter() {

        public Class getSupportedType() {
            return Integer.class;
        }
    };

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestConverterSet.class);
    }

    public void testRemoveNullRemoved2() {
        Converter[] array = new Converter[] { c1, c2, c3, c4 };
        ConverterSet set = new ConverterSet(array);
        ConverterSet result = set.remove(c5, null);
        assertSame(set, result);
    }
}
