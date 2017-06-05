import java.awt.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import com.TextUtil.TextUtil;

public class ParserText implements Serializable {

	/**
	 * 语法分析类 使用LL(1)文法
	 */
	private static final long serialVersionUID = 4134719510493419847L;

	public RandomTest rt;
	public String NextToken;
	public Stack<Character> analysStack;

	private String[][] analyzeTable;

	public String[][] getAnalyzeTable() {
		return analyzeTable;
	}

	public void setAnalyzeTable(String[][] analyzeTable) {
		this.analyzeTable = analyzeTable;
	}

	public ArrayList<String> getGsArray() {
		return gsArray;
	}

	public void setGsArray(ArrayList<String> gsArray) {
		this.gsArray = gsArray;
	}

	public HashMap<Character, ArrayList<String>> getExpressionMap() {
		return expressionMap;
	}

	public void setExpressionMap(
			HashMap<Character, ArrayList<String>> expressionMap) {
		this.expressionMap = expressionMap;
	}

	public HashMap<Character, TreeSet<Character>> getFirstMap() {
		return firstMap;
	}

	public void setFirstMap(HashMap<Character, TreeSet<Character>> firstMap) {
		this.firstMap = firstMap;
	}

	public HashMap<Character, TreeSet<Character>> getFollowMap() {
		return followMap;
	}

	public void setFollowMap(HashMap<Character, TreeSet<Character>> followMap) {
		this.followMap = followMap;
	}

	/**
	 * Select集合
	 */
	private TreeMap<Character, HashMap<String, TreeSet<Character>>> selectMap;
	/**
	 * LL（1）文法产生集合
	 */
	private ArrayList<String> gsArray;
	/**
	 * 表达式集合
	 */
	private HashMap<Character, ArrayList<String>> expressionMap;
	/**
	 * 开始符
	 */
	private Character s;
	/**
	 * Vn非终结符集合
	 */
	private TreeSet<Character> nvSet;
	/**
	 * Vt终结符集合
	 */
	private TreeSet<Character> ntSet;
	/**
	 * First集合
	 */
	private HashMap<Character, TreeSet<Character>> firstMap;
	/**
	 * Follow集合
	 */
	private HashMap<Character, TreeSet<Character>> followMap;

	public ParserText() {
		gsArray = new ArrayList<String>();
		nvSet = new TreeSet<Character>();
		ntSet = new TreeSet<Character>();
		firstMap = new HashMap<Character, TreeSet<Character>>();
		followMap = new HashMap<Character, TreeSet<Character>>();
		expressionMap = new HashMap<Character, ArrayList<String>>();

	}

	public String getNext() {
		return NextToken = rt.getNextToken();
	}

	public void getFirst() {// 获取first集

		for (Character ch : nvSet) {// 在非终结符中迭代

			// System.out.println(ch);
			// System.out.println("-----");
			// 测试代码如上

			// String[] ruleStr = gsArray.iterator().next().split("\\->");
			for (String str : gsArray) {// 在文法集合中找开头字母是非终结符的生成式
				if (str.charAt(0) == ch) {// 对这个生成式

					// System.out.println(str);
					// System.out.println("---------"); //测试代码

					String[] str_array = str.split("\\->");// 分割生成式
					String[] str_array_right = str_array[1].split("\\|");
					TreeSet<Character> tmp = new TreeSet<Character>();
					for (int i = 0; i < str_array_right.length; i++) {
						if (ntSet.contains(str_array_right[i].charAt(0))) {
							// 如果终结符集合中含有右边第一个字符，就加入到firstmap中
							// System.out.println(str_array_right[i].charAt(0));

							Object tmp_1 = new Object();

							tmp.add(str_array_right[i].charAt(0));
							tmp_1 = tmp.clone();
							// System.out.println("-------");
							// System.out.println(tmp_1);

							firstMap.put(str_array[0].charAt(0),
									(TreeSet<Character>) tmp_1);

							// System.out.println(str_array[0].charAt(0));
							// System.out.println(firstMap);

						} else {// 如果该符号是非终结符，则调用计算first集合的方法
							// System.out.println(str_array_right[i].charAt(0));
							// System.out.println("+++++++++");

							Character ch_Tp = calculFirst(str_array_right[i]
									.charAt(0));

							TreeSet<Character> tmp_2 = new TreeSet<Character>();

							// System.out.println(ch_Tp);
							// System.out.println("++++++++++");

							tmp.add(ch_Tp);
							tmp_2 = (TreeSet<Character>) tmp.clone();
							firstMap.put(ch, tmp_2);
							// System.out.println(firstMap);
							// 测试代码

						}
					}
					tmp.clear();

				}

			}

		}
		// 对于非终结符找first集合,符号本身就是first集合的结果
		for (Character ch : ntSet) {
			TreeSet<Character> nt = new TreeSet<Character>();
			nt.add(ch);
			firstMap.put(ch, nt);
		}

	}

