import java.util.ArrayList;
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
		int[] arr = { 14, 12, 16, 1, 3, 18, 7, 76 };
		int target = 16;
		int num_threads = 4;
		int result = PSearch.parallelSearch(target, arr, num_threads);
		System.out.println("Result: " + result);
	}

}
