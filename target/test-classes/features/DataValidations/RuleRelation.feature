@RuleRelation
Feature: Rule Relationship Comparision from Oracle to MongoDB

##PCA-2460  Query the rule relationship table for RVA opportunity updates
@RuleRelationPCA-2460
Scenario Outline:  Data Comparision from Oracle to MongoDB
And   Capture  "TotalRecordsForClient" details  from Oracle "RVAPRD1" for the  "<Client>"  for  "<DataVersion>"    
And   Capture  "MidRulesDetails" details  from Oracle "PMPRD2" for the  "<Client>"  for  "<DataVersion>" 
And   Capture  "DistinctRuleRelationships" details  from Oracle "PMPRD2" for the  "<Client>"  for  "MidRules"  
Then  Validate the RuleRelationship  details in MongoDB "OPPTY" collection against OracleDB

 Examples: 
  | Client           																		 | DataVersion       | 
  |Horizon Healthcare Services, Inc.|		October 							|
  
  
   	 
 ##PCA-3445 	 Rule Relationship Script to update the opportunity collection with the relationships	 
@RuleRelation
Scenario:  Data Comparision from Oracle to MongoDB
Given User retrieves the RuleRelationship data from Oracle and compare with MongoDB


