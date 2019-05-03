package interfaces;

public interface Entry<K, V> {
	K getKey(); 
	V getValue(); 
	public void setKey(K k);
}
