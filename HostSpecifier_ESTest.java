/*
 * Copyright (C) 2009 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.common.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link HostSpecifier}.
 *
 * <p>This version improves on an automatically generated test suite by using descriptive test names,
 * organizing tests by the method under test, and using standard JUnit 4 practices for
 * assertions and exception testing.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true)
public class HostSpecifierTest extends HostSpecifier_ESTest_scaffolding {

  // --- Tests for fromValid() ---

  @Test
  public void fromValid_shouldParseValidIPv4Address() {
    HostSpecifier specifier = HostSpecifier.fromValid("127.0.0.1");
    assertEquals("127.0.0.1", specifier.toString());
  }

  @Test
  public void fromValid_shouldParseValidUnbracketedIpv6Address() {
    // This test corrects the behavior of an original, generated test that failed
    // on valid IPv6 addresses, likely due to a test environment issue.
    HostSpecifier specifier = HostSpecifier.fromValid("::1");
    // The toString() method should canonicalize IPv6 addresses with brackets.
    assertEquals("[::1]", specifier.toString());
  }

  @Test(expected = NullPointerException.class)
  public void fromValid_shouldThrowNullPointerException_forNullInput() {
    HostSpecifier.fromValid(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fromValid_shouldThrowIllegalArgumentException_forInvalidHost() {
    // A host starting with a colon is not a valid IP address or domain.
    HostSpecifier.fromValid(":7");
  }

  @Test
  public void fromValid_shouldThrowIllegalArgumentException_forDomainWithUnrecognizedPublicSuffix() {
    String domain = "com.google.common.net.HostSpecifier";
    try {
      HostSpecifier.fromValid(domain);
      fail("Expected IllegalArgumentException for domain with no recognized public suffix");
    } catch (IllegalArgumentException e) {
      assertEquals("Domain name does not have a recognized public suffix: " + domain, e.getMessage());
    }
  }

  // --- Tests for from() ---

  @Test
  public void from_shouldParseValidIPv4Address() throws ParseException {
    HostSpecifier specifier = HostSpecifier.from("127.0.0.1");
    assertEquals("127.0.0.1", specifier.toString());
  }

  @Test
  public void from_shouldParseValidUnbracketedIpv6Address() throws ParseException {
    // This test corrects the behavior of an original, generated test.
    HostSpecifier specifier = HostSpecifier.from("::1");
    assertEquals("[::1]", specifier.toString());
  }

  @Test(expected = NullPointerException.class)
  public void from_shouldThrowNullPointerException_forNullInput() throws ParseException {
    HostSpecifier.from(null);
  }

  @Test
  public void from_shouldThrowParseException_forInvalidHostSpecifier() {
    String invalidHost = "jR:";
    try {
      HostSpecifier.from(invalidHost);
      fail("Expected ParseException for invalid host specifier");
    } catch (ParseException e) {
      assertEquals("Invalid host specifier: " + invalidHost, e.getMessage());
    }
  }

  // --- Tests for isValid() ---

  @Test
  public void isValid_shouldReturnTrue_forValidIPv4Address() {
    assertTrue(HostSpecifier.isValid("0.0.0.0"));
  }

  @Test
  public void isValid_shouldReturnTrue_forValidUnbracketedIpv6Address() {
    // This test corrects the behavior of an original, generated test.
    assertTrue(HostSpecifier.isValid("::1"));
  }

  @Test
  public void isValid_shouldReturnFalse_forInvalidHostSpecifier() {
    assertFalse(HostSpecifier.isValid("E$U|;qI:3"));
  }

  @Test(expected = NullPointerException.class)
  public void isValid_shouldThrowNullPointerException_forNullInput() {
    HostSpecifier.isValid(null);
  }

  // --- Tests for equals() and hashCode() ---

  @Test
  public void equals_and_hashCode_shouldAdhereToContract() throws ParseException {
    HostSpecifier specifier1 = HostSpecifier.from("127.0.0.1");
    HostSpecifier specifier2 = HostSpecifier.from("127.0.0.1");
    HostSpecifier specifier3 = HostSpecifier.from("192.168.0.1");

    // Reflexivity: an object equals itself
    assertEquals(specifier1, specifier1);

    // Symmetry: if a.equals(b), then b.equals(a)
    assertEquals(specifier1, specifier2);
    assertEquals(specifier2, specifier1);

    // HashCode contract: equal objects must have equal hash codes
    assertEquals(specifier1.hashCode(), specifier2.hashCode());

    // Inequality
    assertNotEquals(specifier1, specifier3);
    assertNotEquals(specifier1, null);
    assertNotEquals(specifier1, new Object());
  }

  // --- Tests for toString() ---

  @Test
  public void toString_shouldReturnOriginalString_forIPv4Address() {
    HostSpecifier specifier = HostSpecifier.fromValid("127.0.0.1");
    assertEquals("127.0.0.1", specifier.toString());
  }

  @Test
  public void toString_shouldAddBracketsToUnbracketedIpv6Address() {
    HostSpecifier specifier = HostSpecifier.fromValid("2001:db8::1");
    assertEquals("[2001:db8::1]", specifier.toString());
  }
}