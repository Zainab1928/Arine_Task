package com.arine.automation.pages;
import com.arine.automation.drivers.DriverFactory;
import com.arine.automation.exception.AutomationException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import static com.arine.automation.glue.CommonSteps.driverUtil;
import static com.arine.automation.pages.BasePage.waitForLoadingPage;
import static com.arine.automation.pages.PractitionersPage.REPORT_DRP_TABLE_COLUMN_DATA;
import static com.arine.automation.pages.PractitionersPage.getReportedDRPTableColumnHeadingIndex;

public class CalenderDiscrepancyInAdvancedFilterPage {

    public static final String DATE_BUTTON = "//*[contains(@class,'mantine-DatePickerInput')]//label[text()='%s']";
    public static final String CALENDER_BUTTON = "//*[contains(@class,'mantine-DatePickerInput-calendarHeaderLevel')]";
    public static final String YEAR_SELECTION = "//*[contains(@class,'mantine-DatePickerInput-yearsList')]/..//button[text()='%s']";
    public static final String MONT_DATE_SELECTION = "//*[contains(@class,'mantine-DatePickerInput-month')]/..//button[text()='%s']";
    public static final String REPORTED_DRP_ADVANCE_FILTER_DROPDOWN = "//*[text()='%s']//following-sibling::*//input[contains(@placeholder,'%s')]";
    public static final String REPORTED_DRP_ADVANCE_FILTER_DROPDOWN_OPTION = "//*[@role='option' and text()='%s']";
    public static final String REPORTED_DRP_ADVANCE_FILTER_INPUT = "//*[text()='%s']//following-sibling::*/input[contains(@placeholder,'%s')]";
    public static final String CAMPAIGNS_SELECT_DROPDOWN = "//input[contains(@class,'Select-input') and @placeholder='Pick one']";
    public static String CAMPAIGNS_SELECT_OPTION_IN_DROPDOWN = "//*[contains(@class,'mantine-Select-item') and text()='%s']";
    String finalDate, fromDate = null, currentDay = null;


    public void selectCampaignDropdownValue(String campaignDate) throws AutomationException {
        waitForLoadingPage();
        WebElement campaignButton = driverUtil.getWebElement(CAMPAIGNS_SELECT_DROPDOWN);
        if (campaignButton == null)
            throw new AutomationException(String.format("Campaigns dropdown not displayed on screen"));
        campaignButton.click();

        WebElement campaignDateElement = driverUtil.getWebElement(String.format(CAMPAIGNS_SELECT_OPTION_IN_DROPDOWN, campaignDate));
        if (campaignDateElement == null)
            throw new AutomationException(String.format("%s Campaigns Value not present in campaigns dropdown", campaignButton));
        campaignDateElement.click();
        waitForLoadingPage();
    }

