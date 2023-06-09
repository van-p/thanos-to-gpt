package Automation.Access.dash.helpers;

public class DashAccessEnums
{

    public enum StateCode
    {
        DraftPeople("PEDF"),
        NewPeople("PENE"),
        PendingPeople("PEPE"),
        VerifiedPeople("PEVE"),

        NewBusiness("BUNE"),
        PendingBusiness("BUPE"),
        VerifiedBusiness("BUVE"),

        EligibilityNotStarted("ELNS"),
        EligibilityRejected("ELRJ"),
        EligibilityApproved("ELAP");


        private final String state;

        StateCode(String state)
        {
            this.state = state;
        }

        public String getCode()
        {
            return state;
        }
    }

    public enum EntityType
    {
        Business("business"),
        Person("person");

        private final String name;

        EntityType(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

    public enum FileCollection
    {
        Passport("identity-documents"),
        Selfies("selfies"),
        ProofOfAddress("proof-of-address"),

        Constitution("constitution"),
        BoardResolution("board-resolution");

        private final String name;

        FileCollection(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

    public enum NavigationMenu
    {
        Businesses(null, "Businesses"),
        BusinessesBusinesses(Businesses, "Businesses"),
        BusinessesKYBs(Businesses, "KYBs"),
        BusinessesAffiliatePartner(Businesses, "AffiliatePartner"),
        BusinessesIntegrationRequests(Businesses, "Integration Requests"),

        People(null, "People"),
        PeoplePeople(People, "People"),
        PeopleKYCs(People, "KYCs"),
        PeopleDirectorInvitations(People, "Director Invitations"),
        PeopleMultiUserAccessInvitations(People, "Multi User Access invitations"),

        Debit(null, "Debit"),
        DebitCashback(Debit, "Cashback"),
        DebitCashbackPromotions(DebitCashback, "Promotions"),

        Settings(null, "Settings"),
        SettingsRisk(Settings, "Risk"),
        SettingRiskReviewsSettings(SettingsRisk, "Reviews settings"),
        SettingRiskReviewsSettingsScorecards(SettingRiskReviewsSettings, "scorecards"),
        SettingsApplicationsSettings(Settings, "Application Settings"),
        SettingsApplicationSettingsApplications(SettingsApplicationsSettings, "Applications"),
        SettingsApplicationSettingsBusinessGroups(SettingsApplicationSettingsApplications, "Business groups");

        private final NavigationMenu parent;
        private final String name;

        NavigationMenu(NavigationMenu parent, String subMenuItem)
        {
            this.parent = parent;
            this.name = subMenuItem;
        }

        public NavigationMenu getParent()
        {
            return parent;
        }

        public String getName()
        {
            return name;
        }
    }

    public enum BusinessType
    {
        FullFeature("FULL_FEATURE"),
        AdvanceOnly("ADVANCE_ONLY");

        private final String name;

        BusinessType(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }
}
