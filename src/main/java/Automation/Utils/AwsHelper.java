package Automation.Utils;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.*;

public class AwsHelper
{

    public static AmazonS3 s3Client;
    public static PropertiesCredentials awsCredentials;

    /**
     * Connect to aws S 3.
     *
     * @param testConfig          - Pass object of config the test config
     * @param credentailsFilePath property file path where credentials are saved
     */
    @SuppressWarnings("deprecation")
    public static void connectToAwsS3(Config testConfig, String credentailsFilePath)
    {
        try
        {
            // to create aws s3 credentials object
            awsCredentials = new PropertiesCredentials(new FileInputStream(credentailsFilePath));
        } catch (IOException e)
        {
            testConfig.logExceptionAndFail("Error accessing AWS properties file.", e);
        }
        testConfig.logComment("AWS Credentials created.");

        // to create connection to aws s3 with given credentials.
        s3Client = new AmazonS3Client(awsCredentials);
        testConfig.logComment("AmazonS3Client created.");
    }

    /**
     * Upload csv to S 3.
     *
     * @param testConfig                  - Pass object of config the test config
     * @param awsFolderToUploadFile       - folder on aws bucket where file will be uploaded
     * @param csvFileNameWithPathToUpload - file name with path on local, from where file will be uploaded
     * @param awsBucketName               the aws bucket name
     * @return the string: returns file name uploaded.
     */
    public static String uploadCsvToS3(Config testConfig, String awsFolderToUploadFile, String csvFileNameWithPathToUpload, String awsBucketName)
    {
        // cannot use File.separator here as it would change it to backward slash on basis of OS but it needs to forward slash only irrespective of OS
        String bucketName = "/" + awsBucketName + "/" + awsFolderToUploadFile;
        File file = new File(csvFileNameWithPathToUpload);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, file.getName(), file);

        s3Client.putObject(putObjectRequest);
        testConfig.logComment("Csv File uploaded to s3 server : " + file.getName());
        return (file.getName());
    }

    /**
     * Download csv from S 3.
     *
     * @param testConfig                  - Pass object of config the test config
     * @param awsFolderForDownload        the aws folder for download
     * @param csvReturnFileNameToDownload - Name of file which will be searched and downloaded from aws
     * @param awsBucketName               the aws bucket name
     * @return - returns path of downloaded csv
     */
    public static String DownloadCsvFromS3(Config testConfig, String awsFolderForDownload, String csvReturnFileNameToDownload, String awsBucketName)
    {

        String bucketName = "/" + awsBucketName + "/" + awsFolderForDownload;
        testConfig.logComment("File Name to Download from S3 : " + csvReturnFileNameToDownload.substring(csvReturnFileNameToDownload.lastIndexOf(File.separator) + 1));
        S3Object object = null;
        int retryCounter = 0;
        boolean isFileFound = false;
        do
        {
            try
            {
                GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, csvReturnFileNameToDownload.substring(csvReturnFileNameToDownload.lastIndexOf(File.separator) + 1));
                object = s3Client.getObject(getObjectRequest);
                isFileFound = true;
            } catch (AmazonS3Exception e)
            {
                retryCounter++;
                WaitHelper.waitForSeconds(testConfig, 5);
                if (e.getStatusCode() == 404)
                {
                    testConfig.logComment("File not found on S3...Retrying..");
                }
            }
        }
        while (!isFileFound && retryCounter < 3);

        InputStream reader = new BufferedInputStream(object.getObjectContent());
        File file = new File(csvReturnFileNameToDownload);
        OutputStream writer;
        try
        {
            writer = new BufferedOutputStream(new FileOutputStream(file));
            int read = -1;

            while ((read = reader.read()) != -1)
            {
                writer.write(read);
            }
            writer.flush();
            writer.close();
            reader.close();
        } catch (FileNotFoundException e)
        {
            testConfig.logExceptionAndFail("Unable to create file to write response data", e);
        } catch (IOException e)
        {
            testConfig.logExceptionAndFail("Unable to access file to write response data", e);
        }

        testConfig.logComment("Csv File Downloadeded From s3 server : " + csvReturnFileNameToDownload.substring(csvReturnFileNameToDownload.lastIndexOf(File.separator) + 1));
        return csvReturnFileNameToDownload;
    }
}
