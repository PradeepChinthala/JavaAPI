package project.pageobjects;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;

import project.variables.ProjectVariables;

public class AddPresentationProfile extends PageObject {

	String btnAddPresentationProfilestr = "//button[@class='jqx-rc-all jqx-button jqx-widget jqx-fill-state-normal']//mat-icon[contains(text(),\"add\")]";
	
	@FindAll({
		@FindBy(xpath="//button[@class='jqx-rc-all jqx-button jqx-widget jqx-fill-state-normal']//mat-icon[contains(text(),\"add\")]"),
		@FindBy(xpath="//button[@class='jqx-rc-all jqx-button jqx-widget jqx-fill-state-normal jqx-fill-state-focus']//mat-icon[contains(text(),'add')]")
	})
	WebElementFacade btnAddPresentationProfile;
	
	@FindBy(xpath = "//input[@id='nameInput']")
	WebElementFacade txtPresentationName;

	@FindBy(xpath = "//input[@id='mat-input-0']")
	WebElementFacade dateTargetDate;

	@FindBy(xpath = "//*[@id='mat-checkbox-1']/label/div")
	WebElementFacade chkboxPayerShortSelectAll;

	@FindBy(xpath = "//*[@id='mat-checkbox-2']/label/div")
	WebElementFacade chkboxLOBSelectAll;

	@FindBy(xpath = "//div[ contains( text(),'Outpatient')]")
	WebElementFacade chkboxSelectOutPatient;

	@FindBy(xpath = "//*[@id='jqxWidgetfcb0c975c836']/div[2]/div/div/div[6]/mat-selection-list/mat-list-option[1]/div/mat-pseudo-checkbox")
	WebElementFacade chckboxPriorityThresholdHigh;

	@FindBy(xpath = "//*[@id='jqxWidgetaa347e0881ca']/div/textarea")
	WebElementFacade txtPresntationProfileNotes;

	@FindBy(xpath = "//jqxbutton[@id='popupContainer']/button[contains(text(),'Cancel')]")
	WebElementFacade btnCancelAddPresentationProfile;

	@FindBy(xpath = "//button[contains(text(),'OK')]")
	WebElementFacade btnOKAddPresentationProfile;

	@FindBy(xpath = "//*[@id='mat-dialog-1']/app-cpd-dialog/div[1]/p")
	WebElementFacade popupOKAddPresentationProfile;

	@FindBy(xpath = "//div[@role = 'dialog']//div[@class='jqx-window-content jqx-widget-content jqx-rc-b']")
	WebElementFacade popupCancelAddPresentationProfile;

	@FindBy(xpath = "//div[@role = 'dialog']//div[@class='jqx-window-content jqx-widget-content jqx-rc-b']//button[contains(text(),'Yes')]")
	WebElementFacade btnYesCancelAddPresentationProfile;

	@FindBy(xpath = "//div[@role = 'dialog']//div[@class='jqx-window-content jqx-widget-content jqx-rc-b']//button[contains(text(),'No')]")
	WebElementFacade btnNoCancelAddPresentationProfile;

	@FindBy(xpath = "(//div[@class='pres-deck-container-class']//*[@id='lblName'])")
	List<WebElementFacade> namePresentationProfile;

	@FindBy(xpath = "//*[@id='mat-dialog-0']/app-cpd-dialog/div[1]/p")
	WebElementFacade popMsgPresentationProfileCreated;

	@FindBy(xpath = "//*[@id='mat-dialog-0']/app-cpd-dialog/div[2]/button/span")
	WebElementFacade popMsgOkPresentationProfileCreated;
	
	@FindBy(xpath = "//div[@class='mat-calendar-body-cell-content mat-calendar-body-selected mat-calendar-body-today']")
	WebElementFacade calendartodayDate;

	@FindBy(xpath = "(//button[@aria-label='Open calendar'])")
	List<WebElementFacade> calendar;

	@FindBy(xpath = "(//div[@class='form-control jqx-widget jqx-checkbox']//b[contains(text(),'Payer Shorts')])")
	List<WebElementFacade> chkboxselectAllPayerShort;
	
	@FindBy(xpath = "(//div[@class='form-control jqx-widget jqx-checkbox']//b[contains(text(),'LOB')])")
	List<WebElementFacade> chkboxselectAllLOB;
	
	@FindBy(xpath = "//jqxcheckbox//div[contains(text(),'Outpatient')]")
	List<WebElementFacade> chkboxOutpatient;
	
	@FindBy(xpath = "//jqxcheckbox//div[contains(text(),'Professional')]")
	List<WebElementFacade> chkboxProfessional;

	@FindBy(xpath = "//jqxcheckbox//div[contains(text(),'High')]")
	List<WebElementFacade> chkboxPriorityHigh;

