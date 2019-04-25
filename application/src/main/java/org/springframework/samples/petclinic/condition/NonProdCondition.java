package org.springframework.samples.petclinic.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;

public class NonProdCondition implements Condition {
  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    return Arrays.stream(context.getEnvironment().getActiveProfiles())
        .noneMatch(profile -> profile.equals("prod"));
  }
}