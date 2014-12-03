package eu.neurovertex.cart;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Instance<E extends Enum> {
	private Map<String, Enum> map = new HashMap<>();
	private E value;

	public Instance(E val) {
		this.value = val;

	}

	public Instance set(String key, Enum val) {
		map.put(key, val);
		return this; // For method chaining
	}

	public Enum get(String key) {
		return map.get(key);
	}

	public Collection<String> getKeys() {
		return map.keySet();
	}

	@Override
	public int hashCode() {
		return map.hashCode() ^ value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Instance)
				&& ((Instance)obj).map.equals(map) && ((Instance)obj).value.equals(value);
	}

	public E getValue() {
		return value;
	}
}
