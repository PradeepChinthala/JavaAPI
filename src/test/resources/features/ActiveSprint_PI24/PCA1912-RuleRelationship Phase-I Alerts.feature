
#Author: Udayakiran.Lanka

@Regression @PCA1912
Feature: Rule Relationship Phase I Alerts,PCA-1912

 @PCA-2471-RuleRelationshiIconPMPresentation/DecisionView   @PCA-2471
 Scenario Outline: Presentation Manager: Rule Relationship icon Presentation/Decision View	,	PCA-3446-Rule Relationship icon: Add the hover over text(Sprint-234)
 Given Capture "DPKey" for the "<Client>" which has disposition value "Present"  from "OPPTY" collection  for "<RuleRelationCombination>"
 Given User "<User>" logged into "PM" application
Then The user should view the PresentationManager HomePage
When user selects "<Client>" from Client drop down list
And User selects  multiple Payershorts "All" from Payershorts FilterSection
And User selects muliple LOBs "All" from LOB FilterSection 
And User clicks on "Apply" button to filter
And User selects Medical Policy/Topic according to "AllMedicalPolicies"
And user filters by clicking on Apply for Medical Policy/Topic
Given CPMorCMD has created a PresentationProfile 
And  User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
Then  Navigate to the "CapturedDPKey"  in the "AvaialbleOpportunityView"
And  User assigns the "CapturedDPkey" to the "Presentation"  at level "DPKEYLEVEL" 
When The CPMorCMD clicks on the "PresentationProfile"
And Apply All Payers and LOBs filters in Presenation profile
And Expand all the items in presenation hierarchy view
And  RuleRelationshipIcon and text on hover should be displayed for the "MedicalPolicyLevel" in the "PresentationDecisonView"
And  RuleRelationshipIcon and text on hover should be displayed for the "TopicLevel" in the "PresentationDecisonView"
And  RuleRelationshipIcon and text on hover should be displayed for the "DPLevel" in the "PresentationDecisonView"
Then   User logsout of the "PM" Application
  
   Examples: 
     | User            |  Client                    |RuleRelationCombination		|
     | ulanka       | Aetna Medicaid |AnyRuleRelationship     	    |
       |iht_ittest05  |Humana, Inc |AnyRuleRelationship     	    |
 	 		  	 


 @RuleRel
 @PCA-2467-RuleRelationshiIconAvaialbleOppdeck   @PCA-2467
 Scenario Outline: Presentation Manager: Rule Relationship icon AvaialbleOppdeck
 Given Capture "DPKey" for the "<Client>" which has disposition value "Present"  from "OPPTY" collection  for "<RuleRelationCombination>"
 Given User "<User>" logged into "PM" application
Then The user should view the PresentationManager HomePage
When user selects "<Client>" from Client drop down list
And User selects  multiple Payershorts "All" from Payershorts FilterSection
And User selects muliple LOBs "All" from LOB FilterSection 
And User clicks on "Apply" button to filter
And User selects Medical Policy/Topic according to "AllMedicalPolicies"
And user filters by clicking on Apply for Medical Policy/Topic
And  User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
Then  Navigate to the "CapturedDPKey"  in the "AvaialbleOpportunityView"
And   RuleRelationshipIcon and text on hover should be displayed on the "DPCardHeader"
And    RuleRelationshipIcon and text on hover should be displayed on the "DPPayerLOBGrid"
Then   User logsout of the "PM" Application
 
 
   Examples: 
      | User     |  Client                        |RuleRelationCombination	|     
        |ulanka |Anthem, Inc  |AnyRuleRelationship     	    |

 

 @PCA-2464-MutuallyExclusiveRuleRelationships  @PCA-2464-1
 Scenario Outline: Presentation Manager: Mutually Exclusive Rule Relationships	
 Given Capture "DPKey" for the "<Client>" which has disposition value "Present"  from "OPPTY" collection  for "MutuallyExclusiveRelationship"
 Given User "<User>" logged into "PM" application
