package eu.neurovertex.cart;

/**
 * Created with IntelliJ IDEA.
 * User: nomiros
 * Date: 02/12/14
 * Time: 17:55
 */
public abstract class DecisionTree<E extends Enum> {
	protected abstract DecisionTree<E> getChild(Instance<E> e);
	public abstract E getValue(Instance<E> e);
	public final String toLatex() {
		StringBuilder sb = new StringBuilder("[");
		toLatex(sb, "", "\t");
		sb.append("]\n");
		return sb.toString();
	}

	protected abstract void toLatex(StringBuilder sb, String suffix, String indent);
}

