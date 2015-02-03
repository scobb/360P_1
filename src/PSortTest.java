import static org.junit.Assert.*;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import junit.framework.TestCase;

import org.junit.Test;


public class PSortTest {
	public void checkArraySorted(int[] arr){
		for (int i = 0; i < arr.length-1; i++){
			assertTrue(arr[i] <= arr[i+1]);
		}
	}

	@Test
	public void emptyArrayTest() {
		// doesn't crash and still has length of 0
		int[] arr = {};
		PSort.parallelSort(arr, 0, arr.length);
		checkArraySorted(arr);
		assertEquals(arr.length,  0);
	}
	
	@Test
	public void testArraySame() {
		int[] arr = {1, 1, 1, 1, 1};
		PSort.parallelSort(arr, 0, arr.length-1);
		checkArraySorted(arr);
	}
	@Test
	public void testArraySorted() {
		int[] arr = {1, 2, 3, 4, 5};
		PSort.parallelSort(arr, 0, arr.length-1);
		checkArraySorted(arr);
	}
	
	@Test
	public void testArraySortedDescending() {
		int[] arr = {5, 4, 3, 2, 1};
		PSort.parallelSort(arr, 0, arr.length-1);
		checkArraySorted(arr);
	}
	@Test
	public void testNegative() {
		int[] arr = {-5, 4, 3, -2, 1};
		PSort.parallelSort(arr, 0, arr.length-1);
		checkArraySorted(arr);
	}
	@Test
	public void testRandom(){
		Random rand = new Random();
		int[] arr = new int[100];
		for (int i = 0; i < arr.length; i++){
			arr[i] = rand.nextInt();
		}
		PSort.parallelSort(arr, 0, arr.length-1);
		checkArraySorted(arr);
	}
	@Test
	public void testLarge(){
		Random rand = new Random();
		int[] arr = new int[10000];
		for (int i = 0; i < arr.length; i++){
			arr[i] = rand.nextInt();
		}
		PSort.parallelSort(arr, 0, arr.length-1);
		checkArraySorted(arr);
	}

}
