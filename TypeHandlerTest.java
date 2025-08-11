package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
 * Tests for {@link TypeHandler}.
 * 
 * The tests are organized by the API under test. Helpers are used to reduce duplication and make the intention explicit.
 */
class TypeHandlerTest {

    // ---------- Test fixtures and helpers ----------

    /** A simple type used for Class and Object creation tests. */
    public static class Instantiable {
        @Override
        public boolean equals(final Object other) {
            return other instanceof Instantiable;
        }
        @Override
        public int hashCode() {
            return 1;
        }
    }

    /** Used for negative Object creation tests: cannot be instantiated. */
    public static final class NotInstantiable {
        private NotInstantiable() {
        }
    }

    /** Always returns the same Path. */
    private static final Converter<Path, InvalidPathException> PATH_CONVERTER = s -> Paths.get("foo");

    private static final String EXISTING_READABLE_FILE = "src/test/resources/org/apache/commons/cli/existing-readable.file";
    private static final String SAMPLE_HTTP_URL = "https://commons.apache.org";

    private static void forcePatternOptionBuilderInit() {
        // Forces loading/initialization of PatternOptionBuilder and any side-effect on TypeHandler.
        @SuppressWarnings("unused")
        final Class<?> ignored = PatternOptionBuilder.FILES_VALUE;
    }

    private static Stream<Date> createDateFixtures() {
        return Stream.of(
                Date.from(Instant.EPOCH),
                Date.from(Instant.ofEpochSecond(0)),
                Date.from(Instant.ofEpochSecond(40_000))
        );
    }

    private static Arguments args(final Object... values) {
        return Arguments.of(values);
    }

    private static Stream<Arguments> createValueTestParameters() throws MalformedURLException {
        forcePatternOptionBuilderInit();

        /*
         * Dates calculated from strings are dependent on configuration and environment. To avoid this,
         * format a known Date to a String and then parse that String using the converter.
         * This gives strings that always match the correct time zone on the running machine.
         */
        final Date date = new Date(1023400137000L);
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

        final List<Arguments> list = new ArrayList<>();

        // Class
        list.add(args(Instantiable.class.getName(), PatternOptionBuilder.CLASS_VALUE, Instantiable.class));
        list.add(args("what ever", PatternOptionBuilder.CLASS_VALUE, ParseException.class));

        // Date
        list.add(args("what ever", PatternOptionBuilder.DATE_VALUE, ParseException.class));
        list.add(args(dateFormat.format(date), PatternOptionBuilder.DATE_VALUE, date));
        list.add(args("Jun 06 17:48:57 EDT 2002", PatternOptionBuilder.DATE_VALUE, ParseException.class));

        // Existing File
        list.add(args("non-existing.file", PatternOptionBuilder.EXISTING_FILE_VALUE, ParseException.class));

        // File
        list.add(args("some-file.txt", PatternOptionBuilder.FILE_VALUE, new File("some-file.txt")));

        // Path
        list.add(args("some-path.txt", Path.class, new File("some-path.txt").toPath()));

        // Files (PatternOptionBuilder.FILES_VALUE is not registered, so conversion falls back to the raw string)
        list.add(args("some.files", PatternOptionBuilder.FILES_VALUE, "some.files"));

        // Integer
        list.add(args("just-a-string", Integer.class, ParseException.class));
        list.add(args("5", Integer.class, 5));
        list.add(args("5.5", Integer.class, ParseException.class));
        list.add(args(Long.toString(Long.MAX_VALUE), Integer.class, ParseException.class));

        // Long
        list.add(args("just-a-string", Long.class, ParseException.class));
        list.add(args("5", Long.class, 5L));
        list.add(args("5.5", Long.class, ParseException.class));

        // Short
        list.add(args("just-a-string", Short.class, ParseException.class));
        list.add(args("5", Short.class, (short) 5));
        list.add(args("5.5", Short.class, ParseException.class));
        list.add(args(Integer.toString(Integer.MAX_VALUE), Short.class, ParseException.class));

        // Byte
        list.add(args("just-a-string", Byte.class, ParseException.class));
        list.add(args("5", Byte.class, (byte) 5));
        list.add(args("5.5", Byte.class, ParseException.class));
        list.add(args(Short.toString(Short.MAX_VALUE), Byte.class, ParseException.class));

        // Character
        list.add(args("just-a-string", Character.class, 'j'));
        list.add(args("5", Character.class, '5'));
        list.add(args("5.5", Character.class, '5'));
        list.add(args("\\u0124", Character.class, Character.toChars(0x0124)[0]));

        // Double
        list.add(args("just-a-string", Double.class, ParseException.class));
        list.add(args("5", Double.class, 5d));
        list.add(args("5.5", Double.class, 5.5));

        // Float
        list.add(args("just-a-string", Float.class, ParseException.class));
        list.add(args("5", Float.class, 5f));
        list.add(args("5.5", Float.class, 5.5f));
        list.add(args(Double.toString(Double.MAX_VALUE), Float.class, Float.POSITIVE_INFINITY));

        // BigInteger
        list.add(args("just-a-string", BigInteger.class, ParseException.class));
        list.add(args("5", BigInteger.class, new BigInteger("5")));
        list.add(args("5.5", BigInteger.class, ParseException.class));

        // BigDecimal
        list.add(args("just-a-string", BigDecimal.class, ParseException.class));
        list.add(args("5", BigDecimal.class, new BigDecimal("5")));
        list.add(args("5.5", BigDecimal.class, new BigDecimal(5.5)));

        // Number (PatternOptionBuilder.NUMBER_VALUE)
        list.add(args("1.5", PatternOptionBuilder.NUMBER_VALUE, Double.valueOf(1.5)));
        list.add(args("15", PatternOptionBuilder.NUMBER_VALUE, Long.valueOf(15)));
        list.add(args("not a number", PatternOptionBuilder.NUMBER_VALUE, ParseException.class));

        // Object
        list.add(args(Instantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, new Instantiable()));
        list.add(args(NotInstantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, ParseException.class));
        list.add(args("unknown", PatternOptionBuilder.OBJECT_VALUE, ParseException.class));

        // String
        list.add(args("String", PatternOptionBuilder.STRING_VALUE, "String"));

        // URL
        list.add(args(SAMPLE_HTTP_URL, PatternOptionBuilder.URL_VALUE, new URL(SAMPLE_HTTP_URL)));
        list.add(args("Malformed-url", PatternOptionBuilder.URL_VALUE, ParseException.class));

        return list.stream();
    }

