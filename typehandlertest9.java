package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link TypeHandler} class, focusing on value creation and type conversion.
 */
@DisplayName("TypeHandler Value Creation Tests")
public class TypeHandlerTest {

    // --- Test Fixture Data Providers ---

    private static Stream<Arguments> classAndObjectValueProvider() {
        return Stream.of(
            // Success cases
            Arguments.of("Creates an instance of a class", Instantiable.class.getName(), PatternOptionBuilder.CLASS_VALUE, Instantiable.class),
            Arguments.of("Creates an instance of an object", Instantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, new Instantiable()),

            // Failure cases
            Arguments.of("Fails for non-existent class", "a.bad.class.Name", PatternOptionBuilder.CLASS_VALUE, ParseException.class),
            Arguments.of("Fails for non-instantiable class", NotInstantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, ParseException.class),
            Arguments.of("Fails for unknown class name", "unknown", PatternOptionBuilder.OBJECT_VALUE, ParseException.class)
        );
    }

    private static Stream<Arguments> dateValueProvider() {
        /*
         * Dates from strings depend on the test environment. To ensure consistency,
         * we format a known Date into a string and then parse it back.
         */
        final Date date = new Date(1023400137000L);
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        return Stream.of(
            // Success case
            Arguments.of("Parses a valid date string", dateFormat.format(date), PatternOptionBuilder.DATE_VALUE, date),

            // Failure cases
            Arguments.of("Fails for an unparseable date string", "what ever", PatternOptionBuilder.DATE_VALUE, ParseException.class),
            Arguments.of("Fails for a differently formatted date string", "Jun 06 17:48:57 EDT 2002", PatternOptionBuilder.DATE_VALUE, ParseException.class)
        );
    }

    private static Stream<Arguments> fileAndPathValueProvider() {
        return Stream.of(
            // Success cases
            Arguments.of("Creates a File object", "some-file.txt", PatternOptionBuilder.FILE_VALUE, new File("some-file.txt")),
            Arguments.of("Creates a Path object", "some-path.txt", Path.class, new File("some-path.txt").toPath()),
            // The FILES_VALUE is not registered by default, so it should just return the string
            Arguments.of("Returns string for unregistered FILES_VALUE", "some.files", PatternOptionBuilder.FILES_VALUE, "some.files"),

            // Failure case
            Arguments.of("Fails if file must exist but doesn't", "non-existing.file", PatternOptionBuilder.EXISTING_FILE_VALUE, ParseException.class)
        );
    }

    private static Stream<Arguments> numberValueProvider() {
        return Stream.of(
            // Generic Number
            Arguments.of("Creates a Double for number with decimal", "1.5", PatternOptionBuilder.NUMBER_VALUE, 1.5d),
            Arguments.of("Creates a Long for number without decimal", "15", PatternOptionBuilder.NUMBER_VALUE, 15L),
            Arguments.of("Fails for a non-numeric string", "not a number", PatternOptionBuilder.NUMBER_VALUE, ParseException.class),

            // Integer
            Arguments.of("Converts '5' to Integer", "5", Integer.class, 5),
            Arguments.of("Fails to convert non-integer string to Integer", "just-a-string", Integer.class, ParseException.class),
            Arguments.of("Fails to convert decimal string to Integer", "5.5", Integer.class, ParseException.class),
            Arguments.of("Fails to convert out-of-range value to Integer", Long.toString(Long.MAX_VALUE), Integer.class, ParseException.class),

            // Long
            Arguments.of("Converts '5' to Long", "5", Long.class, 5L),
            Arguments.of("Fails to convert non-integer string to Long", "just-a-string", Long.class, ParseException.class),
            Arguments.of("Fails to convert decimal string to Long", "5.5", Long.class, ParseException.class),

            // Double
            Arguments.of("Converts '5.5' to Double", "5.5", Double.class, 5.5d),
            Arguments.of("Fails to convert non-numeric string to Double", "just-a-string", Double.class, ParseException.class),

            // Float
            Arguments.of("Converts '5.5' to Float", "5.5", Float.class, 5.5f),
            Arguments.of("Handles overflow by returning Infinity for Float", Double.toString(Double.MAX_VALUE), Float.class, Float.POSITIVE_INFINITY),
            Arguments.of("Fails to convert non-numeric string to Float", "just-a-string", Float.class, ParseException.class),

            // BigInteger
            Arguments.of("Converts '5' to BigInteger", "5", BigInteger.class, new BigInteger("5")),
            Arguments.of("Fails to convert decimal string to BigInteger", "5.5", BigInteger.class, ParseException.class),

            // BigDecimal
            Arguments.of("Converts '5.5' to BigDecimal", "5.5", BigDecimal.class, new BigDecimal("5.5"))
        );
    }

