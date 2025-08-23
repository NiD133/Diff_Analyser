package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for the {@link TypeHandler} class, focusing on value creation from strings.
 */
@DisplayName("TypeHandler Value Creation")
class TypeHandlerTest {

    @BeforeAll
    static void setup() {
        // The PatternOptionBuilder class has a static initializer that populates the
        // TypeHandler's default converters. Referencing any of its static fields
        // triggers this initialization, which is required for these tests.
        assertNotNull(PatternOptionBuilder.CLASS_VALUE);
    }

    @Test
    void createValue_forString_shouldReturnTheSameString() throws ParseException {
        assertEquals("a string", TypeHandler.createValue("a string", PatternOptionBuilder.STRING_VALUE));
    }

    @Nested
    @DisplayName("for Numeric Types")
    class NumericConversionTests {

        static Stream<Arguments> validNumberProvider() {
            return Stream.of(
                Arguments.of("5", Integer.class, 5),
                Arguments.of("5", Long.class, 5L),
                Arguments.of("5", Short.class, (short) 5),
                Arguments.of("5", Byte.class, (byte) 5),
                Arguments.of("5.5", Double.class, 5.5d),
                Arguments.of("5.5", Float.class, 5.5f),
                Arguments.of(Double.toString(Double.MAX_VALUE), Float.class, Float.POSITIVE_INFINITY),
                Arguments.of("5", BigInteger.class, new BigInteger("5")),
                Arguments.of("5.5", BigDecimal.class, new BigDecimal("5.5")),
                Arguments.of("1.5", PatternOptionBuilder.NUMBER_VALUE, 1.5d),
                Arguments.of("15", PatternOptionBuilder.NUMBER_VALUE, 15L)
            );
        }

        @ParameterizedTest(name = "should convert \"{0}\" to {2} ({1})")
        @MethodSource("validNumberProvider")
        void createValue_withValidNumberString_shouldReturnCorrectNumber(String input, Class<?> type, Object expected) throws ParseException {
            assertEquals(expected, TypeHandler.createValue(input, type));
        }

        static Stream<Arguments> invalidNumberProvider() {
            return Stream.of(
                Arguments.of("not a number", Integer.class),
                Arguments.of("5.5", Integer.class),
                Arguments.of(Long.toString(Long.MAX_VALUE), Integer.class),
                Arguments.of("not a number", Long.class),
                Arguments.of("5.5", Long.class),
                Arguments.of("not a number", Short.class),
                Arguments.of(Integer.toString(Integer.MAX_VALUE), Short.class),
                Arguments.of("not a number", Byte.class),
                Arguments.of(Short.toString(Short.MAX_VALUE), Byte.class),
                Arguments.of("not a number", Double.class),
                Arguments.of("not a number", Float.class),
                Arguments.of("not a number", BigInteger.class),
                Arguments.of("5.5", BigInteger.class),
                Arguments.of("not a number", BigDecimal.class),
                Arguments.of("not a number", PatternOptionBuilder.NUMBER_VALUE)
            );
        }

        @ParameterizedTest(name = "should fail to convert \"{0}\" to {1}")
        @MethodSource("invalidNumberProvider")
        void createValue_withInvalidNumberString_shouldThrowParseException(String input, Class<?> type) {
            assertThrows(ParseException.class, () -> TypeHandler.createValue(input, type));
        }
    }

    @Nested
    @DisplayName("for Date Type")
    class DateConversionTests {
        @Test
        void createValue_withValidDateString_shouldReturnDate() throws ParseException {
            // Dates are sensitive to locale/timezone. Create a date and format it
            // to get a string that the parser is guaranteed to understand.
            final Date date = new Date(1023400137000L);
            final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            final String dateString = dateFormat.format(date);

            assertEquals(date, TypeHandler.createValue(dateString, PatternOptionBuilder.DATE_VALUE));
        }

