package com.arine.automation.pages;

import com.arine.automation.drivers.DriverFactory;
import com.arine.automation.exception.AutomationException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

public class PharmacistPortalAdherancePage extends PatientPage {

    // Locators for elements
    public static final String DROPDOWN_AVAILABLE_ITEMS = "//div[@class='css-18ng2q5-group' and @id='react-select-3-group-1-heading']";
    public static final String SELECTED_MEDLIST_ITEM = "(//div[@class='css-1ndbt8o-multiValue'])/div[@class='css-12jo7m5']";
    public static final String SELECTED_MEDLIST_OPTIONS = "//*[text()='Selected']/following-sibling::div//label";
    public static final String NON_ADHERENCE_DROPDOWN = "//*[text()='Reasons for Non-adherence']/following-sibling::span//div[contains(@class,' css-117e4ry')]";
    public static final String CLEAR_SELECTION_BUTTON = "//span[contains(text(), 'Reasons for Non-adherence')]/following::div[contains(text(), 'Adherent - filled through cash')]/following-sibling::div";
    public static final String UNAVAILABLE_MEDLIST_OPTIONS = "//*[contains(text(),'Available options')]/following-sibling::div//label";
    public static final String REMOVE_BTN = "//div[contains(text(), 'Adherent - filled through cash')]/following-sibling::div[contains(@class, 'css-xb97g8')]";

    Actions actions = new Actions(DriverFactory.drivers.get());

    // Method to clear the dropdown and select a value
    public void selectDropdownValue(String dropdownValue) throws AutomationException, InterruptedException {
        WebElement clearSelectionBtn = driverUtil.getWebElementAndScroll(CLEAR_SELECTION_BUTTON);

        if (clearSelectionBtn != null) {
            clearSelectionBtn.click();
        }

        WebElement dropdown = driverUtil.getWebElementAndScroll(NON_ADHERENCE_DROPDOWN);
        if (dropdown == null) {
            throw new AutomationException(String.format("Unable to find dropdown: Select or Type..."));
        }

        dropdown.click();
        actions.sendKeys(Keys.ESCAPE);
        WebElement dropdownOption = driverUtil.getWebElementAndScroll("//*[text()='" + dropdownValue + "']", 2000);
        if (dropdownOption == null) {
            throw new AutomationException(String.format("Unable to find %s option in dropdown", dropdownValue));
        }

        dropdownOption.click();
        waitForLoadingPage();
    }

    // Method to verify selected elements in dropdown
    public void verifySelectedDropdownItems(String expectedValue) throws AutomationException {
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(expectedValue);

        WebElement dropdown = driverUtil.getWebElementAndScroll(NON_ADHERENCE_DROPDOWN);
        if (dropdown == null) {
            throw new AutomationException("Unable to find dropdown: Select or Type...");
        }

        List<WebElement> selectedItems = driverUtil.getWebElements(SELECTED_MEDLIST_ITEM);
        List<String> actualValues = new ArrayList<>();
        for (WebElement item : selectedItems) {
            actualValues.add(item.getText());
        }

        Assert.assertEquals(actualValues, expectedValues);
        System.out.println(actualValues + " | " + expectedValues);
    }

    // Method to verify elements available in dropdown list
    public void verifyDropdownAvailableItems(String expectedValue) throws AutomationException {
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(expectedValue);

        WebElement dropdown = driverUtil.getWebElementAndScroll(NON_ADHERENCE_DROPDOWN);
        if (dropdown == null) {
            throw new AutomationException("Unable to find dropdown: Select or Type...");
        }
        dropdown.click();

        List<WebElement> availableItems = driverUtil.getWebElements(SELECTED_MEDLIST_OPTIONS);
        List<String> actualValues = new ArrayList<>();
        for (WebElement item : availableItems) {
            actualValues.add(item.getText());
        }

        Assert.assertEquals(actualValues, expectedValues);
    }

    // Method to verify elements not available in dropdown list
    public void verifyDropdownUnavailableItems(String expectedValue) throws AutomationException {
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(expectedValue);

        WebElement dropdown = driverUtil.getWebElementAndScroll(NON_ADHERENCE_DROPDOWN);
        if (dropdown == null) {
            throw new AutomationException("Unable to find dropdown: Select or Type...");
        }
        dropdown.click();

        List<WebElement> unavailableItems = driverUtil.getWebElements(UNAVAILABLE_MEDLIST_OPTIONS);
        List<String> actualValues = new ArrayList<>();
        for (WebElement item : unavailableItems) {
            actualValues.add(item.getText());
        }

        SoftAssert softAssert = new SoftAssert();
        for (String value : expectedValues) {
            softAssert.assertFalse(actualValues.contains(value), "The value '" + value + "' is still present in the list.");
        }
        softAssert.assertAll();
    }

    // Method to remove an option from the dropdown
    public void removeDropdownOption(String option) throws AutomationException {
        // Find the remove button for the given option using a dynamic XPath
        WebElement removeBtn = driverUtil.getWebElementAndScroll(REMOVE_BTN);
        if (removeBtn == null) {
            throw new AutomationException("Unable to find remove button for option: " + option);
        }

        // Click the remove button
        removeBtn.click();

        // Optionally wait for the page or dropdown to refresh if necessary
        waitForLoadingPage();
    }

    // Method to verify that an option has been removed from the available items
    public void verifyDropdownOptionRemoved(String option) throws AutomationException {
        // First, click on the dropdown to reveal available items
        WebElement dropdown = driverUtil.getWebElementAndScroll(NON_ADHERENCE_DROPDOWN);
        if (dropdown == null) {
            throw new AutomationException("Unable to find dropdown: Select or Type...");
        }
        dropdown.click();

        // Get the available items after removal
        List<WebElement> availableItems = driverUtil.getWebElements(DROPDOWN_AVAILABLE_ITEMS);

        // Verify that the removed option is no longer in the list of available items
        for (WebElement item : availableItems) {
            if (item.getText().equals(option)) {
                throw new AutomationException("The option is still present: " + option);
            }
        }

        // If the option is not found, print success message
        System.out.println("The option '" + option + "' has been successfully removed.");
    }
}
