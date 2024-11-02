package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@NoArgsConstructor
public class Treap<T extends Comparable<T>> {

    Node<T> root;

    public void add(T key, Integer index) {
        root = insert(root, key, index);
    }


    public void remove(T key) {
        root = deleteNode(root, key);
    }

    public Node<T>[] split(T key) {
        return root.split(key);
    }


    public List<Node<T>> inorder(Node<T> cur) {
        List<Node<T>> res = new ArrayList<>();
        inorder(cur, res);
        return res;
    }

    public Node<T> searchNodeByKey(T key) {
        return searchNode(this.root, key);
    }

    private Node<T> searchNode(Node<T> cur, T key) {
        if (cur.right != null && cur.key.compareTo(key) < 0) {
            return searchNode(cur.right, key);
        }
        if (cur.left != null && cur.key.compareTo(key) >= 0) {
            return searchNode(cur.left, key);
        }
        return cur;
    }

    private Node<T> deleteNode(Node<T> cur, T key) {
        if (cur == null)
            return null;

        if (key.compareTo(cur.key) < 0)
            cur.left = deleteNode(cur.left, key);
        else if (key.compareTo(cur.key) > 0)
            cur.right = deleteNode(cur.right, key);
        else if (cur.left == null) {
            cur = cur.right;
        } else if (cur.right == null) {
            cur = cur.left;
        } else if (cur.left.priority < cur.right.priority) {
            cur = leftRotation(cur);
            cur.left = deleteNode(cur.left, key);
        } else {
            cur = rightRotation(cur);
            cur.right = deleteNode(cur.right, key);
        }
        return cur;
    }

    private Node<T> insert(Node<T> cur, T key, Integer index) {
        if (cur == null) return new Node<>(key, index);

        if (key.compareTo(cur.key) > 0) {
            cur.right = insert(cur.right, key, index);
            if (cur.right.priority < cur.priority) {
                cur = leftRotation(cur);
            }

        } else {
            cur.left = insert(cur.left, key, index);
            if (cur.left.priority < cur.priority) {
                cur = rightRotation(cur);
            }

        }
        return cur;
    }

    private Node<T> leftRotation(Node<T> x) {
        var y = x.right;
        var T2 = y.left;

        y.left = x;
        x.right = T2;

        return y;
    }

    private Node<T> rightRotation(Node<T> y) {
        var x = y.left;
        var T2 = x.right;

        x.right = y;
        y.left = T2;

        return x;
    }

    private void inorder(Node<T> cur, List<Node<T>> res) {
        if (cur == null) return;

        inorder(cur.left, res);
        res.add(cur);
        inorder(cur.right, res);
    }


    @Getter
    @AllArgsConstructor
    public static class Node<T extends Comparable<T>> {
        static Random RND = new Random();
        T key;
        int priority;
        Node<T> left;
        Node<T> right;

        public Node(T key, int priority) {
            this(key, priority, null, null);
        }

        public Node<T>[] split(T key) {
            Node<T> tmp = null;

            Node<T>[] res = (Node<T>[]) Array.newInstance(this.getClass(), 2);

            if (this.key.compareTo(key) < 0) {
                if (this.right == null) {
                    res[1] = null;
                } else {
                    Node<T>[] rightSplit = this.right.split(key);
                    res[1] = rightSplit[1];
                    tmp = rightSplit[0];
                }
                res[0] = new Node<>(this.key, priority, this.left, tmp);
            } else {
                if (left == null) {
                    res[0] = null;
                } else {
                    Node<T>[] leftSplit = this.left.split(key);
                    res[0] = leftSplit[0];
                    tmp = leftSplit[1];
                }
                res[1] = new Node<>(this.key, priority, tmp, this.right);
            }
            return res;
        }
    }
}