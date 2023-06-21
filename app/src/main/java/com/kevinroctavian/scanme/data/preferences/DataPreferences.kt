package com.kevinroctavian.scanme.data.preferences

import android.util.Log
import com.kevinroctavian.scanme.ui.home.StorageSelector
import com.orhanobut.hawk.Hawk

class DataPreferences {

    companion object{

        fun saveStorageType(storageType: StorageSelector) = Hawk.put(StorageSelector::class.java.name, storageType)
        fun getStorageType(): StorageSelector {
            Log.d("d", "Hawk.getStorageType: "+Hawk.get(StorageSelector::class.java.name))
            return Hawk.get(StorageSelector::class.java.name) ?: StorageSelector.DATABASE
        }
    }

}