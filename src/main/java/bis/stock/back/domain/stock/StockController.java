package bis.stock.back.domain.stock;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/stock")
@RestController
public class StockController {
	@RequestMapping("/")
	public String home(){

		return "된다";

	}
}
