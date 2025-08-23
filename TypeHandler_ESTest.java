package org.apache.commons.cli;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for TypeHandler.
 *
 * Notes:
 * - Avoids EvoSuite runner/scaffolding and brittle message assertions.
 * - Uses descriptive test names and groups by API method.
 * - Prefers Assert.assertThrows (JUnit 4.13+) for exception testing.
 */
public class TypeHandlerTest {

    // -------------------------------
    // createNumber(String)
    // -------------------------------

    @Test
    public void createNumber_integerString_returnsLong() throws Exception {
        Number number = TypeHandler.createNumber("6");
        assertTrue(number instanceof Long);
        assertEquals(6L, number.longValue());
    }

    @Test
    public void createNumber_nonNumeric_throwsParseException() {
        assertThrows(ParseException.class, () -> TypeHandler.createNumber("not-a-number"));
    }

    // -------------------------------
    // createValue(String, Class)
    // -------------------------------

    @Test
    public void createValue_nullStringAndType_returnsNull() throws Exception {
        Date value = TypeHandler.createValue(null, Date.class);
        assertNull(value);
    }

    @Test
    public void createValue_dateClass_withInvalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> TypeHandler.createValue("not-a-date", Date.class));
    }

    // -------------------------------
    // createValue(String, Object) [deprecated]
    // -------------------------------

    @Test
    public void createValue_deprecated_nullTargetType_returnsOriginalString() throws Exception {
        Object value = TypeHandler.createValue("keep-me", (Object) null);
        assertEquals("keep-me", value);
    }

    @Test
    public void createValue_deprecated_nonClassTargetType_throwsClassCastException() {
        assertThrows(ClassCastException.class, () -> TypeHandler.createValue("value", (Object) "not-a-class"));
    }

    // -------------------------------
    // createClass(String)
    // -------------------------------

    @Test
    public void createClass_withValidName_returnsClass() throws Exception {
        Class<?> clazz = TypeHandler.createClass("org.apache.commons.cli.ParseException");
        assertEquals(ParseException.class, clazz);
        assertFalse(clazz.isArray());
    }

    @Test
    public void createClass_withInvalidName_throwsParseException() {
        assertThrows(ParseException.class, () -> TypeHandler.createClass("does.not.Exist"));
    }

    @Test
    public void createClass_forInterface_hasInterfaceModifier() throws Exception {
        Class<?> clazz = TypeHandler.createClass("org.apache.commons.cli.Converter");
        assertTrue(Modifier.isInterface(clazz.getModifiers()));
        assertTrue(Modifier.isPublic(clazz.getModifiers()));
    }

    // -------------------------------
    // createURL(String)
    // -------------------------------

    @Test
    public void createURL_withMalformedUrl_throwsParseException() {
        assertThrows(ParseException.class, () -> TypeHandler.createURL("not-a-url"));
    }

    // -------------------------------
    // createObject(String) [deprecated]
    // -------------------------------

    @Test
    public void createObject_knownType_returnsInstance() throws Exception {
        Object instance = TypeHandler.createObject("org.apache.commons.cli.TypeHandler");
        assertNotNull(instance);
        assertTrue(instance instanceof TypeHandler);
    }

    @Test
    public void createObject_unknownType_throwsParseException() {
        assertThrows(ParseException.class, () -> TypeHandler.createObject("no.such.ClassName"));
    }

    // -------------------------------
    // createFile(String)
    // -------------------------------

    @Test
    public void createFile_nonExistingPath_returnsFileObject() {
        File file = TypeHandler.createFile("some-path-that-should-not-exist-12345.txt");
        assertNotNull(file);
        assertEquals("some-path-that-should-not-exist-12345.txt", file.getPath());
        assertFalse(file.exists());
        assertFalse(file.isDirectory());
    }

    // -------------------------------
    // openFile(String) [deprecated]
    // -------------------------------

    @Test
    public void openFile_missingFile_throwsParseException() {
        assertThrows(ParseException.class, () -> {
            try (FileInputStream ignored = TypeHandler.openFile("definitely-missing-file-98765.txt")) {
                // no-op
            }
        });
    }

    // -------------------------------
    // createFiles(String) [deprecated and unsupported]
    // -------------------------------

    @Test
    public void createFiles_alwaysThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles("a,b,c"));
    }

    // -------------------------------
    // Default converter map and TypeHandler instances
    // -------------------------------

    @Test
    public void createDefaultMap_returnsNonEmptyMap() {
        Map<Class<?>, Converter<?, ? extends Throwable>> map = TypeHandler.createDefaultMap();
        assertNotNull(map);
        assertFalse("Default converter map should not be empty", map.isEmpty());
    }

    @Test
    public void getDefault_returnsHandlerWithDateConverter() {
        Converter<Date, ?> converter = TypeHandler.getDefault().getConverter(Date.class);
        assertNotNull(converter);
    }

    @Test
    public void constructor_defaultAndWithMap_succeed() {
        TypeHandler handler1 = new TypeHandler();
        assertNotNull(handler1);

        Map<Class<?>, Converter<?, ? extends Throwable>> map = TypeHandler.createDefaultMap();
        TypeHandler handler2 = new TypeHandler(map);
        assertNotNull(handler2);
    }
}