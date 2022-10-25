public class Node<K> {
	
	K Key;
	int RepeatCount;
	Node next;
	
	
	public Node(K key) {

		Key= key;
		RepeatCount = 1;
		this.next = null;
	}
	
	public K getkey() {
		return Key;
	}
	public void setkey(K key) {
		Key = key;
	}
	public int getRepeatCount() {
		return RepeatCount;
	}
	public void setRepeatCount() {
		this.RepeatCount++;
	}
	public Node getNext() {
		return next;
	}
	public void setNext(Node next) {
		this.next = next;
	}
	



	
}
