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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for TypeHandler class which handles conversion of string values to various Java types.
 */
class TypeHandlerTest {

    // Test helper classes
    
    /** Test class that can be instantiated using default constructor. */
    public static class InstantiableTestClass {
        @Override
        public boolean equals(final Object other) {
            return other instanceof InstantiableTestClass;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }

    /** Test class that cannot be instantiated (private constructor). */
    public static final class NonInstantiableTestClass {
        private NonInstantiableTestClass() {
            // Private constructor prevents instantiation
        }
    }

    // Test constants and fixtures
    
    private static final String TEST_FILE_PATH = "src/test/resources/org/apache/commons/cli/existing-readable.file";
    private static final String SAMPLE_URL = "https://commons.apache.org";
    private static final Date FIXED_TEST_DATE = new Date(1023400137000L); // Fixed date for consistent testing
    
    /** Custom path converter that always returns the same path for testing. */
    private static final Converter<Path, InvalidPathException> CUSTOM_PATH_CONVERTER = s -> Paths.get("foo");

    // Test data providers
    
    /**
     * Provides various Date instances for testing date conversion.
     */
    private static Stream<Date> provideDateTestCases() {
        return Stream.of(
            Date.from(Instant.EPOCH),
            Date.from(Instant.ofEpochSecond(0)),
            Date.from(Instant.ofEpochSecond(40_000))
        );
    }

    /**
     * Provides comprehensive test cases for value conversion testing.
     * Each argument contains: input string, target type, expected result (or exception class).
     */
    private static Stream<Arguments> provideValueConversionTestCases() throws MalformedURLException {
        // Initialize PatternOptionBuilder to ensure TypeHandler table is loaded
        @SuppressWarnings("unused")
        final Class<?> forceStaticLoad = PatternOptionBuilder.FILES_VALUE;
        
        final List<Arguments> testCases = new ArrayList<>();
        
        // Use consistent date formatting to avoid timezone issues
        final DateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        final String formattedTestDate = dateFormatter.format(FIXED_TEST_DATE);

        // Class conversion test cases
        addClassConversionTests(testCases);
        
        // Date conversion test cases
        addDateConversionTests(testCases, formattedTestDate);
        
        // File conversion test cases
        addFileConversionTests(testCases);
        
        // Numeric type conversion test cases
        addNumericConversionTests(testCases);
        
        // Character conversion test cases
        addCharacterConversionTests(testCases);
        
        // Big number conversion test cases
        addBigNumberConversionTests(testCases);
        
        // Object instantiation test cases
        addObjectInstantiationTests(testCases);
        
        // URL conversion test cases
        addUrlConversionTests(testCases);
        
        // String conversion test cases
        testCases.add(Arguments.of("String", PatternOptionBuilder.STRING_VALUE, "String"));

        return testCases.stream();
    }

    private static void addClassConversionTests(List<Arguments> testCases) {
        testCases.add(Arguments.of(
            InstantiableTestClass.class.getName(), 
            PatternOptionBuilder.CLASS_VALUE, 
            InstantiableTestClass.class
        ));
        testCases.add(Arguments.of(
            "non.existent.Class", 
            PatternOptionBuilder.CLASS_VALUE, 
            ParseException.class
        ));
    }

    private static void addDateConversionTests(List<Arguments> testCases, String formattedTestDate) {
        testCases.add(Arguments.of(
            "invalid date string", 
            PatternOptionBuilder.DATE_VALUE, 
            ParseException.class
        ));
        testCases.add(Arguments.of(
            formattedTestDate, 
            PatternOptionBuilder.DATE_VALUE, 
            FIXED_TEST_DATE
        ));
        testCases.add(Arguments.of(
            "Jun 06 17:48:57 EDT 2002", 
            PatternOptionBuilder.DATE_VALUE, 
            ParseException.class
        ));
    }

    private static void addFileConversionTests(List<Arguments> testCases) {
        testCases.add(Arguments.of(
            "non-existing.file", 
            PatternOptionBuilder.EXISTING_FILE_VALUE, 
            ParseException.class
        ));
        testCases.add(Arguments.of(
            "some-file.txt", 
            PatternOptionBuilder.FILE_VALUE, 
            new File("some-file.txt")
        ));
        testCases.add(Arguments.of(
            "some-path.txt", 
            Path.class, 
            new File("some-path.txt").toPath()
        ));
        // FILES_VALUE is not registered, should return the string as-is
        testCases.add(Arguments.of(
            "some.files", 
            PatternOptionBuilder.FILES_VALUE, 
            "some.files"
        ));
    }

