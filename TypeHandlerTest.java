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

    /** Class used for instantiable object tests. */
    public static class Instantiable {
        @Override
        public boolean equals(final Object obj) {
            return obj instanceof Instantiable;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }

    /** Class used for non-instantiable object tests. */
    public static final class NotInstantiable {
        private NotInstantiable() {
            // Private constructor to prevent instantiation
        }
    }

    /** Converter that always returns the same Path. */
    private static final Converter<Path, InvalidPathException> PATH_CONVERTER = s -> Paths.get("foo");

    /** Provides a stream of Date fixtures for testing. */
    private static Stream<Date> createDateFixtures() {
        return Stream.of(
            Date.from(Instant.EPOCH),
            Date.from(Instant.ofEpochSecond(0)),
            Date.from(Instant.ofEpochSecond(40_000))
        );
    }

    /** Provides test parameters for value conversion tests. */
    private static Stream<Arguments> createValueTestParameters() throws MalformedURLException {
        // Force the PatternOptionBuilder to load/modify the TypeHandler table.
        @SuppressWarnings("unused")
        final Class<?> loadStatic = PatternOptionBuilder.FILES_VALUE;

        final List<Arguments> testCases = new ArrayList<>();

        // Prepare date format for consistent date string parsing
        final Date date = new Date(1023400137000L);
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

        // Add test cases
        testCases.add(Arguments.of(Instantiable.class.getName(), PatternOptionBuilder.CLASS_VALUE, Instantiable.class));
        testCases.add(Arguments.of("what ever", PatternOptionBuilder.CLASS_VALUE, ParseException.class));
        testCases.add(Arguments.of("what ever", PatternOptionBuilder.DATE_VALUE, ParseException.class));
        testCases.add(Arguments.of(dateFormat.format(date), PatternOptionBuilder.DATE_VALUE, date));
        testCases.add(Arguments.of("Jun 06 17:48:57 EDT 2002", PatternOptionBuilder.DATE_VALUE, ParseException.class));
        testCases.add(Arguments.of("non-existing.file", PatternOptionBuilder.EXISTING_FILE_VALUE, ParseException.class));
        testCases.add(Arguments.of("some-file.txt", PatternOptionBuilder.FILE_VALUE, new File("some-file.txt")));
        testCases.add(Arguments.of("some-path.txt", Path.class, new File("some-path.txt").toPath()));
        testCases.add(Arguments.of("some.files", PatternOptionBuilder.FILES_VALUE, "some.files"));
        testCases.add(Arguments.of("just-a-string", Integer.class, ParseException.class));
        testCases.add(Arguments.of("5", Integer.class, 5));
        testCases.add(Arguments.of("5.5", Integer.class, ParseException.class));
        testCases.add(Arguments.of(Long.toString(Long.MAX_VALUE), Integer.class, ParseException.class));
        testCases.add(Arguments.of("just-a-string", Long.class, ParseException.class));
        testCases.add(Arguments.of("5", Long.class, 5L));
        testCases.add(Arguments.of("5.5", Long.class, ParseException.class));
        testCases.add(Arguments.of("just-a-string", Short.class, ParseException.class));
        testCases.add(Arguments.of("5", Short.class, (short) 5));
        testCases.add(Arguments.of("5.5", Short.class, ParseException.class));
        testCases.add(Arguments.of(Integer.toString(Integer.MAX_VALUE), Short.class, ParseException.class));
        testCases.add(Arguments.of("just-a-string", Byte.class, ParseException.class));
        testCases.add(Arguments.of("5", Byte.class, (byte) 5));
        testCases.add(Arguments.of("5.5", Byte.class, ParseException.class));
        testCases.add(Arguments.of(Short.toString(Short.MAX_VALUE), Byte.class, ParseException.class));
        testCases.add(Arguments.of("just-a-string", Character.class, 'j'));
        testCases.add(Arguments.of("5", Character.class, '5'));
        testCases.add(Arguments.of("5.5", Character.class, '5'));
        testCases.add(Arguments.of("\\u0124", Character.class, Character.toChars(0x0124)[0]));
        testCases.add(Arguments.of("just-a-string", Double.class, ParseException.class));
        testCases.add(Arguments.of("5", Double.class, 5d));
        testCases.add(Arguments.of("5.5", Double.class, 5.5));
        testCases.add(Arguments.of("just-a-string", Float.class, ParseException.class));
        testCases.add(Arguments.of("5", Float.class, 5f));
        testCases.add(Arguments.of("5.5", Float.class, 5.5f));
        testCases.add(Arguments.of(Double.toString(Double.MAX_VALUE), Float.class, Float.POSITIVE_INFINITY));
        testCases.add(Arguments.of("just-a-string", BigInteger.class, ParseException.class));
        testCases.add(Arguments.of("5", BigInteger.class, new BigInteger("5")));
        testCases.add(Arguments.of("5.5", BigInteger.class, ParseException.class));
        testCases.add(Arguments.of("just-a-string", BigDecimal.class, ParseException.class));
        testCases.add(Arguments.of("5", BigDecimal.class, new BigDecimal("5")));
        testCases.add(Arguments.of("5.5", BigDecimal.class, new BigDecimal(5.5)));
        testCases.add(Arguments.of("1.5", PatternOptionBuilder.NUMBER_VALUE, Double.valueOf(1.5)));
        testCases.add(Arguments.of("15", PatternOptionBuilder.NUMBER_VALUE, Long.valueOf(15)));
        testCases.add(Arguments.of("not a number", PatternOptionBuilder.NUMBER_VALUE, ParseException.class));
        testCases.add(Arguments.of(Instantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, new Instantiable()));
        testCases.add(Arguments.of(NotInstantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, ParseException.class));
        testCases.add(Arguments.of("unknown", PatternOptionBuilder.OBJECT_VALUE, ParseException.class));
        testCases.add(Arguments.of("String", PatternOptionBuilder.STRING_VALUE, "String"));

        final String urlString = "https://commons.apache.org";
        testCases.add(Arguments.of(urlString, PatternOptionBuilder.URL_VALUE, new URL(urlString)));
        testCases.add(Arguments.of("Malformed-url", PatternOptionBuilder.URL_VALUE, ParseException.class));

        return testCases.stream();
    }

    @Test
    void testCreateClass() throws ParseException {
        final Class<?> expectedClass = getClass();
        assertEquals(expectedClass, TypeHandler.createClass(expectedClass.getName()));
    }

    @ParameterizedTest
    @MethodSource("createDateFixtures")
    void testCreateDate(final Date date) {
        assertEquals(date, TypeHandler.createDate(date.toString()));
    }

    @Test
    void testCreateFile() {
        final File expectedFile = new File("").getAbsoluteFile();
        assertEquals(expectedFile, TypeHandler.createFile(expectedFile.toString()));
    }

    @Test
    void testCreateFiles() {
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles(null));
    }

    @Test
    void testCreateNumber() throws ParseException {
        assertEquals(0L, TypeHandler.createNumber("0"));
        assertEquals(0d, TypeHandler.createNumber("0.0"));
    }

    @Test
    void testCreateObject() throws ParseException {
        assertTrue(TypeHandler.createObject(Date.class.getName()) instanceof Date);
    }

    @Test
    void testCreateURL() throws ParseException, MalformedURLException {
        final URL expectedURL = Paths.get("").toAbsolutePath().toUri().toURL();
        assertEquals(expectedURL, TypeHandler.createURL(expectedURL.toString()));
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest(name = "{0} as {1}")
    @MethodSource("createValueTestParameters")
    void testCreateValue(final String input, final Class<?> type, final Object expected) throws Exception {
        final Object objectApiTest = type; // KEEP this cast
        if (expected instanceof Class<?> && Throwable.class.isAssignableFrom((Class<?>) expected)) {
            assertThrows((Class<Throwable>) expected, () -> TypeHandler.createValue(input, type));
            assertThrows((Class<Throwable>) expected, () -> TypeHandler.createValue(input, objectApiTest));
        } else {
            assertEquals(expected, TypeHandler.createValue(input, type));
            assertEquals(expected, TypeHandler.createValue(input, objectApiTest));
        }
    }

    @Test
    void testCreateValueExistingFile() throws Exception {
        try (FileInputStream result = TypeHandler.createValue("src/test/resources/org/apache/commons/cli/existing-readable.file",
                PatternOptionBuilder.EXISTING_FILE_VALUE)) {
            assertNotNull(result);
        }
    }

    @Test
    void testInstantiableEquals() {
        assertEquals(new Instantiable(), new Instantiable());
    }

    @Test
    void testOpenFile() throws ParseException, IOException {
        try (FileInputStream fis = TypeHandler.openFile("src/test/resources/org/apache/commons/cli/existing-readable.file")) {
            IOUtils.consume(fis);
        }
    }

    @Test
    void testRegister() {
        final Map<Class<?>, Converter<?, ? extends Throwable>> converterMap = TypeHandler.createDefaultMap();
        final TypeHandler typeHandler = new TypeHandler(converterMap);
        assertEquals(Converter.PATH, typeHandler.getConverter(Path.class));
        try {
            converterMap.put(Path.class, PATH_CONVERTER);
            assertEquals(PATH_CONVERTER, typeHandler.getConverter(Path.class));
        } finally {
            converterMap.remove(Path.class);
            assertEquals(Converter.DEFAULT, typeHandler.getConverter(Path.class));
        }
    }
}