/* Generated By:JavaCC: Do not edit this line. ARQParserConstants.java */
package org.apache.jena.sparql.lang.arq ;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface ARQParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int SINGLE_LINE_COMMENT = 6;
  /** RegularExpression Id. */
  int WS = 7;
  /** RegularExpression Id. */
  int WSC = 8;
  /** RegularExpression Id. */
  int BOM = 9;
  /** RegularExpression Id. */
  int IRIref = 10;
  /** RegularExpression Id. */
  int PNAME_NS = 11;
  /** RegularExpression Id. */
  int PNAME_LN = 12;
  /** RegularExpression Id. */
  int BLANK_NODE_LABEL = 13;
  /** RegularExpression Id. */
  int VAR1 = 14;
  /** RegularExpression Id. */
  int VAR2 = 15;
  /** RegularExpression Id. */
  int LANGTAG = 16;
  /** RegularExpression Id. */
  int A2Z = 17;
  /** RegularExpression Id. */
  int A2ZN = 18;
  /** RegularExpression Id. */
  int KW_A = 19;
  /** RegularExpression Id. */
  int BASE = 20;
  /** RegularExpression Id. */
  int PREFIX = 21;
  /** RegularExpression Id. */
  int PREDICT = 22;
  /** RegularExpression Id. */
  int MODEL = 23;
  /** RegularExpression Id. */
  int PREDICTION = 24;
  /** RegularExpression Id. */
  int TARGET = 25;
  /** RegularExpression Id. */
  int FEATURE = 26;
  /** RegularExpression Id. */
  int CONDITION = 27;
  /** RegularExpression Id. */
  int USE = 28;
  /** RegularExpression Id. */
  int SAVE = 29;
  /** RegularExpression Id. */
  int LEARNING = 30;
  /** RegularExpression Id. */
  int ALGORITHM = 31;
  /** RegularExpression Id. */
  int SELECT = 32;
  /** RegularExpression Id. */
  int DISTINCT = 33;
  /** RegularExpression Id. */
  int REDUCED = 34;
  /** RegularExpression Id. */
  int JSON = 35;
  /** RegularExpression Id. */
  int DESCRIBE = 36;
  /** RegularExpression Id. */
  int CONSTRUCT = 37;
  /** RegularExpression Id. */
  int ASK = 38;
  /** RegularExpression Id. */
  int LIMIT = 39;
  /** RegularExpression Id. */
  int OFFSET = 40;
  /** RegularExpression Id. */
  int ORDER = 41;
  /** RegularExpression Id. */
  int BY = 42;
  /** RegularExpression Id. */
  int VALUES = 43;
  /** RegularExpression Id. */
  int UNDEF = 44;
  /** RegularExpression Id. */
  int ASC = 45;
  /** RegularExpression Id. */
  int DESC = 46;
  /** RegularExpression Id. */
  int NAMED = 47;
  /** RegularExpression Id. */
  int FROM = 48;
  /** RegularExpression Id. */
  int WHERE = 49;
  /** RegularExpression Id. */
  int AND = 50;
  /** RegularExpression Id. */
  int GRAPH = 51;
  /** RegularExpression Id. */
  int OPTIONAL = 52;
  /** RegularExpression Id. */
  int UNION = 53;
  /** RegularExpression Id. */
  int MINUS_P = 54;
  /** RegularExpression Id. */
  int BIND = 55;
  /** RegularExpression Id. */
  int SERVICE = 56;
  /** RegularExpression Id. */
  int LET = 57;
  /** RegularExpression Id. */
  int EXISTS = 58;
  /** RegularExpression Id. */
  int NOT = 59;
  /** RegularExpression Id. */
  int AS = 60;
  /** RegularExpression Id. */
  int GROUP = 61;
  /** RegularExpression Id. */
  int HAVING = 62;
  /** RegularExpression Id. */
  int SEPARATOR = 63;
  /** RegularExpression Id. */
  int AGG = 64;
  /** RegularExpression Id. */
  int COUNT = 65;
  /** RegularExpression Id. */
  int MIN = 66;
  /** RegularExpression Id. */
  int MAX = 67;
  /** RegularExpression Id. */
  int SUM = 68;
  /** RegularExpression Id. */
  int AVG = 69;
  /** RegularExpression Id. */
  int STDEV = 70;
  /** RegularExpression Id. */
  int STDEV_SAMP = 71;
  /** RegularExpression Id. */
  int STDEV_POP = 72;
  /** RegularExpression Id. */
  int VARIANCE = 73;
  /** RegularExpression Id. */
  int VAR_SAMP = 74;
  /** RegularExpression Id. */
  int VAR_POP = 75;
  /** RegularExpression Id. */
  int SAMPLE = 76;
  /** RegularExpression Id. */
  int GROUP_CONCAT = 77;
  /** RegularExpression Id. */
  int FILTER = 78;
  /** RegularExpression Id. */
  int BOUND = 79;
  /** RegularExpression Id. */
  int COALESCE = 80;
  /** RegularExpression Id. */
  int IN = 81;
  /** RegularExpression Id. */
  int IF = 82;
  /** RegularExpression Id. */
  int BNODE = 83;
  /** RegularExpression Id. */
  int IRI = 84;
  /** RegularExpression Id. */
  int URI = 85;
  /** RegularExpression Id. */
  int CAST = 86;
  /** RegularExpression Id. */
  int CALL = 87;
  /** RegularExpression Id. */
  int MULTI = 88;
  /** RegularExpression Id. */
  int SHORTEST = 89;
  /** RegularExpression Id. */
  int STR = 90;
  /** RegularExpression Id. */
  int STRLANG = 91;
  /** RegularExpression Id. */
  int STRDT = 92;
  /** RegularExpression Id. */
  int DTYPE = 93;
  /** RegularExpression Id. */
  int LANG = 94;
  /** RegularExpression Id. */
  int LANGMATCHES = 95;
  /** RegularExpression Id. */
  int IS_URI = 96;
  /** RegularExpression Id. */
  int IS_IRI = 97;
  /** RegularExpression Id. */
  int IS_BLANK = 98;
  /** RegularExpression Id. */
  int IS_LITERAL = 99;
  /** RegularExpression Id. */
  int IS_NUMERIC = 100;
  /** RegularExpression Id. */
  int REGEX = 101;
  /** RegularExpression Id. */
  int SAME_TERM = 102;
  /** RegularExpression Id. */
  int RAND = 103;
  /** RegularExpression Id. */
  int ABS = 104;
  /** RegularExpression Id. */
  int CEIL = 105;
  /** RegularExpression Id. */
  int FLOOR = 106;
  /** RegularExpression Id. */
  int ROUND = 107;
  /** RegularExpression Id. */
  int CONCAT = 108;
  /** RegularExpression Id. */
  int SUBSTR = 109;
  /** RegularExpression Id. */
  int STRLEN = 110;
  /** RegularExpression Id. */
  int REPLACE = 111;
  /** RegularExpression Id. */
  int UCASE = 112;
  /** RegularExpression Id. */
  int LCASE = 113;
  /** RegularExpression Id. */
  int ENCODE_FOR_URI = 114;
  /** RegularExpression Id. */
  int CONTAINS = 115;
  /** RegularExpression Id. */
  int STRSTARTS = 116;
  /** RegularExpression Id. */
  int STRENDS = 117;
  /** RegularExpression Id. */
  int STRBEFORE = 118;
  /** RegularExpression Id. */
  int STRAFTER = 119;
  /** RegularExpression Id. */
  int YEAR = 120;
  /** RegularExpression Id. */
  int MONTH = 121;
  /** RegularExpression Id. */
  int DAY = 122;
  /** RegularExpression Id. */
  int HOURS = 123;
  /** RegularExpression Id. */
  int MINUTES = 124;
  /** RegularExpression Id. */
  int SECONDS = 125;
  /** RegularExpression Id. */
  int TIMEZONE = 126;
  /** RegularExpression Id. */
  int TZ = 127;
  /** RegularExpression Id. */
  int NOW = 128;
  /** RegularExpression Id. */
  int UUID = 129;
  /** RegularExpression Id. */
  int STRUUID = 130;
  /** RegularExpression Id. */
  int VERSION = 131;
  /** RegularExpression Id. */
  int MD5 = 132;
  /** RegularExpression Id. */
  int SHA1 = 133;
  /** RegularExpression Id. */
  int SHA224 = 134;
  /** RegularExpression Id. */
  int SHA256 = 135;
  /** RegularExpression Id. */
  int SHA384 = 136;
  /** RegularExpression Id. */
  int SHA512 = 137;
  /** RegularExpression Id. */
  int TRUE = 138;
  /** RegularExpression Id. */
  int FALSE = 139;
  /** RegularExpression Id. */
  int DATA = 140;
  /** RegularExpression Id. */
  int INSERT = 141;
  /** RegularExpression Id. */
  int DELETE = 142;
  /** RegularExpression Id. */
  int INSERT_DATA = 143;
  /** RegularExpression Id. */
  int DELETE_DATA = 144;
  /** RegularExpression Id. */
  int DELETE_WHERE = 145;
  /** RegularExpression Id. */
  int LOAD = 146;
  /** RegularExpression Id. */
  int CLEAR = 147;
  /** RegularExpression Id. */
  int CREATE = 148;
  /** RegularExpression Id. */
  int ADD = 149;
  /** RegularExpression Id. */
  int MOVE = 150;
  /** RegularExpression Id. */
  int COPY = 151;
  /** RegularExpression Id. */
  int META = 152;
  /** RegularExpression Id. */
  int SILENT = 153;
  /** RegularExpression Id. */
  int DROP = 154;
  /** RegularExpression Id. */
  int INTO = 155;
  /** RegularExpression Id. */
  int TO = 156;
  /** RegularExpression Id. */
  int DFT = 157;
  /** RegularExpression Id. */
  int ALL = 158;
  /** RegularExpression Id. */
  int WITH = 159;
  /** RegularExpression Id. */
  int USING = 160;
  /** RegularExpression Id. */
  int DIGITS = 161;
  /** RegularExpression Id. */
  int INTEGER = 162;
  /** RegularExpression Id. */
  int DECIMAL = 163;
  /** RegularExpression Id. */
  int DOUBLE = 164;
  /** RegularExpression Id. */
  int INTEGER_POSITIVE = 165;
  /** RegularExpression Id. */
  int DECIMAL_POSITIVE = 166;
  /** RegularExpression Id. */
  int DOUBLE_POSITIVE = 167;
  /** RegularExpression Id. */
  int INTEGER_NEGATIVE = 168;
  /** RegularExpression Id. */
  int DECIMAL_NEGATIVE = 169;
  /** RegularExpression Id. */
  int DOUBLE_NEGATIVE = 170;
  /** RegularExpression Id. */
  int EXPONENT = 171;
  /** RegularExpression Id. */
  int QUOTE_3D = 172;
  /** RegularExpression Id. */
  int QUOTE_3S = 173;
  /** RegularExpression Id. */
  int ECHAR = 174;
  /** RegularExpression Id. */
  int STRING_LITERAL1 = 175;
  /** RegularExpression Id. */
  int STRING_LITERAL2 = 176;
  /** RegularExpression Id. */
  int STRING_LITERAL_LONG1 = 177;
  /** RegularExpression Id. */
  int STRING_LITERAL_LONG2 = 178;
  /** RegularExpression Id. */
  int LPAREN = 179;
  /** RegularExpression Id. */
  int RPAREN = 180;
  /** RegularExpression Id. */
  int NIL = 181;
  /** RegularExpression Id. */
  int LBRACE = 182;
  /** RegularExpression Id. */
  int RBRACE = 183;
  /** RegularExpression Id. */
  int LBRACKET = 184;
  /** RegularExpression Id. */
  int RBRACKET = 185;
  /** RegularExpression Id. */
  int ANON = 186;
  /** RegularExpression Id. */
  int SEMICOLON = 187;
  /** RegularExpression Id. */
  int COMMA = 188;
  /** RegularExpression Id. */
  int DOT = 189;
  /** RegularExpression Id. */
  int EQ = 190;
  /** RegularExpression Id. */
  int NE = 191;
  /** RegularExpression Id. */
  int GT = 192;
  /** RegularExpression Id. */
  int LT = 193;
  /** RegularExpression Id. */
  int LE = 194;
  /** RegularExpression Id. */
  int GE = 195;
  /** RegularExpression Id. */
  int BANG = 196;
  /** RegularExpression Id. */
  int TILDE = 197;
  /** RegularExpression Id. */
  int COLON = 198;
  /** RegularExpression Id. */
  int SC_OR = 199;
  /** RegularExpression Id. */
  int SC_AND = 200;
  /** RegularExpression Id. */
  int PLUS = 201;
  /** RegularExpression Id. */
  int MINUS = 202;
  /** RegularExpression Id. */
  int STAR = 203;
  /** RegularExpression Id. */
  int SLASH = 204;
  /** RegularExpression Id. */
  int DATATYPE = 205;
  /** RegularExpression Id. */
  int AT = 206;
  /** RegularExpression Id. */
  int ASSIGN = 207;
  /** RegularExpression Id. */
  int VBAR = 208;
  /** RegularExpression Id. */
  int CARAT = 209;
  /** RegularExpression Id. */
  int FPATH = 210;
  /** RegularExpression Id. */
  int RPATH = 211;
  /** RegularExpression Id. */
  int QMARK = 212;
  /** RegularExpression Id. */
  int PN_CHARS_BASE = 213;
  /** RegularExpression Id. */
  int PN_CHARS_U = 214;
  /** RegularExpression Id. */
  int PN_CHARS = 215;
  /** RegularExpression Id. */
  int PN_PREFIX = 216;
  /** RegularExpression Id. */
  int PN_LOCAL = 217;
  /** RegularExpression Id. */
  int VARNAME = 218;
  /** RegularExpression Id. */
  int PN_LOCAL_ESC = 219;
  /** RegularExpression Id. */
  int PLX = 220;
  /** RegularExpression Id. */
  int HEX = 221;
  /** RegularExpression Id. */
  int PERCENT = 222;
  /** RegularExpression Id. */
  int UNKNOWN = 223;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "<SINGLE_LINE_COMMENT>",
    "<WS>",
    "<WSC>",
    "\"\\ufeff\"",
    "<IRIref>",
    "<PNAME_NS>",
    "<PNAME_LN>",
    "<BLANK_NODE_LABEL>",
    "<VAR1>",
    "<VAR2>",
    "<LANGTAG>",
    "<A2Z>",
    "<A2ZN>",
    "\"a\"",
    "\"base\"",
    "\"prefix\"",
    "\"predict\"",
    "\"model\"",
    "\"prediction\"",
    "\"target\"",
    "\"feature\"",
    "\"condition\"",
    "\"use\"",
    "\"save\"",
    "\"learning\"",
    "\"algorithm\"",
    "\"select\"",
    "\"distinct\"",
    "\"reduced\"",
    "\"json\"",
    "\"describe\"",
    "\"construct\"",
    "\"ask\"",
    "\"limit\"",
    "\"offset\"",
    "\"order\"",
    "\"by\"",
    "\"values\"",
    "\"undef\"",
    "\"asc\"",
    "\"desc\"",
    "\"named\"",
    "\"from\"",
    "\"where\"",
    "\"and\"",
    "\"graph\"",
    "\"optional\"",
    "\"union\"",
    "\"minus\"",
    "\"bind\"",
    "\"service\"",
    "\"let\"",
    "\"exists\"",
    "\"not\"",
    "\"as\"",
    "\"group\"",
    "\"having\"",
    "\"separator\"",
    "\"agg\"",
    "\"count\"",
    "\"min\"",
    "\"max\"",
    "\"sum\"",
    "\"avg\"",
    "\"stdev\"",
    "\"stdev_samp\"",
    "\"stdev_pop\"",
    "\"variance\"",
    "\"var_samp\"",
    "\"var_pop\"",
    "\"sample\"",
    "\"group_concat\"",
    "\"filter\"",
    "\"bound\"",
    "\"coalesce\"",
    "\"in\"",
    "\"if\"",
    "\"bnode\"",
    "\"iri\"",
    "\"uri\"",
    "\"cast\"",
    "\"call\"",
    "\"multi\"",
    "\"shortest\"",
    "\"str\"",
    "\"strlang\"",
    "\"strdt\"",
    "\"datatype\"",
    "\"lang\"",
    "\"langmatches\"",
    "\"isURI\"",
    "\"isIRI\"",
    "\"isBlank\"",
    "\"isLiteral\"",
    "\"isNumeric\"",
    "\"regex\"",
    "\"sameTerm\"",
    "\"RAND\"",
    "\"ABS\"",
    "\"CEIL\"",
    "\"FLOOR\"",
    "\"ROUND\"",
    "\"CONCAT\"",
    "\"SUBSTR\"",
    "\"STRLEN\"",
    "\"REPLACE\"",
    "\"UCASE\"",
    "\"LCASE\"",
    "\"ENCODE_FOR_URI\"",
    "\"CONTAINS\"",
    "\"STRSTARTS\"",
    "\"STRENDS\"",
    "\"STRBEFORE\"",
    "\"STRAFTER\"",
    "\"YEAR\"",
    "\"MONTH\"",
    "\"DAY\"",
    "\"HOURS\"",
    "\"MINUTES\"",
    "\"SECONDS\"",
    "\"TIMEZONE\"",
    "\"TZ\"",
    "\"NOW\"",
    "\"UUID\"",
    "\"STRUUID\"",
    "\"VERSION\"",
    "\"MD5\"",
    "\"SHA1\"",
    "\"SHA224\"",
    "\"SHA256\"",
    "\"SHA384\"",
    "\"SHA512\"",
    "\"true\"",
    "\"false\"",
    "\"data\"",
    "\"insert\"",
    "\"delete\"",
    "<INSERT_DATA>",
    "<DELETE_DATA>",
    "<DELETE_WHERE>",
    "\"load\"",
    "\"clear\"",
    "\"create\"",
    "\"add\"",
    "\"move\"",
    "\"copy\"",
    "\"meta\"",
    "\"silent\"",
    "\"drop\"",
    "\"into\"",
    "\"to\"",
    "\"default\"",
    "\"all\"",
    "\"with\"",
    "\"using\"",
    "<DIGITS>",
    "<INTEGER>",
    "<DECIMAL>",
    "<DOUBLE>",
    "<INTEGER_POSITIVE>",
    "<DECIMAL_POSITIVE>",
    "<DOUBLE_POSITIVE>",
    "<INTEGER_NEGATIVE>",
    "<DECIMAL_NEGATIVE>",
    "<DOUBLE_NEGATIVE>",
    "<EXPONENT>",
    "\"\\\"\\\"\\\"\"",
    "\"\\\'\\\'\\\'\"",
    "<ECHAR>",
    "<STRING_LITERAL1>",
    "<STRING_LITERAL2>",
    "<STRING_LITERAL_LONG1>",
    "<STRING_LITERAL_LONG2>",
    "\"(\"",
    "\")\"",
    "<NIL>",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "<ANON>",
    "\";\"",
    "\",\"",
    "\".\"",
    "\"=\"",
    "\"!=\"",
    "\">\"",
    "\"<\"",
    "\"<=\"",
    "\">=\"",
    "\"!\"",
    "\"~\"",
    "\":\"",
    "\"||\"",
    "\"&&\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"^^\"",
    "\"@\"",
    "\":=\"",
    "\"|\"",
    "\"^\"",
    "\"->\"",
    "\"<-\"",
    "\"?\"",
    "<PN_CHARS_BASE>",
    "<PN_CHARS_U>",
    "<PN_CHARS>",
    "<PN_PREFIX>",
    "<PN_LOCAL>",
    "<VARNAME>",
    "<PN_LOCAL_ESC>",
    "<PLX>",
    "<HEX>",
    "<PERCENT>",
    "<UNKNOWN>",
  };

}
