/*
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link TypeHandler} class, focusing on its ability to convert
 * string values into various Java types.
 */
class TypeHandlerTest {

    //<editor-fold desc="Test Helper Classes">
    /** Used for Class and Object creation tests. */
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

    /** Used for Class and Object negative creation tests. */
    public static final class NotInstantiable {
        private NotInstantiable() {
        }
    }
    //</editor-fold>

    /** A custom converter for testing registration. */
    private static final Converter<Path, InvalidPathException> DUMMY_PATH_CONVERTER = s -> Paths.get("foo");

    @BeforeAll
    static void setup() {
        // This static access is required to trigger the static initializer in
        // PatternOptionBuilder, which registers several default type converters
        // into the static TypeHandler instance.
        @SuppressWarnings("unused")
        final Class<?> ignored = PatternOptionBuilder.FILES_VALUE;
    }

    //<editor-fold desc="Data Providers for Parameterized Tests">
    private static Stream<Arguments> provideValidConversionArguments() throws MalformedURLException {
        final Date date = new Date(1023400137000L);
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        final String urlString = "https://commons.apache.org";

        return Stream.of(
            // Class
            Arguments.of("Instantiable class name", Instantiable.class.getName(), PatternOptionBuilder.CLASS_VALUE, Instantiable.class),
            // Date
            Arguments.of("Formatted date string", dateFormat.format(date), PatternOptionBuilder.DATE_VALUE, date),
            // File
            Arguments.of("File path string", "some-file.txt", PatternOptionBuilder.FILE_VALUE, new File("some-file.txt")),
            // Path
            Arguments.of("Path string", "some-path.txt", Path.class, new File("some-path.txt").toPath()),
            // Unregistered type (should return original string)
            Arguments.of("Unregistered File[] type", "some.files", PatternOptionBuilder.FILES_VALUE, "some.files"),
            // Numbers
            Arguments.of("Integer string", "5", Integer.class, 5),
            Arguments.of("Long string", "5", Long.class, 5L),
            Arguments.of("Short string", "5", Short.class, (short) 5),
            Arguments.of("Byte string", "5", Byte.class, (byte) 5),
            Arguments.of("Double string", "5.5", Double.class, 5.5),
            Arguments.of("Float string", "5.5", Float.class, 5.5f),
            Arguments.of("Float from large double string", Double.toString(Double.MAX_VALUE), Float.class, Float.POSITIVE_INFINITY),
            Arguments.of("BigInteger string", "5", BigInteger.class, new BigInteger("5")),
            Arguments.of("BigDecimal from int string", "5", BigDecimal.class, new BigDecimal("5")),
            Arguments.of("BigDecimal from double string", "5.5", BigDecimal.class, new BigDecimal(5.5)),
            // Character
            Arguments.of("Single character from string", "just-a-string", Character.class, 'j'),
            Arguments.of("Single character from number", "5", Character.class, '5'),
            Arguments.of("Unicode character", "\\u0124", Character.class, 'ĥ'),
            // Number (generic)
            Arguments.of("Generic number (decimal)", "1.5", PatternOptionBuilder.NUMBER_VALUE, 1.5d),
            Arguments.of("Generic number (integer)", "15", PatternOptionBuilder.NUMBER_VALUE, 15L),
            // Object
            Arguments.of("Instantiable object", Instantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, new Instantiable()),
            // String
            Arguments.of("Simple string", "String", PatternOptionBuilder.STRING_VALUE, "String"),
            // URL
            Arguments.of("Valid URL string", urlString, PatternOptionBuilder.URL_VALUE, new URL(urlString))
        );
    }

