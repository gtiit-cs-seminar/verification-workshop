import org.cprover.CProver;

// 4. Your StackAr against a model: JBMC runs any sequence of push, pop and
// peek on a stack of capacity <= 3 and compares every answer with a plain array
// and a counter. --unwind K bounds the number of operations.
//   ./bmc StackArCheck.operations --unwind 5 --no-unwinding-assertions
public class StackArCheck {

    public static void operations(int capacity) {
        CProver.assume(0 <= capacity && capacity <= 3);
        StackAr s = new StackAr(capacity);
        int[] model = new int[capacity];
        int n = 0;

        while (CProver.nondetBoolean()) {
            int op = CProver.nondetInt();
            if (op == 0 && n < capacity) {                // push, when not full
                int x = CProver.nondetInt();
                s.push(x);
                model[n] = x;
                n++;
            } else if (op == 1 && n > 0) {                // pop, when not empty
                n--;
                assert s.pop() == model[n];
            } else if (op == 2 && n > 0) {                // peek, when not empty
                assert s.peek() == model[n - 1];
            }
            assert s.size() == n;
            assert s.isEmpty() == (n == 0);
            assert s.isFull() == (n == capacity);
        }
    }
}
