package com.google.gson.functional;

import static com.google.common.truth.Truth.assertThat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import org.junit.Test;

public class TypeAdapterRuntimeTypeWrapperTestTest4 {

    private static class Base {
    }

    private static class Subclass extends Base {

        @SuppressWarnings("unused")
        String f = "test";
    }

    private static class Container {

        @SuppressWarnings("unused")
        Base b = new Subclass();
    }

    private static class Deserializer implements JsonDeserializer<Base> {

        @Override
        public Base deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            throw new AssertionError("not needed for this test");
        }
    }

    private static class CyclicBase {

        @SuppressWarnings("unused")
        CyclicBase f;
    }

    private static class CyclicSub extends CyclicBase {

        @SuppressWarnings("unused")
        int i;

        CyclicSub(int i) {
            this.i = i;
        }
    }

    /**
     * When two (or more) {@link JsonDeserializer}s are registered for Base which eventually fall back
     * to reflective adapter as delegate, then on serialization should prefer reflective adapter for
     * Subclass.
     */
    @Test
    public void testJsonDeserializer_ReflectiveTreeSerializerDelegate() {
        Gson gson = new GsonBuilder().// Register delegate which itself falls back to reflective serialization
        registerTypeAdapter(Base.class, new Deserializer()).registerTypeAdapter(Base.class, new Deserializer()).create();
        String json = gson.toJson(new Container());
        assertThat(json).isEqualTo("{\"b\":{\"f\":\"test\"}}");
    }
}