    private static void addNumericConversionTests(List<Arguments> testCases) {
        // Integer conversion tests
        addIntegerConversionTests(testCases);
        
        // Long conversion tests
        addLongConversionTests(testCases);
        
        // Short conversion tests
        addShortConversionTests(testCases);
        
        // Byte conversion tests
        addByteConversionTests(testCases);
        
        // Double conversion tests
        addDoubleConversionTests(testCases);
        
        // Float conversion tests
        addFloatConversionTests(testCases);
        
        // Number (generic) conversion tests
        testCases.add(Arguments.of("1.5", PatternOptionBuilder.NUMBER_VALUE, Double.valueOf(1.5)));
        testCases.add(Arguments.of("15", PatternOptionBuilder.NUMBER_VALUE, Long.valueOf(15)));
        testCases.add(Arguments.of("not a number", PatternOptionBuilder.NUMBER_VALUE, ParseException.class));
    }

    private static void addIntegerConversionTests(List<Arguments> testCases) {
        testCases.add(Arguments.of("invalid integer", Integer.class, ParseException.class));
        testCases.add(Arguments.of("5", Integer.class, 5));
        testCases.add(Arguments.of("5.5", Integer.class, ParseException.class));
        testCases.add(Arguments.of(Long.toString(Long.MAX_VALUE), Integer.class, ParseException.class));
    }

    private static void addLongConversionTests(List<Arguments> testCases) {
        testCases.add(Arguments.of("invalid long", Long.class, ParseException.class));
        testCases.add(Arguments.of("5", Long.class, 5L));
        testCases.add(Arguments.of("5.5", Long.class, ParseException.class));
    }

    private static void addShortConversionTests(List<Arguments> testCases) {
        testCases.add(Arguments.of("invalid short", Short.class, ParseException.class));
        testCases.add(Arguments.of("5", Short.class, (short) 5));
        testCases.add(Arguments.of("5.5", Short.class, ParseException.class));
        testCases.add(Arguments.of(Integer.toString(Integer.MAX_VALUE), Short.class, ParseException.class));
    }

    private static void addByteConversionTests(List<Arguments> testCases) {
        testCases.add(Arguments.of("invalid byte", Byte.class, ParseException.class));
        testCases.add(Arguments.of("5", Byte.class, (byte) 5));
        testCases.add(Arguments.of("5.5", Byte.class, ParseException.class));
        testCases.add(Arguments.of(Short.toString(Short.MAX_VALUE), Byte.class, ParseException.class));
    }

    private static void addDoubleConversionTests(List<Arguments> testCases) {
        testCases.add(Arguments.of("invalid double", Double.class, ParseException.class));
        testCases.add(Arguments.of("5", Double.class, 5d));
        testCases.add(Arguments.of("5.5", Double.class, 5.5));
    }

    private static void addFloatConversionTests(List<Arguments> testCases) {
        testCases.add(Arguments.of("invalid float", Float.class, ParseException.class));
        testCases.add(Arguments.of("5", Float.class, 5f));
        testCases.add(Arguments.of("5.5", Float.class, 5.5f));
        testCases.add(Arguments.of(Double.toString(Double.MAX_VALUE), Float.class, Float.POSITIVE_INFINITY));
    }

    private static void addCharacterConversionTests(List<Arguments> testCases) {
        testCases.add(Arguments.of("just-a-string", Character.class, 'j'));
        testCases.add(Arguments.of("5", Character.class, '5'));
        testCases.add(Arguments.of("5.5", Character.class, '5'));
        testCases.add(Arguments.of("\\u0124", Character.class, Character.toChars(0x0124)[0]));
    }

    private static void addBigNumberConversionTests(List<Arguments> testCases) {
        // BigInteger tests
        testCases.add(Arguments.of("invalid big integer", BigInteger.class, ParseException.class));
        testCases.add(Arguments.of("5", BigInteger.class, new BigInteger("5")));
        testCases.add(Arguments.of("5.5", BigInteger.class, ParseException.class));

        // BigDecimal tests
        testCases.add(Arguments.of("invalid big decimal", BigDecimal.class, ParseException.class));
        testCases.add(Arguments.of("5", BigDecimal.class, new BigDecimal("5")));
        testCases.add(Arguments.of("5.5", BigDecimal.class, new BigDecimal(5.5)));
    }

    private static void addObjectInstantiationTests(List<Arguments> testCases) {
        testCases.add(Arguments.of(
            InstantiableTestClass.class.getName(), 
            PatternOptionBuilder.OBJECT_VALUE, 
            new InstantiableTestClass()
        ));
        testCases.add(Arguments.of(
            NonInstantiableTestClass.class.getName(), 
            PatternOptionBuilder.OBJECT_VALUE, 
            ParseException.class
        ));
        testCases.add(Arguments.of(
            "unknown.class.Name", 
            PatternOptionBuilder.OBJECT_VALUE, 
            ParseException.class
        ));
    }

    private static void addUrlConversionTests(List<Arguments> testCases) throws MalformedURLException {
        testCases.add(Arguments.of(SAMPLE_URL, PatternOptionBuilder.URL_VALUE, new URL(SAMPLE_URL)));
        testCases.add(Arguments.of("malformed-url", PatternOptionBuilder.URL_VALUE, ParseException.class));
    }

