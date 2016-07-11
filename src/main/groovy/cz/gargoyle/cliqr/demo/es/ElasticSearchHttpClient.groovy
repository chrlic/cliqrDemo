package cz.gargoyle.cliqr.demo.es

import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity

class ElasticSearchHttpClient {
	HttpClient httpClient
	String serverUri
	
	ElasticSearchHttpResult result
	
	private static String QUERY_METHOD = "/_search?search_type=query_then_fetch"
	
	def query(String index, String type, String query) {
		HttpPost httpPost = new HttpPost(serverUri + "/" + index + "/" + type + QUERY_METHOD)
		httpPost.setHeader("Content-Type", "application/json")
		StringEntity strEntity = new StringEntity(query)
		httpPost.setEntity(strEntity )
		result = new ElasticSearchHttpResult(httpClient.execute(httpPost))
		return result
	}
	
	def update(String index, String type, String id, int retryOnFailureCount, String updateDoc) {
		HttpPost httpPost = new HttpPost(serverUri + "/" + index + "/" + type + 
			"/" + id + "/_update?retry_on_conflict=" + retryOnFailureCount)
		httpPost.setHeader("Content-Type", "application/json")
		StringEntity strEntity = new StringEntity(updateDoc)
		httpPost.setEntity(strEntity )
		result = new ElasticSearchHttpResult(httpClient.execute(httpPost))
		return result
	}
}
