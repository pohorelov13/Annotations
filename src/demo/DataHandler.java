package demo;

import demo.anno.AfterSuite;
import demo.anno.BeforeSuite;
import demo.anno.Range;
import demo.anno.Test;
import demo.exceptions.IllegalNumberOfMethodsException;
import demo.exceptions.IllegalRangeOfPriorityException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHandler {

    private Object invoke(Object obj, Method method) throws IllegalRangeOfPriorityException {
        Object newObj = null;                               //get method annotations
        Annotation[] annotations = method.getAnnotations();
        if (annotations.length > 0) {
            for (Annotation annotation :
                    annotations) {
                if (annotation instanceof Test) {           //get annotations for annotation @Test
                    Annotation[] annoAnnotations = annotation.annotationType().getAnnotations();
                    if (annoAnnotations.length > 0) {
                        for (Annotation annoAnnotation :
                                annoAnnotations) {
                            if (annoAnnotation instanceof Range) {
                                if (((Test) annotation).priority() > ((Range) annoAnnotation).max()
                                        || ((Test) annotation).priority() < ((Range) annoAnnotation).min()) {
                                    throw new IllegalRangeOfPriorityException(
                                            "Priority is " + ((Test) annotation).priority()
                                            + ", but must be in range from "
                                            + ((Range) annoAnnotation).min() + " to " + ((Range) annoAnnotation).max());
                                }
                            }
                        }
                    }
                }
            }
        }
        try {
            newObj = method.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return newObj;
    }

    public void handleData(Class<?> someClass) {
        handleInvoke(BeforeSuite.class.getSimpleName(), someClass);
        handleInvoke(Test.class.getSimpleName(), someClass);
        handleInvoke(AfterSuite.class.getSimpleName(), someClass);
    }

    private void handleInvoke(String key, Class<?> someClass) {
        Object o;
        try {
            o = someClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        Map<String, List<Method>> listMap = fillMap(someClass);

        if (key.equals(AfterSuite.class.getSimpleName()) || key.equals(BeforeSuite.class.getSimpleName())) {
            if (listMap.get(key).size() == 1) {
                try {
                    listMap.get(key).get(0).invoke(o);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    throw new IllegalNumberOfMethodsException("In the class, the method with the annotation @"
                            + listMap.get(key).get(0).getAnnotations()[0]
                            .annotationType().getSimpleName()
                            + " occurs " + listMap.get(key).size() + " times");
                } catch (IllegalNumberOfMethodsException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (key.equals(Test.class.getSimpleName())) {
            List<Method> sortedTestList = listMap.get(Test.class.getSimpleName())
                    .stream()
                    .sorted((obj, obj2) -> obj.getAnnotation(Test.class).priority() -
                            obj2.getAnnotation(Test.class).priority())
                    .toList();
            for (Method method : sortedTestList) {
                try {
                    invoke(o, method);
                } catch (IllegalRangeOfPriorityException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Map<String, List<Method>> fillMap(Class<?> someClass) { //fill map
        Map<String, List<Method>> listMap = new HashMap<>();
        Method[] declaredMethods = someClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getAnnotations().length > 0) {
                String annoName = declaredMethod.getAnnotations()[0].annotationType().getSimpleName();
                if (!listMap.containsKey(annoName)) {
                    List<Method> methodList = new ArrayList<>();
                    methodList.add(declaredMethod);
                    listMap.put(annoName, methodList);
                } else {
                    listMap.get(annoName).add(declaredMethod);
                }
            }
        }
        return listMap;
    }
}