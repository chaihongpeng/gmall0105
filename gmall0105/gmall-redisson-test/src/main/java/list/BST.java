package list;

public class BST<T> {

    public Entry<T> root;
    private int size;


    //Node节点类
    private class Entry<T> {
        T item;
        Entry<T> left;
        Entry<T> right;

        Entry(T item, Entry<T> left, Entry<T> right) {
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }
}
