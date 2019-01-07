import JSHOP2.*;

import java.util.LinkedList;

public class problem
{
	private static String[] defineConstants()
	{
		String[] problemConstants = new String[75];

		problemConstants[0] = "tasktype";
		problemConstants[1] = "data-mining-task-type-instance";
		problemConstants[2] = "dataminingrequirement";
		problemConstants[3] = "data-mining-requirement-instance";
		problemConstants[4] = "targetclass";
		problemConstants[5] = "target-class-instance";
		problemConstants[6] = "defects";
		problemConstants[7] = "feature1";
		problemConstants[8] = "feature-instance";
		problemConstants[9] = "decision_densityattr";
		problemConstants[10] = "numeric";
		problemConstants[11] = "has-data-type";
		problemConstants[12] = "feature2";
		problemConstants[13] = "condition_countattr";
		problemConstants[14] = "feature3";
		problemConstants[15] = "design_complexityattr";
		problemConstants[16] = "feature4";
		problemConstants[17] = "comment_locattr";
		problemConstants[18] = "feature5";
		problemConstants[19] = "branch_countattr";
		problemConstants[20] = "feature6";
		problemConstants[21] = "total_operatorsattr";
		problemConstants[22] = "feature7";
		problemConstants[23] = "halstead_vocabularyattr";
		problemConstants[24] = "feature8";
		problemConstants[25] = "executable_locattr";
		problemConstants[26] = "feature9";
		problemConstants[27] = "halstead_difficultyattr";
		problemConstants[28] = "feature10";
		problemConstants[29] = "halstead_errorattr";
		problemConstants[30] = "feature11";
		problemConstants[31] = "halstead_lengthattr";
		problemConstants[32] = "feature12";
		problemConstants[33] = "normalized_cyclomatic_complexityattr";
		problemConstants[34] = "feature13";
		problemConstants[35] = "call_pairsattr";
		problemConstants[36] = "feature14";
		problemConstants[37] = "design_densityattr";
		problemConstants[38] = "feature15";
		problemConstants[39] = "total_locattr";
		problemConstants[40] = "feature16";
		problemConstants[41] = "decision_countattr";
		problemConstants[42] = "feature17";
		problemConstants[43] = "cyclomatic_complexityattr";
		problemConstants[44] = "feature18";
		problemConstants[45] = "halstead_effortattr";
		problemConstants[46] = "feature19";
		problemConstants[47] = "total_operandsattr";
		problemConstants[48] = "feature20";
		problemConstants[49] = "formal_parametersattr";
		problemConstants[50] = "feature21";
		problemConstants[51] = "blank_locattr";
		problemConstants[52] = "feature22";
		problemConstants[53] = "unique_operatorsattr";
		problemConstants[54] = "feature23";
		problemConstants[55] = "multiple_condition_countattr";
		problemConstants[56] = "feature24";
		problemConstants[57] = "halstead_levelattr";
		problemConstants[58] = "feature25";
		problemConstants[59] = "code_and_comment_locattr";
		problemConstants[60] = "feature26";
		problemConstants[61] = "unique_operandsattr";
		problemConstants[62] = "feature27";
		problemConstants[63] = "halstead_timeattr";
		problemConstants[64] = "feature28";
		problemConstants[65] = "cyclomatic_densityattr";
		problemConstants[66] = "feature29";
		problemConstants[67] = "halstead_volumeattr";
		problemConstants[68] = "datatable";
		problemConstants[69] = "data-table-instance";
		problemConstants[70] = "ar1";
		problemConstants[71] = "dataminingmodel";
		problemConstants[72] = "data-mining-model-instance";
		problemConstants[73] = "dataminingresult";
		problemConstants[74] = "data-mining-result-instance";

		return problemConstants;
	}

