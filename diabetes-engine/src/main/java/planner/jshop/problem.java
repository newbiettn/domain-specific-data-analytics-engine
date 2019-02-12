package planner.jshop;
import java.util.LinkedList;
import JSHOP2.*;

public class problem implements ProblemInterface

{
	private static String[] defineConstants()
	{
		String[] problemConstants = new String[94];

		problemConstants[0] = "tasktype";
		problemConstants[1] = "data-mining-task-type-instance";
		problemConstants[2] = "dataminingrequirement";
		problemConstants[3] = "data-mining-requirement-instance";
		problemConstants[4] = "targetclass";
		problemConstants[5] = "target-class-instance";
		problemConstants[6] = "class";
		problemConstants[7] = "feature1";
		problemConstants[8] = "feature-instance";
		problemConstants[9] = "steelattr";
		problemConstants[10] = "nominal";
		problemConstants[11] = "has-data-type";
		problemConstants[12] = "feature2";
		problemConstants[13] = "bcattr";
		problemConstants[14] = "feature3";
		problemConstants[15] = "corrattr";
		problemConstants[16] = "feature4";
		problemConstants[17] = "strengthattr";
		problemConstants[18] = "numeric";
		problemConstants[19] = "feature5";
		problemConstants[20] = "bfattr";
		problemConstants[21] = "feature6";
		problemConstants[22] = "cbondattr";
		problemConstants[23] = "feature7";
		problemConstants[24] = "jurofmattr";
		problemConstants[25] = "feature8";
		problemConstants[26] = "bwmeattr";
		problemConstants[27] = "feature9";
		problemConstants[28] = "blattr";
		problemConstants[29] = "feature10";
		problemConstants[30] = "packingattr";
		problemConstants[31] = "feature11";
		problemConstants[32] = "enamelabilityattr";
		problemConstants[33] = "feature12";
		problemConstants[34] = "thickattr";
		problemConstants[35] = "feature13";
		problemConstants[36] = "surface-finishattr";
		problemConstants[37] = "feature14";
		problemConstants[38] = "btattr";
		problemConstants[39] = "feature15";
		problemConstants[40] = "exptlattr";
		problemConstants[41] = "feature16";
		problemConstants[42] = "bluebrightvarncleanattr";
		problemConstants[43] = "feature17";
		problemConstants[44] = "oilattr";
		problemConstants[45] = "feature18";
		problemConstants[46] = "lenattr";
		problemConstants[47] = "feature19";
		problemConstants[48] = "carbonattr";
		problemConstants[49] = "feature20";
		problemConstants[50] = "non-ageingattr";
		problemConstants[51] = "feature21";
		problemConstants[52] = "surface-qualityattr";
		problemConstants[53] = "feature22";
		problemConstants[54] = "ferroattr";
		problemConstants[55] = "feature23";
		problemConstants[56] = "product-typeattr";
		problemConstants[57] = "feature24";
		problemConstants[58] = "formabilityattr";
		problemConstants[59] = "feature25";
		problemConstants[60] = "shapeattr";
		problemConstants[61] = "feature26";
		problemConstants[62] = "phosattr";
		problemConstants[63] = "feature27";
		problemConstants[64] = "mattr";
		problemConstants[65] = "feature28";
		problemConstants[66] = "boreattr";
		problemConstants[67] = "feature29";
		problemConstants[68] = "chromattr";
		problemConstants[69] = "feature30";
		problemConstants[70] = "lustreattr";
		problemConstants[71] = "feature31";
		problemConstants[72] = "pattr";
		problemConstants[73] = "feature32";
		problemConstants[74] = "hardnessattr";
		problemConstants[75] = "feature33";
		problemConstants[76] = "conditionattr";
		problemConstants[77] = "feature34";
		problemConstants[78] = "sattr";
		problemConstants[79] = "feature35";
		problemConstants[80] = "widthattr";
		problemConstants[81] = "feature36";
		problemConstants[82] = "familyattr";
		problemConstants[83] = "feature37";
		problemConstants[84] = "marviattr";
		problemConstants[85] = "feature38";
		problemConstants[86] = "temper_rollingattr";
		problemConstants[87] = "datatable";
		problemConstants[88] = "data-table-instance";
		problemConstants[89] = "anneal";
		problemConstants[90] = "dataminingmodel";
		problemConstants[91] = "data-mining-model-instance";
		problemConstants[92] = "dataminingresult";
		problemConstants[93] = "data-mining-result-instance";

		return problemConstants;
	}

