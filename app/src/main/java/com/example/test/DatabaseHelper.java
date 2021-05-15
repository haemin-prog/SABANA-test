package com.example.test;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    protected static String TAG = "DatabaseHelper";

    private static String databasePath = "";
    private static String databaseName = "market.db";
    private static String tableName = "market";

    private final Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context){

        super(context, databaseName, null, 1); // version of database

        if (Build.VERSION.SDK_INT >= 17){
            databasePath = context.getApplicationInfo().dataDir + "/databases/";
        }
        else {
            databasePath = "/data/data/" + context.getPackageName() + "/databases/";
        }

        this.mContext = context;
    }

    public DatabaseHelper CreateDatabase() throws SQLException{

        if (!CheckDatabase()){
            this.getReadableDatabase();
            this.close();

            try{
                CopyDatabase();
                Log.e(TAG,  "[SUCCESS] " + databaseName + " are Created");
            }
            catch(IOException ioException){
                Log.e(TAG, "[ERROR] " + "Unable to create " + databaseName);
                throw new Error(TAG);
            }
        }

        return this;
    }

    public boolean CheckDatabase(){
        File file = new File(databasePath + databaseName);
        return file.exists();
    }

    public void CopyDatabase() throws IOException{

        InputStream inputStream  = mContext.getAssets().open(databaseName);
        String outputFileName = databasePath + databaseName;
        OutputStream outputStream = new FileOutputStream(outputFileName);

        byte[] buffer = new byte[1024];
        int length;
        while((length = inputStream.read(buffer)) > 0){
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public boolean OpenDatabase(){
        String mPath = databasePath + databaseName;
        mDatabase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);

        return mDatabase != null;
    }

    // Read Table Data
    public List getTableData() {

        try{
            List mList = new ArrayList();

            // Query
            String sql = "SELECT * FROM " + tableName;

            // use Cursor to read table data
            Cursor mCursor = mDatabase.rawQuery(sql, null);

            // read to the end
            if (mCursor != null){

                // read to end of the column
                while(mCursor.moveToNext()){

                    Market market = new Market();

                    // Save database - Record data
                    // int _id / String storeName / String franchise
                    // double latitude / double longitude / String address
                    market.setId(mCursor.getInt(0));
                    market.setStoreName(mCursor.getString(1));
                    market.setFranchise(mCursor.getString(2));
                    market.setLatitude(mCursor.getDouble(3));
                    market.setLongitude(mCursor.getDouble(4));
                    market.setAddress(mCursor.getString(5));

                    // push data to list
                    mList.add(market);
                }

            }
            return mList;

        }
        catch (SQLException mSQLException){
            // Error Message
            Log.e(TAG, mSQLException.toString());
            throw mSQLException;
        }

    }


    // Close Database
    @Override
    public synchronized void close(){
        if (mDatabase != null){
            mDatabase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
