package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link TypeHandler}.
 * This class focuses on the createValue methods, which convert strings to various types.
 */
@DisplayName("TypeHandler Value Creation Test")
public class TypeHandlerTest {

    @BeforeAll
    static void setup() {
        // This forces the PatternOptionBuilder to initialize its static members,
        // which in turn registers the default type converters in the TypeHandler.
        // This is required for tests involving PatternOptionBuilder constants.
        @SuppressWarnings("unused")
        final Class<?> loadStatic = PatternOptionBuilder.FILES_VALUE;
    }

    /**
     * Asserts that value creation works for both the modern API {@code createValue(String, Class)}
     * and the deprecated API {@code createValue(String, Object)}.
     *
     * @param str      The input string to parse.
     * @param type     The target type class.
     * @param expected The expected result, or a Throwable class if an exception is expected.
     */
    private void assertValueCreation(final String str, final Class<?> type, final Object expected) {
        // This object is used to test the deprecated createValue(String, Object) method.
        final Object typeAsObjectForDeprecatedApi = type;

        if (expected instanceof Class && Throwable.class.isAssignableFrom((Class<?>) expected)) {
            @SuppressWarnings("unchecked")
            final Class<Throwable> expectedThrowable = (Class<Throwable>) expected;
            assertThrows(expectedThrowable, () -> TypeHandler.createValue(str, type), "createValue(String, Class) should fail");
            assertThrows(expectedThrowable, () -> TypeHandler.createValue(str, typeAsObjectForDeprecatedApi), "createValue(String, Object) should fail");
        } else {
            assertEquals(expected, TypeHandler.createValue(str, type), "createValue(String, Class) should succeed");
            assertEquals(expected, TypeHandler.createValue(str, typeAsObjectForDeprecatedApi), "createValue(String, Object) should succeed");
        }
    }

    // --- Data Providers for Parameterized Tests ---

    private static Stream<Arguments> provideClassAndObjectValues() {
        return Stream.of(
            Arguments.of(Instantiable.class.getName(), PatternOptionBuilder.CLASS_VALUE, Instantiable.class),
            Arguments.of("invalid.class.name", PatternOptionBuilder.CLASS_VALUE, ParseException.class),
            Arguments.of(Instantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, new Instantiable()),
            Arguments.of(NotInstantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, ParseException.class),
            Arguments.of("unknown.class", PatternOptionBuilder.OBJECT_VALUE, ParseException.class)
        );
    }

    private static Stream<Arguments> provideDateValues() {
        // To avoid environment-dependent date parsing, we format a known date and then parse it back.
        final Date date = new Date(1023400137000L); // Jun 06 17:48:57 EDT 2002
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        return Stream.of(
            Arguments.of(dateFormat.format(date), PatternOptionBuilder.DATE_VALUE, date),
            Arguments.of("not a date", PatternOptionBuilder.DATE_VALUE, ParseException.class),
            // This format is not the default and should fail
            Arguments.of("Jun 06 17:48:57 EDT 2002", PatternOptionBuilder.DATE_VALUE, ParseException.class)
        );
    }

    private static Stream<Arguments> provideFileAndPathValues() {
        return Stream.of(
            Arguments.of("some-file.txt", PatternOptionBuilder.FILE_VALUE, new File("some-file.txt")),
            Arguments.of("some-path.txt", Path.class, new File("some-path.txt").toPath()),
            Arguments.of("non-existing.file", PatternOptionBuilder.EXISTING_FILE_VALUE, ParseException.class),
            // PatternOptionBuilder.FILES_VALUE is not registered with a converter, so it should return the original string
            Arguments.of("some.files", PatternOptionBuilder.FILES_VALUE, "some.files")
        );
    }