    @SuppressWarnings("unchecked")
    private static void assertCreateValueThrows(final String str, final Class<?> type, final Class<? extends Throwable> expected) {
        final Object typeAsObject = type; // must also test the deprecated Object overload
        assertThrows((Class<Throwable>) expected, () -> TypeHandler.createValue(str, type), () -> "Expected " + expected.getSimpleName() + " for type " + type);
        assertThrows((Class<Throwable>) expected, () -> TypeHandler.createValue(str, typeAsObject),
                () -> "Expected " + expected.getSimpleName() + " for object API and type " + type);
    }

    private static void assertCreateValueEquals(final String str, final Class<?> type, final Object expected) throws Exception {
        final Object typeAsObject = type; // must also test the deprecated Object overload
        assertEquals(expected, TypeHandler.createValue(str, type), () -> "Unexpected value for type " + type + " from '" + str + "'");
        assertEquals(expected, TypeHandler.createValue(str, typeAsObject),
                () -> "Unexpected value for object API and type " + type + " from '" + str + "'");
    }

    // ---------- Tests for specific APIs ----------

    @Test
    void testCreateClass() throws ParseException {
        final Class<?> cls = getClass();
        assertEquals(cls, TypeHandler.createClass(cls.getName()), "Should create the current test class by name");
    }

    @ParameterizedTest
    @MethodSource("createDateFixtures")
    void testCreateDate(final Date date) {
        assertEquals(date, TypeHandler.createDate(date.toString()), "Date string should round-trip via TypeHandler.createDate");
    }

    @Test
    void testCreateFile() {
        final File file = new File("").getAbsoluteFile();
        assertEquals(file, TypeHandler.createFile(file.toString()), "File path should round-trip via TypeHandler.createFile");
    }

    @Test
    void testCreateFilesIsUnsupported() {
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles(null), "createFiles is deprecated and should be unsupported");
    }

    @Test
    void testCreateNumber() throws ParseException {
        assertEquals(0L, TypeHandler.createNumber("0"), "Integer-like string should produce a Long");
        assertEquals(0d, TypeHandler.createNumber("0.0"), "Decimal string should produce a Double");
    }

    @Test
    void testCreateObject() throws ParseException {
        assertInstanceOf(Date.class, TypeHandler.createObject(Date.class.getName()), "Should instantiate a java.util.Date");
    }

    @Test
    void testCreateURL() throws ParseException, MalformedURLException {
        final URL fileUrl = Paths.get("").toAbsolutePath().toUri().toURL();
        assertEquals(fileUrl, TypeHandler.createURL(fileUrl.toString()), "File URL should round-trip via TypeHandler.createURL");
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest(name = "{0} as {1}")
    @MethodSource("createValueTestParameters")
    void testCreateValue(final String str, final Class<?> type, final Object expected) throws Exception {
        if (expected instanceof Class<?> && Throwable.class.isAssignableFrom((Class<?>) expected)) {
            assertCreateValueThrows(str, type, (Class<? extends Throwable>) expected);
        } else {
            assertCreateValueEquals(str, type, expected);
        }
    }

    @Test
    void testCreateValueExistingFile() throws Exception {
        try (FileInputStream result = TypeHandler.createValue(EXISTING_READABLE_FILE, PatternOptionBuilder.EXISTING_FILE_VALUE)) {
            assertNotNull(result, "Should open an existing readable file");
        }
    }

    @Test
    void testInstantiableEquals() {
        // Proof of equality for later tests
        assertEquals(new Instantiable(), new Instantiable(), "Instantiable equality must hold for the tests to be valid");
    }

    @Test
    void testOpenFile() throws ParseException, IOException {
        try (FileInputStream fis = TypeHandler.openFile(EXISTING_READABLE_FILE)) {
            IOUtils.consume(fis);
        }
    }

    @Test
    void testRegister() {
        final Map<Class<?>, Converter<?, ? extends Throwable>> defaultMap = TypeHandler.createDefaultMap();
        final TypeHandler typeHandler = new TypeHandler(defaultMap);

        // Default PATH converter
        assertEquals(Converter.PATH, typeHandler.getConverter(Path.class), "Default PATH converter should be registered");

        // Override and then restore
        try {
            defaultMap.put(Path.class, PATH_CONVERTER);
            assertEquals(PATH_CONVERTER, typeHandler.getConverter(Path.class), "Custom PATH converter should be visible via TypeHandler");
        } finally {
            defaultMap.remove(Path.class);
            assertEquals(Converter.DEFAULT, typeHandler.getConverter(Path.class), "After removal, should use Converter.DEFAULT");
        }
    }
}