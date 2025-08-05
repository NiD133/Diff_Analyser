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
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import java.text.ParseException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

/**
 * {@link TestCase} for {@link HostSpecifier}. This is a relatively cursory test, as HostSpecifier
 * is a thin wrapper around {@link InetAddresses} and {@link InternetDomainName}; the unit tests for
 * those classes explore numerous corner cases. The intent here is to confirm that everything is
 * wired up properly.
 *
 * @author Craig Berry
 */
@NullUnmarked
public final class HostSpecifierTest extends TestCase {

  private static final ImmutableList<String> VALID_IPS =
      ImmutableList.of("1.2.3.4", "2001:db8::1", "[2001:db8::1]");

  private static final ImmutableList<String> INVALID_IPS =
      ImmutableList.of("1.2.3", "2001:db8::1::::::0", "[2001:db8::1", "[::]:80");

  private static final ImmutableList<String> VALID_DOMAINS =
      ImmutableList.of("com", "google.com", "foo.co.uk");

  private static final ImmutableList<String> INVALID_DOMAINS =
      ImmutableList.of("foo.blah", "", "[google.com]");

  public void testValidIpAddresses() throws ParseException {
    for (String spec : VALID_IPS) {
      assertValidSpecifier(spec);
    }
  }

  public void testInvalidIpAddresses() {
    for (String spec : INVALID_IPS) {
      assertInvalidSpecifier(spec);
    }
  }

  public void testValidDomains() throws ParseException {
    for (String spec : VALID_DOMAINS) {
      assertValidSpecifier(spec);
    }
  }

  public void testInvalidDomains() {
    for (String spec : INVALID_DOMAINS) {
      assertInvalidSpecifier(spec);
    }
  }

  public void testEquality() {
    new EqualsTester()
        .addEqualityGroup(hostSpecifier("1.2.3.4"), hostSpecifier("1.2.3.4"))
        .addEqualityGroup(
            hostSpecifier("2001:db8::1"), 
            hostSpecifier("2001:db8::1"), 
            hostSpecifier("[2001:db8::1]")
        )
        .addEqualityGroup(hostSpecifier("2001:db8::2"))
        .addEqualityGroup(hostSpecifier("google.com"), hostSpecifier("google.com"))
        .addEqualityGroup(hostSpecifier("www.google.com"))
        .testEquals();
  }

  private static HostSpecifier hostSpecifier(String specifier) {
    return HostSpecifier.fromValid(specifier);
  }

  public void testNulls() {
    NullPointerTester tester = new NullPointerTester();
    tester.testAllPublicStaticMethods(HostSpecifier.class);
    tester.testAllPublicInstanceMethods(HostSpecifier.fromValid("google.com"));
  }

  private void assertValidSpecifier(String spec) throws ParseException {
    // Verify fromValid() accepts valid specifier
    try {
      HostSpecifier.fromValid(spec);
    } catch (IllegalArgumentException e) {
      fail(String.format("HostSpecifier.fromValid(%s) threw unexpected exception: %s", spec, e));
    }

    // Verify from() accepts valid specifier
    try {
      HostSpecifier.from(spec);
    } catch (ParseException e) {
      fail(String.format("HostSpecifier.from(%s) threw unexpected exception: %s", spec, e));
    }

    // Verify isValid() returns true for valid specifier
    assertTrue(
        String.format("Expected HostSpecifier.isValid(%s) to return true", spec),
        HostSpecifier.isValid(spec));
  }

  private void assertInvalidSpecifier(String spec) {
    // Verify fromValid() rejects invalid specifier
    try {
      HostSpecifier.fromValid(spec);
      fail(String.format("HostSpecifier.fromValid(%s) should have thrown IllegalArgumentException", spec));
    } catch (IllegalArgumentException expected) {
      // Expected behavior
    }

    // Verify from() rejects invalid specifier with proper cause
    try {
      HostSpecifier.from(spec);
      fail(String.format("HostSpecifier.from(%s) should have thrown ParseException", spec));
    } catch (ParseException e) {
      Throwable cause = e.getCause();
      if (cause == null || !(cause instanceof IllegalArgumentException)) {
        fail(String.format(
            "Expected ParseException for '%s' to have IllegalArgumentException cause, but got: %s", 
            spec, cause));
      }
    }

    // Verify isValid() returns false for invalid specifier
    assertFalse(
        String.format("Expected HostSpecifier.isValid(%s) to return false", spec),
        HostSpecifier.isValid(spec));
  }
}