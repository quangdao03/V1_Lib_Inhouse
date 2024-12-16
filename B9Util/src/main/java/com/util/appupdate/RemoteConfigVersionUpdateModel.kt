package com.util.appupdate

//default no_update
data class RemoteConfigVersionUpdateModel (
    val lastest_version: String,
    val lastest_update_content: String,
    val must_update: List<String>,
    val should_update: List<String>,
    val no_update: List<String>
)
//{
//  "lastest_version": "1.1.1",
//  "lastest_update_content": "1.0.0",
//  "must_update": [
//    "1.0.0"
//  ],
//  "should_update": [
//    "1.0.0"
//  ],
//  "no_update": [
//    "1.0.0"
//  ]
//}