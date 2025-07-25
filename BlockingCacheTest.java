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

// issue #524
class BlockingCacheTest {

  private static SqlSessionFactory sqlSessionFactory;

  @BeforeEach
  void setupSqlSessionFactoryAndData() throws Exception {
    // 1. Build SqlSessionFactory from configuration file
    try (Reader reader = Resources
        .getResourceAsReader("org/apache/ibatis/submitted/blocking_cache/mybatis-config.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }

    // 2. Populate the in-memory database with initial data using the provided SQL script.
    BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
        "org/apache/ibatis/submitted/blocking_cache/CreateDB.sql");
  }

  @Test
  void blockingCacheShouldIncreaseTotalExecutionTimeDueToLocking() throws InterruptedException {
    // This test verifies that the BlockingCache introduces a delay when multiple threads try to access the same data.

    // 1. Create a thread pool with two threads.
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    // 2. Record the start time.
    long startTime = System.currentTimeMillis();

    // 3. Submit two tasks to the executor. Each task will access the database.
    for (int i = 0; i < 2; i++) {
      executorService.execute(this::accessDB);
    }

    // 4. Shutdown the executor and wait for tasks to complete.
    executorService.shutdown();
    boolean terminated = executorService.awaitTermination(5, TimeUnit.SECONDS);
    if (!terminated) {
      executorService.shutdownNow(); // Force shutdown if tasks are still running after timeout.
    }

    // 5. Calculate the total execution time.
    long totalTime = System.currentTimeMillis() - startTime;

    // 6. Assert that the total execution time is greater than or equal to 1000 milliseconds.
    //    This confirms that the BlockingCache is working as expected, causing threads to wait for each other.
    Assertions.assertThat(totalTime).isGreaterThanOrEqualTo(1000);
  }

  private void accessDB() {
    // This method simulates accessing the database through MyBatis.
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      // 1. Get the PersonMapper from the SqlSession.
      PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);

      // 2. Call the findAll() method on the PersonMapper, which should trigger caching.
      personMapper.findAll();

      // 3. Simulate some processing time (500ms) to allow other threads to potentially contend for the cache lock.
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        Assertions.fail(e.getMessage()); // Fail the test if interrupted.
      }
    }
  }

  @Test
  void ensureLockIsAcquiredBeforePutOnCommit() {
    // This test validates that a lock is acquired before an object is put into the cache on commit.
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
      mapper.delete(-1); // Modify data, triggering cache update on commit.
      mapper.findAll();   // Fetch data, potentially retrieving from or adding to the cache.
      sqlSession.commit(); // Commit the transaction, which should put the updated data into the cache, acquiring a lock.
    }
  }

  @Test
  void ensureLockIsReleasedOnRollback() {
    // This test checks that the lock is released if the transaction is rolled back.
    // 1. Open a session, modify data and then rollback the transaction.
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
      mapper.delete(-1); // Modify data, changes will be discarded on rollback.
      mapper.findAll();   // Access the cache.
      sqlSession.rollback(); // Rollback the transaction, releasing any locks held by the session.
    }

    // 2. Open a new session and verify that the data can be accessed without blocking.
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
      mapper.findAll(); // Access the cache again.  Should not be blocked by a lingering lock.
    }
  }
}