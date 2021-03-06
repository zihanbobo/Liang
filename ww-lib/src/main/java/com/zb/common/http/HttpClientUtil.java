package com.zb.common.http;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HttpClientUtil {
	static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
	public static final Charset UTF8 = Charset.forName("UTF-8");
	public static final Charset GB18030 = Charset.forName("GB18030");
	static final int TIME_OUT = Integer.getInteger("http.timeout", 40000)
			.intValue();
	static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.4 (KHTML, like Gecko) Safari/537.4";
	static HttpClient HTTP_CLIENT = bulidHttpClient();

	static HttpClient bulidHttpClient() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(800);
		cm.setDefaultMaxPerRoute(200);

		// cm.setMaxPerRoute(new HttpRoute(new HttpHost("localhost")), 500);
		// cm.setMaxPerRoute(new HttpRoute(new HttpHost("127.0.0.1")), 500);
		// cm.setMaxPerRoute(new HttpRoute(new HttpHost("api.zhuang.bi")), 500);
		cm.setDefaultConnectionConfig(ConnectionConfig.custom()
				.setBufferSize(4096).setCharset(UTF8).build());

		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(TIME_OUT)
				.setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT)
				.setCookieSpec("ignoreCookies").build();

		return HttpClientBuilder.create().setConnectionManager(cm)
				.setDefaultRequestConfig(requestConfig)
				.setUserAgent(USER_AGENT)
				.addInterceptorFirst(new RequestAcceptEncoding())
				.addInterceptorFirst(new ResponseContentEncoding()).build();
	}

	public static String get(String url, Map<String, String> HEADERS)
			throws IOException {
		HttpGet get = new HttpGet(url);
		return execute(get, HEADERS, null);
	}

	public static String get(String url, Map<String, String> HEADERS,
			Charset forceCharset) throws IOException {
		HttpGet get = new HttpGet(url);
		return execute(get, HEADERS, forceCharset);
	}

	public static String post(String url, Map<String, String> params,
			Map<String, String> headers) throws IOException {
		HttpPost post = new HttpPost(url);
		if ((params != null) && (!params.isEmpty())) {
			List<NameValuePair> ps = new ArrayList<NameValuePair>(params.size());
			for (Map.Entry<String, String> kv : params.entrySet()) {
				ps.add(new BasicNameValuePair((String) kv.getKey(), (String) kv
						.getValue()));
			}
			post.setEntity(new UrlEncodedFormEntity(ps));
		}
		return execute(post, headers, null);
	}

	public static String postXml(String url, String xml) throws IOException,
			URISyntaxException {
		HttpPost post = new HttpPost(url);
		StringEntity myEntity = new StringEntity(xml, "utf8");
		post.addHeader("Content-Type", "text/xml");
		post.setEntity(myEntity);
		return execute(post, null, null);
	}

	public static <T> T http(HttpClient client, HttpRequestBase request,
			Map<String, String> headers, HttpEntityHandler<T> handler)
			throws IOException {
		if ((headers != null) && (!headers.isEmpty())) {
			for (Map.Entry<String, String> kv : headers.entrySet()) {
				request.addHeader((String) kv.getKey(), (String) kv.getValue());
			}
		}
		long begin = System.currentTimeMillis();
		try {
			return client.execute(request, handler, null);
		} catch (ConnectTimeoutException e) {
			log.error(" catch ConnectTimeoutException ,closeExpiredConnections &  closeIdleConnections for 30 s. ");
			client.getConnectionManager().closeExpiredConnections();
			client.getConnectionManager().closeIdleConnections(30L,
					TimeUnit.SECONDS);
			throw e;
		} finally {
			log.info(handler.getName() + "  {},cost {} ms", request.getURI(),
					Long.valueOf(System.currentTimeMillis() - begin));
		}
	}

	private static String execute(final HttpRequestBase request,
			Map<String, String> headers, final Charset forceCharset)
			throws IOException {

		return (String) http(HTTP_CLIENT, request, headers,
				new HttpEntityHandler<String>() {
					public String handle(HttpEntity entity) throws IOException {
						if (entity == null) {
							return null;
						}
						byte[] content = EntityUtils.toByteArray(entity);
						if (forceCharset != null) {
							return new String(content, forceCharset);
						}
						Charset charset = null;
						ContentType contentType = ContentType.get(entity);
						if (contentType != null) {
							charset = contentType.getCharset();
						}
						if (charset == null) {
							charset = HttpClientUtil.GB18030;
						}
						String html = new String(content, charset);
						charset = HttpClientUtil
								.checkMetaCharset(html, charset);
						if (charset != null) {
							html = new String(content, charset);
						}
						return html;
					}

					public String getName() {
						return request.getMethod();
					}
				});

		// return null;
	}

	private static Charset checkMetaCharset(String html, Charset use) {
		String magic = "charset=";
		int index = html.indexOf(magic);
		if ((index > 0) && (index < 1000)) {
			index += magic.length();
			int end = html.indexOf('"', index);
			if (end > index) {
				try {
					String charSetString = html.substring(index, end)
							.toLowerCase();
					if (charSetString.length() > 10) {
						return null;
					}
					if (charSetString.startsWith("gb")) {
						return GB18030.equals(use) ? null : GB18030;
					}
					Charset curr = Charset.forName(charSetString);
					if (!curr.equals(use)) {
						return curr;
					}
				} catch (Exception e) {
					log.error("Get MetaCharset error", e);
				}
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Cookie",
				"BDUSS=JTbUhoeWhST3V5TTVoMXlvZXcyeUUwNHI1eS1Xc3BvNnFnU340MjhlMTE3TDVSQVFBQUFBJCQAAAAAAAAAAApBLRAPsZEvAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACAYIArMAAAALD2RHMAAAAA6p5DAAAAAAAxMC4zOC4yOHWe0VB1ntFQN3");

		// get(
		// "http://imgzb.zhuangdianbi.com/573aa9a20cf2cc7298e5f561", map);
		String url = "http://imgzb.zhuangdianbi.com/573aa9a20cf2cc7298e5f561";
		HttpGet get = new HttpGet(url);
		HttpResponse resp = HTTP_CLIENT.execute(get);
		Header[] hs = resp.getAllHeaders();
		for (Header h : hs) {
			System.out.println(h.getName() + ":" + h.getValue());
		}
	}
}