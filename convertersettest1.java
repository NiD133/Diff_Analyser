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

public class ConverterSetTestTest1 extends TestCase {

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

    //-----------------------------------------------------------------------
    public void testClass() throws Exception {
        Class cls = ConverterSet.class;
        assertEquals(false, Modifier.isPublic(cls.getModifiers()));
        assertEquals(false, Modifier.isProtected(cls.getModifiers()));
        assertEquals(false, Modifier.isPrivate(cls.getModifiers()));
        assertEquals(1, cls.getDeclaredConstructors().length);
        Constructor con = cls.getDeclaredConstructors()[0];
        assertEquals(false, Modifier.isPublic(con.getModifiers()));
        assertEquals(false, Modifier.isProtected(con.getModifiers()));
        assertEquals(false, Modifier.isPrivate(con.getModifiers()));
    }
}