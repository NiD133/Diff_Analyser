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
 * Sanity tests for HostSpecifier. The intent is to verify that the entry points
 * (fromValid, from, isValid) are wired consistently and to illustrate expected behavior
 * with small, easy-to-scan data sets. Deep edge cases are covered by InetAddresses and
 * InternetDomainName tests.
 */
@RunWith(JUnit4.class)
public final class HostSpecifierTest {

  // Valid/invalid inputs are grouped and commented for quick scanning.

  private static final ImmutableList<String> VALID_IPS = ImmutableList.of(
      "1.2.3.4",           // IPv4
      "2001:db8::1",       // IPv6 (no brackets)
      "[2001:db8::1]"      // IPv6 (with brackets)
  );

  private static final ImmutableList<String> INVALID_IPS = ImmutableList.of(
      "1.2.3",             // Too few octets
      "2001:db8::1::::::0",// Too many groups
      "[2001:db8::1",      // Missing closing bracket
      "[::]:80"            // Port is not allowed in HostSpecifier
  );

  private static final ImmutableList<String> VALID_DOMAINS = ImmutableList.of(
      "com",               // Public suffix alone is allowed
      "google.com",
      "foo.co.uk"
  );

  private static final ImmutableList<String> INVALID_DOMAINS = ImmutableList.of(
      "foo.blah",          // Unrecognized TLD
      "",                  // Empty
      "[google.com]"       // Brackets are not allowed around domain names
  );

  @Test
  public void validIpAddresses_areAccepted() throws Exception {
    for (String input : VALID_IPS) {
      assertValid(input);
    }
  }

  @Test
  public void invalidIpAddresses_areRejected() {
    for (String input : INVALID_IPS) {
      assertInvalid(input);
    }
  }

  @Test
  public void validDomainNames_areAccepted() throws Exception {
    for (String input : VALID_DOMAINS) {
      assertValid(input);
    }
  }

  @Test
  public void invalidDomainNames_areRejected() {
    for (String input : INVALID_DOMAINS) {
      assertInvalid(input);
    }
  }

  @Test
  public void equalsHashCode_contract() {
    new EqualsTester()
        .addEqualityGroup(h("1.2.3.4"), h("1.2.3.4"))
        .addEqualityGroup(h("2001:db8::1"), h("2001:db8::1"), h("[2001:db8::1]"))
        .addEqualityGroup(h("2001:db8::2"))
        .addEqualityGroup(h("google.com"), h("google.com"))
        .addEqualityGroup(h("www.google.com"))
        .testEquals();
  }

  @Test
  public void publicApi_nullSafety() {
    NullPointerTester tester = new NullPointerTester();
    tester.testAllPublicStaticMethods(HostSpecifier.class);
    tester.testAllPublicInstanceMethods(HostSpecifier.fromValid("google.com"));
  }

  @Test
  public void toString_bracketsIpv6Addresses() throws Exception {
    assertThat(HostSpecifier.from("2001:db8::1").toString()).isEqualTo("[2001:db8::1]");
    assertThat(HostSpecifier.from("[2001:db8::1]").toString()).isEqualTo("[2001:db8::1]");
  }

  @Test
  public void toString_lowercasesDomain() throws Exception {
    assertThat(HostSpecifier.from("GoOgLe.CoM").toString()).isEqualTo("google.com");
  }

  private static HostSpecifier h(String spec) {
    return HostSpecifier.fromValid(spec);
  }

  /**
   * Asserts that all entry points accept the spec and agree on the canonical result.
   */
  private static void assertValid(String spec) throws ParseException {
    HostSpecifier viaFromValid = HostSpecifier.fromValid(spec);
    HostSpecifier viaFrom = HostSpecifier.from(spec);

    assertThat(viaFrom).isEqualTo(viaFromValid);
    assertThat(HostSpecifier.isValid(spec)).isTrue();
  }

  /**
   * Asserts that all entry points reject the spec and report the expected exceptions.
   */
  private static void assertInvalid(String spec) {
    IllegalArgumentException iae =
        assertThrows(IllegalArgumentException.class, () -> HostSpecifier.fromValid(spec));

    ParseException pe =
        assertThrows(ParseException.class, () -> HostSpecifier.from(spec));
    assertThat(pe).hasCauseThat().isInstanceOf(IllegalArgumentException.class);

    assertThat(HostSpecifier.isValid(spec)).isFalse();

    // Suppress unused warning for clarity; we only assert types above.
    assertThat(iae).isNotNull();
  }
}