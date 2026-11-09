// Warm-up: if this verifies, OpenJML is installed correctly.
//   ./esc hw/Constant.java
public class Constant {

    //@ ensures \result == 42;
    public static int theAnswer() {
        return 42;
    }
}
