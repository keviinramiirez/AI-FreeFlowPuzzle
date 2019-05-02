package interfaces;

public interface Entry<K, V> {
	K getKey(); 
	V getValue(); 
	void setKey(K k);
}
