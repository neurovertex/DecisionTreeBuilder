package eu.neurovertex.cart;

import java.util.Arrays;
import java.util.List;

class SingleClassNode<E extends Enum> extends DecisionTree<E> {
	private E value;

	SingleClassNode(E value) {
		this.value = value;
	}

	@Override
	protected DecisionTree<E> getChild(Instance<E> e) {
		return null;
	}

	@Override
	public List<DecisionTree<E>> getChildren() {
		return Arrays.asList();
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public E getValue(Instance<E> e) {
		return value;
	}

	@Override
	protected void toLatex(StringBuilder sb, String suffix, String indent) {
		sb.append(value.name()).append(suffix);
	}

}
