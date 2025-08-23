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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link TypeHandler}.
 * <p>
 * This test class focuses on the {@code createValue} methods, verifying that
 * various string inputs are correctly converted to their expected types or that
 * appropriate exceptions are thrown for invalid inputs.
 * </p>
 */
public class TypeHandlerTest {

    private static Stream<Date> createDateFixtures() {
        return Stream.of(Date.from(Instant.EPOCH), Date.from(Instant.ofEpochSecond(0)), Date.from(Instant.ofEpochSecond(40_000)));
    }

    private static Stream<Arguments> createValueTestParameters() throws MalformedURLException {
        // Setup: Force initialization of PatternOptionBuilder which populates the TypeHandler.
        @SuppressWarnings("unused")
        final Class<?> loadStatic = PatternOptionBuilder.FILES_VALUE;

        final List<Arguments> arguments = new ArrayList<>();

        // -- Class Type --
        arguments.add(Arguments.of(Instantiable.class.getName(), PatternOptionBuilder.CLASS_VALUE, Instantiable.class));
        arguments.add(Arguments.of("what ever",                  PatternOptionBuilder.CLASS_VALUE, ParseException.class));

        // -- Date Type --
        // Dates from strings depend on the test environment. To create a reliable test,
        // we format a known Date into a string and then parse it back.
        final Date date = new Date(1023400137000L);
        final DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        arguments.add(Arguments.of(dateFormat.format(date),      PatternOptionBuilder.DATE_VALUE,  date));
        arguments.add(Arguments.of("what ever",                  PatternOptionBuilder.DATE_VALUE,  ParseException.class));
        arguments.add(Arguments.of("Jun 06 17:48:57 EDT 2002",   PatternOptionBuilder.DATE_VALUE,  ParseException.class));

        // -- File Types --
        arguments.add(Arguments.of("non-existing.file",          PatternOptionBuilder.EXISTING_FILE_VALUE, ParseException.class));
        arguments.add(Arguments.of("some-file.txt",              PatternOptionBuilder.FILE_VALUE,          new File("some-file.txt")));
        // PatternOptionBuilder.FILES_VALUE is not registered by default, so it should return the original string.
        arguments.add(Arguments.of("some.files",                 PatternOptionBuilder.FILES_VALUE,         "some.files"));

        // -- Path Type --
        arguments.add(Arguments.of("some-path.txt",              Path.class,                               new File("some-path.txt").toPath()));

        // -- Numeric Types (Integer, Long, Short, Byte) --
        arguments.add(Arguments.of("5",                          Integer.class, 5));
        arguments.add(Arguments.of("just-a-string",              Integer.class, ParseException.class));
        arguments.add(Arguments.of("5.5",                        Integer.class, ParseException.class));
        arguments.add(Arguments.of(Long.toString(Long.MAX_VALUE),Integer.class, ParseException.class));

        arguments.add(Arguments.of("5",                          Long.class,    5L));
        arguments.add(Arguments.of("just-a-string",              Long.class,    ParseException.class));
        arguments.add(Arguments.of("5.5",                        Long.class,    ParseException.class));

        arguments.add(Arguments.of("5",                          Short.class,   (short) 5));
        arguments.add(Arguments.of("just-a-string",              Short.class,   ParseException.class));
        arguments.add(Arguments.of("5.5",                        Short.class,   ParseException.class));
        arguments.add(Arguments.of(Integer.toString(Integer.MAX_VALUE), Short.class, ParseException.class));

        arguments.add(Arguments.of("5",                          Byte.class,    (byte) 5));
        arguments.add(Arguments.of("just-a-string",              Byte.class,    ParseException.class));
        arguments.add(Arguments.of("5.5",                        Byte.class,    ParseException.class));
        arguments.add(Arguments.of(Short.toString(Short.MAX_VALUE), Byte.class, ParseException.class));

        // -- Character Type --
        arguments.add(Arguments.of("just-a-string",              Character.class, 'j'));
        arguments.add(Arguments.of("5",                          Character.class, '5'));
        arguments.add(Arguments.of("5.5",                        Character.class, '5'));
        arguments.add(Arguments.of("\\u0124",                     Character.class, '\u0124'));

        // -- Floating-Point Types (Double, Float) --
        arguments.add(Arguments.of("5.5",                        Double.class,  5.5d));
        arguments.add(Arguments.of("5",                          Double.class,  5d));
        arguments.add(Arguments.of("just-a-string",              Double.class,  ParseException.class));

        arguments.add(Arguments.of("5.5",                        Float.class,   5.5f));
        arguments.add(Arguments.of("5",                          Float.class,   5f));
        arguments.add(Arguments.of(Double.toString(Double.MAX_VALUE), Float.class, Float.POSITIVE_INFINITY));
        arguments.add(Arguments.of("just-a-string",              Float.class,   ParseException.class));

        // -- BigNumber Types (BigInteger, BigDecimal) --
        arguments.add(Arguments.of("5",                          BigInteger.class, new BigInteger("5")));
        arguments.add(Arguments.of("5.5",                        BigInteger.class, ParseException.class));
        arguments.add(Arguments.of("just-a-string",              BigInteger.class, ParseException.class));

        arguments.add(Arguments.of("5.5",                        BigDecimal.class, new BigDecimal("5.5")));
        arguments.add(Arguments.of("5",                          BigDecimal.class, new BigDecimal("5")));
        arguments.add(Arguments.of("just-a-string",              BigDecimal.class, ParseException.class));

        // -- Generic Number Type --
        arguments.add(Arguments.of("1.5",                        PatternOptionBuilder.NUMBER_VALUE, Double.valueOf(1.5)));
        arguments.add(Arguments.of("15",                         PatternOptionBuilder.NUMBER_VALUE, Long.valueOf(15)));
        arguments.add(Arguments.of("not a number",               PatternOptionBuilder.NUMBER_VALUE, ParseException.class));

        // -- Object Type (instantiation by class name) --
        arguments.add(Arguments.of(Instantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, new Instantiable()));
        arguments.add(Arguments.of(NotInstantiable.class.getName(), PatternOptionBuilder.OBJECT_VALUE, ParseException.class));
        arguments.add(Arguments.of("unknown",                    PatternOptionBuilder.OBJECT_VALUE, ParseException.class));

        // -- String Type --
        arguments.add(Arguments.of("String",                     PatternOptionBuilder.STRING_VALUE, "String"));

        // -- URL Type --
        final String urlString = "https://commons.apache.org";
        arguments.add(Arguments.of(urlString,                    PatternOptionBuilder.URL_VALUE,    new URL(urlString)));
        arguments.add(Arguments.of("Malformed-url",              PatternOptionBuilder.URL_VALUE,    ParseException.class));

        return arguments.stream();
    }

