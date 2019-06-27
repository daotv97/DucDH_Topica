package com.topica.annotations;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Size {
    public int min() default 0;
    public int max() default 0;
}
