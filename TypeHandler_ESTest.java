package org.apache.commons.cli;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link TypeHandler} class.
 * This class tests the type conversion and creation logic for various data types
 * used in command-line option parsing.
 */
public class TypeHandlerTest {

    //region createValue Tests

    @Test
    public void createValue_shouldReturnNull_whenStringValueIsNull() {
        // Act
        Object result = TypeHandler.createValue(null, Object.class);

        // Assert
        assertNull(result);
    }

    @Test
    public void createValue_shouldThrowParseException_whenConvertingEmptyStringToDate() {
        // Arrange
        String invalidDateString = "";

        // Act & Assert
        assertThrows(ParseException.class, () -> TypeHandler.createValue(invalidDateString, Date.class));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void deprecatedCreateValue_shouldReturnInputString_whenTypeObjectIsNull() {
        // Arrange
        String inputValue = "any-string";

        // Act
        Object result = TypeHandler.createValue(inputValue, (Object) null);

        // Assert
        assertEquals(inputValue, result);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void deprecatedCreateValue_shouldThrowParseException_forInvalidDateString() {
        // Arrange
        String invalidDateString = "not-a-date";
        Class<?> dateType = Date.class;

        // Act & Assert
        assertThrows(ParseException.class, () -> TypeHandler.createValue(invalidDateString, dateType));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void deprecatedCreateValue_shouldThrowClassCastException_whenTypeObjectIsNotAClass() {
        // Arrange
        Object notAClass = "I am a string, not a class";

        // Act & Assert
        assertThrows(ClassCastException.class, () -> TypeHandler.createValue("any-string", notAClass));
    }

    //endregion

    //region createNumber Tests

    @Test
    @SuppressWarnings("deprecation")
    public void createNumber_shouldReturnLong_whenInputIsIntegerString() throws ParseException {
        // Arrange
        String numberString = "6";

        // Act
        Number result = TypeHandler.createNumber(numberString);

        // Assert
        assertEquals(6L, result);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void createNumber_shouldThrowParseException_whenInputIsNotANumber() {
        // Arrange
        String notANumber = "not-a-number";

        // Act & Assert
        ParseException exception = assertThrows(ParseException.class, () -> TypeHandler.createNumber(notANumber));
        assertTrue("Cause should be NumberFormatException", exception.getCause() instanceof NumberFormatException);
    }

    //endregion

    //region createClass and createObject Tests

    @Test
    public void createClass_shouldReturnClass_forValidClassName() throws ParseException {
        // Arrange
        String className = "org.apache.commons.cli.ParseException";

        // Act
        Class<?> result = TypeHandler.createClass(className);

        // Assert
        assertEquals(ParseException.class, result);
    }

    @Test
    public void createClass_shouldThrowParseException_forInvalidClassName() {
        // Arrange
        String invalidClassName = "non.existent.class";

        // Act & Assert
        ParseException exception = assertThrows(ParseException.class, () -> TypeHandler.createClass(invalidClassName));
        assertTrue("Cause should be ClassNotFoundException", exception.getCause() instanceof ClassNotFoundException);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void createObject_shouldReturnNewInstance_forValidClassName() throws ParseException {
        // Arrange
        String className = "org.apache.commons.cli.TypeHandler";

        // Act
        Object result = TypeHandler.createObject(className);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TypeHandler);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void createObject_shouldThrowParseException_forNonExistentClass() {
        // Arrange
        String nonExistentClass = "NonExistentClass";

        // Act & Assert
        ParseException exception = assertThrows(ParseException.class, () -> TypeHandler.createObject(nonExistentClass));
        assertTrue("Cause should be ClassNotFoundException", exception.getCause() instanceof ClassNotFoundException);
    }

    //endregion

    //region File and URL Creation Tests

    @Test
    public void createFile_shouldCreateFileObject_forValidPathStrings() {
        // These test cases ensure a File object is created without throwing an exception.
        // The actual existence of the file on the filesystem is not required.
        assertNotNull(TypeHandler.createFile("."));
        assertNotNull(TypeHandler.createFile("/"));
        assertNotNull(TypeHandler.createFile("a/b/c.txt"));
    }

    @Test
    public void createFile_shouldThrowIllegalArgumentException_whenPathIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> TypeHandler.createFile(null));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void openFile_shouldThrowParseException_whenFileDoesNotExist() {
        // Arrange
        String nonExistentFile = "this/file/does/not/exist.txt";

        // Act & Assert
        ParseException exception = assertThrows(ParseException.class, () -> TypeHandler.openFile(nonExistentFile));
        assertTrue("Cause should be FileNotFoundException", exception.getCause() instanceof FileNotFoundException);
    }

    @Test
    public void createURL_shouldThrowParseException_forMalformedURL() {
        // Arrange
        String malformedUrl = "not-a-valid-url";

        // Act & Assert
        ParseException exception = assertThrows(ParseException.class, () -> TypeHandler.createURL(malformedUrl));
        assertTrue("Cause should be MalformedURLException", exception.getCause() instanceof MalformedURLException);
    }

    //endregion

    //region Unsupported Operation Tests

    @Test
    @SuppressWarnings("deprecation")
    public void createFiles_shouldThrowUnsupportedOperationException() {
        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles("any-string"));
    }

    @Test
    public void createDate_shouldThrowExceptionForInvalidDateString() {
        // Note: The original test expected IllegalArgumentException. The Javadoc for createDate
        // suggests UnsupportedOperationException. This test preserves the behavior of the
        // original generated test suite.
        assertThrows(IllegalArgumentException.class, () -> TypeHandler.createDate("not-a-date"));
    }

    //endregion

    //region Handler Instance Tests

    @Test
    public void defaultConstructor_shouldCreateHandlerWithDefaultConverters() {
        // Act
        TypeHandler handler = new TypeHandler();

        // Assert
        // Check for a known default converter to verify initialization.
        assertNotNull("Handler should have a converter for Date class", handler.getConverter(Date.class));
    }

    @Test
    public void getDefault_shouldReturnSharedInstanceWithDefaultConverters() {
        // Act
        TypeHandler defaultHandler = TypeHandler.getDefault();
        Converter<Date, ?> dateConverter = defaultHandler.getConverter(Date.class);

        // Assert
        assertNotNull("Default handler should not be null", defaultHandler);
        assertNotNull("Default handler should have a converter for Date", dateConverter);
    }

    @Test
    public void createDefaultMap_shouldReturnMapWithStandardConverters() {
        // Act
        Map<Class<?>, Converter<?, ? extends Throwable>> defaultMap = TypeHandler.createDefaultMap();

        // Assert
        assertNotNull(defaultMap);
        assertTrue("Default map should contain a converter for Object.class", defaultMap.containsKey(Object.class));
        assertTrue("Default map should contain a converter for Class.class", defaultMap.containsKey(Class.class));
        assertTrue("Default map should contain a converter for Date.class", defaultMap.containsKey(Date.class));
        assertTrue("Default map should contain a converter for URL.class", defaultMap.containsKey(URL.class));
    }

    //endregion
}