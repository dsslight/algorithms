package coursera.assignment5;

public class Util {
    // TODO fucking bull shit, cos import of functional interfaces are forbidden
    @FunctionalInterface
    public interface Consumer<T> {
        void accept(T t);
    }

    @FunctionalInterface
    public interface Function<T, R> {
        R apply(T t);
    }

    @FunctionalInterface
    public interface BiFunction<T, U, R> {
        R apply(T t, U u);
    }

    public static void checkArgument(final Object arg) {
        checkArgument(arg, val -> val);
    }

    public static void checkNotArgument(final Object arg) {
        checkArgument(arg, val -> !val);
    }

    public static void checkArgument(final Object arg, final Function<Boolean, Boolean> f) {
        if (f.apply(arg == null)) {
            throw new IllegalArgumentException("argument should be not null");
        }
    }
}
