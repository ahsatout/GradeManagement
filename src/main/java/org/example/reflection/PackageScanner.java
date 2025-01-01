package org.example.reflection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PackageScanner {

    public static List<Class<?>> getClasses(String packageName) throws Exception {
        String path = packageName.replace('.', '/');
        File directory = new File(ClassLoader.getSystemClassLoader().getResource(path).getFile());
        List<Class<?>> classes = new ArrayList<>();

        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().replace(".class", "");
                classes.add(Class.forName(className));
            }
        }

        return classes;
    }
}

