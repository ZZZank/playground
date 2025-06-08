import caller.CallerClassGetter;
import lombok.val;

/**
 * @author ZZZank
 */
public class Main {

    public static void main(String[] args) {

        val callerClass = CallerClassGetter.of(System.err::println).get();
        System.out.println("caller class: " + callerClass);
    }
}
