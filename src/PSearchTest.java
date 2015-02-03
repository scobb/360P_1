import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.junit.Test;


public class PSearchTest {
	// using an odd number of tests as the best to force remainders most of the time
	int num_threads = 3;
	Random rand = new Random();
	
	/*
	 * Helper methods
	 */
	private int successfulTest(int size, int target_index){
		int arr[] = new int[size];
		for (int i = 0; i < size; i ++){
			arr[i] = rand.nextInt();
		}
		int target_value = arr[target_index];
		return PSearch.parallelSearch(target_value, arr, num_threads);
		
	}
	
	private int unsuccessfulTest(int size){
		int arr[] = new int[size];
		Integer int_array[] = new Integer[size];
		for (int i = 0; i < size; i++){
			arr[i] = rand.nextInt();
			int_array[i] = arr[i];
		}
		ArrayList<Integer> bob = new ArrayList<Integer>(Arrays.asList(int_array));
		int not_contained = 0;
		while (bob.contains(not_contained)){
			not_contained += 1;
		}
		return PSearch.parallelSearch(not_contained, arr, num_threads);
	}
	/*
	 * Tests
	 */
	
	@Test
	public void zeroThreadTest(){
		int arr[] = new int[100];
		arr[0] = 1;
		assertEquals(-1, PSearch.parallelSearch(1, arr, 0));
	}
	
	@Test
	public void manyThreadTest(){
		int arr[] = new int[100];
		arr[99] = 1;
		assertEquals(99, PSearch.parallelSearch(1, arr, 500));
	}
	@Test
	public void noRemainderTest(){
		int arr[] = new int[99];
		arr[98] = 1;
		assertEquals(98, PSearch.parallelSearch(1, arr, num_threads));
	}

	@Test
	public void randomSuccesfulTest() {
		int size = 100;
		int target_ind = rand.nextInt(size);
		assertEquals(successfulTest(size, target_ind), target_ind);
	}

	@Test
	public void emptyTest(){
		int result = PSearch.parallelSearch(15, new int[0], num_threads);
		assertEquals(result, -1);
	}
	
	@Test
	public void randomUnsuccessfulTest() {
		assertEquals(unsuccessfulTest(100), -1);
	}
	
	@Test
	public void largeSuccessfulTest(){
		int size = 10000;
		int target_index = rand.nextInt(size);
		assertEquals(successfulTest(size, target_index), target_index);
	}
	@Test
	public void largeUnsuccessfulTest(){
		assertEquals(unsuccessfulTest(10000), -1);
	}
	
	@Test
	public void firstTest() {
		assertEquals(successfulTest(10000, 0), 0);
	}
	@Test
	public void lastTest() {
		assertEquals(successfulTest(10000, 99), 99);
	}
}
