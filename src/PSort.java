import java.util.Random;
import java.util.concurrent.*;

public class PSort {
	public static ExecutorService threadPool;

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
			if (end == -1) {
				// handle empty array case
				return;
			}
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
			if (f1 != null) {
				try {
					f1.get();
				} catch (Exception exc) {
					// TODO
					System.out.println("EXCEPTION");
				}
			}
			if (f2 != null) {
				try {
					f2.get();
				} catch (Exception exc) {
					// TODO
					System.out.println("EXCEPTION");
				}
			}
		}

		public static void swap(int[] a, int ind_a, int ind_b) {
			int tmp = a[ind_a];
			a[ind_a] = a[ind_b];
			a[ind_b] = tmp;
		}
	}

	public static void parallelSort(int[] a, int begin, int end) {
		threadPool = Executors.newCachedThreadPool();
		Future<?> f = threadPool.submit(new PSortRunnable(a, begin, end-1));
		try {
			f.get();
		} catch (Exception exc) {
			// TODO
			System.out.println("EXCEPTION:" + exc.getMessage());
		}
		threadPool.shutdown();

	}

}