    private static Stream<Arguments> provideFailedConversionArguments() {
        return Stream.of(
            Arguments.of("Non-existent class", "what ever", PatternOptionBuilder.CLASS_VALUE),
            Arguments.of("Unparseable date", "what ever", PatternOptionBuilder.DATE_VALUE),
            Arguments.of("Non-existing file for EXISTING_FILE_VALUE", "non-existing.file", PatternOptionBuilder.EXISTING_FILE_VALUE),
            Arguments.of("Non-numeric string for Integer", "just-a-string", Integer.class),
            Arguments.of("Decimal string for Integer", "5.5", Integer.class),
            Arguments.of("Out-of-range string for Integer", Long.toString(Long.MAX_VALUE), Integer.class),
            Arguments.of("Non-numeric string for Long", "just-a-string", Long.class),
            Arguments.of("Decimal string for Long", "5.5", Long.class),
            Arguments.of("Non-numeric string for Short", "just-a-string", Short.class),
            Arguments.of("Out-of-range string for Short", Integer.toString(Integer.MAX_VALUE), Short.class),
            Arguments.of("Non-numeric string for Byte", "just-a-string", Byte.class),
            Arguments.of("Out-of-range string for Byte", Short.toString(Short.MAX_VALUE), Byte.class),
            Arguments.of("Non-numeric string for Double", "just-a-string", Double.class),
            Arguments.of("Non-numeric string for Float", "just-a-string", Float.class),
            Arguments.of("Non-numeric string for BigInteger", "just-a-string", BigInteger.class),
            Arguments.of("Decimal string for BigInteger", "5.5", BigInteger.class),
            Arguments.of("Non-numeric string for BigDecimal", "just-a-string", BigDecimal.class),
            Arguments.of("Non-numeric string for generic Number", "not a number", PatternOptionBuilder.NUMBER_VALUE),
            Arguments.of("Non-instantiable class name for Object", NotInstantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE),
            Arguments.of("Unknown class name for Object", "unknown", PatternOptionBuilder.OBJECT_VALUE),
            Arguments.of("Malformed URL string", "Malformed-url", PatternOptionBuilder.URL_VALUE)
        );
    }

    private static Stream<Date> provideDateFixtures() {
        return Stream.of(Date.from(Instant.EPOCH), Date.from(Instant.ofEpochSecond(0)), Date.from(Instant.ofEpochSecond(40_000)));
    }
    //</editor-fold>

    @DisplayName("createValue should convert valid inputs successfully")
    @ParameterizedTest(name = "{0}: ''{1}'' as {2}")
    @MethodSource("provideValidConversionArguments")
    void createValue_withValidInput_shouldConvertSuccessfully(final String description, final String input, final Class<?> type, final Object expected) throws ParseException {
        assertEquals(expected, TypeHandler.createValue(input, type));
    }

    @DisplayName("createValue should throw ParseException for invalid inputs")
    @ParameterizedTest(name = "{0}: ''{1}'' as {2}")
    @MethodSource("provideFailedConversionArguments")
    void createValue_withInvalidInput_shouldThrowParseException(final String description, final String input, final Class<?> type) {
        assertThrows(ParseException.class, () -> TypeHandler.createValue(input, type));
    }

    @DisplayName("Deprecated createValue(String, Object) should handle valid inputs")
    @ParameterizedTest(name = "{0}: ''{1}'' as {2}")
    @MethodSource("provideValidConversionArguments")
    @SuppressWarnings("deprecation")
    void createValue_deprecatedApiWithValidInput_shouldConvertSuccessfully(final String description, final String input, final Class<?> type, final Object expected) throws ParseException {
        assertEquals(expected, TypeHandler.createValue(input, (Object) type));
    }

    @DisplayName("Deprecated createValue(String, Object) should handle invalid inputs")
    @ParameterizedTest(name = "{0}: ''{1}'' as {2}")
    @MethodSource("provideFailedConversionArguments")
    @SuppressWarnings("deprecation")
    void createValue_deprecatedApiWithInvalidInput_shouldThrowParseException(final String description, final String input, final Class<?> type) {
        assertThrows(ParseException.class, () -> TypeHandler.createValue(input, (Object) type));
    }

