// 1. A tree of components (the Composite pattern) that caches, in every node,
// the maximum value of its subtree.
//
// Ported from the Java version of AutoProof's composite.e (an Eiffel verification
// benchmark) used in the GAssert and SpecFuzzer experiments. Changes: the list of
// children is a linked list (firstChild, nextSibling), since JBMC does not model
// java.util collections, and the set of ancestors, which addChild did not need, is gone.
//
// The fields are package-private so that the harness can read them.
public final class Composite {

    /** The children: firstChild, firstChild.nextSibling, ... (null-terminated). */
    Composite firstChild;
    Composite nextSibling;

    /** Parent node (null for a root). */
    Composite parent;

    /** The maximum of initValue and the values of the children. */
    int value;

    /** Initial value (at node creation). */
    final int initValue;

    /** The child whose value is value, if value is greater than initValue; null otherwise. */
    Composite maxChild;

    /** Creates a single node with initial value v. */
    public Composite(int v) {
        initValue = v;
        value = v;
    }

    /** Adds c as a child: c must be a single node (no parent, no children) other than this. */
    public void addChild(Composite c) {
        if (c == null) {
            throw new IllegalArgumentException();
        }
        if (c == this || c.parent != null || c.firstChild != null) {
            throw new IllegalArgumentException();
        }
        c.parent = this;
        c.nextSibling = firstChild;
        firstChild = c;
        update(c);
    }

    /** Updates value of this node and its ancestors taking into account an updated child c. */
    private void update(Composite c) {
        if (value < c.value) {
            value = c.value;
            maxChild = c;
            if (parent != null) {
                parent.update(c);
            }
        }
    }
}
