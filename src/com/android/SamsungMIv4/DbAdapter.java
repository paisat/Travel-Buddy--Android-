package com.android.SamsungMIv4;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbAdapter {
	
	String DATABASE_NAME="travelBuddy.db";
	SQLiteDatabase db;
	String create="create table travel ( _id integer primary key autoincrement,name text not null,notes text,location text,photo TEXT,date text,orientation text);";
	Context context;
	dbHelper helper;
	public DbAdapter(Context _context)
	{
		this.context=_context;
		helper=new dbHelper(_context,DATABASE_NAME,null,3);
		
	}
	
	private class dbHelper extends SQLiteOpenHelper
	{
		public dbHelper(Context context,String dbName,CursorFactory factory,int version)
		{
			super(context,dbName,factory,version);
			
		}
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(create);
			
		}
		
		
		@Override
		public void onUpgrade(SQLiteDatabase db,int newVersion,int oldVersion)
		{
			if(newVersion==2)
			{
			db.execSQL("DROP TABLE IF EXISTS travel");
			onCreate(db);
			}
		}
		
	}
	
	public void close()
	{
		db.close();
	}
	public void open() throws SQLiteException {
		try {
		db = helper.getWritableDatabase();
		} catch (SQLiteException ex) {
		db = helper.getReadableDatabase();
		}
		}
	
	public Cursor fetchAll()
	{
		return db.rawQuery("select * from travel order by _id desc",null);
	}
	
	public boolean  insertRow(String name,String notes,String location,String photo,String date,String orientation)
	{
		ContentValues content = new ContentValues();
		content.put("name", name);
		content.put("notes",notes);
		content.put("location",location);
		content.put("photo", photo);
		content.put("date", date);
		content.put("orientation", orientation);
		
		db.insert("travel",null, content);
		
		return true;
	}
	
	public Cursor fetchRowById(String id)
	{
		String [] arg={id};
		return db.rawQuery("SELECT * FROM travel where _id=?", arg);
		
	}
	
	public boolean delete(Long id)
	{
		String delete="delete from travel where _id="+id+";";
		db.execSQL(delete);
		return true;
		
		
	}
	
	public Cursor fetchRowByOrientation(String orientation)
	{
		
		String [] arg={orientation};
		//return db.query("travel", new String []{"orientation"}, "orientation=?", new String[]{orientation}, null, null, null);
		return db.rawQuery("select * from travel where photo=?", arg);
		
	}
	
	public boolean update(Long id,String columnName,String value)
	{
		
		String ids=String.valueOf(id);
		ContentValues update=new ContentValues();
		update.put(columnName, value);
		db.update("travel", update, "_id"+"=?", new String[]{ids});
		return true;
		
				
	}
	

}
