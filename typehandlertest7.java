package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileInputStream;
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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link TypeHandler} class, focusing on value creation and type conversion.
 */
@DisplayName("TypeHandler Value Creation")
public class TypeHandlerTest {

    // --- Test Fixture Helper Classes ---

    /**
     * A simple instantiable class for testing {@link PatternOptionBuilder#CLASS_VALUE} and
     * {@link PatternOptionBuilder#OBJECT_VALUE}.
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
     * A non-instantiable class with a private constructor to test failure scenarios.
     */
    public static final class NotInstantiable {
        private NotInstantiable() {
        }
    }

    // --- Test Data Providers (MethodSource) ---

    private static Stream<Date> dateFixtures() {
        return Stream.of(
            Date.from(Instant.EPOCH),
            Date.from(Instant.ofEpochSecond(0)),
            Date.from(Instant.ofEpochSecond(40_000))
        );
    }

    /**
     * Provides arguments for the main {@link #testCreateValue(String, Class, Object)} test.
     * It combines streams from more specific data provider methods.
     */
    private static Stream<Arguments> createValueTestCases() throws MalformedURLException {
        // This line is a workaround to trigger the static initializer in PatternOptionBuilder,
        // which populates the TypeHandler's converter map.
        @SuppressWarnings("unused")
        final Class<?> loadStatic = PatternOptionBuilder.FILES_VALUE;

        return Stream.of(
            classAndObjectCases(),
            dateCases(),
            fileAndPathCases(),
            numericCases(),
            otherTypeCases()
        ).flatMap(s -> s);
    }

    /** Provides test cases for Class and Object types. */
    private static Stream<Arguments> classAndObjectCases() {
        return Stream.of(
            Arguments.of(Instantiable.class.getName(), PatternOptionBuilder.CLASS_VALUE, Instantiable.class),
            Arguments.of("java.lang.String", PatternOptionBuilder.CLASS_VALUE, String.class),
            Arguments.of("not.a.real.Class", PatternOptionBuilder.CLASS_VALUE, ParseException.class),
            Arguments.of(Instantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, new Instantiable()),
            Arguments.of(NotInstantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, ParseException.class),
            Arguments.of("unknown", PatternOptionBuilder.OBJECT_VALUE, ParseException.class)
        );
    }

    /** Provides test cases for Date types. */
    private static Stream<Arguments> dateCases() {
        // Dates from strings can be locale-dependent. To create a reliable test,
        // we format a known Date object and then parse that same string.
        final Date date = new Date(1023400137000L); // Jun 06 17:48:57 EDT 2002
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        return Stream.of(
            Arguments.of(dateFormat.format(date), PatternOptionBuilder.DATE_VALUE, date),
            Arguments.of("not a date", PatternOptionBuilder.DATE_VALUE, ParseException.class),
            // Fails because the default format requires a timezone.
            Arguments.of("Jun 06 17:48:57 EDT 2002", PatternOptionBuilder.DATE_VALUE, ParseException.class)
        );
    }

    /** Provides test cases for File, Path, and related types. */
    private static Stream<Arguments> fileAndPathCases() {
        return Stream.of(
            Arguments.of("some-file.txt", PatternOptionBuilder.FILE_VALUE, new File("some-file.txt")),
            Arguments.of("some-path.txt", Path.class, new File("some-path.txt").toPath()),
            Arguments.of("non-existing.file", PatternOptionBuilder.EXISTING_FILE_VALUE, ParseException.class),
            // The FILES_VALUE type is not registered by default, so it should return the raw string.
            Arguments.of("some.files", PatternOptionBuilder.FILES_VALUE, "some.files")
        );
    }

    /** Provides test cases for various numeric types. */
    private static Stream<Arguments> numericCases() {
        return Stream.of(
            // Integer
            Arguments.of("5", Integer.class, 5),
            Arguments.of("not a number", Integer.class, ParseException.class),
            Arguments.of("5.5", Integer.class, ParseException.class),
            // Long
            Arguments.of("5", Long.class, 5L),
            Arguments.of("not a number", Long.class, ParseException.class),
            // Double
            Arguments.of("5.5", Double.class, 5.5d),
            Arguments.of("not a number", Double.class, ParseException.class),
            // Float
            Arguments.of("5.5", Float.class, 5.5f),
            Arguments.of(Double.toString(Double.MAX_VALUE), Float.class, Float.POSITIVE_INFINITY),
            // BigInteger
            Arguments.of("12345", BigInteger.class, new BigInteger("12345")),
            Arguments.of("not a number", BigInteger.class, ParseException.class),
            // BigDecimal
            Arguments.of("123.45", BigDecimal.class, new BigDecimal("123.45")),
            Arguments.of("not a number", BigDecimal.class, ParseException.class),
            // Generic Number
            Arguments.of("1.5", PatternOptionBuilder.NUMBER_VALUE, 1.5d),
            Arguments.of("15", PatternOptionBuilder.NUMBER_VALUE, 15L),
            Arguments.of("not a number", PatternOptionBuilder.NUMBER_VALUE, ParseException.class)
        );
    }

    /** Provides test cases for String, URL, and Character types. */
    private static Stream<Arguments> otherTypeCases() throws MalformedURLException {
        final String urlString = "https://commons.apache.org";
        return Stream.of(
            Arguments.of("a string", PatternOptionBuilder.STRING_VALUE, "a string"),
            Arguments.of(urlString, PatternOptionBuilder.URL_VALUE, new URL(urlString)),
            Arguments.of("Malformed-url", PatternOptionBuilder.URL_VALUE, ParseException.class),
            Arguments.of("c", Character.class, 'c'),
            Arguments.of("just a string", Character.class, 'j')
        );
    }

    // --- Test Methods ---

    @ParameterizedTest
    @MethodSource("dateFixtures")
    void testCreateDate(final Date date) {
        assertEquals(date, TypeHandler.createDate(date.toString()));
    }

    @Test
    void testCreateValueForExistingFile() throws Exception {
        final String filePath = "src/test/resources/org/apache/commons/cli/existing-readable.file";
        try (FileInputStream result = TypeHandler.createValue(filePath, PatternOptionBuilder.EXISTING_FILE_VALUE)) {
            assertNotNull(result, "Should return a non-null FileInputStream for an existing file.");
        }
    }

    @ParameterizedTest(name = "[{index}] Parse \"{0}\" as {1} -> {2}")
    @MethodSource("createValueTestCases")
    void testCreateValue(final String str, final Class<?> type, final Object expected) {
        // Test both the modern createValue(String, Class) and the deprecated createValue(String, Object) APIs.
        final Object typeAsObjectForLegacyApi = type;

        if (isExceptionClass(expected)) {
            @SuppressWarnings("unchecked")
            final Class<Throwable> expectedException = (Class<Throwable>) expected;
            assertThrows(expectedException, () -> TypeHandler.createValue(str, type), "Modern API should throw");
            assertThrows(expectedException, () -> TypeHandler.createValue(str, typeAsObjectForLegacyApi), "Legacy API should throw");
        } else {
            assertEquals(expected, TypeHandler.createValue(str, type), "Modern API should produce correct value");
            assertEquals(expected, TypeHandler.createValue(str, typeAsObjectForLegacyApi), "Legacy API should produce correct value");
        }
    }

    private boolean isExceptionClass(Object obj) {
        return obj instanceof Class<?> && Throwable.class.isAssignableFrom((Class<?>) obj);
    }
}