    // Individual method tests
    
    @Test
    void testCreateClass_WithValidClassName_ReturnsCorrectClass() throws ParseException {
        final Class<?> expectedClass = getClass();
        final Class<?> actualClass = TypeHandler.createClass(expectedClass.getName());
        
        assertEquals(expectedClass, actualClass);
    }

    @ParameterizedTest
    @MethodSource("provideDateTestCases")
    void testCreateDate_WithValidDateString_ReturnsCorrectDate(final Date expectedDate) {
        final Date actualDate = TypeHandler.createDate(expectedDate.toString());
        
        assertEquals(expectedDate, actualDate);
    }

    @Test
    void testCreateFile_WithValidPath_ReturnsCorrectFile() {
        final File expectedFile = new File("").getAbsoluteFile();
        final File actualFile = TypeHandler.createFile(expectedFile.toString());
        
        assertEquals(expectedFile, actualFile);
    }

    @Test
    void testCreateFiles_ThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles(null));
    }

    @Test
    void testCreateNumber_WithValidNumbers_ReturnsCorrectTypes() throws ParseException {
        assertEquals(0L, TypeHandler.createNumber("0"));
        assertEquals(0d, TypeHandler.createNumber("0.0"));
    }

    @Test
    void testCreateObject_WithValidClassName_ReturnsInstanceOfClass() throws ParseException {
        final Object createdObject = TypeHandler.createObject(Date.class.getName());
        
        assertTrue(createdObject instanceof Date);
    }

    @Test
    void testCreateURL_WithValidUrlString_ReturnsCorrectURL() throws ParseException, MalformedURLException {
        final URL expectedUrl = Paths.get("").toAbsolutePath().toUri().toURL();
        final URL actualUrl = TypeHandler.createURL(expectedUrl.toString());
        
        assertEquals(expectedUrl, actualUrl);
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest(name = "Converting ''{0}'' to {1}")
    @MethodSource("provideValueConversionTestCases")
    void testCreateValue_WithVariousInputsAndTypes_ProducesExpectedResults(
            final String inputString, 
            final Class<?> targetType, 
            final Object expectedResult) throws Exception {
        
        // Test both Class and Object API variants
        final Object objectApiParameter = targetType;
        
        if (isExpectedToThrowException(expectedResult)) {
            @SuppressWarnings("cast")
            final Class<Throwable> expectedExceptionType = (Class<Throwable>) expectedResult;
            
            assertThrows(expectedExceptionType, () -> TypeHandler.createValue(inputString, targetType));
            assertThrows(expectedExceptionType, () -> TypeHandler.createValue(inputString, objectApiParameter));
        } else {
            assertEquals(expectedResult, TypeHandler.createValue(inputString, targetType));
            assertEquals(expectedResult, TypeHandler.createValue(inputString, objectApiParameter));
        }
    }

    @Test
    void testCreateValue_WithExistingFile_ReturnsFileInputStream() throws Exception {
        try (FileInputStream fileStream = TypeHandler.createValue(TEST_FILE_PATH, PatternOptionBuilder.EXISTING_FILE_VALUE)) {
            assertNotNull(fileStream);
        }
    }

    @Test
    void testInstantiableTestClass_EqualsContract_WorksCorrectly() {
        assertEquals(new InstantiableTestClass(), new InstantiableTestClass());
    }

    @Test
    void testOpenFile_WithExistingFile_ReturnsValidFileInputStream() throws ParseException, IOException {
        try (FileInputStream fileStream = TypeHandler.openFile(TEST_FILE_PATH)) {
            IOUtils.consume(fileStream);
        }
    }

    @Test
    void testTypeHandlerConverterRegistration_WithCustomConverter_WorksCorrectly() {
        final Map<Class<?>, Converter<?, ? extends Throwable>> converterMap = TypeHandler.createDefaultMap();
        final TypeHandler typeHandler = new TypeHandler(converterMap);
        
        // Verify default converter
        assertEquals(Converter.PATH, typeHandler.getConverter(Path.class));
        
        try {
            // Register custom converter
            converterMap.put(Path.class, CUSTOM_PATH_CONVERTER);
            assertEquals(CUSTOM_PATH_CONVERTER, typeHandler.getConverter(Path.class));
        } finally {
            // Clean up - remove custom converter
            converterMap.remove(Path.class);
            assertEquals(Converter.DEFAULT, typeHandler.getConverter(Path.class));
        }
    }

    // Helper methods
    
    /**
     * Determines if the expected result represents an exception that should be thrown.
     */
    private boolean isExpectedToThrowException(Object expectedResult) {
        return expectedResult instanceof Class<?> && 
               Throwable.class.isAssignableFrom((Class<?>) expectedResult);
    }
}