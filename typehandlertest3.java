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
import java.time.Instant;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the value creation and type conversion methods in {@link TypeHandler}.
 * This class focuses on verifying the behavior of {@code TypeHandler.createValue(...)}.
 */
public class TypeHandlerValueCreationTest {

    /**
     * Provides valid string inputs and their expected successfully converted objects.
     *
     * @return A stream of arguments for {@link #testCreateValueSuccess(String, Class, Object)}.
     * @throws MalformedURLException if the test URL is invalid.
     */
    private static Stream<Arguments> successfulConversionProvider() throws MalformedURLException {
        // Dates calculated from strings can be locale-dependent. To ensure consistency,
        // we format a fixed Date object into a string and then parse that string.
        final Date date = new Date(1023400137000L);
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        final String urlString = "https://commons.apache.org";

        return Stream.of(
            // String, Class and Object types
            Arguments.of("String", String.class, "String"),
            Arguments.of(Instantiable.class.getName(), Class.class, Instantiable.class),
            Arguments.of(Instantiable.class.getName(), Object.class, new Instantiable()),

            // Date type
            Arguments.of(dateFormat.format(date), Date.class, date),

            // File and Path types
            Arguments.of("some-file.txt", File.class, new File("some-file.txt")),
            Arguments.of("some-path.txt", Path.class, new File("some-path.txt").toPath()),
            // PatternOptionBuilder.FILES_VALUE is not registered, so it should return the raw string
            Arguments.of("some.files", File[].class, "some.files"),

            // Numeric types (Primitives and Wrappers)
            Arguments.of("5", Integer.class, 5),
            Arguments.of("5", Long.class, 5L),
            Arguments.of("5", Short.class, (short) 5),
            Arguments.of("5", Byte.class, (byte) 5),
            Arguments.of("5.5", Double.class, 5.5d),
            Arguments.of("5", Double.class, 5.0d),
            Arguments.of("5.5", Float.class, 5.5f),
            Arguments.of("5", Float.class, 5.0f),
            Arguments.of(Double.toString(Double.MAX_VALUE), Float.class, Float.POSITIVE_INFINITY),

            // Character type
            Arguments.of("just-a-string", Character.class, 'j'),
            Arguments.of("5", Character.class, '5'),
            Arguments.of("5.5", Character.class, '5'),
            Arguments.of("\\u0124", Character.class, '\u0124'),

            // BigNumber types
            Arguments.of("5", BigInteger.class, new BigInteger("5")),
            Arguments.of("5.5", BigDecimal.class, new BigDecimal("5.5")),
            Arguments.of("5", BigDecimal.class, new BigDecimal("5")),

            // Generic Number type
            Arguments.of("1.5", Number.class, 1.5d),
            Arguments.of("15", Number.class, 15L),

            // URL type
            Arguments.of(urlString, URL.class, new URL(urlString))
        );
    }

    /**
     * Provides invalid string inputs that are expected to cause a ParseException.
     *
     * @return A stream of arguments for {@link #testCreateValueFailure(String, Class)}.
     */
    private static Stream<Arguments> failedConversionProvider() {
        return Stream.of(
            // Invalid Class and Object creation
            Arguments.of("not.a.real.Class", Class.class),
            Arguments.of("unknown", Object.class),
            Arguments.of(NotInstantiable.class.getName(), Object.class),

            // Invalid Date formats
            Arguments.of("not a date", Date.class),
            Arguments.of("Jun 06 17:48:57 EDT 2002", Date.class), // Fails without US locale

            // Non-existent file
            Arguments.of("non-existing.file", FileInputStream.class),

            // Invalid numeric conversions
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
            Arguments.of("not a number", Double.class),
            Arguments.of("not a number", Float.class),
            Arguments.of("not a number", BigInteger.class),
            Arguments.of("5.5", BigInteger.class),
            Arguments.of("not a number", BigDecimal.class),
            Arguments.of("not a number", Number.class),

            // Invalid URL
            Arguments.of("Malformed-url", URL.class)
        );
    }

    private static Stream<Date> dateFixtures() {
        return Stream.of(
            Date.from(Instant.EPOCH),
            Date.from(Instant.ofEpochSecond(0)),
            Date.from(Instant.ofEpochSecond(40_000))
        );
    }

    @ParameterizedTest
    @MethodSource("dateFixtures")
    void testCreateDateFromString(final Date date) {
        assertEquals(date, TypeHandler.createDate(date.toString()));
    }

    @ParameterizedTest(name = "[{index}] Input: \"{0}\", Type: {1}")
    @MethodSource("successfulConversionProvider")
    void testCreateValueSuccess(final String input, final Class<?> type, final Object expected) throws ParseException {
        // Test the modern createValue(String, Class) API
        final Object resultFromClass = TypeHandler.createValue(input, type);
        assertEquals(expected, resultFromClass);

        // Also test the deprecated createValue(String, Object) API to ensure backward compatibility.
        // The 'type' is cast to Object to select the correct overload.
        final Object resultFromObject = TypeHandler.createValue(input, (Object) type);
        assertEquals(expected, resultFromObject);
    }

    @ParameterizedTest(name = "[{index}] Input: \"{0}\", Type: {1}")
    @MethodSource("failedConversionProvider")
    void testCreateValueFailure(final String input, final Class<?> type) {
        // Test the modern createValue(String, Class) API
        assertThrows(ParseException.class, () -> TypeHandler.createValue(input, type));

        // Also test the deprecated createValue(String, Object) API to ensure backward compatibility.
        assertThrows(ParseException.class, () -> TypeHandler.createValue(input, (Object) type));
    }

    @Test
    void testCreateFilesThrowsException() {
        // This method is not implemented and is expected to throw.
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles(null));
    }

    /**
     * A simple public class with a default constructor, used for testing object instantiation.
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
     * A class with a private constructor, used for testing failed object instantiation.
     */
    public static final class NotInstantiable {
        private NotInstantiable() {
            // Private constructor to prevent instantiation.
        }
    }
}