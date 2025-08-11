package com.google.common.net;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import java.text.ParseException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

/**
 * Test suite for {@link HostSpecifier}. This suite verifies that HostSpecifier correctly
 * interprets valid and invalid host specifiers, and that it integrates properly with
 * InetAddresses and InternetDomainName.
 */
@NullUnmarked
public final class HostSpecifierTest extends TestCase {

  // Valid IP addresses for testing
  private static final ImmutableList<String> VALID_IP_ADDRESSES =
      ImmutableList.of("1.2.3.4", "2001:db8::1", "[2001:db8::1]");

  // Invalid IP addresses for testing
  private static final ImmutableList<String> INVALID_IP_ADDRESSES =
      ImmutableList.of("1.2.3", "2001:db8::1::::::0", "[2001:db8::1", "[::]:80");

  // Valid domain names for testing
  private static final ImmutableList<String> VALID_DOMAINS =
      ImmutableList.of("com", "google.com", "foo.co.uk");

  // Invalid domain names for testing
  private static final ImmutableList<String> INVALID_DOMAINS =
      ImmutableList.of("foo.blah", "", "[google.com]");

  // Test valid IP addresses
  public void testValidIpAddresses() throws ParseException {
    for (String ip : VALID_IP_ADDRESSES) {
      assertValidSpecifier(ip);
    }
  }

  // Test invalid IP addresses
  public void testInvalidIpAddresses() {
    for (String ip : INVALID_IP_ADDRESSES) {
      assertInvalidSpecifier(ip);
    }
  }

  // Test valid domain names
  public void testValidDomains() throws ParseException {
    for (String domain : VALID_DOMAINS) {
      assertValidSpecifier(domain);
    }
  }

  // Test invalid domain names
  public void testInvalidDomains() {
    for (String domain : INVALID_DOMAINS) {
      assertInvalidSpecifier(domain);
    }
  }

  // Test equality of host specifiers
  public void testHostSpecifierEquality() {
    new EqualsTester()
        .addEqualityGroup(createHostSpecifier("1.2.3.4"), createHostSpecifier("1.2.3.4"))
        .addEqualityGroup(createHostSpecifier("2001:db8::1"), createHostSpecifier("2001:db8::1"), createHostSpecifier("[2001:db8::1]"))
        .addEqualityGroup(createHostSpecifier("2001:db8::2"))
        .addEqualityGroup(createHostSpecifier("google.com"), createHostSpecifier("google.com"))
        .addEqualityGroup(createHostSpecifier("www.google.com"))
        .testEquals();
  }

  // Test handling of null values
  public void testNullHandling() {
    NullPointerTester tester = new NullPointerTester();
    tester.testAllPublicStaticMethods(HostSpecifier.class);
    tester.testAllPublicInstanceMethods(HostSpecifier.fromValid("google.com"));
  }

  // Helper method to create a HostSpecifier from a valid specifier
  private static HostSpecifier createHostSpecifier(String specifier) {
    return HostSpecifier.fromValid(specifier);
  }

  // Assert that a specifier is valid
  private void assertValidSpecifier(String specifier) throws ParseException {
    HostSpecifier unused = HostSpecifier.fromValid(specifier);
    unused = HostSpecifier.from(specifier);
    assertTrue(HostSpecifier.isValid(specifier));
  }

  // Assert that a specifier is invalid
  private void assertInvalidSpecifier(String specifier) {
    try {
      HostSpecifier.fromValid(specifier);
      fail("Expected IllegalArgumentException for: " + specifier);
    } catch (IllegalArgumentException expected) {
      // Expected exception
    }

    try {
      HostSpecifier.from(specifier);
      fail("Expected ParseException for: " + specifier);
    } catch (ParseException expected) {
      assertThat(expected).hasCauseThat().isInstanceOf(IllegalArgumentException.class);
    }

    assertFalse(HostSpecifier.isValid(specifier));
  }
}