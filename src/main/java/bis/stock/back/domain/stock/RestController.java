package bis.stock.back.domain.stock;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestController {
	@RequestMapping("/")
	@ResponseBody
	public String home(){

		return "된다";

	}
}
