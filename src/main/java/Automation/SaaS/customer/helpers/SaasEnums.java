package Automation.SaaS.customer.helpers;

import Automation.Utils.DataGenerator;

public class SaasEnums
{

    public enum BillStatus
    {
        Paid("Paid"),
        Draft("Draft"),
        All("All");

        private final String status;

        BillStatus(String status)
        {
            this.status = status;
        }

        public String getStatus()
        {
            return status;
        }
    }

    public enum BudgetIcon
    {
        CustomerSupport("departments.customer-support"),
        Finance("departments.finance"),
        Hr("departments.hr"),
        LegalOrCompliance("departments.legal-or-compliance"),
        Marketing("departments.marketing"),
        Operations("departments.operations"),
        Product("departments.product"),
        Sales("departments.sales"),
        Tech("departments.tech"),
        EmployeeWelfare("expense-category.employee-welfare"),
        Inventory("expense-category.inventory"),
        MealsAndEntertainment("expense-category.meals-and-entertainment"),
        OfficeSupplies("expense-category.office-supplies"),
        Pantry("expense-category.pantry"),
        ProfessionalServices("expense-category.professional-services"),
        Software("expense-category.software"),
        Transport("expense-category.transport"),
        Travel("expense-category.travel"),
        Utilities("expense-category.utilities"),
        Event("others.event"),
        GeneralExpenses("others.general-expenses"),
        Projects("others.project");

        private final String icon;

        BudgetIcon(String icon)
        {
            this.icon = icon;
        }

        public String getIcon()
        {
            return icon;
        }

        public static String getRandomIcon()
        {
            return BudgetIcon.values()[DataGenerator.generateRandomNumberInIntRange(0, BudgetIcon.values().length - 1)].icon;
        }
    }

    public enum BudgetApiFrequency
    {
        OneTime("bullet"),
        Monthly("monthly"),
        Quarterly("quarterly");
        private final String frequency;

        BudgetApiFrequency(String frequency)
        {
            this.frequency = frequency;
        }

        public String getBudgetFrequency()
        {
            return frequency;
        }

        public static BudgetApiFrequency getRandomBudgetFrequency()
        {
            return BudgetApiFrequency.values()[DataGenerator.generateRandomNumberInIntRange(0, BudgetApiFrequency.values().length - 1)];
        }
    }

    public enum PersonTypeEnum
    {
        Themself("THEMSELF"),
        OtherPerson("OTHER_PERSON"),
        OtherAdmin("OTHER_ADMIN"),
        VerificationPending("VERIFICATION_PENDING"),
        Random("RANDOM");

        private final String name;

        PersonTypeEnum(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return this.name;
        }
    }

    public enum CardColor
    {
        SpringGreen("Spring Green", "#01D167", "1, 209, 103"),
        BerryBlue("Berry Blue", "#536DFF", "83, 109, 255"),
        BlazeOrange("Blaze Orange", "#FF5757", "255, 87, 87"),
        RubyRed("Ruby Red", "#D10960", "209, 9, 96"),
        IrisPurple("Iris Purple", "#7F46FF", "127, 70, 255"),
        MidnightBlue("Midnight Blue", "#092843", "9, 40, 67");

        private final String name;
        private final String color;
        private final String rgb;


        CardColor(String name, String color, String rgp)
        {
            this.name = name;
            this.color = color;
            this.rgb = rgp;
        }

        public String getName()
        {
            return this.name;
        }

        public String getColor()
        {
            return this.color;
        }

        public String getRgb()
        {
            return this.rgb;
        }

        public static CardColor getRandomColor()
        {
            return CardColor.values()[DataGenerator.generateRandomNumberInIntRange(0, CardColor.values().length - 1)];
        }
    }

    public enum CardFrequency
    {
        Daily("daily"),
        Weekly("weekly"),
        Monthly("monthly"),
        Quarterly("quarterly"),
        Yearly("yearly"),
        OneTime("one-time");
        private final String frequency;

        CardFrequency(String frequency)
        {
            this.frequency = frequency;
        }

        public String getCardFrequency()
        {
            return frequency;
        }

        public static CardFrequency getRandomCardFrequency()
        {
            return CardFrequency.values()[DataGenerator.generateRandomNumberInIntRange(0, CardFrequency.values().length - 1)];
        }
    }

    public enum CardTypeEnum
    {
        VirtualPhysical("GPR_VIR_UP2PHY"),
        Virtual("GPR_VIR"),
        Physical("Physical");

        private final String cardType;

        CardTypeEnum(String name)
        {
            this.cardType = name;
        }

        public String getCardType()
        {
            return cardType;
        }
    }

    public enum DebitCategory
    {
        Entertainment("Entertainment"),
        FoodAndBeverages("Food & Beverages");

        private final String category;

        DebitCategory(String category)
        {
            this.category = category;
        }

        public String getCategory()
        {
            return category;
        }

        public static DebitCategory getRandomCategory()
        {
            return DebitCategory.values()[DataGenerator.generateRandomNumberInIntRange(0, DebitCategory.values().length - 1)];
        }
    }

    public enum ClaimModelType
    {
        DebitAccount("debit_account"),
        Budget("budget");

        private final String modelType;

        ClaimModelType(String modelType)
        {
            this.modelType = modelType;
        }

        public String getModelType()
        {
            return modelType;
        }
    }

    public enum BudgetMemberType
    {
        Admin("Admin"),
        Owner("owner"),
        Member("member");

