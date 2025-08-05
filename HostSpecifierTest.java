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
import static org.junit.Assert.assertThrows;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import java.text.ParseException;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link HostSpecifier}. This is a relatively cursory test, as HostSpecifier is a thin
 * wrapper around {@link InetAddresses} and {@link InternetDomainName}; the unit tests for those
 * classes explore numerous corner cases. The intent here is to confirm that everything is wired up
 * properly.
 *
 * @author Craig Berry
 */
@NullUnmarked
@RunWith(JUnit4.class)
public final class HostSpecifierTest {

  private static final ImmutableList<String> VALID_IPS =
      ImmutableList.of("1.2.3.4", "2001:db8::1", "[2001:db8::1]");

  private static final ImmutableList<String> INVALID_IPS =
      ImmutableList.of("1.2.3", "2001:db8::1::::::0", "[2001:db8::1", "[::]:80");

  private static final ImmutableList<String> VALID_DOMAINS =
      ImmutableList.of("com", "google.com", "foo.co.uk");

  private static final ImmutableList<String> INVALID_DOMAINS =
      ImmutableList.of("foo.blah", "", "[google.com]");

  @Test
  public void testValidSpecifiers_areAccepted() throws ParseException {
    Iterable<String> validSpecifiers = Iterables.concat(VALID_IPS, VALID_DOMAINS);

    for (String spec : validSpecifiers) {
      String message = "for specifier: " + spec;
      assertThat(HostSpecifier.isValid(spec)).withMessage(message).isTrue();

      // The following factory methods should not throw an exception.
      // The test will fail if they do.
      HostSpecifier.fromValid(spec);
      HostSpecifier.from(spec);
    }
  }

  @Test
  public void testInvalidSpecifiers_areRejected() {
    Iterable<String> invalidSpecifiers = Iterables.concat(INVALID_IPS, INVALID_DOMAINS);

    for (String spec : invalidSpecifiers) {
      String message = "for specifier: " + spec;

      assertThat(HostSpecifier.isValid(spec)).withMessage(message).isFalse();

      assertThrows(
          message, IllegalArgumentException.class, () -> HostSpecifier.fromValid(spec));

      ParseException e =
          assertThrows(message, ParseException.class, () -> HostSpecifier.from(spec));
      assertThat(e).hasCauseThat().isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Test
  public void testEqualsAndHashCode() {
    new EqualsTester()
        .addEqualityGroup(spec("1.2.3.4"), spec("1.2.3.4"))
        .addEqualityGroup(spec("2001:db8::1"), spec("2001:db8::1"), spec("[2001:db8::1]"))
        .addEqualityGroup(spec("2001:db8::2"))
        .addEqualityGroup(spec("google.com"), spec("google.com"))
        .addEqualityGroup(spec("www.google.com"))
        .testEquals();
  }

  private static HostSpecifier spec(String specifier) {
    return HostSpecifier.fromValid(specifier);
  }

  @Test
  public void testNulls() {
    new NullPointerTester()
        .testAllPublicStaticMethods(HostSpecifier.class)
        .testAllPublicInstanceMethods(HostSpecifier.fromValid("google.com"));
  }
}