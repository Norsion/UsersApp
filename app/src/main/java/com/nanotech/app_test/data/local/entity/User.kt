package com.nanotech.app_test.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Users")
data class User(
    @PrimaryKey
    val id: Int,
    val username: String,
)