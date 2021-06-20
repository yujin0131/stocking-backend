package bis.stock.back.domain.stock.dto;

import lombok.Getter;

@Getter
public class StockDto {
	private long marketSum;
	private int now;
	private int diff;
	private float rate;
	private long quant;
	private long amount;
	private int high;
	private int low;
}
