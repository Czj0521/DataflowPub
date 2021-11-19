package com.bdilab.dataflow.utils.dag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 *  Base Dag and for no real time dag.
 *
 * @author wh
 * @date 2021/11/12
 */
@Data
public class Dag {
  protected static final Integer MAX_SIZE = 200;
  private Map<String, DagNode> dagMap = new HashMap<>();

  /**
   * Add a node to the dag.
   *
   * @param dagNode the node being added
   */
  public void addNode(DagNode dagNode) {
    if (MAX_SIZE == dagMap.size()) {
      throw new RuntimeException("The dag capacity is full !");
    }
    String nodeId = dagNode.getNodeId();
    if (nodeId == null || "".equals(nodeId) || dagMap.containsKey(nodeId)) {
      throw new RuntimeException("The node cannot be empty or repeated !");
    }
    dagMap.put(dagNode.getNodeId(), dagNode);
  }

  /**
   * Add an edge to the dag.
   *
   * @param preNodeId ID of preceding node of the edge
   * @param nextNodeId ID of subsequent node of the edge
   */
  public void addEdge(String preNodeId, String nextNodeId) {
    checkNode(preNodeId);
    checkNode(nextNodeId);
    DagNode dagNode = dagMap.get(preNodeId);
    dagNode.getNextNodesId().add(nextNodeId);
    dagNode = dagMap.get(nextNodeId);
    dagNode.getPreNodesId().add(preNodeId);
  }

  /**
   * Remove node from the dag.
   *
   * @param deletedNodeId the ID of node that will be deleted
   */
  public void removeNode(String deletedNodeId) {
    checkNode(deletedNodeId);
    DagNode deletedNode = dagMap.get(deletedNodeId);
    deletedNode.getPreNodesId().forEach((nodeId) -> {
      dagMap.get(nodeId).getNextNodesId().remove(deletedNodeId);
    });
    deletedNode.getNextNodesId().forEach((nodeId) -> {
      dagMap.get(nodeId).getPreNodesId().remove(deletedNodeId);
    });
    dagMap.remove(deletedNodeId);
  }

  /**
   * Remove edge from the dag.
   *
   * @param preNodeId the ID of preceding node
   * @param nextNodeId the ID of subsequent node
   */
  public void removeEdge(String preNodeId, String nextNodeId) {
    checkNode(preNodeId);
    checkNode(nextNodeId);
    DagNode dagNode = dagMap.get(preNodeId);
    dagNode.getNextNodesId().remove(nextNodeId);
    dagNode = dagMap.get(nextNodeId);
    dagNode.getPreNodesId().remove(preNodeId);
  }

  /**
   * Clear dag.
   */
  public void clearDag() {
    dagMap.clear();
  }

  /**
   * Updating node information.
   *
   * @param nodeId new node
   * @param dagNode node ID
   */
  public void updateNode(String nodeId, DagNode dagNode) {
    if (!dagNode.getNodeId().equals(nodeId)) {
      throw new RuntimeException("Nodes ID cannot be modified !");
    }
    checkNode(nodeId);
    dagMap.put(dagNode.getNodeId(), dagNode);
  }

  /**
   * Get node.
   *
   * @param nodeId node ID
   * @return DagNode
   */
  public DagNode getNode(String nodeId) {
    checkNode(nodeId);
    return dagMap.get(nodeId);
  }

  /**
   * Gets the subsequent nodes of this node.
   *
   * @param nodeId node ID
   * @return list of dag node
   */
  public List<DagNode> getNextNodes(String nodeId) {
    checkNode(nodeId);
    List<DagNode> nextNodes = new ArrayList<>();
    dagMap.get(nodeId).getNextNodesId().forEach((nextNodeId) -> {
      nextNodes.add(dagMap.get(nextNodeId));
    });
    return nextNodes;
  }

  /**
   * Gets the preceding node of this node.
   *
   * @param nodeId node ID
   * @return list of dag node
   */
  public List<DagNode> getPreNodes(String nodeId) {
    checkNode(nodeId);
    List<DagNode> preNodes = new ArrayList<>();
    dagMap.get(nodeId).getPreNodesId().forEach((preNodeId) -> {
      preNodes.add(dagMap.get(preNodeId));
    });
    return preNodes;
  }

  /**
   * Get this dag list.
   *
   * @return all nodes
   */
  public List<DagNode> getDag() {
    List<DagNode> nodes = new ArrayList<>();
    dagMap.forEach((k, v) -> {
      nodes.add(v);
    });
    return nodes;
  }

  /**
   * Check whether the node is valid.
   *
   * @param nodeId node ID
   */
  private void checkNode(String nodeId) {
    if (nodeId == null || "".equals(nodeId) || !dagMap.containsKey(nodeId)) {
      throw new RuntimeException("The node does not exist !");
    }
  }
}
