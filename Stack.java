class EmptyStackException extends Exception {
  public EmptyStackException () {
    super("Stack is empty");
  }
  
  public EmptyStackException(String msg) {
    super(msg);
  }
}

public class Stack<T> {
  
  private Node<T> top;
  
  // Begin class Node
  private class Node<T> {
    private T data;
    private Node<T> next;
    
    private Node(T item) {
      data = item;
      next = null;
    }
    
    private Node(T item, Node<T> n) {
      data = item;
      next = n;
    }
    
  }
  // End class Node
  
  Node<T> head;
  
  public Stack() {
    head = null;
  }
  
  public void push(T item) {
    //System.out.print(this);
    if (head == null) {
      head = new Node<T>(item);
    } else {
      Node<T> n = new Node<T>(item);
      n.next = head;
      head = n;
    }
    //System.out.println(this);
  }
  
  public T pop() throws EmptyStackException {
    if (head == null) {
      throw new EmptyStackException("Can not pop empty stack");
    }
    T data = head.data;
    head = head.next;
    return data;
  }
  
  public T peek() throws EmptyStackException {
    if (head == null) {
      throw new EmptyStackException("Can not peek empty stack");
    }
    return head.data;
  }
  
  public boolean empty() {
    if (head == null) {
      return true;
    } else {
      return false;
    }
  }
  
  public String toString() {
    if (head == null) {
      return "[]";
    }
    String str = "[";
    Node<T> p = head;
    while (p != null) {
      str += (p.data + ", ");
      p = p.next;
    }
    str = str.substring(0, str.length() - 2);
    str += "]";
    return str;
  }
}
