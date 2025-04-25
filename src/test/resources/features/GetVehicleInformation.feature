Feature: Verify vehicle information
  As a user
  I want to verify vehicle information from DVLA
  So that I can ensure the data matches our records

  @ui
  Scenario: Verify all the vehicle information on DVLA from the external source
    Given I have vehicle information stored in "xlsx" file in "config" folder
    And I have retrieved all the vehicle information from "VehicleData" file
    When I navigate to vehicle information page and verify vehicle data
    Then All the vehicle information should match the data from the external source