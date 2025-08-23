package com.google.common.graph;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link EndpointPair}.
 */
public class EndpointPairTest {

    // A JUnit 4 rule for declaratively testing that code throws a specific exception.
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void adjacentNode_whenNodeNotInPair_throwsIllegalArgumentException() {
        // Arrange: Create an endpoint pair and a node that is not part of it.
        String node = "A";
        EndpointPair<String> endpointPair = EndpointPair.unordered(node, node);
        String nodeNotInPair = "C";

        // Assert: Configure the expected exception type and message.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("EndpointPair [A, A] does not contain node C");

        // Act: Call the method under test with the node that is not in the pair.
        // This call is expected to throw the exception configured above.
        endpointPair.adjacentNode(nodeNotInPair);
    }
}