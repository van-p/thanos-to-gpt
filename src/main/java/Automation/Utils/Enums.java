package Automation.Utils;

public class Enums
{

    /**
     * Used is GenerateTestNGXmlAndRun class
     */
    public enum ProjectName
    {
        Access("Access"),
        Payments("Payments"),
        SaaS("SaaS"),
        App("App"),
        CustomerFrontend("CustomerFrontend"),
        Dash("Dash");

        String testDataSheetToBeUsed;

        ProjectName(String testDataSheetToBeUsed)
        {
            this.testDataSheetToBeUsed = testDataSheetToBeUsed;
        }
    }

    /**
     * Used is Config class
     */
    public enum TestDataSheetToBeUsed
    {
        CommonTestData,
        CustomerPortalTestData,
        AdminPortalTestData
    }

    /**
     * Used is Browser class
     */
    public enum BrowserName
    {
        chrome,
        firefox
    }

    /**
     * Used is Database class
     */
    public enum DatabaseName
    {
        Thanos(1);

        public final int values;

        DatabaseName(final int value)
        {
            values = value;
        }
    }

    /**
     * Used is Database class
     */
    public enum QueryType
    {
        select,
        update,
        delete
    }

    /**
     * Used is DataGenerator class
     */
    public enum DateRequired
    {
        CurrentDate,
        FutureDate,
        MonthsBeforeCurrentDate,
        MonthsAfterCurrentDate,
    }

    /**
     * Used is DataGenerator class
     */
    public enum RangeType
    {
        Today,
        Yesterday,
        ThisWeek,
        LastWeek,
        ThisMonth,
        LastMonth
    }

    public enum MachineDetails
    {
        Linux1("34.87.185.58"),
        Linux2("34.87.82.171"),
        Linux3("35.197.153.88"),
        Linux4("35.247.144.165");

        private final String machineIpAddress;

        MachineDetails(String machineIpAddress)
        {
            this.machineIpAddress = machineIpAddress;
        }

        public String getMachineIpAddress()
        {
            return this.machineIpAddress;
        }
    }

    public enum DateTimeStringFormatEnum
    {

        DATE_FORMAT_TRANSFER("dd MMM yyyy");
        private final String format;

        DateTimeStringFormatEnum(String format)
        {
            this.format = format;
        }

        public String getFormat()
        {
            return this.format;
        }

    }

    public enum QA
    {
        Akankshi("akankshi@example.com"),
        Amit("amit.d@example.com"),
        Doan("doan.phan@example.com"),
        Duc("duc.do@example.com"),
        Himanshu("himanshu.g@example.com"),
        Lam("lam.tran@example.com"),
        Long("long.nguyen@example.com"),
        MinhTai("minhtai@example.com"),
        Mukesh("mukesh.r@example.com"),
        ThuyNga("nga.huynh@example.com"),
        NgaVu("nga.vu@example.com"),
        Alex("ngan@example.com"),
        Natali("ngoc.nguyen@example.com"),
        TanHo("phamtan@example.com"),
        Phong("phong.nguyen@example.com"),
        Erit("phuoc.ha@example.com"),
        Priya("priya.t@example.com"),
        Quan("quan.t@example.com"),
        Sanjeevan("sanjeevan.k@example.com"),
        Sindhupriya("sindhupriya.k@example.com"),
        Srimanth("srimanth.gs@example.com"),
        Sunil("sunil.m@example.com"),
        Loc("thienloc@example.com"),
        Thuc("thuc.n@example.com"),
        Trung("trung.t@example.com"),
        Truyen("truyen.kieu@example.com"),
        Van("van.p@example.com"),
        VinayKumar("vinaykumar.m@example.com");
        String email;

        QA(String email)
        {
            this.email = email;
        }

        public String getEmail()
        {
            return this.email;
        }
    }
}
