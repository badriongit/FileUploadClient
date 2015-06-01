import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;


public class ImageUpload {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub

		File file = new File("/Users/badrionapple/Documents/securedownload.jpeg");
		//sendImage();
		
		sendImgFileWithParam(file);
		
	}
	
	private static void sendImgFileWithParam(File file) throws ClientProtocolException, IOException {
		 HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost("http://localhost:8080/uploadImg");
		httpost.addHeader("Authorization", "Basic YWE6YmI=");
		
		ObjectMapper objectMapper = new ObjectMapper(); 
		TypeFactory typeFactory = objectMapper.getTypeFactory();
		
		File jsonReq = new File("/Users/badrionapple/Desktop");
		byte[] jsonData = Files.readAllBytes(Paths.get("/Users/badrionapple/Documents/workspace-sts-3.6.3.SR1/UploadClient/src/req.json"));
		// JsonParser parser = 
		//String jsonString = "";
		
		List<ImageBean> someClassList =
				objectMapper.readValue(jsonData, typeFactory.constructCollectionType(List.class, ImageBean.class));
		
		for (ImageBean i : someClassList){
			String id = i.getEntityId();
			String type = i.getEntityType();
			String path = i.getPath();
			System.out.println("Id : " + id + ", Type : " + type + ", Path : " + path);
		}
		
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("entityType", new StringBody("STRING_VALUE"));
		entity.addPart("myImageFile", new FileBody(file));
		//entity.addPart("myAudioFile", new FileBody(audioFile));
		httpost.setEntity(entity);
		HttpResponse response = httpclient.execute(httpost);
		 StatusLine statusLine = response.getStatusLine();
		 HttpEntity resEntity = response.getEntity();
	        if (statusLine.getStatusCode() >= 300) {
	            throw new HttpResponseException(
	                    statusLine.getStatusCode(),
	                    statusLine.getReasonPhrase());
	        }
	        if (resEntity == null) {
	            throw new ClientProtocolException("Response contains no content");
	        }
	        System.out.println(resEntity.getContent());
	}

	public static void sendFile(File uploadFile1) {
        String charset = "UTF-8";
        String requestURL = "http://localhost:8080/uploadImg";
 
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
             
            multipart.addHeaderField("User-Agent", "Badri - File Upload");
            multipart.addHeaderField("Test-Header", "Header-Value");
            multipart.addHeaderField("Authorization", "Basic YWE6YmI="); 
            multipart.addFilePart("fileUpload", uploadFile1);
 
            List<String> response = multipart.finish();
             
            System.out.println("SERVER REPLIED:");
             
            for (String line : response) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
	
	public static void sendImage() throws ClientProtocolException, IOException{
		HttpClient httpclient = new DefaultHttpClient();
	    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

	    HttpPost httppost = new HttpPost("http://localhost:8080/uploadImg");
	    File file = new File("/Users/badrionapple/Documents/securedownload.jpeg");

	    MultipartEntity mpEntity = new MultipartEntity();
	    ContentBody cbFile = new FileBody(file, "image/jpeg");
	    mpEntity.addPart("userfile", cbFile);


	    httppost.setEntity(mpEntity);
	    System.out.println("executing request " + httppost.getRequestLine());
	    HttpResponse response = httpclient.execute(httppost);
	    HttpEntity resEntity = response.getEntity();

	    System.out.println(response.getStatusLine());
	    if (resEntity != null) {
	      System.out.println(EntityUtils.toString(resEntity));
	    }
	    if (resEntity != null) {
	      resEntity.consumeContent();
	    }

	    httpclient.getConnectionManager().shutdown();
	}

}
