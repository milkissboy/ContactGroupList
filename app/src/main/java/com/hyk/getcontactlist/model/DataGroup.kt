package com.hyk.getcontactlist.model

data class DataGroup (
    val id: String,
    val name: String = "",
    val contacts: MutableList<DataContact> = mutableListOf()
)