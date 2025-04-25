# HMRC Vehicle Information Test Framework

A BDD Web Automation framework for testing HMRC vehicle information services using Selenium WebDriver, Cucumber, and Spring.

## Project Overview

This framework is designed to automate testing of vehicle information services, implementing Behavior Driven Development (BDD) principles using Cucumber with Java. It provides a robust, maintainable, and scalable test automation solution.

## Technology Stack

- Java
- Selenium WebDriver
- Cucumber (BDD)
- Spring Framework
- Maven
- TestNG/JUnit
- Logback (Logging)

## Project Structure

```
src/
├── main/java/
│   ├── dao/          # Data Access Objects
│   ├── pages/        # Page Object Models
│   ├── services/     # Core Services (Driver, File operations)
│   └── util/         # Utilities and Helpers
├── main/resources/   # Configuration and Test Data
└── test/
    ├── java/
    │   ├── config/   # Test Configuration (Spring, Cucumber)
    │   ├── runner/   # Test Runners (TestNG, JUnit)
    │   ├── steps/    # Step Definitions for BDD
    │   └── unittest/ # Unit Tests
    └── resources/
        ├── features/ # Cucumber Feature Files
        ├── drivers/  # WebDriver Executables
        └── logback-test.xml  # Logging Configuration
```

## Key Features

- Page Object Model design pattern
- Behavior Driven Development using Cucumber
- Spring Framework for dependency injection
- Cross-browser testing support
- Data-driven testing capabilities
- Excel data handling utilities
- Robust driver management
- Detailed reporting with Cucumber reports
- Configurable test properties
- Logging implementation

## Prerequisites

- Java JDK 8 or higher
- Maven
- Chrome/Firefox/Edge browser
- IDE (IntelliJ IDEA recommended)

## Getting Started

1. Clone the repository
2. Install dependencies:
   ```bash
   mvn clean install
   ```
3. Update `config.properties` with your test configuration
4. Run tests:
   ```bash
   mvn test
   ```

## Test Execution

### Running Tests
```bash
mvn clean test
```

### Running Specific Features
```bash
mvn test -Dcucumber.options="--tags @your-tag"
```

## Test Reports

Test reports are generated in the following locations:
- Cucumber HTML Report: `target/cucumber-reports/cucumber.html`
- JSON Report: `target/cucumber-reports/cucumber.json`
- Pretty Format: `target/cucumber-reports/cucumber-pretty`

## Configuration

Configuration can be modified in:
- `src/main/resources/config.properties`
- `src/test/resources/cucumber.properties`

## Framework Components

### Page Objects
- `BasePage`: Common web element operations
- `VehicleEnquiryPage`: Vehicle search functionality
- `VehicleInformationPage`: Vehicle details display
- `VehicleInfoLandingPage`: Landing page operations

### Services
- `DriverServices`: WebDriver initialization and management
- `FileServices`: File operations and data handling

### Utilities
- `PropertyLoader`: Configuration property management
- `XlsxUtility`: Excel file operations

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contact

For any queries or support, please raise an issue in the repository.
