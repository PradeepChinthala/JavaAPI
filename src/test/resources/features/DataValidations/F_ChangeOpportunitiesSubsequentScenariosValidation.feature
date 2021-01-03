@ChangeOppty
Feature: Change Opportunities Subsequent Scenarios

  #####################################################   Scenario-1  #############################################################
  @Unassigned
  Scenario Outline: Unassigned scenario for change status for "<changeType>" and Preconditon is "<DPType>"
    Given User "<User>" logged into "" application with Services
    When "<User>" sets data for "<changeType>" with "<DPType>"
    And User posts change opportunity data with "<changeType>"
    Then User verifies data for changed opportunity with "<changeType>","<expeLLChange>" for "<Criteria>"

    Examples: 
      | User         | changeType            | DPType                         | Criteria   | expeLLChange                  |
      | iht_ittest09 | CHANGE DP DESC        | RULE CHANGE , REFERENCE CHANGE | Unassigned | CHANGE DP DESC                |
      | iht_ittest09 | CHANGE DP DESC        | RECAT DP                       | Unassigned | CHANGE DP DESC                |
      | iht_ittest09 | CHANGE DP DESC        | CHANGE DP DESC                 | Unassigned | CHANGE DP DESC                |
      | iht_ittest09 | DEACT DP              | CHANGE DP DESC                 | Unassigned | RECATEGORIZED RULE , DEACT DP |
      | iht_ittest09 | DEACT DP              | CLAIM TYPE REMOVED             | Unassigned | RECATEGORIZED RULE , DEACT DP |
      | iht_ittest09 | NEW DP                | CLAIM TYPE ADDED               | Unassigned |                               |
      | iht_ittest09 | RECAT DP              | CHANGE DP DESC                 | Unassigned |                               |
      | iht_ittest09 | RECAT DP              | RULE CHANGE                    | Unassigned |                               |
      | iht_ittest09 | RECAT RULE            | RULE CHANGE                    | Unassigned |                               |
      | iht_ittest09 | RECAT RULE            | CHANGE DP DESC                 | Unassigned |                               |
      | iht_ittest09 | CLAIM TYPE ADDED      | CHANGE DP DESC                 | Unassigned |                               |
      | iht_ittest09 | CLAIM TYPE REMOVED    | CHANGE DP DESC                 | Unassigned |                               |
      | iht_ittest09 | NEW RULE              | VERSION CHANGE                 | Unassigned | NEW RULE , RULE CHANGE        |
      | iht_ittest09 | NEW RULE              | NEW RULE                       | Unassigned | NEW RULE                      |
      | iht_ittest09 | NEW RULE              | CHANGE DP DESC                 | Unassigned | NEW RULE , CHANGE DP DESC     |
      | iht_ittest09 | RULE REFERENCE CHANGE | VERSION CHANGE                 | Unassigned | REFERENCE CHANGE              |
      | iht_ittest09 | RULE VERSION CHANGE   | VERSION CHANGE                 | Unassigned | RULE CHANGE                   |
      | iht_ittest09 | RULE DESC CHANGE      | RECAT DP                       | Unassigned | RULE CHANGE , RECAT DP        |
      | iht_ittest09 | RULE DEACT CHANGE     | VERSION CHANGE                 | Unassigned | DEACTIVATED POLICY            |
      | iht_ittest09 | RULE DEACT CHANGE     | RECATEGORIZED RULE             | Unassigned | DEACTIVATED POLICY            |
      | iht_ittest09 | RULE DEACT CHANGE     | CHANGE DP DESC                 | Unassigned | DEACTIVATED POLICY            |
      | iht_ittest09 | NEW MP TOPIC RECAT    | VERSION CHANGE                 | Unassigned | NEW MP , RECAT TOPIC          |
      | iht_ittest09 | NEW MP TOPIC RECAT    | CHANGE DP DESC                 | Unassigned | NEW MP , RECAT TOPIC          |
      | iht_ittest09 | NEW TOPIC             | CHANGE DP DESC                 | Unassigned | NEW TOPIC , RECAT DP          |

  #####################################################   Scenario-2  #############################################################
  @WithoutRFP
  Scenario Outline: Without RFP scenario for change status for "<changeType>" and Preconditon is "<DPType>"
    Given User "<User>" logged into "" application with Services
    When "<User>" sets data for "<changeType>" with "<DPType>"
    Then Create the change presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign change dps to created profile
    And User posts change opportunity data with "<changeType>"
    Then User verifies data for changed opportunity with "<changeType>","<expeLLChange>" for "<Criteria>"

    Examples: 
      | User         | changeType            | DPType                         | Criteria    | expeLLChange                  |
      | iht_ittest09 | CHANGE DP DESC        | RULE CHANGE , REFERENCE CHANGE | Without RFP | CHANGE DP DESC                |
      | iht_ittest09 | CHANGE DP DESC        | RECAT DP                       | Without RFP | CHANGE DP DESC                |
      | iht_ittest09 | CHANGE DP DESC        | CHANGE DP DESC                 | Without RFP | CHANGE DP DESC                |
      | iht_ittest09 | DEACT DP              | CHANGE DP DESC                 | Without RFP | RECATEGORIZED RULE , DEACT DP |
      | iht_ittest09 | DEACT DP              | CLAIM TYPE REMOVED             | Without RFP | RECATEGORIZED RULE , DEACT DP |
      | iht_ittest09 | NEW DP                | CLAIM TYPE ADDED               | Without RFP |                               |
      | iht_ittest09 | RECAT DP              | CHANGE DP DESC                 | Without RFP |                               |
      | iht_ittest09 | RECAT DP              | RULE CHANGE                    | Without RFP |                               |
      | iht_ittest09 | RECAT RULE            | RULE CHANGE                    | Without RFP |                               |
      | iht_ittest09 | RECAT RULE            | CHANGE DP DESC                 | Without RFP |                               |
      | iht_ittest09 | CLAIM TYPE ADDED      | CHANGE DP DESC                 | Without RFP |                               |
      | iht_ittest09 | CLAIM TYPE REMOVED    | CHANGE DP DESC                 | Without RFP |                               |
      | iht_ittest09 | NEW RULE              | VERSION CHANGE                 | Without RFP | NEW RULE , RULE CHANGE        |
      | iht_ittest09 | NEW RULE              | NEW RULE                       | Without RFP | NEW RULE                      |
      | iht_ittest09 | NEW RULE              | CHANGE DP DESC                 | Without RFP | NEW RULE , CHANGE DP DESC     |
      | iht_ittest09 | RULE REFERENCE CHANGE | VERSION CHANGE                 | Without RFP | RULE CHANGE                   |
      | iht_ittest09 | RULE VERSION CHANGE   | VERSION CHANGE                 | Without RFP | RULE CHANGE                   |
      | iht_ittest09 | RULE DESC CHANGE      | RECAT DP                       | Without RFP | RULE CHANGE , RECAT DP        |
      | iht_ittest09 | RULE DEACT CHANGE     | VERSION CHANGE                 | Without RFP | DEACTIVATED POLICY            |
      | iht_ittest09 | RULE DEACT CHANGE     | RECATEGORIZED RULE             | Without RFP | DEACTIVATED POLICY            |
      | iht_ittest09 | RULE DEACT CHANGE     | CHANGE DP DESC                 | Without RFP | DEACTIVATED POLICY            |
      | iht_ittest09 | NEW MP TOPIC RECAT    | VERSION CHANGE                 | Without RFP | NEW MP , RECAT TOPIC          |
      | iht_ittest09 | NEW MP TOPIC RECAT    | CHANGE DP DESC                 | Without RFP | NEW MP , RECAT TOPIC          |
      | iht_ittest09 | NEW TOPIC             | CHANGE DP DESC                 | Without RFP | NEW TOPIC , RECAT DP          |

  #####################################################   Scenario-3  #############################################################
  @WithRFP
  Scenario Outline: RFP scenario for change status for "<changeType>" and Preconditon is "<DPType>"
    Given User "<User>" logged into "" application with Services
    When "<User>" sets data for "<changeType>" with "<DPType>"
    Then Create the change presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign change dps to created profile
    Given User "<User>" logged into "PM" application
    When user selects "clientkey" from Client drop down list
    Then user should select "Change Opportunities" tab
    Then User validates the "CaptureDecision" of Dp's for the Profile in "Approve"
    And User posts change opportunity data with "<changeType>"
    Then User verifies data for changed opportunity with "<changeType>","<expeLLChange>" for "<Criteria>"

    Examples: 
      | User         | changeType            | DPType                         | Criteria | expeLLChange                  |
      | iht_ittest09 | CHANGE DP DESC        | RULE CHANGE , REFERENCE CHANGE | With RFP | CHANGE DP DESC                |
      | iht_ittest09 | CHANGE DP DESC        | RECAT DP                       | With RFP | CHANGE DP DESC                |
      | iht_ittest09 | CHANGE DP DESC        | CHANGE DP DESC                 | With RFP | CHANGE DP DESC                |
      | iht_ittest09 | DEACT DP              | CHANGE DP DESC                 | With RFP | RECATEGORIZED RULE , DEACT DP |
      | iht_ittest09 | DEACT DP              | CLAIM TYPE REMOVED             | With RFP | RECATEGORIZED RULE , DEACT DP |
      | iht_ittest09 | NEW DP                | CLAIM TYPE ADDED               | With RFP |                               |
      | iht_ittest09 | RECAT DP              | CHANGE DP DESC                 | With RFP |                               |
      | iht_ittest09 | RECAT DP              | RULE CHANGE                    | With RFP |                               |
      | iht_ittest09 | RECAT RULE            | RULE CHANGE                    | With RFP |                               |
      | iht_ittest09 | RECAT RULE            | CHANGE DP DESC                 | With RFP |                               |
      | iht_ittest09 | CLAIM TYPE ADDED      | CHANGE DP DESC                 | With RFP |                               |
      | iht_ittest09 | CLAIM TYPE REMOVED    | CHANGE DP DESC                 | With RFP |                               |
      | iht_ittest09 | NEW RULE              | VERSION CHANGE                 | With RFP | RULE CHANGE                   |
      | iht_ittest09 | NEW RULE              | NEW RULE                       | With RFP | NEW RULE                      |
      | iht_ittest09 | NEW RULE              | CHANGE DP DESC                 | With RFP | CHANGE DP DESC                |
      | iht_ittest09 | RULE REFERENCE CHANGE | VERSION CHANGE                 | With RFP | RULE CHANGE                   |
      | iht_ittest09 | RULE VERSION CHANGE   | VERSION CHANGE                 | With RFP | RULE CHANGE                   |
      | iht_ittest09 | RULE DESC CHANGE      | RECAT DP                       | With RFP | RECAT DP                      |
      | iht_ittest09 | RULE DEACT CHANGE     | VERSION CHANGE                 | With RFP | DEACTIVATED POLICY            |
      | iht_ittest09 | RULE DEACT CHANGE     | RECATEGORIZED RULE             | With RFP | DEACTIVATED POLICY            |
      | iht_ittest09 | RULE DEACT CHANGE     | CHANGE DP DESC                 | With RFP | DEACTIVATED POLICY            |
      | iht_ittest09 | NEW MP TOPIC RECAT    | VERSION CHANGE                 | With RFP | NEW MP , RECAT TOPIC          |
      | iht_ittest09 | NEW MP TOPIC RECAT    | CHANGE DP DESC                 | With RFP | NEW MP , RECAT TOPIC          |
      | iht_ittest09 | NEW TOPIC             | CHANGE DP DESC                 | With RFP | NEW TOPIC , RECAT DP          |

  #####################################################   Scenario-4  #############################################################
  @UnassignedUI 
  Scenario Outline: Unassigned scenario for change status for "<changeType>" and Preconditon is "<DPType>" with UI
    Given User "<User>" logged into "" application with Services
    When "<User>" sets data for "<changeType>" with "<DPType>"
    And User posts change opportunity data with "<changeType>"
    Then User verifies data for changed opportunity with "<changeType>","<expeLLChange>" for "<Criteria>"
    Given User "<User>" logged into "PM" application
    When user selects "clientkey" from Client drop down list
    Then user should select "Change Opportunities" tab
    Then user validates change summary data for "<changeType>" for "<Criteria>"

    Examples: 
      | User         | changeType       | DPType         | Criteria   | expeLLChange |
      | iht_ittest09 | RECAT DP         | CHANGE DP DESC | Unassigned |              |
      | iht_ittest09 | CLAIM TYPE ADDED | CHANGE DP DESC | Unassigned |              |

  #####################################################   Scenario-5  #############################################################
  @WithoutRFPUI 
  Scenario Outline: Without RFP scenario for change status for "<changeType>" and Preconditon is "<DPType>" with UI
    Given User "<User>" logged into "" application with Services
    When "<User>" sets data for "<changeType>" with "<DPType>"
    Then Create the change presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign change dps to created profile
    And User posts change opportunity data with "<changeType>"
    Then User verifies data for changed opportunity with "<changeType>","<expeLLChange>" for "<Criteria>"
    Given User "<User>" logged into "PM" application
    When user selects "clientkey" from Client drop down list
    Then user should select "Change Opportunities" tab
    Then user validates change summary data for "<changeType>" for "<Criteria>"

    Examples: 
      | User         | changeType       | DPType         | Criteria    | expeLLChange |
      | iht_ittest09 | RECAT DP         | CHANGE DP DESC | Without RFP |              |
      | iht_ittest09 | CLAIM TYPE ADDED | CHANGE DP DESC | Without RFP |              |

  #####################################################   Scenario-6  #############################################################
  @WithRFPUI 
  Scenario Outline: RFP scenario for change status for "<changeType>" and Preconditon is "<DPType>" with UI
    Given User "<User>" logged into "" application with Services
    When "<User>" sets data for "<changeType>" with "<DPType>"
    Then Create the change presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign change dps to created profile
    Given User "<User>" logged into "PM" application
    When user selects "clientkey" from Client drop down list
    Then user should select "Change Opportunities" tab
    Then User validates the "CaptureDecision" of Dp's for the Profile in "Approve"
    And User posts change opportunity data with "<changeType>"
    Then User verifies data for changed opportunity with "<changeType>","<expeLLChange>" for "<Criteria>"
    Then user validates change summary data for "<changeType>" for "<Criteria>"

    Examples: 
      | User         | changeType       | DPType         | Criteria | expeLLChange |
      | iht_ittest09 | RECAT DP         | CHANGE DP DESC | With RFP |              |
      | iht_ittest09 | CLAIM TYPE ADDED | CHANGE DP DESC | With RFP |              |
