package com.tevinjeffrey.rutgersct.dagger

import android.content.Context
import android.os.Build
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tevinjeffrey.rmp.common.RMPModule
import com.tevinjeffrey.rutgersct.BuildConfig
import com.tevinjeffrey.rutgersct.RutgersCTApp
import com.tevinjeffrey.rutgersct.utils.PreferenceUtils
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module(includes = [
  DataModule::class,
  RMPModule::class]
)
class RutgersAppModule {
  @Provides
  fun providesGson(): Gson {
    return GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .create()
  }

  @Provides
  fun providesOkHttpClient(userAgentInterceptor: UserAgentInterceptor): OkHttpClient {
    var client: OkHttpClient.Builder = OkHttpClient.Builder()
        .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)

    if (BuildConfig.DEBUG) {
      client = unsafeOkHttpClient
    }

    client.addNetworkInterceptor(userAgentInterceptor)
    return client.build()
  }

  @Provides
  fun providesOkHttpClientBuilder(userAgentInterceptor: UserAgentInterceptor): OkHttpClient.Builder {
    var client = OkHttpClient.Builder()

    if (BuildConfig.DEBUG) {
      client = unsafeOkHttpClient
      client.addNetworkInterceptor(StethoInterceptor())
    }

    client.addNetworkInterceptor(userAgentInterceptor)
    return client
  }

  @Provides
  fun providesPreferenceUtils(context: Context): PreferenceUtils {
    return PreferenceUtils(context)
  }

  @Provides
  fun providesUserAgentInterceptor(context: Context): UserAgentInterceptor {
    val str = context.getString(context.applicationInfo.labelRes)
    val sb = StringBuilder()
    sb.append(str)
        .append("/")
        .append(BuildConfig.APPLICATION_ID)
        .append(" ")
        .append("(")
        .append(BuildConfig.VERSION_NAME)
        .append("; Android ")
        .append(Build.VERSION.SDK_INT)
        .append(")")
    return UserAgentInterceptor(sb.toString())
  }

  @Provides internal fun provideContext(application: RutgersCTApp): Context {
    return application.applicationContext
  }

  /* This interceptor adds a custom User-Agent. */
  inner class UserAgentInterceptor(private val userAgent: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
      val originalRequest = chain.request()
      val requestWithUserAgent = originalRequest.newBuilder()
          .header("User-Agent", userAgent)
          .build()
      return chain.proceed(requestWithUserAgent)
    }
  }

  companion object {

    private val CONNECT_TIMEOUT_MILLIS: Long = 15000
    private val READ_TIMEOUT_MILLIS: Long = 20000

    private// Create a trust manager that does not validate certificate chains
        // Install the all-trusting trust manager
        // Create an ssl socket factory with our all-trusting manager
    val unsafeOkHttpClient: OkHttpClient.Builder
      get() {
        try {
          val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                chain: Array<java.security.cert.X509Certificate>,
                authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<java.security.cert.X509Certificate>,
                authType: String) {
            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
              return arrayOf()
            }
          })
          val sslContext = SSLContext.getInstance("SSL")
          sslContext.init(null, trustAllCerts, java.security.SecureRandom())
          val sslSocketFactory = sslContext.socketFactory

          val builder = OkHttpClient.Builder()
          builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
          builder.hostnameVerifier { _, _ -> true }

          val interceptor = HttpLoggingInterceptor()
          interceptor.level = HttpLoggingInterceptor.Level.HEADERS
          builder.addInterceptor(interceptor)

          return builder
        } catch (e: Exception) {
          throw RuntimeException(e)
        }

      }
  }
}