    @Test
    @DisplayName("createValue with EXISTING_FILE_VALUE should return an open FileInputStream")
    void createValue_forExistingFile_shouldReturnInputStream() throws Exception {
        final String filePath = "src/test/resources/org/apache/commons/cli/existing-readable.file";
        try (FileInputStream result = TypeHandler.createValue(filePath, PatternOptionBuilder.EXISTING_FILE_VALUE)) {
            assertNotNull(result, "Should return a non-null FileInputStream for an existing file.");
        }
    }

    @Test
    @DisplayName("createClass should return the Class object for a valid class name")
    void createClass_withValidClassName_shouldReturnClass() throws ParseException {
        final Class<?> expected = getClass();
        assertEquals(expected, TypeHandler.createClass(expected.getName()));
    }

    @DisplayName("createDate should correctly parse a date string")
    @ParameterizedTest
    @MethodSource("provideDateFixtures")
    void createDate_withValidDateString_shouldReturnDate(final Date date) {
        assertEquals(date, TypeHandler.createDate(date.toString()));
    }

    @Test
    @DisplayName("createFile should return a File object for a given path")
    void createFile_withValidPath_shouldReturnFile() {
        final File file = new File("").getAbsoluteFile();
        assertEquals(file, TypeHandler.createFile(file.toString()));
    }

    @Test
    @DisplayName("createFiles should throw UnsupportedOperationException as it is not implemented")
    void createFiles_shouldThrowUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles(null));
    }

    @Test
    @DisplayName("createNumber should return a Long for an integer string")
    void createNumber_withIntegerString_shouldReturnLong() throws ParseException {
        assertEquals(0L, TypeHandler.createNumber("0"));
    }

    @Test
    @DisplayName("createNumber should return a Double for a decimal string")
    void createNumber_withDecimalString_shouldReturnDouble() throws ParseException {
        assertEquals(0d, TypeHandler.createNumber("0.0"));
    }

    @Test
    @DisplayName("createObject should create an instance of a given class name")
    void createObject_withInstantiableClassName_shouldCreateInstance() throws ParseException {
        assertTrue(TypeHandler.createObject(Date.class.getName()) instanceof Date);
    }



    @Test
    @DisplayName("createURL should return a URL object for a valid URL string")
    void createURL_withValidUrlString_shouldReturnURL() throws ParseException, MalformedURLException {
        final URL url = Paths.get("").toAbsolutePath().toUri().toURL();
        assertEquals(url, TypeHandler.createURL(url.toString()));
    }

    @Test
    @DisplayName("openFile should return an open FileInputStream for a readable file")
    void openFile_withExistingReadableFile_shouldReturnInputStream() throws ParseException, IOException {
        final String filePath = "src/test/resources/org/apache/commons/cli/existing-readable.file";
        try (FileInputStream fis = TypeHandler.openFile(filePath)) {
            assertNotNull(fis);
            // Verify it's readable by consuming its content
            IOUtils.consume(fis);
        }
    }

    @Test
    @DisplayName("Converter registration should be dynamic")
    void converterRegistration_shouldBeDynamic() {
        final Map<Class<?>, Converter<?, ? extends Throwable>> map = TypeHandler.createDefaultMap();
        final TypeHandler typeHandler = new TypeHandler(map);

        // Initially, it should use the default Path converter
        assertEquals(Converter.PATH, typeHandler.getConverter(Path.class));

        try {
            // Register a custom converter
            map.put(Path.class, DUMMY_PATH_CONVERTER);
            assertEquals(DUMMY_PATH_CONVERTER, typeHandler.getConverter(Path.class), "Should return the newly registered converter.");
        } finally {
            // Unregister the custom converter
            map.remove(Path.class);
            assertEquals(Converter.DEFAULT, typeHandler.getConverter(Path.class), "Should revert to the default converter after removal.");
        }
    }

    @Test
    @DisplayName("Instantiable helper class should have value-based equality")
    void instantiableHelperClass_shouldHaveValueEquality() {
        // This test verifies the behavior of the test-local helper class.
        assertEquals(new Instantiable(), new Instantiable());
    }
}