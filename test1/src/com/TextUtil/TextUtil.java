package com.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
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
		}else{
			return false;
		}
		

	}

	public static boolean containbAbIsNull(TreeSet<Character> nvset,
			String itemCharStr, Character c) {

		String aStr=c.toString();
		
		
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

		String aStr=c.toString();
		
		if (itemCharStr.contains(aStr)) {
			String findSTR = null;
			int aIndex=itemCharStr.indexOf(aStr);
			try {
				findSTR = itemCharStr.substring(aIndex + 1, aIndex + 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (ntset.contains(findSTR.charAt(0))) {
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	public static Character getA_lastChar(String itemCharStr, Character c) {
	}

	public static boolean isEmptyStart(String selectExp) {
	}

	public static boolean isNtStart(TreeSet<Character> ntset, String selectExp) {
	}

	public static boolean isNvStart(TreeSet<Character> nvset, String selectExp) {
	}

	public static String findUseExp(
			TreeMap<Character, HashMap<String, TreeSet<Character>>> selectMap,
			Character peek, char charat) {

	}
	public static boolean getAlastChar(String itemCharStr,Character a){}
	
}
