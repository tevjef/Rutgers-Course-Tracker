package com.tevinjeffrey.rutgersct.data.notifications

import android.content.Context
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.perf.metrics.AddTrace
import timber.log.Timber

import java.io.IOException

import javax.inject.Inject

class SubscriptionManager @Inject constructor(
    val context: Context,
    val messaging: FirebaseMessaging) {

  fun fcmToken(): String {
    return FirebaseInstanceId.getInstance().token.orEmpty()
  }

  @Throws(IOException::class)
  @AddTrace(name = "SubscriptionManager.subscribe", enabled = true)
  fun subscribe(topicName: String) {
    val pubSub = FirebaseMessaging.getInstance()
    Timber.d("Subscribing: %s", topicName)
    pubSub.subscribeToTopic(topicName)
  }

  @Throws(IOException::class)
  @AddTrace(name = "SubscriptionManager.unsubscribe", enabled = true)
  fun unsubscribe(topicName: String) {
    val pubSub = FirebaseMessaging.getInstance()
    Timber.d("Unsubscribing: %s", topicName)
    pubSub.unsubscribeFromTopic(topicName)
  }

  companion object {
    val REGISTRATION_COMPLETE = "registrationComplete"
  }
}
