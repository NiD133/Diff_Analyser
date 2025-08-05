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

class TypeHandlerTest {

    // Constants for reusable test values
    private static final String EXISTING_READABLE_FILE = "src/test/resources/org/apache/commons/cli/existing-readable.file";
    private static final String MALFORMED_URL = "Malformed-url";
    private static final String COMMONS_URL = "https://commons.apache.org";
    private static final String TEST_FILE_NAME = "some-file.txt";
    private static final String TEST_PATH_NAME = "some-path.txt";
    private static final String NON_EXISTING_FILE = "non-existing.file";

    /** Used for successful Class and Object creation tests */
    public static class Instantiable {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Instantiable;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }

    /** Used for failure Class and Object creation tests */
    public static final class NotInstantiable {
        private NotInstantiable() {
        }
    }

    /** Custom Path converter for registration tests */
    private static final Converter<Path, InvalidPathException> PATH_CONVERTER = s -> Paths.get("foo");

    private static Stream<Date> createDateFixtures() {
        return Stream.of(
            Date.from(Instant.EPOCH),
            Date.from(Instant.ofEpochSecond(0)),
            Date.from(Instant.ofEpochSecond(40_000))
        );
    }

    private static Stream<Arguments> createValueTestParameters() throws MalformedURLException {
        // Initialize PatternOptionBuilder to ensure TypeHandler table is loaded
        @SuppressWarnings("unused")
        final Class<?> loadStatic = PatternOptionBuilder.FILES_VALUE;

        final List<Arguments> testCases = new ArrayList<>();
        final Date testDate = new Date(1023400137000L);
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

        // Class type tests
        testCases.add(Arguments.of(Instantiable.class.getName(), PatternOptionBuilder.CLASS_VALUE, Instantiable.class));
        testCases.add(Arguments.of("invalid class", PatternOptionBuilder.CLASS_VALUE, ParseException.class));

        // Date type tests
        testCases.add(Arguments.of("invalid date", PatternOptionBuilder.DATE_VALUE, ParseException.class));
        testCases.add(Arguments.of(dateFormat.format(testDate), PatternOptionBuilder.DATE_VALUE, testDate));
        testCases.add(Arguments.of("Jun 06 17:48:57 EDT 2002", PatternOptionBuilder.DATE_VALUE, ParseException.class));

        // File type tests
        testCases.add(Arguments.of(NON_EXISTING_FILE, PatternOptionBuilder.EXISTING_FILE_VALUE, ParseException.class));
        testCases.add(Arguments.of(TEST_FILE_NAME, PatternOptionBuilder.FILE_VALUE, new File(TEST_FILE_NAME)));
        testCases.add(Arguments.of(TEST_PATH_NAME, Path.class, new File(TEST_PATH_NAME).toPath()));
        testCases.add(Arguments.of("some.files", PatternOptionBuilder.FILES_VALUE, "some.files")); // Unregistered type

        // Numeric type tests
        testCases.add(Arguments.of("5", Integer.class, 5));
        testCases.add(Arguments.of("invalid int", Integer.class, ParseException.class));
        testCases.add(Arguments.of("5.5", Integer.class, ParseException.class));
        
        testCases.add(Arguments.of("5", Long.class, 5L));
        testCases.add(Arguments.of("invalid long", Long.class, ParseException.class));
        
        testCases.add(Arguments.of("5", Short.class, (short) 5));
        testCases.add(Arguments.of("invalid short", Short.class, ParseException.class));
        
        testCases.add(Arguments.of("5", Byte.class, (byte) 5));
        testCases.add(Arguments.of("invalid byte", Byte.class, ParseException.class));
        
        testCases.add(Arguments.of("a", Character.class, 'a'));
        testCases.add(Arguments.of("5", Character.class, '5'));
        
        testCases.add(Arguments.of("5.5", Double.class, 5.5));
        testCases.add(Arguments.of("invalid double", Double.class, ParseException.class));
        
        testCases.add(Arguments.of("5.5", Float.class, 5.5f));
        testCases.add(Arguments.of("invalid float", Float.class, ParseException.class));
        
        testCases.add(Arguments.of("5", BigInteger.class, new BigInteger("5")));
        testCases.add(Arguments.of("invalid bigint", BigInteger.class, ParseException.class));
        
        testCases.add(Arguments.of("5.5", BigDecimal.class, new BigDecimal("5.5")));
        testCases.add(Arguments.of("invalid decimal", BigDecimal.class, ParseException.class));
        
        testCases.add(Arguments.of("15", PatternOptionBuilder.NUMBER_VALUE, 15L));
        testCases.add(Arguments.of("1.5", PatternOptionBuilder.NUMBER_VALUE, 1.5));
        testCases.add(Arguments.of("invalid number", PatternOptionBuilder.NUMBER_VALUE, ParseException.class));

        // Object creation tests
        testCases.add(Arguments.of(Instantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, new Instantiable()));
        testCases.add(Arguments.of(NotInstantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, ParseException.class));
        testCases.add(Arguments.of("invalid object", PatternOptionBuilder.OBJECT_VALUE, ParseException.class));

        // String type tests
        testCases.add(Arguments.of("test string", PatternOptionBuilder.STRING_VALUE, "test string"));

        // URL type tests
        testCases.add(Arguments.of(COMMONS_URL, PatternOptionBuilder.URL_VALUE, new URL(COMMONS_URL)));
        testCases.add(Arguments.of(MALFORMED_URL, PatternOptionBuilder.URL_VALUE, ParseException.class));

        return testCases.stream();
    }

