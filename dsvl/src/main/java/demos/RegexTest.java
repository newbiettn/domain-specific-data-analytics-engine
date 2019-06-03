package demos;

/**
 * Describe class purpose here.
 *
 * @author Ngoc Tran
 * @since 2019-05-14
 */
public class RegexTest {
    public static void main(String[] args){
        String pattern = "(\\w)(_)(\\w+)(.model)";
        String example = "model_1_1_RandomForest.model";
        String updated = example.replaceAll(pattern, "$1");
        System.out.println(updated);
    }
}