    private static Stream<Arguments> provideNumericValues() {
        return Stream.of(
            // Integer
            Arguments.of("5", Integer.class, 5),
            Arguments.of("not a number", Integer.class, ParseException.class),
            Arguments.of("5.5", Integer.class, ParseException.class),
            // Long
            Arguments.of("5", Long.class, 5L),
            Arguments.of("not a number", Long.class, ParseException.class),
            // Short
            Arguments.of("5", Short.class, (short) 5),
            Arguments.of(Integer.toString(Integer.MAX_VALUE), Short.class, ParseException.class),
            // Byte
            Arguments.of("5", Byte.class, (byte) 5),
            Arguments.of(Short.toString(Short.MAX_VALUE), Byte.class, ParseException.class),
            // Double
            Arguments.of("5.5", Double.class, 5.5d),
            Arguments.of("not a number", Double.class, ParseException.class),
            // Float
            Arguments.of("5.5", Float.class, 5.5f),
            Arguments.of(Double.toString(Double.MAX_VALUE), Float.class, Float.POSITIVE_INFINITY),
            // BigInteger
            Arguments.of("5", BigInteger.class, new BigInteger("5")),
            Arguments.of("5.5", BigInteger.class, ParseException.class),
            // BigDecimal
            Arguments.of("5.5", BigDecimal.class, new BigDecimal("5.5")),
            // Number (generic)
            Arguments.of("1.5", PatternOptionBuilder.NUMBER_VALUE, 1.5d),
            Arguments.of("15", PatternOptionBuilder.NUMBER_VALUE, 15L),
            Arguments.of("not a number", PatternOptionBuilder.NUMBER_VALUE, ParseException.class)
        );
    }

    private static Stream<Arguments> provideUrlAndStringValues() throws MalformedURLException {
        final String urlString = "https://commons.apache.org";
        return Stream.of(
            Arguments.of("A String", PatternOptionBuilder.STRING_VALUE, "A String"),
            Arguments.of(urlString, PatternOptionBuilder.URL_VALUE, new URL(urlString)),
            Arguments.of("Malformed-url", PatternOptionBuilder.URL_VALUE, ParseException.class)
        );
    }

    private static Stream<Arguments> provideCharacterValues() {
        return Stream.of(
            Arguments.of("c", Character.class, 'c'),
            Arguments.of("character", Character.class, 'c'), // Only the first char is taken
            Arguments.of("5", Character.class, '5'),
            Arguments.of("\\u0124", Character.class, '\u0124')
        );
    }

    // --- Parameterized Tests for createValue ---

    @DisplayName("should create Class and Object values")
    @ParameterizedTest(name = "[{index}] \"{0}\" as {1}")
    @MethodSource("provideClassAndObjectValues")
    void testCreateClassAndObjectValues(final String str, final Class<?> type, final Object expected) {
        assertValueCreation(str, type, expected);
    }

    @DisplayName("should create Date values")
    @ParameterizedTest(name = "[{index}] \"{0}\" as {1}")
    @MethodSource("provideDateValues")
    void testCreateDateValues(final String str, final Class<?> type, final Object expected) {
        assertValueCreation(str, type, expected);
    }

    @DisplayName("should create File and Path values")
    @ParameterizedTest(name = "[{index}] \"{0}\" as {1}")
    @MethodSource("provideFileAndPathValues")
    void testCreateFileAndPathValues(final String str, final Class<?> type, final Object expected) {
        assertValueCreation(str, type, expected);
    }

    @DisplayName("should create numeric values")
    @ParameterizedTest(name = "[{index}] \"{0}\" as {1}")
    @MethodSource("provideNumericValues")
    void testCreateNumericValues(final String str, final Class<?> type, final Object expected) {
        assertValueCreation(str, type, expected);
    }

    @DisplayName("should create URL and String values")
    @ParameterizedTest(name = "[{index}] \"{0}\" as {1}")
    @MethodSource("provideUrlAndStringValues")
    void testCreateUrlAndStringValues(final String str, final Class<?> type, final Object expected) {
        assertValueCreation(str, type, expected);
    }

    @DisplayName("should create Character values")
    @ParameterizedTest(name = "[{index}] \"{0}\" as {1}")
    @MethodSource("provideCharacterValues")
    void testCreateCharacterValues(final String str, final Class<?> type, final Object expected) {
        assertValueCreation(str, type, expected);
    }

    // --- Other TypeHandler Tests ---

    @Test
    @DisplayName("createObject should instantiate a class from its name")
    void testCreateObject() throws ParseException {
        assertTrue(TypeHandler.createObject(Date.class.getName()) instanceof Date);
    }

    private static Stream<Date> createDateFixtures() {
        return Stream.of(
            Date.from(Instant.EPOCH),
            Date.from(Instant.ofEpochSecond(40_000))
        );
    }

    @ParameterizedTest
    @MethodSource("createDateFixtures")
    @DisplayName("createDate should parse its own string representation")
    void testCreateDate(final Date date) {
        assertEquals(date, TypeHandler.createDate(date.toString()));
    }

    // --- Helper Inner Classes for Tests ---

    /** Used for successful Class and Object creation tests. */
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

    /** Used for failed Object creation tests due to a private constructor. */
    public static final class NotInstantiable {
        private NotInstantiable() {
        }
    }
}