    private static Stream<Arguments> urlAndStringValueProvider() throws MalformedURLException {
        final String urlString = "https://commons.apache.org";
        return Stream.of(
            // Success cases
            Arguments.of("Returns the input string for STRING_VALUE", "String", PatternOptionBuilder.STRING_VALUE, "String"),
            Arguments.of("Creates a URL from a valid string", urlString, PatternOptionBuilder.URL_VALUE, new URL(urlString)),

            // Failure case
            Arguments.of("Fails to create a URL from a malformed string", "Malformed-url", PatternOptionBuilder.URL_VALUE, ParseException.class)
        );
    }

    private static Stream<Arguments> characterValueProvider() {
        return Stream.of(
            Arguments.of("Takes first char of a string", "just-a-string", Character.class, 'j'),
            Arguments.of("Takes first char of a numeric string", "5.5", Character.class, '5'),
            Arguments.of("Parses unicode character", "\\u0124", Character.class, '\u0124')
        );
    }

    private static Stream<Date> dateFixturesProvider() {
        return Stream.of(Date.from(Instant.EPOCH), new Date(0), new Date(40_000_000));
    }

    // --- Setup ---

    @BeforeAll
    static void setupTypeHandler() {
        // This static initialization is required to populate the TypeHandler's converter map
        // with the default converters used by PatternOptionBuilder.
        @SuppressWarnings("unused")
        final Class<?> forceInit = PatternOptionBuilder.FILES_VALUE;
    }

    // --- Test Methods ---

    /**
     * Asserts that {@link TypeHandler#createValue(String, Class)} and the deprecated
     * {@link TypeHandler#createValue(String, Object)} behave as expected.
     *
     * @param input    The string value to be converted.
     * @param type     The target type for the conversion.
     * @param expected The expected outcome: either the converted value or the class of the exception to be thrown.
     */
    private void assertValueCreation(final String input, final Class<?> type, final Object expected) throws ParseException {
        // The 'objectApiType' variable is used to test the deprecated createValue(String, Object) method.
        final Object objectApiType = type;

        if (expected instanceof Class<?> && Throwable.class.isAssignableFrom((Class<?>) expected)) {
            @SuppressWarnings("unchecked")
            final Class<Throwable> exceptionClass = (Class<Throwable>) expected;
            assertThrows(exceptionClass, () -> TypeHandler.createValue(input, type), "Modern API should throw");
            assertThrows(exceptionClass, () -> TypeHandler.createValue(input, objectApiType), "Deprecated API should throw");
        } else {
            assertEquals(expected, TypeHandler.createValue(input, type), "Modern API should produce correct value");
            assertEquals(expected, TypeHandler.createValue(input, objectApiType), "Deprecated API should produce correct value");
        }
    }

    @DisplayName("createValue should convert to Class and Object types")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("classAndObjectValueProvider")
    void testCreateValueForClassAndObject(String desc, String input, Class<?> type, Object expected) throws ParseException {
        assertValueCreation(input, type, expected);
    }

    @DisplayName("createValue should convert to Date type")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("dateValueProvider")
    void testCreateValueForDate(String desc, String input, Class<?> type, Object expected) throws ParseException {
        assertValueCreation(input, type, expected);
    }

    @DisplayName("createValue should convert to File and Path types")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("fileAndPathValueProvider")
    void testCreateValueForFileAndPath(String desc, String input, Class<?> type, Object expected) throws ParseException {
        assertValueCreation(input, type, expected);
    }

    @DisplayName("createValue should convert to various Number types")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("numberValueProvider")
    void testCreateValueForNumbers(String desc, String input, Class<?> type, Object expected) throws ParseException {
        assertValueCreation(input, type, expected);
    }

    @DisplayName("createValue should convert to URL and String types")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("urlAndStringValueProvider")
    void testCreateValueForUrlAndString(String desc, String input, Class<?> type, Object expected) throws ParseException {
        assertValueCreation(input, type, expected);
    }



    @DisplayName("createValue should convert to Character type")
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("characterValueProvider")
    void testCreateValueForCharacter(String desc, String input, Class<?> type, Object expected) throws ParseException {
        assertValueCreation(input, type, expected);
    }

    @DisplayName("createDate should parse a standard date string representation")
    @ParameterizedTest
    @MethodSource("dateFixturesProvider")
    void testCreateDate(final Date date) {
        assertEquals(date, TypeHandler.createDate(date.toString()));
    }

    @Test
    @DisplayName("openFile should return a FileInputStream for a readable file")
    void testOpenFile() throws IOException {
        final String filePath = "src/test/resources/org/apache/commons/cli/existing-readable.file";
        try (FileInputStream fis = TypeHandler.openFile(filePath)) {
            // Verifying the stream is open and readable by consuming its content
            IOUtils.consume(fis);
        }
    }

    // --- Helper classes for testing instantiation ---

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