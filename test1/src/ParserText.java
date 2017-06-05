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
	 * �﷨������ ʹ��LL(1)�ķ�
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
	 * Select����
	 */
	private TreeMap<Character, HashMap<String, TreeSet<Character>>> selectMap;
	/**
	 * LL��1���ķ���������
	 */
	private ArrayList<String> gsArray;
	/**
	 * ���ʽ����
	 */
	private HashMap<Character, ArrayList<String>> expressionMap;
	/**
	 * ��ʼ��
	 */
	private Character s;
	/**
	 * Vn���ս������
	 */
	private TreeSet<Character> nvSet;
	/**
	 * Vt�ս������
	 */
	private TreeSet<Character> ntSet;
	/**
	 * First����
	 */
	private HashMap<Character, TreeSet<Character>> firstMap;
	/**
	 * Follow����
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

	public void getFirst() {// ��ȡfirst��

		for (Character ch : nvSet) {// �ڷ��ս���е���

			// System.out.println(ch);
			// System.out.println("-----");
			// ���Դ�������

			// String[] ruleStr = gsArray.iterator().next().split("\\->");
			for (String str : gsArray) {// ���ķ��������ҿ�ͷ��ĸ�Ƿ��ս��������ʽ
				if (str.charAt(0) == ch) {// ���������ʽ

					// System.out.println(str);
					// System.out.println("---------"); //���Դ���

					String[] str_array = str.split("\\->");// �ָ�����ʽ
					String[] str_array_right = str_array[1].split("\\|");
					TreeSet<Character> tmp = new TreeSet<Character>();
					for (int i = 0; i < str_array_right.length; i++) {
						if (ntSet.contains(str_array_right[i].charAt(0))) {
							// ����ս�������к����ұߵ�һ���ַ����ͼ��뵽firstmap��
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

						} else {// ����÷����Ƿ��ս��������ü���first���ϵķ���
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
							// ���Դ���

						}
					}
					tmp.clear();

				}

			}

		}
		// ���ڷ��ս����first����,���ű������first���ϵĽ��
		for (Character ch : ntSet) {
			TreeSet<Character> nt = new TreeSet<Character>();
			nt.add(ch);
			firstMap.put(ch, nt);
		}

	}

	public void getFollow() {// ��ȡfollow��

		/*
		 * Follow��������Է��ս�����Եģ� Follow(U)�������Ǿ����з��ս��U���п��ܵĺ����ս���ŵļ��ϣ�
		 * �ر�أ���#����ʶ����ŵĺ������ע��Follow�����Ǵӿ�ʼ����S��ʼ�Ƶ��� 1.
		 * ֱ����ȡ��ע�����ʽ�Ҳ���ÿһ�����硰��Ua��������ϣ� ��aֱ�����뵽Follow(U)�С� ��a�ǽ�����U����ս����
		 * 2��ֱ����ȡ�������硰��UP����(P�Ƿ��ս��)����ϣ� ��First(P)ֱ�����뵽Follow(U)��
		 * ����������First��P�����п��ַ��� ��ô��Ҫ���󲿣�������S����Follow��S�����뵽Follow��U���С�
		 * ���о���Follow������û�п��ַ��ġ��� 3. ֱ����ȡ����S��>��U������U��β����#��Follow(U)
		 * 4���������ͣ�������U��>��P�Ĳ���ʽ������P�Ƿ��ս������ Ӧ��Follow(U)�е�ȫ�����ݴ��͵�Follow(P)�С�
		 */
		// Set<Character> ch = (Set<Character>) nvSet.clone();
		// followMap.keySet().addAll(ch);// �����ս�����ϼ��뵽followmap��

		// followMap.get("S").add('#');// �ķ��Ŀ�ʼ���ż���#�Ž���follow������

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
		// // ���ս��ɨ������
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

			Character charitem = iter.next();// ���ս��
			System.out.println("charitem " + charitem);
			Set<Character> keyset = expressionMap.keySet();// ���ʽ�ķ��ս������

			for (Character keycharitem : keyset) {
				ArrayList<String> charitemarr = expressionMap.get(keycharitem);// ���ʽ���Ҳ�����ʽ�ļ���
				for (String itemcharstr : charitemarr) {
					System.out.println(keycharitem + "->" + itemcharstr);
					TreeSet<Character> itemset = followMap.get(charitem);// ���е�follow���ϣ��÷��ս����
					calculFollow(charitem, charitem, keycharitem, itemcharstr,
							itemset);
				}
			}
		}

	}

	public void getSelect() {// ��ȡselect��

	}

	public void calculFollow(Character putcharitem, Character charitem,
			Character keycharitem, String itemcharstr,
			TreeSet<Character> itemset) {// ����follow��

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

	public Character calculFirst(char UnfinishChar) {// ����first��
		TreeSet<Character> tmp = new TreeSet<Character>();
		// ArrayList<Character> a=new ArrayList<Character>();
		char ch_tmp = UnfinishChar;
		for (String str : gsArray) {// �����ķ�����
			if (str.charAt(0) == ch_tmp) {
				String[] strArray = str.split("\\->");
				if (ntSet.contains(strArray[1].charAt(0))
						|| str.charAt(0) == '��') {
					// ����ս�������к��и��ַ����򷵻��ַ�

					return strArray[1].charAt(0);

				} else {// �������Ϊ���ս������������ô˷�������
					return calculFirst(strArray[1].charAt(0));
				}

			}
		}
		// ����ķ�����Ϊ�գ��򷵻ؿ�ֵ
		return null;
	}

	public void calculSelect() {// ����select��

	}

	public void getAnalyTable() {// ��ȡ������

		Object[] nvarray = nvSet.toArray();
		Object[] ntarray = ntSet.toArray();

		analyzeTable = new String[nvarray.length + 1][ntarray.length + 1];

		// ��ʼ�����з�����
		System.out.println("Nt/Nv" + "\t\t");
		analyzeTable[0][0] = "Nt/Nv";

		for (int i = 0; i < ntarray.length; i++) {
			if (ntarray[i].equals("��")) {
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

	public void getNvNt() {// ��ȡ���ս�����ս��
		/*
		 * ��α�ʾ�ķ��� ����S->aA ���������split�����ָ� ��-����߷��뼯���У��ұ�Ҳͬ�����뼯����
		 */

		for (String str : gsArray) {
			String[] strAy = str.split("\\->");
			String[] strAy_right = strAy[1].split("\\|");
			nvSet.add(strAy[0].charAt(0));
			// �ж�����ʽ�ұ��ַ��Ƿ����ս��
			for (int i = 0; i < strAy_right.length; i++) {
				for (int j = 0; j < strAy_right[i].length(); j++) {
					if (Character.isLowerCase(strAy_right[i].charAt(j))) {
						ntSet.add(strAy_right[i].charAt(j));
					} else {
						if (!nvSet.contains(strAy_right[i].charAt(j))) {// �жϷ��ս���Ƿ���nvset��
							nvSet.add(strAy_right[i].charAt(j));
						}
					}

				}
			}
			// ���������ս�����Ϻͷ��ս������
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

	public void initExperssionMap() {// ��ʼ�����ʽmap

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
