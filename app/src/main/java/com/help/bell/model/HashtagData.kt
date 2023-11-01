package com.help.bell.model

import java.io.Serializable

data class HashtagData(
    val name: String = "",
    val time: String = "",
    val location: String = "",
    val hashtags: String = "",
    val currentTime: Long = 0L,
    val uid: String = "",
    var key: String = ""
) : Serializable