	public void getFollow() {// 获取follow集

		/*
		 * Follow集合是针对非终结符而言的， Follow(U)所表达的是句型中非终结符U所有可能的后随终结符号的集合，
		 * 特别地，“#”是识别符号的后随符。注意Follow集合是从开始符号S开始推导。 1.
		 * 直接收取：注意产生式右部的每一个形如“…Ua…”的组合， 把a直接收入到Follow(U)中。 因a是紧跟在U后的终结符。
		 * 2．直接收取：对形如“…UP…”(P是非终结符)的组合， 把First(P)直接收入到Follow(U)中
		 * 【在这里，如果First（P）中有空字符， 那么就要把左部（假设是S）的Follow（S）送入到Follow（U）中。
		 * 还有就是Follow集中是没有空字符的】。 3. 直接收取：若S－>…U，即以U结尾，则#∈Follow(U)
		 * 4．反复传送：对形如U－>…P的产生式（其中P是非终结符）， 应把Follow(U)中的全部内容传送到Follow(P)中。
		 */
		// Set<Character> ch = (Set<Character>) nvSet.clone();
		// followMap.keySet().addAll(ch);// 将非终结符集合加入到followmap中

		// followMap.get("S").add('#');// 文法的开始符号加入#号进入follow集合中

		// for (Character chs : nvSet) {
		// for (String str : gsArray) {
		// if (str.charAt(0) == chs) {
		// String[] str_array = str.split("\\->");
		// String[] str_array_right = str_array[1].split("\\|");
		//
		// System.out.println(chs);
		// System.out.println(str);
		// for (int i = 0; i < str_array_right.length; i++) {
		// for (int j = 0; j < str_array_right[i].length() - 1; j++) {
		// // 非终结符扫描数组
		// // if (str_array_right[i].charAt(j) == chs) {
		// if (ntSet
		// .contains(str_array_right[i].charAt(j + 1))) {
		// TreeSet<Character> tmp = new TreeSet<Character>();
		// if (followMap.get(str_array_right[i]
		// .charAt(j + 1)) != null) {
		// tmp = (TreeSet<Character>) followMap.get(
		// str_array_right[i].charAt(j + 1))
		// .clone();
		// } else {
		// tmp.add(str_array_right[i].charAt(j + 1));
		// followMap.put(str_array_right[i].charAt(j), tmp);
		//
		// System.out.println(followMap);
		// }
		//
		// } else if (nvSet.contains(str_array_right[i]
		// .charAt(j + 1))) {
		// Character ch_tp = calculFirst(str_array_right[i]
		// .charAt(j + 1));
		// TreeSet<Character> tmp = new TreeSet<Character>();
		// if (followMap.get(str_array_right[i]
		// .charAt(j + 1)) != null) {
		// tmp = (TreeSet<Character>) followMap.get(
		// str_array_right[i].charAt(j + 1))
		// .clone();
		// } else {
		// tmp.add(ch_tp);
		// followMap.put(str_array_right[i].charAt(j), tmp);
		//
		// System.out.println(followMap);
		// }
		//
		// } else if (str_array_right[i].charAt(j + 1) == '\0') {
		// TreeSet<Character> tmp = new TreeSet<Character>();
		// if (followMap.get(str_array_right[i]
		// .charAt(j + 1)) != null) {
		// tmp = (TreeSet<Character>) followMap.get(
		// str_array_right[i].charAt(j + 1))
		// .clone();
		// }
		//
		// tmp.add('#');
		// tmp.addAll(followMap.get(str_array[0]));
		// followMap.put(str_array_right[i].charAt(j), tmp);
		//
		// System.out.println(followMap);
		// }
		// }
		// }
		//
		// }
		//
		// }
		// }

		for (Character tmpkey : nvSet) {
			TreeSet<Character> tmpset = new TreeSet<Character>();
			followMap.put(tmpkey, tmpset);
		}
		Iterator<Character> iter = nvSet.descendingIterator();

		while (iter.hasNext()) {

			Character charitem = iter.next();// 非终结符
			System.out.println("charitem " + charitem);
			Set<Character> keyset = expressionMap.keySet();// 表达式的非终结符集合

			for (Character keycharitem : keyset) {
				ArrayList<String> charitemarr = expressionMap.get(keycharitem);// 表达式的右侧生成式的集合
				for (String itemcharstr : charitemarr) {
					System.out.println(keycharitem + "->" + itemcharstr);
					TreeSet<Character> itemset = followMap.get(charitem);// 已有的follow集合（该非终结符）
					calculFollow(charitem, charitem, keycharitem, itemcharstr,
							itemset);
				}
			}
		}

	}

	public void getSelect() {// 获取select集

	}

	public void calculFollow(Character putcharitem, Character charitem,
			Character keycharitem, String itemcharstr,
			TreeSet<Character> itemset) {// 计算follow集

		if (putcharitem.equals(s)) {
			itemset.add('#');
			System.out.println("s: " + charitem + " ={#}");
			followMap.put(putcharitem, itemset);
		}
		if (TextUtil.containsAb(ntSet, itemcharstr, charitem)) {
			Character last = TextUtil.getA_lastChar(itemcharstr, charitem);
			itemset.add(last);
			followMap.put(keycharitem, itemset);
		}
		if (TextUtil.containsAB(nvSet, itemcharstr, charitem)) {

		}
	}

