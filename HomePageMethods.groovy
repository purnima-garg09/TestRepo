package com.volaris.web
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import internal.common.utility.AccessibilityMethods
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.volaris.web.checkpoints.HomePageCheckpoints
import internal.GlobalVariable
import internal.common.utility.CommonMethods
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
public class HomePageMethods{
	WebDriver driver;
	CommonMethods commonMethods = new CommonMethods()
	HomePageCheckpoints homepageCheckpoint= new HomePageCheckpoints()
	AccessibilityMethods accessibilityMethods= new AccessibilityMethods()
	boolean calendarDate


	public homePage() {
		GlobalVariable.OS = 'Web'
		accessibilityMethods.accessibilityMethods("Home Page")
		commonMethods.FlightType()
		login()
		selectCultureCurrency()
		closeCookies()
		AcceptAllCookies()
		selectTripType()
		selectOriginDestination()
		selectDates()
		enterPaxDetails()
	}

	public vClubhomePage() {
		GlobalVariable.OS = 'Web'
		accessibilityMethods.accessibilityMethods("Home Page")
		selectCultureCurrency()
		closeCookies()
		AcceptAllCookies()
		selectTripType()
		selectOriginDestination()
		selectDates()
		enterPaxDetails()
	}

	public login() {
		if (GlobalVariable.userType.equalsIgnoreCase("Agency") | GlobalVariable.userType.equalsIgnoreCase("Vclub") | GlobalVariable.username.equalsIgnoreCase("Invex")) {
			WebUI.waitForElementPresent(findTestObject('MBS/Web/HomePage/Login/LoginButton'), 5)
			WebUI.click(findTestObject('MBS/Web/HomePage/Login/LoginButton'))
			if(GlobalVariable.userType.equalsIgnoreCase("Agency")) {
				WebUI.waitForElementClickable(findTestObject('MBS/Web/HomePage/Login/Agency_Radio'), 5)
				//WebUI.click(findTestObject('MBS/Web/HomePage/Login/Agency_Radio'))
				commonMethods.jseClick("(//div[@class='mat-radio-outer-circle'])[2]")
				WebUI.setText(findTestObject('MBS/Web/HomePage/Login/Agency_User'), GlobalVariable.username)
			} else {
				WebUI.setText(findTestObject('MBS/Web/HomePage/Login/VClub_User'), GlobalVariable.username)
			}
			WebUI.setText(findTestObject('MBS/Web/HomePage/Login/Password'), GlobalVariable.password)
			WebUI.click(findTestObject('MBS/Web/HomePage/Login/AccessButton'))
			WebUI.waitForElementNotPresent(findTestObject('MBS/Web/HomePage/Login/AccessButton'), 20)
			WebUI.verifyElementNotPresent(findTestObject('MBS/Web/HomePage/Login/LoginButton'), 5,FailureHandling.STOP_ON_FAILURE)
			commonMethods.waitForLoaderInvisible()
		}
	}

	public void selectCultureCurrency() {
		//will update this together with the menu
		//homepageCheckpoint.verifyDefaultBanner()
		//whole menu needs update
		homepageCheckpoint.verifyHomePageCulture()
		selectCulture()
		if(!GlobalVariable.userType.equals("Agency")) {
			commonMethods.getCurrencyFromList()
		}
		WebUI.click(findTestObject('MBS/Web/HomePage/Culture_Currency/ApplyButton'))
		commonMethods.waitForLoaderInvisible()
		WebUI.waitForElementClickable(findTestObject('MBS/Web/HomePage/OneWay'), 30, FailureHandling.OPTIONAL)
		homepageCheckpoint.verifyHomePageCulture()
		homepageCheckpoint.verifyCulture()
		homepageCheckpoint.verifyCurrency()
		homepageCheckpoint.verifyMenu()
	}

	public void selectCulture() {
		String originalculture = GlobalVariable.culture
		String mcpvalue = WebUI.getText(findTestObject('MBS/Web/HomePage/Culture_Currency/MCP'))
		WebUI.delay(0.5)
		WebUI.click(findTestObject('MBS/Web/HomePage/Culture_Currency/Culture_Currency'))
		if(mcpvalue.contains("English")){
			WebUI.selectOptionByLabel(findTestObject('MBS/Web/HomePage/Culture_Currency/Culture'), GlobalVariable.culture, false)
		}
		else if(mcpvalue.contains("Español")){
			if(originalculture.contains("English")){
				if(GlobalVariable.uri.equalsIgnoreCase('https://pre-web.volaris.com/') | GlobalVariable.uri.equalsIgnoreCase('https://volaris.com/')) {
					originalculture = "Inglés"
				}
				WebUI.selectOptionByLabel(findTestObject('MBS/Web/HomePage/Culture_Currency/Culture'), originalculture, false)
			} else if(originalculture.contains("Español")){
				WebUI.selectOptionByLabel(findTestObject('MBS/Web/HomePage/Culture_Currency/Culture'), originalculture, false)
			}
		}
	}