	private static void createState0(State s)	{
		s.add(new Predicate(110, 0, new TermList(TermConstant.getConstant(111), new TermList(TermConstant.getConstant(109), TermList.NIL))));
		s.add(new Predicate(96, 0, new TermList(TermConstant.getConstant(113), TermList.NIL)));
		s.add(new Predicate(82, 0, new TermList(TermConstant.getConstant(113), new TermList(TermConstant.getConstant(84), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(115), new TermList(TermConstant.getConstant(117), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(118), new TermList(TermConstant.getConstant(120), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(118), new TermList(new TermNumber(2.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(118), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(123), new TermList(TermConstant.getConstant(124), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(123), new TermList(new TermNumber(13.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(123), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(125), new TermList(TermConstant.getConstant(126), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(125), new TermList(new TermNumber(25.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(125), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(127), new TermList(TermConstant.getConstant(128), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(127), new TermList(new TermNumber(8.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(127), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(130), new TermList(TermConstant.getConstant(131), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(130), new TermList(new TermNumber(14.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(130), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(132), new TermList(TermConstant.getConstant(133), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(132), new TermList(new TermNumber(21.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(132), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(134), new TermList(TermConstant.getConstant(135), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(134), new TermList(new TermNumber(28.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(134), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(136), new TermList(TermConstant.getConstant(137), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(136), new TermList(new TermNumber(16.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(136), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(138), new TermList(TermConstant.getConstant(139), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(138), new TermList(new TermNumber(17.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(138), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(140), new TermList(TermConstant.getConstant(141), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(140), new TermList(new TermNumber(37.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(140), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(142), new TermList(TermConstant.getConstant(143), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(142), new TermList(new TermNumber(12.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(142), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(144), new TermList(TermConstant.getConstant(145), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(144), new TermList(new TermNumber(32.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(144), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(146), new TermList(TermConstant.getConstant(147), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(146), new TermList(new TermNumber(10.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(146), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(148), new TermList(TermConstant.getConstant(149), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(148), new TermList(new TermNumber(15.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(148), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(150), new TermList(TermConstant.getConstant(151), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(150), new TermList(new TermNumber(23.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(150), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(152), new TermList(TermConstant.getConstant(153), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(152), new TermList(new TermNumber(26.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(152), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(154), new TermList(TermConstant.getConstant(155), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(154), new TermList(new TermNumber(35.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(154), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(156), new TermList(TermConstant.getConstant(157), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(156), new TermList(new TermNumber(34.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(156), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(158), new TermList(TermConstant.getConstant(159), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(158), new TermList(new TermNumber(3.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(158), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(160), new TermList(TermConstant.getConstant(161), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(160), new TermList(new TermNumber(9.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(160), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(162), new TermList(TermConstant.getConstant(163), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(162), new TermList(new TermNumber(11.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(162), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(164), new TermList(TermConstant.getConstant(165), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(164), new TermList(new TermNumber(24.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(164), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(166), new TermList(TermConstant.getConstant(167), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(166), new TermList(new TermNumber(1.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(166), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(168), new TermList(TermConstant.getConstant(169), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(168), new TermList(new TermNumber(7.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(168), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(170), new TermList(TermConstant.getConstant(171), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(170), new TermList(new TermNumber(31.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(170), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(172), new TermList(TermConstant.getConstant(173), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(172), new TermList(new TermNumber(20.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(172), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(174), new TermList(TermConstant.getConstant(175), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(174), new TermList(new TermNumber(18.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(174), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(176), new TermList(TermConstant.getConstant(177), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(176), new TermList(new TermNumber(36.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(176), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(178), new TermList(TermConstant.getConstant(179), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(178), new TermList(new TermNumber(19.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(178), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(180), new TermList(TermConstant.getConstant(181), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(180), new TermList(new TermNumber(27.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(180), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(182), new TermList(TermConstant.getConstant(183), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(182), new TermList(new TermNumber(30.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(182), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(184), new TermList(TermConstant.getConstant(185), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(184), new TermList(new TermNumber(4.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(184), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(186), new TermList(TermConstant.getConstant(187), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(186), new TermList(new TermNumber(6.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(186), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(188), new TermList(TermConstant.getConstant(189), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(188), new TermList(new TermNumber(29.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(188), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(190), new TermList(TermConstant.getConstant(191), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(190), new TermList(new TermNumber(33.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(190), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(192), new TermList(TermConstant.getConstant(193), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(192), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(192), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(194), new TermList(TermConstant.getConstant(195), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(194), new TermList(new TermNumber(22.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(194), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(12, 0, new TermList(TermConstant.getConstant(196), new TermList(TermConstant.getConstant(197), TermList.NIL))));
		s.add(new Predicate(19, 0, new TermList(TermConstant.getConstant(196), new TermList(new TermNumber(5.0), TermList.NIL))));
		s.add(new Predicate(17, 0, new TermList(TermConstant.getConstant(196), new TermList(new TermNumber(0.0), TermList.NIL))));
		s.add(new Predicate(15, 0, new TermList(TermConstant.getConstant(198), new TermList(new TermList(TermConstant.getConstant(118), new TermList(TermConstant.getConstant(123), new TermList(TermConstant.getConstant(125), new TermList(TermConstant.getConstant(127), new TermList(TermConstant.getConstant(130), new TermList(TermConstant.getConstant(132), new TermList(TermConstant.getConstant(134), new TermList(TermConstant.getConstant(136), new TermList(TermConstant.getConstant(138), new TermList(TermConstant.getConstant(140), new TermList(TermConstant.getConstant(142), new TermList(TermConstant.getConstant(144), new TermList(TermConstant.getConstant(146), new TermList(TermConstant.getConstant(148), new TermList(TermConstant.getConstant(150), new TermList(TermConstant.getConstant(152), new TermList(TermConstant.getConstant(154), new TermList(TermConstant.getConstant(156), new TermList(TermConstant.getConstant(158), new TermList(TermConstant.getConstant(160), new TermList(TermConstant.getConstant(162), new TermList(TermConstant.getConstant(164), new TermList(TermConstant.getConstant(166), new TermList(TermConstant.getConstant(168), new TermList(TermConstant.getConstant(170), new TermList(TermConstant.getConstant(172), new TermList(TermConstant.getConstant(174), new TermList(TermConstant.getConstant(176), new TermList(TermConstant.getConstant(178), new TermList(TermConstant.getConstant(180), new TermList(TermConstant.getConstant(182), new TermList(TermConstant.getConstant(184), new TermList(TermConstant.getConstant(186), new TermList(TermConstant.getConstant(188), new TermList(TermConstant.getConstant(190), new TermList(TermConstant.getConstant(192), new TermList(TermConstant.getConstant(194), new TermList(TermConstant.getConstant(196), TermList.NIL)))))))))))))))))))))))))))))))))))))), TermList.NIL))));
		s.add(new Predicate(75, 0, new TermList(TermConstant.getConstant(198), new TermList(TermConstant.getConstant(115), TermList.NIL))));
		s.add(new Predicate(90, 0, new TermList(TermConstant.getConstant(198), new TermList(TermConstant.getConstant(89), TermList.NIL))));
		s.add(new Predicate(79, 0, new TermList(TermConstant.getConstant(198), new TermList(new TermNumber(898.0), TermList.NIL))));
		s.add(new Predicate(78, 0, new TermList(TermConstant.getConstant(198), new TermList(new TermNumber(38.0), TermList.NIL))));
		s.add(new Predicate(74, 0, new TermList(TermConstant.getConstant(198), new TermList(TermConstant.getConstant(76), TermList.NIL))));
		s.add(new Predicate(6, 0, new TermList(TermConstant.getConstant(198), new TermList(TermConstant.getConstant(200), TermList.NIL))));
		s.add(new Predicate(38, 0, new TermList(TermConstant.getConstant(203), TermList.NIL)));
		s.add(new Predicate(38, 0, new TermList(TermConstant.getConstant(201), TermList.NIL)));
	}

	public static LinkedList<Plan> getPlans()
	{
		LinkedList<Plan> returnedPlans = new LinkedList<Plan>();
		TermConstant.initialize(205);

		Domain d = new datamining();

		d.setProblemConstants(defineConstants());

		State s = new State(111, d.getAxioms());

		JSHOP2.initialize(d, s);

		TaskList tl;
		SolverThread thread;

		createState0(s);

		tl = new TaskList(1, true);
		tl.subtasks[0] = new TaskList(new TaskAtom(new Predicate(7, 0, new TermList(TermConstant.getConstant(111), new TermList(TermConstant.getConstant(113), new TermList(TermConstant.getConstant(198), new TermList(TermConstant.getConstant(201), new TermList(TermConstant.getConstant(203), TermList.NIL)))))), false, false));

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
