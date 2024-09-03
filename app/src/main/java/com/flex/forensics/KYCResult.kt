package com.flex.forensics

import android.os.Parcel
import android.os.Parcelable

data class KYCResult(var type: String) : Parcelable {
    var text: String? = null;
    var barcodes: List<String>? = null;
    var faces: List<String?>? = null;

    constructor(parcel: Parcel) : this("") {
        type = parcel.readString()!!
        text = parcel.readString()
        barcodes = parcel.createStringArrayList()
        faces = parcel.createStringArrayList()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(text)
        parcel.writeStringList(barcodes)
        parcel.writeStringList(faces)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<KYCResult> {
        override fun createFromParcel(parcel: Parcel): KYCResult {
            return KYCResult(parcel)
        }

        override fun newArray(size: Int): Array<KYCResult?> {
            return arrayOfNulls(size)
        }
    }

}