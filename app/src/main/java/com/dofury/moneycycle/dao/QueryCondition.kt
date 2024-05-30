package com.dofury.moneycycle.dao

import android.os.Parcel
import android.os.Parcelable

data class QueryCondition(
    val startDate: String?,
    val endDate: String?,
    val categories: List<String>?,
    val isBudget: Boolean?,
    val memo: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeStringList(categories)
        parcel.writeValue(isBudget)
        parcel.writeString(memo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QueryCondition> {
        override fun createFromParcel(parcel: Parcel): QueryCondition {
            return QueryCondition(parcel)
        }

        override fun newArray(size: Int): Array<QueryCondition?> {
            return arrayOfNulls(size)
        }
    }
}