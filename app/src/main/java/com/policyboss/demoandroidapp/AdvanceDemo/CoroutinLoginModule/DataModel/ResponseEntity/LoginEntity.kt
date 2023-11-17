package com.policyboss.demoandroidapp.LoginModule.DataModel.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "LoginData")
data class LoginEntity(

    @PrimaryKey(autoGenerate = false)
    val FBAId: Int,
    val CustID: String,
    val Designation: String,
    val EditDesig: String,
    val EditEmailId: String,
    val EditMobiNumb: String,
    val EditProfPictName: String,
    val EmailID: String,

    val FBACode: String?,


    val FBAProfileUrl: String,
    val FBAStatus: String,
    val FSM: String,
   val FSMDesig: String? ,
    val FSMEmail: String? ,
    val FSMFullname: String?,
    val FSMMobile: String?,
    val FullName: String,
    val IsDemo: Int,
    val IsFirstLogin: Int,
    val IsFoc: String,
    val IsUidLogin: String,
    val LastloginDate: String,
    val LiveURL: String,
    val LoanId: String,
    val MobiNumb1: String,
    val POSEmail: String,
    val POSPInfo: String,
    val POSPMobile: String,
    val POSPName: String,
    val POSPNo: String? ,
    val POSPProfileUrl: String,
    val POSPStatus: String,
    val PayStatus: String,
    val PaymentUrl: String? ,
    val ProfPictName: String,
    val RegMACAddr: String,
    val RewardPoint: String?,
    val SuccessStatus: String,
    val SuppAgenId: String,
    val SuppEmailId: String,
    val SuppMobiNumb: String,
    val UserName: String,
    val UserType: String,
    val Validfrom: String,
    val referer_code: String,
    val referraid: Int,
    val rm_id: Int
)