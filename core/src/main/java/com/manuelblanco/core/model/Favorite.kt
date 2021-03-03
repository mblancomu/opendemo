package com.manuelblanco.core.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class Favorite(
    @PrimaryKey @ColumnInfo(name = "id")
    val id : Int,
    val name : String?,
    val description : String?,
    val resourceURI : String?
)