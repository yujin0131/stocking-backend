package bis.stock.back.domain.stock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import bis.stock.back.domain.stock.dto.Stock;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StockService { 

	private StockRepository stockRepository;

	@PersistenceContext
	private EntityManager em;
	
	public List<Stock> totalList() {

		return em.createQuery("select s from Stock s", Stock.class)
				.getResultList();
	}

	public String findcode(String itemname) {

		return em.createQuery("select s from Stock s where s.name = :name", Stock.class)
				.setParameter("name", itemname)
				.getSingleResult().getCode();
	}

	public String detail(String itemcode, String itemname) {

		String line ="";
		String result = "";
		ObjectMapper objectMapper = new ObjectMapper();
		JSONObject res = new JSONObject();

		try {
			String urlstr = "https://api.finance.naver.com/service/itemSummary.nhn?itemcode=" + itemcode;
			URL url = new URL(urlstr);

			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(url.openStream()));
			while((line = br.readLine())!=null) {
				result = result.concat(line);
			}
			
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(result);
			System.out.println(obj.toString());
			String amount = obj.get("amount").toString();//거래량
			String high = obj.get("high").toString(); //고점
			String rate = obj.get("rate").toString(); //등락비율
			String low = obj.get("low").toString(); //저점
			String now = obj.get("now").toString(); //현재가
			String diff = obj.get("diff").toString();//등락폭

//			String pbr = obj.get("pbr").toString();//주가순자산비율
//			String risefall = obj.get("risefall").toString();
//			String marketSum = obj.get("marketSum").toString();
//			String eps = obj.get("eps").toString();//주당 순 이익
//			String per = obj.get("per").toString();//주가 수익 비율
//			String quant = obj.get("quant").toString();//거래량

			res.put("itemcode", itemcode);
			res.put("itemname", itemname);
			res.put("now", now);
			res.put("diff", diff);
			res.put("high", high);
			res.put("low", low);
			res.put("rate", rate);
			res.put("amount", amount);

			br.close();
		}catch (Exception e) {

		}

		return res.toJSONString();

	}


}
