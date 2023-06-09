package Automation.Utils;

import Automation.Utils.Enums.DatabaseName;
import Automation.Utils.Enums.QueryType;
import com.mongodb.*;
import org.bson.types.ObjectId;

import java.sql.*;
import java.util.Date;
import java.util.*;

public class Database
{

    private static Object thanosConnection = null;

    /**
     * Creates database connection using the Config parameters - 'DatabaseString', 'DatabaseUsername' and 'DatabasePassword'
     *
     * @param Config       test config instance
     * @param DatabaseName - name of database to be connected
     * @return Database Connection
     */
    public static Object getConnection(Config testConfig, DatabaseName databaseName)
    {
        Object connection = null;
        try
        {
            synchronized (Database.class)
            {
                if (Objects.requireNonNull(databaseName) == DatabaseName.Thanos)
                {
                    connection = thanosConnection;
                    if (connection == null)
                    {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        connection = createConnection(testConfig, databaseName);
                        thanosConnection = connection;
                    }
                }
                if (Config.isDebugMode)
                {
                    testConfig.logComment("DB Connection succeeded");
                }
            }
        } catch (ClassNotFoundException e)
        {
            testConfig.logExceptionAndFail(e);
        }
        return connection;
    }

    private static Connection createConnection(Config testConfig, DatabaseName databaseName)
    {
        try
        {
            String userName = null;
            String password = null;
            String host = testConfig.getRunTimeProperty(databaseName.toString() + "DatabaseString");
            if (databaseName.toString().equalsIgnoreCase("plutus") && !Config.isRemoteExecution)
            {
                host = testConfig.getRunTimeProperty(databaseName + "DatabaseLocalHost");
            }

            if (databaseName.toString().equalsIgnoreCase("thanos") && Config.isRemoteExecution)
            {
                host = "jdbc:mysql://10.100.11.30:3306/thanos";
                userName = CommonUtilities.decryptMessage(System.getProperty("ThanosDatabaseUsername").getBytes());
                password = CommonUtilities.decryptMessage(System.getProperty("ThanosDatabasePassword").getBytes());
            } else
            {
                userName = testConfig.getRunTimeProperty(databaseName + "DatabaseUsername");
                password = testConfig.getRunTimeProperty(databaseName + "DatabasePassword");
            }

            testConfig.logComment("Connecting to " + databaseName + ":-" + host);
            return DriverManager.getConnection(host, userName, password);
        } catch (SQLException e)
        {
            testConfig.logExceptionAndFail(e);
            return null;
        }
    }

    /**
     * Execute query: to run mysql + oracle query, select/update/insert/delete supported
     *
     * @param testConfig   the test config
     * @param sqlRow       : row number of query in sheet
     * @param databaseName : name of database
     * @param queryType    : use select to get records. use modify to update, insert and delete record(s).
     * @return : returns Object, for select query cast to ResultSet. for modify query cast to int.
     */
    public static Object executeQuery(Config testConfig, int sqlRow, QueryType queryType, DatabaseName databaseName)
    {
        // Read the Query column of SQL sheet of Test data excel
        TestDataReader sqlData = testConfig.getExcelSheet("SQL");
        String query = sqlData.getData(testConfig, sqlRow, "Query");
        return executeQuery(testConfig, query, queryType, databaseName);
    }

    public static Object executeQuery(Config testConfig, String sqlQuery, QueryType queryType, DatabaseName databaseName)
    {
        sqlQuery = testConfig.replaceArgumentsWithRunTimeProperties(sqlQuery);
        testConfig.logCommentForDebugging("Executing query - '" + sqlQuery + "'");
        Connection connection = (Connection) getConnection(testConfig, databaseName);
        Object returnValue = null;
        try
        {
            switch (queryType)
            {
                case select:
                    ResultSet resultSet = connection.createStatement().executeQuery(sqlQuery);
                    if (null == resultSet)
                    {
                        testConfig.logWarning("No data was returned for above query");
                    }
                    returnValue = resultSet;
                    break;
                case update:
                    int recordsModified = connection.createStatement().executeUpdate(sqlQuery);
                    if (recordsModified == 0)
                    {
                        testConfig.logWarning("No record updated for this query");
                    } else
                    {
                        testConfig.logCommentForDebugging("Total record updated - " + recordsModified);
                    }
                    returnValue = recordsModified;
                    break;
                case delete:
                    returnValue = connection.createStatement().executeUpdate(sqlQuery);
                    testConfig.logComment("Total records deleted - " + returnValue);
                    break;
            }
        } catch (SQLException e)
        {
            testConfig.logExceptionAndFail(e);
        }
        return returnValue;
    }

    /**
     * This function is used to fetch the data from database and get result of first row into the MAP
     *
     * @param testConfig   - Pass object of config
     * @param sqlRow       - row number of SQL excel sheet
     * @param databaseName - database type
     * @return - map
     */
    public static Map<String, String> executeSelectQuery(Config testConfig, int sqlRow, DatabaseName databaseName)
    {
        // Read the Query column of SQL sheet of Test data excel
        TestDataReader sqlData = testConfig.getExcelSheet("SQL");
        String query = sqlData.getData(testConfig, sqlRow, "Query");
        return executeSelectQuery(testConfig, query, databaseName);
    }

