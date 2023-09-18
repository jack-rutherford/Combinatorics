package algorithm;

import java.util.ArrayList;
import java.util.Arrays;

public class CombinatoricsUtilities {
	
	public static void main(String[] args) {
		String[] str1 = {"2", "1", "3"};
		String[] str2 = {"1", "2", "3", "4"};
		String[] str3 = {"1", "2", "3", "4", "5"};
		String[] str4 = {"Lizzi", "Jack", "Cece"};
		
		ArrayList<String[]> arr1 = generatePermutations(str1);
		ArrayList<String[]> arr2 = generatePermutations(str2);
		ArrayList<String[]> arr3 = generatePermutations(str3);
		ArrayList<String[]> arr4 = generatePermutations(str4);
		
		System.out.println(printArrayList(arr1) + "\n");
		System.out.println(printArrayList(arr2) + "\n");
		System.out.println(printArrayList(arr3) + "\n");
		System.out.println(printArrayList(arr4) + "\n");
		
	}
	
	public static ArrayList<String[]> generatePermutations(String[] input){
		ArrayList<String[]> arr = new ArrayList<String[]>();
		
		// base case if there is only one element in input
		if(input.length == 1) {
			arr.add(input);
			return arr;
		}
		
		// Arrays.sort uses quick sort to sort the array, most efficient way O(nlogn)
		Arrays.sort(input);
		
		// Add the first permutation to arr that gets returned O(n)
		arr.add(Arrays.copyOf(input, input.length));
		
		// Add arrows to determine mobile elements O(n)
		addArrows(input);
		
		
		/**
		 *  while the last permutation has a mobile element (???)
		 *  	find the largest mobile element
		 *  	swap k with the adjacent element k’s arrow points to 
		 *  	reverse the direction of all the elements that are larger than k 
		 *  	add the new permutation to the list
		 */
		while(hasMobileElements(input)) { // O(n!)
			int index = findMaxMobileElement(input); //O(n)
			String str = input[index];
			
			if(leftOrRightArrow(str).equals("right")) {
				input[index] = input[index+1];
				input[index+1] = str;
			}
			else {
				input[index] = input[index-1];
				input[index-1] = str;
			}
			
			switchArrows(input, str); //O(n)
			
			// has to .copyOf or else it edits address of array, changing it in two places
			String[] minArrow = removeArrows(Arrays.copyOf(input, input.length)); //O(n)
			
			arr.add(minArrow); //usually O(1) or O(n)
			
		}
		
		return arr;
	}
	
	/**
	 * O(n), This methods primary purpose is to fix a problem
	 * I made because I didn't think this through fully
	 * @param array of Strings, named input, with arrows
	 * @return array of Strings without arrows
	 */
	private static String[] removeArrows(String[] input) {
		for(int i = 0; i < input.length; i++) {
			input[i] = input[i].substring(1);
		}
		return input;
	}
	
	/**
	 * O(n) efficiency, adds arrows to the string
	 * This will be to determine later which elements are mobile and not mobile
	 * Should only be called once at the beginning of the JT algorithm
	 * @param array of Strings, named input
	 * @return array of Strings, named input, with arrows
	 */
	public static String[] addArrows(String[] input) {
		for(int i = 0; i < input.length; i++) {
			input[i] = "<" + input[i];
		}
		return input;
	}
	
	/**
	 * O(n), Goes through array and determines if there is a mobile
	 * element present in the array, as soon as it finds at least one
	 * it returns true
	 * @param array of Strings, named input
	 * @return found mobile element (true), no mobile element found (false)
	 */
	public static boolean hasMobileElements(String[] input) {
		for(int i = 0; i < input.length; i++) {
			if(isMobileElement(input, i)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * O(1), Checks mobility of a given element.
	 * Element is mobile if the element adjacent to it in the direction
	 * of its arrow, has a smaller value than the element.
	 * So >2 >1, 2 is mobile. >3 <2 >4 <1, 3 and 4 are mobile
	 * 
	 * 
	 * @param array of Strings, named input
	 * @param index of the element we check for mobility
	 * @return true if the element is mobile, false if it is not
	 */
	public static boolean isMobileElement(String[] input, int index) {
		String str = input[index];
		// first element in the array and pointing left means not mobile
		if(index == 0 && leftOrRightArrow(str).equals("left")) {
			return false;
		}
		// last element in the array and pointing right means not mobile
		if(index == input.length-1 && leftOrRightArrow(str).equals("right")) {
			return false;
		}
		
		// pointing right and element to the right smaller than element means mobile
		if(leftOrRightArrow(str).equals("right")) {
			// Strings excluding the arrow, so the arrow doesn't add to bit value
			String temp1 = input[index].substring(1);
			String temp2 = input[index+1].substring(1);
			if(temp1.compareTo(temp2) >= 0) {  
				return true;
			}
		}
		// pointing left and element to the left smaller than element means mobile
		else {
			// Strings excluding the arrow, so the arrow doesn't add to bit value
			String temp1 = input[index].substring(1);
			String temp2 = input[index-1].substring(1);
			if(temp1.compareTo(temp2) >= 0 ) { 
				return true;
			}
		}
		return false; // Not mobile
	}

	/**
	 * O(1), looks at first char of the string and checks if it
	 * is a left or right arrow
	 * @param String named input
	 * @return "left" or "right"
	 */
	public static String leftOrRightArrow(String input) {
		if(input.substring(0, 1).equals(">")) {
			return "right";
		}
		return "left";
	}

	/**
	 * O(n), Takes a String and goes through the list and decides
	 * if you need to switch the arrow. If in the for loop the 
	 * element at index is greater than value of @param str, 
	 * switch the arrow
	 * @param array of Strings, named input
	 * @param String str 
	 * @return array of Strings after switching arrows
	 */
	public static String[] switchArrows(String[] input, String str) {
		String val = str.substring(1);
		for(int i = 0; i < input.length; i++) {
			String arrow = input[i]; //pass this to switchArrow
			String comp = input[i].substring(1); //this for comparison to val
			if(comp.compareTo(val) > 0) { // if e at index > val, switch the arrow
				input[i] = switchArrow(arrow);
			}
		}
		return input;
		
	}
	
	/**
	 * O(1), if the arrows is right, switch it to left, vice versa
	 * @param String named input 
	 * @return String with switched arrow
	 */
	public static String switchArrow(String input) {
		String result = leftOrRightArrow(input);
		String str = "";
		if(result.equals("right")) {
			str = "<" + input.substring(1);
		}
		else {
			str = ">" + input.substring(1);
		}
		return str;
	}

	/**
	 * O(n), goes through the array of Strings and finds the highest value
	 * element and returns the index of the element
	 * @param Array of Strings input
	 * @return index of the highest value mobile element
	 */
	public static int findMaxMobileElement(String[] input) {
		int index = -1;
		String maxVal = "";
		for(int i = 0; i < input.length; i++) {
			String e = input[i].substring(1);
			if(e.compareTo(maxVal) >= 0 && isMobileElement(input, i)) {
				maxVal = e;
				index = i;
			}
		}
		return index;
	}

	public static String printArray(String[] input) {
		String result = "[";
		for(String e : input) {
			result += e + ", ";
		}
		return result.substring(0, result.length()-2) + "]";
	}

	public static String printArrayList(ArrayList<String[]> input) {
		String result = "";
		int i = 1;
		for(String[] e : input) {
			result += i + ": " + printArray(e) + "\n";
			i++;
		}
		return result.substring(0, result.length()-1);
	}
}
