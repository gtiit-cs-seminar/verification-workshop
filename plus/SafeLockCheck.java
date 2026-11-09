import org.cprover.CProver;

// 5. Harnesses for SafeLock: JBMC plays the user, choosing any sequence of
// events. The loop runs once per event, so --unwind K means "at most K events".
//   ./bmc SafeLockCheck.neverOpensAfterTooManyWrongPins --unwind 3 --no-unwinding-assertions
public class SafeLockCheck {

    // Any event, with any PIN or code.
    static SafeLock.Event anyEvent() {
        int kind = CProver.nondetInt();
        CProver.assume(0 <= kind && kind <= 2);
        return switch (kind) {
            case 0 -> new SafeLock.EnterPin(CProver.nondetInt());
            case 1 -> new SafeLock.Close();
            default -> new SafeLock.AdminReset(CProver.nondetInt());
        };
    }

    // Property 1: after MAX_ATTEMPTS wrong PINs in a row (with no correct PIN and
    // no admin reset in between), the safe does not open.
    public static void neverOpensAfterTooManyWrongPins(int pin, int adminCode) {
        CProver.assume(pin != adminCode);
        SafeLock safe = new SafeLock(pin, adminCode);
        int wrongInARow = 0;

        while (CProver.nondetBoolean()) {
            SafeLock.Event e = anyEvent();
            if (e instanceof SafeLock.EnterPin p && p.pin() != pin && safe.state() == SafeLock.State.LOCKED) {
                wrongInARow++;
            }
            if (e instanceof SafeLock.AdminReset r && r.code() == adminCode) {
                wrongInARow = 0;
            }

            safe.handle(e);

            assert !(wrongInARow >= SafeLock.MAX_ATTEMPTS && safe.state() == SafeLock.State.OPEN);
            if (safe.state() == SafeLock.State.OPEN) {
                wrongInARow = 0;                // a legitimate opening starts a new count
            }
        }
    }

    // TODO (5): Property 2: a BLOCKED safe only leaves BLOCKED through an
    // AdminReset with the right code.
    public static void onlyAdminUnblocks(int pin, int adminCode) {
        assert false;   // remove this line
    }
}
