package Automation.Payments.customer.helpers;

public class PaymentStaticDataEn implements PaymentStaticDataBase
{

    public String OTP = "1234";

    @Override
    public String getReviewTransferPageTitle()
    {
        return "Review your transfer";
    }

    @Override
    public String getTransferPageTitle()
    {
        return "To";
    }

    @Override
    public String getOTP()
    {
        return OTP;
    }

    public enum SuccessMessageEnums
    {
        TransferApproved("Transfer has been approved. You can track it in the “Past/Upcoming” transactions tab."),
        TransferDeleted("Pending transfer deleted"),
        TransfersBulkApprove("Pending transfers approved. You can track them in the “Past/Upcoming” transactions tab."),
        TransferRejected("Pending transfer rejected"),
        RecipientDeleted("Recipient deleted"),
        TransferStopped("Recurring transfer successfully stopped"),
        TransferResumed("Recurring transfer successfully resumed"),
        NotResultFound("No results found with"),
        NoTransaction("Roses are red\n" + "Violets are blue\n" + "You haven't made a transaction\n" + "Seems like you're new! \uD83D\uDC4B");
        private final String name;

        SuccessMessageEnums(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

    }

    public enum FromSourceCurrencyEnum
    {
        FromUsdAccount("From USD Account"),
        FromSgdAccount("From SGD Account");
        private final String name;

        FromSourceCurrencyEnum(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

    public enum PendingTransferLabelEnum
    {
        PendingApproval("Pending approval");
        private final String name;

        PendingTransferLabelEnum(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

    public enum advanceAdditionalFeeModalMessages
    {
        AdditionalFeeHeading("Manage your cash flows with ease using advance limits"),
        AdditionalFeeSubHeading("Pay your business expenses via transfers from Advance limit for just a 2% fee");
        private final String name;

        advanceAdditionalFeeModalMessages(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

    @Override
    public String getRecipientListPageTitle()
    {
        return "Select recipient";
    }

    @Override
    public String getNewRecipientPageTitle()
    {
        return "New recipient";
    }

    @Override
    public String getChooseBusinessMessage()
    {
        return "Choose the business you would like to manage";
    }

    @Override
    public String getMakeTransferUsdFxOnWeekendMessage()
    {
        return "USD to EUR transfers aren't supported from Sat 5am to Mon 5:02am SGT. Please try again on Monday. We are working to enable 24/7 transfers for all currency pairs.";
    }

    @Override
    public String getBudgetLabel()
    {
        return "Budget (required)";
    }

    public enum TransactionButtonEnum
    {
        Manage("Manage"),
        GoToHome("Go to Home");
        private final String name;

        TransactionButtonEnum(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

    public enum AdditionalInformationForm
    {
        TellUsMoreHeading("Please tell us more!"),
        TellUsMoreSubHeading("This information will help us process your transfer faster.");
        private final String name;

        AdditionalInformationForm(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

    public enum TransferTypeButtonEnum
    {
        SubmitATransfer("Submit a transfer"),
        MakeATransfer("Make a transfer");
        private final String name;

        TransferTypeButtonEnum(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

    public enum FailedTransferDetailPage
    {
        title("Uh oh, transfer failed."),
        subtitle("Please check your recipient’s details.");
        private final String name;

        FailedTransferDetailPage(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

}