    @Test
    void createClass_ReturnsCorrectClass() throws ParseException {
        final Class<?> cls = getClass();
        assertEquals(cls, TypeHandler.createClass(cls.getName()));
    }

    @ParameterizedTest
    @MethodSource("createDateFixtures")
    void createDate_WithValidDateString_ReturnsCorrectDate(Date date) {
        assertEquals(date, TypeHandler.createDate(date.toString()));
    }

    @Test
    void createFile_WithValidPath_ReturnsFile() {
        final File file = new File("").getAbsoluteFile();
        assertEquals(file, TypeHandler.createFile(file.toString()));
    }

    @Test
    void createFiles_ThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles(null));
    }

    @Test
    void createNumber_WithNumericStrings_ReturnsCorrectNumbers() throws ParseException {
        assertEquals(0L, TypeHandler.createNumber("0"));
        assertEquals(0d, TypeHandler.createNumber("0.0"));
    }

    @Test
    void createObject_WithDateClass_ReturnsDateInstance() throws ParseException {
        assertTrue(TypeHandler.createObject(Date.class.getName()) instanceof Date);
    }

    @Test
    void createURL_WithValidURL_ReturnsURL() throws ParseException, MalformedURLException {
        final URL file = Paths.get("").toAbsolutePath().toUri().toURL();
        assertEquals(file, TypeHandler.createURL(file.toString()));
    }

    @ParameterizedTest(name = "input={0}, type={1}")
    @MethodSource("createValueTestParameters")
    void createValue_WithVariousInputs_ReturnsExpectedResult(String input, Class<?> type, Object expected) {
        if (expected instanceof Class<?> && Throwable.class.isAssignableFrom((Class<?>) expected)) {
            // Handle expected exception cases
            assertThrows((Class<? extends Throwable>) expected, 
                () -> TypeHandler.createValue(input, type));
        } else {
            // Handle successful conversion cases
            assertEquals(expected, TypeHandler.createValue(input, type));
        }
    }

    @Test
    void createValue_WithExistingFile_ReturnsFileInputStream() throws Exception {
        try (FileInputStream result = TypeHandler.createValue(EXISTING_READABLE_FILE, 
                PatternOptionBuilder.EXISTING_FILE_VALUE)) {
            assertNotNull(result);
        }
    }

    @Test
    void instantiable_ObjectsShouldBeEqual() {
        assertEquals(new Instantiable(), new Instantiable());
    }

    @Test
    void openFile_WithExistingFile_ReturnsInputStream() throws ParseException, IOException {
        try (FileInputStream fis = TypeHandler.openFile(EXISTING_READABLE_FILE)) {
            IOUtils.consume(fis);
        }
    }

    @Test
    void register_CustomConverter_OverridesDefaultBehavior() {
        final Map<Class<?>, Converter<?, ? extends Throwable>> map = TypeHandler.createDefaultMap();
        final TypeHandler typeHandler = new TypeHandler(map);
        
        // Verify default converter
        assertEquals(Converter.PATH, typeHandler.getConverter(Path.class));
        
        try {
            // Register custom converter
            map.put(Path.class, PATH_CONVERTER);
            assertEquals(PATH_CONVERTER, typeHandler.getConverter(Path.class));
        } finally {
            // Restore original state
            map.remove(Path.class);
            assertEquals(Converter.DEFAULT, typeHandler.getConverter(Path.class));
        }
    }
}