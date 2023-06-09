package Automation.Payments.dash.helpers;

public class DashPaymentEnums
{

    public enum CommandLine
    {
        InvoicingCheckInvoiceStates("invoicing:check-invoice-states");

        private final String command;

        CommandLine(String command)
        {
            this.command = command;
        }

        public String getCommand()
        {
            return this.command;
        }
    }

}
