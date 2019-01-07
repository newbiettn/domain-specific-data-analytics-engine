package planner.jshop;
import java.util.LinkedList;
import JSHOP2.*;

public class problem implements ProblemInterface

{
	private static String[] defineConstants()
	{
		String[] problemConstants = new String[61];

		problemConstants[0] = "tasktype";
		problemConstants[1] = "data-mining-task-type-instance";
		problemConstants[2] = "dataminingrequirement";
		problemConstants[3] = "data-mining-requirement-instance";
		problemConstants[4] = "targetclass";
		problemConstants[5] = "target-class-instance";
		problemConstants[6] = "class";
		problemConstants[7] = "feature1";
		problemConstants[8] = "feature-instance";
		problemConstants[9] = "stalk-color-above-ringattr";
		problemConstants[10] = "nominal";
		problemConstants[11] = "has-data-type";
		problemConstants[12] = "feature2";
		problemConstants[13] = "ring-typeattr";
		problemConstants[14] = "feature3";
		problemConstants[15] = "habitatattr";
		problemConstants[16] = "feature4";
		problemConstants[17] = "cap-surfaceattr";
		problemConstants[18] = "feature5";
		problemConstants[19] = "stalk-surface-below-ringattr";
		problemConstants[20] = "feature6";
		problemConstants[21] = "stalk-shapeattr";
		problemConstants[22] = "feature7";
		problemConstants[23] = "stalk-rootattr";
		problemConstants[24] = "feature8";
		problemConstants[25] = "odorattr";
		problemConstants[26] = "feature9";
		problemConstants[27] = "gill-attachmentattr";
		problemConstants[28] = "feature10";
		problemConstants[29] = "gill-sizeattr";
		problemConstants[30] = "feature11";
		problemConstants[31] = "veil-colorattr";
		problemConstants[32] = "feature12";
		problemConstants[33] = "ring-numberattr";
		problemConstants[34] = "feature13";
		problemConstants[35] = "populationattr";
		problemConstants[36] = "feature14";
		problemConstants[37] = "spore-print-colorattr";
		problemConstants[38] = "feature15";
		problemConstants[39] = "stalk-color-below-ringattr";
		problemConstants[40] = "feature16";
		problemConstants[41] = "cap-shapeattr";
		problemConstants[42] = "feature17";
		problemConstants[43] = "gill-colorattr";
		problemConstants[44] = "feature18";
		problemConstants[45] = "veil-typeattr";
		problemConstants[46] = "feature19";
		problemConstants[47] = "cap-colorattr";
		problemConstants[48] = "feature20";
		problemConstants[49] = "bruisesattr";
		problemConstants[50] = "feature21";
		problemConstants[51] = "stalk-surface-above-ringattr";
		problemConstants[52] = "feature22";
		problemConstants[53] = "gill-spacingattr";
		problemConstants[54] = "datatable";
		problemConstants[55] = "data-table-instance";
		problemConstants[56] = "training_mushroom-randomize-s800";
		problemConstants[57] = "dataminingmodel";
		problemConstants[58] = "data-mining-model-instance";
		problemConstants[59] = "dataminingresult";
		problemConstants[60] = "data-mining-result-instance";

		return problemConstants;
	}

