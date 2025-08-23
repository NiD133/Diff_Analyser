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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link TypeHandler} class, focusing on its value creation capabilities.
 */
@DisplayName("TypeHandler Value Creation")
public class TypeHandlerTest {

    /**
     * The PatternOptionBuilder class adds additional type converters to the static
     * TypeHandler map. This test class requires these converters to be registered,
     * so we trigger the static initializer of PatternOptionBuilder before any tests run.
     */
    @BeforeAll
    public static void setup() {
        try {
            Class.forName(PatternOptionBuilder.class.getName());
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException("Failed to initialize PatternOptionBuilder", e);
        }
    }

    // Method sources are static and must be at the top level or in a top-level companion class.
    // They are referenced from the @Nested test classes below.

    static Stream<Arguments> provideSuccessfulConversionArguments() throws MalformedURLException {
        final Date date = new Date(1023400137000L);
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        final String urlString = "https://commons.apache.org";

        return Stream.of(
            // Class
            Arguments.of("Class conversion", Instantiable.class.getName(), PatternOptionBuilder.CLASS_VALUE, Instantiable.class),
            // Date
            Arguments.of("Date conversion", dateFormat.format(date), PatternOptionBuilder.DATE_VALUE, date),
            // File system
            Arguments.of("File conversion", "some-file.txt", PatternOptionBuilder.FILE_VALUE, new File("some-file.txt")),
            Arguments.of("Path conversion", "some-path.txt", Path.class, new File("some-path.txt").toPath()),
            // Numbers (Integer, Long, Short, Byte)
            Arguments.of("Integer conversion", "5", Integer.class, 5),
            Arguments.of("Long conversion", "5", Long.class, 5L),
            Arguments.of("Short conversion", "5", Short.class, (short) 5),
            Arguments.of("Byte conversion", "5", Byte.class, (byte) 5),
            // Character
            Arguments.of("Character from string", "just-a-string", Character.class, 'j'),
            Arguments.of("Character from number", "5", Character.class, '5'),
            Arguments.of("Character from unicode", "\\u0124", Character.class, '\u0124'),
            // Floating point numbers (Double, Float)
            Arguments.of("Double from integer string", "5", Double.class, 5d),
            Arguments.of("Double from float string", "5.5", Double.class, 5.5),
            Arguments.of("Float from integer string", "5", Float.class, 5f),
            Arguments.of("Float from float string", "5.5", Float.class, 5.5f),
            Arguments.of("Float from max double", Double.toString(Double.MAX_VALUE), Float.class, Float.POSITIVE_INFINITY),
            // Big numbers (BigInteger, BigDecimal)
            Arguments.of("BigInteger conversion", "5", BigInteger.class, new BigInteger("5")),
            Arguments.of("BigDecimal from integer string", "5", BigDecimal.class, new BigDecimal("5")),
            Arguments.of("BigDecimal from float string", "5.5", BigDecimal.class, new BigDecimal("5.5")),
            // Generic Number
            Arguments.of("Number as Double", "1.5", PatternOptionBuilder.NUMBER_VALUE, 1.5d),
            Arguments.of("Number as Long", "15", PatternOptionBuilder.NUMBER_VALUE, 15L),
            // Object & String
            Arguments.of("Object conversion", Instantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, new Instantiable()),
            Arguments.of("String conversion", "String", PatternOptionBuilder.STRING_VALUE, "String"),
            // URL
            Arguments.of("URL conversion", urlString, PatternOptionBuilder.URL_VALUE, new URL(urlString))
        );
    }

