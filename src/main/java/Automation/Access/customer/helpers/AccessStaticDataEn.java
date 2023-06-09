package Automation.Access.customer.helpers;

public class AccessStaticDataEn implements AccessStaticDataBase
{

    @Override
    public String getexampleApplication()
    {
        return "CNSING";
    }

    @Override
    public String getSecurityTitle()
    {
        return "Security";
    }

    @Override
    public String getSecurityChangePinCardTitle()
    {
        return "Change login password";
    }

    @Override
    public String getSecurityChange2faCardTitle()
    {
        return "2-step verification for login";
    }

    @Override
    public String getSecurityChangeDefaultOtpCardTitle()
    {
        return "Change default OTP delivery method";
    }

    @Override
    public String getLockedAttemptsMessage()
    {
        return "You have been blocked from logging into your example account for 30 minutes because of %s incorrect password attempts.";
    }

    @Override
    public String getLoginRemainingAttemptsError()
    {
        return "Oops, invalid credentials! For your safety, your account will be inaccessible for 30 mins after %s more failed attempt(s)";
    }

    @Override
    public String getOtpErrorMessage()
    {
        return "The OTP you have entered is either invalid or expired! For your safety, your account will be inaccessible for 30 minutes after %s more failed attempt(s).";
    }

    @Override
    public String getUsernameLabel()
    {
        return "Registered email or phone";
    }

    @Override
    public String getUserAccessPageTitle()
    {
        return "Users and Access";
    }

    @Override
    public String getPersonalDetailsTitle()
    {
        return "Personal Information";
    }

    @Override
    public String getFullNameAsPerIDTitle()
    {
        return "Full Name as per ID";
    }

    @Override
    public String getPreferredNameTitle()
    {
        return "Preferred name";
    }

    @Override
    public String getMobileNumberTitle()
    {
        return "Mobile number";
    }

    @Override
    public String getWorkEmailTitle()
    {
        return "Work email";
    }

    @Override
    public String getBankAccountDetailsTitle()
    {
        return "Bank Account Details";
    }

    @Override
    public String getEnabledPersonal2faSuccessMessage()
    {
        return "2-step verification has been turned on successfully";
    }

    @Override
    public String getDisabledPersonal2faSuccessMessage()
    {
        return "2-step verification has been turned off successfully";
    }

    @Override
    public String getEnabledOrganisation2faSuccessMessage()
    {
        return "Organisation 2-step verification has been turned on successfully";
    }

    @Override
    public String getDisabledOrganisation2faSuccessMessage()
    {
        return "Organisation 2-step verification has been turned off successfully";
    }

    @Override
    public String get2StepVerificationTitle()
    {
        return "2-step verification";
    }

    @Override
    public String get2StepVerificationDescriptiveText()
    {
        return "Your account is secured using 2-step verification. Please enter the verification code sent to %s to proceed with login.";
    }

    @Override
    public String getLoginPasswordChangedSuccessText()
    {
        return "Login Password changed successfully!";
    }

    @Override
    public String getLoginPasswordResetSuccessText()
    {
        return "Login Password reset successfully!";
    }

    @Override
    public String getEmailDefaultOtpDeliverySuccessMessage()
    {
        return "Default OTP delivery method has been changed to Email successfully!";
    }

    @Override
    public String getPhoneDefaultOtpDeliverySuccessMessage()
    {
        return "Default OTP delivery method has been changed to SMS successfully!";
    }

}
