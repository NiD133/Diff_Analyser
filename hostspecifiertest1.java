package com.google.common.net;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.collect.ImmutableList;
import java.text.ParseException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link HostSpecifier}.
 */
public class HostSpecifierTest {

    private static final ImmutableList<String> VALID_IP_ADDRESSES =
        ImmutableList.of("1.2.3.4", "2001:db8::1", "[2001:db8::1]");

    private static final ImmutableList<String> INVALID_IP_ADDRESSES =
        ImmutableList.of("1.2.3", "2001:db8::1::::::0", "[2001:db8::1", "[::]:80");

    private static final ImmutableList<String> VALID_DOMAINS =
        ImmutableList.of("com", "google.com", "foo.co.uk");

    private static final ImmutableList<String> INVALID_DOMAINS =
        ImmutableList.of("foo.blah", "", "[google.com]");

    private static Stream<String> validSpecifiers() {
        return Stream.concat(VALID_IP_ADDRESSES.stream(), VALID_DOMAINS.stream());
    }

    private static Stream<String> invalidSpecifiers() {
        return Stream.concat(INVALID_IP_ADDRESSES.stream(), INVALID_DOMAINS.stream());
    }

    @ParameterizedTest
    @MethodSource("validSpecifiers")
    void isValid_forValidSpecifier_returnsTrue(String specifier) {
        assertThat(HostSpecifier.isValid(specifier)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("invalidSpecifiers")
    void isValid_forInvalidSpecifier_returnsFalse(String specifier) {
        assertThat(HostSpecifier.isValid(specifier)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("validSpecifiers")
    void fromValid_forValidSpecifier_succeeds(String specifier) {
        // Asserts that fromValid does not throw an exception for valid inputs.
        assertDoesNotThrow(() -> HostSpecifier.fromValid(specifier));
    }

    @ParameterizedTest
    @MethodSource("invalidSpecifiers")
    void fromValid_forInvalidSpecifier_throwsIllegalArgumentException(String specifier) {
        assertThrows(IllegalArgumentException.class, () -> HostSpecifier.fromValid(specifier));
    }

    @ParameterizedTest
    @MethodSource("validSpecifiers")
    void from_forValidSpecifier_succeeds(String specifier) {
        // Asserts that from() does not throw an exception for valid inputs.
        assertDoesNotThrow(() -> HostSpecifier.from(specifier));
    }

    @ParameterizedTest
    @MethodSource("invalidSpecifiers")
    void from_forInvalidSpecifier_throwsParseExceptionWithCause(String specifier) {
        ParseException exception =
            assertThrows(ParseException.class, () -> HostSpecifier.from(specifier));
        assertThat(exception).hasCauseThat().isInstanceOf(IllegalArgumentException.class);
    }
}