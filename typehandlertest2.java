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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link TypeHandler} class, focusing on its ability to convert string
 * arguments into various typed objects.
 */
@DisplayName("TypeHandler Tests")
class TypeHandlerTest {

    static {
        // This is required to trigger the static initializer in PatternOptionBuilder,
        // which registers several default type converters in the TypeHandler.
        // It ensures that types like DATE_VALUE, URL_VALUE, etc., are recognized.
        @SuppressWarnings("unused")
        final Class<?> unused = PatternOptionBuilder.class;
    }

    /**
     * Tests for the primary conversion method, {@link TypeHandler#createValue(String, Class)}.
     * It also verifies the behavior of the deprecated {@link TypeHandler#createValue(String, Object)}
     * to ensure backward compatibility.
     */
    @Nested
    @DisplayName("createValue() Tests")
    class CreateValueTest {

        /**
         * Verifies that createValue correctly converts a string to the specified type or
         * throws a ParseException for invalid input.
         *
         * @param inputString The raw string to be converted.
         * @param targetType  The class of the target type.
         * @param expected    The expected object after conversion, or the expected exception class.
         */
        @ParameterizedTest(name = "[{index}] Input: \"{0}\", Type: {1}")
        @MethodSource({
            "org.apache.commons.cli.TypeHandlerTest#classAndObjectProvider",
            "org.apache.commons.cli.TypeHandlerTest#dateProvider",
            "org.apache.commons.cli.TypeHandlerTest#fileAndPathProvider",
            "org.apache.commons.cli.TypeHandlerTest#numericTypesProvider",
            "org.apache.commons.cli.TypeHandlerTest#bigNumberTypesProvider",
            "org.apache.commons.cli.TypeHandlerTest#genericNumberProvider",
            "org.apache.commons.cli.TypeHandlerTest#miscTypesProvider"
        })
        void convertsStringToTypedValueOrThrowsException(final String inputString, final Class<?> targetType, final Object expected) {
            if (isException(expected)) {
                @SuppressWarnings("unchecked")
                final Class<? extends Throwable> expectedException = (Class<? extends Throwable>) expected;

                // Test the primary API: createValue(String, Class)
                assertThrows(expectedException, () -> TypeHandler.createValue(inputString, targetType),
                    "Should throw for modern API");

                // Test the deprecated API: createValue(String, Object)
                assertThrows(expectedException, () -> TypeHandler.createValue(inputString, (Object) targetType),
                    "Should throw for deprecated API");
            } else {
                // Test the primary API: createValue(String, Class)
                assertEquals(expected, TypeHandler.createValue(inputString, targetType),
                    "Should convert correctly for modern API");

                // Test the deprecated API: createValue(String, Object)
                assertEquals(expected, TypeHandler.createValue(inputString, (Object) targetType),
                    "Should convert correctly for deprecated API");
            }
        }

        private boolean isException(Object obj) {
            return obj instanceof Class && Throwable.class.isAssignableFrom((Class<?>) obj);
        }
    }

    /**
     * Contains tests for older, direct creation methods that are now deprecated.
     */
    @Nested
    @DisplayName("Deprecated Direct Creation Method Tests")
    class DeprecatedDirectCreationMethodsTest {

        @Test
        @DisplayName("createFile() should handle absolute paths")
        void createFileWithAbsolutePath() {
            final File file = new File("").getAbsoluteFile();
            assertEquals(file, TypeHandler.createFile(file.toString()));
        }
    }

    // --- Test Data Provider Methods ---

    static Stream<Arguments> classAndObjectProvider() {
        return Stream.of(
            Arguments.of(Instantiable.class.getName(), PatternOptionBuilder.CLASS_VALUE, Instantiable.class),
            Arguments.of("invalid.class.name", PatternOptionBuilder.CLASS_VALUE, ParseException.class),
            Arguments.of(Instantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, new Instantiable()),
            Arguments.of(NotInstantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, ParseException.class),
            Arguments.of("unknown", PatternOptionBuilder.OBJECT_VALUE, ParseException.class)
        );
    }

