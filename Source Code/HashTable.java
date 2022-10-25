import java.util.ArrayList;
import java.util.Iterator;

public class HashTable{

	private int TABLE_SIZE = 128;
	private boolean hashFunction = true; // TRUE: SSF FALSE: PAF
	private boolean collisionHandling = true; // TRUE: LP FALSE: DH
	HashEntry[] table;
	HashEntry[] temp;
	double putCount = 0;
	double collisionCount = 0;
	private int prime = 37;
	private int q = 41;


	double loadfactor = 0.5;

	public HashTable() {

		table = new HashEntry[TABLE_SIZE];
	}

	protected int hashValue(String key) {
		int hash = 0;
		// Simple Summation Function (SSF)
		if(hashFunction) {
			hash = simpleSummationFunction(key);
			if(collisionHandling) 
				hash = linearProbing(hash, key);
			else {
				hash = doubleHashing(hash, key);
			}

		}

		//Polynomial Accumulation Function (PAF)
		else {
			hash = polynomialAccumulationFunction(key);
			if(collisionHandling)
				hash = linearProbing(hash, key);
			else
				hash = doubleHashing(hash, key);
		}


		return hash % TABLE_SIZE;
	}
	public int simpleSummationFunction(String key) {
		int hash = 0;
		for (int i = 0; i < key.length(); i++) {
			hash += ((int) key.charAt(i) - 96);
		}
		return hash % TABLE_SIZE;
	}

	public int polynomialAccumulationFunction(String key) {
		long hash = 0;
		long temp = 0;
		long ch = 0;
		for (int i = 0; i < key.length(); i++) 
		{
			ch = key.substring(i,i+1).charAt(0) - 96;

			hash = hash + ch * (int) Math.pow(q, (key.length()-i-1));
		}
		int result = (int) (hash % TABLE_SIZE);
		return result;
	}

	public int linearProbing(int hash, String key) {
		int temp = hash;
		while(table[hash] != null && !table[hash].getKey().equals(key) && !table[hash].getKey().equals("REMOVED"))
		{
			hash++;
			if(hash >= TABLE_SIZE)
				hash = hash % TABLE_SIZE;

		}
		if(temp != hash)
			collisionCount++;

		return hash;
	}

	public int doubleHashing(int hash, String key) {

		int j = 1;
		int firstHash = hash;
		int secondHash = q - hash % q;
		while(table[hash] != null && !table[hash].getKey().equals(key) && !table[hash].getKey().equals("REMOVED"))
		{
			hash = (firstHash + ( j * secondHash)) % TABLE_SIZE;
			q++;
		}				

		return hash;
	}
	public void add(int hash, String value) {
		Node start = table[hash].getValue();

		do {
			if(value.equals(start.getkey())) {
				start.setRepeatCount();
				return;
			}
			else if(start.getNext() != null) {
				start = start.getNext();
			}
		}
		while(start.getNext() != null);

		if(value.equals(start.getkey())) {
			start.setRepeatCount();
			return;
		}
		Node temp = new Node(value);
		start.setNext(temp);
		table[hash].increaseCount();
	}
	public String put(int hash, String key, String value) {

		if(table[hash] == null) {
			Node node = new Node(value);
			table[hash] = new HashEntry(key, node);
			table[hash].increaseCount();
			sizeControl();
			return value;
		}
		if(search(key) != -1) {


			HashEntry temp = table[search(key)];
			if(temp != null) {
				add(hash, value);
				sizeControl();
				return value;
			}
			else {
				if(table[hash].getKey().equals("REMOVED")) {
					Node node = new Node(value);
					table[hash] = new HashEntry(key, node);
					table[hash].increaseCount();
					sizeControl();
					return value;
				}
				else {
					if(collisionHandling)
						hash = linearProbing(hash, key);
					else
						hash = doubleHashing(hash, key);
					Node node = new Node(value);
					table[hash] = new HashEntry(key, node);
					table[hash].increaseCount();
					sizeControl();
					return value;
				}
			}
		}
		return value;
	}


	public String remove(int h, String k) {

		int hash = search(k);

		if(hash == -1 )
			System.out.println("Given key could not be found");
		else {
			Node node = new Node("REMOVED");
			table[hash].setValue(node);
		}

		return null;
	}
	public int search(String key) {
		int hash = hashValue(key);
		int temp = hash;

		do
		{
			if(table[hash].getKey().equals(key)&&table[hash]!=null)
				break;
			else
				hash++;
		}
		while(hash % TABLE_SIZE != temp);

		return hash;

	}
	public boolean isPrime(int num) {
		int temp;
		boolean isPrime=true;
		for(int i = 2; i <= num / 2 ; i++)
		{
			temp = num % i;
			if(temp == 0)
			{
				isPrime = false;
				break;
			}
		}
		//If isPrime is true then the number is prime else not
		if(isPrime)
			return true;
		else
			return false;
	}
	public void resize(){
		temp = new HashEntry[TABLE_SIZE];
		for (int i = 0; i < table.length; i++) {
			temp[i] = table [i];
		}


		TABLE_SIZE = TABLE_SIZE * 2;

		while(!isPrime(TABLE_SIZE)) {
			TABLE_SIZE++;			
		}

		table = new HashEntry[TABLE_SIZE];

		String value;
		int count;	
		String textName;

		for (int i = 0; i < temp.length; i++) {
			if(temp[i] != null) {
				Node startNode = temp[i].getValue();
				String key = (String) temp[i].getKey();

				do {

					textName = (String)startNode.getkey();
					int a = hashValue(key);
					put(a, key, textName);
					startNode = startNode.getNext();
				}
				while(startNode != null);


			}

		}


		System.out.println("--- RESIZING IS COMPLETED ");
		System.out.println("--- OLD SIZE: " + temp.length + " --- NEW SIZE: " + table.length );
	}

	public String get(int hash) {
		String texts = " ";
		Node temp = table[hash].getValue();
		int count = table[hash].getCount();

		while(temp.getNext() != null) {
			
			texts = texts +  temp.getRepeatCount()+ " - " + temp.getkey() + "\n" ;
			temp = temp.getNext();
		}
		texts = (count + " documents found.\n") + texts; 
		return texts;

	}

	public void sizeControl() {
		int size = 0;
		for (int j = 0; j < table.length; j++) {
			if(table[j] != null)
				size++;
		}
		if(size >= loadfactor* TABLE_SIZE ) {
			resize();
		}
	}

	public int getTABLE_SIZE() {
		return TABLE_SIZE;
	}

	public HashEntry[] getTable() {
		return table;
	}

	public void setTable(HashEntry[] table) {
		this.table = table;
	}
	

public class HashEntry<K>{

	private K key;
	private Node value;	// first node
	private int count;

	public HashEntry(K key, Node value) {
		this.key = key;
		this.value = value;
		this.count = 1;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K Key) {
		this.key = Key;
	}

	public Node getValue() {
		return value;
	}

	public void setValue(Node value) {
		this.value = value;
	}

	public int getCount() {
		return count;
	}

	public void increaseCount() {
		this.count++;
	}
	
	}

	public boolean isEmpty() {
		return putCount == 0;
	}

	public int getSize() {
		return (int) putCount;
	}
	
	public void clear() {
		putCount = 0;
	}
	




}
