package edu.hsutx;

/**
 * @author Jonathan Makenene
 * @version 1.0
 * Red-Black Tree implementation for CSCI-3323 assignment
 * This Red-Black Tree follows the properties of Red-Black Trees for balanced binary search trees.
 */
public class RedBlackTree<E> {
    Node root;
    int size;

    /**
     * Inner Node class representing each node in the Red-Black Tree.
     * Each node stores a key, value, left and right children, and the parent.
     * The color of each node is either red (true) or black (false).
     */
    protected class Node {
        public String key;
        public E value;
        public Node left;
        public Node right;
        public Node parent;
        public boolean color; // true = red, false = black

        /**
         * Constructor to initialize a new node with key, value, parent, and color.
         * 
         * @param key The key associated with the node
         * @param value The value associated with the node
         * @param parent The parent node of this node
         * @param color The color of the node (true for red, false for black)
         */
        public Node(String key, E value, Node parent, boolean color) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.left = null;
            this.right = null;
            this.color = color;
        }

        /**
         * Calculates the depth of the node by traversing upwards to the root.
         * The depth is the number of edges from this node to the root.
         * 
         * @return The depth of the node in the tree
         */
        public int getDepth() {
            int countDepth = 0;
            Node current = this;
            while (current.parent != null) {
                current = current.parent;
                countDepth++;
            }
            return countDepth;
        }

