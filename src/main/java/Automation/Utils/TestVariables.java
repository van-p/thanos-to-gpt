package Automation.Utils;

import Automation.Utils.Enums.QA;
import Automation.Utils.Enums.TestDataSheetToBeUsed;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestVariables
{

    /**
     * @return the data sheet name to be used by the test case
     */
    String dataSheetName() default "ParameterData";

    String testrailData() default ""; //Format = suiteId:testcaseIds(comma separated)

    QA automatedBy() default QA.Mukesh;

    /**
     * @return the row numbers to be used by testcases
     */
    int[] dataSheetRowNum() default 0;

    /**
     * @return the workbook to be used by the test case
     */
    TestDataSheetToBeUsed testDataSheetToBeUsed() default TestDataSheetToBeUsed.CommonTestData;
}
