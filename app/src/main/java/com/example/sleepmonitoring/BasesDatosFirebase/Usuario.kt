package com.example.sleepmonitoring.BasesDatosFirebase

    data class Usuario(
        val nombre:String="",
        val fechaNcimiento:String="",
        val horaAcostar:String="",
        val horaLevantar:String="",
        val tema: String="1",
        val diasLibres: MutableList<Int> =mutableListOf(),
        val avatar: String= "",
        var imagen: String = "H"
    )
