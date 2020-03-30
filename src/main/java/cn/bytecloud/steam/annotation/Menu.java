package cn.bytecloud.steam.annotation;

import org.apache.shiro.authz.annotation.Logical;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Menu {
    String value() default "";

}