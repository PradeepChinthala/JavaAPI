@RestServices @Sanity
Feature: BOSE- Rest Services

  @Sanity @QASmoketest
  Scenario Outline: Verify the Login Functionality with user "<User>"
    Given User "<User>" logged into "Ihealth123" application with Services
    Then Verify the user first name "<firstname>" and last name "<lastName>"
    Then user logout from the presentation manager application

    Examples: 
      | User         | firstname | lastName |
      | iht_ittest05 | Generic05 | ITTest   |
      | iht_ittest09 | Generic09 | ITTest   |

  @Sanity @QASmoke1
  Scenario Outline: Verify the Client Team data and clients assigned to user
    Given User "<User>" logged into "Ihealth123" application with Services
    And get Summary information for given client "<Client>"
    Then verify the clientTeamData assigned for given user "<User>"
    And verify the clients assigned for given user "<User>"
    And get the DP and Savings count for given client "<Client>"
    Then verify DP and LOB information for given Payershorts "<Payershort>" and Medical policies "<MedicalPolicy>" for client "<Client>"

    Examples: 
      | User         | Client         | MedicalPolicy | Payershort |
      | iht_ittest05 | Aetna Medicaid |               |            |

  @Sanity @testirest @PresProfileSrvc
  Scenario Outline: Create Presentation Profile
    Given User "<User>" logged into "Ihealth123" application with Services
    Then Create the presenatation profile with given request data client "<Client>" payershort "<Payershort>" lob "<LOB>" product "<Product>" and prority "<Priority>" with "<User>"

    Examples: 
      | User         | Client       | LOB                          | Payershort                          | Product    | Priority |
      | iht_ittest05 | Humana, Inc. | Commercial;Medicaid;Medicare | HUMCC;HUMCF;HUMCH;HUMCM;HUMCP;HUMIL | Outpatient | High     |

  #| iht_ittest05 | UnitedHealth Group        | Commercial;Medicaid;Medicare | HSPBR;HSPC1;HSPIL                   | Outpatient | High     |
  @Sanity @testinf
  Scenario Outline: Validate Payershort and Lobs
    Given User "<User>" logged into "Ihealth123" application with Services
    Given get all payer shorts and lobs for the client "<Client>" for given "<User>"

    Examples: 
      | User         | Client         | LOB                          | Payershort               | LOB        | Topics                        |
      | iht_ittest05 | Aetna Medicaid | Commercial;Medicaid;Medicare | 1204;1094;1101;1171;1112 | Commercial | 1186;1187;2557;2322;30;109;75 |

  @SanityEndtoEnd @CreatePresen_assignDP
  Scenario Outline: End to End - Capture Decision
    Given User "<User>" logged into "Ihealth123" application with Services
    #Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    Then the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Multiple" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "<Payershort>" lob "<LOB>" product "<Product>" and prority "<Priority>" with "<User>"
    #Then Assign created Dpkey to the profile
    Then assign multiple dps to created profile

    #Then Capture the decision with status "Approve"
    Examples: 
      | User         | Client         | LOB | Payershort | LOB | Topics |
      #| iht_ittest03 |  UnitedHealth Group			|Commercial;Medicaid;Medicare   |1204;1094;1101;1171;1112 | Commercial       |1186;1187;2557;2322;30;109;75 |
      | iht_ittest05 | Aetna Medicaid |     |            |     |        |

  @Sanity @Test1
  Scenario Outline: Verify the Client Team data and clients assigned to user
    Given User "<User>" logged into "Ihealth123" application with Services
    And get Summary information for given client "<Client>"

    Examples: 
      | User         | Client         |
      | iht_ittest05 | Aetna Medicaid |

  @ChangeOpty
  Scenario Outline: Verify the Client Team data and clients assigned to user
    Given User "<User>" logged into "Ihealth123" application with Services
    And Change Oppty Scenario for "<Change>" change

    Examples: 
      | User         | Change         |
      | iht_ittest05 | Aetna Medicaid |
