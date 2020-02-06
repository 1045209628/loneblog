package com.xz.blog.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

@Configuration
public class OkHttpConfig {

	// TODO 配置ssl

	@Bean
	public OkHttpClient okHttpClient() {
		return new OkHttpClient.Builder()
				.retryOnConnectionFailure(false)
				.readTimeout(30, TimeUnit.SECONDS)
				.writeTimeout(30, TimeUnit.SECONDS)
				.connectTimeout(30, TimeUnit.SECONDS)
				.connectionPool(pool())
				.sslSocketFactory(sslSocketFactory(), x509TrustManager())
				.hostnameVerifier((hostname, session) -> true)
				.build();
	}

	@Bean
	public X509TrustManager x509TrustManager() {
		return new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		};
	}

	@Bean
	public SSLSocketFactory sslSocketFactory() {
		try {
			// 信任任何链接
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { x509TrustManager() }, new SecureRandom());
			return sslContext.getSocketFactory();
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Bean
	public ConnectionPool pool() {
		return new ConnectionPool(200, 300, TimeUnit.SECONDS);
	}

}
