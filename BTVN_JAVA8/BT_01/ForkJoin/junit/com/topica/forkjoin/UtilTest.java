package com.topica.forkjoin;

import static junit.framework.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class UtilTest {
	private int[] arr1;
	private int[] arr2;
	private int[] arr3;
	private int[] arr4;
	
	@Before
	public void setUp() {
		arr1 = new int[]{ 13, 2, 76, 45, 0, 31, 11, 86, 29, 65 };
		arr2 = makeSortedCopy(arr1);
		
		Random r = new Random(123L);
		arr3 = new int[50];
		for (int i = 0; i < arr3.length; i++) {
			arr3[i] = r.nextInt();
		}
		arr4 = makeSortedCopy(arr3);
	}

	private int[] makeSortedCopy(int[] arr1) {
		int[] arr2 = new int[arr1.length];
		System.arraycopy(arr1, 0, arr2, 0, arr1.length);
		Arrays.sort(arr2);
		return arr2;
	}
	
	@Test
	public void testIsSorted() {
		assertFalse(Util.isSorted(arr1));
		assertTrue(Util.isSorted(arr2));
		assertFalse(Util.isSorted(arr3));
		assertTrue(Util.isSorted(arr4));
	}
}