        private final String memberType;

        BudgetMemberType(String memberType)
        {
            this.memberType = memberType;
        }

        public String getMemberType()
        {
            return memberType;
        }
    }

    public enum BudgetMemberState
    {
        Accepted("accepted"),
        Revoked("revoked");

        private final String state;

        BudgetMemberState(String state)
        {
            this.state = state;
        }

        public String getState()
        {
            return state;
        }
    }

    public enum BudgetState
    {
        Active("BDAC"),
        Deactivated("BDDA");

        private final String state;

        BudgetState(String state)
        {
            this.state = state;
        }

        public String getState()
        {
            return state;
        }
    }

    public enum FileMimeType
    {
        APPLICATION_PDF("application/pdf"),
        IMAGE_JPG("image/jpg"),
        IMAGE_PNG("image/png"),
        IMAGE_JPEG("image/jpeg"),
        CSV("csv");

        private final String mimeType;

        FileMimeType(String mimeType)
        {
            this.mimeType = mimeType;
        }

        public String getName()
        {
            return this.mimeType;
        }
    }

    public enum SourceOfFund
    {
        IdrDebitAccount("IDR account"),
        SgdDebitAccount("SGD account"),
        UsdDebitAccount("USD account"),
        AdvanceLimit("Advance limit");

        private final String accountName;

        SourceOfFund(String accountName)
        {
            this.accountName = accountName;
        }

        public String getAccountName()
        {
            return accountName;
        }
    }

    public enum BudgetRole
    {
        Admin("You’re an Admin"),
        OwnerTransfer("You’re a Budget owner"),
        OwnerNoTransfer("You’re a Budget owner"),
        Owner("You’re a Budget owner"),
        Member("You’re a Budget member");

        private final String roleLabel;

        BudgetRole(String roleLabel)
        {
            this.roleLabel = roleLabel;
        }

        public String getRoleLabel()
        {
            return roleLabel;
        }
    }

    public enum BudgetUiFrequency
    {
        OneTime("One-time"),
        RecurringMonthly("Monthly"),
        RecurringQuarterly("Quarterly");

        private final String frequencyName;

        BudgetUiFrequency(String frequency)
        {
            this.frequencyName = frequency;
        }

        public String getFrequencyName()
        {
            return frequencyName;
        }

        public static BudgetUiFrequency getByName(String name)
        {
            for (BudgetUiFrequency frequency : BudgetUiFrequency.values())
            {
                if (frequency.getFrequencyName().equals(name))
                {
                    return frequency;
                }
            }
            return BudgetUiFrequency.valueOf(name);
        }
    }

    public enum PaymentLinkStatus
    {
        Due("Due"),
        Overdue("Overdue"),
        Expired("Expired"),
        Paid("Paid"),
        Unpaid("Unpaid");

        private final String status;

        PaymentLinkStatus(String status)
        {
            this.status = status;
        }

        public String getStatus()
        {
            return status;
        }
    }

    public enum ClaimState
    {
        PaymentScheduled("CMPS"),
        PendingReview("CMPE"),
        PendingPayment("CMTB"),
        Paid("CMPA"),
        Rejected("CMRE"),
        PaymentProcessing("CMPP");

        private final String code;

        ClaimState(String code)
        {
            this.code = code;
        }

        public String getCode()
        {
            return code;
        }
    }

    public enum XeroAccountingType
    {
        Xero("xero"),
        Netsuite("netsuite");

        private final String type;

        XeroAccountingType(String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return type;
        }
    }

    public enum ReportingFileType
    {
        Text("text"),
        Dropdown("dropdown");

        private final String type;

        ReportingFileType(String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return type;
        }
    }

    public enum CardTransactionType
    {
        Debit("DEBIT"),
        Reversal("REVERSAL");

        private final String type;

        CardTransactionType(String type)
        {
            this.type = type;
        }

        public String getCardTransactionType()
        {
            return type;
        }
    }

    public enum ExportModule
    {
        Budgets("Budgets"),
        Cards("Cards"),
        Claims("Claims"),
        Invoices("Invoices"),
        Bills("Bills"),
        AccountStatements("Account Statements"),
        MoneyIn("Money In"),
        MoneyOut("Money Out"),
        ;

        private final String module;

        ExportModule(String module)
        {
            this.module = module;
        }

        public String getModule()
        {
            return module;
        }
    }

    public enum ExportDateRange
    {
        Last30Days("Last 30 days"),
        Last60Days("Last 60 days"),
        Last90Days("Last 90 days"),
        Custom("Custom"),
        All("All");

        private final String range;

        ExportDateRange(String range)
        {
            this.range = range;
        }

        public String getRange()
        {
            return range;
        }
    }

    public enum FileType
    {PDF, XLSX, CSV}

    public enum ExportFormat
    {Default, Xero, Netsuite, SeeAll}

    public enum ClaimStatus
    {
        Pending("Pending"), Approved("Approved"), Rejected("Rejected"), Paid("Paid"), Scheduled("Scheduled"), Processing("Processing"), Cancelled("Cancelled"), All("All");

        private final String status;

        ClaimStatus(String status)
        {
            this.status = status;
        }

        public String getStatus()
        {
            return status;
        }
    }

    public enum InvoiceState
    {
        Due("Due"),
        Draft("Draft"),
        Paid("Paid");

        private final String state;

        InvoiceState(String state)
        {
            this.state = state;
        }

        public String getState()
        {
            return state;
        }
    }
}
