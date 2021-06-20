package bis.stock.back.domain.stock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.json.JsonParseException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.ObjectMapper;




@RestController
public class StockController {

	@RequestMapping(value="/")
	public RedirectView login(RedirectAttributes redirect) {
		String code = "005930";

		redirect.addAttribute("code", code);
		return new RedirectView("/stock");
	}
	//private final StockService stockservice;

	@RequestMapping(value = "/stock")
	@ResponseBody
	public String home(@RequestParam("code") String code){
		String line ="";
		String result = "";
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			String urlstr = "https://api.finance.naver.com/service/itemSummary.nhn?itemcode=" + code;
			URL url = new URL(urlstr);

			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(url.openStream()));
			while((line = br.readLine())!=null) {
				result = result.concat(line);
			}
			
			
		}catch (Exception e) {

		}





		return result.toString();


	}
}