	public void closeCookies() {
		if(WebUI.waitForElementPresent(findTestObject('MBS/Web/HomePage/Cookies'), 1)) {
			WebUI.click(findTestObject('MBS/Web/HomePage/Cookies'), FailureHandling.OPTIONAL)
		}
	}
	
	public void AcceptAllCookies() {
		String xpath = '//*[@id="onetrust-accept-btn-handler"]';
		TestObject cookieButton = new TestObject().addProperty("xpath", ConditionType.EQUALS, xpath);
	
		try {
			if (WebUI.waitForElementPresent(cookieButton, 3)) {
				WebUI.comment('Accept All Cookies Found');
				WebUI.click(cookieButton, FailureHandling.OPTIONAL);
				WebUI.comment('Clicked');
			} else {
				WebUI.comment('Accept All Cookies Not Found');
			}
		} catch (Exception e) {
			WebUI.comment('Failed Checking: ' + e.getMessage());
		}
	}

	public selectTripType() {
		if(GlobalVariable.trip.equalsIgnoreCase('OneWay')) {
			commonMethods.jseClick("//mat-button-toggle[@class='mat-button-toggle mat-button-toggle-appearance-standard']//div[@class='mat-button-toggle-label-content']")
			homepageCheckpoint.verifySigleTrip()
		}
	}

	public selectOriginDestination() {
		if(!GlobalVariable.origin.equalsIgnoreCase('MEX')) {
			origin()
			WebUI.setText(findTestObject('MBS/Web/HomePage/toStation'), GlobalVariable.destination)
			destination()
		} else {
			origin()
			commonMethods.jseClick("//div[@class='col btnSearch SearchDestination']//input")
			WebUI.setText(findTestObject('MBS/Web/HomePage/toStation'), GlobalVariable.destination)
			destination()
		}
	}

	public void origin() {
		driver = DriverFactory.getWebDriver();
		for(int i=1; i<=5; i++) {
			commonMethods.jseClick("//div[@class='col btnSearch SearchOrigin']//input")
			WebUI.setText(findTestObject('MBS/Web/HomePage/fromStation'), GlobalVariable.origin)
			try {
				homepageCheckpoint.verifyOriginTyping()
				driver.findElement(By.xpath("//div[contains(text(),'" + GlobalVariable.origin + "')]")).click();
				break
			} catch (Throwable e) {
				WebUI.delay(3)
				driver.findElement(By.xpath("//mat-icon[contains(text(),'close')]")).click();
				WebUI.delay(3)
			}
		}
	}

	public void destination() {
		driver = DriverFactory.getWebDriver();
		homepageCheckpoint.verifyDestinationTyping()
		driver.findElement(By.xpath("//div[contains(text(),'" + GlobalVariable.destination + "')]")).click();
		commonMethods.waitForLoaderInvisible()
	}

