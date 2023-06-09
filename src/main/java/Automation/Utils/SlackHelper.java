package Automation.Utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;

/**
 * This class will be used for sending Slack Notifications
 *
 * @author mukesh.rajput
 */

public class SlackHelper
{

    public static ArrayList<String> slackThreads = new ArrayList<String>();

    //Default
    public static String botUserOAuthToken = CommonUtilities.decryptMessage(System.getProperty("SlackBotUserOAuthToken").getBytes());
    //Upgraded access
    public static String userOAuthToken = CommonUtilities.decryptMessage(System.getProperty("SlackUserOAuthToken").getBytes());

    public static String triggerSlackNotifications(String slackGroupName, String message, String attachmentFile, String slackChannelId)
    {
        String thread_ts = sendSlackMessage(slackGroupName, message, attachmentFile, slackChannelId);

        // Remove duplicates and send thread message
        Set<String> set = new HashSet<>(slackThreads);
        slackThreads.clear();
        slackThreads.addAll(set);
        for (int i = 0; i < set.size(); i++)
        {
            replyOnThread(slackGroupName, slackThreads.get(i), thread_ts);
        }
        return thread_ts;
    }

    public static String sendSlackMessage(String slackGroupName, String message, String attachmentFile, String... SlackChannelId)
    {
        boolean isMessageSent = false;
        Response response = null;
        String thread_ts = null;
        HashMap<String, String> apiParameters = new HashMap<String, String>();
        apiParameters.put("token", botUserOAuthToken);
        if (attachmentFile == null)
        {
            try
            {
                apiParameters.put("channel", slackGroupName);
                apiParameters.put("text", message);
                apiParameters.put("as_user", "true");
                String apiUrl = "https://slack.com/api/chat.postMessage";
                RequestSpecification reqspec = given().contentType(ContentType.URLENC);
                reqspec = reqspec.formParams(apiParameters);
                response = reqspec.when().post(apiUrl);
                JSONObject jsonObj = new JSONObject(response.asString());
                thread_ts = jsonObj.get("ts").toString();
                if (response.asString().contains("\"ok\":true,\"channel\":\""))
                {
                    isMessageSent = true;
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else
        {
            try
            {
                apiParameters.put("channels", slackGroupName);
                apiParameters.put("initial_comment", message);
                String apiUrl = "https://slack.com/api/files.upload";
                RequestSpecification reqspec = given();
                reqspec = reqspec.formParams(apiParameters);
                reqspec = reqspec.multiPart("file", new File(attachmentFile), "multipart/form-data");
                response = reqspec.when().post(apiUrl);

                JSONObject jsonObj = new JSONObject(response.asString());
                JSONArray p = (JSONArray) ((JSONObject) ((JSONObject) ((JSONObject) jsonObj.get("file")).get("shares")).get("public")).get(SlackChannelId[0]);
                thread_ts = ((JSONObject) p.get(0)).get("ts").toString();
                if (response.asString().contains("\"ok\":true,\"file\""))
                {
                    isMessageSent = true;
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (isMessageSent)
        {
            System.out.println("Slack message sent to channel : " + slackGroupName);
        } else
        {
            System.out.println("UNABLE TO SEND SLACK MESSAGE TO : " + slackGroupName + "\n" + response.asString());
        }

        return thread_ts;
    }

    public static void replyOnThread(String slackGroupName, String message, String thread_ts)
    {
        boolean isMessageSent = false;
        Response response = null;
        HashMap<String, String> apiParameters = new HashMap<String, String>();
        apiParameters.put("token", botUserOAuthToken);

        try
        {
            apiParameters.put("channel", slackGroupName);
            apiParameters.put("text", message);
            apiParameters.put("as_user", "true");
            apiParameters.put("thread_ts", thread_ts);
            String apiUrl = "https://slack.com/api/chat.postMessage";
            RequestSpecification reqspec = given().contentType(ContentType.URLENC);
            reqspec = reqspec.formParams(apiParameters);
            response = reqspec.when().post(apiUrl);
            if (response.asString().contains("\"ok\":true,\"channel\":\""))
            {
                isMessageSent = true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        if (isMessageSent)
        {
            System.out.println("Slack thread created for : " + message);
        } else
        {
            System.out.println("Failed to reply on given Thread : " + response.asString());
        }

    }

    public static boolean sendSlackMessageToGroupId(String slackGroupId, String message)
    {
        try
        {
            HashMap<String, String> apiParameters = new HashMap<String, String>();
            apiParameters.put("text", message);
            String apiUrl = "https://hooks.slack.com/services/T02T4D001/" + slackGroupId;
            RequestSpecification reqspec = given().contentType(ContentType.JSON);
            reqspec.body(apiParameters);
            Response response = reqspec.when().post(apiUrl);
            return (response.asString().equalsIgnoreCase("ok"));
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * This function is to read latest message from a slack channel
     *
     * @param slackChannelId -
     * @param count          - number of last messages to be fetched
     * @return latest messages from the channel as per passed count
     */
    public static JSONArray fetchSlackMessagesFromChannel(String slackChannelId, String count)
    {
        try
        {
            HashMap<String, String> apiParameters = new HashMap<String, String>();
            apiParameters.put("token", userOAuthToken);
            apiParameters.put("channel", slackChannelId);
            apiParameters.put("inclusive", "true");
            apiParameters.put("count", count);
            String apiUrl = "https://slack.com/api/conversations.history";
            RequestSpecification reqspec = given().contentType(ContentType.JSON);
            reqspec.params(apiParameters);
            Response response = reqspec.when().get(apiUrl);
            JSONArray messages = new JSONObject(response.asString()).getJSONArray("messages");
            return messages;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * This function is to read latest message from a slack channel
     *
     * @param slackChannelId -
     * @return latest message from the channel
     */
    public static String fetchLatestSlackMessageFromChannel(String slackChannelId)
    {
        JSONArray messages = fetchSlackMessagesFromChannel(slackChannelId, "1");
        JSONObject message = (JSONObject) messages.get(0);
        return message.getString("text");
    }

    /**
     * This function is to get login otp from slackbot Robin
     *
     * @param testConfig  - object of config
     * @param phoneNumber - phone number for which you need otp
     * @return latest otp of the phone number
     */
    public static String getLoginOtpViaRobinSlackbot(Config testConfig, String phoneNumber)
    {
        String otp = null;
        int counter = 0;
        String message;
        synchronized (SlackHelper.class)
        {
            testConfig.logComment("Fetching latest otp for : " + phoneNumber);
            SlackHelper.sendSlackMessage("#thanos-is-here", "!!otp " + phoneNumber, null);
            WaitHelper.waitForSeconds(testConfig, 2);

            JSONArray messages = fetchSlackMessagesFromChannel("CMBEGRSDP", "1");
            do
            {
                message = ((JSONObject) messages.get(counter)).getString("text");
                counter++;
            } while (!message.equals("!!otp " + phoneNumber));

            message = ((JSONObject) messages.get(counter - 2)).getString("text");
            otp = message.split("otp: ")[1].substring(0, 4);
            testConfig.logCommentForDebugging(otp);
            //TODO Re-visit the code as required once there is some update from icp team on otp logic
            //			String[] messages = SlackHelper.fetchLatestSlackMessageFromChannel("CMBEGRSDP").split("otp: ");
            //			String[] getTheDate = SlackHelper.fetchLatestSlackMessageFromChannel("CMBEGRSDP").split("otp_expires_at: ");
            //
            //			int index = 1;
            //			String strDateFormat = "E MMM dd yyyy HH:mm:ss";
            //			SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            //			if (messages.length > 2)
            //			{
            //				String expired = getTheDate[1].substring(0, 24);
            //				Date latest;
            //				try
            //				{
            //					latest = sdf.parse(expired);
            //					Date date;
            //					for (int i = 1; i < messages.length; i++)
            //					{
            //						expired = getTheDate[i].substring(0, 24);
            //						date = sdf.parse(expired);
            //						if (date.after(latest))
            //						{
            //							latest = date;
            //							index = i;
            //						}
            //					}
            //					otp = messages[index].substring(0, 4);
            //				}
            //				catch (ParseException e)
            //				{
            //					testConfig.logExceptionAndFail(e);
            //				}
            //			}
            //			else
            //				otp = messages[messages.length - 1].substring(0, 4);

        }
        return otp;
    }

    /**
     * This function is to get latest login otp from slackbot Chewbacca
     *
     * @param testConfig  - object of config
     * @param phoneNumber - phone number for which you need otp
     * @return latest otp of the phone number
     */
    public static String getOtpViaChewbaccaSlackbot(Config testConfig, String phoneNumber)
    {
        String[] otp;
        synchronized (SlackHelper.class)
        {
            testConfig.logComment("Fetching latest otp for : " + phoneNumber);
            SlackHelper.sendSlackMessage("#thanos-is-here", "<@chewbacca> otp " + phoneNumber, null);
            WaitHelper.waitForSeconds(testConfig, 5);
            otp = SlackHelper.fetchLatestSlackMessageFromChannel("CMBEGRSDP").split("Otp : ");
        }
        return otp[1].substring(0, 4);
    }

    public static String getThreadIdForMatchingMessage(String slackChannelId, String partialMatchingText)
    {
        JSONArray messages = SlackHelper.fetchSlackMessagesFromChannel(slackChannelId, "30");
        for (int i = 0; i < messages.length(); i++)
        {
            JSONObject message = (JSONObject) messages.get(i);
            if (message.toString().contains(partialMatchingText))
            {
                System.out.println(message);
                return message.getString("ts");
            }
        }
        return null;
    }

    public static void multipleReplyOnThread(String slackGroupName, String slackThreadId, ArrayList<String> messageList)
    {

        for (int i = 0; i < SlackHelper.slackThreads.size(); i++)
        {
            SlackHelper.replyOnThread(slackGroupName, messageList.get(i), slackThreadId);
        }
    }
}