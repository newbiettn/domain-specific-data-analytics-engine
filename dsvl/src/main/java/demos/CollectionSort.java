package demos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-06-03
 */
public class CollectionSort {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("gender");
        names.add("age");
        Collections.sort(names);
        System.out.println(names.toString());
    }
}
