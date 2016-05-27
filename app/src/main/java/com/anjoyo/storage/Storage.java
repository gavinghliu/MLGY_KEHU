package com.anjoyo.storage;

import java.io.File;

import android.os.Environment;

public interface Storage {

	public static String STORAGE_HOME = Environment.getExternalStoragePublicDirectory("MLGY").getAbsolutePath();
	
    public File getFile();

}
