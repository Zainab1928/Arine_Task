package com.arine.automation.pages;

import com.arine.automation.drivers.DriverFactory;
import com.arine.automation.exception.AutomationException;
import com.arine.automation.util.ExcelUtil;
import com.arine.automation.util.WebDriverUtil;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class LoginFunctionalityPage extends LoginPage{

    Actions actions = new Actions(DriverFactory.drivers.get());

    public static final String loginDataFilePath = "src/test/resources/test-data/dev/loginData.xlsx";
    public static final String loginSheetName = "LoginSheet1";
    public static final String SEARCH_PATIENT_INPUT = "//*[contains(@class,'inputTextPatientId')]/..//input[contains(@class,'Input')]";

    public void validateLoginFunctionality(String userName, String password) throws AutomationException {
        // Check if user is already logged in and log out if needed
        if (driverUtil.getWebElementAndScroll(LOGOUT_BUTTON, WebDriverUtil.WAIT_1_SEC) != null) {
            doLogout();  // Log out if already logged in
        }

        // If username or password is not provided, fetch from Excel
        if ((userName == null || userName.isEmpty()) || (password == null || password.isEmpty())) {
            List<Map<String, String>> loginData = ExcelUtil.getLoginData(loginDataFilePath, loginSheetName);

            if (loginData != null && !loginData.isEmpty()) {
                userName = loginData.get(0).get("username");
                password = loginData.get(0).get("password");
            } else {
                throw new AutomationException("No login data found in Excel file.");
            }
        }

        // Proceed with login using the fetched credentials
        WebElement userNameInput = driverUtil.getWebElement(USER_NAME_INPUT);
        WebElement passwordInput = driverUtil.getWebElement(PASSWORD_INPUT);
        WebElement loginButton = driverUtil.getWebElement(LOGIN_BUTTON);

        // Fill out the login form
        userNameInput.clear();
        userNameInput.sendKeys(userName);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        loginButton.click();
    }



    // Method to search for patient by Patient ID, fetches Patient ID from Excel if not provided
    public void searchPatientById(String patientId) throws AutomationException {
        if (patientId == null || patientId.isEmpty()) {
            List<Map<String, String>> loginData = ExcelUtil.getLoginData(loginDataFilePath, loginSheetName);

            // Get Patient ID from Excel if data exists
            if (loginData != null && !loginData.isEmpty()) {
                patientId = loginData.get(2).get("PatientId"); // Adjust based on your Excel sheet's column header
            }
        }

        WebElement patientIdInput = driverUtil.getWebElement(SEARCH_PATIENT_INPUT); // Add the correct locator

        // Perform search actions
        patientIdInput.clear();
        patientIdInput.sendKeys(patientId);
        patientIdInput.sendKeys(Keys.ENTER);

    }
}




