#This feature is not valid now becuse update disposition functionality is not working and 'Do Not Present - CPM Review','Do Not Present' dispositions got removed in cpw 

#Author: chaitanya kumar natuva
#@Regression @BOSE3303
#Feature: BOSE3303
#
  #==>Need to add the Export functionality to validate the DP,which is not visible after updating the disposition
  #===> Updating the Disposition at DP level
  #===> Updating the Disposition at DP/LOB level
  #===> Updating the Disposition for multiple DP's
  #####################################################   Scenario-1  #############################################################
  #@BOSE3303_1
  #Scenario Outline: BOSE3303_1 Validate the Updated DP is not visble in PM for the "<Updated Disposition>" Disposition
    #Given User "<User>" logged into "" application with Services
    #Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    #Given User "<User>" logged into "PM" application
    #When user selects "<Client>" from Client drop down list
    #And User selects  multiple Payershorts "All" from Payershorts FilterSection
    #And User selects muliple LOBs "All" from LOB FilterSection
    #When user selects "All" Claimtypes in filtersection
    #And User clicks on "Apply" button to filter
    #And User selects Medical Policy/Topic according to "Captured Topic"
    #And user filters by clicking on Apply for Medical Policy/Topic
    #Then validate the captured "Single" DPKey at "Available DPs Section"
    #Then validate the captured "Single" DPKey at "Presetation Profile Section"
    #Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "<Updated Disposition>" DPKey
    #Then the user should not view "Updated DPKey" at "Available DPs Section"
    #Then the user should not view "Updated DPKey" at "Presetation Profile Section"
#
    #Examples: 
      #| User   | Client             | Updated Disposition                  |
      #| nkumar | WellPoint          | Same RVA-Do Not Present - CPM Review |
      #| nkumar | UnitedHealth Group | Same RVA-Do Not Present              |
      #| nkumar | AmeriHealth Mercy  | Same RVA-Not Reviewed                |
      #| nkumar | Humana             | Same RVA-Invalid                     |
#
  #####################################################   Scenario-2  #############################################################
  #@BOSE3303_2
  #Scenario Outline: BOSE3303_2 Validate the Updated LOB-DP is not visble in PM for the "<Updated Disposition>" Disposition
    #Given User "<User>" logged into "" application with Services
    #Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single-Multiple LOB" DPKey
    #Given User "<User>" logged into "PM" application
    #When user selects "<Client>" from Client drop down list
    #And User selects  multiple Payershorts "All" from Payershorts FilterSection
    #And User selects muliple LOBs "All" from LOB FilterSection
    #When user selects "All" Claimtypes in filtersection
    #And User clicks on "Apply" button to filter
    #And User selects Medical Policy/Topic according to "Captured Topic"
    #And user filters by clicking on Apply for Medical Policy/Topic
    #Then validate the captured "Single" DPKey at "Available DPs Section"
    #Then validate the captured "Single" DPKey at "Presetation Profile Section"
    #Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "<Updated Disposition>" DPKey
    #Then the user should not view "Updated LOB-DPKey" at "Available DPs Section"
    #Then the user should not view "Updated LOB-DPKey" at "Presetation Profile Section"
#
    #Examples: 
      #| User   | Client             | Updated Disposition                             |
      #| nkumar | WellPoint          | Same RVA Single LOB-Do Not Present - CPM Review |
      #| nkumar | Cigna              | Same RVA Single LOB-Do Not Present              |
      #| nkumar | Humana             | Same RVA Single LOB-Not Reviewed                |
      #| nkumar | UnitedHealth Group | Same RVA Single LOB-Invalid                     |
