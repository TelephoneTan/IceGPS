package pub.telephone.iceGPS.dataSource;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import kotlin.jvm.functions.Function1;
import pub.telephone.iceGPS.MyApp;

public class Manager<U> {
    final List<WeakReference<U>> cache = new ArrayList<>();

    void clean() {
        cache.removeIf(c -> c.get() == null);
    }

    public U Register(U u) {
        clean();
        cache.add(new WeakReference<>(u));
        return u;
    }

    public Void CallOnAll(Function1<U, Void> fn) {
        return CallOn(null, fn);
    }

    public Void CallOn(Predicate<U> predicate, Function1<U, Void> fn) {
        MyApp.Companion.post(() -> {
            for (WeakReference<U> c : cache) {
                U u = c.get();
                if (u == null) {
                    continue;
                }
                if (predicate != null && !predicate.test(u)) {
                    continue;
                }
                fn.invoke(u);
            }
        });
        return null;
    }
}
