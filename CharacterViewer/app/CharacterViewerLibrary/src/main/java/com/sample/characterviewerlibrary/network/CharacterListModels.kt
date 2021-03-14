package com.sample.characterviewerlibrary.network

object CharacterListModels {

    var mTopicsDownloadedList : List<RelatedTopics>? = null
    var mPhoneSelectedTopic : RelatedTopics? = null

    data class Result (
         val RelatedTopics : List<RelatedTopics>?
    )

    data class RelatedTopics (
        val FirstURL : String?,
        val Icon : Image?,
        val Result : String?,
        val Text : String?
    )

    data class Image (
        val URL : String?
    )
}