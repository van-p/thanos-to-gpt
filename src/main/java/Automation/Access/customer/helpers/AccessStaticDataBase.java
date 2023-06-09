package Automation.Access.customer.helpers;

public interface AccessStaticDataBase
{

    String getexampleApplication();

    String getSecurityTitle();

    String getSecurityChangePinCardTitle();

    String getSecurityChange2faCardTitle();

    String getSecurityChangeDefaultOtpCardTitle();

    String getLockedAttemptsMessage();

    String getLoginRemainingAttemptsError();

    String getOtpErrorMessage();

    String getUsernameLabel();

    String getUserAccessPageTitle();

    String getPersonalDetailsTitle();

    String getFullNameAsPerIDTitle();

    String getPreferredNameTitle();

    String getMobileNumberTitle();

    String getWorkEmailTitle();

    String getBankAccountDetailsTitle();

    String getEnabledPersonal2faSuccessMessage();

    String getDisabledPersonal2faSuccessMessage();

    String getEnabledOrganisation2faSuccessMessage();

    String getDisabledOrganisation2faSuccessMessage();

    String get2StepVerificationTitle();

    String get2StepVerificationDescriptiveText();

    String getLoginPasswordChangedSuccessText();

    String getLoginPasswordResetSuccessText();

    String getEmailDefaultOtpDeliverySuccessMessage();

    String getPhoneDefaultOtpDeliverySuccessMessage();
}
