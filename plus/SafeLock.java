// 5. The controller of a hotel-room safe.
//
// The safe opens with the right PIN. After MAX_ATTEMPTS wrong PINs in a row it
// blocks, and only the admin code (hotel staff) unblocks it. Closing the door
// locks the safe again.
public final class SafeLock {

    public enum State { LOCKED, OPEN, BLOCKED }

    /** What the user can do: type a PIN, close the door, or (staff) type the admin code. */
    public sealed interface Event permits EnterPin, Close, AdminReset {}
    public record EnterPin(int pin) implements Event {}
    public record Close() implements Event {}
    public record AdminReset(int code) implements Event {}

    public static final int MAX_ATTEMPTS = 3;

    private final int pin;
    private final int adminCode;
    private State state = State.LOCKED;
    private int failed = 0;                // wrong PINs in a row

    // bonus 2: a class invariant for OpenJML

    public SafeLock(int pin, int adminCode) {
        this.pin = pin;
        this.adminCode = adminCode;
    }

    public /*@ pure @*/ State state() {
        return state;
    }

    public void handle(Event e) {
        if (e instanceof EnterPin p) {
            enterPin(p.pin());
        } else if (e instanceof Close) {
            close();
        } else if (e instanceof AdminReset r && r.code() == adminCode) {
            state = State.LOCKED;
            failed = 0;
        }
    }

    private void enterPin(int p) {
        if (state != State.LOCKED) {
            return;
        }
        if (p == pin) {
            state = State.OPEN;
            failed = 0;
        } else {
            failed++;
            if (failed >= MAX_ATTEMPTS) {
                state = State.BLOCKED;
            }
        }
    }

    private void close() {
        if (state == State.OPEN) {
            state = State.LOCKED;
        }
        failed = 0;
    }
}
