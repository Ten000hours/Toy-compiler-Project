package com.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class TextUtil {

	public static boolean containsAB(TreeSet<Character> nvset,
			String itemCharStr, Character c) {
		String aStr = c.toString();
		if (itemCharStr.contains(aStr)) {
			int aIndex = itemCharStr.indexOf(aStr);
			String findStr;
			try {
				findStr = itemCharStr.substring(aIndex + 1, aIndex + 2);
			} catch (Exception e) {
				return false;
			}
			if (nvset.contains(findStr.charAt(0))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	/*
	 * ����bAb��b=�� 
	 */
	public static boolean containbA_bIsNull(TreeSet<Character> nvset,
			String itemCharStr, Character c,
			HashMap<Character, ArrayList<String>> experssionMap) {

		String aStr = c.toString();
		if (containsAB(nvset, itemCharStr, c)) {
			Character aLast=getA_lastChar(itemCharStr, c);
			System.out.println("++++++++++"+experssionMap.toString());
			ArrayList<String> list=experssionMap.get(aLast);
			if (list.contains("��")) {
				System.out.println("�ü��ϰ������ַ�");
				return true;
			}
		}
		return false;
		

	}

	public static boolean containsbA(TreeSet<Character> ntset,
			String itemCharStr, Character a,
			HashMap<Character, ArrayList<String>> expressionMap) {
		String aStr = a.toString();
		String lastString = itemCharStr.substring(itemCharStr.length() - 1);
		if (lastString.equals(aStr)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean containsAb(TreeSet<Character> ntset,
			String itemCharStr, Character c) {

		String aStr = c.toString();

		if (itemCharStr.contains(aStr)) {
			String findSTR = null;
			int aIndex = itemCharStr.indexOf(aStr);
			try {
				findSTR = itemCharStr.substring(aIndex + 1, aIndex + 2);
			} catch (Exception e) {
				return false;
			}
			if (ntset.contains(findSTR.charAt(0))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static Character getA_lastChar(String itemCharStr, Character c) {
		String atr = c.toString();
		if (itemCharStr.contains(atr)) {
			int index = itemCharStr.indexOf(atr);
			String sub;
			try {
				sub = itemCharStr.substring(index + 1, index + 2);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return null;
			}
			return sub.charAt(0);
		}
		return null;

	}

	/*
	 * �Ƿ����Կշ��ſ�ʼ��
	 */
	public static boolean isEmptyStart(String selectExp) {

		if (selectExp.charAt(0) == '��') {
			return true;
		}
		return false;
	}

	/*
	 * �Ƿ������ս����ʼ
	 */
	public static boolean isNtStart(TreeSet<Character> ntset, String selectExp) {
		char a = selectExp.charAt(0);
		if (ntset.contains(a)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param nvset ���ս������
	 * @param selectMap ѡ�е�string��
	 * �Ƿ����Է��ս����ʼ��
	 */
	public static boolean isNvStart(TreeSet<Character> nvset, String selectExp) {
		char a = selectExp.charAt(0);
		if (nvset.contains(a)) {
			return true;
		} else {
			return false;
		}
	}

	 /** 
     * ���Ҳ���ʽ 
     *  
     * @param selectMap 
     * @param peek 
     *            ��ǰNv 
     * @param charAt 
     *            ��ǰ�ַ� 
     * @return 
     */ 
	public static String findUseExp(
			TreeMap<Character, HashMap<String, TreeSet<Character>>> selectMap,
			Character peek, char charat) {
		try {
			HashMap<String, TreeSet<Character>> Hmap=selectMap.get(peek);
			Set<String> Exp=Hmap.keySet();
			//System.out.println("exp "+Exp);
			for (String useExp : Exp) {
				TreeSet<Character> ts=Hmap.get(useExp);
				//System.out.println("useexp:"+useExp+" charat :"+charat+" ts "+ts);
				if (ts.contains(charat)) {
					return useExp;
				}
				//System.out.println("-------------------------");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		return null;
	}
}
