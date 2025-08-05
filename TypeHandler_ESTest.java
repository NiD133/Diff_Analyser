package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.File;
import java.util.Date;
import java.util.Map;
import org.apache.commons.cli.Converter;
import org.apache.commons.cli.TypeHandler;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class TypeHandler_ESTest extends TypeHandler_ESTest_scaffolding {

    private static final String INVALID_CLASS_NAME = "converterMap";
    private static final String INVALID_DATE_STRING = "w2E%~v5+#";
    private static final String INVALID_NUMBER_STRING = "I%5HIg?GFqZ!&";
    private static final String INVALID_URL_STRING = "N)i";
    private static final String EMPTY_STRING = "";

    @Test(timeout = 4000)
    public void testCreateNumberWithValidString() throws Throwable {
        // Test creating a number from a valid string
        Number number = TypeHandler.createNumber("6");
        assertEquals(6L, number);
    }

    @Test(timeout = 4000)
    public void testCreateValueWithEmptyString() throws Throwable {
        // Test creating a value with an empty string
        Object value = TypeHandler.createValue(EMPTY_STRING, (Object) null);
        assertEquals(EMPTY_STRING, value);
    }

    @Test(timeout = 4000)
    public void testCreateValueWithNullString() throws Throwable {
        // Test creating a value with a null string
        Class<MockThrowable> clazz = MockThrowable.class;
        MockThrowable mockThrowable = TypeHandler.createValue(null, clazz);
        assertNull(mockThrowable);
    }

    @Test(timeout = 4000)
    public void testCreateFileWithDotString() throws Throwable {
        // Test creating a file with a dot string
        File file = TypeHandler.createFile(".7");
        assertEquals(0L, file.length());
    }

    @Test(timeout = 4000)
    public void testCreateFileWithInvalidString() throws Throwable {
        // Test creating a file with an invalid string
        File file = TypeHandler.createFile("wnU<h1?h~G9CL");
        assertFalse(file.isDirectory());
    }

    @Test(timeout = 4000)
    public void testCreateDefaultMap() throws Throwable {
        // Test creating a default map
        Map<Class<?>, Converter<?, ? extends Throwable>> map = TypeHandler.createDefaultMap();
        TypeHandler typeHandler = new TypeHandler(map);
    }

    @Test(timeout = 4000)
    public void testCreateClassWithValidName() throws Throwable {
        // Test creating a class with a valid name
        Class<?> clazz = TypeHandler.createClass("org.apache.commons.cli.ParseException");
        assertFalse(clazz.isArray());
    }

    @Test(timeout = 4000)
    public void testCreateValueWithInvalidDate() throws Throwable {
        // Test creating a value with an invalid date string
        Class<Date> clazz = Date.class;
        try {
            TypeHandler.createValue(INVALID_CLASS_NAME, clazz);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.cli.ParseException", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateValueWithInvalidClassCast() throws Throwable {
        // Test creating a value with an invalid class cast
        try {
            TypeHandler.createValue(EMPTY_STRING, (Object) EMPTY_STRING);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.apache.commons.cli.TypeHandler", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateObjectWithInvalidClassName() throws Throwable {
        // Test creating an object with an invalid class name
        try {
            TypeHandler.createObject("P");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.cli.ParseException", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateFileWithNullString() throws Throwable {
        // Test creating a file with a null string
        try {
            TypeHandler.createFile(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.cli.TypeHandler", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateClassWithInvalidName() throws Throwable {
        // Test creating a class with an invalid name
        try {
            TypeHandler.createClass(INVALID_CLASS_NAME);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.cli.ParseException", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateValueWithEmptyDateString() throws Throwable {
        // Test creating a value with an empty date string
        Class<Date> clazz = Date.class;
        try {
            TypeHandler.createValue(EMPTY_STRING, clazz);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.cli.ParseException", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetConverterForDateClass() throws Throwable {
        // Test getting a converter for the Date class
        TypeHandler typeHandler = TypeHandler.getDefault();
        Class<Date> clazz = Date.class;
        Converter<Date, ?> converter = typeHandler.getConverter(clazz);
        assertNotNull(converter);
    }

    @Test(timeout = 4000)
    public void testCreateDateWithInvalidString() throws Throwable {
        // Test creating a date with an invalid string
        try {
            TypeHandler.createDate(INVALID_DATE_STRING);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.cli.TypeHandler", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateURLWithInvalidString() throws Throwable {
        // Test creating a URL with an invalid string
        try {
            TypeHandler.createURL(INVALID_URL_STRING);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.cli.ParseException", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateNumberWithInvalidString() throws Throwable {
        // Test creating a number with an invalid string
        try {
            TypeHandler.createNumber(INVALID_NUMBER_STRING);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.cli.ParseException", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateFilesWithUnsupportedOperation() throws Throwable {
        // Test creating files with unsupported operation
        try {
            TypeHandler.createFiles("org.apache.commons.cli.Converter");
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.cli.TypeHandler", e);
        }
    }

    @Test(timeout = 4000)
    public void testOpenFileWithInvalidPath() throws Throwable {
        // Test opening a file with an invalid path
        try {
            TypeHandler.openFile("EEE MMM dd HH:mm:ss zzz yyyy");
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.cli.ParseException", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateValueWithNullParameters() throws Throwable {
        // Test creating a value with null parameters
        Object value = TypeHandler.createValue(null, (Object) null);
        assertNull(value);
    }

    @Test(timeout = 4000)
    public void testCreateObjectWithValidClassName() throws Throwable {
        // Test creating an object with a valid class name
        Object object = TypeHandler.createObject("org.apache.commons.cli.TypeHandler");
        assertNotNull(object);
    }

    @Test(timeout = 4000)
    public void testTypeHandlerConstructor() throws Throwable {
        // Test TypeHandler constructor
        TypeHandler typeHandler = new TypeHandler();
    }

    @Test(timeout = 4000)
    public void testCreateClassWithConverterName() throws Throwable {
        // Test creating a class with a converter name
        Class<?> clazz = TypeHandler.createClass("org.apache.commons.cli.Converter");
        assertEquals(1537, clazz.getModifiers());
    }

    @Test(timeout = 4000)
    public void testCreateFileWithRootPath() throws Throwable {
        // Test creating a file with root path
        File file = TypeHandler.createFile("/");
        assertNull(file.getParent());
    }
}