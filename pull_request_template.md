## Description
Briefly describe the purpose of your pull request by providing a summary of the changes you made.

## Automation Results Link
Provide the Results Link for Automation tests executed on this branch (not Jenkins build link)

## [Self Review Checklist](https://github.com/weexample/thanos/wiki/Self-Review-Pointers)
- [ ] I have used separate test users/accounts for each test case.
- [ ] I have ensured that unnecessary changes are not shown in code.
- [ ] I have stored URLs in a properties file.
- [ ] I have passed data as variables from test cases (for testdata not present in csv file)
- [ ] I have used descriptive variable names, avoiding abbreviations.
- [ ] I have avoided committing unused or commented code.
- [ ] I have preferred using CSS & element IDs/Names instead of XPATH for creating locators.
- [ ] I have avoided creating unnecessary variables and passed values directly if possible.
- [ ] I have removed any debugging code from the codebase.
- [ ] I have used assertElementText to verify text messages rather than assertElementDisplayed.
- [ ] I have used `CamelCase` for enums and `camelCase` for all the variables and functions
- [ ] I have checked for typos in code.
- [ ] I have named functions based on their actions.
- [ ] I have merged functions with similar actions by parameterizing them.
- [ ] I have only created functions in Helper classes if they interact with 2 or more page objects.
- [ ] Every closing activity in my code returns the next page (wherever applicable)
- [ ] I have used Thanos framework functions instead of direct calls to Selenium functions.
- [ ] I have used waitForElementToBeDisplayed instead of waitForPageLoad outside of Page constructor.
- [ ] I have used testConfig.logComment instead of testConfig.logStep outside of test cases.
- [ ] I have used plain English comments inside testConfig.logStep to explain test case flow and verifications.
- [ ] I have correctly put `testrailData` and `automatedBy` inside @TestVariables tag


## Additional Notes
Add any additional notes or comments that you would like to include.
