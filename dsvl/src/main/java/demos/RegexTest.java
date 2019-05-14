package demos;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-14
 */
public class RegexTest {
    public static void main(String[] args){
        String pattern = "(diab:has)(\\w+)";
        String example = "diab:hasURN";
        String updated = example.replaceAll(pattern, "$2");
        System.out.println(updated);
    }
}
