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

public class TypeAdapterRuntimeTypeWrapperTestTest2 {

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
     * When only {@link JsonDeserializer} is registered for Base, then on serialization should prefer
     * reflective adapter for Subclass since Base would use reflective adapter as delegate.
     */
    @Test
    public void testJsonDeserializer_ReflectiveSerializerDelegate() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Base.class, new Deserializer()).create();
        String json = gson.toJson(new Container());
        assertThat(json).isEqualTo("{\"b\":{\"f\":\"test\"}}");
    }
}
