package planner.jshop;
import java.util.LinkedList;
import JSHOP2.*;

public class problem implements ProblemInterface

{
	private static String[] defineConstants()
	{
		String[] problemConstants = new String[22];

		problemConstants[0] = "tasktype";
		problemConstants[1] = "data-mining-task-type-instance";
		problemConstants[2] = "dataminingrequirement";
		problemConstants[3] = "data-mining-requirement-instance";
		problemConstants[4] = "targetclass";
		problemConstants[5] = "target-class-instance";
		problemConstants[6] = "deceased";
		problemConstants[7] = "feature1";
		problemConstants[8] = "feature-instance";
		problemConstants[9] = "genderattr";
		problemConstants[10] = "nominal";
		problemConstants[11] = "has-data-type";
		problemConstants[12] = "feature2";
		problemConstants[13] = "ageattr";
		problemConstants[14] = "numeric";
		problemConstants[15] = "datatable";
		problemConstants[16] = "data-table-instance";
		problemConstants[17] = "training_tmp";
		problemConstants[18] = "dataminingmodel";
		problemConstants[19] = "data-mining-model-instance";
		problemConstants[20] = "dataminingresult";
		problemConstants[21] = "data-mining-result-instance";

		return problemConstants;
	}

	private static void createState0(State s)	{
		s.add(new Predicate(110, 0, new TermList(TermConstant.getConstant(111), new TermList(TermConstant.getConstant(109), TermList.NIL))));
		s.add(new Predicate(96, 0, new TermList(TermConstant.getConstant(113), TermList.NIL)));
		s.add(new Predicate(82, 0, new TermList(TermConstant.getConstant(113), new TermList(TermConstant.getConstant(88), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(115), new TermList(TermConstant.getConstant(117), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(118), new TermList(TermConstant.getConstant(120), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(118), new TermList(new TermNumber(1.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(118), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(123), new TermList(TermConstant.getConstant(124), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(123), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(123), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(15, 0, new TermList(TermConstant.getConstant(126), new TermList(new TermList(TermConstant.getConstant(118), new TermList(TermConstant.getConstant(123), TermList.NIL)), TermList.NIL))));
		s.add(new Predicate(75, 0, new TermList(TermConstant.getConstant(126), new TermList(TermConstant.getConstant(115), TermList.NIL))));
		s.add(new Predicate(90, 0, new TermList(TermConstant.getConstant(126), new TermList(TermConstant.getConstant(89), TermList.NIL))));
		s.add(new Predicate(79, 0, new TermList(TermConstant.getConstant(126), new TermList(new TermNumber(35100.0), TermList.NIL))));
		s.add(new Predicate(78, 0, new TermList(TermConstant.getConstant(126), new TermList(new TermNumber(2.0), TermList.NIL))));
		s.add(new Predicate(74, 0, new TermList(TermConstant.getConstant(126), new TermList(TermConstant.getConstant(76), TermList.NIL))));
		s.add(new Predicate(6, 0, new TermList(TermConstant.getConstant(126), new TermList(TermConstant.getConstant(128), TermList.NIL))));
		s.add(new Predicate(38, 0, new TermList(TermConstant.getConstant(131), TermList.NIL)));
		s.add(new Predicate(38, 0, new TermList(TermConstant.getConstant(129), TermList.NIL)));
	}

	public static LinkedList<Plan> getPlans()
	{
		LinkedList<Plan> returnedPlans = new LinkedList<Plan>();
		TermConstant.initialize(133);

		Domain d = new datamining();

		d.setProblemConstants(defineConstants());

		State s = new State(111, d.getAxioms());

		JSHOP2.initialize(d, s);

		TaskList tl;
		SolverThread thread;

		createState0(s);

		tl = new TaskList(1, true);
		tl.subtasks[0] = new TaskList(new TaskAtom(new Predicate(7, 0, new TermList(TermConstant.getConstant(111), new TermList(TermConstant.getConstant(113), new TermList(TermConstant.getConstant(126), new TermList(TermConstant.getConstant(129), new TermList(TermConstant.getConstant(131), TermList.NIL)))))), false, false));

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
