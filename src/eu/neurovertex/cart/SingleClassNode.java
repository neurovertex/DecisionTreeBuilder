package eu.neurovertex.cart;

/**
 * Created with IntelliJ IDEA.
 * User: nomiros
 * Date: 03/12/14
 * Time: 00:07
 */
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
	public E getValue(Instance<E> e) {
		return value;
	}

	@Override
	protected void toLatex(StringBuilder sb, String suffix, String indent) {
		sb.append(value.name()).append(suffix);
	}
}
