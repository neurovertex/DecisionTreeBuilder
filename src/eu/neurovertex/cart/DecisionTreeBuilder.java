package eu.neurovertex.cart;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: nomiros
 * Date: 02/12/14
 * Time: 18:22
 */
public class DecisionTreeBuilder<E extends Enum> {
	private final Map<Instance<E>, Integer> map = new HashMap<>();
	private final Map<String, List<Enum>> attributes = new HashMap<>();
	private final List<Enum> values = new ArrayList<>();
	private Predicate<Map<Instance<E>, Integer>> stopCondition = m -> false;

	public void add(Instance<E> key, int nb) {
		map.put(key, (map.containsKey(key) ? map.get(key) : 0) + nb);
		if (values.size() == 0) {
			//noinspection unchecked
			Collections.addAll(values, (E[]) key.getValue().getDeclaringClass().getEnumConstants());
			for (String k : key.getKeys()) {
				List<Enum> values = new ArrayList<>();
				Collections.addAll(values, (Enum[]) key.get(k).getDeclaringClass().getEnumConstants());
				attributes.put(k, values);
			}
		}
	}

	public DecisionTree<E> build() {
		return build(map);
	}

	public void setToleratedImpurity(double d) {
		if (d < 0 || d > 1.0)
			throw new IllegalArgumentException("Invalid impurity. Must be in [0,1]");
		stopCondition = stopCondition.or(m -> impurity(m, Optional.empty()) <= d);
	}

	public void setMinExamples(int min) {
		if (min < 0)
			throw new IllegalArgumentException("Negative sample size");
		stopCondition = stopCondition.or(m -> m.values().stream().mapToInt(i -> i).sum() < min);
	}

	public void resetStopCondition() {
		stopCondition = m -> false;
	}

	private DecisionTree<E> build(Map<Instance<E>, Integer> map) {
		if (!stopCondition.test(map)) {
			Map<String, Double> gains = attributes.keySet().stream()
					.collect(Collectors.toMap(k -> k, k -> gain(map, k)));
			Optional<Map.Entry<String, Double>> entry = gains.entrySet().stream().filter(e -> Double.isFinite(e.getValue())).sorted(Comparator.comparingDouble(Map.Entry::getValue)).findFirst();
			double impurity = impurity(map, Optional.empty());
			if (!entry.map(e -> impurity - e.getValue() <= 0.00001).orElse(true)) // double rounding error-safe nullity comparison
			{
				String key = entry.get().getKey();
				Enum val = getMajoritaryClass(map, Optional.of(key));
				Map<Instance<E>, Integer> complement = filter(map, Optional.of(key), val);
				return new BinarySplitNode<>(key, val, build(map), build(complement));
			}
		}
		return new SingleClassNode<>((E) getMajoritaryClass(map, Optional.empty()));
	}

	private double gain(Map<Instance<E>, Integer> map, String key) {
		Enum val = getMajoritaryClass(map, Optional.of(key));
		Map<Instance<E>, Integer> filtered = new HashMap<>(map), complement = filter(filtered, Optional.of(key), val);
		return impurity(filtered, Optional.empty()) + impurity(complement, Optional.empty());
	}

	private Map<Instance<E>, Integer> filter(Map<Instance<E>, Integer> map, Optional<String> optKey, Enum val) {
		Map<Instance<E>, Integer> complement = new HashMap<>();
		Set<Instance<E>> keys = map.keySet().stream().filter(k -> !(optKey.isPresent() && k.get(optKey.get()).equals(val)) || k.getValue().equals(val))
				.collect(Collectors.toSet());
		keys.forEach(k -> complement.put(k, map.remove(k)));
		return complement;
	}

	private double impurity(Map<Instance<E>, Integer> map, Optional<String> optKey) {
		int[] counts = new int[optKey.map(k -> attributes.get(k).size())
				.orElse(values.size())];
		int sum = 0;

		for (Enum val : optKey.map(attributes::get).orElse(values)) {
			sum += counts[val.ordinal()] = count(map, optKey, val);
		}

		double impurity;
		if (sum > 0) {
			impurity = 1.;
			for (int i : counts) {
				impurity -= Math.pow(i / (double) sum, 2);
			}
		} else
			impurity = 0.;

		return impurity;
	}

	private int count(Map<Instance<E>, Integer> map, Optional<String> key, Enum val) {
		int cnt = 0;
		for (Instance<E> k : map.keySet()) {
			if (key.isPresent() && k.get(key.get()).equals(val) || k.getValue().equals(val))
				cnt += map.get(k);
		}
		return cnt;
	}

	private Enum getMajoritaryClass(Map<Instance<E>, Integer> map, Optional<String> key) {
		Map<Enum, Integer> counts = new HashMap<>();
		key.map(attributes::get).orElse(values).stream().forEach(v -> counts.put(v, -count(map, key, v)));
		//noinspection unchecked
		return counts.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).findFirst().get().getKey();
	}

}
