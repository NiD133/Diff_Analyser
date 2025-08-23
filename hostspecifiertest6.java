package com.google.common.net;

import static com.google.common.truth.Truth.assertThat;
import com.google.common.collect.ImmutableList;
import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import java.text.ParseException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

public class HostSpecifierTestTest6 extends TestCase {

    private static final ImmutableList<String> GOOD_IPS = ImmutableList.of("1.2.3.4", "2001:db8::1", "[2001:db8::1]");

    private static final ImmutableList<String> BAD_IPS = ImmutableList.of("1.2.3", "2001:db8::1::::::0", "[2001:db8::1", "[::]:80");

    private static final ImmutableList<String> GOOD_DOMAINS = ImmutableList.of("com", "google.com", "foo.co.uk");

    private static final ImmutableList<String> BAD_DOMAINS = ImmutableList.of("foo.blah", "", "[google.com]");

    private static HostSpecifier spec(String specifier) {
        return HostSpecifier.fromValid(specifier);
    }

    private void assertGood(String spec) throws ParseException {
        // Throws exception if not working correctly
        HostSpecifier unused = HostSpecifier.fromValid(spec);
        unused = HostSpecifier.from(spec);
        assertTrue(HostSpecifier.isValid(spec));
    }

    private void assertBad(String spec) {
        try {
            HostSpecifier.fromValid(spec);
            fail("Should have thrown IllegalArgumentException: " + spec);
        } catch (IllegalArgumentException expected) {
        }
        try {
            HostSpecifier.from(spec);
            fail("Should have thrown ParseException: " + spec);
        } catch (ParseException expected) {
            assertThat(expected).hasCauseThat().isInstanceOf(IllegalArgumentException.class);
        }
        assertFalse(HostSpecifier.isValid(spec));
    }

    public void testNulls() {
        NullPointerTester tester = new NullPointerTester();
        tester.testAllPublicStaticMethods(HostSpecifier.class);
        tester.testAllPublicInstanceMethods(HostSpecifier.fromValid("google.com"));
    }
}