    @ParameterizedTest
    @MethodSource("createDateFixtures")
    void testCreateDate(final Date date) {
        assertEquals(date, TypeHandler.createDate(date.toString()));
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest(name = "[{index}] Input: \"{0}\", Type: {1}")
    @MethodSource("createValueTestParameters")
    void testCreateValue(final String str, final Class<?> type, final Object expectedResult) throws Exception {
        // This test validates both the modern createValue(String, Class<?>) API
        // and the deprecated createValue(String, Object) API.
        final Object typeAsObjectForDeprecatedApi = type;

        final boolean expectsException = expectedResult instanceof Class && Throwable.class.isAssignableFrom((Class<?>) expectedResult);

        if (expectsException) {
            final Class<? extends Throwable> expectedThrowable = (Class<? extends Throwable>) expectedResult;

            // Test modern API: createValue(String, Class)
            assertThrows(expectedThrowable, () -> TypeHandler.createValue(str, type),
                "Modern API should throw for invalid input");

            // Test deprecated API: createValue(String, Object)
            assertThrows(expectedThrowable, () -> TypeHandler.createValue(str, typeAsObjectForDeprecatedApi),
                "Deprecated API should throw for invalid input");
        } else {
            // Test modern API: createValue(String, Class)
            assertEquals(expectedResult, TypeHandler.createValue(str, type),
                "Modern API should produce correct value");

            // Test deprecated API: createValue(String, Object)
            assertEquals(expectedResult, TypeHandler.createValue(str, typeAsObjectForDeprecatedApi),
                "Deprecated API should produce correct value");
        }
    }

    /**
     * Used for Class and Object creation tests.
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
     * Used for negative Class and Object creation tests.
     */
    public static final class NotInstantiable {
        private NotInstantiable() {
            // private constructor to prevent instantiation
        }
    }

    @Test
    void testCreateNumber() throws ParseException {
        assertEquals(0L, TypeHandler.createNumber("0"));
        assertEquals(0d, TypeHandler.createNumber("0.0"));
    }
}