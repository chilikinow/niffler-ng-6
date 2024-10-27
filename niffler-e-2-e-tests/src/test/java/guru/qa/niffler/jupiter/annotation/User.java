package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.SpendingExtension;
import guru.qa.niffler.jupiter.extension.UserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({UserExtension.class, CategoryExtension.class, SpendingExtension.class})
public @interface User {
  String username() default "";

  int friends() default 0;  // Количество друзей

  int incomeInvitations() default 0;  // Количество входящих приглашений

  int outcomeInvitations() default 0;  // Количество исходящих приглашений

  Category[] categories() default {};

  Spending[] spendings() default {};
}