    public void taskFilterSelect(String filterName, String filterOption, String buttonName) throws AutomationException, ParseException {
        Actions actions = new Actions(DriverFactory.drivers.get());
        WebElement selectTaskFilterIcon = driverUtil.getWebElement(PageFactory.prescriberAnalyticsPage().CAMPAIGNS_REPORTS_FILTER_SEARCH_ICON);
        if (selectTaskFilterIcon == null)
            throw new AutomationException("No filter search icon visible in Campaigns tab!");
        selectTaskFilterIcon.click();

        if (filterName.equalsIgnoreCase("Algorithm Impact") || filterName.equalsIgnoreCase("Resolved") || filterName.equalsIgnoreCase("Implementation Status")) {
            WebElement campaignAdvanceFilterDropDown = driverUtil.getWebElement(String.format(REPORTED_DRP_ADVANCE_FILTER_DROPDOWN, filterName, filterName));
            if (campaignAdvanceFilterDropDown == null)
                throw new AutomationException("No '" + filterName + "' advance filter dropdown defined in campaign filter options!");
            campaignAdvanceFilterDropDown.click();
            WebElement campaignAdvanceFilterDropDownOption = driverUtil.getWebElement(String.format(REPORTED_DRP_ADVANCE_FILTER_DROPDOWN_OPTION, filterOption));
            if (campaignAdvanceFilterDropDownOption == null)
                throw new AutomationException("No '" + filterName + "' campaign Advance Filter DropDown Option defined in campaign filter options!");
            campaignAdvanceFilterDropDownOption.click();
            actions.sendKeys(Keys.ESCAPE).build().perform();
        } else if (filterName.equalsIgnoreCase("Report Sent Date") || filterName.equalsIgnoreCase("Report Created Date")) {
            WebElement element = driverUtil.getWebElement(String.format(DATE_BUTTON + "//following-sibling::div/button", filterName), 10);
//            element = driverUtil.getWebElement(String.format(DATE_BUTTON+"//following-sibling::div/button", dateType), 10);

            if (element != null) {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                String currentDate = formatter.format(date);
                Calendar cal = Calendar.getInstance();
                if (filterOption.equalsIgnoreCase("future date")) {
                    cal.add(Calendar.DAY_OF_MONTH, +2);
                    Date nextDate = cal.getTime();
                    SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
                    currentDate = format.format(nextDate);
                } else {
                    cal.add(Calendar.DAY_OF_MONTH, -(365 - 2));
                    Date frmDate = cal.getTime();
                    SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
                    fromDate = format.format(frmDate);
                }
                finalDate = fromDate + "-" + currentDate;
                element.click();
                String dateRange[] = finalDate.split("-");
                for (int i = 0; i < dateRange.length; i++) {
                    String dateVal = dateRange[i];
                    DriverFactory.drivers.get().findElement(By.xpath(CALENDER_BUTTON)).click();
                    DriverFactory.drivers.get().findElement(By.xpath(CALENDER_BUTTON)).click();
                    String yearValue = DriverFactory.drivers.get().findElement(By.xpath(CALENDER_BUTTON)).getText();
                    String years[] = yearValue.split(" – ");
                    String values[] = dateVal.split("\\s");
                    if (Integer.parseInt(values[2].trim()) > Integer.parseInt(years[1].trim()))
                        DriverFactory.drivers.get().findElement(By.xpath(CALENDER_BUTTON + "//following-sibling::button")).click();
                    else if (Integer.parseInt(values[2].trim()) < Integer.parseInt(years[0].trim()))
                        DriverFactory.drivers.get().findElement(By.xpath(CALENDER_BUTTON + "//preceding-sibling::button")).click();

                    DriverFactory.drivers.get().findElement(By.xpath(String.format(YEAR_SELECTION, values[2]))).click();
                    DriverFactory.drivers.get().findElement(By.xpath(String.format(MONT_DATE_SELECTION, values[1]))).click();
                    if (values[0].startsWith("0"))
                        currentDay = values[0].replace("0", "");
                    else
                        currentDay = values[0];
                    DriverFactory.drivers.get().findElement(By.xpath(String.format(MONT_DATE_SELECTION, currentDay))).click();
                }
            } else {
                throw new AutomationException(filterName + " unable to find records on Reported DRPS Table Advanced filter: ");
            }
        } else {
            WebElement campaignAdvanceFilterInput = driverUtil.getWebElementAndScroll(String.format(REPORTED_DRP_ADVANCE_FILTER_INPUT, filterName, filterName));
            if (campaignAdvanceFilterInput == null)
                throw new AutomationException("No '" + filterName + "' filter dropdown defined in Task page filter options!");
            campaignAdvanceFilterInput.click();
            campaignAdvanceFilterInput.clear();
            campaignAdvanceFilterInput.sendKeys(filterOption);
        }
        actions.sendKeys(Keys.ESCAPE).build().perform();
        clickOnButtonPresentInPractitionersTab(buttonName);
        waitForLoadingPage();
    }

    public void clickOnButtonPresentInPractitionersTab(String text) throws AutomationException {
        WebElement button = driverUtil.getWebElementAndScroll(String.format(PrescriberAnalyticsPage.CAMPAIGNS_TAB_BUTTONS, text));
        if (button == null)
            throw new AutomationException(String.format("Unable to find %s button on patient tab or it might taking too long time to load!", text));
        button.click();
        waitForLoadingPage();
    }

    public void verifyColumnValuesInReportsDRPTable(String columnName, String filterOption) throws AutomationException, ParseException {
        WebElement tableHeading = driverUtil.getWebElement("//table//thead//div[contains(@class,'TableHeadCell-Content-Wrapper') and text()='" + columnName + "']");
        if (tableHeading == null)
            throw new AutomationException(columnName + " Column Name not visible in reports table!");
        int columnIndex = getReportedDRPTableColumnHeadingIndex(columnName);
        List<WebElement> columnData = driverUtil.getWebElements(String.format(REPORT_DRP_TABLE_COLUMN_DATA, columnIndex));
        List columnCellDataList = new ArrayList();
        System.out.println("columnIndex== " + columnIndex);
        for (int i = 0; i < columnData.size(); i++) {
            columnCellDataList.add(columnData.get(i).getText());
            String columnCellData = columnData.get(i).getText();

            if (filterOption.equalsIgnoreCase("Report Sent Date") || filterOption.equalsIgnoreCase("Report Created Date")) {
                Date columnCellDateData = new SimpleDateFormat("dd/MM/yyyy").parse(columnCellData);
                if (columnName.equalsIgnoreCase("Last Faxed")) {
                    Date expectedDateRange1 = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);
                    Date expectedDateRange2 = new SimpleDateFormat("dd/MM/yyyy").parse(finalDate);
                    Date expectedDateRange1PreviousDay = new Date(expectedDateRange1.getTime() - 1);
                    Date expectedDateRange2PreviousDay = new Date(expectedDateRange2.getTime() + 1);

                    if (!columnCellDateData.after(expectedDateRange1PreviousDay) && !columnCellDateData.before(expectedDateRange2PreviousDay) && columnCellDateData != null)
                        throw new AutomationException("In " + columnName + " Column only " + filterOption + " this data should be displayed but we found " + columnData.get(i).getText() + " as well");
                } else if (!columnCellData.contains(filterOption) && !columnCellData.isEmpty())
                    throw new AutomationException("In " + columnName + " Column only " + filterOption + " this data should be displayed but we found " + columnData.get(i).getText() + " as well");
            }
        }
    }

}
