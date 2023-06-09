package Automation.Utils;

import Automation.Utils.Enums.DateRequired;
import Automation.Utils.Enums.RangeType;
import org.apache.commons.lang3.StringUtils;
import org.jboss.aerogear.security.otp.Totp;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataGenerator
{

    public static HashMap<String, String> month = new HashMap<String, String>();

    /**
     * Compare dates in different format. this function take input date and input format, change it to some expected format to compare with today, tomorrow or any other date.
     *
     * @param testConfig           the test config
     * @param inputFormat-->       input date Format.
     * @param expectedFormat       --> expected date format.
     * @param dateToBeCompared     --> Date which need to be compared the date to be compared
     * @param dateForComparison--> date with which input date need to be compared, e.g. current date,tomorrow date,last month date
     * @return true, if successful
     */
    public static boolean compareDatesInDifferentFormat(Config testConfig, String inputFormat, String expectedFormat, String dateToBeCompared, String dateForComparison)
    {
        try
        {
            DateFormat currentTimeFormatReceived = new SimpleDateFormat(inputFormat);
            Date inputDate = currentTimeFormatReceived.parse(dateToBeCompared);
            Calendar cal = Calendar.getInstance();
            DateFormat requiredFormat = new SimpleDateFormat(expectedFormat);
            String inputDateInExpectedFormat = requiredFormat.format(inputDate);
            String comparingDate = null;

            switch (dateForComparison)
            {
                case "currentDate":
                    comparingDate = requiredFormat.format(cal.getTime());
                    break;
                case "LastMonth":
                    cal.add(Calendar.MONTH, -1);
                    comparingDate = requiredFormat.format(cal.getTime());
                    break;
                case "Tomorrow":
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    comparingDate = requiredFormat.format(cal.getTime());
                    break;
                default:
                    comparingDate = dateForComparison;
                    break;
            }

            if (inputDateInExpectedFormat.equalsIgnoreCase(comparingDate))
            {
                testConfig.logComment("Input " + inputDateInExpectedFormat + " and expected dates are matched with " + comparingDate);
                return true;
            } else
            {
                testConfig.logComment("Input " + inputDateInExpectedFormat + " and expected date " + comparingDate + " are Not matched");
                return false;
            }
        } catch (Exception e)
        {
            return false;
        }

    }

    /**
     * Convert date in string to LocalDate format (example - 14/07/2019 to LocalDate format (ex: dd/MM/yyyy), will return 14/07/2019 in LocalDate)
     *
     * @param dateInString - date in String
     * @param formatTime   - expected format date
     * @return - input string that already converted to by LocalDate
     */
    public static LocalDate convertStringToLocalDateGivenFormat(String dateInString, String formatTime)
    {
        DateTimeFormatter DTF = DateTimeFormatter.ofPattern(formatTime);
        LocalDate localDate = java.time.LocalDate.parse(dateInString, DTF);
        return localDate;
    }

    /**
     * Convert date Time in string to LocalDateTime format (example - 14/07/2019T13:15:22.222Z to LocalDate format (ex: dd/MM/yyyy'T'HH:mm:ss.SSS'Z'), will return 14/07/2019T13:15:22.222 in LocalDateTime)
     *
     * @param dateInString - date in String
     * @param formatTime   - expected format date
     * @return - input string that already converted to by LocalDateTime
     */
    public static LocalDateTime convertStringToLocalDateTimeGivenFormat(String dateInString, String formatTime)
    {
        DateTimeFormatter DTF = DateTimeFormatter.ofPattern(formatTime);
        LocalDateTime localDateTime = java.time.LocalDateTime.parse(dateInString, DTF);
        return localDateTime;
    }

    /**
     * Convert date Time from WIB to UTC format (example - 2022-07-29T00:00:00+0700, will return 2022-07-28T17:00:00Z in UTC)
     *
     * @param dateInString - date in String
     * @return - input string that is converted to UTC format
     */
    public static String convertDateTimeToUTCDateTime(String dateInString)
    {
        String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
        org.joda.time.format.DateTimeFormatter dtf = DateTimeFormat.forPattern(pattern);
        return dtf.parseDateTime(dateInString).toDateTime(DateTimeZone.UTC).toString().replace(".000", "");
    }

    public static String covertTime12HrsFormat(String hours, String min)
    {
        String formattedTime = null;
        try
        {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(hours + ":" + min);
            formattedTime = _12HourSDF.format(_24HourDt);
        } catch (Exception e)
        {
            formattedTime = null;
        }
        return formattedTime;
    }

    /*
     * Generating OTP from the QR code
     */
    public static String generateOtp(String key)
    {
        Totp authOtp = new Totp(key);
        String twoFaCode = authOtp.now();
        return twoFaCode;
    }

    /**
     * Generate a random Alpha-Numeric string of given length
     *
     * @param length - Length of string to be generated
     * @return - string
     */
    public static String generateRandomAlphaNumericString(int length)
    {
        Random rd = new Random();
        String aphaNumericString = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++)
        {
            sb.append(aphaNumericString.charAt(rd.nextInt(aphaNumericString.length())));
        }
        return sb.toString();
    }

    /**
     * Generate a random decimal number
     *
     * @param lowerBound    - int
     * @param upperBound    - int
     * @param decimalPlaces - int
     * @return an decimal number between that bound upto given decimal points
     */
    public static String generateRandomDecimalValue(int lowerBound, int upperBound, int decimalPlaces)
    {
        Random random = new Random();
        double dbl;
        dbl = random.nextDouble() * (upperBound - lowerBound) + lowerBound;
        return String.format("%." + decimalPlaces + "f", dbl);
    }

    /**
     * Generate a random guid
     *
     * @return guid as a string
     */
    public static String generateRandomGuid()
    {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * Generate a random number of given length
     *
     * @param length - Length of number to be generated
     * @return long
     */
    public static long generateRandomNumber(int length)
    {
        long randomNumber = 1;
        int retryCount = 1;
        int totalCounts = 1;
        // retryCount added for generating specified length's number
        while (retryCount > 0 && totalCounts < 10)
        {
            String strNum = Double.toString(Math.random());
            strNum = strNum.replace(".", "");

            if (strNum.length() > length)
            {
                strNum = strNum.substring(1, length + 1);
            } else
            {
                int remainingLength = length - strNum.length() + 1;
                randomNumber = generateRandomNumber(remainingLength);
                strNum = strNum.concat(Long.toString(randomNumber));
            }

            try
            {
                randomNumber = Long.parseLong(strNum);
            } catch (NumberFormatException e)
            {
                randomNumber = 0;
            }
            if (String.valueOf(randomNumber).length() < length)
            {
                retryCount++;
                totalCounts++;
            } else
            {
                retryCount = 0;
            }
        }
        return randomNumber;
    }

    public static int generateRandomNumberInIntRange(int min, int max)
    {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    /**
     * Generate a random Special Character string of given length
     *
     * @param length - Length of string to be generated
     * @return - generated string
     */
    public static String generateRandomSpecialCharacterString(int length)
    {
        Random rd = new Random();
        String specialCharString = "~!@#$%^*()_<>?/{}[]|\";";
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++)
        {
            sb.append(specialCharString.charAt(rd.nextInt(specialCharString.length())));
        }
        return sb.toString();
    }

    /**
     * Generate a random Alphabets string of given length
     *
     * @param length - Length of string to be generated
     * @return - generated string
     */
    public static String generateRandomString(int length)
    {
        Random rd = new Random();
        String aphaNumericString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++)
        {
            sb.append(aphaNumericString.charAt(rd.nextInt(aphaNumericString.length())));
        }
        return sb.toString();
    }

    public static String getCurrentDate(String format)
    {
        // get current date
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentDateTime()
    {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
    }

    public static String getCurrentDateTime(String format)
    {
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateNow = formatter.format(currentDate.getTime());
        return dateNow;
    }

    public static String getCurrentDateTimeInUTC(String format)
    {
        return LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(format));
    }

    // get current time in given format
    public static String getCurrentTime(String format)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String currentTime = formatter.format(cal.getTime());
        return currentTime;
    }

    /**
     * Gets the specific date from current date.
     *
     * @param format                   the format
     * @param dateRequired             the date required
     * @param timeUnitsFromCurrentDate the time units from current date
     * @return the specific date
     */
    public static String getSpecificDate(String format, DateRequired dateRequired, int... timeUnitsFromCurrentDate)
    {
        SimpleDateFormat dtf = new SimpleDateFormat(format);
        dtf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        return dtf.format(getDate(format, dateRequired, timeUnitsFromCurrentDate));
    }

    /**
     * @param format                   : format in which date is needed
     * @param dateRequired             : what type of date needed, current or some specific months current date
     * @param timeUnitsFromCurrentDate : -ve for past value and +ve for future value
     * @return : return date in date format
     */
    public static String getDate(String format, DateRequired dateRequired, int... timeUnitsFromCurrentDate)
    {
        SimpleDateFormat dtf = new SimpleDateFormat(format);
        dtf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        Calendar cal = Calendar.getInstance();
        String dateToBeReturned = null;
        int timeUnit = 0;
        if (timeUnitsFromCurrentDate.length > 0)
        {
            timeUnit = timeUnitsFromCurrentDate[0];
        }
        switch (dateRequired)
        {
            case CurrentDate:
                dateToBeReturned = dtf.format(cal.getTime());
                break;

            case FutureDate:
                cal.add(Calendar.DAY_OF_YEAR, timeUnit);
                dateToBeReturned = dtf.format(cal.getTime());
                break;

            case MonthsBeforeCurrentDate:
                cal.add(Calendar.MONTH, timeUnit);
                dateToBeReturned = dtf.format(cal.getTime());
                break;

            case MonthsAfterCurrentDate:
                cal.add(Calendar.MONTH, timeUnit);
                dateToBeReturned = dtf.format(cal.getTime());
                break;
        }

        return dateToBeReturned;
    }

    /**
     * @param initialFormat - initial format of date
     * @param finalFormat   - final format in which date is required
     * @param date          - input date
     * @param increment     - unit and duration which needs to be added to date
     * @return - generated date time in string
     */
    public static String getDateTime(String initialFormat, String finalFormat, String date, HashMap<String, Integer> increment)
    {
        DateTime datetime;
        if (StringUtils.isEmpty(date))
        {
            datetime = DateTime.now(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT+07:00")));
        } else
        {
            datetime = DateTime.parse(date, DateTimeFormat.forPattern(initialFormat));
        }

        if (increment != null)
        {
            for (String unit : increment.keySet())
            {
                switch (unit)
                {
                    case "days":
                        datetime = datetime.plusDays(increment.get(unit));
                        break;
                    case "minutes":
                        datetime = datetime.plusMinutes(increment.get(unit));
                        break;
                    case "hours":
                        datetime = datetime.plusHours(increment.get(unit));
                        break;
                    case "seconds":
                        datetime = datetime.plusSeconds(increment.get(unit));
                        break;
                    case "weeks":
                        datetime = datetime.plusWeeks(increment.get(unit));
                        break;
                    case "months":
                        datetime = datetime.plusMonths(increment.get(unit));
                        break;
                    case "years":
                        datetime = datetime.plusYears(increment.get(unit));
                        break;
                    default:
                        break;
                }
            }
        }
        return datetime.toString(finalFormat);
    }

    public static String getTimeinMillSeconds()
    {
        Date date = new Date();
        long time = date.getTime();
        return String.valueOf(time);
    }

    public static String retrieveIndonesianMonthFormat(String date)
    {
        performDataOperationOnHashMap(1);
        String expValue = "";
        String[] dateReq = date.split("-");
        for (Map.Entry<String, String> entry : month.entrySet())
        {
            if (dateReq[1].equals(entry.getKey()))
            {
                expValue = entry.getValue();
                break;
            }
        }
        expValue = dateReq[0] + "-" + expValue + "-" + dateReq[2];
        performDataOperationOnHashMap(0);
        return expValue;
    }

    public static void performDataOperationOnHashMap(int selection)
    {
        if (selection == 1)
        {
            month.put("Jan", "Jan");
            month.put("Feb", "Feb");
            month.put("Mar", "Mar");
            month.put("Apr", "Apr");
            month.put("May", "Mei");
            month.put("Jun", "Jun");
            month.put("Jul", "Jul");
            month.put("Aug", "Agt");
            month.put("Sep", "Sep");
            month.put("Oct", "Okt");
            month.put("Nov", "Nov");
            month.put("Dec", "Des");
        } else
        {
            month.clear();
        }
    }

    public static List<String> getStartAndEndDateTimeBetweenRange(RangeType rangeType)
    {
        String format = "dd/MM/yyyy HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime input = LocalDateTime.now();
        List<String> dateRange = new LinkedList<>();
        LocalDateTime start, end;
        switch (rangeType)
        {
            case ThisWeek:
                start = input.minusWeeks(0).with(DayOfWeek.MONDAY);
                end = start.plusDays(6);
                dateRange.add(start.format(formatter));
                dateRange.add(end.format(formatter));
                break;
            case LastWeek:
                start = input.minusWeeks(1).with(DayOfWeek.MONDAY);
                end = start.plusDays(6);
                dateRange.add(start.format(formatter));
                dateRange.add(end.format(formatter));
                break;
            case ThisMonth:
                start = input.withDayOfMonth(1);
                end = input.plusMonths(1).withDayOfMonth(1).minusDays(1);
                dateRange.add(start.format(formatter));
                dateRange.add(end.format(formatter));
                break;
            case LastMonth:
                start = input.minusMonths(1).withDayOfMonth(1);
                end = input.withDayOfMonth(1).minusDays(1);
                dateRange.add(start.format(formatter));
                dateRange.add(end.format(formatter));
                break;
            default:
                break;
        }
        return dateRange;
    }

    public static <T extends Enum<?>> T getRandomEnum(Class<T> className)
    {
        int x = new Random().nextInt(className.getEnumConstants().length);
        return className.getEnumConstants()[x];
    }

    public static String getRandomAlphaNumberStringFixedLengthWithSpace(int len, int spaceLen)
    {
        return generateRandomFixedLengthWithSpace("1234567890" + "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ", len, spaceLen);
    }

    public static String generateRandomFixedLengthWithSpace(String alphabet, int len, int spaceLen)
    {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        int space = 1;
        for (int i = 0; i < len; i++)
        {
            sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
            if (space >= spaceLen)
            {
                sb.append(" ");
                space = 1;
            } else
            {
                space++;
            }
        }

        return sb.toString();
    }

    public static String getDateFromToday(int days, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        return sdf.format(cal.getTime());
    }
}
