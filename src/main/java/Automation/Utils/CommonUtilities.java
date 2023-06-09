package Automation.Utils;

import Automation.Utils.Api.ApiDetails;
import Automation.Utils.Api.ApiHelper;
import Automation.Utils.Enums.ProjectName;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.text.PDFTextStripper;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.zeroturnaround.zip.ZipUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Base64.Decoder;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtilities
{

    private static final int BUFFER_SIZE = 4096;

    /**
     * This function is used to encrypt the message
     *
     * @param plainMessage - message which needs to be encrypt
     * @return encrypted message
     */
    public static byte[] encryptMessage(String plainMessage)
    {
        try
        {
            String key = new String(Base64.getDecoder().decode(System.getProperty("ThanosToken").getBytes()));
            IvParameterSpec iv = new IvParameterSpec(key.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(plainMessage.getBytes());
            return Base64.getEncoder().encode(encrypted);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Convert Delimiter sperated string for eg String1,String2,String3 to String array
     *
     * @param inputString - input string
     * @param delimiter   - separator
     * @return array of string
     */
    public static String[] convertDelimiterSeparatedStringToArray(String inputString, String delimiter)
    {
        if (!inputString.trim().equals(""))
        {
            return inputString.split(delimiter);
        } else
        {
            return null;
        }
    }

    /**
     * This function return the URL of a file on runtime depending on LOCAL or Remote Execution
     *
     * @param fileUrl - path of file
     * @return - string
     */
    public static String convertFilePathToHtmlUrl(String fileUrl)
    {
        String finalPath = fileUrl;
        if (fileUrl.contains("RegressionResults"))
        {
            finalPath = "http:" + fileUrl.replace(File.separator, "/").replace("RegressionResults/", "Results/").replace("file:/", "/");
        } else if (fileUrl.contains(System.getProperty("user.dir") + File.separator + ".." + File.separator + "SharedResults"))
        {
            finalPath = fileUrl.replace(System.getProperty("user.dir") + File.separator + ".." + File.separator + "SharedResults", "http://10.100.11.30");
        } else if (fileUrl.contains("LocalResults") && fileUrl.contains(File.separator + "html"))
        {// Handling execution via GenerateTestNGXmlAndRun class but with remoteExecution off
            finalPath = fileUrl.replace(File.separator, "/");
            finalPath = "//" + finalPath;
        } else if ((fileUrl.contains("LocalResults") || fileUrl.contains("test-output")) && !fileUrl.contains("file://"))
        {// Handling local execution via Eclipse
            finalPath = fileUrl.replace(File.separator, "/");
            finalPath = "file://" + finalPath;
        }

        // Use QA Dashboard file path
        if (finalPath.contains("http://10.100.11.30"))
        {
            finalPath = finalPath.replace("http://10.100.11.30", "https://qa-dashboard.staging.example.com");
        }
        return finalPath;
    }

    /**
     * convert json array to map
     *
     * @param jsonArray  - json array to be converted to map
     * @param initialKey - string value that needs to be added as prefix to all the keys
     * @return map
     */
    public static HashMap<String, String> convertJsonArrayToHashMap(JSONArray jsonArray, String initialKey)
    {
        HashMap<String, String> hashmap = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++)
        {
            String key = String.valueOf(i);
            if (!StringUtils.isEmpty(initialKey))
            {
                key = initialKey + "." + key;
            }
            Object obj = jsonArray.get(i);
            if (obj instanceof JSONObject jsonobj)
            {
                hashmap.putAll(convertJsonObjectToHashMap(jsonobj, key));
            } else if (obj instanceof String)
            {
                hashmap.put(key, (String) obj);
            }
        }
        return hashmap;
    }

    /**
     * convert a json to map
     *
     * @param jsonObject - json to be converted to map
     * @param initialKey - string value which needs to be added as prefix to all the keys in resultant map
     * @return map
     */
    public static HashMap<String, String> convertJsonObjectToHashMap(JSONObject jsonObject, String initialKey)
    {
        HashMap<String, String> hashmap = new HashMap<>();
        Iterator<String> keysItr = jsonObject.keys();
        while (keysItr.hasNext())
        {
            String key = keysItr.next();
            Object value = jsonObject.get(key);
            if (value.toString().equals("null"))
            {
                value = "";
            }
            if (!StringUtils.isEmpty(initialKey))
            {
                key = initialKey + "." + key;
            }
            if (value instanceof String)
            {
                hashmap.put(key, (String) value);
            } else if (value instanceof JSONObject)
            {
                hashmap.putAll(convertJsonObjectToHashMap((JSONObject) value, key));
            } else if (value instanceof JSONArray)
            {
                hashmap.putAll(convertJsonArrayToHashMap((JSONArray) value, key));
            } else
            {
                hashmap.put(key, String.valueOf(value));
            }
        }
        return hashmap;
    }

    public static String convertPDFContentToText(Config testConfig, String strURL)
    {
        String parsedText = null;
        try
        {
            File file = new File(strURL);
            PDDocument document = Loader.loadPDF(file);

            AccessPermission ap = document.getCurrentAccessPermission();
            if (!ap.canExtractContent())
            {
                testConfig.logWarning("You do not have permission to extract text from pdf");
            }

            PDFTextStripper stripper = new PDFTextStripper();
            for (int pageNumber = 1; pageNumber <= document.getNumberOfPages(); ++pageNumber)
            {
                // Set the page interval to extract. If you don't, then all pages would be extracted.
                stripper.setStartPage(pageNumber);
                stripper.setEndPage(pageNumber);
                parsedText = stripper.getText(document);
            }
        } catch (Exception e)
        {
            testConfig.logWarning("Unable to parse the PDF! " + e.getMessage());
        }

        testConfig.logComment("+++++++++++++++++++++++++++++++++++++++++++++++++++");
        testConfig.logComment(parsedText);
        testConfig.logComment("+++++++++++++++++++++++++++++++++++++++++++++++++++");
        return parsedText;
    }

    /**
     * This Method is used to copy a particular file from one location to another or copy a directory from one location to another with all sub directories and files in it
     *
     * @param testConfig  - object of config
     * @param source      - path from where you want to copy
     * @param destination - path where you want to paste
     * @param isFolder    - true or false
     * @return true or false
     */
    public static boolean copyFileOrFolder(Config testConfig, String source, String destination, boolean isFolder)
    {
        File filesource = new File(source);
        File fileDestination = new File(destination);
        boolean result = false;
        try
        {
            if (isFolder)
            {
                FileUtils.copyDirectory(filesource, fileDestination);
            } else
            {
                FileUtils.copyFile(filesource, fileDestination);
            }
            Log.logCommentWithOptionalConfig(testConfig, "File copied successfully from : " + source + " to : " + destination);
            result = true;
        } catch (IOException e)
        {
            Log.logCommentWithOptionalConfig(testConfig, "Exception while copiing successfully from : " + source + " to : " + destination);
            e.printStackTrace();
        }
        return result;
    }

    public static String createFileInResultsDirectory(Config testConfig, String subDirectoryName)
    {
        String fileName = testConfig.getRunTimeProperty("resultsDirectory") + File.separator + subDirectoryName + File.separator + testConfig.testcaseName + "_" + DataGenerator.generateRandomAlphaNumericString(15) + "_" + new SimpleDateFormat("HH-mm-ss").format(new Date());
        CommonUtilities.createFolder(testConfig, fileName.substring(0, fileName.lastIndexOf(File.separator)));
        return fileName;
    }

    /**
     * This Method is used to create folder at given path
     *
     * @param testConfig - object of config
     * @param path       - path of folder which we need to create
     * @return true or false
     */
    public static boolean createFolder(Config testConfig, String path)
    {
        File newdir = new File(path);
        boolean result = false;
        if (!newdir.exists())
        {
            try
            {
                Files.createDirectories(Paths.get(path));
                Log.logCommentWithOptionalConfig(testConfig, "Directory created successfully : " + path);
                result = true;
            } catch (Exception e)
            {
                Log.logCommentWithOptionalConfig(testConfig, "Exception while creating Directory : " + path);
                e.printStackTrace();
            }
        } else
        {
            Log.logCommentWithOptionalConfig(testConfig, "Directory: " + path + " already Exist");
            result = true;
        }
        return result;
    }

    /**
     * This Method is used to delete folder at given path
     *
     * @param testConfig - object of config
     * @param path       - file path
     * @return true or false
     */
    public static boolean deleteFolder(Config testConfig, String path)
    {

        File newdir = new File(path);
        boolean result = false;
        if (newdir.exists())
        {
            try
            {
                FileUtils.deleteDirectory(new File(path));
                Log.logCommentWithOptionalConfig(testConfig, "Directory deleted successfully : " + path);
                result = true;
            } catch (Exception se)
            {
                Log.logCommentWithOptionalConfig(testConfig, "Exception while deleting Directory : " + path);
                se.printStackTrace();
            }
        } else
        {
            Log.logCommentWithOptionalConfig(testConfig, "Directory: " + path + " does not exist");
            result = true;
        }
        return result;
    }

    /**
     * Convert date time into desired format.
     *
     * @param inDate        - string
     * @param inDateFormat  - string
     * @param outDateFormat - string
     * @return outDate - string
     */
    public static String formatDate(String inDate, String inDateFormat, String outDateFormat)
    {
        SimpleDateFormat inSDF = new SimpleDateFormat(inDateFormat);
        SimpleDateFormat outSDF = new SimpleDateFormat(outDateFormat);
        String outDate = "";
        if (inDate != null)
        {
            try
            {
                Date date = inSDF.parse(inDate);
                outDate = outSDF.format(date);
            } catch (ParseException ex)
            {
            }
        }
        return outDate;
    }

    public static Calendar convertDateStringToCalendar(String dateStr, String timeFormat)
    {
        try
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat(timeFormat).parse(dateStr));
            return calendar;
        } catch (ParseException e)
        {
            return null;
        }
    }

    public static String formatNumber(String number)
    {
        return formatNumber(Double.parseDouble(number));
    }

    public static String formatNumber(double number)
    {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }

    /**
     * format the string as json
     *
     * @param input - string
     * @return formatted json string
     */
    @SuppressWarnings("deprecation")
    public static String formatStringAsJson(String input)
    {
        return new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(new JsonParser().parse(input));
    }

    public static String getPublicIP(Config testConfig)
    {
        BufferedReader in = null;
        String[] url = {"http://checkip.amazonaws.com", "https://api.ipify.org/", "https://myexternalip.com/raw", "https://ipecho.net/plain"};
        String ip = null;
        for (int i = 0; i < url.length; i++)
        {
            try
            {
                URL whatismyip = new URL(url[i]);
                in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
                ip = in.readLine().trim();
                Log.logCommentWithOptionalConfig(testConfig, "Current Machine's IP Address:- '" + ip + "'");
            } catch (Exception ioe)
            {
                Log.logCommentWithOptionalConfig(testConfig, "Unable to get the IP. Trying again...");
                WaitHelper.waitForSeconds(testConfig, 2);
                ioe.printStackTrace();
            } finally
            {
                if (in != null)
                {
                    try
                    {
                        in.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    if (ip != null)
                    {
                        break;
                    }
                }
            }
        }
        return ip;
    }

    /**
     * get the sha1 hash of the passed string
     *
     * @param input - string
     * @return sha1 hash
     */
    public static String getShaHash(String input)
    {
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            return DatatypeConverter.printHexBinary(messageDigest.digest(input.getBytes())).toLowerCase();
        } catch (Exception e)
        {
            return null;
        }
    }

    public static boolean listContainsString(List<String> list, String stringToMatch)
    {
        Iterator<String> iter = list.iterator();
        while (iter.hasNext())
        {
            String tempString = iter.next();
            if (tempString.contains(stringToMatch))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Mask the sensitive data
     *
     * @param testConfig    - object of config
     * @param sensitiveData - object
     * @return text with masked card detail if present
     */
    @SuppressWarnings("unchecked")
    public static String maskSensitiveDetails(Config testConfig, Object sensitiveData)
    {
        List<ProjectName> projectsWithSensitiveDetails = Arrays.asList(ProjectName.Access, ProjectName.Payments, ProjectName.SaaS, ProjectName.CustomerFrontend);
        if (!StringUtils.equalsIgnoreCase(testConfig.getRunTimeProperty("environment"), "production") || StringUtils.isEmpty(sensitiveData.toString()) || !projectsWithSensitiveDetails.contains(ProjectName.valueOf(testConfig.getRunTimeProperty("projectName").toUpperCase())))
        {
            return sensitiveData.toString();
        }
        if (sensitiveData instanceof HashMap)
        {
            HashMap<String, String> originalData = (HashMap<String, String>) sensitiveData;
            HashMap<String, String> maskedData = new HashMap<>();
            for (String key : originalData.keySet())
            {
                String value = originalData.get(key);
                switch (key)
                {
                    case "card_number":
                    case "card_cvv":
                    case "card_exp_month":
                    case "card_exp_year":
                    case "Authorization":
                        maskedData.put(key, maskString(value));
                        break;
                    default:
                        maskedData.put(key, value);
                        break;
                }
            }
            return maskedData.toString();
        } else if (sensitiveData instanceof String)
        {
            if (testConfig.getRunTimeProperty("ProjectName").equalsIgnoreCase("iris") || testConfig.getRunTimeProperty("ProjectName").equalsIgnoreCase("platform") || testConfig.getRunTimeProperty("ProjectName").equalsIgnoreCase("reporting"))
            {
                JSONObject jsonObject = new JSONObject(sensitiveData.toString());
                String temp;
                if (jsonObject.has("password"))
                {
                    temp = maskString(jsonObject.get("password").toString());
                    jsonObject.put("password", temp);
                }
                if (jsonObject.has("apiKey"))
                {
                    temp = maskString(jsonObject.get("apiKey").toString());
                    jsonObject.put("apiKey", temp);
                }
                return jsonObject.toString();
            } else
            {
                String originalString = (String) sensitiveData;
                String maskedcard = "$1********$2";
                Pattern cardPattern = Pattern.compile("\\b([0-9]{2})[0-9]{8,14}([0-9]{2})\\b");
                Matcher matcher = cardPattern.matcher(originalString);
                if (matcher.find())
                {
                    return matcher.replaceAll(maskedcard);
                }
            }
        }
        return sensitiveData.toString();
    }

    /**
     * This Method is used to mask String with * leaving first 2 and last 2 characters of the String
     *
     * @param strText - string to be masked
     * @return String - string after masking
     */
    public static String maskString(String strText)
    {
        if (StringUtils.isEmpty(strText))
        {
            return "";
        }
        StringBuilder sbMaskString;
        int strLen = strText.length();
        int start = 2, end = strLen - 2;
        int maskLength = end - start;
        if (maskLength <= 0)
        {
            sbMaskString = new StringBuilder(strLen);
            for (int i = 0; i < strLen; i++)
            {
                sbMaskString.append("*");
            }
            return sbMaskString.toString();
        } else
        {
            sbMaskString = new StringBuilder(maskLength);
            for (int i = 0; i < maskLength; i++)
            {
                sbMaskString.append("*");
            }
            return strText.substring(0, start) + sbMaskString + strText.substring(start + maskLength);
        }
    }

    public static <T, E> T getMapKeyByValue(Map<T, E> map, E value)
    {
        for (Entry<T, E> entry : map.entrySet())
        {
            if (Objects.equals(value, entry.getValue()))
            {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Method is used to convert ASCII value to HEX
     *
     * @param testConfig - Pass object of config
     * @param str        - string
     * @return - string
     */
    public static String convertStringToHex(Config testConfig, String str)
    {
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString(chars[i]));
        }
        testConfig.logComment("Ascii value converted to HEX.");
        return hex.toString();
    }

    /**
     * Method is used to convert HEX value to ASCII
     *
     * @param testConfig - Pass object of config
     * @param hex        - string
     * @return - string
     */
    public static String convertHexToString(Config testConfig, String hex)
    {
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < hex.length() - 1; i += 2)
        {
            // grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            // convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            // convert the decimal to character
            sb.append((char) decimal);
            temp.append(decimal);
        }
        testConfig.logComment("Decimal : " + temp);
        return sb.toString();
    }

    /**
     * Method is used to convert IDR price value to float
     *
     * @param price - Pass String of price in IDR
     * @return - Float
     */
    public static Float parsePriceToFloat(String price)
    {
        return Float.parseFloat(StringUtils.replaceEach(price, new String[]{"Rp", ".", " "}, new String[]{"", "", ""}).replace(",", "."));
    }

    /**
     * Method is used to download report/image for http link without using Webdriver It can be run on linux machines not having any browser
     *
     * @param testConfig - Pass object of config
     * @param apiDetails - Containing http url
     * @param filename   - Destination file
     * @return - void
     */
    public static void downloadUrlAsFile(Config testConfig, ApiDetails apiDetails, String outputPath, String filename)
    {
        File outputFile = new File(outputPath, filename);
        RestAssured.urlEncodingEnabled = false;
        ApiHelper apiHelper = new ApiHelper(testConfig);
        Response response = apiHelper.executeRequestAndGetResponse(apiDetails, null);
        if (response.getStatusCode() == 200)
        {

            if (outputFile.exists())
            {
                outputFile.delete();
            }
            byte[] fileContents = response.getBody().asByteArray();
            OutputStream outStream = null;
            try
            {
                outStream = new FileOutputStream(outputFile);
                outStream.write(fileContents);
            } catch (Exception e)
            {
                testConfig.logFail("Error writing file " + outputFile.getAbsolutePath());
            } finally
            {
                if (outStream != null)
                {
                    try
                    {
                        outStream.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Method is used to download report/image for http link without using Webdriver It can be run on linux machines not having any browser
     *
     * @param testConfig - Pass object of config
     * @param apiDetails - Containing http url
     * @return - void
     */
    public static void downloadZipFileFromUrl(Config testConfig, ApiDetails apiDetails, String outputPath, String zipFileName)
    {
        RestAssured.urlEncodingEnabled = false;
        ApiHelper apiHelper = new ApiHelper(testConfig);
        Response response = apiHelper.executeRequestAndGetResponse(apiDetails, null);
        if (response.getStatusCode() == 200)
        {
            InputStream inputStream = response.getBody().asInputStream();
            FileOutputStream fileOutputStream = null;
            try
            {
                fileOutputStream = new FileOutputStream(outputPath + File.separator + zipFileName);
                byte[] b = new byte[BUFFER_SIZE];
                int count;
                while ((count = inputStream.read(b)) >= 0)
                {
                    fileOutputStream.write(b, 0, count);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
                ZipUtil.unpack(new File(outputPath + File.separator + zipFileName), new File(outputPath));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static JSONObject getJsonObjectFromJsonFile(String jsonFilePath)
    {
        try
        {
            String content = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            return new JSONObject(content);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the base 64 encode of String passed in parameter.
     *
     * @return the base 64 encode string
     */
    public static String getBase64EncodeString(String curl)
    {
        return java.util.Base64.getEncoder().encodeToString(curl.getBytes());
    }

    public static String getBase64EncodeString(byte[] message)
    {
        return java.util.Base64.getEncoder().encodeToString(message);
    }

    public static String getJsonStringFromJsonFile(Config testConfig, String completeFilePath)
    {
        String jsonString = "";
        try
        {
            byte[] dataFromFile = FileUtils.readFileToByteArray(new File(completeFilePath));
            jsonString = new String(dataFromFile, Charset.defaultCharset());
        } catch (Exception e)
        {
            testConfig.logFail("Not able to get JSON String from JSON File.");
        }
        return jsonString;
    }

    public static void putJsonStringToJsonFile(Config testConfig, String completeFilePath, String dataToWrite)
    {
        try
        {
            FileWriter file = new FileWriter(completeFilePath);
            file.write(dataToWrite);
            file.close();
        } catch (IOException e)
        {
            testConfig.logExceptionAndFail(e);
        }
    }

    /**
     * Generates a SHA512 hex code; used for bni_topup signature
     *
     * @param baseString
     * @return
     */
    public static String sha512Hex(String baseString)
    {
        try
        {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            final byte[] bytes = messageDigest.digest(baseString.getBytes(StandardCharsets.ISO_8859_1));
            return new String(Hex.encode(bytes));
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static org.json.simple.JSONObject fetchJsonWithUiLocatorsAsPerRequiredView(String jsonFileName, String locatorFilePath)
    {
        File fileDirectory = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "Automation" + File.separator + locatorFilePath + File.separator + "ElementLocators");
        String fileName = jsonFileName + ".json";
        FileReader read = null;
        try
        {
            read = new FileReader(fileDirectory + File.separator + fileName);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        JSONParser jsonParser = new JSONParser();
        Object obj = null;
        try
        {
            obj = jsonParser.parse(read);
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e)
        {
            e.printStackTrace();
        }
        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
        if (Config.isMobileViewExecution)
        {
            jsonObject = (org.json.simple.JSONObject) jsonObject.get("Mobile");
        } else
        {
            jsonObject = (org.json.simple.JSONObject) jsonObject.get("Web");
        }

        return jsonObject;
    }

    /*
    This function is to generate file from encrypted data on runtime.
     */
    public static String createFileForEncryptedData(Config testConfig, String encryptedData, String expectedFileName)
    {
        String filePath = null;
        if (encryptedData.contains("Encrypted:"))
        {
            encryptedData = encryptedData.split("Encrypted:")[1];
        }
        testConfig.logCommentForDebugging("User directory: " + System.getProperty("user.dir"));
        if (new File(System.getProperty("user.dir") + File.separator + "pom.xml").exists())
        {
            filePath = System.getProperty("user.dir") + File.separator + "target" + File.separator + expectedFileName.split("\\.")[0];
        } else
        {
            filePath = System.getProperty("user.dir") + File.separator + "build" + File.separator + expectedFileName.split("\\.")[0];
        }
        CommonUtilities.createFolder(testConfig, filePath);
        String gcpFileName = filePath + File.separator + expectedFileName;
        testConfig.logCommentForDebugging("GCP File path: " + gcpFileName);
        try
        {
            FileWriter gcpFile = new FileWriter(gcpFileName);
            gcpFile.write(CommonUtilities.decryptMessage(encryptedData.getBytes()));
            gcpFile.close();
        } catch (IOException e)
        {
            testConfig.logExceptionAndFail(e);
            return null;
        }
        return gcpFileName;
    }

    /**
     * This function is used to decrypt the message
     *
     * @param encryptedMessage - message which needs to be decrypt
     * @return decrypted message
     */

    public static String decryptMessage(byte[] encryptedMessage)
    {
        try
        {
            Decoder decoder = Base64.getDecoder();
            String key = new String(decoder.decode(System.getProperty("ThanosToken")));
            IvParameterSpec iv = new IvParameterSpec(key.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(decoder.decode(encryptedMessage));
            return new String(original);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * The path normalization will deal with malicious inputs
     *
     * @param currentFilePath current file path
     * @return normalized path
     */
    public static String normalizePath(String currentFilePath)
    {
        Path normalizedPath = Paths.get(currentFilePath).normalize();
        return String.valueOf(normalizedPath);
    }

    /**
     * Get day of month suffix like: th, nd, rd,...
     *
     * @param dayOfMonth selected day
     * @return suffix
     */
    public static String getDayOfMonthSuffix(int dayOfMonth)
    {
        if (dayOfMonth >= 11 && dayOfMonth <= 13)
        {
            return "th";
        }
        switch (dayOfMonth % 10)
        {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    /**
     * Find file contains name, delete it and return true if file is created less than x minutes ago.
     */
    public static boolean findFileContainsName(Config testConfig, String folderPath, String fileNameToSearch, String fileType, int minutes)
    {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        testConfig.logComment("Check if file is created less than 3 minutes ago. Delete it to clean up space.");
        if (files != null)
        {
            for (File file : files)
            {
                String fileName = file.getName();
                if (fileName.contains(fileNameToSearch) && fileName.endsWith(fileType.toLowerCase()) && (System.currentTimeMillis() - file.lastModified()) / 60000 < minutes)
                {
                    file.delete();
                    return true;
                }
            }
        }
        return false;
    }

}
