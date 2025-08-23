package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link TypeHandler} class.
 * This suite focuses on the static {@code createValue} methods, which convert strings
 * to various data types.
 */
@DisplayName("TypeHandler Conversion Tests")
class TypeHandlerTest {

    static {
        // This is needed to trigger the static initialization of PatternOptionBuilder,
        // which registers the default converters in the TypeHandler's static map.
        // Accessing a static field is a common way to ensure a class is loaded.
        @SuppressWarnings("unused")
        final Class<?> unused = PatternOptionBuilder.CLASS_VALUE;
    }

    //
    // Test cases for successful conversions
    //

    @DisplayName("Should convert string to the expected type successfully")
    @ParameterizedTest(name = "[{index}] \"{0}\" -> ({1}) {2}")
    @MethodSource("provideSuccessfulConversionArguments")
    void testCreateValue_Success(final String input, final Class<?> type, final Object expected) throws ParseException {
        final Object result = TypeHandler.createValue(input, type);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> provideSuccessfulConversionArguments() throws MalformedURLException {
        return Stream.of(
                provideObjectAndClassCases(),
                provideDateCases(),
                provideFileAndUrlCases(),
                provideNumericCases(),
                provideTextualCases())
            .flatMap(s -> s);
    }

    private static Stream<Arguments> provideObjectAndClassCases() {
        return Stream.of(
            Arguments.of(Instantiable.class.getName(), PatternOptionBuilder.CLASS_VALUE, Instantiable.class),
            Arguments.of(Instantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, new Instantiable()),
            Arguments.of("String", PatternOptionBuilder.STRING_VALUE, "String")
        );
    }

    private static Stream<Arguments> provideDateCases() {
        // Dates from strings depend on the environment. To create a reliable test,
        // we format a known Date into a string and then parse that string.
        final Date date = new Date(1023400137000L);
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        return Stream.of(
            Arguments.of(dateFormat.format(date), PatternOptionBuilder.DATE_VALUE, date)
        );
    }

    private static Stream<Arguments> provideFileAndUrlCases() throws MalformedURLException {
        final String urlString = "https://commons.apache.org";
        return Stream.of(
            Arguments.of("some-file.txt", PatternOptionBuilder.FILE_VALUE, new File("some-file.txt")),
            Arguments.of("some-path.txt", Path.class, new File("some-path.txt").toPath()),
            // The FILES_VALUE converter is not registered by default, so it should return the raw string.
            Arguments.of("some.files", PatternOptionBuilder.FILES_VALUE, "some.files"),
            Arguments.of(urlString, PatternOptionBuilder.URL_VALUE, new URL(urlString))
        );
    }

    private static Stream<Arguments> provideNumericCases() {
        return Stream.of(
            // Integer
            Arguments.of("5", Integer.class, 5),
            // Long
            Arguments.of("5", Long.class, 5L),
            // Short
            Arguments.of("5", Short.class, (short) 5),
            // Byte
            Arguments.of("5", Byte.class, (byte) 5),
            // Double
            Arguments.of("5", Double.class, 5d),
            Arguments.of("5.5", Double.class, 5.5d),
            // Float
            Arguments.of("5", Float.class, 5f),
            Arguments.of("5.5", Float.class, 5.5f),
            Arguments.of(Double.toString(Double.MAX_VALUE), Float.class, Float.POSITIVE_INFINITY),
            // BigInteger & BigDecimal
            Arguments.of("5", BigInteger.class, new BigInteger("5")),
            Arguments.of("5", BigDecimal.class, new BigDecimal("5")),
            Arguments.of("5.5", BigDecimal.class, new BigDecimal("5.5")),
            // Generic Number
            Arguments.of("1.5", PatternOptionBuilder.NUMBER_VALUE, 1.5d),
            Arguments.of("15", PatternOptionBuilder.NUMBER_VALUE, 15L)
        );
    }

    private static Stream<Arguments> provideTextualCases() {
        return Stream.of(
            Arguments.of("just-a-string", Character.class, 'j'),
            Arguments.of("5", Character.class, '5'),
            Arguments.of("5.5", Character.class, '5'),
            Arguments.of("\\u0124", Character.class, '\u0124')
        );
    }

    //
    // Test cases for failing conversions
    //

    @DisplayName("Should throw ParseException for invalid input")
    @ParameterizedTest(name = "[{index}] \"{0}\" as {1}")
    @MethodSource("provideFailingConversionArguments")
    void testCreateValue_Failure(final String input, final Class<?> type) {
        assertThrows(ParseException.class, () -> TypeHandler.createValue(input, type));
    }

    private static Stream<Arguments> provideFailingConversionArguments() {
        return Stream.of(
            // Invalid class, date, object
            Arguments.of("what ever", PatternOptionBuilder.CLASS_VALUE),
            Arguments.of("what ever", PatternOptionBuilder.DATE_VALUE),
            Arguments.of("Jun 06 17:48:57 EDT 2002", PatternOptionBuilder.DATE_VALUE),
            Arguments.of(NotInstantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE),
            Arguments.of("unknown", PatternOptionBuilder.OBJECT_VALUE),

            // Invalid file, URL
            Arguments.of("non-existing.file", PatternOptionBuilder.EXISTING_FILE_VALUE),
            Arguments.of("Malformed-url", PatternOptionBuilder.URL_VALUE),

            // Invalid numbers
            Arguments.of("not a number", PatternOptionBuilder.NUMBER_VALUE),
            Arguments.of("just-a-string", Integer.class),
            Arguments.of("5.5", Integer.class),
            Arguments.of(Long.toString(Long.MAX_VALUE), Integer.class),
            Arguments.of("just-a-string", Long.class),
            Arguments.of("5.5", Long.class),
            Arguments.of("just-a-string", Short.class),
            Arguments.of("5.5", Short.class),
            Arguments.of(Integer.toString(Integer.MAX_VALUE), Short.class),
            Arguments.of("just-a-string", Byte.class),
            Arguments.of("5.5", Byte.class),
            Arguments.of(Short.toString(Short.MAX_VALUE), Byte.class),
            Arguments.of("just-a-string", Double.class),
            Arguments.of("just-a-string", Float.class),
            Arguments.of("just-a-string", BigInteger.class),
            Arguments.of("5.5", BigInteger.class),
            Arguments.of("just-a-string", BigDecimal.class)
        );
    }

    //
    // Other specific tests
    //

    @Test
    @DisplayName("Should correctly handle deprecated createValue(String, Object) API")
    void testCreateValue_DeprecatedObjectApi() throws ParseException {
        // Test a success case to ensure the deprecated method delegates correctly
        assertEquals(123, TypeHandler.createValue("123", (Object) Integer.class));

        // Test a failure case to ensure exceptions are propagated
        assertThrows(ParseException.class, () -> TypeHandler.createValue("not-a-number", (Object) Integer.class));
    }

    private static Stream<Date> provideDateFixtures() {
        return Stream.of(
            Date.from(Instant.EPOCH),
            Date.from(Instant.ofEpochSecond(0)),
            Date.from(Instant.ofEpochSecond(40_000))
        );
    }

    @ParameterizedTest
    @MethodSource("provideDateFixtures")
    void testCreateDate(final Date date) {
        assertEquals(date, TypeHandler.createDate(date.toString()));
    }

    /** A custom converter for testing registration. */
    private static final Converter<Path, InvalidPathException> TEST_PATH_CONVERTER = s -> Paths.get("foo");

    @Test
    @DisplayName("Should allow registering and unregistering custom converters")
    void testRegister() {
        final Map<Class<?>, Converter<?, ? extends Throwable>> map = TypeHandler.createDefaultMap();
        final TypeHandler typeHandler = new TypeHandler(map);

        // Initially, the default Path converter should be present
        assertEquals(Converter.PATH, typeHandler.getConverter(Path.class));

        try {
            // Register a custom converter
            map.put(Path.class, TEST_PATH_CONVERTER);
            assertEquals(TEST_PATH_CONVERTER, typeHandler.getConverter(Path.class), "Custom converter should be active");
        } finally {
            // Unregister the custom converter
            map.remove(Path.class);
            assertEquals(Converter.DEFAULT, typeHandler.getConverter(Path.class), "Converter should be removed");
        }
    }

    /** A simple instantiable class for testing Class and Object creation. */
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

    /** A class with a private constructor to test negative creation scenarios. */
    public static final class NotInstantiable {
        private NotInstantiable() {
            // Private constructor prevents instantiation
        }
    }
}