Then The user should view the PresentationManager HomePage
When user selects "<Client>" from Client drop down list
And User selects  multiple Payershorts "All" from Payershorts FilterSection
And User selects muliple LOBs "All" from LOB FilterSection 
And User clicks on "Apply" button to filter
And User selects Medical Policy/Topic according to "AllMedicalPolicies"
And user filters by clicking on Apply for Medical Policy/Topic
Given CPMorCMD has created a PresentationProfile 
And  User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
Then  Navigate to the "CapturedDPKey"  in the "AvaialbleOpportunityView"
And  User assigns the "CapturedDPkey" to the "Presentation"  at level "DPKeyLevel" 
Then The Application should "Display"  the information message "<InfoMessage>"
Then   User logsout of the "PM" Application
           
   Examples: 
   | User     |  Client                   |RuleRelationCombination		     	|InfoMessage|
   #| ulanka | Aetna Medicaid |MutuallyExclusiveRelationship		|The selection contains rules with a mutually exclusive relationship. Please select the rule relationship icon for more details.|
  # |ulanka | Anthem, Inc |MutuallyExclusiveRelationship				|The selection contains rules with a mutually exclusive relationship. Please select the rule relationship icon for more details.	|
  # |ulanka | AmeriHealth Mercy |MutuallyExclusiveRelationship				|The selection contains rules with a mutually exclusive relationship. Please select the rule relationship icon for more details.	|
 # |ulanka |  Humana, Inc |MutuallyExclusiveRelationship				|The selection contains rules with a mutually exclusive relationship. Please select the rule relationship icon for more details.	| 
   |iht_ittest05  |Humana, Inc  |MutuallyExclusiveRelationship				|The selection contains rules with a mutually exclusive relationship. Please select the rule relationship icon for more details.	|
 
 @RuleRel
 @PCA-2464-MutuallyExclusiveRuleRelationships2   @PCA-2464-2
 Scenario Outline: Presentation Manager: Mutually Exclusive Rule Relationships	 at TopicLevel
 Given Capture "DPKey" for the "<Client>" which has disposition value "Present"  from "OPPTY" collection  for "<RuleRelationCombination>"
 Given User "<User>" logged into "PM" application
Then The user should view the PresentationManager HomePage
When user selects "<Client>" from Client drop down list
And User selects  multiple Payershorts "All" from Payershorts FilterSection
And User selects muliple LOBs "All" from LOB FilterSection 
And User clicks on "Apply" button to filter
And User selects Medical Policy/Topic according to "AllMedicalPolicies"
And user filters by clicking on Apply for Medical Policy/Topic
Given CPMorCMD has created a PresentationProfile 
And  User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
Then  Navigate to the "CapturedDPKey"  in the "AvaialbleOpportunityView"
And  User assigns the "CapturedDPkey" to the "Presentation"  at level "TOPIC LEVEL" 
Then The Application should "Display"  the information message "<InfoMessage>"
Then   User logsout of the "PM" Application
           
   Examples: 
   | User    |  Client            |RuleRelationCombination									|InfoMessage|   
   |ulanka  | Anthem, Inc  |MutuallyExclusiveRelationship				|The selection contains rules with a mutually exclusive relationship. Please select the rule relationship icon for more details.	|
 
    
    
 @RuleRel
  @PCA-2464-MutuallyExclusiveRuleRelationships3   @PCA-2464-3
 Scenario Outline: Presentation Manager: Mutually Exclusive Rule Relationships	 at MedPolicyLevel
  Given Capture "DPKey" for the "<Client>" which has disposition value "Present"  from "OPPTY" collection  for "<RuleRelationCombination>"
 Given User "<User>" logged into "PM" application
Then The user should view the PresentationManager HomePage
When user selects "<Client>" from Client drop down list
And User selects  multiple Payershorts "All" from Payershorts FilterSection
And User selects muliple LOBs "All" from LOB FilterSection 
And User clicks on "Apply" button to filter
And User selects Medical Policy/Topic according to "AllMedicalPolicies"
And user filters by clicking on Apply for Medical Policy/Topic
Given CPMorCMD has created a PresentationProfile 
And  User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
Then  Navigate to the "CapturedDPKey"  in the "AvaialbleOpportunityView"
And  User assigns the "CapturedDPkey" to the "Presentation"  at level "MedicalPolicyLevel" 
Then The Application should "Display"  the information message "<InfoMessage>"
Then   User logsout of the "PM" Application
           
   Examples: 
 | User      | Client            									|RuleRelationCombination									|InfoMessage|
 | ulanka |Anthem, Inc	 |MutuallyExclusiveRelationship		|The selection contains rules with a mutually exclusive relationship. Please select the rule relationship icon for more details.|
 #|iht_ittest05  |Humana, Inc  |MutuallyExclusiveRelationship				|The selection contains rules with a mutually exclusive relationship. Please select the rule relationship icon for more details.	|
 
     
 @RuleRel  
 @PCA-2464-MutuallyExclusiveRuleRelationships4    @PCA-2464-4  @RuleRel  
 Scenario Outline: Presentation Manager: Non-Mutually Exclusive Rule Relationships	 at PayerLOBlevel & DP Level
 Given Capture "DPKey" for the "<Client>" which has disposition value "Present"  from "OPPTY" collection  for "Non-MutuallyExclusiveRelationship"
 Given User "<User>" logged into "PM" application
