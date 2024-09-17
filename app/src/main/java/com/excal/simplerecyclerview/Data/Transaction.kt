package com.excal.simplerecyclerview.Data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName="transaction_table")
data class Transaction(

    @PrimaryKey(autoGenerate=true) val targetId:Int=0,
    @ColumnInfo(name="targetName") var targetName:String,
//    @ColumnInfo(name="amount") var amount:Int,
    @ColumnInfo(name="targetPerlombaan") var targetPerlombaan:String,
//    @ColumnInfo(name="idGambar") var idGambar:Int

): Parcelable
