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

public class TypeAdapterRuntimeTypeWrapperTestTest6 {

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
     * When a {@link JsonDeserializer} is registered for Subclass, and a custom {@link JsonSerializer}
     * is registered for Base, then Gson should prefer the reflective adapter for Subclass for
     * backward compatibility (see https://github.com/google/gson/pull/1787#issuecomment-1222175189)
     * even though normally TypeAdapterRuntimeTypeWrapper should prefer the custom serializer for
     * Base.
     */
    @Test
    public void testJsonDeserializer_SubclassBackwardCompatibility() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Subclass.class, (JsonDeserializer<Subclass>) (json, typeOfT, context) -> {
            throw new AssertionError("not needed for this test");
        }).registerTypeAdapter(Base.class, (JsonSerializer<Base>) (src, typeOfSrc, context) -> new JsonPrimitive("base")).create();
        String json = gson.toJson(new Container());
        assertThat(json).isEqualTo("{\"b\":{\"f\":\"test\"}}");
    }
}
