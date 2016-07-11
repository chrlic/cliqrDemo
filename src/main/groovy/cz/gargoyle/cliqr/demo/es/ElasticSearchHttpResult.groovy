package cz.gargoyle.cliqr.demo.es

import org.apache.http.HttpResponse
import org.apache.http.client.ResponseHandler
import org.apache.http.impl.client.BasicResponseHandler

class ElasticSearchHttpResult  {
	HttpResponse response
	String body
	String errorMessage
	String errorBody
	
	public ElasticSearchHttpResult(HttpResponse httpResponse) {
		response = httpResponse
		if (response.getStatusLine().getStatusCode() != 200) {
			BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		
			String result = ""
			String line = ""
			while ((line = rd.readLine()) != null) {
				result += line + "\n"
			}
			errorBody = result.toString()
		} else {
			ResponseHandler<String> handler = new BasicResponseHandler()
			body = handler.handleResponse(response)
		}
	}
	
	def getResponseBody() {
		return body
	}
	
	def getResponseCode() {
		return response.getStatusLine().getStatusCode()
	}
	
	def getResponseMessage() {
		return response.getStatusLine().getReasonPhrase()
	}
	
	def getResponseErrorBody() {
		return errorBody
	}
}
