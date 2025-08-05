/*
 *    Copyright 2009-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.mapping;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.BDDAssertions.then;

import java.lang.reflect.Field;
import org.apache.ibatis.builder.InitializingObject;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.Test;

class CacheBuilderTest {

    @Test
    void shouldInitializeCacheWhenImplementationImplementsInitializingObject() {
        // Given: A cache builder configured with InitializingCache implementation
        CacheBuilder builder = new CacheBuilder("test")
            .implementation(InitializingCache.class);

        // When: Building the cache
        Cache cache = builder.build();

        // Then: The cache should be properly initialized
        InitializingCache baseCache = unwrapToBaseCache(cache);
        then(baseCache.isInitialized())
            .as("Cache should be initialized after build")
            .isTrue();
    }

    @Test
    void shouldThrowCacheExceptionWhenInitializationFails() {
        // When: Building cache with implementation that throws during initialization
        when(() -> 
            new CacheBuilder("test")
                .implementation(InitializingFailureCache.class)
                .build()
        );
        
        // Then: Expect CacheException with specific error message
        then(caughtException())
            .as("Should throw CacheException when initialization fails")
            .isInstanceOf(CacheException.class)
            .hasMessage(
                "Failed cache initialization for 'test' on " +
                "'org.apache.ibatis.mapping.CacheBuilderTest$InitializingFailureCache'"
            );
    }

    /**
     * Unwraps decorated cache to access base implementation instance.
     * CacheBuilder wraps the base cache with decorators, so we need to access
     * the underlying delegate to verify initialization state.
     */
    private InitializingCache unwrapToBaseCache(Cache cache) {
        try {
            Field delegateField = cache.getClass().getDeclaredField("delegate");
            delegateField.setAccessible(true);
            return (InitializingCache) delegateField.get(cache);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Failed to unwrap cache delegate", e);
        }
    }

    /**
     * Test cache implementation that tracks initialization status.
     * Verifies that CacheBuilder calls InitializingObject interface methods.
     */
    private static class InitializingCache extends PerpetualCache implements InitializingObject {
        private boolean initialized;

        public InitializingCache(String id) {
            super(id);
        }

        @Override
        public void initialize() {
            this.initialized = true;
        }

        public boolean isInitialized() {
            return initialized;
        }
    }

    /**
     * Test cache implementation that always fails during initialization.
     * Verifies that CacheBuilder properly handles initialization exceptions.
     */
    private static class InitializingFailureCache extends PerpetualCache implements InitializingObject {
        public InitializingFailureCache(String id) {
            super(id);
        }

        @Override
        public void initialize() {
            throw new IllegalStateException("Test initialization failure");
        }
    }
}