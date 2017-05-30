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
	 * 形如bAb，b=空 param=
	 */
	public static boolean containbA_bIsNull(TreeSet<Character> nvset,
			String itemCharStr, Character c,
			HashMap<Character, ArrayList<Character>> experssionMap) {

		String aStr = c.toString();
		

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
			String itemCharStr, Character c,
			HashMap<Character, ArrayList<Character>> expressionMap) {

		String aStr = c.toString();

		if (itemCharStr.contains(aStr)) {
			String findSTR = null;
			int aIndex = itemCharStr.indexOf(aStr);
			try {
				findSTR = itemCharStr.substring(aIndex + 1, aIndex + 2);
			} catch (Exception e) {
				e.printStackTrace();
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
			String sub = itemCharStr.substring(index + 1, index + 2);
			return sub.charAt(0);
		}
		return null;

	}

	/*
	 * 是否是以空符号开始的
	 */
	public static boolean isEmptyStart(String selectExp) {

		if (selectExp.charAt(0) == 'ε') {
			return true;
		}
		return false;
	}

	/*
	 * 是否是以终结符开始
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
	 * @param nvset 非终结符集合
	 * @param selectMap 选中的string串
	 * 是否是以非终结符开始的
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
     * 查找产生式 
     *  
     * @param selectMap 
     * @param peek 
     *            当前Nv 
     * @param charAt 
     *            当前字符 
     * @return 
     */ 
	public static String findUseExp(
			TreeMap<Character, HashMap<String, TreeSet<Character>>> selectMap,
			Character peek, char charat) {
		HashMap<String, TreeSet<Character>> Hmap=selectMap.get(peek);
		try {
			Set<String> Exp=Hmap.keySet();
			for (String useExp : Exp) {
				TreeSet<Character> ts=Hmap.get(useExp);
				if (ts.contains(charat)) {
					return useExp;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
