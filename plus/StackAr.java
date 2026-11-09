// 4. A stack of ints on an array: implement it and write its specification.
//   ./esc plus/StackAr.java
// The fields are spec_public: they are private, but the public contracts may mention them.
public class StackAr {

    /** Stack capacity. */
    private static final /*@ spec_public @*/ int DEFAULT_CAPACITY = 10;

    /** Array of elements on the stack. */
    private final /*@ spec_public @*/ int[] elems;

    /** Index of the top element (-1 when the stack is empty). */
    private /*@ spec_public @*/ int top = -1;

    // TODO (4): class invariant

    // TODO (4): specify every constructor and method
    public StackAr() {
        this(DEFAULT_CAPACITY);
    }

    public StackAr(int capacity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public /*@ pure @*/ boolean isEmpty() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public /*@ pure @*/ boolean isFull() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public /*@ pure @*/ int size() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void push(int o) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int pop() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public /*@ pure @*/ int peek() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
