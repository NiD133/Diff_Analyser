/*
 * Copyright (C) 2009 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.net;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import java.text.ParseException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link HostSpecifier}. This is a relatively cursory test, as HostSpecifier
 * is a thin wrapper around {@link InetAddresses} and {@link InternetDomainName}; the unit tests for
 * those classes explore numerous corner cases. The intent here is to confirm that everything is
 * wired up properly.
 *
 * @author Craig Berry
 */
@NullUnmarked
public final class HostSpecifierTest extends TestCase {

  // Valid IP address test cases
  private static final ImmutableList<String> VALID_IPV4_ADDRESSES =
      ImmutableList.of("1.2.3.4");
  
  private static final ImmutableList<String> VALID_IPV6_ADDRESSES =
      ImmutableList.of("2001:db8::1", "[2001:db8::1]");

  private static final ImmutableList<String> VALID_IP_ADDRESSES =
      ImmutableList.<String>builder()
          .addAll(VALID_IPV4_ADDRESSES)
          .addAll(VALID_IPV6_ADDRESSES)
          .build();

  // Invalid IP address test cases
  private static final ImmutableList<String> INVALID_IP_ADDRESSES =
      ImmutableList.of(
          "1.2.3",                    // Incomplete IPv4
          "2001:db8::1::::::0",       // Malformed IPv6
          "[2001:db8::1",             // Missing closing bracket
          "[::]:80"                   // IPv6 with port (not allowed)
      );

  // Valid domain name test cases
  private static final ImmutableList<String> VALID_DOMAIN_NAMES =
      ImmutableList.of(
          "com",           // Top-level domain
          "google.com",    // Standard domain
          "foo.co.uk"      // Domain with country code
      );

  // Invalid domain name test cases
  private static final ImmutableList<String> INVALID_DOMAIN_NAMES =
      ImmutableList.of(
          "foo.blah",        // Invalid TLD
          "",                // Empty string
          "[google.com]"     // Domain in brackets (not allowed)
      );

  public void testValidIpAddresses_ShouldBeAccepted() throws ParseException {
    for (String validIpAddress : VALID_IP_ADDRESSES) {
      assertHostSpecifierIsValid(validIpAddress);
    }
  }

  public void testInvalidIpAddresses_ShouldBeRejected() {
    for (String invalidIpAddress : INVALID_IP_ADDRESSES) {
      assertHostSpecifierIsInvalid(invalidIpAddress);
    }
  }

  public void testValidDomainNames_ShouldBeAccepted() throws ParseException {
    for (String validDomainName : VALID_DOMAIN_NAMES) {
      assertHostSpecifierIsValid(validDomainName);
    }
  }

  public void testInvalidDomainNames_ShouldBeRejected() {
    for (String invalidDomainName : INVALID_DOMAIN_NAMES) {
      assertHostSpecifierIsInvalid(invalidDomainName);
    }
  }

  public void testEquals_ShouldTreatEquivalentSpecifiersAsEqual() {
    new EqualsTester()
        // IPv4 addresses should equal themselves
        .addEqualityGroup(
            createHostSpecifier("1.2.3.4"), 
            createHostSpecifier("1.2.3.4"))
        // IPv6 addresses with and without brackets should be equal
        .addEqualityGroup(
            createHostSpecifier("2001:db8::1"), 
            createHostSpecifier("2001:db8::1"), 
            createHostSpecifier("[2001:db8::1]"))
        // Different IPv6 addresses should not be equal
        .addEqualityGroup(
            createHostSpecifier("2001:db8::2"))
        // Domain names should equal themselves
        .addEqualityGroup(
            createHostSpecifier("google.com"), 
            createHostSpecifier("google.com"))
        // Different domain names should not be equal
        .addEqualityGroup(
            createHostSpecifier("www.google.com"))
        .testEquals();
  }

  public void testNullHandling_ShouldRejectNullInputs() {
    NullPointerTester nullTester = new NullPointerTester();

    // Test all public static methods reject null inputs
    nullTester.testAllPublicStaticMethods(HostSpecifier.class);
    
    // Test all public instance methods reject null inputs
    HostSpecifier validHostSpecifier = HostSpecifier.fromValid("google.com");
    nullTester.testAllPublicInstanceMethods(validHostSpecifier);
  }

  /**
   * Helper method to create a HostSpecifier from a known-valid string.
   */
  private static HostSpecifier createHostSpecifier(String validSpecifier) {
    return HostSpecifier.fromValid(validSpecifier);
  }

  /**
   * Asserts that the given specifier is considered valid by all HostSpecifier validation methods.
   * 
   * @param specifier the host specifier string to validate
   * @throws ParseException if the specifier is unexpectedly invalid
   */
  private void assertHostSpecifierIsValid(String specifier) throws ParseException {
    // Should not throw any exceptions
    HostSpecifier fromValidResult = HostSpecifier.fromValid(specifier);
    HostSpecifier fromResult = HostSpecifier.from(specifier);
    
    // Validation method should return true
    assertTrue("Expected HostSpecifier.isValid() to return true for: " + specifier, 
               HostSpecifier.isValid(specifier));
  }

  /**
   * Asserts that the given specifier is considered invalid by all HostSpecifier validation methods.
   * 
   * @param specifier the host specifier string that should be invalid
   */
  private void assertHostSpecifierIsInvalid(String specifier) {
    // fromValid() should throw IllegalArgumentException
    try {
      HostSpecifier.fromValid(specifier);
      fail("Expected IllegalArgumentException for invalid specifier: " + specifier);
    } catch (IllegalArgumentException expected) {
      // This is the expected behavior
    }

    // from() should throw ParseException with IllegalArgumentException as cause
    try {
      HostSpecifier.from(specifier);
      fail("Expected ParseException for invalid specifier: " + specifier);
    } catch (ParseException expected) {
      assertThat(expected)
          .hasCauseThat()
          .isInstanceOf(IllegalArgumentException.class);
    }

    // isValid() should return false
    assertFalse("Expected HostSpecifier.isValid() to return false for: " + specifier,
                HostSpecifier.isValid(specifier));
  }
}