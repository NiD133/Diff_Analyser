package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class CacheKeyTestTest1 {

    private static <T> T serialize(T object) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(object);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        return (T) new ObjectInputStream(bais).readObject();
    }

    @Test
    void shouldTestCacheKeysEqual() {
        Date date = new Date();
        CacheKey key1 = new CacheKey(new Object[] { 1, "hello", null, new Date(date.getTime()) });
        CacheKey key2 = new CacheKey(new Object[] { 1, "hello", null, new Date(date.getTime()) });
        assertEquals(key1, key2);
        assertEquals(key2, key1);
        assertEquals(key1.hashCode(), key2.hashCode());
        assertEquals(key1.toString(), key2.toString());
    }
}
