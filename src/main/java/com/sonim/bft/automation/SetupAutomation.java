package com.sonim.bft.automation;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.collect.ImmutableMap;
import com.sonim.bft.restapi.model.Status;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class SetupAutomation extends Thread {
	private String deviceName;
	private String deviceId;
	private String androidVersion;
	private String appuimIP;
	private String appiumPort;
	private String wifiName;
	private String wifiPwd;
	private String gmailId;
	private String gmailPwd;
	DesiredCapabilities caps;
	AppiumDriver<MobileElement> driver = null;
	List<String> script;
	Map<String, Object> command;
	List<MobileElement> li;

	public SetupAutomation(String deviceName, String deviceId, String androidVersion, String appuimIP,
			String appiumPort, String wifiName, String wifiPwd, String gmailId, String gmailPwd) {
		this.deviceName = deviceName;
		this.deviceId = deviceId;
		this.androidVersion = androidVersion;
		this.appuimIP = appuimIP;
		this.appiumPort = appiumPort;
		this.wifiName = wifiName;
		this.wifiPwd = wifiPwd;
		this.gmailId = gmailId;
		this.gmailPwd = gmailPwd;

	}

	public void run() {
		try {
			Status.MESSAGE = new StringBuffer("Starting setup with device " + this.deviceId);
			System.out.println("Starting setup with device ");
			for (int i = 1; i < 7; i++) {
				if (this.deviceSetup())
					break;
				else if (i == 6) {
					System.out.println("Not able to perfoem task. Stopped setup wizaerd");
					return;
				}
				System.out.println("Opening communication channel. Pass " + i + " retrying......");
				Thread.sleep(1000 * 30);
			}
			System.out.println("Completed setup with device ");
			Status.MESSAGE = new StringBuffer("Updating settings and permissions : " + this.deviceId);
			System.out.println("Updating settings and permissions");
			for (int i = 1; i < 4; i++) {
				if (this.updateSettings())
					break;
				else if (i == 3) {
					System.out.println("Not able to perfoem task. St0pped setup wizaerd");
					return;
				}
				System.out.println("Pass " + i + " retrying......");
				Thread.sleep(1000 * 20);
			}
			System.out.println("Setup completed");
			Status.MESSAGE = new StringBuffer("Creating gmail : " + this.deviceId);
			System.out.println("Setup gmail");
			for (int i = 1; i < 4; i++) {
				if (this.gmailSetup())
					break;
				else if (i == 3) {
					System.out.println("Not able to perfoem task. Stopped setup wizaerd");
					return;
				}
				System.out.println("Pass " + i + " retrying......");
				Thread.sleep(1000 * 20);
			}
			System.out.println("Setup gmail completed");
			TestAutomation automation = new TestAutomation();
			automation.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Removing screen lock form setup
		/*
		 * // Swipe up Thread.sleep(1000 * 2); script = Arrays.asList("tap",
		 * "470 1560"); command = ImmutableMap.of("command", "input", "args", script);
		 * driver.executeScript("mobile: shell", command);
		 * 
		 * // Swipe down to select setting Thread.sleep(1000 * 2); script =
		 * Arrays.asList("swipe", "540 1900 540 1 2000"); command =
		 * ImmutableMap.of("command", "input", "args", script);
		 * driver.executeScript("mobile: shell", command); Thread.sleep(1000 * 3);
		 * driver.executeScript("mobile: shell", command);
		 * 
		 * // Tap on Settings Thread.sleep(1000 * 5); script = Arrays.asList("tap",
		 * "45 1200"); command = ImmutableMap.of("command", "input", "args", script);
		 * driver.executeScript("mobile: shell", command);
		 * 
		 * // Swipe down for System script = Arrays.asList("swipe",
		 * "540 1900 540 1 2000"); command = ImmutableMap.of("command", "input", "args",
		 * script); driver.executeScript("mobile: shell", command); Thread.sleep(1000 *
		 * 3); driver.executeScript("mobile: shell", command);
		 * 
		 * // Tap on Security Thread.sleep(1000 * 5); script = Arrays.asList("tap",
		 * "220 460"); command = ImmutableMap.of("command", "input", "args", script);
		 * driver.executeScript("mobile: shell", command);
		 * 
		 * // Tap on Screen lock Thread.sleep(1000 * 2); script = Arrays.asList("tap",
		 * "216 1210"); command = ImmutableMap.of("command", "input", "args", script);
		 * driver.executeScript("mobile: shell", command);
		 * 
		 * // Tap on None Thread.sleep(1000 * 2); script = Arrays.asList("tap",
		 * "10 250"); command = ImmutableMap.of("command", "input", "args", script);
		 * driver.executeScript("mobile: shell", command);
		 * 
		 * // Back to Home script = Arrays.asList("keyevent", "KEYCODE_HOME"); command =
		 * ImmutableMap.of("command", "input", "args", script);
		 * driver.executeScript("mobile: shell", command);
		 * 
		 * // Open gmail // google folder script = Arrays.asList("tap", "45 1190");
		 * command = ImmutableMap.of("command", "input", "args", script);
		 * driver.executeScript("mobile: shell", command);
		 * 
		 * // gmail icon folder script = Arrays.asList("tap", "500 680"); command =
		 * ImmutableMap.of("command", "input", "args", script);
		 * driver.executeScript("mobile: shell", command);
		 * 
		 * // tap got it script = Arrays.asList("tap", "10 1760"); command =
		 * ImmutableMap.of("command", "input", "args", script);
		 * driver.executeScript("mobile: shell", command);
		 * 
		 * // Take me to gmail script = Arrays.asList("tap", "10 1760"); command =
		 * ImmutableMap.of("command", "input", "args", script);
		 * driver.executeScript("mobile: shell", command);
		 * 
		 * // Back to Home script = Arrays.asList("keyevent", "KEYCODE_HOME"); command =
		 * ImmutableMap.of("command", "input", "args", script);
		 * driver.executeScript("mobile: shell", command);
		 * 
		 * // if(Status.DEVICE1.toString().indexOf(deviceId)>0) // Status.DEVICE1 = new
		 * StringBuffer("Finished"); //adb shell input swipe 540 1900 540 1 2000 // else
		 * // Status.DEVICE2 = new StringBuffer("Finished"); //
		 * if(Status.STATUS.toString().equals("Setup Finisehd")) // return; //
		 * if(Status.DEVICE1.toString().equals("Finished") &&
		 * Status.DEVICE2.toString().equals("Finished")) { // Status.STATUS = new
		 * StringBuffer("Setup Finisehd"); // TestAutomation automation =new
		 * TestAutomation(); // automation.run(); // } Status.MESSAGE = new
		 * StringBuffer("Completed setup with device " + this.deviceId); TestAutomation
		 * automation = new TestAutomation(); automation.start();
		 * 
		 * } catch (Exception e) { System.out.println(e.getMessage()); } finally { //
		 * driver.close(); }
		 */
	}

	public boolean deviceSetup() {
		caps = new DesiredCapabilities();
		try {
			caps.setCapability("deviceName", this.deviceName);
			caps.setCapability("udid", this.deviceId); // Give Device ID of your mobile phone
			caps.setCapability("platformName", "Android");
			caps.setCapability("platformVersion", this.androidVersion);
			caps.setCapability("appPackage", "com.google.android.setupwizard");
			caps.setCapability("appActivity", "SetupWizardActivity");
			caps.setCapability("noReset", "true");

			// Instantiate Appium Driver
			driver = new AndroidDriver<MobileElement>(new URL("http://" + appuimIP + ":" + appiumPort + "/wd/hub"),
					caps);
			System.out.println("Started setup process");
			// System.out.println("Appium start : " + appuimIP + ":" + appiumPort);
			driver.findElementById("com.google.android.setupwizard:id/start").click();
			// driver.findElementById("com.google.android.setupwizard:id/skip_button").click();
			Thread.sleep(1000 * 5);
			driver.findElementById("com.google.android.setupwizard:id/flow_choice_new").click();
			
			//**********Use this block for wifi setp, and comment below setup skip code block *********************
			// Select and configure Wifi
			Thread.sleep(1000 * 8);

			/*
			 * List<MobileElement> li =
			 * driver.findElementsByClassName("android.widget.TextView"); for (int i = 0; i
			 * < li.size(); i++) { if (li.get(i).getText().toString().equals(this.wifiName))
			 * {// ASUS_2.4 Hyconosys-Wi-Fi li.get(i).click(); break; } if (i == li.size() -
			 * 1) {
			 * System.out.println("Wifi connection failed. Please check wifi name '"+this.
			 * wifiName+" is available"); return false; } }
			 * driver.findElementById("com.android.settings:id/password").setValue(this.
			 * wifiPwd); // Cts0T3amS0n1mGM5! // // a1b2c3d4e5
			 * driver.findElementById("android:id/button1").click(); Thread.sleep(1000 *
			 * 35);
			 */			
			
			//**********Use this block for skip wifi setp, and comment above code block *********************
			driver.findElementById("com.google.android.setupwizard:id/skip_button").click();
			driver.findElementById("android:id/button1").click();
			// date time next button
//			Thread.sleep(1000 * 5);
//			driver.findElementById("com.google.android.setupwizard:id/next_button").click();			
			// name next button
			Thread.sleep(1000 * 5);
			driver.findElementById("com.google.android.setupwizard:id/next_button").click();
			
			//not now
			Thread.sleep(1000 * 2);
			li = driver.findElementsByClassName("android.widget.TextView");
			for (int i = 0; i < li.size(); i++) {
				if (li.get(i).getText().toString().indexOf("Not now") > -1) {
					li.get(i).click();
					break;
				}
				if (i == li.size() - 1)
					return false;
			}
			Thread.sleep(1000 * 1);
			driver.findElementById("android:id/button1").click();
			
			// Tap More
			Thread.sleep(1000 * 5);
			script = Arrays.asList("tap", "760 1745");
			command = ImmutableMap.of("command", "input", "args", script);
			driver.executeScript("mobile: shell", command);
			// Tap More
			driver.executeScript("mobile: shell", command);
			// Tap Accept
			driver.executeScript("mobile: shell", command);
			
			// tap no thanks
			Thread.sleep(1000 * 2);
			driver.findElementById("com.google.android.setupwizard:id/skip_button").click();

			// Tap Next
			Thread.sleep(1000 * 5);
			script = Arrays.asList("tap", "760 1755");
			command = ImmutableMap.of("command", "input", "args", script);
			driver.executeScript("mobile: shell", command);
			Thread.sleep(1000 * 5);
			/*
			 * // Mail id frame List<String> script = Arrays.asList("tap", "80 630");
			 * Map<String, Object> command = ImmutableMap.of("command", "input", "args",
			 * script); driver.executeScript("mobile: shell", command); script =
			 * Arrays.asList("text", this.gmailId); command = ImmutableMap.of("command",
			 * "input", "args", script); driver.executeScript("mobile: shell", command);
			 * script = Arrays.asList("keyevent", "KEYCODE_BACK"); command =
			 * ImmutableMap.of("command", "input", "args", script);
			 * driver.executeScript("mobile: shell", command); script = Arrays.asList("tap",
			 * "750 1740"); command = ImmutableMap.of("command", "input", "args", script);
			 * driver.executeScript("mobile: shell", command); // Password frame
			 * Thread.sleep(1000 * 5); script = Arrays.asList("tap", "80 645"); command =
			 * ImmutableMap.of("command", "input", "args", script);
			 * driver.executeScript("mobile: shell", command); script =
			 * Arrays.asList("text", this.gmailPwd); command = ImmutableMap.of("command",
			 * "input", "args", script); driver.executeScript("mobile: shell", command);
			 * script = Arrays.asList("keyevent", "KEYCODE_BACK"); command =
			 * ImmutableMap.of("command", "input", "args", script);
			 * driver.executeScript("mobile: shell", command); script = Arrays.asList("tap",
			 * "750 1740"); command = ImmutableMap.of("command", "input", "args", script);
			 * driver.executeScript("mobile: shell", command);
			 * 
			 * // welcome window Tap I agree Thread.sleep(1000 * 5); script =
			 * Arrays.asList("tap", "750 1740"); command = ImmutableMap.of("command",
			 * "input", "args", script); driver.executeScript("mobile: shell", command);
			 */
			// not now tap
			/*
			 * Thread.sleep(1000 * 30); script = Arrays.asList("tap", "80 1359"); command =
			 * ImmutableMap.of("command", "input", "args", script);
			 * driver.executeScript("mobile: shell", command);
			 * 
			 * // tap skip anyway script = Arrays.asList("tap", "630 1125"); command =
			 * ImmutableMap.of("command", "input", "args", script);
			 * driver.executeScript("mobile: shell", command);
			 * 
			 * // Tap More Thread.sleep(1000 * 5); script = Arrays.asList("tap",
			 * "760 1745"); command = ImmutableMap.of("command", "input", "args", script);
			 * driver.executeScript("mobile: shell", command); // Tap More
			 * driver.executeScript("mobile: shell", command); // Tap Accept
			 * driver.executeScript("mobile: shell", command);
			 * 
			 * // Tap Continue // Thread.sleep(1000 * 25); // script = Arrays.asList("tap",
			 * "1000 1865"); // command = ImmutableMap.of("command", "input", "args",
			 * script); // driver.executeScript("mobile: shell", command);
			 * 
			 * // Tap No thanks Thread.sleep(1000 * 3);
			 * driver.findElementById("com.google.android.setupwizard:id/skip_button").click
			 * ();
			 * 
			 * // Tap Next Thread.sleep(1000 * 5); script = Arrays.asList("tap",
			 * "760 1755"); command = ImmutableMap.of("command", "input", "args", script);
			 * driver.executeScript("mobile: shell", command);
			 */
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			// e.printStackTrace();
			return false;
		} finally {
			// driver.quit();
		}
		return true;
	}

	public boolean updateSettings() {
		caps = new DesiredCapabilities();
		try {
			caps.setCapability("deviceName", this.deviceName);
			caps.setCapability("udid", this.deviceId); // Give Device ID of your mobile phone
			caps.setCapability("platformName", "Android");
			caps.setCapability("platformVersion", this.androidVersion);
			caps.setCapability("appPackage", "com.android.settings");
			caps.setCapability("appActivity", ".Settings");
			caps.setCapability("noReset", "true");

			// Instantiate Appium Driver
			driver = new AndroidDriver<MobileElement>(new URL("http://" + appuimIP + ":" + appiumPort + "/wd/hub"),
					caps);
			// select Security
			Thread.sleep(1000 * 5);
			script = Arrays.asList("start", "-a android.settings.SECURITY_SETTINGS");
			command = ImmutableMap.of("command", "am", "args", script);
			driver.executeScript("mobile: shell", command);

//			li = driver.findElementsByClassName("android.widget.TextView");
//			for (int i = 0; i < li.size(); i++) {
//				if (li.get(i).getText().toString().indexOf("Security")>-1) {
//					li.get(i).click();
//					break;
//				}
//				return;
//			}
			// select screen lock
			li = driver.findElementsByClassName("android.widget.TextView");
//			li.get(9).click();
			for (int i = 0; i < li.size(); i++) {
				if (li.get(i).getText().toString().indexOf("Screen lock") > -1) {
					li.get(i).click();
					break;
				}
				if (i == li.size() - 1)
					return false;
			}
			// select none
			li = driver.findElementsByClassName("android.widget.TextView");
			li.get(1).click();
//			for (int i = 0; i < li.size(); i++) {
//				if (li.get(i).getText().toString().indexOf("None")>-1) {
//					li.get(i).click();
//					break;
//				}
//				return;
//			}
			Thread.sleep(1000 * 5);
			script = Arrays.asList("keyevent", "KEYCODE_HOME");
			command = ImmutableMap.of("command", "input", "args", script);
			driver.executeScript("mobile: shell", command);
			
			//Set gmail storage permision  adb -s 903290c3 shell pm grant com.google.android.gm android.permission.WRITE_EXTERNAL_STORAGE
			
			Thread.sleep(1000 * 3);
			script = Arrays.asList("grant", "com.google.android.gm android.permission.WRITE_EXTERNAL_STORAGE");
			command = ImmutableMap.of("command", "pm", "args", script);
			driver.executeScript("mobile: shell", command);
			
			/*
			Thread.sleep(1000 * 2);
			script = Arrays.asList("start", "-a android.settings.SETTINGS");
			command = ImmutableMap.of("command", "am", "args", script);
			driver.executeScript("mobile: shell", command);

			// Select Apps
			li = driver.findElementsByClassName("android.widget.TextView");
			for (int i = 0; i < li.size(); i++) {
				if (li.get(i).getText().toString().indexOf("Apps") > -1) {
					li.get(i).click();
					break;
				}
				if (i == li.size() - 1)
					return false;
			}
			// select All Apps
			Thread.sleep(1000 * 2);
			li = driver.findElementsByClassName("android.widget.TextView");
			for (int i = 0; i < li.size(); i++) {
				if (li.get(i).getText().toString().indexOf("See all") > -1) {
					li.get(i).click();
					break;
				}
				if (i == li.size() - 1)
					return false;
			}
			// select Gmail
			Thread.sleep(1000 * 2);
			li = driver.findElementsByClassName("android.widget.TextView");
			for (int i = 0; i < li.size(); i++) {
				if (li.get(i).getText().toString().indexOf("Gmail") > -1) {
					li.get(i).click();
					break;
				}
				if (i == li.size() - 1)
					return false;
			}

			// Select Permissions
			Thread.sleep(1000 * 2);
			li = driver.findElementsByClassName("android.widget.TextView");
			for (int i = 0; i < li.size(); i++) {
				if (li.get(i).getText().toString().indexOf("Permissions") > -1) {
					li.get(i).click();
					break;
				}
				if (i == li.size() - 1)
					return false;
			}

			// Tap Storage
			Thread.sleep(1000 * 2);
			li = driver.findElementsByClassName("android.widget.TextView");
			for (int i = 0; i < li.size(); i++) {
				if (li.get(i).getText().toString().indexOf("Storage") > -1) {
					li.get(i).click();
					break;
				}
				if (i == li.size() - 1)
					return false;
			}

			// Back to Home
			Thread.sleep(1000 * 2);
			script = Arrays.asList("keyevent", "KEYCODE_HOME");
			command = ImmutableMap.of("command", "input", "args", script);
			driver.executeScript("mobile: shell", command);
			*/
			
			Thread.sleep(1000 * 2);
			script = Arrays.asList("start", "-a android.settings.WIFI_SETTINGS");
			command = ImmutableMap.of("command", "am", "args", script);
			driver.executeScript("mobile: shell", command);

			// Tap Storage
			li = driver.findElementsByClassName("android.widget.TextView");
			for (int i = 0; i < li.size(); i++) {
				if (li.get(i).getText().toString().equals(this.wifiName)) {
					li.get(i).click();
					break;
				}
				if (i == li.size() - 1) {
					System.out.println("Wifi connection failed. Please check wifi name '"+this.wifiName+" is available");
					return false;
				}
			}
			driver.findElementById("com.android.settings:id/password").setValue(this.wifiPwd); 
			driver.findElementById("android:id/button1").click();

			// Back to Home
			Thread.sleep(1000 * 3);
			script = Arrays.asList("keyevent", "KEYCODE_HOME");
			command = ImmutableMap.of("command", "input", "args", script);
			driver.executeScript("mobile: shell", command);

			Thread.sleep(1000 * 2);
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			return false;
		} finally {
			// driver.quit();
		}
		return true;
	}

	public boolean gmailSetup() {
		caps = new DesiredCapabilities();
		try {
			caps.setCapability("deviceName", this.deviceName);
			caps.setCapability("udid", this.deviceId); // Give Device ID of your mobile phone
			caps.setCapability("platformName", "Android");
			caps.setCapability("platformVersion", this.androidVersion);
			caps.setCapability("appPackage", "com.google.android.gm");
			caps.setCapability("appActivity", ".ui.MailActivityGmail");
			caps.setCapability("noReset", "true");
			driver = new AndroidDriver<MobileElement>(new URL("http://" + appuimIP + ":" + appiumPort + "/wd/hub"),
					caps);
			Thread.sleep(1000 * 5);
			driver.findElementById("com.google.android.gm:id/welcome_tour_got_it").click();
			Thread.sleep(1000 * 3);
			driver.findElementById("com.google.android.gm:id/setup_addresses_add_another").click();
			// Tap Google
			Thread.sleep(1000 * 2);
			li = driver.findElementsByClassName("android.widget.TextView");
			li.get(1).click();
//			for (MobileElement e: li) {
//				System.out.println(e.getText());
//				if (e.getText().toString().indexOf("Google")>-1) {
//					e.click();
//					break;
//				}
//				return;
//			}
			Thread.sleep(1000 * 10);
			driver.findElementById("identifierId").setValue(this.gmailId);
			driver.findElementById("identifierNext").click();
			Thread.sleep(1000 * 5);
			driver.findElementByClassName("android.widget.EditText").setValue(this.gmailPwd);
			// Password next button
			driver.findElementById("passwordNext").click();
			Thread.sleep(1000 * 10);
			// I agreee
			driver.findElementById("signinconsentNext").click();
			Thread.sleep(1000 * 7);
			// More Button
			driver.findElementByClassName("android.widget.Button").click();
			Thread.sleep(1000 * 3);
			// Accept Button
			driver.findElementByClassName("android.widget.Button").click();
			// Take me to gmail
			Thread.sleep(1000 * 8);
			driver.findElementById("com.google.android.gm:id/action_done").click();
			// Back to Home
			Thread.sleep(1000 * 3);
			script = Arrays.asList("keyevent", "KEYCODE_HOME");
			command = ImmutableMap.of("command", "input", "args", script);
			driver.executeScript("mobile: shell", command);

		} catch (Exception e) {
			// e.printStackTrace();
			return false;
		} finally {
			// driver.quit();
		}
		return true;
	}

	public String startAppium() {
		return "Started";
	}
}