    static Stream<Arguments> provideFailedConversionArguments() {
        return Stream.of(
            // Class
            Arguments.of("Invalid class name", "what ever", PatternOptionBuilder.CLASS_VALUE),
            // Date
            Arguments.of("Invalid date format", "what ever", PatternOptionBuilder.DATE_VALUE),
            Arguments.of("Locale-dependent date", "Jun 06 17:48:57 EDT 2002", PatternOptionBuilder.DATE_VALUE),
            // File
            Arguments.of("Non-existent file", "non-existing.file", PatternOptionBuilder.EXISTING_FILE_VALUE),
            // Numbers
            Arguments.of("Invalid Integer", "just-a-string", Integer.class),
            Arguments.of("Integer from float string", "5.5", Integer.class),
            Arguments.of("Integer from long string", Long.toString(Long.MAX_VALUE), Integer.class),
            Arguments.of("Invalid Long", "just-a-string", Long.class),
            Arguments.of("Long from float string", "5.5", Long.class),
            Arguments.of("Invalid Short", "just-a-string", Short.class),
            Arguments.of("Short from float string", "5.5", Short.class),
            Arguments.of("Short from integer string", Integer.toString(Integer.MAX_VALUE), Short.class),
            Arguments.of("Invalid Byte", "just-a-string", Byte.class),
            Arguments.of("Byte from float string", "5.5", Byte.class),
            Arguments.of("Byte from short string", Short.toString(Short.MAX_VALUE), Byte.class),
            // Floating point
            Arguments.of("Invalid Double", "just-a-string", Double.class),
            Arguments.of("Invalid Float", "just-a-string", Float.class),
            // Big numbers
            Arguments.of("Invalid BigInteger", "just-a-string", BigInteger.class),
            Arguments.of("BigInteger from float string", "5.5", BigInteger.class),
            Arguments.of("Invalid BigDecimal", "just-a-string", BigDecimal.class),
            // Generic Number
            Arguments.of("Invalid Number", "not a number", PatternOptionBuilder.NUMBER_VALUE),
            // Object
            Arguments.of("Non-instantiable Object", NotInstantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE),
            Arguments.of("Unknown Object", "unknown", PatternOptionBuilder.OBJECT_VALUE),
            // URL
            Arguments.of("Malformed URL", "Malformed-url", PatternOptionBuilder.URL_VALUE)
        );
    }

    @Nested
    @DisplayName("Successful Conversions")
    class SuccessfulConversions {
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.commons.cli.TypeHandlerTest#provideSuccessfulConversionArguments")
        void createsValueSuccessfully(final String displayName, final String input, final Class<?> type, final Object expected) throws ParseException {
            assertEquals(expected, TypeHandler.createValue(input, type));
        }
    }

    @Nested
    @DisplayName("Failed Conversions")
    class FailedConversions {
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.commons.cli.TypeHandlerTest#provideFailedConversionArguments")
        void throwsParseExceptionOnInvalidInput(final String displayName, final String input, final Class<?> type) {
            assertThrows(ParseException.class, () -> TypeHandler.createValue(input, type));
        }
    }

    @Nested
    @DisplayName("Other Behavior Tests")
    class OtherBehaviorTests {
        private static Stream<Date> provideDateFixtures() {
            return Stream.of(Date.from(Instant.EPOCH), new Date(0), new Date(40_000_000));
        }

        @DisplayName("createDate should correctly parse its own string output")
        @ParameterizedTest
        @MethodSource("provideDateFixtures")
        void testCreateDate(final Date date) {
            assertEquals(date, TypeHandler.createDate(date.toString()));
        }

        @Test
        @DisplayName("Should return original string for unregistered type")
        void testUnregisteredTypeReturnsString() throws ParseException {
            // PatternOptionBuilder.FILES_VALUE is not registered with a converter,
            // so TypeHandler should fall back to returning the original string.
            final String input = "some.files";
            assertEquals(input, TypeHandler.createValue(input, PatternOptionBuilder.FILES_VALUE));
        }

        @Test
        @DisplayName("Deprecated createValue(String, Object) should still function")
        void testDeprecatedCreateValueApi() throws ParseException {
            // A simple smoke test to ensure the deprecated API still works as expected.
            final Object integerType = Integer.class;
            assertEquals(5, TypeHandler.createValue("5", integerType));
            assertThrows(ParseException.class, () -> TypeHandler.createValue("not-a-number", integerType));
        }
    }

    /**
     * A simple, instantiable class for testing object creation.
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
     * A class with a private constructor to test failed object instantiation.
     */
    public static final class NotInstantiable {
        private NotInstantiable() {
        }
    }
}