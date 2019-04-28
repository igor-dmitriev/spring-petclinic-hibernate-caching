package org.springframework.samples.petclinic.tests.pets;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.database.rider.junit5.DBUnitExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.samples.petclinic.steps.MainSteps;
import org.springframework.samples.petclinic.tests.CiUiTest;

import io.qameta.allure.selenide.AllureSelenide;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ExtendWith(DBUnitExtension.class)
public class PetsPageTest extends CiUiTest {
  private MainSteps mainSteps = new MainSteps(homePath(), apiLoginPath());
  public ConnectionHolder connectionHolder = () -> dataSource().getConnection();

  @Test
  @DataSet(
      value = {
          "datasets/test_user.xml",
          "datasets/pets/owner-to-create-pet.xml"
      },
      executeScriptsBefore = "datasets/cleanup.sql",
      strategy = SeedStrategy.INSERT
  )
  public void shouldCreatePet() {
    SelenideLogger.addListener("allure", new AllureSelenide().savePageSource(false)); // tracing

    /*open("/");
    $("#username").val("test");
    $("#password").val("testovich");
    $("#login-button").click();
    $(linkText("FIND OWNERS")).click();

    $(linkText("Jean Coleman")).click();
    $(byText("Add New Pet")).click();
    */
    mainSteps.fastLogin();
    Selenide.open("/#/owners/1000/pets/new"); // fast open

    $("#name").val("Dan");
    $("#birth-date").val("2019-02-02");
    $(byText("Jean")).click();
    $("#type").selectOption("dog");
    $("#add-pet-button").click();

    ElementsCollection pets = $$("#pets-and-visits-table tbody tr:nth-child(1) td dl dd");
    pets.get(0).shouldHave(text("Dan"));
    pets.get(1).shouldHave(text("2019/02/02"));
    pets.get(2).shouldHave(text("dog"));

    SelenideLogger.removeListener("allure");
  }
}