	public Character calculFirst(char UnfinishChar) {// 计算first集
		TreeSet<Character> tmp = new TreeSet<Character>();
		// ArrayList<Character> a=new ArrayList<Character>();
		char ch_tmp = UnfinishChar;
		for (String str : gsArray) {// 迭代文法集合
			if (str.charAt(0) == ch_tmp) {
				String[] strArray = str.split("\\->");
				if (ntSet.contains(strArray[1].charAt(0))
						|| str.charAt(0) == 'ε') {
					// 如果终结符集合中含有该字符，则返回字符

					return strArray[1].charAt(0);

				} else {// 如果依旧为非终结符，则继续调用此方法计算
					return calculFirst(strArray[1].charAt(0));
				}

			}
		}
		// 如果文法集合为空，则返回空值
		return null;
	}

	public void calculSelect() {// 计算select集

	}

	public void getAnalyTable() {// 获取分析表

		Object[] nvarray = nvSet.toArray();
		Object[] ntarray = ntSet.toArray();

		analyzeTable = new String[nvarray.length + 1][ntarray.length + 1];

		// 初始化首行分析表
		System.out.println("Nt/Nv" + "\t\t");
		analyzeTable[0][0] = "Nt/Nv";

		for (int i = 0; i < ntarray.length; i++) {
			if (ntarray[i].equals("ε")) {
				ntarray[i] = "#";
			}
			System.out.println(ntarray + "\t\t");
			analyzeTable[0][i + 1] = ntarray[i] + "";
		}
		System.out.println("");

		for (int i = 0; i < nvarray.length; i++) {
			System.out.println(nvarray[i] + "\t\t");
			analyzeTable[i + 1][0] = nvarray[i] + "";

			for (int j = 0; j < ntarray.length; j++) {
				String findUseExp = TextUtil.findUseExp(selectMap,
						Character.valueOf((Character) nvarray[i]),
						Character.valueOf((Character) ntarray[i]));

				if (findUseExp == null) {
					System.out.println("\t\t");
					analyzeTable[i + 1][j + 1] = "";
				} else {
					System.out.println(nvarray[i] + "->" + findUseExp + "\t\t");
					analyzeTable[i + 1][j + 1] = nvarray[i] + "->" + findUseExp;

				}
			}
			System.out.println();
		}

	}

	public void getNvNt() {// 获取非终结符和终结符
		/*
		 * 如何表示文法？ 比如S->aA 解决方法：split方法分割 将-》左边放入集合中，右边也同样放入集合中
		 */

		for (String str : gsArray) {
			String[] strAy = str.split("\\->");
			String[] strAy_right = strAy[1].split("\\|");
			nvSet.add(strAy[0].charAt(0));
			// 判断生成式右边字符是否是终结符
			for (int i = 0; i < strAy_right.length; i++) {
				for (int j = 0; j < strAy_right[i].length(); j++) {
					if (Character.isLowerCase(strAy_right[i].charAt(j))) {
						ntSet.add(strAy_right[i].charAt(j));
					} else {
						if (!nvSet.contains(strAy_right[i].charAt(j))) {// 判断非终结符是否在nvset中
							nvSet.add(strAy_right[i].charAt(j));
						}
					}

				}
			}
			// 至此生成终结符集合和非终结符集合
		}
		// // NtList=new ArrayList<Character>();
		// // NtList=new ArrayList<Character>();
		// String[] RuleArray = rule.split("\\->");
		// // System.out.println(RuleArray[1]);
		// // System.out.println(RuleArray[1].charAt(0));
		//
		// char tmp = RuleArray[0].charAt(0);
		//
		// NvList.add(tmp);
		//
		// for (int i = 0; i < RuleArray.length; i++) {
		// NtList.add(RuleArray[1].charAt(i));
		//
		// }
		// // System.out.println(NtList);

	}

	public void initExperssionMap() {// 初始化表达式map

		for (String str : gsArray) {
			String[] strs = str.split("\\->");
			Character chLeft = strs[0].charAt(0);

			if (!expressionMap.containsKey(chLeft)) {
				ArrayList<String> tmp = new ArrayList<String>();
				tmp.clear();
				tmp.add(strs[1]);
				expressionMap.put(chLeft, tmp);
			} else {
				ArrayList<String> tmp = expressionMap.get(chLeft);
				tmp.clear();
				tmp.add(strs[1]);
				expressionMap.put(chLeft, tmp);
			}

		}
	}

	public static void main(String[] args) {
		ParserText pt = new ParserText();
		ArrayList<String> gsArray = new ArrayList<String>();
		gsArray.add("S->aA");
		gsArray.add("A->Ba|b");
		gsArray.add("C->A");
		gsArray.add("B->c");

		pt.setGsArray(gsArray);
		pt.getNvNt();
		pt.getFollow();
		HashMap<Character, TreeSet<Character>> followMap = pt.getFollowMap();
		System.out.println(followMap);
		System.out.println(pt.ntSet);
	}

}
