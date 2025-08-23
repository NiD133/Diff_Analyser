package com.google.gson.functional;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.junit.Test;

/**
 * Tests how Gson handles custom serializers in the presence of inheritance.
 */
public class CustomSerializerForBaseTypeTest {

  // A simple base class for polymorphism testing.
  private static class Animal {}

  // A subclass to represent the runtime type.
  private static class Dog extends Animal {
    @SuppressWarnings("unused")
    String breed = "Golden Retriever";
  }

  // A container class where the declared type of the field is the base class.
  private static class PetCarrier {
    // The field is declared as Animal but holds a Dog instance at runtime.
    @SuppressWarnings("unused")
    Animal animal = new Dog();
  }

  private static final String CUSTOM_SERIALIZED_VALUE = "custom-animal";
  private static final String EXPECTED_JSON = "{\"animal\":\"" + CUSTOM_SERIALIZED_VALUE + "\"}";

  /**
   * Tests that a custom JsonSerializer registered for a base type is used for serializing an
   * instance of a subclass. This behavior bypasses the default reflective adapter that Gson would
   * normally use for the subclass's runtime type.
   */
  @Test
  public void toJson_whenSerializerRegisteredForBaseType_usesItForSubclassInstance() {
    // Arrange: Create a Gson instance with a custom serializer for the Animal base class.
    JsonSerializer<Animal> animalSerializer =
        (src, typeOfSrc, context) -> new JsonPrimitive(CUSTOM_SERIALIZED_VALUE);

    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Animal.class, animalSerializer)
        .create();

    PetCarrier petCarrier = new PetCarrier();

    // Act: Serialize the container object.
    String json = gson.toJson(petCarrier);

    // Assert: Verify that the custom serializer for the base type was used, not the
    // reflective adapter for the subclass.
    assertThat(json).isEqualTo(EXPECTED_JSON);
  }
}