package Automation.SaaS.api.budget;

import Automation.Access.customer.api.AccessJsonDetails;
import Automation.Access.customer.helpers.AccessEnums.BusinessType;
import Automation.Access.customer.helpers.AccessEnums.RoleType;
import Automation.Access.customer.helpers.AccessHelper;
import Automation.Payments.customer.api.PaymentJsonDetails;
import Automation.Payments.customer.helpers.PaymentEnums;
import Automation.Payments.customer.helpers.PaymentHelper;
import Automation.SaaS.customer.api.SaasJsonDetails;
import Automation.SaaS.customer.helpers.SaasHelper;
import Automation.Utils.Config;
import Automation.Utils.Enums.QA;
import Automation.Utils.TestBase;
import Automation.Utils.TestVariables;
import org.testng.annotations.Test;

public class TestCreateNewBudget extends TestBase
{

    @TestVariables(testrailData = "30:C4990", automatedBy = QA.Mukesh)
    @Test(description = "To verify that admin can create the budget", dataProvider = "getTestConfig", timeOut = DEFAULT_TEST_TIMEOUT, groups = {"regression", "apiCases"})
    public void testCreateNewBudget(Config testConfig)
    {
        int userDetailsRowNum = 1;
        SaasHelper saasHelper = new SaasHelper(testConfig, userDetailsRowNum);
        AccessHelper accessHelper = new AccessHelper(testConfig, userDetailsRowNum);
        PaymentHelper paymentHelper = new PaymentHelper(testConfig, userDetailsRowNum);

        testConfig.logStep("Execute Api /v1/auth/token then call API /v1/auth/login to setup information for next request");
        accessHelper.doAuthentication(AccessJsonDetails.AuthLoginOkSingleBusinessWithDebitAccount, BusinessType.Business);

        testConfig.logStep("Execute API /v1/debit-accounts with valid credentials and get debit_account_uuid info");
        paymentHelper.setDebitAccountUuid(PaymentJsonDetails.GetSingleDebitAccountResponse, PaymentEnums.CurrencyEnum.SGD);

        testConfig.logStep("Execute Api GetListOfEmployees to prepare the payload for creating budget and then execute Api /v1/budgets");
        saasHelper.createBudget(accessHelper, SaasJsonDetails.CreateBudgetSingleOwnerRequestSchema, RoleType.Director);
    }
}
