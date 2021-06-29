package bis.stock.back.domain.stock;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bis.stock.back.domain.stock.dto.Stock;

public interface StockRepository extends JpaRepository<Stock, Long>{
	Optional<Stock> findByName(String name); //아직 사용 못해봄,,

}
