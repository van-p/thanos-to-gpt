package Automation.Access.customer.helpers;

import Automation.Utils.DataGenerator;

public class AccessEnums
{

    public enum BusinessType
    {
        Business("business"),
        Person("person"),
        Account("account"),
        DebitAccount("debit-account");

        private final String type;

        BusinessType(String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return type;
        }
    }

    public enum RoleType
    {
        Director("customers.director"),
        FinanceTransferOnly("customers.finance.transfer-only"),
        FinanceSubmitOnly("customers.finance.submit-only"),
        FinanceViewOnly("customers.finance.no-transfer-or-submit-right"),
        Employee("customers.card-only");

        private final String type;

        RoleType(String type)
        {
            this.type = type;
        }

        public static RoleType getRandomRoleType()
        {
            return RoleType.values()[DataGenerator.generateRandomNumberInIntRange(0, RoleType.values().length - 1)];
        }

        public String getType()
        {
            return type;
        }
    }

    public enum LoginType
    {
        IncorrectPassword3Times,
        IncorrectOtp3Times
    }

    public enum CompanyRole
    {
        Director("Director"),
        NonDirector("Non-director");

        private final String role;

        CompanyRole(String role)
        {
            this.role = role;
        }

        public String getRole()
        {
            return role;
        }
    }

    public enum AccessRole
    {
        Admin("Admin"),
        Finance("Finance"),
        Employee("Employee");

        private final String role;

        AccessRole(String role)
        {
            this.role = role;
        }

        public static AccessRole getRandomAccessRole()
        {
            return AccessRole.values()[DataGenerator.generateRandomNumberInIntRange(0, AccessRole.values().length - 1)];
        }

        public String getRole()
        {
            return role;
        }
    }

    public enum FinanceRights
    {
        FinanceTransferRights("Transfer without restrictions"),
        FinanceSubmitRights("Submit transfer requests only"),
        FinanceViewRights("Cannot make/submit transfers");

        private final String rights;

        FinanceRights(String rights)
        {
            this.rights = rights;
        }

        public static FinanceRights getRandomFinanceRights()
        {
            return FinanceRights.values()[DataGenerator.generateRandomNumberInIntRange(0, FinanceRights.values().length - 1)];
        }

        public String getRights()
        {
            return rights;
        }
    }

    public enum QuickAccessItem
    {
        OverviewGroup("Overview"),
        Accounts("Accounts"),
        MakeBulkTransfer("Make bulk transfers"),
        MakeATransfer("Make a transfer"),
        Transactions("Transactions"),
        ScheduleATransfer("Schedule a transfer"),
        Analytics("Analytics"),
        CreditLimit("Credit limit"),
        ApproveATransfer("Approve a transfer"),
        Statements("Statements"),
        ViewPendingTransfers("View pending transfers"),

        SpendManagementGroup("Spend"),
        Budgets("Budgets"),
        CreateABudget("Create a budget"),
        Cards("Cards"),
        IssueACard("Issue a card"),
        Claims("Claims"),
        SubmitAClaim("Submit a claim"),
        ReviewAClaim("Review a claim"),
        PayAClaim("Pay a claim"),
        Bills("Bills"),
        PayABill("Pay a bill"),
        AdvanceLimit("Advance limit"),

        ReceiveGroup("Receive payments"),
        Receivables("Receivables"),
        SendAnInvoice("Send an invoice"),
        AccountDetails("Account details"),
        PaynowDetails("PayNow details"),

        DirectoryGroup("Directory"),
        Recipients("Recipients"),
        AddARecipient("Add a recipient"),
        Customers("Customers"),

        UserAndAccessGroup("Users & Access"),
        Users("Users"),
        InviteAUser("Invite a user"),
        RolesAndPermissions("Roles and permissions"),

        AccountingGroup("Accounting"),
        Xero("Xero"),
        QuickBooks("QuickBooks"),
        NetSuite("NetSuite"),
        ExportStatement("Export statement"),

        RewardsGroup("Rewards"),
        CardCashBack("Card cashback"),
        Referrals("Referrals"),
        PartnerPerks("Partner perks"),

        HelpAndSupport("Help and support"),
        RequestAFeature("Request a feature"),
        ChatWithUs("Chat with us"),
        PaymentMethods("Payment methods"),
        FAQS("FAQs"),
        Aboutexample("example Blog"),
        AcceptableUsePolicies("Acceptable use policies"),
        ReportABug("Report a bug"),
        ScheduleACall("Schedule a call"),

        Settings("Settings"),
        BusinessDetails("Business details"),
        PersonalDetails("Person Details"),
        Security("Security"),
        Notifications("Notifications"),
        FileUploader("File uploader"),
        Contracts("Contracts"),
        Language("Language"),
        SubmitATransfer("Submit a transfer"),
        PaymentLinks("Payment links");

        private final String item;

        QuickAccessItem(String item)
        {
            this.item = item;
        }

        public String getItem()
        {
            return item;
        }
    }

    public enum AfterLoginExpectedLandingPage
    {
        LoginPage,
        AccountsListPage,
        DashBoardPage
    }

    public enum AfterNavigationPage
    {
        BudgetPage,
        DashBoardPage,
        AdvanceLimit,
        Claims,
        RecipientList,
        PaymentLinks,
        UserAccessPage,
        SecurityPage,
        BillPage,
        PersonalDetailsPage,
        UpdateEmailAddressPage,
        CardPage,
        InvoicesPage,
        ExportDataPage,
        XeroPage
    }

    public enum NavigationMenuItem
    {
        Overview("Overview"),
        Spend("Spend"),
        Receive("Receive"),
        Directory("Directory"),
        Accounting("Accounting"),
        UserAndAccess("Users & Access"),
        Settings("Settings");

