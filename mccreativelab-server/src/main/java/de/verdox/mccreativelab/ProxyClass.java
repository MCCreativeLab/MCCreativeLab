package de.verdox.mccreativelab;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used as a proxy for other objects.
 * We primarily use a proxy to intercept or change the behavior of various classes in nms
 * @param <T> the generic type to proxy
 */
public class ProxyClass<T> implements MethodInterceptor {
    private final T target;
    private final Map<String, Map<Class<?>[], Method>> cachedMethods = new HashMap<>();

    public static <T> T proxy(T object) {
        ProxyClass<T> proxyClass = new ProxyClass<>(object);
        return proxyClass.createProxy();
    }

    private ProxyClass(T target) {
        this.target = target;
        for (Method declaredMethod : this.target.getClass().getDeclaredMethods()) {
            cachedMethods.computeIfAbsent(declaredMethod.getName(), s -> new HashMap<>())
                    .putIfAbsent(declaredMethod.getParameterTypes(), declaredMethod);
        }
    }

    protected T createProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy proxy) throws Throwable {
        if (this.cachedMethods.containsKey(method.getName())) {
            Map<Class<?>[], Method> methodsWithSameParameterTypes = this.cachedMethods.get(method.getName());
            if (methodsWithSameParameterTypes.containsKey(method.getParameterTypes())) {
                Method foundMethod = methodsWithSameParameterTypes.get(method.getParameterTypes());
                foundMethod.invoke(this, objects);
            }
        }
        return proxy.invokeSuper(target, objects);
    }
}
