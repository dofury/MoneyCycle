package com.dofury.moneycycle.dto

data class User(var idToken: String, var email: String, var nickname: String, var profile: String){
    constructor() : this ("","","","")
}
