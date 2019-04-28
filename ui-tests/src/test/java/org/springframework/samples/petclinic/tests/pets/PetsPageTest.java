package org.springframework.samples.petclinic.tests.pets;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.logevents.SelenideLogger;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.tests.CiUiTest;

import io.qameta.allure.selenide.AllureSelenide;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.By.linkText;

public class PetsPageTest extends CiUiTest {
  @Test
  public void shouldCreatePet() {
    SelenideLogger.addListener("allure", new AllureSelenide().savePageSource(false));

    open("/");
    $("#username").val("test");
    $("#password").val("testovich");
    $("#login-button").click();
    $(byText("FIND OWNERS")).click();

    $(linkText("Jean Coleman")).click();
    $(byText("Add New Pet")).click();
    $("#name").val("Tom");
    $("#birth-date").val("2019-02-02");
    $(byText("Jean")).click();
    $("#type").selectOption("cat");
    $("#add-pet-button").click();

    ElementsCollection pets = $$("#pets-and-visits-table tbody tr:nth-child(3) td dl dd");
    pets.get(0).shouldHave(text("Tom"));
    pets.get(1).shouldHave(text("2019/02/02"));
    pets.get(2).shouldHave(text("cat"));

    SelenideLogger.removeListener("allure");
  }
}
