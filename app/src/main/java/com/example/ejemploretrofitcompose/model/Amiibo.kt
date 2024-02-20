package com.example.ejemploretrofitcompose.model



// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
class Amiibo {
    var amiiboSeries: String? = null
    var character: String? = null
    var gameSeries: String? = null
    var head: String? = null
    var image: String? = null
    var name: String? = null
    var release: Release? = null
    var tail: String? = null
    var type: String? = null
}

class Release {
    var au: String? = null
    var eu: String? = null
    var jp: String? = null
    var na: String? = null
}

class Raiz {
    var amiibo: ArrayList<Amiibo>? = null
}