package com.help.bell.model

import java.io.Serializable

data class User(
    val name: String = "",
    val email: String = "",
    val sex: String = "",
    val op1: String = "",
    val op2: String = "",
    val op3: String = "",
    val op4: String = "",
    val op5: String = "",
    ) : Serializable