	@FindBy(xpath = "//jqxcheckbox//div[contains(text(),'Medium')]")
	List<WebElementFacade> chkboxPriorityMedium;

	@FindBy(xpath = "//jqxcheckbox//div[contains(text(),'Low')]")
	List<WebElementFacade> chkboxPriorityLow;

	@FindBy(xpath = "//textarea[@class='jqx-text-area-element jqx-widget jqx-widget-content']")
	List<WebElementFacade> txtNotes;

	private final String presProfileCollapseStatus = "//div[@class='pres-deck-container-class']//div[@class='prestest']/jqxexpander/div[@class='jqx-widget jqx-expander']/div[1]";

	@FindBy(xpath = "//div[@role='dialog']//div[contains(text(),'Are you sure you want to cancel?')]")
	List<WebElementFacade> popCancelMsg;
	
	@FindBy(xpath = "//div[@role='dialog']//div[contains(text(),'Are you sure you want to cancel?')]//parent::div//button[contains(text(),'Yes')]")
	List<WebElementFacade> popCancelMsgYesButton ;

	@FindBy(xpath = "//div[@role='dialog']//div[contains(text(),'Are you sure you want to cancel?')]//parent::div//button[contains(text(),'NO')]")
	List<WebElementFacade> popCancelMsgNOButton ;
	
	
//	@FindBy(xpath = "")
//	WebElementFacade ;
	
	
	public boolean user_clicks_on_sign_on_the_Header_section_from_right_top_corner() {
//		WebElementFacade btnAddPresentationProfile;
//		btnAddPresentationProfile = find(By.xpath(btnAddPresentationProfilestr));
//		PageFactory.initElements(getDriver(), this.btnAddPresentationProfile);
		btnAddPresentationProfile.click();
		return true;
	}

	public boolean cancel_button_should_be_enabled() {
		return btnCancelAddPresentationProfile.isEnabled();
	}

	public boolean ok_button_should_be_disabled() {
		return !btnOKAddPresentationProfile.isEnabled();
	}

	public void populate_presentation_name_to_add_presentation_profile() {
		ProjectVariables.uniquePresentationProfile = "testProfile" + System.currentTimeMillis();
		txtPresentationName.sendKeys(ProjectVariables.uniquePresentationProfile);
	}

	public boolean ok_button_should_be_enabled() {
		return btnOKAddPresentationProfile.isEnabled();
	}

	public boolean user_clicks_on_OK_button() {
		btnOKAddPresentationProfile.click();
		return true;
	}

	public boolean validate_presentation_profile_created_in_previous_step() {
				return namePresentationProfile.get(0).getText().equals(ProjectVariables.uniquePresentationProfile);
	}

	public boolean user_validates_presentation_profile_created_success_message(String arg1) {
				return popMsgPresentationProfileCreated.getText().equals(arg1);
	}

	public boolean user_clicks_ok_on_presentation_profile_success_message() {
		popMsgOkPresentationProfileCreated.click();
		return true;
	}

	public void populate_all_fields_on__presentation_profile() {
		populate_presentation_name_to_add_presentation_profile();
		calendar.get(0).click();
		calendartodayDate.click();
		chkboxselectAllPayerShort.get(0).click();
		chkboxselectAllLOB.get(0).click();
		chkboxOutpatient.get(0).click();
		chkboxProfessional.get(0).click();
		chkboxPriorityHigh.get(0).click();
		txtNotes.get(0).sendKeys(ProjectVariables.uniquePresentationProfile);
	}

	public boolean user_clicks_on_Cancel_button() {
		btnCancelAddPresentationProfile.click();
		return true;
	}

	public String verify_Presentation_section_is_collapsed_or_expanded() {
		List<WebElement> e = getDriver().findElements(By.xpath(presProfileCollapseStatus)); 
		if(e.get(0).getAttribute("class").toString().equals("jqx-widget-header jqx-expander-header jqx-expander-header-disabled jqx-fill-state-normal"))
			return "collapsed";
		else if(e.get(0).getAttribute("class").toString().equals("jqx-widget-header jqx-expander-header jqx-expander-header-disabled jqx-fill-state-pressed jqx-expander-header-expanded"))
			return "expanded";
		return "error";
	}

	public boolean system_should_alert_the_user_with_Yes_and_No_Buttons() {
		return (popCancelMsg.get(0).isDisplayed() && popCancelMsgYesButton.get(0).isDisplayed() && popCancelMsgNOButton.get(0).isDisplayed());
	}

	public boolean user_clicks_on_on_Confirmation_pop_up(String arg1) {
		if(arg1.equals("Yes")){
				popCancelMsgYesButton.get(0).click();
				return true;
		}else if(arg1.equals("NO")){
			popCancelMsgNOButton.get(0).click();
			return true;
		}
		return false;
	}
}
