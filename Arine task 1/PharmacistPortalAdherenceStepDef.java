package com.arine.automation.glue;

import com.arine.automation.exception.AutomationException;
import com.arine.automation.pages.PageFactory;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import static com.arine.automation.glue.CommonSteps.takeScreenshot;

public class PharmacistPortalAdherenceStepDef {
    CommonSteps common = new CommonSteps();

    @And("User clicks on dropDown in Medlist page and selects {string} option")
    public void userClicksOnDropdownInMedlistPageAndSelectsOption(String dropdownValue) throws AutomationException, InterruptedException {
        common.logInfo("User clicks on dropDown in Medlist page and selects '" + dropdownValue + "'");
        PageFactory.pharmacistPortalAdherancePage().selectDropdownValue(dropdownValue);
        takeScreenshot();
    }

    @And("Verify {string} option in dropdown field present in Medlist tab")
    public void verifyOptionInDropdownFieldPresentInMedlistTab(String selectedValue) throws AutomationException {
        common.logInfo("User verifies the selected value '" + selectedValue + "' is displayed in the dropdown input box in Medlist page");
        PageFactory.pharmacistPortalAdherancePage().verifySelectedDropdownItems(selectedValue);
        takeScreenshot();
    }

    @And("Verify {string} option in dropdown list present in Medlist tab")
    public void verifyDropdownOptionsPresentInMedlistTab(String selectedValue) throws AutomationException {
        common.logInfo("User verifies that the selected value '" + selectedValue + "' is displayed in the dropdown list under the selected section in Medlist page");
        PageFactory.pharmacistPortalAdherancePage().verifyDropdownAvailableItems(selectedValue);
        takeScreenshot();
    }

    @And("Verify {string} option is not visible in dropdown list present in Medlist tab")
    public void verifyOptionIsNotVisibleInDropdownListPresentInMedlistTab(String selectedValue) throws AutomationException {
        common.logInfo("User verifies that the selected value '" + selectedValue + "' is not visible in the dropdown list under the available section in Medlist page");
        PageFactory.pharmacistPortalAdherancePage().verifyDropdownUnavailableItems(selectedValue);
        takeScreenshot();
    }

    @And("Remove {string} option from dropdown by clicking on remove button")
    public void removeOptionFromDropdown(String option) throws AutomationException {
        common.logInfo("User removes the '" + option + "' option from dropdown by clicking on the remove button");
        PageFactory.pharmacistPortalAdherancePage().removeDropdownOption(option);
        takeScreenshot();
    }

    @Then("Verify {string} option removed successfully from dropdown list")
    public void verifyOptionRemovedSuccessfullyFromDropdownList(String option) throws AutomationException {
        common.logInfo("User verifies that the '" + option + "' option has been successfully removed from the dropdown list");
        PageFactory.pharmacistPortalAdherancePage().verifyDropdownOptionRemoved(option);
        takeScreenshot();
    }
}
