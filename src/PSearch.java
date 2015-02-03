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
		if (numThreads <= 0){
			return -1;
		}
		ExecutorService executorService = Executors
				.newFixedThreadPool(numThreads);
		
		// these values will help us make our start/end indices
		int num_per_thread = A.length / numThreads;
		int remainder = A.length - numThreads * num_per_thread;
		
		// this is where we'll save results
		ArrayList<Future<Integer>> results = new ArrayList<Future<Integer>>();
		
		// start index begins at 0
		int start_ind = 0;
		
		for (int i = 0; i < numThreads; i++) {
			if (num_per_thread == 0 && remainder == 0){
				// more threads than the length of the array. Search should be complete.
				break;
			}
			
			// if there's still work to do, let's divide it equitably.
			try {
				int end_ind = start_ind + num_per_thread;
				if (remainder > 0) {
					remainder--;
					end_ind++;
				}
				results.add(executorService.submit(new SearchCallable(A,
						start_ind, end_ind, x)));
				start_ind = end_ind;
			} catch (Exception exc) {
				System.out.println("EXCEPTION in execute: " + exc.getMessage());

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
				System.out.println("EXCEPTION in get: " + exc.getMessage());

			}
		}
		return -1;
	}

}
