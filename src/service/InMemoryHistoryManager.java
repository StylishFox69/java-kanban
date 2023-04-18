package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> history = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void addTask(Task task) {
        remove(task.getId());
        Node node;
        if (last == null) {
            node = new Node(null, task, null);
            first = node;
            last = node;
        } else {
            node = new Node(last, task, null);
            last.next = node;
            last = node;
        }
        history.put(task.getId(), node);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> nodest = new ArrayList<>();
        Node current = first;
        while (current != null) {
            nodest.remove(current.item);
            nodest.add(current.item);
            current = current.next;
        }
        return nodest;
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            Node node = history.get(id);
            removeNode(node);
            history.remove(id);
        }
    }

    private void removeNode(Node node) {
        Node next = node.getNext();
        Node prev = node.getPrev();
        if (first != last) {
            if (node.next == null && node.prev != null) {
                node.prev.next = null;
                last = node.prev;
            } else if (node.prev == null && node.next != null) {
                first = node.next;
                node.next.prev = null;
            } else {
                if (node.prev != null) {
                    node.prev.next = next;
                }
                if (node.next != null) {
                    node.next.prev = prev;
                }
            }
        } else {
            first = null;
            last = null;
        }
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "history=" + history +
                '}';
    }

    private static class Node {
        Task item;
        Node next;
        Node prev;

        Node(Node prev, Task element, Node next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }

        public Node getPrev() {
            return prev;
        }

        public Node getNext() {
            return next;
        }
    }
}
