package suncertify.util;

import java.util.List;

public class ListConverter {

	public static int[] convertIntegerListToIntArray(List<Integer> list) {
		final int[] intArray = new int[list.size()];
		for (int index = 0; index < list.size(); index++) {
			intArray[index] = list.get(index);
		}
		return intArray;
	}

}
