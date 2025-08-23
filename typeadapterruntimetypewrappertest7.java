package com.google.gson.functional;

import static com.google.common.truth.Truth.assertThat;
import com.google.gson.Gson;
import org.junit.Test;

/**
 * Tests for {@code TypeAdapterRuntimeTypeWrapper}, focusing on serialization behavior with
 * polymorphic and recursive types.
 */
public class TypeAdapterRuntimeTypeWrapperTest {

  private static class RecursiveParent {
    @SuppressWarnings("unused")
    RecursiveParent field;
  }

  private static class RecursiveChild extends RecursiveParent {
    @SuppressWarnings("unused")
    final int value;

    RecursiveChild(int value) {
      this.value = value;
    }
  }

  /**
   * This test verifies that Gson correctly serializes the runtime type of a field, even when the
   * field's declared type is recursive.
   *
   * <p>Internally, when Gson creates an adapter for a recursive type like {@code RecursiveParent},
   * it uses a temporary placeholder adapter for the recursive {@code field}. This test ensures that
   * when this placeholder is later used, it correctly delegates to the adapter for the field's
   * actual runtime type ({@code RecursiveChild}), not just its declared type ({@code
   * RecursiveParent}).
   */
  @Test
  public void serializationOfRecursiveType_shouldUseRuntimeType() {
    // Arrange
    Gson gson = new Gson();
    RecursiveParent object = new RecursiveParent();
    // The 'field' is declared as RecursiveParent, but we assign a subclass instance.
    object.field = new RecursiveChild(2);
    String expectedJson = "{\"field\":{\"value\":2}}";

    // Act
    String actualJson = gson.toJson(object);

    // Assert
    // Verify that the subclass field 'value' is present in the JSON,
    // proving that the runtime type was used for serialization.
    assertThat(actualJson).isEqualTo(expectedJson);
  }
}