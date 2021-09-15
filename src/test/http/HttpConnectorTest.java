package test.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kh.toy.common.exception.PageNotFoundException;
import com.kh.toy.common.http.HttpConnector;
import com.kh.toy.common.http.RequestParams;
import com.kh.toy.member.model.dto.User;

/**
 * Servlet implementation class HttpConnectorTest
 */
@WebServlet("/test/http/*")
public class HttpConnectorTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HttpConnectorTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] uriArr = request.getRequestURI().split("/");
		switch (uriArr[uriArr.length-1]) {
			case "test-kakao":
				  testKakaoAPI();
				break;
			case "test-naver":
				  testNaverAPI();
				break;
			default:throw new PageNotFoundException();
		}
	}
	
	private void testNaverAPI() {
		
		User user = User.builder()
				.userId("pclass")
				.password("12345")
				.tell("01011112222")
				.email("aaa@bbb.com") 
				.build();
		
		
		System.out.println(user);
		
		 Gson gson = new Gson();
		
		 Map<String,Object> body = new LinkedHashMap<String, Object>();
		 List<Map<String,Object>> keywordGroups = new ArrayList<Map<String,Object>>(); 
		 
		 Map<String,Object> keywordGroup = new LinkedHashMap<String, Object>();
		 List<String> keywords = new ArrayList<String>();
		 
		 keywords.add("스프링");
		 keywords.add("스프링부트");
		 
		 keywordGroup.put("groupName","자바");
		 keywordGroup.put("keywords",keywords);
		 
		 keywordGroups.add(keywordGroup);
		 
		 keywordGroup = new LinkedHashMap<String, Object>();
		 keywords = new ArrayList<String>();
		 keywords.add("DJango");
		 keywords.add("flutter");
		 
		 keywordGroup.put("groupName","파이썬");
		 keywordGroup.put("keywords",keywords);
		 
		 keywordGroups.add(keywordGroup);
		 
		 body.put("startDate", "2016-01-01");
		 body.put("endDate", "2021-01-01");
		 body.put("timeUnit", "month");
		 body.put("keywordGroups", keywordGroups);
		 
		 String[] ages = {"3","4","5","6"};
		 body.put("ages", ages);
		 
		 String jsonDatas = gson.toJson(body);
		 System.out.println(jsonDatas);
		 
		 HttpConnector conn = new HttpConnector();
		 Map<String,String> headers = new HashMap<String, String>();
		 headers.put("X-Naver-Client-Id", "O_PVY28f_ilEO4rr4QS6");
		 headers.put("X-Naver-Client-Secret", "jtxlGACDlg");
		 headers.put("Content-Type", "application/json; charset=utf-8");
		 
		 String responseBody = conn.post("https://openapi.naver.com/v1/datalab/search", headers, jsonDatas);
		 System.out.println(responseBody);
	}
	
	//@Test
	private void testKakaoAPI() {
		HttpConnector conn = new HttpConnector();
		String queryString = conn.urlEncodedForm(RequestParams.builder().param("query", "자바").build());
		
		String url = "https://dapi.kakao.com/v3/search/book?"+queryString;
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Authorization", "KakaoAK eaa7f0a4e6e4902213a99929a969c2f5");
		
		JsonObject datas = conn.getAsJson(url, headers).getAsJsonObject();
		
		for (JsonElement e : datas.getAsJsonArray("documents")) {
			String author = e.getAsJsonObject().get("authors").getAsString();
			String title = e.getAsJsonObject().get("title").getAsString();
			System.out.println("작가 : " + author);
			System.out.println("제목 : " + title);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
