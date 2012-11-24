package com.lwcom.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.util.Log;

public class SettingStorage {

	public static void store(Object obj, String name, Context context) {
		String path = context.getCacheDir().getAbsolutePath() + "/" + name;
		try {
			createBack(path);
			FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.close();
			fos.close();
		} catch (Exception e) {
			restoreBack(path);
			Log.e("err", "cant store object");
		}
	}

	private static void createBack(String path) {
		File f = new File(path);
		if (f.exists()) {
			f.renameTo(new File(path + "_back"));
		}
	}

	private static void restoreBack(String path) {
		File f = new File(path + "_back");
		if (f.exists()) {
			f.renameTo(new File(path));
		}
	}

	public static Object restore(String name, Context context) {
		String path = context.getCacheDir().getAbsolutePath() + "/" + name;
		File f = new File(path);
		if (!f.exists()) {
			restoreBack(path);
		}
		return restore(path);
	}

	private static Object restore(String path) {
		File f = new File(path);
		Object obj = null;
		if (!f.exists()) {
			return null;
		}
		try {
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			obj = ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			Log.e("err", "cant restore object");
		}
		return obj;
	}

}
