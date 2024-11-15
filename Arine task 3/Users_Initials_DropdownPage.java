package com.arine.automation.pages;

import com.arine.automation.drivers.DriverFactory;
import com.arine.automation.exception.AutomationException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.Arrays;
import java.util.List;

import static com.arine.automation.glue.CommonSteps.driverUtil;

public class Users_Initials_DropdownPage {
    Actions actions = new Actions(DriverFactory.drivers.get());

    public static final String USER_INITIALS_DROPDOWN = "//button[span[contains(text(), '(DD')]]";
    public static final String DROPDOWN_MENU_ITEMS = "//li[text()='Manage Access'] | //li[text()='Layout'] | //li[text()='Sign Out']";
    public static final String LAYOUT_FIELD = "//li[contains(text(), 'Layout')]";
    public static final String LAYOUT_OPTIONS = "//li[text()='Layout']//following-sibling::div//li[contains(text(), 'Lock Layout') or contains(text(), 'Unlock Layout') or contains(text(), 'Reset Layout')]";
    public static final String MANAGE_ACCESS_FIELD = "//li[contains(text(), 'Manage Access')]";
    public static final String MANAGE_ACCESS_SUBMENU_ITEMS = "//li[text()='Manage Access']//following-sibling::div//li[text()='MFA Settings' or text()='Register SSO with GOOGLE']";


    // Method to open the user initials dropdown
    public void openUserInitialsDropdown() throws AutomationException {
        WebElement dropdownButton = driverUtil.getWebElementAndScroll(USER_INITIALS_DROPDOWN);
        dropdownButton.click(); // Click to open the dropdown
    }

    // Method to verify that the dropdown contains the expected options
    public void verifyDropdownOptions(String options) throws AutomationException {
        List<String> expectedOptions = Arrays.asList(options.split(",\\s*"));
        List<WebElement> dropdownItems = driverUtil.getWebElements(DROPDOWN_MENU_ITEMS);

        if (dropdownItems == null) {
            throw new AutomationException("Dropdown items not found.");
        }

        for (String expectedOption : expectedOptions) {
            boolean optionFound = dropdownItems.stream()
                    .anyMatch(item -> item.getText().equalsIgnoreCase(expectedOption));
            if (!optionFound) {
                throw new AutomationException("Dropdown option '" + expectedOption + "' not found.");
            }
        }
    }


    // Method to click on a specific field in the dropdown
    public void clickOnField(String fieldName) throws AutomationException {
        String locator;

        if ("Layout".equalsIgnoreCase(fieldName)) {
            locator = LAYOUT_FIELD;
        } else if ("Manage Access".equalsIgnoreCase(fieldName)) {
            locator = MANAGE_ACCESS_FIELD;
        } else {
            throw new AutomationException("Field not recognized: " + fieldName);
        }

        WebElement field = driverUtil.getWebElement(locator);
        if (field == null) {
            throw new AutomationException("Field '" + fieldName + "' not found.");
        }
        field.click(); // Perform the click action
    }

    // Method to verify that the submenu options are displayed when hovering on the Layout field
    public void verifyDropdownSubmenuOptions(String fieldName, String expectedOptions) throws AutomationException {
        // Locate the submenu associated with the Layout field

        // Get the expected options
        List<String> expectedSubmenuOptions = Arrays.asList(expectedOptions.split(",\\s*"));

        // Perform hover action to display the submenu
        WebElement layoutField = driverUtil.getWebElementAndScroll(LAYOUT_FIELD);
        actions.moveToElement(layoutField).perform();

        // Wait for submenu to be visible (you might need to adjust wait time or implement a proper wait)
        driverUtil.applyWait(LAYOUT_OPTIONS, 5);

        // Get submenu items
        List<WebElement> submenuItems = driverUtil.getWebElements(LAYOUT_OPTIONS);
        if (submenuItems == null || submenuItems.isEmpty()) {
            throw new AutomationException("Submenu items not found for '" + fieldName + "' field.");
        }

        // Verify that each expected option is present in the submenu
        for (String expectedOption : expectedSubmenuOptions) {
            boolean optionFound = submenuItems.stream()
                    .anyMatch(item -> item.getText().equalsIgnoreCase(expectedOption));
            if (!optionFound) {
                throw new AutomationException("Submenu option '" + expectedOption + "' not found.");
            }
        }
    }


    public void verifyManageAccessSubmenuOptions(String option1, String option2) throws AutomationException {
        // Combine the options into a single string
        String expectedOptions = option1 + ", " + option2;

        // Split the expected options into a list
        List<String> expectedSubmenuOptions = Arrays.asList(expectedOptions.split(",\\s*"));

        // Perform hover action to display the submenu
        WebElement manageAccessField = driverUtil.getWebElementAndScroll(MANAGE_ACCESS_FIELD);
        actions.moveToElement(manageAccessField).perform();

        // Wait for submenu to be visible
        driverUtil.applyWait(MANAGE_ACCESS_SUBMENU_ITEMS, 5);

        // Get submenu items
        List<WebElement> submenuItems = driverUtil.getWebElements(MANAGE_ACCESS_SUBMENU_ITEMS);
        if (submenuItems == null || submenuItems.isEmpty()) {
            throw new AutomationException("Submenu items not found for 'Manage Access' field.");
        }

        // Verify that each expected option is present in the submenu
        for (String expectedOption : expectedSubmenuOptions) {
            boolean optionFound = submenuItems.stream()
                    .anyMatch(item -> item.getText().equalsIgnoreCase(expectedOption));
            if (!optionFound) {
                throw new AutomationException("Submenu option '" + expectedOption + "' not found.");
            }
        }
    }

}