        @ParameterizedTest
        @ValueSource(strings = {"not a date", "Jun 06 17:48:57 EDT 2002"})
        void createValue_withInvalidDateString_shouldThrowParseException(String invalidDate) {
            assertThrows(ParseException.class, () -> TypeHandler.createValue(invalidDate, PatternOptionBuilder.DATE_VALUE));
        }
    }

    @Nested
    @DisplayName("for File System Types")
    class FileSystemConversionTests {
        @Test
        void createValue_forFile_shouldReturnFile() throws ParseException {
            assertEquals(new File("some-file.txt"), TypeHandler.createValue("some-file.txt", PatternOptionBuilder.FILE_VALUE));
        }

        @Test
        void createValue_forExistingFile_whenFileDoesNotExist_shouldThrowParseException() {
            assertThrows(ParseException.class, () -> TypeHandler.createValue("non-existing.file", PatternOptionBuilder.EXISTING_FILE_VALUE));
        }

        @Test
        void createValue_forPath_shouldReturnPath() throws ParseException {
            assertEquals(Paths.get("some-path.txt"), TypeHandler.createValue("some-path.txt", Path.class));
        }
    }

    @Nested
    @DisplayName("for Class and Object Types")
    class ObjectClassConversionTests {
        @Test
        void createValue_forClass_withValidClassName_shouldReturnClass() throws ParseException {
            assertEquals(Instantiable.class, TypeHandler.createValue(Instantiable.class.getName(), PatternOptionBuilder.CLASS_VALUE));
        }

        @Test
        void createValue_forClass_withInvalidClassName_shouldThrowParseException() {
            assertThrows(ParseException.class, () -> TypeHandler.createValue("not.a.Class", PatternOptionBuilder.CLASS_VALUE));
        }

        @Test
        void createValue_forObject_withInstantiableClass_shouldReturnNewInstance() throws ParseException {
            assertEquals(new Instantiable(), TypeHandler.createValue(Instantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE));
        }

        @Test
        void createValue_forObject_withNonInstantiableClass_shouldThrowParseException() {
            assertThrows(ParseException.class, () -> TypeHandler.createValue(NotInstantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE));
        }

        @Test
        void createValue_forObject_withInvalidClassName_shouldThrowParseException() {
            assertThrows(ParseException.class, () -> TypeHandler.createValue("not.a.Class", PatternOptionBuilder.OBJECT_VALUE));
        }
    }

    @Nested
    @DisplayName("for Other Types")
    class OtherTypeConversionTests {
        @Test
        void createValue_withValidUrlString_shouldReturnUrl() throws ParseException, MalformedURLException {
            final String urlString = "https://commons.apache.org";
            assertEquals(new URL(urlString), TypeHandler.createValue(urlString, PatternOptionBuilder.URL_VALUE));
        }

        @Test
        void createValue_withMalformedUrlString_shouldThrowParseException() {
            assertThrows(ParseException.class, () -> TypeHandler.createValue("bad-url", PatternOptionBuilder.URL_VALUE));
        }

        @ParameterizedTest
        @CsvSource({
            "a-string, a",
            "5, 5",
            "5.5, 5" // Only the first character is used
        })
        void createValue_forCharacter_shouldReturnFirstChar(String input, char expected) throws ParseException {
            assertEquals(expected, TypeHandler.createValue(input, Character.class));
        }
    }

    @Test
    @DisplayName("createValue(String, Object) [deprecated] should behave like createValue(String, Class)")
    void testCreateValueWithDeprecatedObjectApi() throws ParseException {
        // Test a success case with the deprecated method
        assertEquals(5, TypeHandler.createValue("5", (Object) Integer.class));

        // Test a failure case with the deprecated method
        assertThrows(ParseException.class, () -> TypeHandler.createValue("not-a-number", (Object) Integer.class));
    }

    // Helper classes for Class and Object creation tests
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

    public static final class NotInstantiable {
        private NotInstantiable() {
        }
    }
}