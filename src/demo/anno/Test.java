package demo.anno;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Range
public @interface Test {
    int priority() default 1;

}
