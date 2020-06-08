package org.springframework.samples.petclinic.web;

import org.springframework.context.annotation.Conditional;
import org.springframework.samples.petclinic.condition.NonProdCondition;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//@Profile("!prod") https://stackoverflow.com/questions/25427684/using-profile-in-spring-boot
@Conditional(NonProdCondition.class)
public class TestController {

  @GetMapping("/test")
  public String test() {
    return "test.html";
  }
}
