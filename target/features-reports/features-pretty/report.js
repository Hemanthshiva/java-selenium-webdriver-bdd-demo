$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("features/GetVehicleInformation.feature");
formatter.feature({
  "name": "Verify vehicle information",
  "description": "",
  "keyword": "Feature"
});
formatter.scenario({
  "name": "Verify all the vehicle information on DVLA from the external source",
  "description": "",
  "keyword": "Scenario",
  "tags": [
    {
      "name": "@tag"
    }
  ]
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "I have vehicle information stored in \"xlsx\" file in \"config\" folder",
  "keyword": "Given "
});
formatter.match({
  "location": "GetVehicleInfoSteps.iHaveVehicleInformationStoredInFileInFolder(String,String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "I have retrieved all the vehicle information from \"VehicleData\" file",
  "keyword": "And "
});
formatter.match({
  "location": "GetVehicleInfoSteps.iHaveRetrievedAllTheVehicleInformationFromFile(String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "I navigate to vehicle information page and verify vehicle data",
  "keyword": "When "
});
formatter.match({
  "location": "GetVehicleInfoSteps.iNavigateToVehicleInformationPageAndVerifyVehicleData()"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "All the vehicle information should match the data from the external source",
  "keyword": "Then "
});
formatter.match({
  "location": "GetVehicleInfoSteps.allTheVehicleInformationShouldMatchTheDataFromTheExternalSource()"
});
formatter.result({
  "status": "passed"
});
formatter.after({
  "status": "passed"
});
});