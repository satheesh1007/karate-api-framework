@acceptance
Feature: Post Budget Line Item
  Scenario: Create a budget line item and print the response
    Given url baseUrl
    #And path '/EnterpriseDesktop/api/v2/budgets/f1ee589c-2960-4b30-a655-66e84fbcffce/lineitems?sessionId=c464c4c755e045728d240fe0f35d525b'
    And request
      """
      {
        "division": "2262",
        "costCode": "2373",
        "costType": 6,
        "estimatedStart": "2025-04-10T18:30:00.000Z",
        "estimatedEnd": "2025-04-28T18:30:00.000Z",
        "curve": 0,
        "originalAmount": "10,000",
        "uomId": 15,
        "unitQuantity": 100,
        "unitCost": "100.00",
        "status": 0,
        "description": "",
        "budgetUniqueId": "77c84f76-6d71-4a3e-8174-e4b5f535256b"
      }
      """
    When method post
    Then status 200
    * print 'Response:', response


