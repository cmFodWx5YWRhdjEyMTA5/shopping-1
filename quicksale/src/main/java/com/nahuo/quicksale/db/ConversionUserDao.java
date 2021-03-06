//package com.nahuo.quicksale.db;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.text.TextUtils;
//
//import com.easemob.util.HanziToPinyin;
//import com.nahuo.quicksale.common.Constant;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ConversionUserDao {
//	private ChatDBHelper dbHelper;
//
//	public ConversionUserDao(Context context) {
//		dbHelper = ChatDBHelper.getInstance(context);
//	}
//
//	/**
//	 * 保存好友list
//	 *
//	 * @param contactList
//	 */
//	public void saveContactList(List<ChatUserModel> contactList) {
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
//		if (db.isOpen()) {
//			// db.delete(DBColumns.ConversionUser.TABLE_NAME, null, null);
//			for (ChatUserModel user : contactList) {
//
//				if (user.getUsername().isEmpty()) {
//					break;
//				}
//				ContentValues values = new ContentValues();
//				values.put(DBColumns.ConversionUser.COLUMN_NAME_ID,
//						user.getUsername());
//				if (user.getNick() != null)
//					values.put(DBColumns.ConversionUser.COLUMN_NAME_NICK,
//							user.getNick());
//				db.replace(DBColumns.ConversionUser.TABLE_NAME, null, values);
//			}
//		}
//	}
//
//	/**
//	 * 获取好友list
//	 *
//	 * @return
//	 */
//	public Map<String, ChatUserModel> getContactList() {
//		SQLiteDatabase db = dbHelper.getReadableDatabase();
//		Map<String, ChatUserModel> users = new HashMap<String, ChatUserModel>();
//		if (db.isOpen()) {
//			Cursor cursor = db.rawQuery("select * from "
//					+ DBColumns.ConversionUser.TABLE_NAME /* + " desc" */, null);
//			while (cursor.moveToNext()) {
//				String username = cursor
//						.getString(cursor
//								.getColumnIndex(DBColumns.ConversionUser.COLUMN_NAME_ID));
//				if (TextUtils.isEmpty(username)) {
//					continue;
//				}
//				String nick = cursor
//						.getString(cursor
//								.getColumnIndex(DBColumns.ConversionUser.COLUMN_NAME_NICK));
//				ChatUserModel user = new ChatUserModel();
//				user.setUsername(username);
//				user.setNick(nick);
//				String headerName = null;
//				if (!TextUtils.isEmpty(user.getNick())) {
//					headerName = user.getNick();
//				} else {
//
//					headerName = user.getUsername();
//				}
//
//				if (username.equals(Constant.NEW_FRIENDS_USERNAME)
//						|| username.equals(Constant.GROUP_USERNAME)) {
//					user.setHeader("");
//				} else if (Character.isDigit(headerName.charAt(0))) {
//					user.setHeader("#");
//				} else {
//					user.setHeader(HanziToPinyin.getInstance()
//							.get(headerName.substring(0, 1)).get(0).target
//							.substring(0, 1).toUpperCase());
//					char header = user.getHeader().toLowerCase().charAt(0);
//					if (header < 'a' || header > 'z') {
//						user.setHeader("#");
//					}
//				}
//				users.put(username, user);
//			}
//			cursor.close();
//		}
//		return users;
//	}
//
//	/**
//	 * 删除一个联系人
//	 *
//	 * @param username
//	 */
//	public void deleteContact(String username) {
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
//		if (db.isOpen()) {
//			db.delete(DBColumns.ConversionUser.TABLE_NAME,
//					DBColumns.ConversionUser.COLUMN_NAME_ID + " = ?",
//					new String[] { username });
//		}
//	}
//
//	/**
//	 * 保存一个联系人
//	 *
//	 * @param user
//	 */
//	public void saveContact(ChatUserModel user) {
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put(DBColumns.ConversionUser.COLUMN_NAME_ID, user.getUsername());
//		if (user.getNick() != null)
//			values.put(DBColumns.ConversionUser.COLUMN_NAME_NICK,
//					user.getNick());
//		if (db.isOpen()) {
//			db.replace(DBColumns.ConversionUser.TABLE_NAME, null, values);
//		}
//	}
//
//}