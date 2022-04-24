package com.example.ass3

class Contact(s: String, s1: String) {
    //initialize variables
    var name: String = s
    var number: String = s1

    //Generate getter and setter
    @JvmName("getName1")
    fun getName():String {
        return name
    }
    @JvmName("getNumber1")
    fun getNumber():String{
        return number
    }
    @JvmName("setName1")
    fun setName(nam:String){
        this.name = nam
    }
    @JvmName("setNumber1")
    fun setNumber(num: String)
    {
        this.number = num
    }
}
