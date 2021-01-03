@ELLDataComparison
Feature: ELL Data Comparision from Oracle to MongoDB

@OracleELLDataToMongoDBComparison
Scenario: ELL Data Comparision from Oracle to MongoDB
Given User Retreive the ELL data from oracle and compare with MongoDB

@ELLDataTopicDescComp
Scenario: Topic Long Description Comparision from ELLdata to policy hierarchy
Given User validate the Topic Description from ELL data from Policy Hierarchy

@ELLDataToOpptyComparison
Scenario: ELL Data to Oppty Data Comparison for Midrules
Given User validate the ELL data for MidRules with Oppty collection

@SortOrderValidation
Scenario Outline: Validate Sort Order with Oracle and MongoDB
Given User validate the Sort Order between oracle and MongoDB for "<Level>"
Examples:
	   |   Level          | 
	   |   DP             |
	   |  Topic           |
	   |  Medical Policy  |
	   |  DPS RETIRE      |
	   |  MPS RETIRE      |
	   |  TPS RETIRE      |
	   
	   
@PCA-692
Scenario Outline: Ability to track Active/Inactive status for  a sub_rule_key
Given User Ability to track Active/Inactive status for  a sub_rule_key with oracle database "<OracleDatabase>" and mongo collection "<MongoCollection>"
Examples:
	   |   OracleDatabase          | MongoCollection                                     |
	   |   DEACTIVATED RULES 		   |	SUB_RULE_KEY;DOS_TO;DEACTIVATED_10		             |
	   |   DISABLED RULES    		   |	SUB_RULE_KEY;DOS_TO;DISABLED_10   		             |
	   |   RETIRED RULES    		   |	SUB_RULE_KEY;DOS_TO		                             |
	   
@PCA-2458
Scenario Outline: CPW Update opportunities for the eLL changes
Given CPW Update opportunities for the eLL changes for EllData to Oppty collection for "<Level>"
Examples:
	   |   Level          		     | 
	   |  DPS MIDRULES RETIRE      |
	   |  MPS MIDRULES RETIRE      |
	   |  TPS MIDRULES RETIRE      |
	   
@PCA-2411
Scenario Outline: PM Ability to update Monthly Policy changes on PM
Given Ability to update Monthly Policy changes on PM with status having disabled as "<Disabled>", deactivate as "<Deactivate>" and Retired as "<Retired>" with status "<Status>"
Examples:
     |   Disabled                | Deactivate      |Retired | Status      |
	   |   0                		   |	0	             |-1		  | RETIRED     |
	   |   0                		   |	-1	           |0		    | DEACTIVATED |
	   |   -1               		   |	0	             |0   	  | DISABLED    |
	   |   0                		   |	-1	           |-1   	  | RETIRED     |
	   |   -1               		   |	-1	           |0   	  | DISABLED    |
	   
	  

@PCA-3759
Scenario: Ability to track when new rule version is created
Given Ability to track when new rule version is created


@ValidateMonthlyOTtoOppty
Scenario: ELL Data to Oppty Data Comparison for Midrules
Given User validate the ELL data for MidRules with Oppty collection

@MonthlyToOppty
Scenario Outline: Compate data with Mongo Monthly and Oppty Collections for "<Collection>"
Given User validate the Mongo Monthly collection "<Collection>" for fields "<FIELDS>" oppty Collections as PrimaryKey "<Key>"
Examples:
 |   Collection                 | FIELDS                                                             |Key            |
 |  ell_ot_dp                   |DP_KEY;TOPIC_KEY;DP_SORT_ORDER;DP_DESC                              | DP_KEY        |
 |  ell_ot_mp                   |MED_POL_KEY;MP_SORT_ORDER;MP_TITLE                                  | MED_POL_KEY   |
 |  ell_ot_topic                |TOPIC_KEY;MED_POL_KEY;TOPIC_SORT_ORDER;TOPIC_TITLE                  | TOPIC_KEY     |
 
 @SwitchKey
Scenario Outline: Validate Swith key for client "<ClientKey>"
Given Validate Switch key for given clientkey "<ClientKey>"
Examples:
    |ClientKey | 
    |    56    |
    |    20    |
    |		25     |   
		|		43     | 
		|		51     |
		|		87     |
		|		138    | 
		|		11     | 
		|		44     | 
		|		77     | 
		|		121    | 
		|		128    | 
		|		134    |   
		|		14     |   		
		|		72     | 
		|		84     | 
		|		112    | 
    |		4      | 
		|		24     | 
		|		46     | 
		|		53     | 
		|		81     | 
		|		8      | 
		|		37     | 
		|		48     | 
		|		75     | 
		|		110    | 
		|		114    | 
		|		3      | 
		|		27     | 
		|		58     | 
		|		9      | 
		|		12     | 
		|		19     | 
		|		122    | 
   	|		150    |
   	
@PCA-3868
Scenario: Ability to update and notify when new rule version is created
Given Ability to update and notify when new rule version is created

@PCA-3808
Scenario: Ability to track, update and notify when new Mid rule is created
Given Ability to track, update and notify when new Mid rule is created
 



