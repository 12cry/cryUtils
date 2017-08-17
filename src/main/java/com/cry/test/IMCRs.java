package com.cry.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class IMCRs {
	public static void main(String[] args) throws Exception {
		IMCRs t = new IMCRs();
//		t.f1();
		t.f2();
	}

	public void f1() throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getCredentialsProvider().setCredentials(new AuthScope("172.16.3.249", 8080, "iMC RESTful Web Services"), new UsernamePasswordCredentials("admin", "111111"));
//		http://172.16.3.249:8080/imcrs/plat/res/device
//		String url ="http://172.16.3.249:8080/imcrs/plat/res/device/17/synchronize";
		String url ="http://172.16.3.249:8080/imcrs/netasset/asset/detail";
//		String url ="http://172.16.3.249:8080/imcrs/fault/alarm";
		HttpGet get = new HttpGet(url);
		get.addHeader("accept", "application/xml");
		HttpResponse response = client.execute(get);
		System.out.println(response.getStatusLine());
		System.out.println(EntityUtils.toString(response.getEntity()));
	}

	public void f2() throws Exception {

//		String url ="http://172.16.3.249:8080/imcrs/plat/res/device/17";
//		"http://172.16.3.249:8080/imcrs/fault/alarm?size=100"
		
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope("172.16.3.249", 8080, "iMC RESTful Web Services"), new UsernamePasswordCredentials("admin", "111111"));
		HttpClientContext context = HttpClientContext.create();
	    context.setCredentialsProvider(credsProvider);
	    
		CloseableHttpClient httpclient = HttpClients.createDefault();
//		String url ="http://172.16.3.249:8080/imcrs/fault/alarm";
		String url ="http://172.16.3.249:8080/imcrs/netasset/asset/detail";
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response1 = httpclient.execute(httpGet,context);
	    
		try {
			System.out.println(response1.getStatusLine());
			HttpEntity entity1 = response1.getEntity();
			System.out.println(EntityUtils.toString(response1.getEntity()));
			EntityUtils.consume(entity1);
		} finally {
			response1.close();
		}
	}
}