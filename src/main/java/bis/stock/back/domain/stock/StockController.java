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
import bis.stock.back.domain.stock.dto.Stock;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@RestController
@RequestMapping("/stock")
public class StockController {

	@Autowired
	StockService stockService;

	// stock/detail?itemname= 하면 나온다.
	@RequestMapping(value="/detail")
	public String stock(@RequestParam("itemname") String itemname, RedirectAttributes redirect) {
		
		String itemcode = stockService.findcode(itemname);
		
		//주식코드가 6자리 앞에 0 붙여서 정보를 안줘서 직접 붙였다.
		itemcode = String.format("%06d", Integer.parseInt(itemcode)).toString();

		return stockService.stock(itemcode, itemname);
	}



}

