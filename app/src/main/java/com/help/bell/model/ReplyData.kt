package com.help.bell.model

import java.io.Serializable

data class ReplyData(
    val name: String = "",
    val currentTime: Long = 0L,
    val reply: String = "",
    val uid: String = ""
) : Serializable