Then The user should view the PresentationManager HomePage
When user selects "<Client>" from Client drop down list
And User selects  multiple Payershorts "All" from Payershorts FilterSection
And User selects muliple LOBs "All" from LOB FilterSection 
And User clicks on "Apply" button to filter
And User selects Medical Policy/Topic according to "AllMedicalPolicies"
And user filters by clicking on Apply for Medical Policy/Topic
Given CPMorCMD has created a PresentationProfile 
And  User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
Then  Navigate to the "CapturedDPKey"  in the "AvaialbleOpportunityView"
And  User assigns the "CapturedDPkey" to the "Presentation"  at level "DPKeyLevel" 
Then The Application should "SHOULD NOT DISPLAY"  the information message "<InfoMessage>"
Then   User logsout of the "PM" Application
           
   Examples: 
 | User                |  Client                        |RuleRelationCombination																																		|InfoMessage|
   |ulanka |Anthem, Inc   |AnyOneOfRelationships-Companion-Counterpart-OutofSequence				|The selection contains rules with a mutually exclusive relationship. Please select the rule relationship icon for more details.|
  #|ulanka | Aetna Medicaid |AnyOneOfRelationships-Companion-Counterpart-OutofSequence				|The selection contains rules with a mutually exclusive relationship. Please select the rule relationship icon for more details.|
 
     
    

@PCA-2464-MutuallyExclusiveRuleRelationships5   @PCA-2464-5  @RuleRel   @ReRuns
 Scenario Outline: Presentation Manager: Non-Mutually Exclusive Rule Relationships	at Topic Level
 Given Capture "DPKey" for the "<Client>" which has disposition value "Present"  from "OPPTY" collection  for "Non-MutuallyExclusiveRelationship"
 Given User "<User>" logged into "PM" application
Then The user should view the PresentationManager HomePage
When user selects "<Client>" from Client drop down list
And User selects  multiple Payershorts "All" from Payershorts FilterSection
And User selects muliple LOBs "All" from LOB FilterSection 
And User clicks on "Apply" button to filter
And User selects Medical Policy/Topic according to "AllMedicalPolicies"
And user filters by clicking on Apply for Medical Policy/Topic
Given CPMorCMD has created a PresentationProfile 
And  User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
Then  Navigate to the "CapturedDPKey"  in the "AvaialbleOpportunityView"
And  User assigns the "CapturedDPkey" to the "Presentation"  at level "TOPIC LEVEL" 
Then The Application should "SHOULD NOT DISPLAY"  the information message "<InfoMessage>"
Then   User logsout of the "PM" Application
           
 Examples: 
 | User                |  Client                        |RuleRelationCombination																																		|InfoMessage|
   |ulanka  |Anthem, Inc   |AnyOneOfRelationships-Companion-Counterpart-OutofSequence				|The selection contains rules with a mutually exclusive relationship. Please select the rule relationship icon for more details.|
  #|ulanka | Aetna Medicaid |AnyOneOfRelationships-Companion-Counterpart-OutofSequence				|The selection contains rules with a mutually exclusive relationship. Please select the rule relationship icon for more details.|
 
     

  @PCA-2464-MutuallyExclusiveRuleRelationships6  @ReRun
 Scenario Outline: Presentation Manager: Non-Mutually Exclusive Rule Relationships	at MedicalPolicy Level
 Given Capture "DPKey" for the "<Client>" which has disposition value "Present"  from "OPPTY" collection  for "Non-MutuallyExclusiveRelationship"
 Given User "<User>" logged into "PM" application
Then The user should view the PresentationManager HomePage
When user selects "<Client>" from Client drop down list
And User selects  multiple Payershorts "All" from Payershorts FilterSection
And User selects muliple LOBs "All" from LOB FilterSection 
And User clicks on "Apply" button to filter
And User selects Medical Policy/Topic according to "AllMedicalPolicies"
And user filters by clicking on Apply for Medical Policy/Topic
Given CPMorCMD has created a PresentationProfile 
And  User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
Then  Navigate to the "CapturedDPKey"  in the "AvaialbleOpportunityView"
And  User assigns the "CapturedDPkey" to the "Presentation"  at level "MedicalPolicyLevel" 
Then The Application should "SHOULD NOT DISPLAY"  the information message "<InfoMessage>"
Then   User logsout of the "PM" Application
           
 Examples: 
 | User                |  Client                        |RuleRelationCombination																																		|InfoMessage|
  # |iht_ittest05  |Humana, Inc |AnyOneOfRelationships-Companion-Counterpart-OutofSequence				|The selection contains rules with a mutually exclusive relationship. Please select the rule relationship icon for more details.|
  |ulanka | Aetna Medicaid |AnyOneOfRelationships-Companion-Counterpart-OutofSequence				|The selection contains rules with a mutually exclusive relationship. Please select the rule relationship icon for more details.|
 
     


 @PCA-3024   @RuleRel @ReRun
 Scenario Outline: Presentation Manager: AssignmentOfCompanion, Counterpart, and Out of Sequence Assignment logic at presentation profile
 Given Capture "DPKey" for the "<Client>" which has disposition value "Present"  from "OPPTY" collection  for "<RuleRelationCombination>"
 Given User "<User>" logged into "PM" application
