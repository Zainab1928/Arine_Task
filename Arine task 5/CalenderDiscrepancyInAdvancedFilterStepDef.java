package com.arine.automation.glue;

import com.arine.automation.exception.AutomationException;
import com.arine.automation.pages.PageFactory;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

import java.text.ParseException;
import static com.arine.automation.glue.CommonSteps.takeScreenshot;

public class CalenderDiscrepancyInAdvancedFilterStepDef {

    CommonSteps common = new CommonSteps();
   @Then("Verify the User are able select the campaigns value as: \"([^\"]*)\"$")
    public void userSelectCampaignDate(String campaignsDate) throws AutomationException {
        common.logInfo("User select campaigns value as: "+campaignsDate);
       PageFactory.calenderDiscrepancyInAdvancedFilterPage().selectCampaignDropdownValue(campaignsDate);
    }

    @And("^Select the reported drp table filter \"([^\"]*)\" as \"([^\"]*)\" and click on \"([^\"]*)\" button$")
    public void selectTaskFilter(String filterName,String filterOption,String buttonName) throws AutomationException, ParseException {
        common.logInfo("Select reported drp table filter '" + filterName + "' as '"+filterOption+"' and click on '"+buttonName+"' button");
        PageFactory.calenderDiscrepancyInAdvancedFilterPage().taskFilterSelect(filterName,filterOption,buttonName);
        takeScreenshot();
    }

    @And("^Verify \"([^\"]*)\" column only \"([^\"]*)\" values in reports drp table$")
    public void verifyColumnContainsValuesInReportsDRPTable(String columnName,String filterOption) throws AutomationException, ParseException {
        common.logInfo("Verify '" + columnName + "' column contains only '"+filterOption+"' values in reports table");
        PageFactory.calenderDiscrepancyInAdvancedFilterPage().verifyColumnValuesInReportsDRPTable(columnName,filterOption);
        takeScreenshot();
    }
}
