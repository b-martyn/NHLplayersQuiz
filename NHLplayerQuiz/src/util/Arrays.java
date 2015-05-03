package util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import player.Player;
import player.Position;

public class Arrays {
	
	public static void main(String[] args){
		Player p1 = new Player(16, "Bob", "Saget", Position.CENTER, 34);
		Player p2 = new Player(13, "Joe", "Thorton", Position.DEFENSEMAN, 34);
		Player p3 = new Player(17, "Tim", "Serre", Position.GOALIE, 34);
		Player p4 = new Player(13, "Penny", "Martyn", Position.LEFT_WING, 34);
		Player p5 = new Player(16, "Sarge", "Bilbo", Position.RIGHT_WING, 34);
		Player[] db = {p1, p2, p3, p4};
		Player[] web = {p3, p4, p5};
		Comparator<Player> comparator = Player.ID_ORDER;
		for(Player player : Arrays.<Player>findUnique(db, web, comparator)){
			System.out.println(player);
		}
		System.out.println("------------------------------------------------");
		for(Player player : Arrays.<Player>findUnique(web, db, comparator)){
			System.out.println(player);
		}
		System.out.println("------------------------------------------------");
		for(Player player : Arrays.<Player>findMatches(db, web, comparator)){
			System.out.println(player);
		}
	}
	
	public static void arrayShuffle(Object[] array){
		// iterate through array and uniformly swap each element with a random element to the sub-array < index
		for(int i = 0; i < array.length; i++){
			int randomIndex = (int)(Math.random() * (i + 1));
			exchange(array, i, randomIndex);
		}
	}
	
	// Swap item array[j] with item array[i]
	private static void exchange(Object[] array, int i, int j){
		Object item = array[i];
		array[i] = array[j];
		array[j] = item;
	}
	
	// Combine 2 Arrays
	@SuppressWarnings("unchecked")
	public static <S> S[] arrayCombine(S[] array1, S[] array2){
		S[] newArray = (S[])Array.newInstance(array1.getClass().getComponentType(), array1.length + array2.length);
		int index = 0;
		for(;index < array1.length; index++){
			newArray[index] = array1[index];
		}
		for(int i = 0; i < array2.length; i++, index++){
			newArray[index] = array2[i];
		}
		return newArray;
	}
	
	// Returns array of items in array 1 that match items in array 2 using comparator
	@SuppressWarnings("unchecked")
	public static <S> S[] findMatches(S[] array1, S[] array2, Comparator<? super S> comparator){
		List<S> list = new ArrayList<>();
		
		Arrays.<S>shellSort(array2, comparator);
		for(int i = 0; i < array1.length; i++){
			int searchResult = Arrays.<S>binarySearch(array2, comparator, array1[i]);
			if(searchResult >= 0){
				list.add(array1[i]);
			}
		}
		
		S[] newArray = (S[])Array.newInstance(array1.getClass().getComponentType(), list.size());
		for(int i = 0; i < newArray.length; i++){
			newArray[i] = list.get(i);
		}
		return newArray;
	}
	
	// Returns array of items in array 1 that are not in array 2 using comparator
	@SuppressWarnings("unchecked")
	public static <S> S[] findUnique(S[] array1, S[] array2, Comparator<? super S> comparator){
		List<S> list = new ArrayList<>();
		
		Arrays.<S>shellSort(array2, comparator);
		for(int i = 0; i < array1.length; i++){
			int searchResult = Arrays.<S>binarySearch(array2, comparator, array1[i]);
			if(searchResult == -1){
				list.add(array1[i]);
			}
		}
		
		S[] newArray = (S[])Array.newInstance(array1.getClass().getComponentType(), list.size());
		for(int i = 0; i < newArray.length; i++){
			newArray[i] = list.get(i);
		}
		return newArray;
	}
	
	public static <S> void shellSort(S[] array, Comparator<? super S> comparator){
		// sort by multiples of h
		int h = 1;
		// 3h + 1 increment sequence
		while(h < array.length){
			h = 3 * h + 1;
		}
		
		// Sort array by moving multiples of h down array until
		// element is larger than preceding element h units away
		// When h hits 1 it becomes a normal insertion sort
		while(h >= 1){
			for(int i = h; i < array.length; i++){
				for(int j = i; j >= h; j -= h){
					if(comparator.compare(array[j], array[j - h]) < 0){
						exchange(array, j, j - h);
					}else{
						break;
					}
				}
			}
			// Move to next increment.  Is rounded down so result matches
			// 3h + 1 increment sequence
			h = h / 3;
		}
	}
	
	// keep guessing the middle of the array and cut the array in half 
	// until only 1 element remains and it matches or doesn't
	public static <S> int binarySearch(S[] array, Comparator<? super S> comparator, S element){
		int index = -1;
		
		int min = 0;
		int max = array.length - 1;
		
		while(min <= max){
			int guessIndex = (int)Math.floor((min + max) / 2);
			if(comparator.compare(array[guessIndex], element) == 0){
				index = guessIndex;
				break;
			}else if(comparator.compare(array[guessIndex], element) < 0){
				min = guessIndex + 1;
			}else{
				max = guessIndex - 1;
			}
		}
		
		return index;
	}
}
