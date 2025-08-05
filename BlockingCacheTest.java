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
package org.apache.ibatis.submitted.blocking_cache;

import java.io.Reader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlockingCacheTest {

    private static final int THREAD_COUNT = 2;
    private static final long THREAD_SLEEP_TIME = 500; // ms
    private static final long MIN_EXPECTED_TIME = THREAD_SLEEP_TIME * THREAD_COUNT; // ms
    private static final long THREAD_SHUTDOWN_TIMEOUT = 5; // seconds

    private static SqlSessionFactory sqlSessionFactory;

    @BeforeEach
    void setUp() throws Exception {
        // Create SqlSessionFactory using blocking cache configuration
        try (Reader reader = Resources.getResourceAsReader(
                "org/apache/ibatis/submitted/blocking_cache/mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        }

        // Initialize in-memory database with test data
        BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
                "org/apache/ibatis/submitted/blocking_cache/CreateDB.sql");
    }

    @Test
    void concurrentAccess_ShouldSerializeCacheAccess() throws InterruptedException {
        // Setup: Create thread pool for concurrent cache access
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT, r -> {
            Thread t = new Thread(r);
            t.setName("BlockingCacheTest-Thread");
            return t;
        });

        long startTime = System.currentTimeMillis();

        // Execute concurrent database access operations
        for (int i = 0; i < THREAD_COUNT; i++) {
            threadPool.execute(this::performBlockedDatabaseOperation);
        }

        // Gracefully shutdown thread pool with timeout
        threadPool.shutdown();
        boolean terminated = threadPool.awaitTermination(THREAD_SHUTDOWN_TIMEOUT, TimeUnit.SECONDS);
        if (!terminated) {
            threadPool.shutdownNow();
        }

        long totalDuration = System.currentTimeMillis() - startTime;

        // Verify: Blocking cache should force sequential execution (2 * 500ms = 1000ms)
        Assertions.assertThat(totalDuration)
                .as("Blocking cache should serialize operations causing total time >= " + MIN_EXPECTED_TIME + "ms")
                .isGreaterThanOrEqualTo(MIN_EXPECTED_TIME);
    }

    private void performBlockedDatabaseOperation() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
            mapper.findAll();  // This operation is affected by BlockingCache
            
            // Simulate processing time to demonstrate blocking effect
            Thread.sleep(THREAD_SLEEP_TIME);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted during database operation", e);
        }
    }

    @Test
    void commitTransaction_ShouldReleaseCacheLock() {
        // Verify lock release after commit
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
            
            // Perform operations that acquire cache lock
            mapper.delete(-1);  // Non-existent ID
            mapper.findAll();
            
            sqlSession.commit();  // Should release cache lock
        }
        // Test passes if no deadlock occurs during commit
    }

    @Test
    void rollbackTransaction_ShouldReleaseCacheLock() {
        // First session: Operations followed by rollback
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
            
            // Perform operations that acquire cache lock
            mapper.delete(-1);  // Non-existent ID
            mapper.findAll();
            
            sqlSession.rollback();  // Should release cache lock
        }

        // Second session: Verify lock was released by successful operation
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
            mapper.findAll();  // Should not be blocked if lock released
        }
    }
}