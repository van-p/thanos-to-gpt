package Automation.Payments.customer.helpers;

import java.util.List;
import java.util.Random;

public class PaymentEnums
{

    public enum Frequency
    {
        OneTime("One time"),
        Weekly("Weekly"),
        Monthly("Monthly");

        private final String name;

        Frequency(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return this.name;
        }
    }

    public enum TransferTitleEnum
    {
        TransferReceived("Transfer received"),
        TransferCompleted("Transfer completed"),
        ScheduledTransfer("Scheduled transfer"),
        RecurringTransfer("Recurring transfer"),
        TransferSubmitted("Transfer submitted"),
        TransferFundsConverted("Transfer funds converted"),
        ScheduledTransferSubmitted("Scheduled transfer submitted"),
        RecurringTransferSubmitted("Recurring transfer submitted"),
        TransferInitiated("Transfer initiated"),
        CardPayment("Card Payment"),
        TransferFailed("Transfer Failed");


        private final String name;

        TransferTitleEnum(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

    }

    public enum TransferCategory
    {
        Equipment("Equipment"),
        GovernmentServices("Government Services"),
        Inventory("Inventory"),
        Marketing("Marketing"),
        OfficeRental("Office rental"),
        Salary("Salary"),
        Sales("Sales"),
        ServiceProvider("Service provider"),
        Software("Software"),
        Taxes("Taxes"),
        Travel("Travel"),
        UncategorizedCardPayment("Uncategorized card payment"),
        Utilities("Utilities");

        private static final List<TransferCategory> values = List.of(values());
        private static final int size = values.size();
        private static final Random random = new Random();
        private final String name;

        TransferCategory(String name)
        {
            this.name = name;
        }

        public static TransferCategory randomTransferCategory()
        {
            return values.get(random.nextInt(size));
        }

        public String getName()
        {
            return name;
        }
    }

    public enum TransferType
    {
        Completed,
        Processing,
        Scheduled,
        Recurring,
        Initiated,
        Submitted,
        ScheduledSubmitted,
        RecurringSubmitted
    }

    public enum AccountType
    {
        local,
        fx,
        mca
    }

    public enum MakerCheckerFlowType
    {
        Approve,
        Reject,
        Modify,
        Delete
    }

    public enum CurrencyEnum
    {
        USD,
        SGD,
        GBP,
        EUR,
        IDR,
        VND,
        PHP,
        AED,
        OMR,
        INR,
        AUD,
        MYR,
        THB,
        CAD
    }

    public enum ExternalService
    {
        Neo("neo"),
        Dbs("dbs"),
        TransferWise("transferwise");

        private final String service;

        ExternalService(String service)
        {
            this.service = service;
        }

        public String getService()
        {
            return this.service;
        }
    }

    public enum NewOrModifyTransferEnum
    {
        newTransfer,
        modifyTransfer
    }

    public enum StatusEnum
    {
        Upcoming("Upcoming"),
        NextTransfer("Next transfer"),
        Stopped("Stopped");

        private final String name;

        StatusEnum(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return this.name;
        }
    }
}
