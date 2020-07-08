package org.kaws.annotation;

import java.lang.annotation.*;


@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CallStatistics {

    StorageType value() default StorageType.MYSQL;

    boolean validate() default true;

}
