package sort;

public class QuickSort implements Sort{
    public void sort(int[] sort) {
        sort(sort,0,sort.length-1);
    }
    public void sort(int[] list, int head, int tail) {
        if (head >= tail) {
            return;
        }
        int temp = list[head];
        int min = head;
        int max = tail;
        while (min < max) {
            while (list[max] >= temp && max > min) {
                max--;
            }
            if (max > min) {
                list[min] = list[max];
                min++;
            }
            while (list[min] <= temp && max > min) {
                min++;
            }
            if (max > min) {
                list[max] = list[min];
                max--;
            }
        }
        list[min] = temp;
        sort(list, head, max-1);
        sort(list, max+1, tail);
    }
}
