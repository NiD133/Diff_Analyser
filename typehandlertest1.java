package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link TypeHandler}.
 *
 * <p>
 * This test class focuses on the {@link TypeHandler#createValue(String, Class)} method,
 * verifying its ability to convert strings to various types and to correctly handle invalid inputs.
 * Tests are separated into successful conversions and expected failures for clarity.
 * </p>
 */
public class TypeHandlerTest {

    /**
     * Provides arguments for successful type conversion tests.
     * Each argument consists of an input string, a target type, and the expected converted object.
     *
     * @return A stream of arguments for parameterized tests.
     * @throws MalformedURLException if the URL for the test is invalid.
     */
    private static Stream<Arguments> successfulConversionArguments() throws MalformedURLException {
        // This static initialization is needed to populate the TypeHandler's internal map.
        @SuppressWarnings("unused")
        final Class<?> loadStatic = PatternOptionBuilder.FILES_VALUE;

        // Dates calculated from strings can be locale-dependent. To ensure consistency,
        // we format a fixed Date object into a string and then use that string for the test.
        final Date date = new Date(1023400137000L);
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        final String urlString = "https://commons.apache.org";

        return Stream.of(
            // Class
            Arguments.of(Instantiable.class.getName(), PatternOptionBuilder.CLASS_VALUE, Instantiable.class),
            // Date
            Arguments.of(dateFormat.format(date), PatternOptionBuilder.DATE_VALUE, date),
            // File
            Arguments.of("some-file.txt", PatternOptionBuilder.FILE_VALUE, new File("some-file.txt")),
            // Path
            Arguments.of("some-path.txt", Path.class, new File("some-path.txt").toPath()),
            // Files (not a registered type, should return the string)
            Arguments.of("some.files", PatternOptionBuilder.FILES_VALUE, "some.files"),
            // Numbers (Integer, Long, Short, Byte)
            Arguments.of("5", Integer.class, 5),
            Arguments.of("5", Long.class, 5L),
            Arguments.of("5", Short.class, (short) 5),
            Arguments.of("5", Byte.class, (byte) 5),
            // Character
            Arguments.of("just-a-string", Character.class, 'j'),
            Arguments.of("5", Character.class, '5'),
            Arguments.of("5.5", Character.class, '5'),
            Arguments.of("\\u0124", Character.class, '\u0124'),
            // Floating point numbers (Double, Float)
            Arguments.of("5", Double.class, 5d),
            Arguments.of("5.5", Double.class, 5.5d),
            Arguments.of("5", Float.class, 5f),
            Arguments.of("5.5", Float.class, 5.5f),
            Arguments.of(Double.toString(Double.MAX_VALUE), Float.class, Float.POSITIVE_INFINITY),
            // Big Numbers (BigInteger, BigDecimal)
            Arguments.of("5", BigInteger.class, new BigInteger("5")),
            Arguments.of("5", BigDecimal.class, new BigDecimal("5")),
            Arguments.of("5.5", BigDecimal.class, new BigDecimal("5.5")),
            // Generic Number
            Arguments.of("1.5", PatternOptionBuilder.NUMBER_VALUE, 1.5d),
            Arguments.of("15", PatternOptionBuilder.NUMBER_VALUE, 15L),
            // Object
            Arguments.of(Instantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, new Instantiable()),
            // String
            Arguments.of("String", PatternOptionBuilder.STRING_VALUE, "String"),
            // URL
            Arguments.of(urlString, PatternOptionBuilder.URL_VALUE, new URL(urlString))
        );
    }

    /**
     * Provides arguments for failed type conversion tests.
     * Each argument consists of an input string and a target type that should cause a ParseException.
     *
     * @return A stream of arguments for parameterized tests.
     */
    private static Stream<Arguments> failedConversionArguments() {
        return Stream.of(
            // Class
            Arguments.of("not.a.real.class", PatternOptionBuilder.CLASS_VALUE),
            // Date
            Arguments.of("not a date", PatternOptionBuilder.DATE_VALUE),
            Arguments.of("Jun 06 17:48:57 EDT 2002", PatternOptionBuilder.DATE_VALUE), // Invalid format
            // Existing File
            Arguments.of("non-existing.file", PatternOptionBuilder.EXISTING_FILE_VALUE),
            // Numbers (Integer, Long, Short, Byte)
            Arguments.of("not a number", Integer.class),
            Arguments.of("5.5", Integer.class),
            Arguments.of(Long.toString(Long.MAX_VALUE), Integer.class),
            Arguments.of("not a number", Long.class),
            Arguments.of("5.5", Long.class),
            Arguments.of("not a number", Short.class),
            Arguments.of("5.5", Short.class),
            Arguments.of(Integer.toString(Integer.MAX_VALUE), Short.class),
            Arguments.of("not a number", Byte.class),
            Arguments.of("5.5", Byte.class),
            Arguments.of(Short.toString(Short.MAX_VALUE), Byte.class),
            // Floating point numbers (Double, Float)
            Arguments.of("not a number", Double.class),
            Arguments.of("not a number", Float.class),
            // Big Numbers (BigInteger, BigDecimal)
            Arguments.of("not a number", BigInteger.class),
            Arguments.of("5.5", BigInteger.class),
            Arguments.of("not a number", BigDecimal.class),
            // Generic Number
            Arguments.of("not a number", PatternOptionBuilder.NUMBER_VALUE),
            // Object
            Arguments.of(NotInstantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE),
            Arguments.of("unknown", PatternOptionBuilder.OBJECT_VALUE),
            // URL
            Arguments.of("Malformed-url", PatternOptionBuilder.URL_VALUE)
        );
    }

    @ParameterizedTest(name = "[{index}] \"{0}\" successfully converts to type {1}")
    @MethodSource("successfulConversionArguments")
    @SuppressWarnings("deprecation") // Testing a deprecated method
    void testCreateValue_SuccessfulConversion(final String input, final Class<?> type, final Object expected) throws ParseException {
        // Test the primary API: createValue(String, Class)
        assertEquals(expected, TypeHandler.createValue(input, type));

        // Test the deprecated API: createValue(String, Object)
        // The 'type' parameter is passed as an Object to select the correct overload.
        assertEquals(expected, TypeHandler.createValue(input, (Object) type));
    }

    @ParameterizedTest(name = "[{index}] \"{0}\" fails to convert to type {1}")
    @MethodSource("failedConversionArguments")
    @SuppressWarnings("deprecation") // Testing a deprecated method
    void testCreateValue_FailedConversion(final String input, final Class<?> type) {
        // Test the primary API: createValue(String, Class)
        assertThrows(ParseException.class, () -> TypeHandler.createValue(input, type));

        // Test the deprecated API: createValue(String, Object)
        // The 'type' parameter is passed as an Object to select the correct overload.
        assertThrows(ParseException.class, () -> TypeHandler.createValue(input, (Object) type));
    }

    @Test
    void testCreateClass() throws ParseException {
        final Class<?> cls = getClass();
        assertEquals(cls, TypeHandler.createClass(cls.getName()));
    }

    @Test
    void testCreateDate() {
        final Date now = new Date();
        assertEquals(now, TypeHandler.createDate(now.toString()));
    }

    /**
     * A simple helper class that can be instantiated for testing object creation.
     */
    public static class Instantiable {
        @Override
        public boolean equals(final Object obj) {
            return obj instanceof Instantiable;
        }

        @Override
        public int hashCode() {
            return Instantiable.class.hashCode();
        }
    }

    /**
     * A helper class with a private constructor to test failed object creation.
     */
    public static final class NotInstantiable {
        private NotInstantiable() {
        }
    }
}