    public static Map<String, String> executeSelectQuery(Config testConfig, String query, DatabaseName databaseName)
    {
        int rowNumber = 1;
        Map<String, String> resultMap = null;
        ResultSet resultSet = (ResultSet) executeQuery(testConfig, query, QueryType.select, databaseName);

        int row = 1;
        try
        {
            while (resultSet.next())
            {
                if (row == rowNumber)
                {
                    resultMap = createHashMapFromResultSet(testConfig, resultSet);
                    testConfig.logComment("Query Result :- " + resultMap);
                    break;
                } else
                {
                    row++;
                }
            }
        } catch (SQLException e)
        {
            testConfig.logExceptionAndFail(e);
        }
        return resultMap;
    }

    public static Map<String, String> getDataFromMongo(Config testConfig, int collectionRowNo, DatabaseName databaseName)
    {
        TestDataReader collections = testConfig.getExcelSheet("Collections");
        String collectionName = collections.getData(testConfig, collectionRowNo, "Collection");
        String queryParamKey = collections.getData(testConfig, collectionRowNo, "QueryParamKey");
        String queryParamValue = collections.getData(testConfig, collectionRowNo, "QueryParamValue");
        String expectedDataKey = collections.getData(testConfig, collectionRowNo, "ExpectedDataKey");
        String sortByKey = collections.getData(testConfig, collectionRowNo, "sortByKey");
        String sortByValue = collections.getData(testConfig, collectionRowNo, "sortByValue");
        int descendingValue = Integer.valueOf(sortByValue);
        queryParamValue = testConfig.replaceArgumentsWithRunTimeProperties(queryParamValue);

        BasicDBObject searchQuery = new BasicDBObject();
        if (queryParamValue.contains("Object"))
        {
            queryParamValue = queryParamValue.substring(queryParamValue.lastIndexOf("_") + 1);
            searchQuery.put(queryParamKey, new ObjectId(queryParamValue));
        } else
        {
            searchQuery.put(queryParamKey, queryParamValue);
        }

        testConfig.logComment("Executing query - " + searchQuery);
        DB db = (DB) getConnection(testConfig, databaseName);
        DBCollection dbCollection = db.getCollection(collectionName);

        BasicDBObject sortByQuery = new BasicDBObject();
        sortByQuery.put(sortByKey, descendingValue);
        DBCursor dbCursor = dbCollection.find(searchQuery).sort(sortByQuery).limit(1);
        if (dbCursor != null)
        {
            testConfig.logWarning("Query is " + dbCursor);
        }
        Map<String, String> resultMap = null;

        try
        {
            while (dbCursor.hasNext())
            {
                DBObject tobj = dbCursor.next();
                resultMap = createHashMapFromDBObject(testConfig, tobj, expectedDataKey);
                break;
            }

        } catch (Exception e)
        {
            testConfig.logExceptionAndFail(e);
        } finally
        {
            dbCursor.close();
        }
        return resultMap;
    }

    public static int getResultCountFromMongo(Config testConfig, String searchParam, String searchValue, String collectionName, DatabaseName databaseName)
    {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append(searchParam, searchValue);
        testConfig.logComment("Executing query - " + searchQuery);

        DB db = (DB) getConnection(testConfig, databaseName);
        DBCollection dbCollection = db.getCollection(collectionName);

        DBCursor cursor = dbCollection.find(searchQuery);
        int recordsCount = cursor.count();
        cursor.close();
        return (recordsCount);
    }

    /**
     * Gets the object from mongo.
     *
     * @param testConfig     -> the test config
     * @param searchParam    --> Parameter to search, use comma seprated values for AND query i.e "orderId,txnId,merchantId".
     * @param searchValue    --> Values of search param in comma seprated type for AND query i.e "order12,txn12,merchant12"
     * @param collectionName --> collection name to fetch data from
     * @param sortResult     --> true if result sorting required else false
     * @param sortParam      -->if sortResult is true, name of field on which sorting required else keep null/empty
     * @param AscOrDesc      --> use -1 for Descending order, use 1 for ascending order.
     * @param limit          --> True for limiting results else false
     * @param limitingValue  : if limit=true, set limiting records else 0.
     * @param databaseName   the db type : Name of DB
     * @return the object from mongo
     */
    public static DBCursor getObjectFromMongo(Config testConfig, String searchParam, String searchValue, String collectionName, Boolean sortResult, String sortParam, int AscOrDesc, Boolean limit, int limitingValue, DatabaseName databaseName)
    {
        BasicDBObject searchQuery = new BasicDBObject();
        if (searchParam.contains(","))
        {
            String[] key = searchParam.split(",");
            String[] value = searchValue.split(",");
            List<BasicDBObject> andQuery = new ArrayList<BasicDBObject>();
            for (int i = 0; i < key.length; i++)
            {
                andQuery.add(new BasicDBObject(key[i], value[i]));
            }
            searchQuery.append("$and", andQuery);
        } else if (searchParam == "_id" || searchParam == "consolidate_action_id")
        {
            searchQuery.append(searchParam, new ObjectId(searchValue));
        } else
        {
            searchQuery.append(searchParam, searchValue);
        }

        testConfig.logComment("Executing query - " + searchQuery);
        DB db = (DB) getConnection(testConfig, databaseName);
        DBCollection dbCollection = db.getCollection(collectionName);
        DBCursor cursor = dbCollection.find(searchQuery);

        if (sortResult)
        {
            cursor = cursor.sort(new BasicDBObject(sortParam, AscOrDesc));
        }

        if (limit)
        {
            cursor = cursor.limit(limitingValue);
        }

        return cursor;
    }

