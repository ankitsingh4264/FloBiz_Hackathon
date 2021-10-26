package com.example.flobizhackathon.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Response(
	@PrimaryKey
	var id: Long = 1,
	var quotaMax: Int? = null,
	var quotaRemaining: Int? = null,
	var hasMore: Boolean? = null,
	var items: RealmList<Items?>? = null
):RealmObject()

open class Items(
	var owner: Owner? = null,
	@SerializedName("creation_date")
	var creationDate: Long? = null,
	@SerializedName("answer_count")

	var answerCount: Long? = null,
	var title: String? = null,
	@SerializedName("question_id")

	var questionId:String? = null,
	var tags: RealmList<String?>? = null,
    @SerializedName( "view_count")
	var viewCount: Long? = null,
	var link:String?=null

):RealmObject()

open class Owner(
	@SerializedName("profile_image")


	var profileImage: String? = null,
	@SerializedName("display_name")


	var displayName: String? = null,
	):RealmObject()

