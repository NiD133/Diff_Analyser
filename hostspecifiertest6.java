package com.google.common.net;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import java.text.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link HostSpecifier}.
 */
@RunWith(JUnit4.class)
public class HostSpecifierTest {

    private static final ImmutableList<String> VALID_SPECIFIERS =
        ImmutableList.of(
            "1.2.3.4",          // IPv4
            "2001:db8::1",      // Unbracketed IPv6
            "[2001:db8::1]",    // Bracketed IPv6
            "com",              // Top-level domain
            "google.com",       // Standard domain
            "foo.co.uk"         // Multi-level domain
        );

    private static final ImmutableList<String> INVALID_SPECIFIERS =
        ImmutableList.of(
            "1.2.3",                // Incomplete IPv4
            "2001:db8::1::::::0",   // Invalid IPv6
            "[2001:db8::1",         // Mismatched brackets
            "[::]:80",              // Port numbers are not allowed
            "foo.blah",             // Unrecognized top-level domain
            "",                     // Empty string
            "[google.com]"          // Brackets are not allowed on domains
        );

    @Test
    public void isValid_forValidSpecifiers_returnsTrue() {
        for (String spec : VALID_SPECIFIERS) {
            assertThat(HostSpecifier.isValid(spec)).isTrue();
        }
    }

    @Test
    public void isValid_forInvalidSpecifiers_returnsFalse() {
        for (String spec : INVALID_SPECIFIERS) {
            assertThat(HostSpecifier.isValid(spec)).isFalse();
        }
    }

    @Test
    public void from_forValidSpecifiers_succeeds() throws ParseException {
        for (String spec : VALID_SPECIFIERS) {
            // This test just checks that from() does not throw an exception for valid inputs.
            HostSpecifier.from(spec);
        }
    }

    @Test
    public void from_forInvalidSpecifiers_throwsParseException() {
        for (String spec : INVALID_SPECIFIERS) {
            ParseException e =
                assertThrows(
                    "for specifier: " + spec,
                    ParseException.class,
                    () -> HostSpecifier.from(spec));
            // The public API guarantees the cause is an IllegalArgumentException.
            assertThat(e).hasCauseThat().isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    public void fromValid_forValidSpecifiers_succeeds() {
        for (String spec : VALID_SPECIFIERS) {
            // This test just checks that fromValid() does not throw an exception for valid inputs.
            HostSpecifier.fromValid(spec);
        }
    }

    @Test
    public void fromValid_forInvalidSpecifiers_throwsIllegalArgumentException() {
        for (String spec : INVALID_SPECIFIERS) {
            assertThrows(
                "for specifier: " + spec,
                IllegalArgumentException.class,
                () -> HostSpecifier.fromValid(spec));
        }
    }

    @Test
    public void toString_returnsCanonicalForm() {
        // Domain names are lower-cased.
        assertThat(HostSpecifier.fromValid("Google.COM").toString()).isEqualTo("google.com");

        // IPv4 is unchanged.
        assertThat(HostSpecifier.fromValid("1.2.3.4").toString()).isEqualTo("1.2.3.4");

        // IPv6 is bracketed for URI compatibility.
        assertThat(HostSpecifier.fromValid("2001:db8::1").toString()).isEqualTo("[2001:db8::1]");
        assertThat(HostSpecifier.fromValid("[2001:db8::2]").toString()).isEqualTo("[2001:db8::2]");
    }

    @Test
    public void testEqualsAndHashCode() {
        new EqualsTester()
            .addEqualityGroup(
                HostSpecifier.fromValid("google.com"), HostSpecifier.fromValid("Google.COM"))
            .addEqualityGroup(HostSpecifier.fromValid("1.2.3.4"))
            .addEqualityGroup(
                HostSpecifier.fromValid("2001:db8::1"), HostSpecifier.fromValid("[2001:db8::1]"))
            .testEquals();
    }

    @Test
    public void testNulls() {
        NullPointerTester tester = new NullPointerTester();
        tester.testAllPublicStaticMethods(HostSpecifier.class);
        // Test an instance to cover instance methods like equals(), hashCode(), and toString().
        tester.testAllPublicInstanceMethods(HostSpecifier.fromValid("google.com"));
    }
}