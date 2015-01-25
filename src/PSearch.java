import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

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
		int num_per_thread = A.length / numThreads;
		ArrayList<Integer> results = new ArrayList<Integer>();
		int start_ind = 0;
		for (int i = 0; i < numThreads; i++) {
			try {
				if (i == numThreads - 1) {
					results.add((new SearchCallable(A, start_ind, A.length, x))
							.call());
				} else {
					results.add((new SearchCallable(A, start_ind, start_ind
							+ num_per_thread, x)).call());
				}
				start_ind += num_per_thread;
			} catch (Exception exc) {

			}
		}
		for (int result: results) {
			if (result != -1)
				return result;
		}
		return -1;
	}
	
	public static void main(String[] args) {
		int[] arr = { 14, 12, 16, 1, 3, 18, 7, 76 };
		int target = 100;
		int num_threads = 4;
		int result = PSearch.parallelSearch(target, arr, num_threads);
		System.out.println("Result: " + result);
	}

}
