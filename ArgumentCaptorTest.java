package org.mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.validateMockitoUsage;

import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;

class ArgumentCaptorTest {

  /**
   * Ensures Mockito's internal state is clean after each test. This prevents unexpected behavior
   * in subsequent tests due to matcher misuse.  Any unverified mock invocations with argument matchers will throw an exception.
   * The try-catch block handles (and ignores) potential InvalidUseOfMatchersException that can
   * occur during cleanup if matchers were incorrectly used.
   */
  @AfterEach
  void validateMockitoState() {
    try {
      validateMockitoUsage();
    } catch (InvalidUseOfMatchersException ignored) {
      // Ignore exceptions during cleanup, as they don't affect the test's outcome.
    }
  }

  @Test
  void captureMethodReturnsNullByDefault() {
    // Given
    ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);

    // When
    // Capturing an argument with no actual method call defaults to null.

    // Then
    assertThat(captor.capture()).isNull();
  }

  @Test
  void getCaptorTypeReturnsTheCorrectClass() {
    // Given
    class Foo {}

    class Bar {}

    ArgumentCaptor<Foo> fooCaptor = ArgumentCaptor.forClass(Foo.class);
    ArgumentCaptor<Bar> barCaptor = ArgumentCaptor.forClass(Bar.class);

    // When
    // Retrieving the captor type.

    // Then
    assertThat(fooCaptor.getCaptorType()).isEqualTo(Foo.class);
    assertThat(barCaptor.getCaptorType()).isEqualTo(Bar.class);
  }

  @Test
  void captorInfersTypeFromUsage() {
    // Given
    // Creating an ArgumentCaptor without specifying the class directly.
    ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.captor();

    // When
    // Retrieving the captor type.

    // Then
    // The captor type should be inferred from the generic type declaration.
    assertThat(captor.getCaptorType()).isEqualTo(Map.class);
  }

  @Test
  void captorWithExplicitVarargsThrowsIllegalArgumentException() {
    // Expect: Passing explicit arguments to ArgumentCaptor.captor() should throw an exception.

    // Test case 1: Single argument.
    assertThatThrownBy(() -> ArgumentCaptor.captor(1234L))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(null); // Message may vary, but the exception type is important

    // Test case 2: Multiple arguments.
    assertThatThrownBy(() -> ArgumentCaptor.captor("this shouldn't", "be here"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(null);

    // Test case 3: Null varargs array.
    assertThatThrownBy(() -> ArgumentCaptor.<String>captor((String[]) null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(null);
  }
}