Then The user should view the PresentationManager HomePage
When user selects "<Client>" from Client drop down list
And User selects  multiple Payershorts "All" from Payershorts FilterSection
And User selects muliple LOBs "All" from LOB FilterSection 
And User clicks on "Apply" button to filter
And User selects Medical Policy/Topic according to "AllMedicalPolicies"
And user filters by clicking on Apply for Medical Policy/Topic
Given CPMorCMD has created a PresentationProfile 
And  User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
Then  Navigate to the "CapturedDPKey"  in the "AvaialbleOpportunityView"
And Capture the corresponding DPs for the "CapturedDPKey" 
And  User assigns the "CapturedDPkey" to the "Presentation"  at level "DPKeyLevel" 
When The CPMorCMD clicks on the "PresentationProfile"
And Apply All Payers and LOBs filters in Presenation profile
And Expand all the items in presenation hierarchy view
Then The corresponding Companion, Counterpart, and Out of Sequence Rules or DPs for the "CapturedDPKey"  are included in the assignment to the same "Presentation"
Then   User logsout of the "PM" Application
           
   Examples: 
   | User     |  Client                   |RuleRelationCombination												|
    |ulanka  |AmeriHealth Mercy    |Non-MutuallyExclusiveRelationship				|
 
    
    
   
 @PCA-3025  @RuleRel  @ReRun
 Scenario Outline: Presentation Manager:Rule relationship Popup Available Opportunity View
 Given Capture "DPKey" for the "<Client>" which has disposition value "Present"  from "OPPTY" collection  for "RuleRelationCombination"
 Given User "<User>" logged into "PM" application
Then The user should view the PresentationManager HomePage
When user selects "<Client>" from Client drop down list
And User selects  multiple Payershorts "All" from Payershorts FilterSection
And User selects muliple LOBs "All" from LOB FilterSection 
And User clicks on "Apply" button to filter
And User selects Medical Policy/Topic according to "AllMedicalPolicies"
And user filters by clicking on Apply for Medical Policy/Topic
Given CPMorCMD has created a PresentationProfile 
And  User filters the "AvailableDPsDeck" with filter drpdown value "NotStartedOnly" to show related DPs
Then  Navigate to the "CapturedDPKey"  in the "AvaialbleOpportunityView"
Then   Validate the RuleRelationship Grid data details in  "AvailableOpportunityView"
Then   User logsout of the "PM" Application
           
   Examples: 
   | User               |  Client                  |RuleRelationCombination																														|
  |ulanka  |Anthem, Inc  |AnyOneOfRelationships-Companion-Counterpart-OutofSequence	|
 
   ##*********************  ******************  *******************************************### 	
    
 
 ##*********************  Data Comparision  *******************************************### 	
    
     	 
 ##PCA-3445 	 Rule Relationship Script to update the opportunity collection with the relationships	 
#@RuleRelation
#Scenario:  Data Comparision from Oracle to MongoDB
#Given User retrieves the RuleRelationship data from Oracle and compare with MongoDB
    #
  ##PCA-2460  Query the rule relationship table for RVA opportunity updates
#@RuleRelationPCA-2460
#Scenario Outline:  Data Comparision from Oracle to MongoDB
#And   Capture  "TotalRecordsForClient" details  from Oracle "RVAPRD1" for the  "<Client>"  for  "<DataVersion>"    
#And   Capture  "MidRulesDetails" details  from Oracle "PMPRD2" for the  "<Client>"  for  "<DataVersion>" 
#And   Capture  "DistinctRuleRelationships" details  from Oracle "PMPRD2" for the  "<Client>"  for  "MidRules"  
#Then  Validate the RuleRelationship  details in MongoDB "OPPTY" collection against OracleDB
#
 #Examples: 
  #| Client           																		 | DataVersion       | 
  #|Horizon Healthcare Services, Inc.|		October 							|

