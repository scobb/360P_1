import java.util.concurrent.*;
public class PSort {
	public static ExecutorService threadPool = Executors.newCachedThreadPool();
	public static class PSortRunnable implements Runnable {
		private int begin;
		private int end;
		private int[] a;

		public PSortRunnable(int[] a, int begin, int end) {
			this.begin = begin;
			this.end = end;
			this.a = a;
		}

		public void run() {
			qsort();
		}

		private void qsort() {
			// choose a pivot
			int pivot = begin + (end - begin) / 2;
			int pivot_value = a[pivot];

			// move all values < pivot_value to left of it, >pivot_value to
			// right
			int i = begin;
			int j = end;

			while (i <= j) {
				while (a[i] < pivot_value) {
					// walk from the left until we get a value >= to the pivot
					// value
					i++;
				}
				while (a[j] > pivot_value) {
					// walk from the right until we get a value <= to the pivot
					// value
					j--;
				}
				if (i <= j) {
					// if we haven't crossed in our walk, swap.
					swap(a, i, j);
					i++;
					j--;
				}
			}
			Future<?> f1 = null;
			if (begin < j) {
				// if we have an unsorted first half, recurse on it
				f1 = threadPool.submit(new PSortRunnable(a, begin, j));
			}
			Future<?> f2 = null;
			if (i < end) {
				// if we have an unsorted second half, recurse on it
				f2 = threadPool.submit(new PSortRunnable(a, i, end));
			}
			if (f1 != null){
				while (!f1.isDone());
			}

			if (f2 != null){
				while (!f2.isDone());
			}
		}

		public static void swap(int[] a, int ind_a, int ind_b) {
			int tmp = a[ind_a];
			a[ind_a] = a[ind_b];
			a[ind_b] = tmp;
		}
	}

	public static void parallelSort(int[] a, int begin, int end) {
		ExecutorService es = Executors.newSingleThreadExecutor();
		es.execute(new PSortRunnable(a, begin, end));
		es.shutdown();
		while (!es.isTerminated());
		threadPool.shutdown();
	}

	public static void main(String[] args) {
		int size = 50;
		int[] arr = new int[size];
		for (int k = 0; k < arr.length; k++){
			arr[k] = size - k;
		}
		for (int k = 0; k < arr.length; k++) {
			System.out.print(arr[k] + " ");
		}
		System.out.println();
		parallelSort(arr, 0, arr.length - 1);
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
	}

}
