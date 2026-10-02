package kz.iitu.springlab01.audit;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Audited {

    String action();

    boolean logArguments() default false;
}