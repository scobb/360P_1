
public class PSort {
	// should use runnable.
	public static void parallelSort(int[] a, int begin, int end){
		// going to start by implementing quicksort without any threads.
		
		// choose a pivot
		int pivot = begin + (end - begin)/2;
		int pivot_value = a[pivot];
		
		// move all values < pivot_value to left of it, >pivot_value to right
		int i = begin;
		int j = end;
		
		while (i <= j) {
			while (a[i] < pivot_value) {
				// walk from the left until we get a value >= to the pivot value
				i++;
			}
			while (a[j] > pivot_value) {
				// walk from the right until we get a value <= to the pivot value
				j--;
			}
			if (i <= j) {
				// if we haven't crossed in our walk, swap.
				swap(a, i, j);
				i++;
				j--;
			}
		}
		
		if (begin < j) {
			// if we have an unsorted first half, recurse on it
			parallelSort(a, begin, j);
		}
		if (i < end) {
			// if we have an unsorted second half, recurse on it
			parallelSort(a, i, end);
		}
	}
	
	public static void swap(int[] a, int ind_a, int ind_b) {
		int tmp = a[ind_a];
		a[ind_a] = a[ind_b];
		a[ind_b] = tmp;
	}
	
	public static void main(String[] args) {
		int[] arr = { 17, 4, 8, 1, 3, 2, 5, 7, 11, 10, 15 };
		for (int k = 0; k < arr.length; k++) {
			System.out.print(arr[k] + " " );
		}
		System.out.println();
		parallelSort(arr, 0, arr.length - 1);
		for (int i = 0; i < arr.length; i ++) {
			System.out.print(arr[i] + " ");
		}
	}

}
