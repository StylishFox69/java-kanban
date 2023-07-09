package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> history = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        }
        remove(task.getId());
        Node node = new Node(last, task, null);
        if (last == null) {
            node.prev = null;
            node.next = null;
            first = node;
        } else {
            node.prev = last;
            node.next = null;
            last.next = node;
        }
        last = node;
        history.put(task.getId(), node);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> nodest = new ArrayList<>(history.size());
        Node current = first;
        while (current != null) {
            nodest.add(current.item);
            current = current.next;
        }
        return nodest;
    }

    @Override
    public void remove(int id) {
            Node node = history.remove(id);
            if(node != null){
                removeNode(node);
            }
            history.remove(id);
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
                    node.prev.next = next;
                    node.next.prev = prev;
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
