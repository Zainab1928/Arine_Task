package com.arine.automation.pages;

import com.arine.automation.drivers.DriverFactory;
import com.arine.automation.exception.AutomationException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MBISearchDiscrepancyPage extends PatientPage {

    Actions actions = new Actions(DriverFactory.drivers.get());

    private static final String FIRST_TABLE_ROW_XPATH = "//table//tr[td[contains(text(), '%s')]]";
    private static final String PATIENT_NAME_INPUT = "//div[contains(@class, 'src-routes-PharmacistPortal-LandingPage-components-units-PatientBanner-__patientName___')]";
    private static final String ARINE_ID_XPATH = "//label[contains(text(), 'Arine ID')]/span[contains(@class, 'src-routes-PharmacistPortal-LandingPage-components-units-PatientInput-__patientDetail__arineId___')]";
    private static final String RESULT = "//h3[contains(normalize-space(text()), 'Showing 1 result for HPID#:')]";
    private static final String PATIENT_NAME_XPATH = "//div[contains(@class, 'PatientBanner') and contains(text(), 'TestBonnie TestJackson')]";
    private static final String ADDRESS_INPUT_XPATH = "//label[@for='address2']/following-sibling::input[@id='address2']";
    private static final String FIRST_FIELD_XPATH = "//tr[contains(td[1], 'TestBonnie TestJackson')]";
    private static final String MBI_INPUT_XPATH = "//div[contains(@class, 'SearchPatient')]//input[@name='mbi' and preceding-sibling::text()[normalize-space()='MBI']]";
    private static final String mbiResultXPath = "//h3[contains(text(), 'Showing') and contains(text(), 'results for MBI')]";

    public void validateResultAndHPID(String expectedHPID) throws AutomationException {
        // Wait for the result element to be visible
        try {
            driverUtil.waitForVisibleElement(By.xpath(RESULT)); // Use RESULT variable
            WebElement resultElement = driverUtil.getWebElement(RESULT); // Use RESULT variable
            String actualResult = resultElement.getText();
            String actualHPID = actualResult.split("HPID#:")[1].trim();
            // Validate the HPID
            if (!actualHPID.equals(expectedHPID)) {
                throw new AutomationException("HPID mismatch! Expected: " + expectedHPID + " but found: " + actualHPID);
            }

        } catch (Exception e) {
            throw new AutomationException("Failed to validate HPID result: " + e.getMessage());
        }
    }

    public void editPatientDetails(String newAddress) throws AutomationException {
        // Wait for the first field in patient details to be clickable
        driverUtil.waitForElementClickable(By.xpath(FIRST_FIELD_XPATH));

        // Click on the first field using JavaScript
        driverUtil.clickUsingJavaScript(FIRST_FIELD_XPATH);

        // Wait for the patient name tab to be clickable and click on it using JavaScript
        driverUtil.waitForElementClickable(By.xpath(PATIENT_NAME_XPATH));
        driverUtil.clickUsingJavaScript(PATIENT_NAME_XPATH);

        // Wait for the address input to be clickable
        driverUtil.waitForElementClickable(By.xpath(ADDRESS_INPUT_XPATH));

        // Get the current value in the address input field
        WebElement addressInput = driverUtil.getWebElement(ADDRESS_INPUT_XPATH);
        String currentAddress = addressInput.getAttribute("value");

        // Update the address
        addressInput.clear();
        addressInput.sendKeys(newAddress);

        // Verify the address is updated
        String updatedAddress = addressInput.getAttribute("value");
        if (updatedAddress.equals(currentAddress)) {
            throw new AutomationException("Address not updated! Current: " + currentAddress + " New: " + updatedAddress);
        }
    }

    // Method to search patient by MBI
    public void searchPatientByMBI(String mbi) throws AutomationException {
        try {
            // Wait for MBI input field and enter MBI
            driverUtil.waitForVisibleElement(By.xpath(MBI_INPUT_XPATH));
            WebElement mbiInput = driverUtil.getWebElement(MBI_INPUT_XPATH);
            mbiInput.clear();
            mbiInput.sendKeys(mbi);

            // Press the Enter key to initiate the search
            mbiInput.sendKeys(Keys.ENTER);

        } catch (Exception e) {
            throw new AutomationException("Failed to search for patient by MBI: " + e.getMessage());
        }
    }

    public void validateMBIResult(String expectedMBI) throws AutomationException {
        // Wait for the MBI result element to be visible
        try {
            driverUtil.waitForVisibleElement(By.xpath(mbiResultXPath));
            WebElement mbiResultElement = driverUtil.getWebElement(mbiResultXPath);
            String actualResult = mbiResultElement.getText();

            // Extract the MBI ID from the result string
            String[] parts = actualResult.split("MBI:"); // Split by "MBI:"
            if (parts.length > 1) {
                String actualMBI = parts[1].trim(); // Get the part after "MBI:"

                // Validate the MBI
                if (!actualMBI.equals(expectedMBI)) {
                    throw new AutomationException("MBI mismatch! Expected: " + expectedMBI + " but found: " + actualMBI);
                }
            } else {
                throw new AutomationException("MBI ID not found in result: " + actualResult);
            }

        } catch (Exception e) {
            throw new AutomationException("Failed to validate MBI result: " + e.getMessage());
        }
    }

    public void checkAllInputFieldsDisabled() throws AutomationException {
        String[] inputFields = {
                ADVANCE_SEARCH_FIRST_NAME_INPUT,
                ADVANCE_SEARCH_LAST_NAME_INPUT,
                SEARCH_PATIENT_PHONE_INPUT,
                SEARCH_PATIENT_ZIP_CODE_INPUT,
                SEARCH_PATIENT_STATE_NAME_INPUT,
                SEARCH_PATIENT_CITY_NAME_INPUT,
                SEARCH_PATIENT_HPID_INPUT,
                ADVANCE_SEARCH_BIRTH_DATE_INPUT
        };

        boolean allDisabled = true; // Flag to track if all fields are disabled

        for (String xpath : inputFields) {
            WebElement inputField = driverUtil.getWebElement(xpath);
            if (inputField.isEnabled()) {
                allDisabled = false; // Found an enabled field
                break; // No need to check further
            }
        }

        if (!allDisabled) {
            throw new AutomationException("Not all input fields are disabled when MBI is entered.");
        }
    }

    // Method to click on the patient profile based on the patient name
    public void clickOnPatientProfile(String patientName) throws AutomationException {
        try {
            // Build the dynamic XPath using the patient name
            String dynamicPatientRowXPath = String.format(FIRST_TABLE_ROW_XPATH, patientName);

            // Wait for the specific row containing the patient name to be clickable and click it
            driverUtil.waitForElementClickable(By.xpath(dynamicPatientRowXPath));
            driverUtil.clickUsingJavaScript(dynamicPatientRowXPath);

        } catch (Exception e) {
            throw new AutomationException("Failed to click on patient profile: " + e.getMessage());
        }
    }

    // Method to verify the same patient profile is loaded
    public void verifySamePatientProfileIsLoaded() throws AutomationException {
        try {
            // Get the text of the patient ID input field
            WebElement patientIdInput = driverUtil.getWebElement(SEARCH_PATIENT_ID_INPUT);
            String patientId = patientIdInput.getAttribute("value").trim(); // Use getAttribute for input fields

            // Get the text of the Arine ID
            WebElement arineIdElement = driverUtil.getWebElement(ARINE_ID_XPATH);
            String arineId = arineIdElement.getText().trim();

            // Compare both IDs
            if (!patientId.equals(arineId)) {
                throw new AutomationException("Patient profile mismatch! Patient ID: " + patientId + " does not match Arine ID: " + arineId);
            }
        } catch (Exception e) {
            throw new AutomationException("Failed to verify the same patient profile is loaded: " + e.getMessage());
        }
    }

    public void editPatientCity(String newCity) throws AutomationException {
        // Wait for the patient name input to be clickable and then click it
        driverUtil.waitForElementClickable(By.xpath(PATIENT_NAME_INPUT));
        driverUtil.clickUsingJavaScript(PATIENT_NAME_INPUT);

        // Use getWebElementAndScroll to get the city input field and scroll to it
        WebElement cityInput = driverUtil.getWebElementAndScroll(SEARCH_PATIENT_CITY_NAME_INPUT, 10, "City input field not found or not clickable!");

        // Clear the existing value
        cityInput.clear(); // Clear existing value

        cityInput.sendKeys(Keys.chord(Keys.CONTROL, "a")); // Select all text
        cityInput.sendKeys(Keys.BACK_SPACE); // Clear selected text

        // Enter the new city value
        cityInput.sendKeys(newCity);

        // Optional: Wait for saving data after entering the new city value
        // Assuming 2 seconds is a sufficient time for the application to save the city
        driverUtil.waitForAWhile(4, TimeUnit.SECONDS); // Wait for 2 seconds

        // Verify the updated city value
        String updatedCity = cityInput.getAttribute("value");
        if (!updatedCity.equals(newCity)) {
            throw new AutomationException("City not updated! Expected: " + newCity + " but found: " + updatedCity);
        }
    }

    public void verifyCityUpdatedInPatientProfile(String expectedCity) throws AutomationException {
            // Wait and click on the patient name input field
            driverUtil.waitForElement(By.xpath(PATIENT_NAME_INPUT), 10); // Wait for patient name field
            driverUtil.clickUsingJavaScript(PATIENT_NAME_INPUT);

            // Wait for the city input field to be visible
            driverUtil.waitForElement(By.xpath(SEARCH_PATIENT_CITY_NAME_INPUT), 10);

            // Retrieve the city input field and scroll to it if necessary
            WebElement cityInput = driverUtil.getWebElementAndScroll(SEARCH_PATIENT_CITY_NAME_INPUT, 10, "City input field not found or not clickable!");

            // Optional: Brief wait to allow UI updates if needed
            driverUtil.waitForAWhile(500);

            // Retrieve and log the actual city value
            String actualCity = cityInput.getAttribute("value");
            System.out.println("Actual city after searching patient by MBI: " + actualCity); // Debug statement

            // Compare the actual city with the expected city
            if (!actualCity.equals(expectedCity)) {
                throw new AutomationException("City verification failed! Expected: " + expectedCity + ", but found: " + actualCity);
            }
    }

    public void verifyNoMBIFieldInAdvancedSearch() throws AutomationException {
        String mbiFieldText = "MBI"; // Adjust this string to the actual text of the MBI field
        WebElement mbiFieldElement = driverUtil.findElementByText(mbiFieldText);
        // Assert that the MBI field is not present
        if (mbiFieldElement != null) {
            throw new AutomationException("MBI field is unexpectedly present in the Advanced Search modal!");
        }
    }
}