    static Stream<Arguments> dateProvider() {
        // Dates from strings can be locale-dependent. To create a reliable test,
        // we format a known Date into a string and then parse it back.
        final Date date = new Date(1023400137000L); // Jun 06 2002
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        return Stream.of(
            Arguments.of(dateFormat.format(date), PatternOptionBuilder.DATE_VALUE, date),
            Arguments.of("not a date", PatternOptionBuilder.DATE_VALUE, ParseException.class),
            // This format is not supported by the default parser
            Arguments.of("Jun 06 17:48:57 EDT 2002", PatternOptionBuilder.DATE_VALUE, ParseException.class)
        );
    }

    static Stream<Arguments> fileAndPathProvider() {
        return Stream.of(
            Arguments.of("some-file.txt", PatternOptionBuilder.FILE_VALUE, new File("some-file.txt")),
            Arguments.of("non-existing.file", PatternOptionBuilder.EXISTING_FILE_VALUE, ParseException.class),
            Arguments.of("some-path.txt", Path.class, new File("some-path.txt").toPath())
        );
    }

    static Stream<Arguments> numericTypesProvider() {
        return Stream.of(
            // Integer
            Arguments.of("5", Integer.class, 5),
            Arguments.of("not a number", Integer.class, ParseException.class),
            Arguments.of("5.5", Integer.class, ParseException.class),
            Arguments.of(Long.toString(Long.MAX_VALUE), Integer.class, ParseException.class),
            // Long
            Arguments.of("5", Long.class, 5L),
            Arguments.of("not a number", Long.class, ParseException.class),
            Arguments.of("5.5", Long.class, ParseException.class),
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
            Arguments.of(Double.toString(Double.MAX_VALUE), Float.class, Float.POSITIVE_INFINITY)
        );
    }

    static Stream<Arguments> bigNumberTypesProvider() {
        return Stream.of(
            // BigInteger
            Arguments.of("5", BigInteger.class, new BigInteger("5")),
            Arguments.of("not a number", BigInteger.class, ParseException.class),
            Arguments.of("5.5", BigInteger.class, ParseException.class),
            // BigDecimal
            Arguments.of("5.5", BigDecimal.class, new BigDecimal("5.5")),
            Arguments.of("not a number", BigDecimal.class, ParseException.class)
        );
    }

    static Stream<Arguments> genericNumberProvider() {
        return Stream.of(
            Arguments.of("1.5", PatternOptionBuilder.NUMBER_VALUE, 1.5d),
            Arguments.of("15", PatternOptionBuilder.NUMBER_VALUE, 15L),
            Arguments.of("not a number", PatternOptionBuilder.NUMBER_VALUE, ParseException.class)
        );
    }

    static Stream<Arguments> miscTypesProvider() throws MalformedURLException {
        return Stream.of(
            // String (should pass through)
            Arguments.of("a string", PatternOptionBuilder.STRING_VALUE, "a string"),
            // URL
            Arguments.of("https://commons.apache.org", PatternOptionBuilder.URL_VALUE, new URL("https://commons.apache.org")),
            Arguments.of("bad-url", PatternOptionBuilder.URL_VALUE, ParseException.class),
            // Character
            Arguments.of("j", Character.class, 'j'),
            Arguments.of("java", Character.class, 'j'), // takes the first char
            // FILES_VALUE is not registered by default, so it should return the raw string
            Arguments.of("some.files", PatternOptionBuilder.FILES_VALUE, "some.files")
        );
    }

    // --- Helper classes for object creation tests ---

    /** A simple class that can be instantiated by TypeHandler. */
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

    /** A class with a private constructor that cannot be instantiated by TypeHandler. */
    public static final class NotInstantiable {
        private NotInstantiable() {
        }
    }
}