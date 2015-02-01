import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PSearch {
	public static class SearchCallable implements Callable<Integer> {
		private int[] arr;
		private int begin_ind;
		private int end_ind;
		private int target_val;

		public SearchCallable(int[] arr, int begin_ind, int end_ind,
				int target_val) {
			this.arr = arr;
			this.begin_ind = begin_ind;
			this.end_ind = end_ind;
			this.target_val = target_val;
		}

		@Override
		public Integer call() throws Exception {
			for (int i = begin_ind; i < end_ind; i++) {
				if (arr[i] == target_val) {
					return i;
				}
			}
			return -1;
		}

	}

	public static int parallelSearch(int x, int[] A, int numThreads) {
		// need to use Callable
		if (numThreads <= 0){
			// TODO - throw an exception?
			return -1;
		}
		ExecutorService executorService = Executors
				.newFixedThreadPool(numThreads);
		int num_per_thread = A.length / numThreads;
		int remainder = A.length - num_per_thread;
		ArrayList<Future<Integer>> results = new ArrayList<Future<Integer>>();
		int start_ind = 0;
		for (int i = 0; i < numThreads; i++) {
			try {
				int end_ind = start_ind + num_per_thread;
				if (remainder > 0) {
					remainder--;
					end_ind++;
				}
				results.add(executorService.submit(new SearchCallable(A,
						start_ind, end_ind, x)));
				start_ind += num_per_thread;
			} catch (Exception exc) {

			}
		}
		for (Future<Integer> result : results) {
			try {
				int index = result.get();
				if (index != -1) {
					return index;
				}
			} catch (Exception exc) {
				//TODO - handle ??

			}
		}
		return -1;
	}

	public static void main(String[] args) {
		int size = 100;
		int arr[] = new int[size];
		Random rand = new Random();
		for (int i = 0; i < size; i++){
			arr[i] = rand.nextInt();
		}
		int target_ind = rand.nextInt(size);
		int target = arr[target_ind];
		int num_threads = 4;
		System.out.println("Testing a value in array of size " + size);
		int result = PSearch.parallelSearch(target, arr, num_threads);
		assert result == target_ind;
		
		System.out.println("Testing a value that is not contained in the array.");
		Integer int_array[] = new Integer[size];
		for (int i = 0; i < size; i++){
			int_array[i] = arr[i];
		}
		ArrayList<Integer> bob = new ArrayList<Integer>(Arrays.asList(int_array));
		int not_contained = 0;
		while (bob.contains(not_contained)){
			not_contained += 1;
		}
		result = PSearch.parallelSearch(not_contained, arr, num_threads);
		assert result == -1;
		
		System.out.println("Testing on an empty array.");
		result = PSearch.parallelSearch(target, new int[0], num_threads);
		assert result == -1;
		
		System.out.println("Testing with no threads.");
		result = PSearch.parallelSearch(target, new int[0], 0);
		assert result == -1;
		
		System.out.println("Tests passed.");
	}

}
