package bis.stock.back.domain.stock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.ObjectMapper;

import bis.stock.back.domain.auth.AuthService;
import bis.stock.back.domain.auth.dto.JoinDto;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@RestController
@RequestMapping("/stock")
public class StockController {

	@Autowired
	StockService stockService;

	//db 거치는거에서 오류가 나서 그부분은 아직 머지 안했어요 그냥 stock/code 하면 임시로 삼성전자 코드 넣어서 그렇게만 되게 해뒀습니다.
	//이부분은 나중에 detail로 갈 때 주식 코드 보내주는 거로 나중에 필요한곳에 옮기면된다,,,
	@RequestMapping(value="/code") //보려면 일단 /code로 들어가면 됩니다
	public RedirectView login(RedirectAttributes redirect) {

		//주식 코드 db 만들면 받아오는거로 변경 예정 일단 아직은 임의 코드
		String code = "005930";
		//String name = "삼성전자";
		//System.out.println("여긴");
		//code = stockService.findcode(name);
		//System.out.println("오나" + code);
		//code = "005930";
		redirect.addAttribute("code", code);
		return new RedirectView("/stock/detail");
	}

	//일단 아직 수정예정,,
	@RequestMapping(value = "/detail")
	@ResponseBody
	public String stock(@RequestParam("code") String itemcode){


		return stockService.stock(itemcode);
	}



}

