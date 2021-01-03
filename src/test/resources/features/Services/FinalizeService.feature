@RestServices
Feature: Finalize- Rest Service

  @FinalizeService
  Scenario Outline: Finalize the captured decisions based on the status for Decision "<Decision>"
    Given User "<User>" logged into "" application with Services
    Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Single" DPKey
    #Given the "<User>" moving the data from CPW to PM through the service for the client "<Client>" with "Multiple" DPKey
    Then Create the presenatation profile with given request data client "<Client>" payershort "" lob "" product "" and prority "" with "<User>"
    Then assign multiple dps to created profile
    #Then Capture the decision with status "Approve"
    Then Capture the decision "<Decision>" with one or more status for client "<Client>"

    #Then Finalize the decision based on "Finalize"
    Examples: 
      | User         | Client         | Decision |
      | iht_ittest05 | Aetna Medicaid | Approve  |
		  #| iht_ittest05 | Aetna Medicaid | Approve with Mod |
		  #| iht_ittest05 | Aetna Medicaid | Reject           |
		  #| iht_ittest05 | Aetna Medicaid | Defer            |
 
