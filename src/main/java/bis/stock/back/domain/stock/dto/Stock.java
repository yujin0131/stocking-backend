package bis.stock.back.domain.stock.dto;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import bis.stock.back.domain.user.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Stock {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column
	private String name;
	
	@Column(unique = true)
	private String code;
	
	@Column
	private String category;
	
	protected Stock() {	
	}
	
    @Builder
    public Stock(String name, String code, String category) {
    	this.name = name;
    	this.code = code;
    	this.category = category;
    }
}
