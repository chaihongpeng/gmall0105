package stream;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestTream {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("bbbb11");
        list.add("dddd03");
        list.add("cccc002");
        list.add("dddd001");
        Stream<String> stream = list.stream();
//        List<String> collect = stream.map(x -> x = x + "001").collect(Collectors.toList());
        Stream<Integer> integerStream = stream.sorted((x, y) -> x.length() > y.length() ? 1 : -1).filter(x -> x.length() > 2).distinct().map(x -> Integer.parseInt(x.substring(4)));
//        integerStream.forEach(x -> System.out.println(x));
        List<Integer> collect = integerStream.limit(2).collect(Collectors.toList());
        System.out.println(collect);
        TreeMap<Object, Object> objectObjectTreeMap = new TreeMap<>();
    }
}
