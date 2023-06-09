package Automation.Access.dash.helpers;

public class GenieOptionsEnum
{

    public enum BusinessType
    {

        NewPendingBusiness("New Pending Business"),
        VerifiedBusiness("Verified Business");

        private final String type;

        BusinessType(String type)
        {
            this.type = type;
        }

        public String getBusinessType()
        {
            return type;
        }
    }

    public enum ScenarioOption
    {

        Integrations("Integrations"),
        MultiRole("MultiRole"),
        Budget("Budget"),
        Claim("Claim"),
        Card("Card"),
        AccountingIntegrations("AccountingIntegrations"),
        BusinessSettings("BusinessSettings"),
        KybVerified("KybVerified"),
        KycVerified("KycVerified"),
        DebitAccounts("DebitAccounts");

        private final String option;

        ScenarioOption(String option)
        {
            this.option = option;
        }

        public String getScenarioOption()
        {
            return option;
        }
    }
}
