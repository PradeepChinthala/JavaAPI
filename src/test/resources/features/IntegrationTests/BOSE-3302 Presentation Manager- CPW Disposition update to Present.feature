#This feature is not valid now becuse update disposition functionality is not working and 'Do Not Present - CPM Review','Do Not Present' dispositions got removed in cpw
#Author: chaitanya kumar natuva
#@Regression @BOSE3302
#Feature: BOSE3302
#
  #===> Updating the Disposition at DP level
  #===> Updating the Disposition at DP/LOB level
  #===> Updating the Disposition for multiple DP's
  ######################################################   Scenario-1  #############################################################
  #@BOSE3302_1
  #Scenario Outline: BOSE3302_1 Validate the Updated DP is visble in PM for the "<Updated Disposition>" Disposition in CPW
  #Given User "<User>" logged into "" application with Services
    #Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "<Updated Disposition>" DPKey
    #Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Present" DPKey
    #Given User "<User>" logged into "PM" application
    #When user selects "<Client>" from Client drop down list
    #And User selects  multiple Payershorts "All" from Payershorts FilterSection
    #And User selects muliple LOBs "All" from LOB FilterSection
    #When user selects "All" Claimtypes in filtersection
    #And User clicks on "Apply" button to filter
    #And User selects Medical Policy/Topic according to "Captured Topic"
    #And user filters by clicking on Apply for Medical Policy/Topic
    #Then validate the captured "Single" DPKey at "Available DPs Section"
#
    #Examples: 
      #| User   | Client             | Updated Disposition                |
      #| nkumar | WellPoint          | Single-Do Not Present - CPM Review |
      #| nkumar | UnitedHealth Group | Single-Do Not Present              |
      #| nkumar | AmeriHealth Mercy  | Single-Not Reviewed                |
      #| nkumar | Humana             | Single-Invalid                     |
#
  ####################################################   Scenario-2  #############################################################
  #@BOSE3302_2
  #Scenario Outline: BOSE3302_2 Validate the Updated DP-LOB is visble in PM for the "<Disposition>" Disposition in CPW
    #Given User "<User>" logged into "" application with Services
    #Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "<Disposition>" DPKey
    #Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single LOB-Present" DPKey
    #Given User "<User>" logged into "PM" application
    #When user selects "<Client>" from Client drop down list
    #And User selects  multiple Payershorts "All" from Payershorts FilterSection
    #And User selects muliple LOBs "All" from LOB FilterSection
    #When user selects "All" Claimtypes in filtersection
    #And User clicks on "Apply" button to filter
    #And User selects Medical Policy/Topic according to "Captured Topic"
    #And user filters by clicking on Apply for Medical Policy/Topic
    #Then validate the captured "Single" DPKey at "Available DPs Section"
#
    #Examples: 
      #| User   | Client             | Disposition                                     |
      #| nkumar | WellPoint     | Single Multiple LOB-Do Not Present - CPM Review |
      #| nkumar | UnitedHealth Group | Single Multiple LOB-Do Not Present              |
      #| nkumar | AmeriHealth Mercy  | Single Multiple LOB-Not Reviewed                |
      #| nkumar | Humana             | Single Multiple LOB-Invalid                     |
