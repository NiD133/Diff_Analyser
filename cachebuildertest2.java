package org.apache.ibatis.mapping;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.BDDAssertions.then;
import java.lang.reflect.Field;
import org.apache.ibatis.builder.InitializingObject;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CacheBuilderTestTest2 {

    @SuppressWarnings("unchecked")
    private <T> T unwrap(Cache cache) {
        Field field;
        try {
            field = cache.getClass().getDeclaredField("delegate");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
        try {
            field.setAccessible(true);
            return (T) field.get(cache);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } finally {
            field.setAccessible(false);
        }
    }

    private static class InitializingCache extends PerpetualCache implements InitializingObject {

        private boolean initialized;

        public InitializingCache(String id) {
            super(id);
        }

        @Override
        public void initialize() {
            this.initialized = true;
        }
    }

    private static class InitializingFailureCache extends PerpetualCache implements InitializingObject {

        public InitializingFailureCache(String id) {
            super(id);
        }

        @Override
        public void initialize() {
            throw new IllegalStateException("error");
        }
    }

    @Test
    void initializingFailure() {
        when(() -> new CacheBuilder("test").implementation(InitializingFailureCache.class).build());
        then(caughtException()).isInstanceOf(CacheException.class).hasMessage("Failed cache initialization for 'test' on 'org.apache.ibatis.mapping.CacheBuilderTest$InitializingFailureCache'");
    }
}
