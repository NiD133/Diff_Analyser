package com.google.common.net;

import static org.junit.Assert.*;

import java.text.ParseException;
import org.junit.Test;

/**
 * Focused, readable tests for HostSpecifier.
 *
 * These tests avoid EvoSuite-specific machinery and use descriptive names,
 * clear Arrange-Act-Assert structure, and stable assertions that don’t rely
 * on brittle exception messages.
 */
public class HostSpecifierTest {

  // isValid

  @Test
  public void isValid_acceptsIpv4() {
    assertTrue(HostSpecifier.isValid("127.0.0.1"));
  }

  @Test
  public void isValid_acceptsIpv6_unbracketed() {
    assertTrue(HostSpecifier.isValid("2001:db8::1"));
  }

  @Test
  public void isValid_acceptsIpv6_bracketed() {
    assertTrue(HostSpecifier.isValid("[2001:db8::1]"));
  }

  @Test
  public void isValid_rejectsHostWithPort() {
    assertFalse(HostSpecifier.isValid("example.com:80"));
  }

  @Test(expected = NullPointerException.class)
  public void isValid_null_throwsNpe() {
    HostSpecifier.isValid(null);
  }

  // fromValid

  @Test
  public void fromValid_ipv4_roundTrips() {
    HostSpecifier host = HostSpecifier.fromValid("127.0.0.1");
    assertEquals("127.0.0.1", host.toString());
  }

  @Test
  public void fromValid_domain_isLowerCased() {
    HostSpecifier host = HostSpecifier.fromValid("GOOGLE.COM");
    assertEquals("google.com", host.toString());
  }

  @Test
  public void fromValid_ipv6_isBracketedInToString() {
    HostSpecifier host = HostSpecifier.fromValid("2001:db8::1");
    assertEquals("[2001:db8::1]", host.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void fromValid_rejectsDomainWithoutPublicSuffix() {
    // "localhost" does not have a recognized public suffix.
    HostSpecifier.fromValid("localhost");
  }

  @Test(expected = IllegalArgumentException.class)
  public void fromValid_rejectsHostWithPort() {
    HostSpecifier.fromValid("example.com:80");
  }

  @Test(expected = NullPointerException.class)
  public void fromValid_null_throwsNpe() {
    HostSpecifier.fromValid(null);
  }

  // from

  @Test
  public void from_ipv4_roundTrips() throws ParseException {
    HostSpecifier host = HostSpecifier.from("127.0.0.1");
    assertEquals("127.0.0.1", host.toString());
  }

  @Test
  public void from_rejectsHostWithPort_parseException() {
    try {
      HostSpecifier.from("example.com:80");
      fail("Expected ParseException");
    } catch (ParseException expected) {
      // type-only assertion keeps the test stable
    }
  }

  @Test(expected = NullPointerException.class)
  public void from_null_throwsNpe() throws ParseException {
    HostSpecifier.from(null);
  }

  // equals, hashCode, toString

  @Test
  public void equals_sameCanonicalForm_isTrue() {
    HostSpecifier a = HostSpecifier.fromValid("127.0.0.1");
    HostSpecifier b = HostSpecifier.fromValid("127.0.0.1");
    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
  }

  @Test
  public void equals_self_isTrue() {
    HostSpecifier a = HostSpecifier.fromValid("127.0.0.1");
    assertTrue(a.equals(a));
  }

  @Test
  public void equals_differentType_isFalse() {
    HostSpecifier a = HostSpecifier.fromValid("127.0.0.1");
    assertFalse(a.equals(new Object()));
  }

  @Test
  public void toString_ipv4_returnsCanonical() {
    HostSpecifier host = HostSpecifier.fromValid("127.0.0.1");
    assertEquals("127.0.0.1", host.toString());
  }
}