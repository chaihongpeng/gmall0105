package sort;

public class GuiSort implements Sort {


    @Override
    public void sort(int[] sort) {
        mer(sort, 0, sort.length - 1, new int[sort.length]);
    }

    public void mer(int[] sort ,int head,int tail ,int[] temp) {
        if (head < tail) {
            int mid = (head + tail) / 2;
            mer(sort, head, mid, temp);
            mer(sort, mid + 1, tail, temp);
            mer(sort, head, mid, tail, temp);
        }
    }

    public void mer(int[] sort, int head,int mid, int tail, int[] temp ) {
        int i = head;
        int j = mid + 1;
        int t = 0;
        while (i <= mid && j <= tail) {
            if (sort[i] <= sort[j]) {
                temp[t] = sort[i];
                i++;
                t++;
            } else {
                temp[t] = sort[j];
                j++;
                t++;
            }
        }
        while (i <= mid) {
            temp[t] = sort[i];
            i++;
            t++;
        }
        while (j <= tail) {
            temp[t] = sort[j];
            j++;
            t++;
        }
        t = 0;
        int tempHead = head;
        while (tempHead <= tail) {
            sort[tempHead] = temp[t];
            t++;
            tempHead++;
        }
    }
}