	public selectDates() {
		driver = DriverFactory.getWebDriver();
		commonMethods.journeyDate()
		commonMethods.waitForLoaderInvisible()
		homepageCheckpoint.verifyIfLoaderIsVisibleTwice()
		WebUI.waitForElementPresent(findTestObject('MBS/Web/HomePage/CalendarAutoFocus'), 20, FailureHandling.OPTIONAL)
		homepageCheckpoint.verifyCalendarTagline()
		homepageCheckpoint.verifylowFareAvailability()
		WebUI.delay(1)
		try {
			for (int i = 1; i <= 18; i++) {
				List<WebElement> departure = driver.findElements(By.xpath("//td[starts-with(@class,'today datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'weekend datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'today active start-date end-date datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'today active start-date active end-date datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'today weekend active start-date end-date datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'today weekend active start-date active end-date datecell-" + GlobalVariable.departureDate + "')]"));
				println("//td[starts-with(@class,'today datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'weekend datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'today active start-date end-date datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'today active start-date active end-date datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'today weekend active start-date end-date datecell-" + GlobalVariable.departureDate + "')]")
				if(departure.empty) {
					List<WebElement> Monthlist = driver.findElements(By.xpath("//div[@class='mat-tab-label-content']/div[@class='ng-star-inserted' or @class='ng-star-inserted active-month']"))
					for(int k=0; k<Monthlist.size();k++) {
						Monthlist.get(k).click()
						WebUI.delay(10)
						List<WebElement> CheckDept = driver.findElements(By.xpath("//td[starts-with(@class,'today datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'weekend datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'today active start-date end-date datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'today active start-date active end-date datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'today weekend active start-date end-date datecell-" + GlobalVariable.departureDate + "') or starts-with(@class,'today weekend active start-date active end-date datecell-" + GlobalVariable.departureDate + "')]"));
						if(!CheckDept.empty) {
							CheckDept.get(0)
							break
						}
					}
					//WebUI.click(findTestObject('MBS/Web/HomePage/NextMonthCalendar'))
					commonMethods.waitForLoaderInvisible()
				} else {
					for(WebElement elee : departure) {
						if(elee.isDisplayed()) {
							WebUI.delay(2)
							elee.click();
							if(GlobalVariable.trip.equalsIgnoreCase('OneWay')) {
								WebUI.click(findTestObject('MBS/Web/HomePage/button_Done'))
							}
						}
					}
					break
				}
			}
			if(GlobalVariable.trip.equalsIgnoreCase('RoundTrip')) {
				commonMethods.waitForLoaderInvisible()
				WebUI.waitForElementPresent(findTestObject('MBS/Web/HomePage/CalendarAutoFocus'), 15, FailureHandling.OPTIONAL)
				homepageCheckpoint.verifyReturnlowFareAvailability()
				WebUI.delay(1)
				for (int j = 1; j <= 18; j++) {
					List<WebElement> returns = driver.findElements(By.xpath("//td[starts-with(@class,'today datecell-" + GlobalVariable.returnDate + "') or starts-with(@class,'datecell-" + GlobalVariable.returnDate + "') or starts-with(@class,'weekend datecell-" + GlobalVariable.returnDate + "')]"));
					if(returns.empty) {
						WebUI.click(findTestObject('MBS/Web/HomePage/NextMonthCalendar'))
						commonMethods.waitForLoaderInvisible()
					} else {
						for(WebElement elee : returns) {
							if(elee.isDisplayed()) {
								elee.click();
								WebUI.click(findTestObject('MBS/Web/HomePage/button_Done'))
							}
						}
						break
					}
				}
			}
		} catch(Throwable e) {
			if(WebUI.waitForElementVisible(findTestObject('MBS/Web/HomePage/NoFlightAvailable'), 2,)) {
				println("*** Fight not available ***")
			}
			WebUI.click(findTestObject('MBS/Web/HomePage/button_Done'))
		}
	}

	public void enterPaxDetails() {
		driver = DriverFactory.getWebDriver();
		int ad = Integer.parseInt(GlobalVariable.adult)
		int ch = Integer.parseInt(GlobalVariable.child)
		int bby = Integer.parseInt(GlobalVariable.baby)
		commonMethods.jseClick("//shared-passenger-count-summary//span[@class='icon-arrow_down']")
		WebUI.waitForElementPresent(findTestObject('MBS/Web/HomePage/paxPrompt'), 2, FailureHandling.OPTIONAL)
		homepageCheckpoint.verifyPaxLabels()
		if(ad.equals(0)) {
			homepageCheckpoint.verifyBaby()
			WebUI.click(findTestObject('MBS/Web/HomePage/Passenger/Adult_0'))
		}
		if(!ad.equals(0) && !ad.equals(1)) {
			homepageCheckpoint.verifyBaby()
			for(int i = 1; i < ad; i++) {
				WebUI.click(findTestObject('MBS/Web/HomePage/Passenger/Adult'))
			}
		}
		if(!ch.equals(0)) {
			for(int i = 0; i < ch; i++) {
				WebUI.click(findTestObject('MBS/Web/HomePage/Passenger/Child'))
			}
		}
		if(!bby.equals(0)) {
			for(int i = 0; i < bby; i++) {
				WebUI.delay(1)
				commonMethods.jseClick("(//span[@class='icon-positive'])[3]")
			}
		}
		homepageCheckpoint.verifyMexicanPassports()
		commonMethods.jseClick("//div[@class='passenger-type ui-widget-content ibe']")
		homepageCheckpoint.verifyPaxInput()
		homepageCheckpoint.verifyLinkedInIcon()
		homepageCheckpoint.getSelectedOriginAndDestination()
		homepageCheckpoint.getDepartureAndRetunDate()
		homepageCheckpoint.verifyCalendarMonthOnHomePageAfterDateSelected()
		homepageCheckpoint.verifyHeaderAfterScrollDown()
		commonMethods.jseClick("//button[contains(@class,'search-btn')]")
		WebUI.waitForElementClickable(findTestObject('MBS/Web/FlightSelect/DeptartureFare'), 15, FailureHandling.OPTIONAL)
	}
}