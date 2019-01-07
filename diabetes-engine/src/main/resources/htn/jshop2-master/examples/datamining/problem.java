import java.util.LinkedList;
import JSHOP2.*;

public class problem
{
	private static String[] defineConstants()
	{
		String[] problemConstants = new String[25];

		problemConstants[0] = "tasktype";
		problemConstants[1] = "data-mining-task-type-instance";
		problemConstants[2] = "dataminingrequirement";
		problemConstants[3] = "data-mining-requirement-instance";
		problemConstants[4] = "targetclass";
		problemConstants[5] = "target-class-instance";
		problemConstants[6] = "class";
		problemConstants[7] = "feature1";
		problemConstants[8] = "feature-instance";
		problemConstants[9] = "feature2";
		problemConstants[10] = "feature3";
		problemConstants[11] = "feature4";
		problemConstants[12] = "sepallength";
		problemConstants[13] = "sepalwidth";
		problemConstants[14] = "petallength";
		problemConstants[15] = "petalwidth";
		problemConstants[16] = "has-data-type";
		problemConstants[17] = "categorical";
		problemConstants[18] = "datatable";
		problemConstants[19] = "data-table-instance";
		problemConstants[20] = "iris";
		problemConstants[21] = "dataminingmodel";
		problemConstants[22] = "data-mining-model-instance";
		problemConstants[23] = "dataminingresult";
		problemConstants[24] = "data-mining-result-instance";

		return problemConstants;
	}

	private static void createState0(State s)	{
		s.add(new Predicate(54, 0, new TermList(TermConstant.getConstant(55), new TermList(TermConstant.getConstant(53), TermList.NIL))));
		s.add(new Predicate(10, 0, new TermList(TermConstant.getConstant(59), new TermList(TermConstant.getConstant(61), TermList.NIL))));
		s.add(new Predicate(10, 0, new TermList(TermConstant.getConstant(62), new TermList(TermConstant.getConstant(67), TermList.NIL))));
		s.add(new Predicate(10, 0, new TermList(TermConstant.getConstant(64), new TermList(TermConstant.getConstant(68), TermList.NIL))));
		s.add(new Predicate(10, 0, new TermList(TermConstant.getConstant(65), new TermList(TermConstant.getConstant(69), TermList.NIL))));
		s.add(new Predicate(10, 0, new TermList(TermConstant.getConstant(66), new TermList(TermConstant.getConstant(70), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(62), new TermList(new TermNumber(1.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(64), new TermList(new TermNumber(2.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(65), new TermList(new TermNumber(3.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(66), new TermList(new TermNumber(4.0), TermList.NIL))));
		s.add(new Predicate(15, 0, new TermList(TermConstant.getConstant(62), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(15, 0, new TermList(TermConstant.getConstant(64), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(15, 0, new TermList(TermConstant.getConstant(65), new TermList(new TermNumber(10.0), TermList.NIL))));
		s.add(new Predicate(15, 0, new TermList(TermConstant.getConstant(66), new TermList(new TermNumber(50.0), TermList.NIL))));
		s.add(new Predicate(13, 0, new TermList(TermConstant.getConstant(73), new TermList(new TermList(TermConstant.getConstant(62), new TermList(TermConstant.getConstant(64), new TermList(TermConstant.getConstant(65), new TermList(TermConstant.getConstant(66), TermList.NIL)))), TermList.NIL))));
		s.add(new Predicate(41, 0, new TermList(TermConstant.getConstant(73), new TermList(TermConstant.getConstant(59), TermList.NIL))));
		s.add(new Predicate(47, 0, new TermList(TermConstant.getConstant(73), new TermList(TermConstant.getConstant(46), TermList.NIL))));
		s.add(new Predicate(45, 0, new TermList(TermConstant.getConstant(73), new TermList(new TermNumber(30.0), TermList.NIL))));
		s.add(new Predicate(44, 0, new TermList(TermConstant.getConstant(73), new TermList(new TermNumber(4.0), TermList.NIL))));
		s.add(new Predicate(40, 0, new TermList(TermConstant.getConstant(73), new TermList(TermConstant.getConstant(42), TermList.NIL))));
		s.add(new Predicate(43, 0, new TermList(TermConstant.getConstant(73), TermList.NIL)));
		s.add(new Predicate(6, 0, new TermList(TermConstant.getConstant(73), new TermList(TermConstant.getConstant(75), TermList.NIL))));
		s.add(new Predicate(29, 0, new TermList(TermConstant.getConstant(78), TermList.NIL)));
		s.add(new Predicate(29, 0, new TermList(TermConstant.getConstant(76), TermList.NIL)));
	}

	public static LinkedList<Plan> getPlans()
	{
		LinkedList<Plan> returnedPlans = new LinkedList<Plan>();
		TermConstant.initialize(80);

		Domain d = new datamining();

		d.setProblemConstants(defineConstants());

		State s = new State(55, d.getAxioms());

		JSHOP2.initialize(d, s);

		TaskList tl;
		SolverThread thread;

		createState0(s);

		tl = new TaskList(1, true);
		tl.subtasks[0] = new TaskList(new TaskAtom(new Predicate(7, 0, new TermList(TermConstant.getConstant(55), new TermList(TermConstant.getConstant(57), new TermList(TermConstant.getConstant(73), new TermList(TermConstant.getConstant(76), new TermList(TermConstant.getConstant(78), TermList.NIL)))))), false, false));

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