	private static void createState0(State s)	{
		s.add(new Predicate(73, 0, new TermList(TermConstant.getConstant(74), new TermList(TermConstant.getConstant(72), TermList.NIL))));
		s.add(new Predicate(69, 0, new TermList(TermConstant.getConstant(76), TermList.NIL)));
		s.add(new Predicate(57, 0, new TermList(TermConstant.getConstant(76), new TermList(TermConstant.getConstant(60), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(78), new TermList(TermConstant.getConstant(80), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(81), new TermList(TermConstant.getConstant(83), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(81), new TermList(new TermNumber(24.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(81), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(86), new TermList(TermConstant.getConstant(87), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(86), new TermList(new TermNumber(20.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(86), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(88), new TermList(TermConstant.getConstant(89), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(88), new TermList(new TermNumber(25.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(88), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(90), new TermList(TermConstant.getConstant(91), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(90), new TermList(new TermNumber(2.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(90), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(92), new TermList(TermConstant.getConstant(93), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(92), new TermList(new TermNumber(17.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(92), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(94), new TermList(TermConstant.getConstant(95), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(94), new TermList(new TermNumber(8.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(94), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(96), new TermList(TermConstant.getConstant(97), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(96), new TermList(new TermNumber(9.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(96), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(98), new TermList(TermConstant.getConstant(99), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(98), new TermList(new TermNumber(4.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(98), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(100), new TermList(TermConstant.getConstant(101), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(100), new TermList(new TermNumber(13.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(100), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(102), new TermList(TermConstant.getConstant(103), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(102), new TermList(new TermNumber(15.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(102), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(104), new TermList(TermConstant.getConstant(105), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(104), new TermList(new TermNumber(10.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(104), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(106), new TermList(TermConstant.getConstant(107), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(106), new TermList(new TermNumber(27.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(106), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(108), new TermList(TermConstant.getConstant(109), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(108), new TermList(new TermNumber(19.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(108), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(110), new TermList(TermConstant.getConstant(111), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(110), new TermList(new TermNumber(26.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(110), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(112), new TermList(TermConstant.getConstant(113), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(112), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(112), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(114), new TermList(TermConstant.getConstant(115), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(114), new TermList(new TermNumber(18.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(114), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(116), new TermList(TermConstant.getConstant(117), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(116), new TermList(new TermNumber(22.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(116), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(118), new TermList(TermConstant.getConstant(119), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(118), new TermList(new TermNumber(14.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(118), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(120), new TermList(TermConstant.getConstant(121), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(120), new TermList(new TermNumber(7.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(120), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(122), new TermList(TermConstant.getConstant(123), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(122), new TermList(new TermNumber(28.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(122), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(124), new TermList(TermConstant.getConstant(125), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(124), new TermList(new TermNumber(1.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(124), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(126), new TermList(TermConstant.getConstant(127), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(126), new TermList(new TermNumber(6.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(126), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(128), new TermList(TermConstant.getConstant(129), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(128), new TermList(new TermNumber(21.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(128), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(130), new TermList(TermConstant.getConstant(131), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(130), new TermList(new TermNumber(12.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(130), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(132), new TermList(TermConstant.getConstant(133), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(132), new TermList(new TermNumber(3.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(132), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(134), new TermList(TermConstant.getConstant(135), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(134), new TermList(new TermNumber(5.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(134), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(136), new TermList(TermConstant.getConstant(137), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(136), new TermList(new TermNumber(16.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(136), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(138), new TermList(TermConstant.getConstant(139), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(138), new TermList(new TermNumber(23.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(138), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(140), new TermList(TermConstant.getConstant(141), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(140), new TermList(new TermNumber(11.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(140), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(15, 0, new TermList(TermConstant.getConstant(142), new TermList(new TermList(TermConstant.getConstant(81), new TermList(TermConstant.getConstant(86), new TermList(TermConstant.getConstant(88), new TermList(TermConstant.getConstant(90), new TermList(TermConstant.getConstant(92), new TermList(TermConstant.getConstant(94), new TermList(TermConstant.getConstant(96), new TermList(TermConstant.getConstant(98), new TermList(TermConstant.getConstant(100), new TermList(TermConstant.getConstant(102), new TermList(TermConstant.getConstant(104), new TermList(TermConstant.getConstant(106), new TermList(TermConstant.getConstant(108), new TermList(TermConstant.getConstant(110), new TermList(TermConstant.getConstant(112), new TermList(TermConstant.getConstant(114), new TermList(TermConstant.getConstant(116), new TermList(TermConstant.getConstant(118), new TermList(TermConstant.getConstant(120), new TermList(TermConstant.getConstant(122), new TermList(TermConstant.getConstant(124), new TermList(TermConstant.getConstant(126), new TermList(TermConstant.getConstant(128), new TermList(TermConstant.getConstant(130), new TermList(TermConstant.getConstant(132), new TermList(TermConstant.getConstant(134), new TermList(TermConstant.getConstant(136), new TermList(TermConstant.getConstant(138), new TermList(TermConstant.getConstant(140), TermList.NIL))))))))))))))))))))))))))))), TermList.NIL))));
		s.add(new Predicate(50, 0, new TermList(TermConstant.getConstant(142), new TermList(TermConstant.getConstant(78), TermList.NIL))));
		s.add(new Predicate(65, 0, new TermList(TermConstant.getConstant(142), new TermList(TermConstant.getConstant(64), TermList.NIL))));
		s.add(new Predicate(54, 0, new TermList(TermConstant.getConstant(142), new TermList(new TermNumber(121.0), TermList.NIL))));
		s.add(new Predicate(53, 0, new TermList(TermConstant.getConstant(142), new TermList(new TermNumber(29.0), TermList.NIL))));
		s.add(new Predicate(49, 0, new TermList(TermConstant.getConstant(142), new TermList(TermConstant.getConstant(51), TermList.NIL))));
		s.add(new Predicate(6, 0, new TermList(TermConstant.getConstant(142), new TermList(TermConstant.getConstant(144), TermList.NIL))));
		s.add(new Predicate(37, 0, new TermList(TermConstant.getConstant(147), TermList.NIL)));
		s.add(new Predicate(37, 0, new TermList(TermConstant.getConstant(145), TermList.NIL)));
	}

	public static LinkedList<Plan> getPlans()
	{
		LinkedList<Plan> returnedPlans = new LinkedList<Plan>();
		TermConstant.initialize(149);

		Domain d = new datamining();

		d.setProblemConstants(defineConstants());

		State s = new State(74, d.getAxioms());

		JSHOP2.initialize(d, s);

		TaskList tl;
		SolverThread thread;

		createState0(s);

		tl = new TaskList(1, true);
		tl.subtasks[0] = new TaskList(new TaskAtom(new Predicate(7, 0, new TermList(TermConstant.getConstant(74), new TermList(TermConstant.getConstant(76), new TermList(TermConstant.getConstant(142), new TermList(TermConstant.getConstant(145), new TermList(TermConstant.getConstant(147), TermList.NIL)))))), false, false));

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