    public static Map<String, String> updateDataInMongo(Config testConfig, int collectionRowNo, Date dateToBeUpdated, DatabaseName databaseName)
    {
        TestDataReader collections = testConfig.getExcelSheet("Collections");
        String collectionName = collections.getData(testConfig, collectionRowNo, "Collection");
        String queryParamKey = collections.getData(testConfig, collectionRowNo, "QueryParamKey");
        String updateQueryParamKey = collections.getData(testConfig, collectionRowNo, "updationKey");
        String updateQueryParamValue = collections.getData(testConfig, collectionRowNo, "updationValue");

        String queryParamValue = testConfig.getRunTimeProperty("_id");

        // Creating Where Clause Seach Query.
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append(queryParamKey, new ObjectId(queryParamValue));

        /*
         * Creating Update value Clause.
         * query will be : db.getCollection('payouts').update({"_id":ObjectId}, {$set:{"requested_at":ISODate("2019-07-23T12:21:59.814Z")}})
         */
        BasicDBObject updateValues = new BasicDBObject();
        updateValues.append("$set", new BasicDBObject().append(updateQueryParamKey, dateToBeUpdated));
        testConfig.logComment("Executing query - " + updateValues);

        DB db = (DB) getConnection(testConfig, databaseName);
        DBCollection dbCollection = db.getCollection(collectionName);

        // TODO: Work on results, to show how many rows are updated.
        WriteResult results = dbCollection.update(searchQuery, updateValues);
        /*
         * Reading updated Value
         * Query will be like : db.getCollection('payouts').find({"merchant_id":"G804572155"}).sort({"requested_at":-1}).limit(1);
         */
        searchQuery.put(queryParamKey, new ObjectId(queryParamValue));
        DBCursor dbCursor = dbCollection.find(searchQuery).limit(1);

        if (null == dbCursor)
        {
            testConfig.logWarning("No data was returned for this query");
        }
        Map<String, String> resultMap = null;
        try
        {
        } catch (Exception e)
        {
            testConfig.logExceptionAndFail(e);
        } finally
        {
            dbCursor.close();
        }
        return resultMap;
    }

    public static Map<String, String> createHashMapFromResultSet(Config testConfig, ResultSet resultSet)
    {
        HashMap<String, String> mapData = new HashMap<String, String>();

        try
        {
            ResultSetMetaData meta = resultSet.getMetaData();
            for (int col = 1; col <= meta.getColumnCount(); col++)
            {
                try
                {
                    String columnName = meta.getColumnLabel(col);
                    String columnValue = resultSet.getObject(col).toString();

                    // Code to handle TINYINT case
                    if (meta.getColumnTypeName(col).equalsIgnoreCase("TINYINT"))
                    {
                        columnValue = Integer.toString(resultSet.getInt(col));
                    }

                    mapData.put(columnName, columnValue);
                } catch (Exception e)
                {
                    mapData.put(meta.getColumnLabel(col), "");
                }
            }
        } catch (SQLException e)
        {
            testConfig.logExceptionAndFail(e);
        }
        return mapData;
    }

    private static Map<String, String> createHashMapFromDBObject(Config testConfig, DBObject DBObject, String expectedDataKey)
    {
        HashMap<String, String> mapData = new HashMap<String, String>();
        try
        {
            Set<String> dbObjectKeys = DBObject.keySet();
            if (!expectedDataKey.equals("NA"))
            {
                String[] expectedDataKeys = expectedDataKey.split(",");
                for (int i = 0; i < expectedDataKeys.length; i++)
                {
                    for (String key : dbObjectKeys)
                    {
                        try
                        {
                            mapData.put(key, DBObject.get(key).toString());
                        } catch (NullPointerException e)
                        {
                            mapData.put(key, "");
                        }
                    }
                }
            } else
            {
                for (String key : dbObjectKeys)
                {
                    mapData.put(key, DBObject.get(key).toString());
                }
            }
        } catch (Exception e)
        {
            testConfig.logExceptionAndFail(e);
        }
        return mapData;
    }
}