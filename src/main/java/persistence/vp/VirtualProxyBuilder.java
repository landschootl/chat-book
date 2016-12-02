package persistence.vp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Classe représentant le virtual proxy builder.
 *
 * @author Laurent THIEBAULT & Ludovic LANDSCHOOT (via Clément BALLABRIGA)
 */
public class VirtualProxyBuilder<T> implements InvocationHandler {
    T realObject = null;
    Factory<T> factory;
    Class<?> iface;

    public VirtualProxyBuilder(Class<?> iface, Factory<T> factory) {
        this.iface = iface;
        this.factory = factory;
    }

    public T getProxy() {
        @SuppressWarnings("unchecked")
        T r  = (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class<?>[] { iface }, this);
        return r;
    }

    /**
     * Méthode appelé lorsqu'on utilise l'object fonctory.
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (realObject == null) {
            realObject = factory.create();
        }
        return method.invoke(realObject, args);
    }
}
