package com.arine.automation.glue;

import com.arine.automation.drivers.DriverFactory;
import com.arine.automation.exception.AutomationException;
import com.arine.automation.pages.LoginFunctionalityPage;
import com.arine.automation.pages.LoginPage;
import com.arine.automation.util.ExcelUtil;
import com.arine.automation.util.WebDriverUtil;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import com.arine.automation.pages.PageFactory;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static com.arine.automation.glue.CommonSteps.logInfo;
import static com.arine.automation.glue.CommonSteps.takeScreenshot;

public class LoginFunctionalityStepDef extends LoginPage {

    CommonSteps common = new CommonSteps();

    @Given("^User launched Browser \"([^\"]*)\"$")
    public void launchBrowser(String browser) throws AutomationException {
        logInfo("User launched " + browser);
        if (DriverFactory.drivers.get() == null) {
            WebDriver driver = DriverFactory.getInstance().initDriver(browser);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
            driver.manage().window().maximize();

            // Initialize PageFactory and WebDriverUtil without iTestContext
            PageFactory.init();
            driverUtil = new WebDriverUtil();
        }
    }

    @When("User login by {string} and {string}")
    public void userLoginByCredentials(String username, String password) throws AutomationException {
        // If username and password are empty, fetch them from Excel
        if (username == null || username.isEmpty()) {
            List<Map<String, String>> loginData = ExcelUtil.getLoginData(LoginFunctionalityPage.loginDataFilePath, LoginFunctionalityPage.loginSheetName);
            if (loginData != null && !loginData.isEmpty()) {
                username = loginData.get(0).get("username");
            } else {
                throw new AutomationException("No username found in Excel file.");
            }
        }
        if (password == null || password.isEmpty()) {
            List<Map<String, String>> loginData = ExcelUtil.getLoginData(LoginFunctionalityPage.loginDataFilePath, LoginFunctionalityPage.loginSheetName);
            if (loginData != null && !loginData.isEmpty()) {
                password = loginData.get(0).get("password");
            } else {
                throw new AutomationException("No password found in Excel file.");
            }
        }
        // Perform the login with fetched or provided values
        PageFactory.loginFunctionality().validateLoginFunctionality(username, password);
        takeScreenshot();
    }

    @And("Search patient by: {string}")
    public void userSearchPatientById(String patientId) throws AutomationException {
        // Call the searchPatientById method from LoginFunctionality page class
        PageFactory.loginFunctionality().searchPatientById(patientId);
        takeScreenshot();
    }
}
