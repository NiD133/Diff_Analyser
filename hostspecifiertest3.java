package com.google.common.net;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link HostSpecifier}. This test class focuses on domain name validation.
 */
@DisplayName("HostSpecifier")
class HostSpecifierTest {

  @Nested
  @DisplayName("when parsing domain names")
  class DomainNameParsing {

    @ParameterizedTest
    @ValueSource(strings = {"com", "google.com", "foo.co.uk"})
    @DisplayName("should succeed for valid public suffixes or domains")
    void factoryMethods_withValidDomain_succeed(String validDomain) throws ParseException {
      // HostSpecifier.isValid() should return true for valid domains.
      assertThat(HostSpecifier.isValid(validDomain)).isTrue();

      // HostSpecifier.from() should successfully parse the domain.
      HostSpecifier fromSpecifier = HostSpecifier.from(validDomain);
      assertThat(fromSpecifier.toString()).isEqualTo(validDomain);

      // HostSpecifier.fromValid() should also parse, and create an equal instance.
      HostSpecifier fromValidSpecifier = HostSpecifier.fromValid(validDomain);
      assertThat(fromValidSpecifier).isEqualTo(fromSpecifier);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "foo.blah",     // Not a recognized public suffix
        "",             // Empty string is invalid
        "[google.com]"  // Brackets are only for IP literals
    })
    @DisplayName("should fail for invalid domains")
    void factoryMethods_withInvalidDomain_fail(String invalidDomain) {
      // HostSpecifier.isValid() should return false for invalid domains.
      assertThat(HostSpecifier.isValid(invalidDomain)).isFalse();

      // HostSpecifier.fromValid() should throw an IllegalArgumentException.
      assertThrows(IllegalArgumentException.class, () -> HostSpecifier.fromValid(invalidDomain));

      // HostSpecifier.from() should throw a ParseException, caused by an IAE.
      ParseException e = assertThrows(ParseException.class, () -> HostSpecifier.from(invalidDomain));
      assertThat(e).hasCauseThat().isInstanceOf(IllegalArgumentException.class);
    }
  }
}