package com.gq.composefoundation.retrofit

import com.gq.basic.AppContext
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object RetrofitSSLCommon {

    const val RetrofitSSLCerName = "retrofit_ssl.cer"

    fun sslContext(assetsCerName: String = RetrofitSSLCerName): Pair<SSLContext, X509TrustManager> {
        val sslContext = SSLContext.getInstance("TLS")
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(getKeyStore(assetsCerName))
        val trustManagers = trustManagerFactory.trustManagers
        sslContext.init(null, trustManagers, SecureRandom())
        return sslContext to trustManagers[0] as X509TrustManager
    }


    private fun getKeyStore(assetsCerName: String): KeyStore {

        val iStream = AppContext.application.assets.open(assetsCerName)

        val certificateFactory = CertificateFactory.getInstance("X.509")
        val certificate = certificateFactory.generateCertificate(iStream)

        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null)
        keyStore.setCertificateEntry("ca", certificate)
        return keyStore
    }

}