	private static void createState0(State s)	{
		s.add(new Predicate(110, 0, new TermList(TermConstant.getConstant(111), new TermList(TermConstant.getConstant(109), TermList.NIL))));
		s.add(new Predicate(103, 0, new TermList(TermConstant.getConstant(113), TermList.NIL)));
		s.add(new Predicate(82, 0, new TermList(TermConstant.getConstant(113), new TermList(TermConstant.getConstant(87), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(115), new TermList(TermConstant.getConstant(117), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(118), new TermList(TermConstant.getConstant(120), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(118), new TermList(new TermNumber(13.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(118), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(123), new TermList(TermConstant.getConstant(124), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(123), new TermList(new TermNumber(18.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(123), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(125), new TermList(TermConstant.getConstant(126), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(125), new TermList(new TermNumber(21.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(125), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(127), new TermList(TermConstant.getConstant(128), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(127), new TermList(new TermNumber(1.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(127), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(129), new TermList(TermConstant.getConstant(130), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(129), new TermList(new TermNumber(12.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(129), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(131), new TermList(TermConstant.getConstant(132), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(131), new TermList(new TermNumber(9.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(131), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(133), new TermList(TermConstant.getConstant(134), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(133), new TermList(new TermNumber(10.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(133), new TermList(new TermNumber(30.866287121095553), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(135), new TermList(TermConstant.getConstant(136), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(135), new TermList(new TermNumber(4.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(135), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(137), new TermList(TermConstant.getConstant(138), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(137), new TermList(new TermNumber(5.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(137), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(139), new TermList(TermConstant.getConstant(140), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(139), new TermList(new TermNumber(7.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(139), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(141), new TermList(TermConstant.getConstant(142), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(141), new TermList(new TermNumber(16.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(141), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(143), new TermList(TermConstant.getConstant(144), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(143), new TermList(new TermNumber(17.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(143), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(145), new TermList(TermConstant.getConstant(146), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(145), new TermList(new TermNumber(20.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(145), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(147), new TermList(TermConstant.getConstant(148), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(147), new TermList(new TermNumber(19.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(147), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(149), new TermList(TermConstant.getConstant(150), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(149), new TermList(new TermNumber(14.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(149), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(151), new TermList(TermConstant.getConstant(152), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(151), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(151), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(153), new TermList(TermConstant.getConstant(154), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(153), new TermList(new TermNumber(8.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(153), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(155), new TermList(TermConstant.getConstant(156), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(155), new TermList(new TermNumber(15.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(155), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(157), new TermList(TermConstant.getConstant(158), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(157), new TermList(new TermNumber(2.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(157), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(159), new TermList(TermConstant.getConstant(160), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(159), new TermList(new TermNumber(3.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(159), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(161), new TermList(TermConstant.getConstant(162), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(161), new TermList(new TermNumber(11.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(161), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(163), new TermList(TermConstant.getConstant(164), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(163), new TermList(new TermNumber(6.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(163), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(15, 0, new TermList(TermConstant.getConstant(165), new TermList(new TermList(TermConstant.getConstant(118), new TermList(TermConstant.getConstant(123), new TermList(TermConstant.getConstant(125), new TermList(TermConstant.getConstant(127), new TermList(TermConstant.getConstant(129), new TermList(TermConstant.getConstant(131), new TermList(TermConstant.getConstant(133), new TermList(TermConstant.getConstant(135), new TermList(TermConstant.getConstant(137), new TermList(TermConstant.getConstant(139), new TermList(TermConstant.getConstant(141), new TermList(TermConstant.getConstant(143), new TermList(TermConstant.getConstant(145), new TermList(TermConstant.getConstant(147), new TermList(TermConstant.getConstant(149), new TermList(TermConstant.getConstant(151), new TermList(TermConstant.getConstant(153), new TermList(TermConstant.getConstant(155), new TermList(TermConstant.getConstant(157), new TermList(TermConstant.getConstant(159), new TermList(TermConstant.getConstant(161), new TermList(TermConstant.getConstant(163), TermList.NIL)))))))))))))))))))))), TermList.NIL))));
		s.add(new Predicate(75, 0, new TermList(TermConstant.getConstant(165), new TermList(TermConstant.getConstant(115), TermList.NIL))));
		s.add(new Predicate(90, 0, new TermList(TermConstant.getConstant(165), new TermList(TermConstant.getConstant(89), TermList.NIL))));
		s.add(new Predicate(79, 0, new TermList(TermConstant.getConstant(165), new TermList(new TermNumber(6499.0), TermList.NIL))));
		s.add(new Predicate(78, 0, new TermList(TermConstant.getConstant(165), new TermList(new TermNumber(22.0), TermList.NIL))));
		s.add(new Predicate(74, 0, new TermList(TermConstant.getConstant(165), new TermList(TermConstant.getConstant(76), TermList.NIL))));
		s.add(new Predicate(77, 0, new TermList(TermConstant.getConstant(165), TermList.NIL)));
		s.add(new Predicate(6, 0, new TermList(TermConstant.getConstant(165), new TermList(TermConstant.getConstant(167), TermList.NIL))));
		s.add(new Predicate(38, 0, new TermList(TermConstant.getConstant(170), TermList.NIL)));
		s.add(new Predicate(38, 0, new TermList(TermConstant.getConstant(168), TermList.NIL)));
	}

	public static LinkedList<Plan> getPlans()
	{
		LinkedList<Plan> returnedPlans = new LinkedList<Plan>();
		TermConstant.initialize(172);

		Domain d = new datamining();

		d.setProblemConstants(defineConstants());

		State s = new State(111, d.getAxioms());

		JSHOP2.initialize(d, s);

		TaskList tl;
		SolverThread thread;

		createState0(s);

		tl = new TaskList(1, true);
		tl.subtasks[0] = new TaskList(new TaskAtom(new Predicate(7, 0, new TermList(TermConstant.getConstant(111), new TermList(TermConstant.getConstant(113), new TermList(TermConstant.getConstant(165), new TermList(TermConstant.getConstant(168), new TermList(TermConstant.getConstant(170), TermList.NIL)))))), false, false));

		thread = new SolverThread(tl, 1);
		thread.start();

		try {
			while (thread.isAlive())
				Thread.sleep(500);
		} catch (InterruptedException e) {
		}

		returnedPlans.addAll( thread.getPlans() );

		return returnedPlans;
	}

	public static LinkedList<Predicate> getFirstPlanOps() {
		return getPlans().getFirst().getOps();
	}
}
