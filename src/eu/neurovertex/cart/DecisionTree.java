package eu.neurovertex.cart;

import java.util.List;

public abstract class DecisionTree<E extends Enum> {
	protected abstract DecisionTree<E> getChild(Instance<E> e);

	public abstract List<DecisionTree<E>> getChildren();

	public abstract boolean isLeaf();

	public abstract E getValue(Instance<E> e);

	protected abstract void toLatex(StringBuilder sb, String suffix, String indent);

	public final String toLatex() {
		StringBuilder sb = new StringBuilder("[");
		toLatex(sb, "", "\t");
		sb.append("]\n");
		return sb.toString();
	}

}

