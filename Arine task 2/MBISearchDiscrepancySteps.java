package com.arine.automation.glue;

import com.arine.automation.exception.AutomationException;
import com.arine.automation.pages.MBISearchDiscrepancyPage;
import com.arine.automation.pages.PageFactory;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static com.arine.automation.glue.CommonSteps.takeScreenshot;
import static org.testng.AssertJUnit.assertEquals;

public class MBISearchDiscrepancySteps {

    CommonSteps common = new CommonSteps();

    @And("^Successfully show the patient matching the inputted HPID from the search results.\"(.*)\"$")
    public void checkTheHPIDResult(String expectedHPID) throws AutomationException {
        PageFactory.mbiSearchDiscrepancyPage().validateResultAndHPID(expectedHPID);
        common.logInfo("the HPID shows the result for HPID:'" + expectedHPID + "' is displayed");
        takeScreenshot();
    }

    @Then("Verify user able to Edit the Patients demographic details with new address {string}")
    public void verifyUserAbleToEditPatientDemographicDetails(String newAddress) throws AutomationException {
        PageFactory.mbiSearchDiscrepancyPage().editPatientDetails(newAddress);
        takeScreenshot();
    }

    @When("^User search patient by MBI: \"(.*)\"$")
    public void searchPatientByMBI(String mbi) throws AutomationException {
        PageFactory.mbiSearchDiscrepancyPage().searchPatientByMBI(mbi);
        takeScreenshot();
    }

    @And("^Successfully show the patient matching the inputted MBI from the search results.\"(.*)\"$")
    public void checkTheMBIResult(String expectedMBI) throws AutomationException {
        PageFactory.mbiSearchDiscrepancyPage().validateMBIResult(expectedMBI);
        common.logInfo("The MBI result shows for MBI '" + expectedMBI + "' is displayed");
        takeScreenshot();
    }

    @Then("Check other input fields are disabled when enter MBI id")
    public void checkOtherInputFieldsAreDisabled() throws AutomationException {
        PageFactory.mbiSearchDiscrepancyPage().checkAllInputFieldsDisabled(); // Check if all fields are disabled
        common.logInfo("other input fields are disabled when enter MBI id");
        takeScreenshot();
    }

    @And("Click on patient profile: {string}")
    public void clickOnPatientProfile(String patientName) throws AutomationException {
        PageFactory.mbiSearchDiscrepancyPage().clickOnPatientProfile(patientName);
        takeScreenshot();
    }

    @Then("Verify the same patient profile is loaded")
    public void verifySamePatientProfileIsLoaded() throws AutomationException {
        PageFactory.mbiSearchDiscrepancyPage().verifySamePatientProfileIsLoaded();
        common.logInfo("same patient profile is loaded! Patient ID: \" + patientId + \" match with Arine ID: \" + arineId");
        takeScreenshot();
    }

    @Then("Verify user able to Edit the Patients demographic details with new city {string}")
    public void verifyUserAbleToEditPatientDetails(String newCity) throws AutomationException {
        PageFactory.mbiSearchDiscrepancyPage().editPatientCity(newCity);
        takeScreenshot();
    }

    @Then("Verify the same patient profile is loaded with the updated city field {string}")
    public void verifySamePatientProfileWithUpdatedCityField(String city) throws AutomationException {
        PageFactory.mbiSearchDiscrepancyPage().verifyCityUpdatedInPatientProfile(city);
        common.logInfo("same patient profile is loaded with the updated city field: \" + expectedCity + \", and found: \" + actualCity");
        takeScreenshot();
    }

    @Then("Verify there is no MBI field in the Advanced Search modal")
    public void verifyNoMBIFieldInAdvancedSearch() throws AutomationException {
        PageFactory.mbiSearchDiscrepancyPage().verifyNoMBIFieldInAdvancedSearch();
        common.logInfo("Verification successful: No MBI field is present in the Advanced Search modal.");
        takeScreenshot();
    }



//    @And("Check patient profile results on the Patient Advanced Search Modal")
//    public void checkPatientProfileResults() throws AutomationException {
//        PageFactory.mbiSearchDiscrepancyPage().checkPatientProfileResults();
//        takeScreenshot();
//    }
//
//    @And("Click on the patient profile")
//    public void clickOnPatientProfile() throws AutomationException {
//        PageFactory.mbiSearchDiscrepancyPage().clickOnPatientProfile();
//        takeScreenshot();
//    }
//
//    @Then("Verify the same patient profile is loaded")
//    public void verifySamePatientProfileIsLoaded() throws AutomationException {
//        PageFactory.mbiSearchDiscrepancyPage().verifySamePatientProfileIsLoaded();
//        takeScreenshot();
//    }


}