        private String name;

        NavigationMenuItem(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        private void setName(String name)
        {
            this.name = name;
        }
    }

    public enum SubMenuItem
    {
        Transactions(NavigationMenuItem.Overview, "Transactions"),
        Claims(NavigationMenuItem.Spend, "Claims"),
        AdvanceLimit(NavigationMenuItem.Spend, "Advance limit"),
        Recipients(NavigationMenuItem.Directory, "Recipients"),
        Budgets(NavigationMenuItem.Spend, "Budgets"),
        Users(NavigationMenuItem.UserAndAccess, "Users"),
        Home(null, "Home"),
        PaymentLinks(NavigationMenuItem.Receive, "Payment links"),
        Bills(NavigationMenuItem.Spend, "Bills"),
        PersonalDetailsPage(NavigationMenuItem.Settings, "Personal Details"),
        Cards(NavigationMenuItem.Spend, "Cards"),
        Invoices(NavigationMenuItem.Receive, "Invoices"),
        ExportData(NavigationMenuItem.Accounting, "Export Data"),
        Xero(NavigationMenuItem.Accounting, "Xero");

        private final NavigationMenuItem parent;
        private final String name;

        SubMenuItem(NavigationMenuItem parent, String subMenuItem)
        {
            this.parent = parent;
            this.name = subMenuItem;
        }

        public NavigationMenuItem getParent()
        {
            return parent;
        }

        public String getName()
        {
            return name;
        }
    }

    public enum CustomerPortalLanguage
    {
        en,
        id,
        vi,
        defaultLanguage
    }

    public enum BudgetQuickActions
    {
        AddPeople("add-people"),
        UpdateLimit("update-limit"),
        MakeATransfer("make-payment"),
        IssueACard("issue-card"),
        SubmitAClaim("submit-claim");

        private final String quickAction;

        BudgetQuickActions(String quickAction)
        {
            this.quickAction = quickAction;
        }

        public String getName()
        {
            return this.quickAction;
        }
    }

    public enum UserNameType
    {
        Email,
        Phone
    }

    public enum OtpChannelType
    {
        Email,
        Phone
    }

    public enum ModelUuidsType
    {
        ApprovalPolicy("Approval policy", "9752c00d-3c36-42a6-adc9-87fd9185b078"),
        BillPay("BillPay", "9752c00d-28b1-4267-a129-3b44b52279f5"),
        Users("Users", "9752c00d-1814-48ba-a9d2-13029f6a1b88"),
        Claims("Claims", "9752c00d-1532-4e68-a522-df91b8fa6e22"),
        Budgets("Budgets", "9752c00d-146c-40b0-aea7-c7d225d7f476"),
        Cards("Cards", "9752c00d-10ae-412f-a16e-17ac7ab28d08"),
        PaymentLinks("Payment links", "96b1df94-21c6-4c1f-8219-7c36f4f0f69c"),
        AdvanceLimit("Advance limit", "96b1df94-16d9-42e6-a224-2f361976029c"),
        PayNow("PayNow", "96b1df94-155c-4c80-a2d7-5c2de9f76a3b"),
        Accounts("Accounts", "9752c00c-f6f8-44de-a100-c591a7e992fb");

        private final String modelName;
        private final String modelUuids;

        ModelUuidsType(String modelName, String modelUuids)
        {

            this.modelName = modelName;
            this.modelUuids = modelUuids;

        }

        public static ModelUuidsType getRandomFeatureType()
        {
            return ModelUuidsType.values()[DataGenerator.generateRandomNumberInIntRange(0, 9)];
        }

        public String getModelName()
        {
            return modelName;
        }

        public String getModelUuids()
        {
            return modelUuids;
        }
    }

    public enum Country_Uuid
    {
        SINGAPORE("Singapore", "9470e8a1-b676-45a1-8ca4-b233213e0160"),
        INDONESIA("Indonesia", "9470e8a1-b19f-4c0a-8799-3068257b3d9c");

        private final String type;
        private final String countryUuid;

        Country_Uuid(String type, String countryUuid)
        {
            this.type = type;
            this.countryUuid = countryUuid;
        }

        public static Country_Uuid getRandomCountry_Uuid()
        {
            return Country_Uuid.values()[DataGenerator.generateRandomNumberInIntRange(0, 1)];
        }

        public String getType()
        {
            return type;
        }

        public String getCountryUuid()
        {
            return countryUuid;
        }

    }

    public enum RestrictionType
    {
        RESTRICTED("restricted"),
        UNRESTRICTED("unrestricted"),
        EXPANDABLE("expandable"),
        DISABLED("disabled");

        private final String type;

        RestrictionType(String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return type;
        }
    }

    public enum BillingType
    {
        ACCESS("access"),
        METERED("metered");

        private final String type;

        BillingType(String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return type;
        }
    }

    public enum BillingFrequencyType
    {
        MONTHLY("monthly");

        private final String type;

        BillingFrequencyType(String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return this.type;
        }
    }

    public enum PlanUuidType
    {
        QUAN("98b0cfed-7d08-49a8-9f37-6c9bdc107003"),
        AUTOONE("9941f7ab-ddfb-4096-860a-22a5bb7e989f"),
        AUTOTWO("9941f7ce-bf12-42f8-a572-8d17f2a00b79");

        private final String type;

        PlanUuidType(String type)
        {
            this.type = type;
        }

        public static PlanUuidType getRandomPlanUuidType()
        {
            return PlanUuidType.values()[DataGenerator.generateRandomNumberInIntRange(0, 2)];
        }

        public String getType()
        {
            return type;
        }
    }
}