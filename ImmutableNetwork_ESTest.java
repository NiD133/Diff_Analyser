/*
 * Test suite for ImmutableNetwork class
 * Tests the creation, copying, and building functionality of immutable networks
 */

package com.google.common.graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.ImmutableNetwork;
import com.google.common.graph.Network;
import com.google.common.graph.NetworkBuilder;
import com.google.common.graph.StandardMutableNetwork;
import com.google.common.graph.StandardNetwork;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class ImmutableNetwork_ESTest extends ImmutableNetwork_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void testBuilderCanAddEdgeToDirectedNetwork() throws Throwable {
      // Given: A directed network builder and standard network
      NetworkBuilder<Object, Object> directedBuilder = NetworkBuilder.directed();
      StandardNetwork<Integer, Integer> baseNetwork = new StandardNetwork<>(directedBuilder);
      
      // When: Creating an immutable network builder and adding an edge
      NetworkBuilder<Integer, Integer> networkBuilder = NetworkBuilder.from(baseNetwork);
      ImmutableNetwork.Builder<Integer, Integer> immutableBuilder = 
          new ImmutableNetwork.Builder<>(networkBuilder);
      
      Integer sourceNode = 1029;
      Integer targetNode = 1;
      Integer edgeValue = 1;
      
      // Then: The builder should successfully add the edge
      ImmutableNetwork.Builder<Integer, Integer> result = 
          immutableBuilder.addEdge(sourceNode, targetNode, edgeValue);
      
      assertNotNull("Builder should return non-null after adding edge", result);
  }

  @Test(timeout = 4000)
  public void testCopyOfThrowsNullPointerExceptionForNullNetwork() throws Throwable {
      // Given: A null network reference
      Network<Integer, Integer> nullNetwork = null;
      
      // When/Then: Copying null network should throw NullPointerException
      try { 
        ImmutableNetwork.copyOf(nullNetwork);
        fail("Expected NullPointerException when copying null network");
      } catch(NullPointerException e) {
         verifyException("com.google.common.graph.NetworkBuilder", e);
      }
  }

  @Test(timeout = 4000)
  public void testCopyOfThrowsNullPointerExceptionForNullImmutableNetwork() throws Throwable {
      // Given: A null immutable network reference
      ImmutableNetwork<Object, Integer> nullImmutableNetwork = null;
      
      // When/Then: Copying null immutable network should throw NullPointerException
      try { 
        ImmutableNetwork.copyOf(nullImmutableNetwork);
        fail("Expected NullPointerException when copying null immutable network");
      } catch(NullPointerException e) {
         verifyException("com.google.common.base.Preconditions", e);
      }
  }

  @Test(timeout = 4000)
  public void testBuildingUndirectedNetworkWithParallelEdgesAndNode() throws Throwable {
      // Given: An undirected network that allows parallel edges
      NetworkBuilder<Object, Object> undirectedBuilder = NetworkBuilder.undirected();
      StandardNetwork<Integer, Comparable<Integer>> baseNetwork = 
          new StandardNetwork<>(undirectedBuilder);
      
      NetworkBuilder<Integer, Comparable<Integer>> builderFromNetwork = 
          NetworkBuilder.from(baseNetwork);
      builderFromNetwork.allowsParallelEdges(true);
      
      StandardNetwork<Integer, Integer> parallelEdgeNetwork = 
          new StandardNetwork<>(builderFromNetwork);
      ImmutableNetwork<Integer, Integer> originalImmutable = 
          ImmutableNetwork.copyOf(parallelEdgeNetwork);
      
      // When: Building a new immutable network with an added node
      NetworkBuilder<Integer, Integer> newBuilder = NetworkBuilder.from(originalImmutable);
      ImmutableNetwork.Builder<Integer, Integer> immutableBuilder = newBuilder.immutable();
      
      Integer newNode = 949;
      immutableBuilder.addNode(newNode);
      ImmutableNetwork<Integer, Integer> newImmutable = immutableBuilder.build();
      
      // Then: The new network should be different from the original
      assertNotSame("New immutable network should be different from original", 
                   originalImmutable, newImmutable);
  }

  @Test(timeout = 4000)
  public void testBuildingDirectedNetworkWithParallelEdgesAndNode() throws Throwable {
      // Given: A directed network that allows parallel edges
      NetworkBuilder<Object, Object> directedBuilder = NetworkBuilder.directed();
      directedBuilder.allowsParallelEdges(true);
      
      StandardNetwork<Integer, Integer> baseNetwork = new StandardNetwork<>(directedBuilder);
      ImmutableNetwork<Integer, Integer> originalImmutable = ImmutableNetwork.copyOf(baseNetwork);
      
      // When: Building a new immutable network with an added node
      NetworkBuilder<Integer, Integer> newBuilder = NetworkBuilder.from(originalImmutable);
      ImmutableNetwork.Builder<Integer, Integer> immutableBuilder = newBuilder.immutable();
      
      Integer newNode = -967;
      ImmutableNetwork.Builder<Integer, Integer> builderWithNode = immutableBuilder.addNode(newNode);
      ImmutableNetwork<Integer, Integer> newImmutable = builderWithNode.build();
      
      // Then: The new network should be different from the original
      assertNotSame("New immutable network should be different from original", 
                   newImmutable, originalImmutable);
  }

  @Test(timeout = 4000)
  public void testBuildingUndirectedNetworkWithEdgeUsingEndpointPair() throws Throwable {
      // Given: An undirected mutable network
      NetworkBuilder<Object, Object> undirectedBuilder = NetworkBuilder.undirected();
      StandardMutableNetwork<Integer, Integer> mutableNetwork = 
          new StandardMutableNetwork<>(undirectedBuilder);
      
      // When: Building an immutable network with an edge using EndpointPair
      NetworkBuilder<Integer, Integer> networkBuilder = NetworkBuilder.from(mutableNetwork);
      ImmutableNetwork.Builder<Integer, Integer> immutableBuilder = networkBuilder.immutable();
      
      Integer node1 = -1124;
      Integer node2 = -405;
      Integer edgeValue = -1124;
      EndpointPair<Integer> endpoints = EndpointPair.unordered(node1, node2);
      
      ImmutableNetwork.Builder<Integer, Integer> builderWithEdge = 
          immutableBuilder.addEdge(endpoints, edgeValue);
      ImmutableNetwork<Integer, Integer> result = builderWithEdge.build();
      
      // Then: The network should be successfully created
      assertNotNull("Immutable network should be created successfully", result);
  }

  @Test(timeout = 4000)
  public void testCopyOfImmutableNetworkReturnsSameInstance() throws Throwable {
      // Given: An existing immutable network
      NetworkBuilder<Object, Object> directedBuilder = NetworkBuilder.directed();
      StandardNetwork<Integer, Integer> baseNetwork = new StandardNetwork<>(directedBuilder);
      ImmutableNetwork<Integer, Integer> originalImmutable = ImmutableNetwork.copyOf(baseNetwork);
      
      // When: Copying the immutable network
      ImmutableNetwork<Integer, Integer> copiedImmutable = 
          ImmutableNetwork.copyOf(originalImmutable);
      
      // Then: Should return the same instance (optimization)
      assertSame("Copying immutable network should return same instance", 
                originalImmutable, copiedImmutable);
  }

  @Test(timeout = 4000)
  public void testCopyOfImmutableNetworkWithGenericTypesReturnsSameInstance() throws Throwable {
      // Given: An existing immutable network with Object types
      NetworkBuilder<Object, Object> directedBuilder = NetworkBuilder.directed();
      StandardMutableNetwork<Object, Object> mutableNetwork = 
          new StandardMutableNetwork<>(directedBuilder);
      ImmutableNetwork<Object, Object> originalImmutable = 
          ImmutableNetwork.copyOf(mutableNetwork);
      
      // When: Copying the immutable network using the deprecated method
      ImmutableNetwork<Object, Object> copiedImmutable = 
          ImmutableNetwork.copyOf(originalImmutable);
      
      // Then: Should return the same instance
      assertSame("Copying immutable network should return same instance", 
                originalImmutable, copiedImmutable);
  }

  @Test(timeout = 4000)
  public void testAsGraphReturnsImmutableGraph() throws Throwable {
      // Given: An immutable network
      NetworkBuilder<Object, Object> directedBuilder = NetworkBuilder.directed();
      StandardNetwork<Integer, Integer> baseNetwork = new StandardNetwork<>(directedBuilder);
      ImmutableNetwork<Integer, Integer> immutableNetwork = ImmutableNetwork.copyOf(baseNetwork);
      
      // When: Converting to graph
      ImmutableGraph<Integer> graph = immutableNetwork.asGraph();
      
      // Then: Should return a non-null immutable graph
      assertNotNull("asGraph() should return non-null immutable graph", graph);
  }

  @Test(timeout = 4000)
  public void testBuilderCanBuildEmptyNetworkWithSingleNode() throws Throwable {
      // Given: A directed network builder
      NetworkBuilder<Object, Object> directedBuilder = NetworkBuilder.directed();
      StandardNetwork<Integer, Integer> baseNetwork = new StandardNetwork<>(directedBuilder);
      
      // When: Building an immutable network with a single node
      NetworkBuilder<Integer, Integer> networkBuilder = NetworkBuilder.from(baseNetwork);
      ImmutableNetwork.Builder<Integer, Integer> immutableBuilder = networkBuilder.immutable();
      
      Integer singleNode = -967;
      ImmutableNetwork.Builder<Integer, Integer> builderWithNode = 
          immutableBuilder.addNode(singleNode);
      ImmutableNetwork<Integer, Integer> result = builderWithNode.build();
      
      // Then: The network should be successfully created
      assertNotNull("Immutable network with single node should be created successfully", result);
  }
}