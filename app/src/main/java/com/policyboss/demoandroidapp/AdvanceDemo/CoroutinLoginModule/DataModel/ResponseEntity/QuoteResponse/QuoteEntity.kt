package com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.DataModel.ResponseEntity.QuoteResponse

//@Entity(tableName = "Quote")
data class QuoteEntity(

    //@PrimaryKey(autoGenerate = true)
    val _id: String,
    val author: String,
    val authorSlug: String,
    val content: String,
    val dateAdded: String,
    val dateModified: String,
    val length: Int,

)