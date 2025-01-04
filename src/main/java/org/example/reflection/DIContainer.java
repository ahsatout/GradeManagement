package org.example.reflection;

import org.example.annotation.Component;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class DIContainer {

    private final Map<Class<?>, Object> components = new HashMap<>();

    public DIContainer(String... basePackages) throws Exception {
        // Loop through each base package and scan for @Component-annotated classes
        for (String basePackage : basePackages) {
            for (Class<?> clazz : PackageScanner.getClasses(basePackage)) {
                if (clazz.isAnnotationPresent(Component.class)) {
                    registerComponent(clazz);
                }
            }
        }
    }

    private void registerComponent(Class<?> clazz) throws Exception {
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length > 0) {
            Constructor<?> constructor = constructors[0];
            Object[] dependencies = new Object[constructor.getParameterCount()];
            for (int i = 0; i < constructor.getParameterCount(); i++) {
                dependencies[i] = getBean(constructor.getParameterTypes()[i]);
            }
            Object instance = constructor.newInstance(dependencies);
            components.put(clazz, instance);
        }
    }

    public <T> T getBean(Class<T> clazz) {
        return clazz.cast(components.get(clazz));
    }
}
