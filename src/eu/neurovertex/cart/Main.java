package eu.neurovertex.cart;

/**
 * Created with IntelliJ IDEA.
 * User: nomiros
 * Date: 02/12/14
 * Time: 17:53
 */
public class Main {
	public static void main(String[] args) {
		DecisionTreeBuilder builder = new DecisionTreeBuilder();
		int[][][] data = new int[][][]{
				{	{6, 37}, {91, 1}
				}, {{2, 21}, {1, 41}}
		};

		Instance<Classes> instance = new Instance<>(Classes.Sain);
		for (Temperature temp : Temperature.values())
			for (Gorge gorge : Gorge.values())
				for (Classes cl : Classes.values()) {
					instance = new Instance<>(cl);
					instance.set("Température", temp);
					instance.set("Gorge", gorge);
					builder.add(instance, data[temp.ordinal()][gorge.ordinal()][cl.ordinal()]);
				}
		builder.setMinExamples(30);
		builder.setToleratedImpurity(0.1);
		DecisionTree<Classes> tree =  builder.build();

		for (Temperature temp : Temperature.values()) {
			instance.set("Température", temp);
			for (Gorge gorge : Gorge.values()) {
				instance.set("Gorge", gorge);
				System.out.printf("<Temp=%s,Gorge=%s> -> %s (S=%d,M=%d)%n", temp.name(), gorge.name(), tree.getValue(instance),
									data[temp.ordinal()][gorge.ordinal()][Classes.Sain.ordinal()],
									data[temp.ordinal()][gorge.ordinal()][Classes.Malade.ordinal()]);
			}
		}
		System.out.println();
		System.out.println(tree.toLatex());
	}

	enum Classes {
		Sain, Malade
	}

	enum Temperature {
		Basse, Haute
	}

	enum Gorge {
		Irritee, NonIrritee
	}
}
