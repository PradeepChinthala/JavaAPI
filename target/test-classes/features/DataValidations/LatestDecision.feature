@LatestDecision
Feature: Cross over latestDecision collections Comparision from Oracle to MongoDB

@LatestDecisionOracletoMongoDB
Scenario Outline: Cross over latestDecision collections validation from oracle to MongoDB for "<DP>" 
Given User Retreive the Latest Collection data from oracle and compare with MongoDB for "<DP>"
Examples:
     |   DP                 | 
	   | LATEST COLLECTION    |
	   | INFORMATIONAL DP     |
	   | CONFIGURATION DP     |

 



