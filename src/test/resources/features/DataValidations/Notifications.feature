@Notifications
Feature: Validate Notifications

@CleanPrevNotifications
Scenario: Clean Previous Notify eLL changes on Presentation Profile
Given Clean Data sheet "src//test//resources//Notifications.xlsx"

@Notification
Scenario Outline: Notify eLL changes on Presentation Profile
Given Notify eLL changes "<Change>"on Presentation Profile
Examples:
     |   Change                 | 
	   |DP DESC CHANGE            |
	   |TOPIC LONG DESC CHANGE    |
		 |DP SORT ORDER CHANGE      |
		 |TOPIC DESC CHANGE         |
		 |RULE RECAT	              |
		 |DP RECAT                  |
		 |TOPIC RECAT               |
		 |TOPIC SORT ORDER          |
		 |MP SORT ORDER             |
		 |DP RETIRE                 |
		 |TOPIC RETIRE              |
		 |MP RETIRE                 |
		 |RULE DISABLED             |
		 |RULE DEACTIVATE           |
   	 |NEW MIDRULE               |
		 |NEW MIDRULE VERSION       |
		 |SWITCH                    |
		 |PRIMARY SOURCE KEY        |
		 |PRIMARY SOURCE DESCRIPTION|
		 |SAVINGS                   |
		 
@ValidateNotifications
Scenario: Validate Notifications from the generated sheet
Given Validate the Notifications from "src//test//resources//Notifications.xlsx"



