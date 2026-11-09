import org.cprover.CProver;

// 1. JBMC builds trees of NODES nodes by calling addChild on any pair of nodes
// allowed by its precondition, and checks the class invariant of every node after
// each call. --unwind K bounds the number of calls (and every other loop).
//   ./bmc CompositeCheck.addChildren --unwind 4 --no-unwinding-assertions
public class CompositeCheck {

    static final int NODES = 3;

    public static void addChildren() {
        Composite[] nodes = new Composite[NODES];
        for (int i = 0; i < NODES; i++) {
            nodes[i] = new Composite(CProver.nondetInt());
        }
        while (CProver.nondetBoolean()) {
            Composite p = nodes[anyIndex()];
            Composite c = nodes[anyIndex()];
            CProver.assume(c != p && c.parent == null && c.firstChild == null);   // addChild's precondition
            p.addChild(c);
            for (Composite n : nodes) {
                checkInvariant(n);
            }
        }
    }

    static int anyIndex() {
        int i = CProver.nondetInt();
        CProver.assume(0 <= i && i < NODES);
        return i;
    }

    // The class invariant of Composite, from the comments on its fields.
    static void checkInvariant(Composite n) {
        for (Composite c = n.firstChild; c != null; c = c.nextSibling) {
            assert c.parent == n;                  // each child points back to its parent
        }
        // TODO (1): value is the maximum of initValue and the children's values;
        // maxChild is null exactly when value == initValue, and otherwise it is
        // one of the children and its value is value.
    }
}
