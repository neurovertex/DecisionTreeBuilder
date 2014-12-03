package eu.neurovertex.cart;

import java.util.Arrays;
import java.util.List;

class BinarySplitNode<E extends Enum> extends DecisionTree<E> {
	private static final String SUFFIX_PATTERN = ", edge label={node[midway,%s,font=\\scriptsize]{%s}}",
								ELSENAME_PATTERN = "¬%s";
	private final String attribute;
	private final Enum val;
	private final DecisionTree<E> ifEqual, ifNonEqual;

	BinarySplitNode(String attribute, Enum val, DecisionTree<E> ifEqual, DecisionTree<E> ifNonEqual) {
		this.attribute = attribute;
		this.val = val;
		this.ifEqual = ifEqual;
		this.ifNonEqual = ifNonEqual;
	}

	@Override
	protected DecisionTree<E> getChild(Instance<E> e) {
		return e.get(attribute).equals(val) ? ifEqual : ifNonEqual;
	}

	@Override
	public List<DecisionTree<E>> getChildren() {
		return Arrays.asList(ifEqual, ifNonEqual);
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public E getValue(Instance<E> e) {
		return getChild(e).getValue(e);
	}

	@Override
	protected void toLatex(StringBuilder sb, String suffix, String indent) {
		sb.append(attribute).append(suffix).append('\n').append(indent).append('[');
		ifEqual.toLatex(sb, String.format(SUFFIX_PATTERN, "left", val.name()), indent + "\t");
		sb.append("]\n").append(indent).append("[");
		String elseName;
		if (val.getDeclaringClass().getEnumConstants().length == 2)
			elseName = ((Enum)val.getDeclaringClass().getEnumConstants()[1-val.ordinal()]).name();
		else
			elseName = String.format(ELSENAME_PATTERN, val.name());
		ifNonEqual.toLatex(sb, String.format(SUFFIX_PATTERN, "right", elseName), indent + "\t");
		sb.append("]");
	}

	public String getAttribute() {
		return attribute;
	}

	public Enum getValue() {
		return val;
	}
}