        /**
         * Calculates the black depth of the node by counting the number of black nodes
         * along the path to the root.
         * 
         * @return The black depth of the node
         */
        public int getBlackDepth() {
            int blackDepth = 0;
            Node current = this;
            while (current != null) {
                if (current.color == false) { // If node is black
                    blackDepth++;
                }
                current = current.parent;
            }
            return blackDepth;
        }
    }

    /**
     * Initializes the Red-Black Tree as empty with a size of zero.
     */
    public RedBlackTree() {
        root = null; // Start with an empty tree.
        size = 0;
    }

    /**
     * Inserts a new node into the Red-Black Tree.
     * This method performs a standard Binary Search Tree (BST) insertion and then
     * restores Red-Black Tree properties by performing recoloring and rotations if necessary.
     * 
     * @param key The key of the node to be inserted
     * @param value The value of the node to be inserted
     */
    public void insert(String key, E value) {
        Node x = new Node(key, value, null, true); // New node is initially red
        if (root == null) {
            root = x;
            root.color = false; // Root is always black
            size++;
            return;
        }

        Node p = null;
        Node current = root;

        while (current != null) {
            p = current;
            if (key.compareTo(current.key) < 0) {
                current = current.left;
            } else if (key.compareTo(current.key) > 0) {
                current = current.right;
            } else {
                return; // No need to insert duplicate keys
            }
        }
        
        x.parent = p;
        if (key.compareTo(p.key) < 0) {
            p.left = x;
        } else {
            p.right = x;
        }

        size++;
        fixInsertion(x); // Fix the Red-Black Tree properties after insertion
    }

    /**
     * Deletes a node from the Red-Black Tree.
     * Handles the three cases for node deletion:
     * 1. Node has no children
     * 2. Node has one child
     * 3. Node has two children
     * After deletion, the tree may become unbalanced, requiring rebalancing and recoloring.
     * 
     * @param key The key of the node to be deleted
     */
    public void delete(String key) {
        Node nodeToDelete = find(key);
        if (nodeToDelete == null) {
            return; // Node not found
        }

        Node y = nodeToDelete;
        Node x;
        boolean originalColor = y.color; // Store the original color of the node to be deleted

        // Case 1 & 2: Node has one or zero children
        if (nodeToDelete.left == null) {
            x = nodeToDelete.right;
            transplant(nodeToDelete, nodeToDelete.right);
        } else if (nodeToDelete.right == null) {
            x = nodeToDelete.left;
            transplant(nodeToDelete, nodeToDelete.left);
        } else {
            // Case 3: Node has two children
            y = minimum(nodeToDelete.right); // In-order successor
            originalColor = y.color;
            x = y.right;

            if (y.parent == nodeToDelete) {
                if (x != null) x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = nodeToDelete.right;
                y.right.parent = y;
            }

            transplant(nodeToDelete, y);
            y.left = nodeToDelete.left;
            y.left.parent = y;
            y.color = nodeToDelete.color;
        }

        size--;

        if (originalColor == false) { // Rebalance if a black node was deleted
            fixDeletion(x);
        }
    }

    /**
     * Transplants one subtree into another subtree.
     * Used in the delete operation to replace a node with another.
     * 
     * @param u The node to be replaced
     * @param v The node to transplant into u's position
     */
    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }
    
    /**
     * Finds the node with the minimum key in the subtree rooted at the given node.
     * 
     * @param node The root node of the subtree
     * @return The node with the minimum key in the subtree
     */
    private Node minimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Rebalances the tree after insertion to ensure Red-Black Tree properties are maintained.
     * Handles recoloring and rotations as needed.
     * 
     * @param node The node that was just inserted and may require rebalancing
     */
    private void fixInsertion(Node node) {
        while (node != root && node.parent.color == true) { // Parent is red
            if (node.parent == node.parent.parent.left) { // Parent is left child
                Node x = node.parent.parent.right;
    
                if (x != null && x.color == true) { // Case 1: Uncle is red
                    node.parent.color = false;
                    x.color = false;
                    node.parent.parent.color = true;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) { // Case 2: Node is right child
                        node = node.parent;
                        rotateLeft(node);
                    }
                    // Case 3: Node is left child
                    node.parent.color = false;
                    node.parent.parent.color = true;
                    rotateRight(node.parent.parent);
                }
            } else { // Mirror cases if parent is right child
                Node y = node.parent.parent.left;
    
                if (y != null && y.color == true) { // Case 1: Uncle is red
                    node.parent.color = false;
                    y.color = false;
                    node.parent.parent.color = true;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) { // Case 2: Node is left child
                        node = node.parent;
                        rotateRight(node);
                    }
                    // Case 3: Node is right child
                    node.parent.color = false;
                    node.parent.parent.color = true;
                    rotateLeft(node.parent.parent);
                }
            }
        }
        root.color = false; // Ensure root is always black
    }

    /**
     * Fixes the tree after deletion to restore Red-Black Tree properties.
     * Handles recoloring and rotations as needed.
     * 
     * @param node The node to be fixed
     */
    private void fixDeletion(Node node) {
        while (node != null && node != root && isBlack(node)) {
            if (node == node.parent.left) {
                Node sibling = node.parent.right;
    
                if (isRed(sibling)) { // Case 1: Sibling is red
                    sibling.color = false;
                    node.parent.color = true;
                    rotateLeft(node.parent);
                    sibling = node.parent.right;
                }
    
                if (isBlack(sibling.left) && isBlack(sibling.right)) { // Case 2: Sibling's children are black
                    sibling.color = true;
                    node = node.parent;
                } else {
                    if (isBlack(sibling.right)) { // Case 3: Sibling's left child is red, right child is black
                        sibling.left.color = false;
                        sibling.color = true;
                        rotateRight(sibling);
                        sibling = node.parent.right;
                    }
    
                    sibling.color = node.parent.color; // Case 4: Sibling's right child is red
                    node.parent.color = false;
                    sibling.right.color = false;
                    rotateLeft(node.parent);
                    node = root;
                }
            } else { // Mirror cases if node is a right child
                Node sibling = node.parent.left;
    
                if (isRed(sibling)) { // Case 1: Sibling is red
                    sibling.color = false;
                    node.parent.color = true;
                    rotateRight(node.parent);
                    sibling = node.parent.left;
                }
    
                if (isBlack(sibling.right) && isBlack(sibling.left)) { // Case 2: Sibling's children are black
                    sibling.color = true;
                    node = node.parent;
                } else {
                    if (isBlack(sibling.left)) { // Case 3: Sibling's right child is red, left child is black
                        sibling.right.color = false;
                        sibling.color = true;
                        rotateLeft(sibling);
                        sibling = node.parent.left;
                    }
    
                    sibling.color = node.parent.color; // Case 4: Sibling's left child is red
                    node.parent.color = false;
                    sibling.left.color = false;
                    rotateRight(node.parent);
                    node = root;
                }
            }
        }
        if (node != null) {
            node.color = false; // Ensure the node is black if not null
        }
    }

    /**
     * Performs a left rotation on the given node to restore balance in the tree.
     * 
     * @param node The node to perform a left rotation on
     */
    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }
        rightChild.parent = node.parent;
        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }
        rightChild.left = node;
        node.parent = rightChild;
    }

    /**
     * Performs a right rotation on the given node to restore balance in the tree.
     * 
     * @param node The node to perform a right rotation on
     */
    private void rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }
        leftChild.parent = node.parent;
        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.right) {
            node.parent.right = leftChild;
        } else {
            node.parent.left = leftChild;
        }
        leftChild.right = node;
        node.parent = leftChild;
    }

    /**
     * Finds a node with the given key.
     * 
     * @param key The key of the node to search for
     * @return The node with the given key, or null if not found
     */
    Node find(String key) {
        Node current = root;
        while (current != null) {
            int comp = key.compareTo(current.key);
            if (comp < 0) {
                current = current.left;
            } else if (comp > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    /**
     * Returns the value associated with a given key.
     * 
     * @param key The key of the node to retrieve the value for
     * @return The value associated with the given key, or null if the key does not exist
     */
    public E getValue(String key) {
        Node current = root;
        while (current != null) {
            int comp = key.compareTo(current.key);
            if (comp < 0) {
                current = current.left;
            } else if (comp > 0) {
                current = current.right;
            } else {
                return current.value;
            }
        }
        return null;
    }

    /**
     * Checks if the tree is empty.
     * 
     * @return True if the tree is empty, false otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Returns the depth of the node with the given key.
     * 
     * @param key The key of the node to find the depth for
     * @return The depth of the node with the given key, or 0 if the key does not exist
     */
    public int getDepth(String key) {
        Node node = find(key);
        if (node != null) return node.getDepth();
        return 0;
    }

    /**
     * Checks if a node is red.
     * 
     * @param node The node to check
     * @return True if the node is red, false otherwise
     */
    private boolean isRed(Node node) {
        return node != null && node.color == true;
    }

    /**
     * Checks if a node is black.
     * 
     * @param node The node to check
     * @return True if the node is black, false otherwise
     */
    private boolean isBlack(Node node) {
        return node == null || node.color == false;
    }

    /**
     * Returns the size of the tree.
     * 
     * @return The number of nodes in the tree
     */
    public int getSize() {
        return size;
    }

    /**
     * Validates whether the tree satisfies the Red-Black Tree properties.
     * 
     * @return True if the tree is a valid Red-Black Tree, false otherwise
     */
    public boolean validateRedBlackTree() {
        if (root == null) {
            return true; // An empty tree is trivially a valid Red-Black Tree
        }
        if (isRed(root)) {
            return false; // Root must be black
        }

        return validateNode(root, 0, -1);
    }

    /**
     * Recursively validates whether the current node and its descendants satisfy Red-Black Tree properties.
     * 
     * @param node The current node to check
     * @param blackCount The current count of black nodes on the path
     * @param expectedBlackCount The expected black count for all paths
     * @return True if the tree satisfies the Red-Black Tree properties, false otherwise
     */
    private boolean validateNode(Node node, int blackCount, int expectedBlackCount) {
        if (node == null) {
            if (expectedBlackCount == -1) {
                expectedBlackCount = blackCount;
            }
            return blackCount == expectedBlackCount;
        }

        if (isRed(node)) {
            if (isRed(node.left) || isRed(node.right)) {
                return false;
            }
        } else {
            blackCount++;
        }

        return validateNode(node.left, blackCount, expectedBlackCount) &&
                validateNode(node.right, blackCount, expectedBlackCount);
    }
}
